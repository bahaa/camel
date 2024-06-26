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
package org.apache.camel.impl;

import java.util.concurrent.TimeUnit;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.seda.SedaEndpoint;
import org.junit.jupiter.api.Test;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TwoRouteSuspendResumeTest extends ContextTestSupport {

    @Test
    public void testSuspendResume() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("A");

        template.sendBody("seda:foo", "A");

        assertMockEndpointsSatisfied();

        log.info("Suspending");

        // now suspend and dont expect a message to be routed
        resetMocks();
        mock.expectedMessageCount(0);

        MockEndpoint mockBar = getMockEndpoint("mock:bar");
        mockBar.expectedMessageCount(1);

        context.getRouteController().suspendRoute("foo");

        // need to give seda consumer thread time to idle
        await().atMost(1, TimeUnit.SECONDS).until(() -> {
            return context.getEndpoint("seda:foo", SedaEndpoint.class).getQueue().isEmpty();
        });

        // even though we wait for the queues to empty, there is a race condition where the consumer
        // may still process messages while it's being suspended due to asynchronous message handling.
        // as a result, we need to wait a bit longer to ensure that the seda consumer is suspended before
        // sending the next message.
        Thread.sleep(1000L);

        template.sendBody("seda:foo", "B");
        template.sendBody("direct:bar", "C");

        // we can still send a message to bar when foo route is suspended
        mockBar.assertIsSatisfied();
        mock.assertIsSatisfied(1000);

        assertEquals("Suspended", context.getRouteController().getRouteStatus("foo").name());
        assertEquals("Started", context.getRouteController().getRouteStatus("bar").name());

        log.info("Resuming");

        // now resume and expect the previous message to be routed
        resetMocks();
        mock.expectedBodiesReceived("B");
        context.getRouteController().resumeRoute("foo");
        assertMockEndpointsSatisfied();

        assertEquals("Started", context.getRouteController().getRouteStatus("foo").name());
        assertEquals("Started", context.getRouteController().getRouteStatus("bar").name());
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("seda:foo").routeId("foo").to("log:foo").to("mock:result");

                from("direct:bar").routeId("bar").to("log:bar").to("mock:bar");
            }
        };
    }
}
