package org.appfuse.webapp.pages;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.services.ComponentInvocation;
import org.apache.tapestry5.internal.services.ComponentInvocationImpl;
import org.apache.tapestry5.internal.services.PageRenderTarget;
import org.apache.tapestry5.services.ContextPathEncoder;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.webapp.base.BasePageTestCase;
import org.subethamail.wiser.Wiser;

import java.util.HashMap;
import java.util.Map;

public class PasswordHintTest  extends BasePageTestCase {

// TODO: Rewrite test case using Tapestry5 built-in features
// and fix ussues with T5 PageTester	

	 private PageTester tester; 
	 private ContextPathEncoder contextPathEncoder;
	 
    @Override
    protected void onSetUpBeforeTransaction() throws Exception {
//        super.onSetUpBeforeTransaction();
//        tester = getPageTester();
//        contextPathEncoder = tester.getService(ContextPathEncoder.class);
    }

    @Override
    protected void onTearDownAfterTransaction() throws Exception {
        super.onTearDownAfterTransaction();
        tester.shutdown();
    }
    
//    public void testActivate() throws Exception {
//        // start SMTP Server
//        Wiser wiser = new Wiser();
//        wiser.setPort(getSmtpPort());
//        wiser.start();
//
//
//        Object[] context = new Object[]{ "user"};
//        ComponentInvocation invocation = new ComponentInvocationImpl(contextPathEncoder,
//                new PageRenderTarget("PasswordHint"), null, context, false);
//        
//        Document doc = tester.invoke(invocation);
//
//        assertNotNull(doc);
//        
//        
//        // verify an account information e-mail was sent
//        wiser.stop();
//        //assertTrue(wiser.getMessages().size() == 1);
//        
//
//    }

    
}
