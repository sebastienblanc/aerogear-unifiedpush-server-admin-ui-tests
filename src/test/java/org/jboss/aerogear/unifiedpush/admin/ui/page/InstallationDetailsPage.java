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

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jboss.aerogear.unifiedpush.admin.ui.model.Installation;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class InstallationDetailsPage extends PushServerAdminUiPage {

    @FindByJQuery("div.content section h3")
    private WebElement HEADER_TITLE;

    @FindByJQuery("div.content section div.token")
    private WebElement TOKEN;

    @FindBy(id = "mobile-application-variant-table")
    private WebElement INSTALLATION_DETAILS_TABLE;

    @FindByJQuery("div#mobile-application-variant-table table tbody tr")
    private WebElement INSTALLATION_DETAILS;

    @FindByJQuery("div#mobile-application-variant-table table.rcue-table tbody tr:eq(0) td:eq(4)")
    private WebElement STATUS_FIELD;
    
    @FindByJQuery("div.content div:eq(0) a:eq(1)")
    private WebElement BREADCRUMB_VARIANTS_LINK;
    
    @FindByJQuery("div.content div:eq(0) a:eq(2)")
    private WebElement BREADCRUMB_VARIANT_LINK;

    public void navigateToVariantPage() {
        BREADCRUMB_VARIANT_LINK.click();
    }
    
    public void navigateToVariantsPage() {
        BREADCRUMB_VARIANTS_LINK.click();
    }
    
    public void pressToggleLink() {
        final WebElement toggleAnchor = INSTALLATION_DETAILS.findElement(By.tagName("a"));
        toggleAnchor.click();
        waitModel().pollingEvery(1, TimeUnit.SECONDS).until().element(STATUS_FIELD).text().contains("Disabled");
    }

    public Installation getInstallationDetails() {
        Installation installation = null;
        final List<WebElement> tableDataList = INSTALLATION_DETAILS.findElements(By.tagName("td"));
        if (tableDataList.size() == 6) {
            final String alias = tableDataList.get(0).getText();
            final String category = tableDataList.get(1).getText();
            final String deviceType = tableDataList.get(2).getText();
            final String platform = tableDataList.get(3).getText();
            final String status = tableDataList.get(4).getText();
            installation = new Installation(getToken(), deviceType, null, alias, platform, status, null, category);
        }

        return installation;
    }

    public String getToken() {
        String partialyParsed = TOKEN.getText().substring("Device Token: ".length()).trim();
        int newLinePosition = partialyParsed.indexOf("\n");
        String finalParse = partialyParsed.substring(0, newLinePosition);
        return finalParse;
    }

    public String getHeaderTitle() {
        return HEADER_TITLE.getText();
    }

	private List<WebElement> filterInstallationRows() {
		return null;
	}
    
    @Override
    public void waitUntilPageIsLoaded() {
    	super.waitUntilPageIsLoaded();
    	waitModel().pollingEvery(1, TimeUnit.SECONDS).until().element(INSTALLATION_DETAILS_TABLE).is().present();
    	waitModel().pollingEvery(1, TimeUnit.SECONDS).until().element(INSTALLATION_DETAILS).is().present();
    }

	public void waitUntilTableContainsRows(final int numOfRows) {
        waitModel().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver notUsed) {
                final List<WebElement> list = filterInstallationRows();
                return list != null && list.size() == numOfRows;
            }
        });  	
	}
}
