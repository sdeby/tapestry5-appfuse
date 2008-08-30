package org.appfuse.webapp.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Environment;
import org.appfuse.Constants;
import org.appfuse.webapp.base.BasePage;


/**
 * Global Layout component
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
//@IncludeJavaScriptLibrary("context:scripts/global.js")
//@IncludeStylesheet("context:styles/{prop:cssTheme}/theme.css",
//		"context:styles/{prop:cssTheme}/print.css")
public class Layout {

	@Inject
	private Environment environment;

	@Inject
	private Context context;

	@Inject
	private RenderSupport renderSupport;

	
	@Property
	@Parameter(required = true)
	private String title;

//
//	@InjectContainer
//	@Property
//	private BasePage page;

//	
//	private String message;
//	
//	private String type;
	
	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
	private String heading;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
	private String menu;

	@Property
	@Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
	private String bodyId;
	
//	void setupRender() {
//		renderSupport.addScriptLink(globalLibrary);
//	}

	public String getCssTheme() {
		return context.getInitParameter(Constants.CSS_THEME);
	}

}
