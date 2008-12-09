package org.appfuse.webapp.pages;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.webapp.base.BasePageTestCase;

public class SignupTest extends BasePageTestCase {
	
	private PageTester tester;
	

  @Override
  protected void onSetUpBeforeTransaction() throws Exception {
      super.onSetUpBeforeTransaction();
      tester = getPageTester();
  }

  @Override
  protected void onTearDownAfterTransaction() throws Exception {
      super.onTearDownAfterTransaction();
      tester.shutdown();
  }

    
  	//TODO: Fix issues with PageTester
	//@Test
	public void testClickCancel() {
//		Document doc = tester.renderPage("Signup");
//		assertNotNull(doc);
//		Element link = doc.getElementById("cancelBottom");
//		doc = tester.clickLink(link);
//		System.out.println(doc.toString());
//		assertTrue(doc.toString().contains("Login Page"));		
	}
	
	//TODO: Fix issues with PageTester
	//@Test
	public void testClickSignup() {
//		Document doc = tester.renderPage("Signup");
//		Element form = doc.getElementById("form");
//		Element submit = doc.getElementById("saveBottom");
//	
//	//	 Setup form
//	   Map<String, String> fieldValues = new HashMap<String, String>();
//	   fieldValues.put("username", "self-registered");
//       fieldValues.put("password", "Password1");
//       fieldValues.put("confirmPassword", "Password1");
//       fieldValues.put("firstName", "First");
//       fieldValues.put("lastName", "Last");
//
//       fieldValues.put("email", "self-registered@raibledesigns.com");
//       fieldValues.put("website", "http://raibledesigns.com");
//       fieldValues.put("passwordHint", "Password is one with you.");
//   
//       fieldValues.put("city", "Denver");
//       fieldValues.put("state","CO");
//       fieldValues.put("country", "US");
//       fieldValues.put("postalCode", "80210");
//
//
//       doc = tester.clickSubmit(submit, fieldValues);
//       System.out.println(doc.toString());
//       assertTrue(doc.toString().contains("success"));     
	}
    
}