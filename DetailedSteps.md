Detailed steps for the upgrade to Tapestry 5

# Getting Ready #

We will use **AppFuse** version 2.0.2 (http://www.appfuse.org) to kick start our project.

Let's run the following command to get a new basic Tapestry 4 project in the state needed for our migration:

```

mvn archetype:create -DarchetypeGroupId=org.appfuse.archetypes \
                     -DarchetypeArtifactId=appfuse-basic-tapestry \
                     -DremoteRepositories=http://static.appfuse.org/releases \
                     -DarchetypeVersion=2.0.2 -DgroupId=org.appfuse -DartifactId=t5-appfuse-demo
```

This base project relies on some built-in tapestry 4 components that needs to be modified. Therefore we need to get those as well.
This can be achieved with the following command:

```
mvn appfuse:full-source
```

Now, let's run the initial build to retrieve all dependencies:

```
mvn jetty:run-war -Phsqldb
```

NOTE: We are using HSQLDB as database.

If everything goes well, fire up a browser and point it at this URL: http://localhost:8080
Login as admin/admin and click around to ensure the Tapestry 4 version is fully functional.

Assuming all went well, we are now ready to start the upgrade to Tapestry 5.


# Foundation #
For users coming from Tapestry 4, here are a few key concepts or changes that need to sink in before moving forward:

  * No more XML configurations
  * Components (Pages & Components) are no longer abstract
  * All Components need a class file
  * T5 promotes convention over configuration(pages, components, mixins and services)
  * The listener concept is now expanded and replaced with "event handlers"
  * There is no more "RequestCycle" object
  * T5 page life cycle is different
  * IPropertySelection is gone and can usually be replaced by the Select component

Refer to http://tapestry.apache.org/tapestry5 for an exhaustive list of changes

With the above in mind, we are now ready to start the "surgery", scalpel please :)

# The Easy Part #

## Maven Update ##
Edit the pom.xml and remove all the tapestry 4 dependencies and add Tapestry 5 equivalent. Here are the main changes:

```
         [snip]
       <dependency>
            <groupId>org.apache.tapestry</groupId>
            <artifactId>tapestry-core</artifactId>
            <version>${tapestry.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tapestry</groupId>
            <artifactId>tapestry-spring</artifactId>
            <version>${tapestry.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tapestry</groupId>
            <artifactId>tapestry-upload</artifactId>
            <version>${tapestry.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.tapestry</groupId>
            <artifactId>tapestry-test</artifactId>
            <version>${tapestry.version}</version>
            <scope>test</scope>
        </dependency>
             ...
       <web.framework>tapestry5</web.framework>
       <tapestry.version>5.0.18</tapestry.version>
         [snip]
```


## Application Deployment Descriptor ##

Now, the web.xml file needs to be updated

Remove old style tapestry 4 servlet and replace with TapestrySpring filter.
Note that we are not using the standard Tapestry filter (TapestryFilter) simply because T5 comes fully integrated with Spring.

Here are the main changes:

```
       ....
   <display-name>My Tapestry Application</display-name>
    <context-param>
        <param-name>tapestry.app-package</param-name>
        <param-value>org.appfuse.webapp</param-value>
    </context-param>
      ....
    <filter>
        <filter-name>app</filter-name>
        <filter-class>org.apache.tapestry5.spring.TapestrySpringFilter</filter-class>
    </filter>
      .....
    <filter-mapping>
        <filter-name>app</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

We also removed all jsp referenced in the web.xml.

# A Little Bit More Work: Pages & Components Classes #

## First, Let's Re-org! ##
We need to clean-up and refactor a little bit to adhere to Tapestry 5 project layout
as defined [here](http://tapestry.apache.org/tapestry5/guide/project-layout.html).
We will end up with the following structure:

| **Desc.** | **Path** |
|:----------|:---------|
|Pages | src/main/java/org/appfuse/webapp/pages|
|Components| src/main/java/org/appfuse/webapp/components|
|Abstract Pages or Components| src/main/java/org/appfuse/webapp/base|
|Mixins| src/main/java/org/appfuse/webapp/mixins|
|Services| src/main/java/org/appfuse/webapp/services|
|Page Templates| src/main/webapp/ or src/main/webapp/admin |
|Component Templates| src/main/resources/org/appfuse/webapp/components|

NOTE: Using the base directory for abstract pages or component is not a requirement. However, make sure they are NOT under the pages or components directories because
Tapestry will attempt to enhance classes under the pages, compoents and mixins directories.


## Services ##

### Package: org.appfuse.webapp.services ###

  * _AppModule (New)_
> Standard Tapestry configuration module based on the filter name (app)

> In order to reuse existing code, we override the default location of the message catalog
> by contributing to the application default parameter with the following parameter: **SymbolConstants.APPLICATION\_CATALOG**

  * _ServiceFacade (New)_
> New interface encapsulating all Spring Managers and useful service calls

  * _ServiceFacadeImpl (New)_
> Implementation of the ServiceFacade interface

  * _ValidationDelegate (New)_
> New validation decorator to mimic T4/Dojo validation integration. This replaces the org.appfuse.webapp.ValidationDelegate

## Pages ##

### Package: org.appfuse.webapp.pages.base ###

  * _BasePage_
> This class was moved from org.appfuse.webapp.pages since it's not a page. In addition, some changes were made to leverage Java 5 features and handle the message catalog.

### Package: org.appfuse.webapp.pages ###

#### General ####

> The following steps were systematically applied to all pages:

  * transform this class into a non-abstract class first and remove all dependencies
  * Replace Spring managers with  "ServiceFacade" service, a wrapper around all injected managers
  * Add private properties and accessors to Java class if previously defined in page spec file
  * For page navigation, inject page, set properties and return page
  * For event handlers, we use naming convention. This can easily be replaced by annotations if you prefer.


#### Details ####
Here is quick summary of each page update. The pages are listed in alphabetical order.

  * _AccessDenied_  (New Class)
> This new class was introduced to handle 403 errors

  * _Error_  (New Class)
> This class implements the ExceptionReport interface to easily overwrite the default Tapestry exception report page

  * _FileDisplay_
> > This page now uses the new tapestry-upload module. The general steps listed above were applied

  * _FileUpload_
> > Steps similar to the FileDisplay class  were applied

The event handler methods are updated according to the table below. Please note that we could have also used the @OnEvent annotation, but we decided to use naming convention over configuration instead.

Inject Page
|Tapestry 4 |Tapestry 5 (no annotations) |
|:----------|:---------------------------|
|void cancel() |Object onCancel() |
|void upload(IRequestCycle) |Object onSuccess() |

  * _Index_  (New Class)

> This is the general index page and is used to redirect the request to the proper page

  * _Login_  (New Class)
> This class replaces the login.jsp page

  * _Logout_  (New Class)
> This class replaces the logout.jsp page

  * _MainMenu_

Inject Page and ServiceFacade
|Tapestry 4 |Tapestry 5 |
|:----------|:----------|
|void editProfile() |Object onActionFromEditProfile() |

  * _NotFound_  (New Class)
> This class handles 401 errors


  * _PasswordHint_

> . Inject Page and ServiceFacade

|Tapestry 4 |Tapestry 5 |
|:----------|:----------|
|void pageBeginRender(PageEvent) |void beginRender() |
|void execute() |Object onActivate(String) |


  * _SignupForm_ (Renamed)

> - This class was renamed to Signup.
I initially planned to do minimal changes to the pages, however as time went on, I thought It would be better to update
these to be embrace Tapestry naming convention and be more consistent throughout the application.

  * _UserForm_ (Renamed)

> This class was renamed to UserEdit.

> Convert listeners to event handler methods.
|Tapestry 4 |Tapestry 5 |
|:----------|:----------|
|void pageBeginRender(PageEvent) |void beginRender() |
|void cancel() |Object onCancel() |
|void save(IRequestCycle) |void onPrepare() / Object onSuccess() / void onValidateForm() |
|void delete(IRequestCycle) |void onSelected() / Object onDelete() |



  * ~~_MockPageService_~~
> Deleted. No longer needed

  * ~~_MockRequestCycle_~~
> Deleted. No longer needed

  * ~~_CountryModel_~~ and ~~_OptionsModel_~~
> These are not page artifacts, therefore don't belong to the pages package.
In fact, both classes were no longer needed and were therefore deleted.
The "ServiceFacade" interface contains methods to handle the labels.


### Package: org.appfuse.webapp.pages.admin ###

  * _ActiveUsers_ (New)

  * _ClickStreams_ (New)

  * _Reload_
Inject MainMenu page and ComponentResources to generate Links
|Tapestry 4 |Tapestry 5 |
|:----------|:----------|
|void execute(IRequestCycle) |Object onActivate() |

  * _UserList_
> Inject UserEdit page

|Tapestry 4 |Tapestry 5 |
|:----------|:----------|
|void edit(IRequestCycle) |Object onActionFromEdit(long id) |

  * _ViewStream_ (New)


## Components ##

### Package: org.appfuse.webapp.components ###

> When tackling T4 components (jwc + page), follow these steps:
    * create Java class if does not exist
    * Replace properties with private instances in Java class
    * Use injection or parameters whenever necessary

Below are the changes in alphabetical order:

  * _CheckboxSelect_ (New)
> > This will replace the  CheckBoxMultiplePropertySelectionRenderer

  * _HashedPasswordField_ (New)
> > The standard password component doesn't allow displaying the values. Since the values are encrypted, we can safely modify the original component to fit our need.


  * _Layout_ (New)
> > Wrapper for look and feel.

  * _Flash_ (New)
> > This component adds a "flash" message in the layout component.

  * _ShowError_ (New)
> > These is based on the T4 jwc and page template.

  * _ShowMessage_ (New)
> > These is based on the T4 jwc and page template.

  * _ShowValidationMessage_ (New)
> > These is based on the T4 jwc and page template.


  * _UserForm_
> > There were lot of similarities between the original SignupForm and UserForm pages.

> This component was introduce to encapsulate all common features (DRY)


  * ~~_FieldLabel_~~
> Deleted. replaced by Label component in T5


  * ~~_CheckBoxMultiplePropertySelectionRenderer_~~
> Deleted.

  * ~~_HiddenMultiplePropertySelectionRenderer_~~
> Deleted

  * ~~_ValidationDelegate_~~
> Deleted. This is not a component and is replaced by the validation decorator.

## Misc. ##


### Package: org.appfuse.webapp.data ###
  * FileData (New)
> > Wrapper around properties used in FileUpload and FileDisplay classes

  * FlashMessage (New)
> > Wrapper around flash display text and type

  * Visit (Renamed)

> . ASO Visit renamed UserSession (the name "Visit" was used for historical reasons i.e. Tapestry 3)

### Package: org.appfuse.webapp.util ###

> MessageUtil
> > Utility class to convert from MessageFormat to String.format



# Getting Your Hands Dirty: Templates and Resources #

## Page templates ##
  * Move page templates to
```
<ROOT>/src/main/webapp
```
or
```
<ROOT>/src/main/resources/org/appfuse/webapp/pages
```


  * Wrap all page templates with layout component

```
   <t:layout xmlns:t="http://tapestry.apache.org/schema/tapestry_5_0_0.xsd">
    ...

   </t:layout>
```
## Component Templates ##
  * Move component templates to
```
    <ROOT>/src/main/resources/org/appfuse/webapp/components
```
  * Wrap all templates with tapestry namespace
```
   <html xmlns:t="http://tapestry.apache.org/schema/tapestry_5_0_0.xsd">
   <head>
     ...
   </head>
    <t:body>
       ....
    </t:body>
   </html>
```

  * Main changes
    1. `ognl:` prefix replace by default prop: (in general)
    1. `<span key="value"/>` idiom replaced by ${message:value}
    1. @Any removed and replaced by value expansion
    1. @Insert replaced by expansion
    1. @TextField replaced by 

&lt;t:textfield&gt;


    1. @Body replaced by 

&lt;t:body&gt;


    1. @DirectLink replaced by 

&lt;t:actionlink&gt;

 or 

&lt;t:eventlink&gt;


    1. @PageLink replaced by 

&lt;t:pagelink&gt;


    1. @If replaced by 

&lt;t:if&gt;


    1. @FieldLabel replaced by 

&lt;t:label&gt;


    1. jwcid="@Label" removed
    1. File uploads are handled by a form enclosing a 

&lt;t:upload&gt;

: We are using the tapestry-upload module for this.
    1. istener are replaced by t:id and event handler in component class


## Spring Security ##

  * Update Spring Security file to reference new pages
```
<ROOT>/src/main/webapp/WEB-INF/security.xml
```

## Resources ##


> . The resource files are typically named after the filter name and located under WEB-INF. For example,  **app`*`.properties**
In order to re-use the existing configuration, we can specify application.catalog in the AppModule.

**NOTE: In future versions, the resources files will be renamed to enforce convention over configuration.**


## Unit Testing (More to come...) ##




The final source code can be found here http://code.google.com/p/tapestry5-appfuse/