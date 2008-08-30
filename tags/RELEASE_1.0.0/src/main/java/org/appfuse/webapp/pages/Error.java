package org.appfuse.webapp.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.services.ExceptionReporter;
import org.apache.tapestry5.PersistenceConstants;

/**
 * Customized errror handling page 
 * 
 * @author Serge Eby
 * @version $Id$
 */
public class Error implements ExceptionReporter {
	@Property
    @Persist(PersistenceConstants.FLASH)
	private String error;

     public void reportException(Throwable exception) {
         error = exception.getLocalizedMessage();
     }
}
