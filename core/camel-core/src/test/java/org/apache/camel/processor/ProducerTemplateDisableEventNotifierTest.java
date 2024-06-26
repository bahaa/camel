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
package org.apache.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.ContextTestSupport;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProducerTemplateDisableEventNotifierTest extends ContextTestSupport {

    private final MySentEventNotifier notifier = new MySentEventNotifier();

    @Override
    protected CamelContext createCamelContext() throws Exception {
        DefaultCamelContext context = (DefaultCamelContext) super.createCamelContext();
        context.getManagementStrategy().addEventNotifier(notifier);
        return context;
    }

    @Test
    public void testExchangeSent() throws Exception {
        getMockEndpoint("mock:result").expectedMessageCount(1);

        // we dont want events for producer template itself so disable that
        ProducerTemplate other = context.createProducerTemplate();
        other.setEventNotifierEnabled(false);

        other.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();

        assertEquals(2, notifier.getEvents().size());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").to("direct:bar").to("mock:result");

                from("direct:bar").delay(1);
            }
        };
    }

}
