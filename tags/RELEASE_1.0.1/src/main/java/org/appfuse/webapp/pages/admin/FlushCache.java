package org.appfuse.webapp.pages.admin;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ApplicationGlobals;
import org.appfuse.webapp.base.BasePage;

import com.opensymphony.oscache.web.ServletCacheAdministrator;

/**
 * Flush cache
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class FlushCache extends BasePage {
	
	@Property
	@Inject
	@Path("context:images/iconInformation.gif")
	private Asset iconInformation;
	

	@Inject
	private ApplicationGlobals globals;
	
	private ServletCacheAdministrator admin;
	
	void onActivate() {
		if (admin == null) {
			admin = ServletCacheAdministrator.getInstance(globals.getServletContext());
		}	
	}
	
	void beginRender() {
		getLogger().debug("Ready to flush the cache");
		admin.flushAll();
	}
	
}
