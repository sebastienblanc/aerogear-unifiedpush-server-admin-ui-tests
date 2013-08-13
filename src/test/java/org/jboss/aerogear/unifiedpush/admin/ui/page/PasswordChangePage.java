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

import static org.jboss.aerogear.unifiedpush.admin.ui.utils.WebElementUtils.clearNfill;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.WebElement;

public class PasswordChangePage extends PushServerAdminUiPage {

    @FindBy(jquery = "#other p:eq(0) input[type=\"password\"]")
    private WebElement PASSWORD_FIELD;

    @FindBy(jquery = "#other p:eq(1) input[type=\"password\"]")
    private WebElement CONFIRM_PASSWORD_FIELD;

    @FindBy(jquery = "#other button")
    private WebElement SUBMIT_BUTTON;

    public void changePassword(String password) {
        fillForm(password);
        guardXhr(SUBMIT_BUTTON).click();
    }

    protected void fillForm(String password) {
        clearNfill(PASSWORD_FIELD, password);
        clearNfill(CONFIRM_PASSWORD_FIELD, password);
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(SUBMIT_BUTTON).is().visible();
    }
}
