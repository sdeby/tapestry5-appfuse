package org.appfuse.webapp.pages;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.webapp.base.BasePage;

/**
 * Page to handle 403 errors 
 * 
 * @author Serge Eby
 * @version $Id$
 */
public class AccessDenied extends BasePage {

	@Property
	@Inject
	@Path("context:images/403.jpg")
	private Asset accessDeniedImage;
	
	@Inject
	private ComponentResources resources;
	
	public String getaccessDeniedMessage() {
		String message = getText("403.message");
		String url = resources.createPageLink("Index", false).toURI();
		return String.format(message, url);
	}
}
