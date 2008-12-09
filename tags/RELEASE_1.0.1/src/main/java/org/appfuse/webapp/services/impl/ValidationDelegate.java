package org.appfuse.webapp.services.impl;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.services.Environment;

/**
 * Custom Implementation of the ValidationDecorator Interface
 * 
 * @author Serge Eby
 * @version $Id$
 * 
 */
public class ValidationDelegate implements ValidationDecorator {

	private final Environment environment;

	private final Asset iconAsset;

	private final MarkupWriter markupWriter;

	public ValidationDelegate(Environment environment, Asset iconAsset,
			MarkupWriter markupWriter) {
		this.environment = environment;
		this.markupWriter = markupWriter;
		this.iconAsset = iconAsset;
	}

	public void insideField(Field field) {

		if (inError(field)) {
			if (isMissing(field)) {
				addErrorClassToCurrentElement("fieldMissing");
				return;
			}
			addErrorClassToCurrentElement("fieldInvalid");
		}
	}

	public void beforeLabel(Field field) {

	}

	public void insideLabel(Field field, Element labelElement) {
		if (inError(field)) {
			addErrorClassToCurrentElement("error");
		}
		if (field.isRequired()) {
			labelElement.raw("<span class=\"req\"> *</span>");
		}

	}

	public void afterLabel(Field field) {
	}

	public void beforeField(Field field) {
		if (inError(field)) {
			markupWriter.element("span", "class", "fieldError");
			String iconId = field.getClientId() + ":icon";
			markupWriter.element("img", "src", iconAsset.toClientURL(), "alt",
					"", "class", "validationWarning", "id", iconId);
			markupWriter.end(); // img
			markupWriter.writeRaw("&nbsp;");

			String error = getError(field);
			if (error == null) {
				error = "";
			}
			markupWriter.writeRaw(error);
			markupWriter.end(); // span
		}

	}

	public void afterField(Field field) {
	}

	
	private ValidationTracker getTracker() {
		return environment.peekRequired(ValidationTracker.class);
	}
	
	private boolean inError(Field field) {
		return getTracker().inError(field);
	}

	private String getError(Field field) {
		return getTracker().getError(field);
	}

	private boolean isMissing(Field field) {
		String value = getTracker().getInput(field);
		
		boolean fiedIsBlank = value == null || value.trim().length() == 0;
		return (field.isRequired() && fiedIsBlank);		
	}

	private void addErrorClassToCurrentElement(String errorClass) {
		markupWriter.getElement().addClassName(errorClass);
	}

}
