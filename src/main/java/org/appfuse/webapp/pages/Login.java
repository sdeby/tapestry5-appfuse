package org.appfuse.webapp.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.appfuse.Constants;
import org.appfuse.webapp.base.BasePage;
import org.springframework.security.ui.AbstractProcessingFilter;

/**
 * Login Page
 * 
 * @author Serge Eby
 * @version $Id$
 */
@IncludeJavaScriptLibrary("context:scripts/login.js")
public class Login extends BasePage {

	private static final String AUTH_FAILED = "error";
	private static final String SECURITY_URL = "/j_security_check";

	@Inject
	private Request request;

	@Property
	@Inject
	@Path("context:images/iconWarning.gif")
	private Asset iconWarning;

	
	@Inject
	private ComponentResources resources;

	@Inject
	private Context context;

	@Environmental
	private RenderSupport renderSupport;


	@Property
	private String errorMessage = null;

	String onPassiavate() {
		return errorMessage;
	}

	void onActivate(String loginError) {
		if (AUTH_FAILED.equals(loginError)) {
			this.errorMessage = ((Exception) request
					.getSession(true)
					.getAttribute(
							AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY))
					.getMessage();
			getLogger().error("Error while attempting to login: {}", errorMessage);
		}
	}


	void afterRender() {
		JSONObject params = new JSONObject();
		params.put("url", getUrl());
		params.put("passwordHintLink", getPasswordHintLink());
		params.put("requiredUsername", getRequiredUsernameError());
		params.put("requiredPassword", getRequiredPasswordError());

		renderSupport.addScript("initialize(%s);", params);
	}


	void cleanupRender() {
		this.errorMessage = null;
	}

	
	public String getSpringSecurityUrl() {
		return String.format("%s%s", request.getContextPath(), SECURITY_URL);
	}

	@SuppressWarnings("unchecked")
	public boolean isRememberMeEnabled() {
		Map config = (HashMap) context.getAttribute(Constants.CONFIG);
		if (config != null) {
			return (config.get("rememberMeEnabled") != null);
		}
		return false;
	}

	
	public String getSignup() {
		String link = resources.createPageLink("Signup", false).toAbsoluteURI();
		return getText("login.signup", link);
	}

	//~-- Javascript/JSON object helper methods
	private String getRequiredFieldError(String field) {
		return getText("errors.required", field);
	}

	private String getRequiredUsernameError() {
		return getRequiredFieldError(getText("label.username"));
	}

	private String getRequiredPasswordError() {
		return getRequiredFieldError(getText("label.password"));
	}

	private String getPasswordHintLink() {
		return resources.createPageLink("PasswordHint", false).toAbsoluteURI();
	}

	private String getUrl() {
		return resources.createPageLink("Login", false).toAbsoluteURI();
	}

}
