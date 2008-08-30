package org.appfuse.webapp.pages.admin;

import java.io.IOException;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.apache.tapestry5.services.Context;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.listener.StartupListener;
import org.appfuse.webapp.pages.MainMenu;
import org.slf4j.Logger;

/**
 *  @author Serge Eby
 * @version $Id$
 * 
 */
public class Reload extends BasePage {

	@Inject
	private Logger logger;

	@InjectPage
	private MainMenu mainMenu;

	@Inject
	private ComponentResources resources;

	@Inject
	private ApplicationGlobals globals;

	Object onActivate() throws IOException {
		StartupListener.setupContext(globals.getServletContext());
		mainMenu.addInfo("reload.succeeded", true);
		return mainMenu;
	}
}