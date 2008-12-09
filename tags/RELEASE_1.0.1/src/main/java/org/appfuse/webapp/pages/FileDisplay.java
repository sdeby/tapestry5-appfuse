package org.appfuse.webapp.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.appfuse.webapp.base.BasePage;

/**
* This class handles the uploading of a file and writing it to
* the filesystem.  Eventually, it will also add support for persisting the
* files information into the database.
*
* @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
* @author Serge Eby
* @version $Id$
* 
*/
public class FileDisplay extends BasePage {
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private UploadedFile file;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String name;

	@Property
	@Persist(PersistenceConstants.FLASH)
	private String path;
	
	@Property
	@Persist(PersistenceConstants.FLASH)
	private String url;
	
	Object initialize(UploadedFile file, String name, String path, String url) {
		this.file = file;
		this.name = name;
		this.path = path;
		this.url  = url;
		return this;
	}

	Object onDone() {
		return MainMenu.class;	
	}
	
	Object onAnotherUpload() {
		return FileUpload.class;
	}
	
}
