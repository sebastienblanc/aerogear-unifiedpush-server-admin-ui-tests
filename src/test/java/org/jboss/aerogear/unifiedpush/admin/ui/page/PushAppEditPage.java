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

import static org.jboss.aerogear.unifiedpush.admin.ui.utils.StringUtilities.isEmpty;
import static org.jboss.aerogear.unifiedpush.admin.ui.utils.WebElementUtils.clearNfill;
import static org.jboss.arquillian.graphene.Graphene.element;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class PushAppEditPage extends PushServerAdminUiPage {

    @FindBy(tagName = "form")
    private WebElement FORM;

    @FindBy(jquery = "form input[type=\"submit\"]")
    private WebElement SUBMIT_BUTTON;

    @FindBy(jquery = "form input[type=\"reset\"]")
    private WebElement CANCEL_BUTTON;

    @FindBy(jquery = "form p:eq(0) input[type=\"text\"]")
    private WebElement NAME_FIELD;

    @FindBy(jquery = "form p:eq(1) textarea")
    private WebElement DESCRIPTION_FIELD;

    private static final String PAGE_URL = "#/mobileApps/edit/undefined";

    public String getPageURL() {
        return PAGE_URL;
    }

    public String getName() {
        return NAME_FIELD.getAttribute("value");
    }

    public String getDescription() {
        return DESCRIPTION_FIELD.getAttribute("value");
    }

    public void registerNewPushApp(String name, String desc) {
        fillForm(name, desc);
        SUBMIT_BUTTON.click();
    }

    public void updatePushApp(String name, String desc) {
        if (!isEmpty(name)) {
            clearNfill(NAME_FIELD, name);
        }
        if (!isEmpty(desc)) {
            clearNfill(DESCRIPTION_FIELD, desc);
        }
        SUBMIT_BUTTON.click();
    }

    public void cancel() {
        CANCEL_BUTTON.click();
    }

    private void fillForm(String name, String desc) {
        clearNfill(NAME_FIELD, name);
        clearNfill(DESCRIPTION_FIELD, desc);
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until(element(CANCEL_BUTTON).isVisible());
        waitModel().until(element(SUBMIT_BUTTON).isVisible());
    }
}
