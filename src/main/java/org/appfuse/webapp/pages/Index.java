package org.appfuse.webapp.pages;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Response;
import org.appfuse.webapp.base.BasePage;

/**
 * Main index page. This also handles 404 errors 
 * 
 * @author Serge Eby
 * @version $Id$
 */
public class Index extends BasePage {


	@Inject 
	private RequestGlobals globals;
	
	@Inject 
	private Request request;
	
	@Inject 
	private Response response;
	
	
	@SuppressWarnings("unchecked")
	private List eventContext;

	@SuppressWarnings("unchecked")
	public List getEventContext() {
		return eventContext;
	}

	Object onActivate(List context) {
		eventContext = context;
		if (context != null && !context.isEmpty()) {
			int index = 0;
			for (Object obj : context) {
				index++;
				getLogger().debug(String.format("Context #%d =  %s", index, obj.toString()));
			}
			getLogger().debug("Redirecting to PageNotFound");
			return NotFound.class;
		}
		// Redirect to MainMenu
		return MainMenu.class;

	}
}
