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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spi.CamelEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepEventNotifierTest extends ContextTestSupport {

    private final MyEventNotifier notifier = new MyEventNotifier();

    @Test
    public void testStepEventNotifier() throws Exception {
        context.addService(notifier);
        context.getManagementStrategy().addEventNotifier(notifier);

        assertEquals(0, notifier.getEvents().size());

        getMockEndpoint("mock:foo").expectedMessageCount(1);
        getMockEndpoint("mock:bar").expectedMessageCount(1);
        getMockEndpoint("mock:result").expectedMessageCount(1);

        template.sendBody("direct:start", "Hello World");

        assertMockEndpointsSatisfied();

        assertEquals(4, notifier.getEvents().size());
        assertIsInstanceOf(CamelEvent.StepStartedEvent.class, notifier.getEvents().get(0));
        assertIsInstanceOf(CamelEvent.StepCompletedEvent.class, notifier.getEvents().get(1));
        assertIsInstanceOf(CamelEvent.StepStartedEvent.class, notifier.getEvents().get(2));
        assertIsInstanceOf(CamelEvent.StepCompletedEvent.class, notifier.getEvents().get(3));
        assertEquals("foo", ((CamelEvent.StepEvent) notifier.getEvents().get(0)).getStepId());
        assertEquals("foo", ((CamelEvent.StepEvent) notifier.getEvents().get(1)).getStepId());
        assertEquals("bar", ((CamelEvent.StepEvent) notifier.getEvents().get(2)).getStepId());
        assertEquals("bar", ((CamelEvent.StepEvent) notifier.getEvents().get(3)).getStepId());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").step("foo").to("log:foo").to("mock:foo").end().step("bar").to("log:bar").to("mock:bar")
                        .end().to("mock:result");
            }
        };
    }

    private static class MyEventNotifier extends EventNotifierSupport {

        private final List<CamelEvent> events = new ArrayList<>();

        public MyEventNotifier() {
            setIgnoreCamelContextEvents(true);
            setIgnoreExchangeEvents(true);
            setIgnoreRouteEvents(true);
            setIgnoreServiceEvents(true);
            setIgnoreStepEvents(false);
        }

        @Override
        public void notify(CamelEvent event) throws Exception {
            events.add(event);
        }

        public List<CamelEvent> getEvents() {
            return events;
        }
    }
}
