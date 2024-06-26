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
package org.apache.camel.component.xslt;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.apache.camel.CamelContext;
import org.apache.camel.RuntimeCamelException;
import org.apache.camel.TestSupport;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 */
public class XsltCustomErrorListenerTest extends TestSupport {

    private final MyErrorListener listener = new MyErrorListener();

    private static class MyErrorListener implements ErrorListener {

        private boolean warning;
        private boolean error;
        private boolean fatalError;

        @Override
        public void warning(TransformerException exception) throws TransformerException {
            warning = true;
        }

        @Override
        public void error(TransformerException exception) throws TransformerException {
            error = true;
        }

        @Override
        public void fatalError(TransformerException exception) throws TransformerException {
            fatalError = true;
        }

        public boolean isWarning() {
            return warning;
        }

        public boolean isError() {
            return error;
        }

        public boolean isFatalError() {
            return fatalError;
        }
    }

    @Test
    public void testErrorListener() throws Exception {
        RouteBuilder builder = createRouteBuilder();
        CamelContext context = new DefaultCamelContext();
        context.getRegistry().bind("myListener", listener);
        context.addRoutes(builder);
        assertThrows(RuntimeCamelException.class, () -> context.start());

        assertFalse(listener.isWarning());
        assertTrue(listener.isError(), "My error listener should been invoked");
        assertTrue(listener.isFatalError(), "My error listener should been invoked");
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() throws Exception {
                from("seda:a").to("xslt:org/apache/camel/builder/xml/example-with-errors.xsl?errorListener=#myListener");
            }
        };
    }

}
