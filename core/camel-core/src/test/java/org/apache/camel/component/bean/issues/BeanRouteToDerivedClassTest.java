/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.bean.issues;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.Registry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BeanRouteToDerivedClassTest extends ContextTestSupport {

    private final DerivedClass derived = new DerivedClass();

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Test
    public void testDerivedClassCalled() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("bean:derived?method=process");
            }
        });
        context.start();

        template.sendBody("direct:start", "Hello World");

        assertEquals("Hello World", derived.getAndClearBody(), "Derived class should have been invoked");
    }

    @Test
    public void testDerivedClassCalledWithNoCustomProcessor() throws Exception {
        context.getTypeConverterRegistry().addTypeConverter(Processor.class, MyMessageListener.class,
                new MyMessageToProcessorConverter());

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("bean:derived?method=process");

                from("direct:other").to("bean:derived");
            }
        });
        context.start();

        Object out = template.requestBody("direct:start", "Hello World");
        assertEquals("Hello World", derived.getAndClearBody(), "Derived class should have been invoked");
        assertEquals("Hello World", out.toString());

        out = template.requestBody("direct:other", new MyMessage("Hello World"));
        assertNull(derived.getAndClearBody(), "Derived class should NOT have been invoked");
        assertEquals("Bye World", out.toString());

        out = template.requestBody("direct:other", new MyMessage("Hello Again"));
        assertNull(derived.getAndClearBody(), "Derived class should NOT have been invoked");
        assertEquals("Bye World", out.toString());
    }

    @Test
    public void testDerivedClassCalledWithCustomProcessor() throws Exception {
        context.getTypeConverterRegistry().addTypeConverter(Processor.class, MyMessageListener.class,
                new MyMessageToProcessorConverter());

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Explicit method name given so always call this
                from("direct:start").to("bean:derived?method=process");

                // no explicit method name then a custom processor can kick in
                from("direct:other").to("bean:derived");
            }
        });
        context.start();

        Object out = template.requestBody("direct:start", new MyMessage("Hello World"));
        assertEquals("Hello World", derived.getAndClearBody(), "Derived class should have been invoked");
        assertEquals("Hello World", out.toString());

        out = template.requestBody("direct:other", new MyMessage("Hello World"));
        assertNull(derived.getAndClearBody(), "Derived class should NOT have been invoked");
        assertEquals("Bye World", out.toString());

        out = template.requestBody("direct:other", new MyMessage("Hello Again"));
        assertNull(derived.getAndClearBody(), "Derived class should NOT have been invoked");
        assertEquals("Bye World", out.toString());
    }

    @Override
    protected Registry createCamelRegistry() throws Exception {
        Registry jndi = super.createCamelRegistry();
        jndi.bind("derived", derived);
        return jndi;
    }

}
