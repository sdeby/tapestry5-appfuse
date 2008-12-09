package org.appfuse.webapp.pages;

import javax.servlet.http.Cookie;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;
import org.appfuse.webapp.base.BasePage;
import org.springframework.security.ui.rememberme.TokenBasedRememberMeServices;

/**
 * Logout Page
 * 
 * @author Serge Eby
 * @version $Id$
 * 
 */
public class Logout extends BasePage {

	@Inject
	private Request request;

	@Inject
	private RequestGlobals requestGlobals;

	@Inject
	private Cookies cookies;

	Object onActivate() {

		// Clear session
		Session session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			getLogger().debug("Session successfully invalidated!");
		}

		// Remove RememberMe cookie
		// This doesn't work because T5 adds an extra "/" to the path
		//cookies.removeCookieValue(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);

		// Workaround
		clearCookie();

		return MainMenu.class;
	}

	private void clearCookie() {
		Cookie cookie = new Cookie(
				TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY,
				null);
		String contextPath = request.getContextPath();
		cookie.setPath(contextPath != null && 
				       contextPath.length() > 0 ? contextPath : "/");
		cookie.setMaxAge(0);
		cookie.setSecure(request.isSecure());
		requestGlobals.getHTTPServletResponse().addCookie(cookie);
	}
}
