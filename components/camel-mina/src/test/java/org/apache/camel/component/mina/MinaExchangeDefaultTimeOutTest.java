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
package org.apache.camel.component.mina;

import org.apache.camel.builder.RouteBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * To test timeout.
 */
public class MinaExchangeDefaultTimeOutTest extends BaseMinaTest {

    @Test
    public void testDefaultTimeOut() {
        String result = (String) assertDoesNotThrow(() -> template
                .requestBody(String.format("mina:tcp://localhost:%1$s?textline=true&sync=true", getPort()), "Hello World"),
                "Should not get a RuntimeCamelException");
        assertEquals("Okay I will be faster in the future", result);
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {

            public void configure() {
                fromF("mina:tcp://localhost:%1$s?textline=true&sync=true", getPort()).process(e -> {
                    assertEquals("Hello World", e.getIn().getBody(String.class));
                    // just be a little bit slow
                    Thread.sleep(250);
                    e.getMessage().setBody("Okay I will be faster in the future");
                });
            }
        };
    }
}
