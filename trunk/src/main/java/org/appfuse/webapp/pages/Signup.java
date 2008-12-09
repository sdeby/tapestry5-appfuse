package org.appfuse.webapp.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.appfuse.Constants;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.components.UserForm;
import org.appfuse.webapp.data.FlashMessage.Type;
import org.appfuse.webapp.services.ServiceFacade;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;

/**
 * Self-registration page for new users
 * 
 * @author Serge Eby
 * @version $Id$
 * 
 * 
 */
public class Signup extends BasePage {

	@Inject
	private ServiceFacade serviceFacade;

	@Persist
	@Property
	private User user;

	@Inject
	private Request request;
	
	@Inject
	private ComponentResources resources;
	
	@InjectPage
	private MainMenu mainMenu;
	
	@Inject
	private Response response;

	
	@Component(id = "signup")
	private UserForm form;

	
	private String flashMsg = null;
	
	
	void beginRender() {
		if (user == null) {
			user = new User();
		}
	}

	// ~ Event Handlers

	Object onCancel() {
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("entered cancel method");
		}
		return Login.class;
	}

	void onValidateForm() {
		// make sure the password fields match
		if (!StringUtils.equals(user.getPassword(), user.getConfirmPassword())) {			
			flashMsg = getText("errors.twofields", 
					getText("user.confirmPassword"), getText("user.password"));
			addFlash(flashMsg, Type.FAILURE);
			form.recordError(form.getConfirmPasswordField(), flashMsg);
		}
	}

	Object onSuccess() throws IOException {
		getLogger().debug("entered save method");

		// Enable user;
		user.setEnabled(true);

		// Set the default user role on this new user
		user.addRole(serviceFacade.getRoleManager()
				.getRole(Constants.USER_ROLE));

		try {
			user = serviceFacade.getUserManager().saveUser(user);
		} 
		catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			getLogger().warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN, null);
			return null; // FIXME
		} 
		catch (UserExistsException e) {		
			flashMsg = getText("errors.existing.user", 
					                   user.getUsername(), user.getEmail());
			addFlash(flashMsg, Type.FAILURE);
			form.recordError(form.getUsernameField(), flashMsg);
			
			// redisplay the unencrypted passwords
			user.setPassword(user.getConfirmPassword());
			return null; 
		}
		
		// log user in automatically
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				user.getUsername(), user.getConfirmPassword(), user.getAuthorities());
		auth.setDetails(user);
		SecurityContextHolder.getContext().setAuthentication(auth);

		// Send user an e-mail
		if (getLogger().isDebugEnabled()) {
			getLogger().debug("Sending user '{}' an account information e-mail", user.getUsername());
		}

		SimpleMailMessage message = serviceFacade.getMailMessage();
		message.setTo(user.getFullName() + "<" + user.getEmail() + ">");

		StringBuffer msg = new StringBuffer();
		msg.append(getText("signup.email.message"));
		msg.append("\n\n").append(getText("user.username"));
		msg.append(": ").append(user.getUsername()).append("\n");
		msg.append(getText("user.password")).append(": ");
		msg.append(user.getPassword());
		msg.append("\n\nLogin at: ")
				.append(RequestUtil.getAppURL(getRequest()));
		message.setText(msg.toString());
		message.setSubject(getText("signup.email.subject"));

		try {
			serviceFacade.getMailEngine().send(message);
		} catch (MailException me) {
			addFlash(me.getMostSpecificCause().getMessage(), Type.FAILURE);
			//return null;
		}

		flashMsg = getText("user.registered");
		mainMenu.addFlash(flashMsg, Type.SUCCESS);
		//response.sendRedirect(resources.createPageLink("mainmenu", false).toAbsoluteURI());
		
		// Use Link to make PageTester happy :)
		response.sendRedirect(resources.createPageLink("mainmenu", false));
		
		return null;
	}
	
}
