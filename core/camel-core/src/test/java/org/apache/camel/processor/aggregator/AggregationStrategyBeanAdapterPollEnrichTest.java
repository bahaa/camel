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
package org.apache.camel.processor.aggregator;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

public class AggregationStrategyBeanAdapterPollEnrichTest extends ContextTestSupport {

    private final MyBodyAppender appender = new MyBodyAppender();

    @Test
    public void testNoData() throws Exception {
        getMockEndpoint("mock:result").expectedBodiesReceived("A");

        template.sendBody("direct:start", "A");

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testData() throws Exception {
        template.sendBody("seda:foo", "B");

        getMockEndpoint("mock:result").expectedBodiesReceived("AB");

        template.sendBody("direct:start", "A");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start").pollEnrich("seda:foo", 100, AggregationStrategies.bean(appender)).to("mock:result");
            }
        };
    }

    public static final class MyBodyAppender {

        public String append(String existing, String next) {
            return existing + next;
        }

    }
}
