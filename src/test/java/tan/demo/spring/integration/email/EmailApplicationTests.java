package tan.demo.spring.integration.email;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.dumbster.smtp.SimpleSmtpServer;
import com.dumbster.smtp.SmtpMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class EmailApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	MessageChannel smtpChannel;

	@Test
	public void mailsend() throws IOException {

		SimpleSmtpServer mailServer = SimpleSmtpServer.start(12345);
		System.out.println("mail port... " + mailServer.getPort());

		smtpChannel.send(new GenericMessage<>(buildMailMessage("Test 1", "content 1")));
		smtpChannel.send(new GenericMessage<>(buildMailMessage("Test 2", "content 2")));

		List<SmtpMessage> messagesSent = mailServer.getReceivedEmails();

		mailServer.stop();

		assertEquals(2, messagesSent.size());
	}

	@Test
	public void sendMailServerNotAnswerting() throws Exception {

		smtpChannel.send(new GenericMessage<>(buildMailMessage("Test 1", "content 1")));

	}

	private MailMessage buildMailMessage(String subject, String content) {

		MailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo("mtanmaya.malik@gmail.com");
		mailMessage.setFrom("tanmayamallik@gmail.com");
		mailMessage.setText(content);
		mailMessage.setSubject(subject);

		return mailMessage;
	}

}
