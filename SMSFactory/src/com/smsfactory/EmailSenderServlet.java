package com.smsfactory;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.common.base.Throwables;

@SuppressWarnings("serial")
public class EmailSenderServlet extends HttpServlet {
	private final Transport transport;

	public EmailSenderServlet() {
		this(new Transport());
	}

	public EmailSenderServlet(Transport transport) {
		this.transport = transport;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			if (!findSmsContent(req).contains("@")) {
				return;
			}
			transport.send(createMessage(req));
		} catch (Exception e) {
			e.printStackTrace();
			resp.setContentType("text/plain");
			e.printStackTrace(resp.getWriter());
			Throwables.propagate(e);
		}
	}

	private String findSmsContent(HttpServletRequest req)
			throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage messageReceived = new MimeMessage(session,
				req.getInputStream());
		String findSmsContent = findSmsContent(messageReceived);
		return findSmsContent;
	}

	private Message createMessage(HttpServletRequest req)
			throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage messageReceived = new MimeMessage(session,
				req.getInputStream());

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("robot@smsfactory.fr", "no-reply"));

		String recipientEmail = findRecipientEmail(messageReceived);
		if (recipientEmail.contains("@")) {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					recipientEmail, ""));
		} else {
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"contact@ericlefevre.net", ""));
		}
		String smsContent = findSmsContent(messageReceived);
		String emailBody = smsContent.isEmpty() ? (String) messageReceived
				.getContent() : smsContent;
		emailBody += "\n----\nCe message a �t� envoy� � partir du num�ro de t�l�phone "
				+ findPhoneNumber(messageReceived)
				+ " par SMS � nos services � l'intention de "
				+ recipientEmail
				+ ". Merci de ne pas r�pondre � cet email.";
		emailBody += "\n\nhttp://smsfactory.fr/ l'envoi d'email par SMS";
		msg.setText(emailBody);
		msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(
				"eric@smsfactory.fr", "Eric"));

		msg.setSubject("Message envoye par SMS");
		return msg;
	}

	private String findRecipientEmail(MimeMessage messageReceived)
			throws MessagingException, IOException {
		return findSmsContent(messageReceived).split(" ")[0];
	}

	private String findSmsContent(MimeMessage messageReceived)
			throws MessagingException, IOException {
		String emailContent = (String) messageReceived.getContent();
		String afterEmailPreambule = "";
		boolean startCopying = false;
		for (String line : emailContent.split("\n")) {
			if (startCopying) {
				afterEmailPreambule += line + "\n";
			} else {
				if (isLineAddedByTxtForward(line)) {
					startCopying = true;
				}
			}
		}
		afterEmailPreambule = afterEmailPreambule.trim();
		return afterEmailPreambule;
	}

	private String findPhoneNumber(MimeMessage messageReceived)
			throws MessagingException, IOException {
		String emailContent = (String) messageReceived.getContent();
		for (String line : emailContent.split("\n")) {
			if (isLineAddedByTxtForward(line)) {
				return line.split("[()]")[1];
			}
		}

		return "<Num�ro de t�l�phone inconnu>";
	}

	private static boolean isLineAddedByTxtForward(String line) {
		return line.startsWith("You received a text message from ");
	}

}
