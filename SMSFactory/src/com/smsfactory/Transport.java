package com.smsfactory;

import javax.mail.Message;
import javax.mail.MessagingException;

public class Transport {
	
	public void send(Message message) throws MessagingException {
		javax.mail.Transport.send(message);
	}

}
