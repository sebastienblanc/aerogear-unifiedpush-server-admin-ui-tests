/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.unifiedpush.admin.ui.page;

import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.openqa.selenium.JavascriptExecutor;

public class ConfirmationBoxPage {

    @ArquillianResource
    private JavascriptExecutor executor;

    private static final String FORCE_ACCEPTING_ALL_JS_CONFIRM_BOXES = "window.confirm = function(m) { return true; };";

    public void acceptConfirmBoxes() {
        executor.executeScript(JavaScript.fromString(FORCE_ACCEPTING_ALL_JS_CONFIRM_BOXES).getSourceCode(), new Object[0]);
    }

}
