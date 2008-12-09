package org.appfuse.webapp.pages;

import org.apache.tapestry5.annotations.ApplicationState;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.data.UserSession;
import org.appfuse.webapp.services.ServiceFacade;
import org.slf4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.userdetails.UserDetails;


/**
 * Main entry point of the application
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class MainMenu extends BasePage {
	
	@Inject
	private Logger logger;
	
	@Inject
	private ServiceFacade serviceFacade;
	
	@InjectPage
	private UserEdit userEdit;
	
	@Property
	@Persist
	private User user;
	
	@ApplicationState
	private UserSession userSession;
	
	void pageLoaded() {
		logger.debug("Storing user info in the ASO");
		userSession.setCurrentUser((User) getUserInfo());
		userSession.setCookieLogin(true);
	}
	
	private UserDetails getUserInfo() {
		UserDetails userDetails = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			Object principal = auth.getPrincipal();
			if (principal != null) {
				if (principal instanceof String) {
					userDetails = null;
				} else {
					userDetails = (UserDetails) principal;
				}
			}
		}
		return userDetails;
	}
	
    Object onActionFromEditProfile() {
    	//FIXME: Use ASO since user object already stored in it
        String username = getRequest().getRemoteUser();
       
        logger.debug("fetching user profile: " + username);

        user = serviceFacade.getUserManager().getUserByUsername(username);
        user.setConfirmPassword(user.getPassword());
        userEdit.setUser(user);
        userEdit.setFrom("MainMenu");
        return userEdit;
    }
}