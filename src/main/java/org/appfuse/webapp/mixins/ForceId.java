package org.appfuse.webapp.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.InjectContainer;

/**
 * This was "copied" from Tapestry5 base code 
 * Useful to force an Id on components such as ActionLink and EventLink
 */
public class ForceId {
    @InjectContainer
    private ClientElement container;

    void afterRender() {
        container.getClientId();
    }
}
