package org.appfuse.webapp.pages.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.appfuse.webapp.base.BasePage;

import com.opensymphony.clickstream.Clickstream;

/**
 * Tracks users activity 
 * 
 *  @author Serge Eby
 * @version $Id$
 *
 */
public class ClickStreams extends BasePage {

	
	private String showBots = "false";
	
	@Inject 
	private Request request;
	
	@Inject 
	private Context context;
	
	@Inject
	private ComponentResources resources;
	
	@Persist
	@Property
	private Map<String,Clickstream> clickstreams;
	
	
	@Persist
	@Property
	private Map<String,Clickstream> activeStreams;
	
	@Property
	private Map.Entry<String,Clickstream> entry;
	
	@Property
	private String key;
	
	private Clickstream stream;
	
	
	@Property
	private int index;
	
	String onPassivate() {
		return showBots;	
	}
	
	 void onActivate(String context) {
			if (context == null) {
				context = "false";
			}
			this.showBots = context;
			
	}
		
	
	@SuppressWarnings("unchecked")
	void setupRender() {
		if (clickstreams == null) {
			getLogger().debug("Initializing clickstreams object from servlet context");
			clickstreams = (Map<String,Clickstream>) context.getAttribute("clickstreams");
			
		}
		if (clickstreams == null) {
			getLogger().debug("Clickstreams still null !!!!");
		}
		
		
		if ("both".equals(showBots)) {
			activeStreams = clickstreams;
			return;
		}
		
		// Update streams accordingly
		activeStreams = new HashMap<String, Clickstream>();

		for (Map.Entry<String,Clickstream> entry : clickstreams.entrySet()) {
			String key = entry.getKey();
			Clickstream value = entry.getValue();
			
			if ("true".equals(showBots) && value.isBot()) {
				activeStreams.put(key, value);
			}
			else if ("false".equals(showBots) && !value.isBot()) {
				activeStreams.put(key, value);
			}		
		}
		

	}
	
	
	public int getNext() {
		return index + 1;
	}
	
	
	public String getShowBots() {
		String showBotsParam = request.getParameter("showbots");
		if (showBotsParam != null) {
			if ("true".equals(showBotsParam)) {
				showBots = "true";
			}
			else if ("both".equals(showBotsParam)) {
					showBots = "both";
			}
		}
		return showBots;		
	}
	
	public boolean isClickStreamEmpty() {
		return (activeStreams == null || activeStreams.keySet().size() == 0);
	}
	

	
	public String getStreamHostname() {			
		Clickstream value = entry.getValue();
		String hostname = (value.getHostname() != null && !"".equals(value.getHostname()) ? 
				value.getHostname() : "Stream");
		return hostname;
	}
	
	public int getStreamSize() {
		return entry.getValue().getStream().size();
	}
	
	
	public Object getAllStreamsLink() {
		return createLink("both");
	}

	public Object getBotStreamsLink() {
		return createLink("true");
	}

	public Object getNoBotStreamsLink() {
		return createLink("false");
	}

	private Object createLink(String flag) {
		return resources.createPageLink(resources.getPageName(), true, flag);
	}
	
}
