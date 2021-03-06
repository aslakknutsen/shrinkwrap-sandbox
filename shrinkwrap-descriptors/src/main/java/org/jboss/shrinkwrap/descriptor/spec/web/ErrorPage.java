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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dan Allen
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "error-pageType", propOrder = {
      "errorCode",
      "exceptionClass",
      "location"
})
public class ErrorPage
{
   // FIXME this should be an enum
   // @see HttpServletResponse
   @XmlElement(name = "error-code")
   protected Integer errorCode;
   
   @XmlElement(name = "exception-type")
   protected String exceptionClass;
   
   @XmlElement(required = true)
   protected String location;

   public ErrorPage() {}
   
   public ErrorPage(int errorCode, String location)
   {
      this.errorCode = errorCode;
      this.location = location;
   }
   
   public ErrorPage(String exceptionClass, String location)
   {
      this.exceptionClass = exceptionClass;
      this.location = location;
   }
   
   public Integer getErrorCode()
   {
      return errorCode;
   }

   public void setErrorCode(Integer errorCode)
   {
      this.errorCode = errorCode;
   }

   public String getExceptionClass()
   {
      return exceptionClass;
   }

   public void setExceptionClass(String exceptionClazz)
   {
      this.exceptionClass = exceptionClazz;
   }

   public String getLocation()
   {
      return location;
   }

   public void setLocation(String location)
   {
      this.location = location;
   }
}
