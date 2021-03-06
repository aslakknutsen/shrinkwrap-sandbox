/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.shrinkwrap.descriptor.spec.web;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.EventListener;

import javax.faces.application.ProjectStage;
import javax.faces.application.StateManager;
import javax.faces.webapp.FacesServlet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.jboss.shrinkwrap.api.Asset;
import org.jboss.shrinkwrap.descriptor.spec.web.LoginConfig.AuthMethodType;

/**
 * @author Dan Allen
 */
public class WebAppDef implements Asset
{
   protected WebApp webApp;
   
   public WebAppDef()
   {
      this(new WebApp());
   }
   
   public WebAppDef(WebApp webApp)
   {
      this.webApp = webApp;
   }
   
   public WebAppDef version(String version)
   {
      webApp.setVersion(version);
      return this;
   }
   
   public WebAppDef metadataComplete()
   {
      webApp.setMetadataComplete(true);
      return this;
   }
   
   public WebAppDef moduleName(String name)
   {
      webApp.setModuleName(name);
      return this;
   }
   
   public WebAppDef description(String description)
   {
      webApp.getDescriptions().add(new LocalizedText(description));
      return this;
   }
   
   public WebAppDef displayName(String displayName)
   {
      webApp.getDisplayNames().add(new LocalizedText(displayName));
      return this;
   }
   
   public WebAppDef distributable()
   {
      webApp.setDistributable(true);
      return this;
   }
   
   public WebAppDef contextParam(String name, Object value)
   {
      webApp.getContextParams().add(new Param(name, value.toString()));
      return this;
   }
   
   public WebAppDef facesDevelopmentMode()
   {
      return contextParam(ProjectStage.PROJECT_STAGE_PARAM_NAME, ProjectStage.Development.toString());
   }
   
   // TODO continue with other known parameters
   public WebAppDef facesStateSavingMethod(String value)
   {
      return contextParam(StateManager.STATE_SAVING_METHOD_PARAM_NAME, value);
   }
   
   public WebAppDef facesConfigFiles(String... paths)
   {
      // poor man's way of doing join
      String v = Arrays.asList(paths).toString();
      if (v.length() == 2)
      {
         return this;
      }
      return contextParam(FacesServlet.CONFIG_FILES_ATTR, v.substring(0, v.length() - 1));
   }
   
   public WebAppDef listener(Class<? extends EventListener> clazz)
   {
      return listener(clazz.getName());
   }
   
   public WebAppDef listener(String clazz)
   {
      webApp.getListeners().add(new Listener(clazz));
      return this;
   }
   
   public WebAppDef filter(Class<? extends javax.servlet.Filter> clazz, String... urlPatterns)
   {
     return filter(clazz.getSimpleName(), clazz.getName(), urlPatterns);
   }
   
   public FilterDef filter(String clazz, String... urlPatterns)
   {
      return filter(getSimpleName(clazz), clazz, urlPatterns);
   }
   
   public WebAppDef filter(String name, Class<? extends javax.servlet.Filter> clazz, String[] urlPatterns)
   {
     return filter(name, clazz.getName(), urlPatterns);
   }
   
   public FilterDef filter(String name, String clazz, String[] urlPatterns)
   {
      Filter filter = new Filter(name, clazz);
      webApp.getFilters().add(filter);
      for (String p : urlPatterns) {
         webApp.getFilterMappings().add(new FilterMapping(name, p));
      }
      return new FilterDef(webApp, filter);
   }

   public WebAppDef servlet(Class<? extends javax.servlet.Servlet> clazz, String... urlPatterns)
   {
      return servlet(clazz.getSimpleName(), clazz.getName(), urlPatterns);
   }
   
   public WebAppDef servlet(String clazz, String... urlPatterns)
   {
      return servlet(getSimpleName(clazz), clazz, urlPatterns);
   }
   
   public WebAppDef servlet(String name, Class<? extends javax.servlet.Servlet> clazz, String[] urlPatterns)
   {
      return servlet(name, clazz.getName(), urlPatterns);
   }
   
   public WebAppDef servlet(String name, String clazz, String[] urlPatterns)
   {
      webApp.getServlets().add(new Servlet(name, clazz));
      webApp.getServletMappings().add(new ServletMapping(name, urlPatterns));
      return this;
   }
   
   public WebAppDef facesServlet()
   {
      return servlet(FacesServlet.class, "*.jsf");
   }
   
   public WebAppDef welcomeFiles(String... servletPaths)
   {
      for (String p : servletPaths)
      {
         webApp.getWelcomeFiles().add(p);
      }
      return this;
   }
   
   public WebAppDef welcomeFile(String servletPath)
   {
      return welcomeFiles(servletPath);
   }
   
   public WebAppDef sessionTimeout(int timeout)
   {
      webApp.getSessionConfig().setTimeout(timeout);
      return this;
   }
   
   public WebAppDef sessionTrackingModes(SessionConfig.TrackingModeType... sessionTrackingModes)
   {
      for (SessionConfig.TrackingModeType m : sessionTrackingModes)
      {
         webApp.getSessionConfig().getTrackingModes().add(m);
      }
      return this;
   }
   
   public CookieConfigDef sessionCookieConfig()
   {
      return new CookieConfigDef(webApp);
   }
   
   public WebAppDef errorPage(int errorCode, String location)
   {
      webApp.getErrorPages().add(new ErrorPage(errorCode, location));
      return this;
   }
   
   public WebAppDef errorPage(String exceptionClass, String location)
   {
      webApp.getErrorPages().add(new ErrorPage(exceptionClass, location));
      return this;
   }
   
   public WebAppDef errorPage(Class<? extends Throwable> exceptionClass, String location)
   {
      return errorPage(exceptionClass.getName(), location);
   }
   
   public WebAppDef loginConfig(AuthMethodType authMethod, String realmName)
   {
      return loginConfig(authMethod.toString(), realmName);
   }
   
   public WebAppDef loginConfig(String authMethod, String realmName)
   {
      webApp.getLoginConfig().add(new LoginConfig(authMethod, realmName));
      return this;
   }
   
   public WebAppDef formLoginConfig(String loginPage, String errorPage)
   {
      webApp.getLoginConfig().add(new LoginConfig(AuthMethodType.FORM.toString(), new FormLoginConfig(loginPage, errorPage)));
      return this;
   }
   
   public SecurityConstraintDef securityConstraint()
   {
      return securityConstraint(null);
   }
   
   public SecurityConstraintDef securityConstraint(String displayName)
   {
      SecurityConstraint securityConstraint = new SecurityConstraint();
      if (displayName != null)
      {
         securityConstraint.getDisplayNames().add(new LocalizedText(displayName));
      }
      webApp.getSecurityConstraints().add(securityConstraint);
      return new SecurityConstraintDef(webApp, securityConstraint);
   }
   
   public WebAppDef securityRole(String roleName)
   {
      webApp.getSecurityRoles().add(new SecurityRole(roleName));
      return this;
   }
   
   public WebAppDef securityRole(String roleName, String description)
   {
      webApp.getSecurityRoles().add(new SecurityRole(roleName, description));
      return this;
   }
   
   public WebAppDef absoluteOrdering(boolean others, String... names)
   {
      webApp.getAbsoluteOrdering().add(new AbsoluteOrdering(others, names));
      return this;
   }
   
   public WebAppDef absoluteOrdering(String... names)
   {
      webApp.getAbsoluteOrdering().add(new AbsoluteOrdering(names));
      return this;
   }
   
   public WebApp descriptor()
   {
      return webApp;
   }
   
   @Override
   public InputStream openStream()
   {
      try
      {
         JAXBContext context = JAXBContext.newInstance(WebApp.class);
         Marshaller m = context.createMarshaller();
         m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, webApp.getSchemaLocation());
         m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
         m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
         ByteArrayOutputStream os = new ByteArrayOutputStream();
         m.marshal(webApp, os);
         return new ByteArrayInputStream(os.toByteArray());
      }
      catch (Exception e)
      {
         throw new RuntimeException("Could not convert descriptor to XML", e);
      }
   }
   
   private String getSimpleName(String fqcn)
   {
      if (fqcn.indexOf('.') >= 0)
      {
         return fqcn.substring(fqcn.lastIndexOf('.') + 1);
      }
      return fqcn;
   }
}
