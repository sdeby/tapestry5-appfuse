package org.appfuse.webapp.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.appfuse.Constants;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.components.UserForm;
import org.appfuse.webapp.data.FlashMessage.Type;
import org.appfuse.webapp.pages.admin.UserList;
import org.appfuse.webapp.services.ServiceFacade;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationTrustResolver;
import org.springframework.security.AuthenticationTrustResolverImpl;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

/**
 * Allow adding new users or viewing/updating existing users
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class UserEdit extends BasePage {

	@Property
	private User user;
	
	private String userId;
	
	@Property
	private List<String> selectedRoles;


	@Inject
	private Request request;
	
	@Property
	private List<String> userRoles;

	@Inject
	private ComponentResources resources;


	@Inject
	private ServiceFacade serviceFacade;

	@InjectPage
	private UserList userList;

	@InjectPage
	private MainMenu mainMenu;


	@Persist 
	@Property
	private String from;


	@Component(id = "edit")
	private UserForm form;

	private boolean delete = false;
	
	private String flashMsg = null;

	
	 public Object initialize(String from, String userId) {
		this.from = from;
		this.userId = userId;
		return this;
	}
	
	
	String onPassivate() {
		return userId;			
	}
	
	void onActivate(String id) {
		this.userId = id;	
	}
	

	void beginRender() {	
		// if user logged in with remember me, display a warning that they
		// can't change passwords
		getLogger().debug("checking for remember me login...");
		
		if (isRememberMe()) {
			// add warning message
			addFlash(getText("userProfile.cookieLogin"), Type.SUCCESS);
		}
	}

	// ~ --- Event Handlers
	
	void onPrepare() {
		
		if (userId == null) {
			getLogger().debug("Initializing new user object");
			user = new User();
			user.addRole(new Role(Constants.USER_ROLE));
		}
		else {
			getLogger().debug("Retrieving existing user");
			user = serviceFacade.getUserManager().getUser(userId);	
			user.setConfirmPassword(user.getPassword());
		}		

		getLogger().debug("This page was activated from: {} " , from);
		
		selectedRoles = new ArrayList<String>(user.getRoles().size());

		for (Role role : user.getRoles()) {
			getLogger().debug("Adding Role: " + role.getName());
			selectedRoles.add(role.getName());
		}

		//setUserRoles(selectedRoles);
		userRoles =  selectedRoles;		
	}

	
	void onValidateForm() {
		if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {		
			flashMsg = getText("errors.twofields", 
					getText("user.confirmPassword"), getText("user.password"));
			addFlash(flashMsg, Type.FAILURE);
			form.recordError(form.getConfirmPasswordField(), flashMsg);
		}
	}
	

	Object onSuccess() throws UserExistsException, IOException {		
		getLogger().debug("entering onSuccess method");
		
		// Delete Button Clicked
		if (delete) {
			return onDelete();
		}
		
		
		HttpServletRequest request = getRequest();


		if (selectedRoles != null && !selectedRoles.isEmpty()) {
			user.getRoles().clear();
			for (String roleName : selectedRoles) {
				getLogger().debug("Adding Role: {} " , roleName);
				user.addRole(serviceFacade.getRoleManager().getRole(roleName));
			}
		}
		Integer originalVersion = user.getVersion();

		try {
			user = serviceFacade.getUserManager().saveUser(user);			
		} 
		catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			getLogger().warn(ade.getMessage());
			//getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
			//return null;
			return AccessDenied.class;
		} 
		catch (UserExistsException e) {
			flashMsg = getText("errors.existing.user", 
					                    user.getUsername(), user.getEmail());
			addFlash(flashMsg, Type.FAILURE);
			form.recordError(form.getEmailField(), flashMsg);
			
			user.setPassword(user.getConfirmPassword());
			user.setVersion(originalVersion);
			return null;
		}

		if (!form.isFromList()
				&& user.getUsername().equals(getRequest().getRemoteUser())) {
			// add success messages
			flashMsg = getText("user.saved", user.getFullName());
			 mainMenu.addFlash(flashMsg, Type.SUCCESS);
			return mainMenu;
			
		} else {
			// add success messages
			if (originalVersion == null) {
				sendNewUserEmail(request, user);
				flashMsg = getText("user.added", user.getFullName());
				userList.addFlash(flashMsg, Type.SUCCESS);
				return userList;
			} else {
				flashMsg = getText("user.updated.byAdmin", user.getFullName());
				addFlash(flashMsg, Type.SUCCESS);
				return null; // return to current page
			}
		}
	}

	
	Object onCancel() {
		getLogger().debug("Entering 'cancel' method");

		if ("list".equalsIgnoreCase(from)) {
			return resources.createPageLink("admin/UserList", false);
		} 
		else {
			return resources.createPageLink("MainMenu", false);
		}
	}


	void onSelectedFromEdit() {
		delete = true;
	}
	
	Object onDelete() {
		getLogger().debug("entered delete method");	
		// Save full name before deletion
		String fullName = user.getFullName();
		serviceFacade.getUserManager().removeUser(user.getId().toString());
		String flashMsg = getText("user.deleted", fullName);
		userList.addFlash(flashMsg, Type.SUCCESS);
		getLogger().debug("After deletion.. ready to return userList object");	
		return userList;
	}


	public boolean isRememberMe() {
		 AuthenticationTrustResolver resolver = new AuthenticationTrustResolverImpl();
		 SecurityContext ctx = SecurityContextHolder.getContext();
		
		 if (ctx != null) {
			 Authentication auth = ctx.getAuthentication();
			 return resolver.isRememberMe(auth);
		 }
		 return false;
	 }


	public boolean isCookieLogin() {
		return isRememberMe();
	}


	// ~ Helper methods
	private void sendNewUserEmail(HttpServletRequest request, User user) {
		// Send user an e-mail
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("Sending user '" + user.getUsername()
					+ "' an account information e-mail");
		}

		SimpleMailMessage message = serviceFacade.getMailMessage();
		message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

		StringBuffer msg = new StringBuffer();
		msg.append(getText("newuser.email.message", user.getFullName()));
		msg.append("\n\n").append(getText("user.username"));
		msg.append(": ").append(user.getUsername()).append("\n");
		msg.append(getText("user.password")).append(": ");
		msg.append(user.getPassword());
		msg.append("\n\nLogin at: ").append(RequestUtil.getAppURL(request));
		message.setText(msg.toString());

		message.setSubject(getText("signup.email.subject"));

		try {
			serviceFacade.getMailEngine().send(message);
		} catch (MailException me) {
			addFlash(me.getCause().getLocalizedMessage(), Type.FAILURE);
		}
	}
}
