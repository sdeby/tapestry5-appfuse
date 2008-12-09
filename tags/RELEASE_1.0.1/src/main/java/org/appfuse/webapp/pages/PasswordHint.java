package org.appfuse.webapp.pages;

import java.io.IOException;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.appfuse.model.User;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.data.FlashMessage.Type;
import org.appfuse.webapp.services.ServiceFacade;
import org.appfuse.webapp.util.RequestUtil;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * Managed Bean to send password hints to registered users.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * @author Serge Eby
 * @version $Id$
 */
public class PasswordHint extends BasePage {

	@Inject
	private ServiceFacade serviceFacade;

	@Inject
	private Request request;

	@Inject
	private Response response;

	@Inject
	private ComponentResources resources;
	
	@InjectPage
	private Login login;

	private String username;

	String onPassivate() {
		return username;
	}

	void onActivate(String username) throws IOException {
		this.username = username;
		// ensure that the username has been sent
		if (username == null || "".equals(username)) {
			getLogger()
					.warn(
							"Username not specified, notifying user that it's a required field.");

			String flashMsg = getText("errors.required",
					getText("user.username"));
			login.addFlash(flashMsg, Type.FAILURE);
			response.sendRedirect(request.getContextPath());
			//sendRedirectLink();
			return;
		}

	}

	boolean setupRender() throws IOException {
		String flashMsg = null;
		Type msgType = null;

		if (getLogger().isDebugEnabled()) {
			getLogger().debug(
					"Processing Password Hint for username: " + username);
		}

		// look up the user's information
		try {
			User user = serviceFacade.getUserManager().
			             getUserByUsername(username);

			StringBuffer msg = new StringBuffer();
			msg.append("Your password hint is: ")
					.append(user.getPasswordHint());
			msg.append("\n\nLogin at: ").append(
					RequestUtil.getAppURL(getRequest()));

			SimpleMailMessage message = serviceFacade.getMailMessage();
			message.setTo(user.getEmail());

			String subject = '[' + getText("webapp.name") + "] "
					+ getText("user.passwordHint");
			message.setSubject(subject);
			message.setText(msg.toString());
			serviceFacade.getMailEngine().send(message);

			flashMsg = getText("login.passwordHint.sent", username,
					user.getEmail());
			msgType = Type.SUCCESS;

		} 
		catch (UsernameNotFoundException e) {
			getLogger().warn(e.getMessage());
			// If exception is expected do not rethrow
			flashMsg = getText("login.passwordHint.error", username);
			msgType = Type.FAILURE;

		} 
		catch (MailException me) {
			flashMsg = me.getCause().getLocalizedMessage();
			msgType = Type.FAILURE;
		}

		// Set flash message on Login Page
		login.addFlash(flashMsg, msgType);

		response.sendRedirect(request.getContextPath());
		//sendRedirectLink();
		
		// Short-circuit other phases since this page doesn't have a template
		return false;
	}
	
	// Use Link instead of String for redirection -- for testing purpose
	// response.sendRedirect(String) is not yet implemented in PageTester (version 5.0.18)
	private void sendRedirectLink() throws IOException {
		Link link = resources.createPageLink(request.getContextPath(), false);
		response.sendRedirect(link);
	
	}

}
