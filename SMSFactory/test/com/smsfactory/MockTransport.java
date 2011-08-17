package com.smsfactory;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

public class MockTransport extends Transport {
	private Message message;

	@Override
	public void send(Message message) throws MessagingException {
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	public InternetAddress getFrom() throws MessagingException {
		return (InternetAddress) message.getFrom()[0];
	}

	public InternetAddress getTo() throws MessagingException {
		return (InternetAddress) message.getRecipients(RecipientType.TO)[0];
	}
	
	public String getContent() throws IOException, MessagingException {
		return (String) getMessage().getContent();
	}
}
