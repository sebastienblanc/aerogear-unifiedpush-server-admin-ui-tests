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

import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.WebElement;

public class AndroidVariantEditPage extends VariantEditPage {

    @FindByJQuery("div.rcue-dialog-inner form section:eq(0) input[type=\"text\"]")
    private WebElement GOOGLE_API_KEY_INPUT_FIELD;

    public String getGoogleApiKey() {
        return GOOGLE_API_KEY_INPUT_FIELD.getAttribute("value");
    }

    /**
     * Input: name, desc, google api key
     */
    @Override
    public void updateVariant(String... input) {
        fillVariantDetails(input[0], input[1]);
        clearNfill(GOOGLE_API_KEY_INPUT_FIELD, input[2]);
        submitForm();
    }
}
