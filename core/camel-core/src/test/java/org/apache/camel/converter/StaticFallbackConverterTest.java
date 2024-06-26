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
package org.apache.camel.converter;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.TimeZone;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StaticFallbackConverterTest extends ContextTestSupport {

    @Test
    public void testStaticFallbackConverter() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        TimeZone tz = TimeZone.getDefault();

        String money = context.getTypeConverter().convertTo(String.class, exchange, tz);
        assertEquals("Time talks", money);
    }

    @Test
    public void testStaticFallbackMandatoryConverter() throws Exception {
        Exchange exchange = new DefaultExchange(context);
        TimeZone tz = TimeZone.getDefault();

        String money = context.getTypeConverter().mandatoryConvertTo(String.class, exchange, tz);
        assertEquals("Time talks", money);
    }

    @Test
    public void testStaticFallbackMandatoryFailed() throws Exception {
        Exchange exchange = new DefaultExchange(context);

        try {
            context.getTypeConverter().mandatoryConvertTo(Date.class, exchange, new Timestamp(0));
            fail("Should have thrown an exception");
        } catch (NoTypeConversionAvailableException e) {
            // expected
        }
    }

    @Test
    public void testStaticFallbackFailed() throws Exception {
        Exchange exchange = new DefaultExchange(context);

        Date out = context.getTypeConverter().convertTo(Date.class, exchange, new Timestamp(0));
        assertNull(out);
    }

}
