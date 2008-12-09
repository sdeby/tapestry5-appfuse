package org.appfuse.webapp.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.ExceptionReporter;
import org.appfuse.webapp.base.BasePage;

/**
 * Customized errror handling page 
 * 
 * @author Serge Eby
 * @version $Id$
 */
public class Error extends BasePage implements ExceptionReporter {
	
	@Property
    @Persist(PersistenceConstants.FLASH)
	private String error;

     public void reportException(Throwable exception) {
         error = exception.getLocalizedMessage();
     }
}
