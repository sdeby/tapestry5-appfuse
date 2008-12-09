package org.appfuse.webapp.pages.admin;

import java.util.Set;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Context;
import org.appfuse.model.User;
import org.appfuse.webapp.base.BasePage;

/**
 * Lists all active users
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class ActiveUsers extends BasePage {

	private final static String[] COLUMNS = { "username" };

	@Property
	@Inject
	@Path("context:images/arrow_up.png")
	private Asset upArrow;

	@Property
	@Inject
	@Path("context:images/arrow_down.png")
	private Asset downArrow;

	@Retain
	@Property
	private BeanModel<User> model;

	@Property
	private User user;
	
	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources resources;

	@Inject
	private Context context;

	{
		model = beanModelSource.createDisplayModel(User.class, resources.getMessages());
		model.include(COLUMNS);
		model.add("fullname");
		// Set labels
		model.get("username").label(getText("user.username"));
		model.get("fullname").label(getText("activeUsers.fullName"));
	}

	
	@SuppressWarnings("unchecked")
	public Set<User> getActiveUsers() {
		Set<User> users = (Set<User>) context.getAttribute("userNames");
		return users;
	}

	public String getMainMenuLink() {
		return resources.createPageLink("MainMenu", false).toURI();
	}
}
