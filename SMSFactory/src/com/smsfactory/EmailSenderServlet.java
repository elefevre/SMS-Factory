package com.smsfactory;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.repackaged.com.google.common.base.Join;
import com.google.appengine.repackaged.com.google.common.base.Throwables;

@SuppressWarnings("serial")
public class EmailSenderServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try {
			Transport.send(createMessage(req));
		} catch (Exception e) {
			e.printStackTrace();
			resp.setContentType("text/plain");
			e.printStackTrace(resp.getWriter());
			Throwables.propagate(e);

		}
	}

	public Message createMessage(HttpServletRequest req)
			throws MessagingException, IOException {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		MimeMessage messageReceived = new MimeMessage(session,
				req.getInputStream());

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("ericlef@gmail.com", "no-reply"));

		msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
				findRecipientEmail(messageReceived), "Eric chez Algodeal"));
		msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(
				"eric@smsfactory.fr", "Eric"));
		msg.setSubject("Test de l'envoi de mail par GAE");

		String msgBody = "";
		// msgBody += addHeaderLines(messageReceived);
		// msgBody += addHeaders(messageReceived);
		// msgBody += addAddresses(messageReceived.getAllRecipients(),
		// "Recipient");
		// msgBody += addAddresses(messageReceived.getFrom(), "From");
		// msgBody += addAddresses(messageReceived.getReplyTo(), "Reply to");
		// msgBody += addAddresses(new Address[] { messageReceived.getSender()
		// },
		// "Sender");
		// msgBody += addAddresses(
		// messageReceived.getRecipients(RecipientType.TO), "TO Recipient");
		// msgBody += addAddresses(
		// messageReceived.getRecipients(RecipientType.CC), "CC Recipient");
		// msgBody += addAddresses(
		// messageReceived.getRecipients(RecipientType.BCC),
		// "BCC Recipient");
		// msgBody += addContent(messageReceived);
		// msgBody += addString("Description",
		// messageReceived.getDescription());
		// msgBody += addString("Disposition: ",
		// messageReceived.getDisposition());
		// msgBody += addString("Encoding: ", messageReceived.getEncoding());
		// msgBody += addString("FileName: ", messageReceived.getFileName());
		// msgBody += addString("LineCount: ", "" +
		// messageReceived.getLineCount());
		// msgBody += addString("MessageID", messageReceived.getMessageID());
		// msgBody += addString("MessageNumber",
		// "" + messageReceived.getMessageNumber());
		// msgBody += addString("Size", "" + messageReceived.getSize());
		// msgBody += addString("Subject", "" + messageReceived.getSubject());

		ServletInputStream inputStream = req.getInputStream();
		int i;
		while ((i = inputStream.read()) != -1) {
			msgBody += new String(new byte[] { (byte) i });
		}
		msg.setText(msgBody);

		return msg;
	}

	private String findRecipientEmail(MimeMessage messageReceived)
			throws MessagingException, IOException {
		String emailContent = (String) messageReceived.getContent();
		String[] lines = emailContent.split("\n");
		String afterEmailPreambule = "";
		boolean startCopying = false;
		for (String line : lines) {
			if (startCopying) {
				if (!line.startsWith("You received a text message from "))
					afterEmailPreambule += line + "\n";
			} else {
				if (line.startsWith("Date: ")) {
					startCopying = true;
				}
			}
		}
		afterEmailPreambule = afterEmailPreambule.trim();
		String recipient = afterEmailPreambule.split(" ")[0];
		return recipient;
	}

	private String addString(String name, String value) {
		return name + ": " + value + "\n";
	}

	private String addContent(MimeMessage message) throws MessagingException,
			IOException {
		String msg = "";
		msg += "ContentID: " + message.getContentID() + "\n";
		msg += "ContentMD5: " + message.getContentMD5() + "\n";
		msg += "ContentType: " + message.getContentType() + "\n";
		if (message.getContentLanguage() == null) {
			msg += addString("ContentLanguages", "none");
		} else {
			msg += "ContentLanguages: "
					+ Join.join(" -- ", message.getContentLanguage()) + "\n";
		}
		if (message.getContent() instanceof MimeMultipart)
			msg += addContentAsMimeMultipart((MimeMultipart) message
					.getContent());
		if (message.getContent() instanceof String)
			msg += addString("Content (String)", (String) message.getContent());
		return msg;
	}

	private String addContentAsMimeMultipart(MimeMultipart content)
			throws MessagingException, IOException {
		String contentMsg = "Content: " + content + "\n";
		for (int i = 0; i < content.getCount(); i++) {
			contentMsg += "BodyPart " + i + ": "
					+ content.getBodyPart(i).getContent();
		}
		return contentMsg;
	}

	private String addAddresses(Address[] addresses, String name) {
		if (addresses == null)
			return addString(name, "none");
		String msg = "";
		for (Address address : addresses) {
			if (address == null)
				continue;
			msg += name + " 'type': " + address.getType() + ", toString: "
					+ address + "\n";
		}
		msg += "\n";
		return msg;
	}

	private String addHeaderLines(MimeMessage message)
			throws MessagingException {
		String msg = "";
		@SuppressWarnings("unchecked")
		Enumeration<String> headerLines = message.getAllHeaderLines();
		while (headerLines.hasMoreElements()) {
			String name = headerLines.nextElement();
			if (message.getHeader(name) == null)
				continue;
			msg += "Header Line '" + name + "'" + ": "
					+ Join.join(" -- ", message.getHeader(name)) + "\n";
		}
		msg += "\n";
		return msg;
	}

	private String addHeaders(MimeMessage message) throws MessagingException {
		String msg = "";
		Enumeration<?> headers = message.getAllHeaders();
		while (headers.hasMoreElements()) {
			Object header = headers.nextElement();
			msg += "Header '" + header + "'\n";
		}
		msg += "\n";
		return msg;
	}

}
