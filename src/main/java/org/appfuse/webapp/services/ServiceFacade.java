package org.appfuse.webapp.services;

import java.util.List;
import java.util.Map;

import org.appfuse.service.MailEngine;
import org.appfuse.service.RoleManager;
import org.appfuse.service.UserManager;
import org.springframework.mail.SimpleMailMessage;

/**
 * This interface wraps most services to minimize repeated injections
 * 
 * @version $Id$
 * @author Serge Eby 
 * 
 */
public interface ServiceFacade {

	UserManager getUserManager();
	RoleManager getRoleManager();
	MailEngine getMailEngine();
	SimpleMailMessage getMailMessage();
	List<String> getAvailableRoles();
	Map<String, String> getAvailableCountries();
}
