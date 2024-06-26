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
package org.apache.camel.processor.onexception;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.Registry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DefaultErrorHandlerOnExceptionOccurredProcessorTest extends ContextTestSupport {

    @Override
    protected Registry createCamelRegistry() throws Exception {
        Registry jndi = super.createCamelRegistry();
        jndi.bind("myProcessor", new MyProcessor());
        return jndi;
    }

    @Test
    public void testOnExceptionOccurred() throws Exception {
        try {
            template.sendBody("direct:start", "Hello World");
            fail("Should throw exception");
        } catch (Exception e) {
            // expected
        }

        MyProcessor myProcessor = context.getRegistry().lookupByNameAndType("myProcessor", MyProcessor.class);
        // 1 = first time + 0 redelivery attempts
        assertEquals(1, myProcessor.getInvoked());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                MyProcessor myProcessor = context.getRegistry().lookupByNameAndType("myProcessor", MyProcessor.class);

                errorHandler(defaultErrorHandler().onExceptionOccurred(myProcessor));

                from("direct:start").to("log:a").to("direct:foo").to("log:b");

                from("direct:foo").throwException(new IllegalArgumentException("Forced"));
            }
        };
    }

    public static class MyProcessor implements Processor {

        private int invoked;

        @Override
        public void process(Exchange exchange) throws Exception {
            invoked++;
        }

        public int getInvoked() {
            return invoked;
        }
    }

}
