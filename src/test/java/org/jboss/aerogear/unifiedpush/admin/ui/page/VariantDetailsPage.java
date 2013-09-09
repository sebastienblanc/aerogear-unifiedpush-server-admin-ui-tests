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

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.ArrayList;
import java.util.List;

import org.jboss.aerogear.unifiedpush.admin.ui.model.Installation;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class VariantDetailsPage extends PushServerAdminUiPage {

    @FindByJQuery("div.content section h2")
    private WebElement HEADER_TITLE;

    @FindByJQuery("div.content section span.rcue-code:eq(0)")
    private WebElement VARIANT_ID;

    @FindByJQuery("div.content section span.rcue-code:eq(1)")
    private WebElement SECRET;

    @FindBy(id = "mobile-application-variant-table")
    private WebElement MOBILE_INSTALLATIONS_TABLE_CONTAINER;

    @FindByJQuery("#mobile-application-variant-table table tr")
    private List<WebElement> MOBILE_INSTALLATION_ROWS;

    @FindByJQuery("div.content div:eq(1) a:eq(0)")
    private WebElement BREADCRUMB_PUSH_APPS_LINK;

    @FindByJQuery("div.content div:eq(1) a:eq(1)")
    private WebElement BREADCRUMB_VARIANTS_LINK;

    public void navigateToPushAppsPage() {
        BREADCRUMB_PUSH_APPS_LINK.click();
    }

    public void navigateToVariantsPage() {
        BREADCRUMB_VARIANTS_LINK.click();
    }

    public String getHeaderTitle() {
        return HEADER_TITLE.getText();
    }

    public String getVariantId() {
        return VARIANT_ID.getText();
    }

    public String getSecret() {
        return SECRET.getText();
    }

    public List<Installation> getInstallationList() {
        final List<Installation> installationList = new ArrayList<Installation>();
        for (WebElement row : MOBILE_INSTALLATION_ROWS) {
            final List<WebElement> tableDataList = row.findElements(By.tagName("td"));
            if (tableDataList.size() == 4) {
                final String token = tableDataList.get(0).getText();
                final String device = tableDataList.get(1).getText();
                final String platform = tableDataList.get(2).getText();
                final String status = tableDataList.get(3).getText();
                installationList.add(new Installation(token, device, null, null, platform, status, null));
            }
        }
        return installationList;
    }

    public boolean tokenIdExistsInList(String tokenId, List<Installation> list) {
        if (tokenId != null && list != null && !list.isEmpty()) {
            for (Installation installation : list) {
                if (tokenId.equals(installation.getDeviceToken())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(MOBILE_INSTALLATIONS_TABLE_CONTAINER).is().visible();
    }
}
