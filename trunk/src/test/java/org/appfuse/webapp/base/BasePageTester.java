package org.appfuse.webapp.base;

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.spring.SpringModuleDef;
import org.apache.tapestry5.ioc.def.ModuleDef;
import org.apache.tapestry5.test.PageTester;
import org.apache.xbean.spring.context.ClassPathXmlApplicationContext;
import org.appfuse.webapp.services.AppModule;
import org.appfuse.webapp.services.TestModule;
import org.springframework.context.ApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

public abstract class BasePageTester  extends Assert {

	private static final String APP_PACKAGE = "org.appfuse.webapp";
	private final static String APP_NAME = "app";

	protected PageTester tester;
	protected Document doc;
	protected Map<String, String> fieldValues;
	private ApplicationContext context;

	// @BeforeMethod
	@BeforeTest(alwaysRun = true)
	protected void before() {
		// tester = new SpringPageTester(APP_PACKAGE, APP_NAME,
		// "src/main/webapp", TestModule.class);
		tester = new PageTester(APP_PACKAGE, APP_NAME, "src/main/webapp") {

			@Override
			protected ModuleDef[] provideExtraModuleDefs() {
				ApplicationContext springContext = null;

				try {
					springContext = getSpringContext();
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

			private ApplicationContext getSpringContext() {
				if (context == null) {
					context = new ClassPathXmlApplicationContext(new String[] {
							"classpath:/applicationContext-resources.xml",
							"classpath:/applicationContext-dao.xml",
							"classpath:/applicationContext-service.xml",
							"classpath*:/applicationContext.xml",
							"/WEB-INF/applicationContext*.xml", });
				}
				return context;
			}
		};

		fieldValues = new HashMap<String, String>();
	}

	// @AfterMethod
	@AfterTest(alwaysRun = true)
	protected void after() {
		if (tester != null) {
			tester.shutdown();
		}

	}

}