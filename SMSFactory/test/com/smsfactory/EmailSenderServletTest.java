package com.smsfactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

import org.junit.Before;
import org.junit.Test;

public class EmailSenderServletTest {
	private EmailSenderServlet servlet;
	private MockTransport mockTransport;

	@Before
	public void setup() {
		mockTransport = new MockTransport();
		servlet = new EmailSenderServlet(mockTransport);
	}

	@Test
	public void sender_name_should_make_clear_responses_are_ignored()
			throws Exception {
		servlet.doPost(new MockHttpServletRequest(), null);

		assertThat(mockTransport.getFrom().getPersonal(), is("no-reply"));
	}

	@Test
	public void sender_email_should_be_the_account_name_on_googleappengine()
			throws Exception {
		servlet.doPost(new MockHttpServletRequest(), null);

		assertThat(mockTransport.getFrom().getAddress(),
				is("robot@smsfactory.fr"));
	}

	@Test
	public void sends_an_email_to_the_address_at_the_beginning_of_the_content()
			throws Exception {
		servlet.doPost(new MockHttpServletRequest(
				emailWithSmsContent("user@site.com hello world")), null);

		assertThat(mockTransport.getTo().getAddress(), is("user@site.com"));
	}

	@Test
	public void sends_the_text_that_follows_the_email_address()
			throws Exception {
		servlet.doPost(new MockHttpServletRequest(
				emailWithSmsContent("user@site.com hello world")), null);

		assertThat((String) mockTransport.getContent(),
				containsString("hello world"));
	}

	@Test
	public void uses_a_title() throws Exception {
		servlet.doPost(new MockHttpServletRequest(email()), null);

		assertThat((String) mockTransport.getMessage().getSubject(),
				is("Message envoye par SMS"));
	}

	@Test
	public void adds_a_signature() throws Exception {
		servlet.doPost(new MockHttpServletRequest(email()), null);

		assertThat(
				mockTransport.getContent(),
				containsString("\n\nhttp://smsfactory.fr/ l'envoi d'email par SMS"));
	}

	@Test
	public void adds_an_explanation() throws Exception {
		servlet.doPost(
				new MockHttpServletRequest(emailContent(
						"user@site.com hello world", "+555555555")), null);

		assertThat(
				mockTransport.getContent(),
				containsString("\n----\nCe message a �t� envoy� � partir du num�ro de t�l�phone +555555555 par SMS � nos services � l'intention de user@site.com. Merci de ne pas r�pondre � cet email."));
	}

	private static String email() {
		return emailContent(null, null);
	}

	private static String emailWithSmsContent(String smsContent) {
		return emailContent(smsContent, null);
	}

	private static String emailContent(String smsContent, String phoneNumber) {
		return "Received: by 10.52.106.201 with SMTP id gw9mr961116vdb.45.1311092253529;\n"
				+ //
				"       Tue, 19 Jul 2011 09:17:33 -0700 (PDT)\n"
				+ //
				"Return-Path: <txtforward@txtforward.com>\n"
				+ //
				"Received: from domU-12-31-39-04-60-11.txtforward.com (txtforward.com [174.129.217.87])\n"
				+ //
				"       by gmr-mx.google.com with ESMTP id v20si4373245vdu.2.2011.07.19.09.17.33;\n"
				+ //
				"       Tue, 19 Jul 2011 09:17:33 -0700 (PDT)\n"
				+ //
				"Received-SPF: pass (google.com: best guess record for domain of txtforward@txtforward.com designates 174.129.217.87 as permitted sender) client-ip=174.129.217.87;\n"
				+ //
				"Authentication-Results: gmr-mx.google.com; spf=pass (google.com: best guess record for domain of txtforward@txtforward.com designates 174.129.217.87 as permitted sender) smtp.mail=txtforward@txtforward.com\n"
				+ //
				"Received: by domU-12-31-39-04-60-11.txtforward.com (Postfix, from userid 48)\n"
				+ //
				"       id 4FED2A6E69; Tue, 19 Jul 2011 12:17:33 -0400 (EDT)\n"
				+ //
				"To: sms@smsfactory-test.appspotmail.com\n"
				+ //
				"\n"
				+ //
				"Subject: Text : Eric Lefevre-Ardant (+33613828286)\n"
				+ //
				"From: Eric Lefevre-Ardant (+555555555) - txtForward <noreply@txtForward.com>\n"
				+ //
				"Reply-To: noreply@txtForward.com\n"
				+ //
				"MIME-Version: 1.0\n"
				+ //
				"Content-type: text/plain; charset=utf-8\n"
				+ //
				"Content-Transfer-Encoding: quoted-printable\n"
				+ //
				"Message-Id: <20110719161733.4FED2A6E69@domU-12-31-39-04-60-11.txtforward.com>\n"
				+ //
				"Date: Tue, 19 Jul 2011 12:17:33 -0400 (EDT)\n"
				+ //
				"\n"
				+ //
				"\n"
				+ //
				"\n"
				+ //
				"You received a text message from Eric Lefevre-Ardant ("
				+ phoneNumber + ")\n" + //
				"\n" + smsContent;
	}

}
