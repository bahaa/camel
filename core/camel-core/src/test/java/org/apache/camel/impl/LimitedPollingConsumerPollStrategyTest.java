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

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.engine.LimitedPollingConsumerPollStrategy;
import org.apache.camel.support.service.ServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisabledOnOs(architectures = { "s390x" },
              disabledReason = "This test does not run reliably on s390x (see CAMEL-21438)")
public class LimitedPollingConsumerPollStrategyTest extends ContextTestSupport {

    private LimitedPollingConsumerPollStrategy strategy;

    @Test
    public void testLimitedPollingConsumerPollStrategy() {
        Exception expectedException = new Exception("Hello");

        strategy = new LimitedPollingConsumerPollStrategy();
        strategy.setLimit(3);

        final Endpoint endpoint = getMockEndpoint("mock:foo");
        MockScheduledPollConsumer consumer = new MockScheduledPollConsumer(endpoint, expectedException);
        consumer.setPollStrategy(strategy);

        consumer.start();
        assertTrue(consumer.isStarted(), "Should be started");

        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        consumer.run();
        assertTrue(consumer.isSuspended(), "Should be suspended");

        consumer.stop();
    }

    @Test
    public void testLimitAtTwoLimitedPollingConsumerPollStrategy() {
        Exception expectedException = new Exception("Hello");

        strategy = new LimitedPollingConsumerPollStrategy();
        strategy.setLimit(2);

        final Endpoint endpoint = getMockEndpoint("mock:foo");
        MockScheduledPollConsumer consumer = new MockScheduledPollConsumer(endpoint, expectedException);
        consumer.setPollStrategy(strategy);

        consumer.start();
        assertTrue(consumer.isStarted(), "Should still be started");

        consumer.run();
        consumer.run();
        assertTrue(consumer.isSuspended(), "Should be suspended");

        consumer.stop();
    }

    @Test
    public void testLimitedPollingConsumerPollStrategySuccess() {
        Exception expectedException = new Exception("Hello");

        strategy = new LimitedPollingConsumerPollStrategy();
        strategy.setLimit(3);

        final Endpoint endpoint = getMockEndpoint("mock:foo");
        MockScheduledPollConsumer consumer = new MockScheduledPollConsumer(endpoint, expectedException);
        consumer.setPollStrategy(strategy);

        consumer.start();
        assertTrue(consumer.isStarted(), "Should be started");

        consumer.run();
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");

        // now force success
        consumer.setExceptionToThrowOnPoll(null);
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");

        consumer.stop();
    }

    @Test
    public void testLimitedPollingConsumerPollStrategySuccessThenFail() {
        Exception expectedException = new Exception("Hello");

        strategy = new LimitedPollingConsumerPollStrategy();
        strategy.setLimit(3);

        final Endpoint endpoint = getMockEndpoint("mock:foo");
        MockScheduledPollConsumer consumer = new MockScheduledPollConsumer(endpoint, expectedException);
        consumer.setPollStrategy(strategy);

        consumer.start();

        // fail 2 times
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");

        // now force success 2 times
        consumer.setExceptionToThrowOnPoll(null);
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");

        // now fail again, after hitting limit at 3
        consumer.setExceptionToThrowOnPoll(expectedException);
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        consumer.run();
        assertTrue(consumer.isSuspended(), "Should be suspended");

        consumer.stop();
    }

    @Test
    public void testTwoConsumersLimitedPollingConsumerPollStrategy() {
        Exception expectedException = new Exception("Hello");

        strategy = new LimitedPollingConsumerPollStrategy();
        strategy.setLimit(3);

        final Endpoint endpoint = getMockEndpoint("mock:foo");
        MockScheduledPollConsumer consumer = new MockScheduledPollConsumer(endpoint, expectedException);
        consumer.setPollStrategy(strategy);

        MockScheduledPollConsumer consumer2 = new MockScheduledPollConsumer(endpoint, null);
        consumer2.setPollStrategy(strategy);

        consumer.start();
        consumer2.start();

        consumer.run();
        consumer2.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        assertTrue(consumer2.isStarted(), "Should still be started");
        consumer.run();
        consumer2.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        assertTrue(consumer2.isStarted(), "Should still be started");
        consumer.run();
        consumer2.run();
        assertTrue(consumer.isSuspended(), "Should be suspended");
        assertTrue(consumer2.isStarted(), "Should still be started");

        consumer.stop();
        consumer2.stop();
    }

    @Test
    public void testRestartManuallyLimitedPollingConsumerPollStrategy() {
        Exception expectedException = new Exception("Hello");

        strategy = new LimitedPollingConsumerPollStrategy();
        strategy.setLimit(3);

        final Endpoint endpoint = getMockEndpoint("mock:foo");
        MockScheduledPollConsumer consumer = new MockScheduledPollConsumer(endpoint, expectedException);
        consumer.setPollStrategy(strategy);

        consumer.start();

        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        consumer.run();
        assertTrue(consumer.isSuspended(), "Should be suspended");

        // now start the consumer again
        ServiceHelper.resumeService(consumer);

        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        consumer.run();
        assertTrue(consumer.isSuspended(), "Should be suspended");

        // now start the consumer again
        ServiceHelper.resumeService(consumer);
        // and let it succeed
        consumer.setExceptionToThrowOnPoll(null);
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");
        consumer.run();
        assertTrue(consumer.isStarted(), "Should still be started");

        consumer.stop();
    }

}
