package org.appfuse.webapp.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.appfuse.webapp.data.FlashMessage;
import org.appfuse.webapp.data.FlashMessage.Type;

/**
 * Displays message based on the object parameter
 * 
 * @author Serge Eby
 * @version $Id$
 * 
 * 
 */
public class Flash {

	@Property
	@Parameter(required = true, allowNull = true)
	private FlashMessage object;

	@Inject
	private Messages messages;

	@Property
	@Inject
	@Path("context:/images/iconInformation.gif")
	private Asset iconInformation;

	@Property
	@Inject
	@Path("context:/images/iconWarning.gif")
	private Asset iconWarning;

	final boolean beginRender(MarkupWriter writer) {
		// Skip if no empty of null message
		if (object == null || "".equals(object.getMessage())) {
			return false;
		}

		// Default to success values
		String clientId = "successMessages";
		String className = "message";
		Asset icon = iconInformation;
		String altName = messages.get("icon.information");

		// Overwrite default values if type is failure
		if (object.getType() == Type.FAILURE) {
			clientId = "errorMessages";
			className = "error";
			altName = messages.get("icon.warning");
			icon = iconWarning;
		}

		writer.element("div", 
				       "id", clientId, 
				       "class", className);
		writer.element("img", 
				       "src", icon.toClientURL(), 
				       "alt", altName,
				       "class", "icon");
		writer.write(object.getMessage());
		writer.end(); // img

		writer.end(); // div

		return false;
	}
}
