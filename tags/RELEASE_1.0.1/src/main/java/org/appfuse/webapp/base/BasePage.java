package org.appfuse.webapp.base;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.appfuse.webapp.data.FlashMessage;
import org.appfuse.webapp.data.FlashMessage.Type;
import org.appfuse.webapp.util.MessageUtil;
import org.slf4j.Logger;

/**
 * Base page containing common functionalities
 * 
 * @author Serge Eby
 * @version $Id$
 */
public class BasePage {
	
	@Inject
    private Logger logger;
    

    @Inject 
    private Messages messages;
    
    
    @Persist(PersistenceConstants.FLASH)
    private FlashMessage flashMessage;
    
    @Inject
    private RequestGlobals globals;
        

    
    public Logger getLogger() {
		return logger;
	}

	public Messages getMessages() {
		return messages;
	}

	public FlashMessage getFlashMessage() {
		return flashMessage;
	}

	public void setFlashMessage(FlashMessage flashMessage) {
		this.flashMessage = flashMessage;
	}

	public void addFlash(String message, Type type) {
		setFlashMessage(new FlashMessage(message, type));
	}

	public void addFlashByKey(String key, Type type, Object... args) {
    	setFlashMessage(new FlashMessage(getText(key, args), type));
    }
    
    // Message Catalog helper    
	protected String getText(String key, Object... args) {
		return MessageUtil.format(getMessages().get(key), args);
	}

	protected HttpServletRequest getRequest() {
    	return globals.getHTTPServletRequest();
    }
    
}
