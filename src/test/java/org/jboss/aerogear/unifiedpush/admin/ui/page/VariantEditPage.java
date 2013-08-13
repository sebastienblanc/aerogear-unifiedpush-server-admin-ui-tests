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

public abstract class VariantEditPage extends PushServerAdminUiPage {

    @FindBy(jquery = "div.rcue-dialog-inner form p:eq(0) input[name=\"name\"]")
    private WebElement VARIANT_NAME;

    @FindBy(jquery = "div.rcue-dialog-inner form p:eq(1) textarea[name=\"description\"]")
    private WebElement VARIANT_DESC;
    
    @FindBy(jquery = "div.rcue-dialog-inner form input[type=\"reset\"]")
    private WebElement CANCEL_BUTTON;
    
    @FindBy(jquery = "div.rcue-dialog-inner form input[type=\"submit\"]")
    private WebElement SUBMIT_BUTTON;
    
    public void fillVariantDetails(String name, String desc) {
        clearNfill(VARIANT_NAME, name);
        clearNfill(VARIANT_DESC, desc);
    }
    
    public String getName() {
        return VARIANT_NAME.getAttribute("value");
    }

    public String getDescription() {
        return VARIANT_DESC.getAttribute("value");
    }
    
    public void submitForm() {
        guardXhr(SUBMIT_BUTTON).click();
    }
    
    public void cancel() {
        guardXhr(CANCEL_BUTTON).click();
    }
    
    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(SUBMIT_BUTTON).is().visible();
    }
    
    protected abstract void updateVariant(String... input);
}
