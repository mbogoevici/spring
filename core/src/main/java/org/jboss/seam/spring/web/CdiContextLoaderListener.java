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
package org.jboss.seam.spring.web;

import org.jboss.seam.spring.context.SpringContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AbstractRefreshableWebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * Variant of {@link ContextLoaderListener} that re-exposes a Spring {@link ApplicationContext}
 * bootstrapped by the CDI extension as a {@link WebApplicationContext}.
 *
 * The listener will create an empty {@link WebApplicationContext} with the CDI-bootstrapped
 * {@link ApplicationContext} as its parent.
 *
 * @author Marius Bogoevici
 */
public class CdiContextLoaderListener extends ContextLoaderListener {

    @Inject @SpringContext(name = "default") ApplicationContext context;

    @Override
    protected ApplicationContext loadParentContext(ServletContext servletContext) {
       return context;
    }


    @Override
    protected WebApplicationContext createWebApplicationContext(ServletContext sc, ApplicationContext parent) {
        if (sc.getInitParameter(CONFIG_LOCATION_PARAM) != null ||
                new ServletContextResource(sc, XmlWebApplicationContext.DEFAULT_CONFIG_LOCATION).exists()) {
            if (parent != null) {
                throw new ApplicationContextException("Cannot set CDI bootstrapped ApplicationContext as parent, " +
                        "root WebApplicationContext already has one set");
            }
            return super.createWebApplicationContext(sc, context);
        }
        ConfigurableWebApplicationContext configurableWebApplicationContext = new WrapperWebApplicationContext();
        configurableWebApplicationContext.setServletContext(sc);
        configurableWebApplicationContext.setParent(context);
        configurableWebApplicationContext.refresh();
        return configurableWebApplicationContext;
    }

    private static class WrapperWebApplicationContext extends AbstractRefreshableWebApplicationContext {
        @Override
        protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
            // no bean definitions
        }
    }
}

