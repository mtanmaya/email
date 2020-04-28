/**
 * 
 */
package tan.demo.spring.integration.email.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mail.MailSendingMessageHandler;
import org.springframework.mail.MailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author tmalik
 *
 */

@Configuration
public class MailConfig {

	@Value("${smtp.host}")
	private String smptHost;

	@Value("${smtp.port}")
	private Integer smtpPort;

	@Bean
	public JavaMailSenderImpl mailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(smptHost);
		mailSender.setPort(smtpPort);

		return mailSender;
	}

	@Bean
	public MessageChannel smtpChannel() {
		return new DirectChannel();
	}

	@ServiceActivator(inputChannel = "smtpChannel", outputChannel = "nullChannel")
	public MessageHandler mailsSenderMessagingHandler(Message<MailMessage> message) {

		MailSendingMessageHandler mailSendingMessageHandler = new MailSendingMessageHandler(mailSender());
		mailSendingMessageHandler.handleMessage(message);

		return mailSendingMessageHandler;
	}

}
