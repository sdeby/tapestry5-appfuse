package org.appfuse.webapp.pages.admin;

import java.io.IOException;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.appfuse.webapp.base.BasePage;
import org.appfuse.webapp.data.FlashMessage.Type;
import org.appfuse.webapp.listener.StartupListener;
import org.appfuse.webapp.pages.MainMenu;

/**
 *  @author Serge Eby
 * @version $Id$
 * 
 */
public class Reload extends BasePage {

	@InjectPage
	private MainMenu mainMenu;

	@Inject
	private ApplicationGlobals globals;

	Object onActivate() throws IOException {
		StartupListener.setupContext(globals.getServletContext());
		String flashMsg = getText("reload.succeeded");
		mainMenu.addFlash(flashMsg, Type.SUCCESS);
		return mainMenu;
	}
}
