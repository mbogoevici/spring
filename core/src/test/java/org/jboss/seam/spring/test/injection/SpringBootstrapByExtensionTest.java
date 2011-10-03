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

package org.jboss.seam.spring.test.injection;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.spring.bootstrap.SpringContext;
import org.jboss.seam.spring.test.bootstrap.ConfigurationContextProducer;
import org.jboss.seam.spring.utils.Locations;
import org.jboss.seam.spring.test.utils.ContextInjected;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;

import static org.jboss.seam.spring.test.utils.Dependencies.SEAM_SPRING_EXTENSION_LOCATION;
import static org.jboss.seam.spring.test.utils.Dependencies.springDependencies;
import static org.jboss.seam.spring.test.utils.Dependencies.corePackages;

/**
 * @author: Marius Bogoevici
 */
@RunWith(Arquillian.class)
public class SpringBootstrapByExtensionTest {

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource(SEAM_SPRING_EXTENSION_LOCATION)
                .addAsLibraries(springDependencies())
                .addPackages(true, corePackages())
                .addAsResource("org/jboss/seam/spring/test/bootstrap/applicationContext.xml")
                .addAsResource("org/jboss/seam/spring/test/bootstrap/metainf/org.jboss.seam.spring.contexts", Locations.SEAM_SPRING_CONTEXTS_LOCATION)
                .addClasses(SimpleBean.class, ComplicatedBean.class);
    }


    @Test
    public void testContextExists(@SpringContext ApplicationContext applicationContext) {
        Assert.assertNotNull(applicationContext);
    }

//    @Test
//    public void testCdiBeanInjectedWithSpringBean(SpringInjected springInjected) {
//        Assert.assertNotNull(springInjected);
//        Assert.assertNotNull(springInjected.simpleBean);
//        Assert.assertEquals(springInjected.simpleBean.getMessage(), "Hello");
//        Assert.assertNotNull(springInjected.complicatedBean);
//        Assert.assertNotNull(springInjected.complicatedBean.simpleBean);
//        Assert.assertEquals(springInjected.complicatedBean.simpleBean.getMessage(), "Hello");
//    }

}
