package org.appfuse.webapp.pages;

import org.springframework.security.ui.rememberme.TokenBasedRememberMeServices;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Session;

/**
 * Logout Page
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class Logout {
	
	@Inject
	private Request request;
	

	@Inject
	private Cookies cookies;
	
	Object onActivate() {
		
		// Clear session
		Session session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		
		// Remove RememberMe cookie
		cookies.removeCookieValue(TokenBasedRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
		
		return MainMenu.class;
	}
}
