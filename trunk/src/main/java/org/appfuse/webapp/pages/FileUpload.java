package org.appfuse.webapp.pages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.upload.services.UploadedFile;
import org.apache.tapestry5.corelib.components.EventLink;
import org.appfuse.Constants;
import org.appfuse.webapp.base.BasePage;
import org.slf4j.Logger;

/**
* This class handles the uploading of a file and writing it to
* the filesystem.  Eventually, it will also add support for persisting the
* files information into the database.
*
* @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
* @author Serge Eby
* @version $Id$
*/
public  class FileUpload extends BasePage {
	

	@Inject
	private Logger logger;

	@Property
	private UploadedFile file;
	
	@Property
	private String name;
	
		
	@InjectPage
	private FileDisplay fileDisplay;
	
	@Inject
	private Request request;
	
	@Inject
	private Context context;
	
	@Inject
	private ComponentResources resources;
	

	@Component(parameters={"event=cancel"})
	private EventLink cancel;

	
	Object onCancel() {
        logger.debug("entered 'cancel' method");
		return MainMenu.class;
	}

 
     Object onSuccess() {
        
        if (file == null) {
            return null;
        }
        
        // write the file to the filesystem the directory to upload to
        String uploadDir =
            getServletContext().getRealPath("/resources") + "/" +
            getRequest().getRemoteUser() + "/";

       
        // Create the directory if it doesn't exist
        File dirPath = new File(uploadDir);

        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        File copied = new File(uploadDir + file.getFileName());
        file.write(copied);
        String path = dirPath.getAbsolutePath() + Constants.FILE_SEP +
        file.getFileName();
        String url =
            getRequest().getContextPath() + "/resources" + "/" +
            getRequest().getRemoteUser() + "/" +  file.getFileName();
 
        return fileDisplay.initialize(file, name, path, url);        
    }
    
}
