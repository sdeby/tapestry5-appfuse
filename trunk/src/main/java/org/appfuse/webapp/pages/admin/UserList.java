package org.appfuse.webapp.pages.admin;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.appfuse.model.User;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.pages.MainMenu;
import org.appfuse.webapp.pages.UserEdit;
import org.appfuse.webapp.services.ServiceFacade;

/**
 * @author Serge Eby
 * @version $Id$
 * 
 * 
 */
public class UserList extends BasePage {

	private final static String[] COLUMNS = { "username", "email", "enabled" };

	@Inject
	private ServiceFacade serviceFacade;

	@Property
	private BeanModel<User> model;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private ComponentResources resources;

	@InjectPage
	private UserEdit userEdit;

	@Property
	private User user;

	@Component(parameters = { "event=add" })
	private EventLink addTop, addBottom;

	@Component(parameters = { "event=done" })
	private EventLink doneTop, doneBottom;

	{
		model = beanModelSource.createDisplayModel(User.class, resources.getMessages());
		model.include(COLUMNS);
		model.add("fullname");
		// Set labels
		model.get("username").label(getText("user.username"));
		model.get("email").label(getText("user.email"));
		model.get("enabled").label(getText("user.enabled"));
		model.get("fullname").label(getText("activeUsers.fullName"));
	}

	@SuppressWarnings("unchecked")
	public List<User> getUsers() {
		return serviceFacade.getUserManager().getUsers(null);
	}

	Object onAdd() {
		userEdit.initialize("list", null);
		return userEdit;
	}

	Object onDone() {
		return MainMenu.class;
	}

	Object onActionFromEdit(String id) {
		getLogger().debug("fetching user with id: {}", id);
		userEdit.initialize("list", id);
		return userEdit;
	}

}
