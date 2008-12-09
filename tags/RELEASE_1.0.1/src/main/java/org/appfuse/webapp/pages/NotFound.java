package org.appfuse.webapp.pages;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.webapp.base.BasePage;

/**
 * Page to be displayed whenever a page is not found (404 error)
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class NotFound extends BasePage {

	@Property
	@Inject
	@Path("context:images/404.jpg")
	private Asset notFoundImage;
	
	@Inject
	private ComponentResources resources;
	
	public String getNotFoundMessage() {
		String url = resources.createPageLink("MainMenu", false).toURI();
		return getText("404.message", url);
	}
	
	
}
