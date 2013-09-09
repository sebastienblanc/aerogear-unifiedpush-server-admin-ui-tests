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

import java.io.File;

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

public class iOSVariantEditPage extends VariantEditPage {

    @FindByJQuery("div.rcue-dialog-inner form section input[type=\"file\"]")
    private WebElement APPLE_CERTIFICATE_INPUT_FILE;

    @FindByJQuery("div.rcue-dialog-inner form section input[type=\"password\"]")
    private WebElement APPLE_PASSPHRASE_INPUT_FIELD;

    @FindByJQuery("div.rcue-dialog-inner form section input[type=\"checkbox\"]")
    private WebElement IOS_PRODUCTION_FLAG_CHECKBOX;

    @FindByJQuery("div.rcue-dialog-inner form input[type=\"submit\"]")
    private WebElement SUBMIT_BUTTON;

    @FindByJQuery("div.rcue-dialog-inner form input[type=\"reset\"]")
    private WebElement CANCEL_BUTTON;

    public boolean isProd() {
        return IOS_PRODUCTION_FLAG_CHECKBOX.isSelected();
    }

    @Override
    public void updateVariant(String... input) {
        if (input[0] != null && input[1] != null) {
            fillVariantDetails(input[0], input[1]);
        }
        if (input[2] != null) {
            APPLE_CERTIFICATE_INPUT_FILE.sendKeys((new File(input[2])).getAbsolutePath());
        }
        if (input[3] != null) {
            clearNfill(APPLE_PASSPHRASE_INPUT_FIELD, input[3]);
        }

        super.submitForm();
    }
}
