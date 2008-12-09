package org.appfuse.webapp.data;

/**
 * Wrapper class for "flash" messages displayed on pages
 * 
 * @author Serge Eby
 * @version $Id$
 *
 */
public class FlashMessage {
	public enum Type {
		SUCCESS, FAILURE;
	}
	
	private String message;
	private Type type;
	
	
	public FlashMessage(String message, Type type) {
		this.message = message;
		this.type = type;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
}
