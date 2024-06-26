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
package org.apache.camel.component.bean;

import java.io.IOException;

import org.apache.camel.ExchangeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeanWithExchangeExceptionAnnotationWrappedExceptionTest extends BeanWithExchangeExceptionAnnotationTest {

    @Override
    protected Object getBean() {
        return new MyBean();
    }

    public static class MyBean {
        private static final String MESSAGE
                = "I'm being thrown from " + BeanWithExchangeExceptionAnnotationWrappedExceptionTest.class;

        public void throwException() throws Exception {
            // wrap the problem in an IO exception
            throw new IOException("Forced", new MyCustomException(MESSAGE));
        }

        // to unit test that we can set a type to the @ExchangeException that we
        // want this caused by exception
        // in the exception hieracy
        public void handleException(@ExchangeException MyCustomException custom) {
            assertNotNull(custom);
            assertEquals(MESSAGE, custom.getMessage());
        }
    }
}
