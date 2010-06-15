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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dan Allen
 */
public class WebAppDefTest
{
   @Test
   public void testValidDef() throws Exception
   {
      WebApp webApp = new WebAppDef()
            .moduleName("test")
            .description("A description of my webapp")
            .displayName("Sample")
            .distributable()
            .contextParam("javax.faces.PROJECT_STAGE", "Development")
            .listener("org.jboss.seam.servlet.SeamListener")
            .filter("UrlRewriteFilter", "org.tuckey.web.filters.urlrewrite.UrlRewriteFilter", new String[] {"/*"}).initParam("one", "two")
            .servlet("Faces Servlet", "javax.faces.webapp.FacesServlet", new String[] {"*.jsf"})
            .welcomeFile("/index.jsf")
            .sessionTimeout(60)
            .errorPage(500, "/500.jsp")
            .errorPage(IOException.class, "/outputError.jsp")
            .formLoginConfig("/login.jsp", "/invalidLogin.jsp")
            .securityConstraint()
                  .webResourceCollection("All Access", "/public/*", "DELETE", "PUT", "HEAD", "OPTIONS", "TRACE", "GET", "POST")
                  .userDataConstraint("NONE")
            .securityConstraint("Restricted GET To Employees")
                  .webResourceCollection("Restricted Access - Get Only", "/restricted/employee/*", "GET")
                  .authConstraint("Employee")
                  .userDataConstraint("NONE")
            .securityConstraint("excluded")
                   .webResourceCollection("No Access").urlPatterns("/protected/*", "/templates/*")
                   .authConstraint()
                   .userDataConstraint("NONE")
            .securityRole("Employee", "Employees of the company")
            .descriptor();
      
      ByteArrayOutputStream actual = marshal(webApp);
      System.out.println(actual.toString());

      ByteArrayOutputStream expected = getResourceContents("/test-web.xml");
      
      Assert.assertEquals(expected.toString(), actual.toString());
   }
   
   @Test
   public void testDefaultFilterName() throws Exception
   {
      WebApp webApp = new WebAppDef()
            .filter("org.tuckey.web.filters.urlrewrite.UrlRewriteFilter", "/*")
            .descriptor();
      
      ByteArrayOutputStream actual = marshal(webApp);
      System.out.println(actual.toString());
      
      ByteArrayOutputStream expected = getResourceContents("/test-filter-web.xml");
      
      Assert.assertEquals(expected.toString(), actual.toString());
   }
   
   @Test
   public void testDefaultServletName() throws Exception
   {
      WebApp webApp = new WebAppDef()
            .servlet("javax.faces.webapp.FacesServlet", "*.jsf")
            .descriptor();
      
      ByteArrayOutputStream actual = marshal(webApp);
      System.out.println(actual.toString());
      
      ByteArrayOutputStream expected = getResourceContents("/test-servlet-web.xml");
      
      Assert.assertEquals(expected.toString(), actual.toString());
   }
   
   @Test
   public void testRootAttributes() throws Exception
   {
      WebApp webApp = new WebAppDef()
            .version("2.5")
            .metadataComplete()
            .descriptor();
      
      ByteArrayOutputStream actual = marshal(webApp);
      System.out.println(actual.toString());
      
      ByteArrayOutputStream expected = getResourceContents("/test-attributes-web.xml");
      
      Assert.assertEquals(expected.toString(), actual.toString());
   }
   
   private ByteArrayOutputStream marshal(WebApp webApp) throws Exception
   {
      JAXBContext context = JAXBContext.newInstance(WebApp.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, webApp.getSchemaLocation());
      
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      m.marshal(webApp, bos);
      return bos;
   }
   
   private ByteArrayOutputStream getResourceContents(String resource) throws Exception
   {
      InputStream in = getClass().getResourceAsStream(resource);
      
      int bufferSize = 4096;
      final ByteArrayOutputStream out = new ByteArrayOutputStream(bufferSize * 2);
      final byte[] buffer = new byte[bufferSize];
      int read = 0;
      try {
         while (((read = in.read(buffer)) != -1)) {
            out.write(buffer, 0, read);
         }
      }
      finally {
         in.close();
      }

      return out;
   }
}
