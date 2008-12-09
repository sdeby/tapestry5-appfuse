package org.appfuse.webapp.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.test.PageTester;
import org.appfuse.Constants;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

public abstract class BasePageTestCase extends
		AbstractTransactionalDataSourceSpringContextTests {
	protected final Log log = LogFactory.getLog(getClass());

	protected final static String EXTENSION = ".html";
	protected static final String MESSAGES = Constants.BUNDLE_KEY;
	private int smtpPort = 25250;

	private static final String APP_PACKAGE = "org.appfuse.webapp";
	private final static String APP_NAME = "app";

	protected String[] getConfigLocations() {
		super.setAutowireMode(AUTOWIRE_BY_NAME);
		return new String[] { "classpath:/applicationContext-resources.xml",
				"classpath:/applicationContext-dao.xml",
				"classpath:/applicationContext-service.xml",
				"classpath*:/applicationContext.xml", // for modular archetypes
				"/WEB-INF/applicationContext*.xml" };
	}

	@Override
	protected void onSetUpBeforeTransaction() throws Exception {
		smtpPort = smtpPort + (int) (Math.random() * 100);

		// change the port on the mailSender so it doesn't conflict with an
		// existing SMTP server on localhost
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl) applicationContext
				.getBean("mailSender");
		mailSender.setPort(getSmtpPort());
		mailSender.setHost("localhost");
	}

	protected int getSmtpPort() {
		return smtpPort;
	}

	protected PageTester getPageTester() {
		PageTester tester = new PageTester(APP_PACKAGE, APP_NAME, "src/main/webapp") {

			@Override
			protected ModuleDef[] provideExtraModuleDefs() {
				ApplicationContext springContext = null;

				try {
					springContext = getApplicationContext();
				} catch (Exception ex) {
					throw new RuntimeException(
							"SpringMessages.failureObtainingContext(ex)", ex);
				}

				if (springContext == null) {
					throw new RuntimeException(
							"SpringMessages.missingContext()");
				}

				return new ModuleDef[] { new SpringModuleDef(springContext) };
			}
		};

		return tester;
	}

	// protected IPage getPage(Class clazz) {
	// return getPage(clazz, null);
	// }
	//    
	// protected IPage getPage(Class clazz, Map<String, Object> properties) {
	// Creator creator = new Creator();
	// if (properties == null) {
	// properties = new HashMap<String, Object>();
	// }
	//        
	// Messages messages = new MessageFormatter(log, MESSAGES);
	// MockHttpServletRequest request = new MockHttpServletRequest();
	// request.setRemoteUser("user");
	//        
	// properties.put("engineService", new MockPageService());
	// properties.put("messages", messages);
	// properties.put("request", request);
	// properties.put("response", new MockHttpServletResponse());
	//        
	// return (IPage) creator.newInstance(clazz, properties);
	// }
}