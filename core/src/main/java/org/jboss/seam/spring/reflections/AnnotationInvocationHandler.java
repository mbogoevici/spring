/*
 * JBoss, Home of Professional Open Source
 * Copyright [2011], Red Hat, Inc., and individual contributors
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

package org.jboss.seam.spring.reflections;

import org.springframework.core.convert.ConversionService;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link InvocationHandler} implementation for dynamically instantiating annotations
 *
 * @author: Marius Bogoevici
 */
public class AnnotationInvocationHandler implements InvocationHandler {

    private Map<String, Object> registeredValues = new HashMap<String, Object>();

    private ConversionService conversionService;

    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void setAttributes(Map<String, Object> registeredValues) {
        this.registeredValues = registeredValues;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (registeredValues.containsKey(method.getName())) {
            return conversionService.convert(registeredValues.get(method.getName()), method.getReturnType());
        } else {
            return method.getDefaultValue();
        }

    }
}
