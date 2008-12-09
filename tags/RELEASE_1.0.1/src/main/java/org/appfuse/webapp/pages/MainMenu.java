package org.appfuse.webapp.pages;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.appfuse.model.User;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.services.ServiceFacade;


/**
 * Main entry point of the application
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class MainMenu extends BasePage {
		
	@Inject
	private RequestGlobals requestGlobals; 
	
	@Inject
	private ServiceFacade serviceFacade;
	
	@InjectPage
	private UserEdit userEdit;
	
	@Property
	private User user;
	
	void onActivate() {
		String username = requestGlobals.getHTTPServletRequest().getRemoteUser();
		user = serviceFacade.getUserManager().getUserByUsername(username);
	}
	

	  Object onActionFromEditProfile() {
	        getLogger().debug("fetching user profile: {} ", user.getUsername());
	        String userId = user.getId().toString();
	        userEdit.initialize( "main", userId);
	        return userEdit;
	 }
	    
}
