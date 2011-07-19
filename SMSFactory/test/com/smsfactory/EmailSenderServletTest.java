package com.smsfactory;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;

public class EmailSenderServletTest {

	@Test
	public void sender_name_should_make_clear_responses_are_ignored()
			throws Exception {
		Message message = new EmailSenderServlet()
				.createMessage(new MockHttpServletRequest());
		InternetAddress from = (InternetAddress) message.getFrom()[0];

		assertThat(from.getPersonal(), is("no-reply"));
	}

	@Test
	public void sender_email_should_be_the_account_name_on_googleappengine()
			throws Exception {
		Message message = new EmailSenderServlet()
				.createMessage(new MockHttpServletRequest());
		InternetAddress from = (InternetAddress) message.getFrom()[0];

		assertThat(from.getAddress(), is("ericlef@gmail.com"));
	}

	@SuppressWarnings("rawtypes")
	private class MockHttpServletRequest implements HttpServletRequest {
		@Override
		public void setCharacterEncoding(String arg0)
				throws UnsupportedEncodingException {
		}

		@Override
		public void setAttribute(String arg0, Object arg1) {
		}

		@Override
		public void removeAttribute(String arg0) {
		}

		@Override
		public boolean isSecure() {
			return false;
		}

		@Override
		public int getServerPort() {
			return 0;
		}

		@Override
		public String getServerName() {
			return null;
		}

		@Override
		public String getScheme() {
			return null;
		}

		@Override
		public RequestDispatcher getRequestDispatcher(String arg0) {
			return null;
		}

		@Override
		public int getRemotePort() {
			return 0;
		}

		@Override
		public String getRemoteHost() {
			return null;
		}

		@Override
		public String getRemoteAddr() {
			return null;
		}

		@Override
		public String getRealPath(String arg0) {
			return null;
		}

		@Override
		public BufferedReader getReader() throws IOException {
			return null;
		}

		@Override
		public String getProtocol() {
			return null;
		}

		@Override
		public String[] getParameterValues(String arg0) {
			return null;
		}

		@Override
		public Enumeration getParameterNames() {
			return null;
		}

		@Override
		public Map getParameterMap() {
			return null;
		}

		@Override
		public String getParameter(String arg0) {
			return null;
		}

		@Override
		public Enumeration getLocales() {
			return null;
		}

		@Override
		public Locale getLocale() {
			return null;
		}

		@Override
		public int getLocalPort() {
			return 0;
		}

		@Override
		public String getLocalName() {
			return null;
		}

		@Override
		public String getLocalAddr() {
			return null;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return new DelegatingServletInputStream(new ByteArrayInputStream(
					new byte[] {}));
		}

		@Override
		public String getContentType() {
			return null;
		}

		@Override
		public int getContentLength() {
			return 0;
		}

		@Override
		public String getCharacterEncoding() {
			return null;
		}

		@Override
		public Enumeration getAttributeNames() {
			return null;
		}

		@Override
		public Object getAttribute(String arg0) {
			return null;
		}

		@Override
		public boolean isUserInRole(String arg0) {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdValid() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromUrl() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromURL() {
			return false;
		}

		@Override
		public boolean isRequestedSessionIdFromCookie() {
			return false;
		}

		@Override
		public Principal getUserPrincipal() {
			return null;
		}

		@Override
		public HttpSession getSession(boolean arg0) {
			return null;
		}

		@Override
		public HttpSession getSession() {
			return null;
		}

		@Override
		public String getServletPath() {
			return null;
		}

		@Override
		public String getRequestedSessionId() {
			return null;
		}

		@Override
		public StringBuffer getRequestURL() {
			return null;
		}

		@Override
		public String getRequestURI() {
			return null;
		}

		@Override
		public String getRemoteUser() {
			return null;
		}

		@Override
		public String getQueryString() {
			return null;
		}

		@Override
		public String getPathTranslated() {
			return null;
		}

		@Override
		public String getPathInfo() {
			return null;
		}

		@Override
		public String getMethod() {
			return null;
		}

		@Override
		public int getIntHeader(String arg0) {
			return 0;
		}

		@Override
		public Enumeration getHeaders(String arg0) {
			return null;
		}

		@Override
		public Enumeration getHeaderNames() {
			return null;
		}

		@Override
		public String getHeader(String arg0) {
			return null;
		}

		@Override
		public long getDateHeader(String arg0) {
			return 0;
		}

		@Override
		public Cookie[] getCookies() {
			return null;
		}

		@Override
		public String getContextPath() {
			return null;
		}

		@Override
		public String getAuthType() {
			return null;
		}
	}

	public class DelegatingServletInputStream extends ServletInputStream {
		private final InputStream sourceStream;

		public DelegatingServletInputStream(InputStream sourceStream) {
			this.sourceStream = sourceStream;
		}

		public final InputStream getSourceStream() {
			return this.sourceStream;
		}

		public int read() throws IOException {
			return this.sourceStream.read();
		}

		public void close() throws IOException {
			super.close();
			this.sourceStream.close();
		}

	}
}
