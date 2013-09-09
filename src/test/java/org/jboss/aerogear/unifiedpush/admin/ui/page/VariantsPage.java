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
import java.util.concurrent.TimeUnit;

import org.jboss.aerogear.unifiedpush.admin.ui.model.AbstractVariant;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class VariantsPage extends PushServerAdminUiPage {

    @FindByJQuery("table.rcue-table thead div.topcoat-button a")
    private WebElement ADD_VARIANT_BUTTON;

    @FindByJQuery("div.content section h2")
    private WebElement HEADER_TITLE;

    @FindByJQuery("div.content section span.rcue-code:eq(0)")
    private WebElement APPLICATION_ID;

    @FindByJQuery("div.content section span.rcue-code:eq(1)")
    private WebElement MASTER_SECRET;

    @FindByJQuery("table.rcue-table tbody tr")
    private List<WebElement> VARIANTS_LIST;

    @FindByJQuery("div.content a[href=\"#/mobileApps\"]")
    private WebElement BACK_TO_PUSH_APPS_LINK;

    public void navigateToPushAppsPage() {
        BACK_TO_PUSH_APPS_LINK.click();
    }

    public void addVariant() {
        ADD_VARIANT_BUTTON.click();
    }

    public String getHeaderTitle() {
        return HEADER_TITLE.getText();
    }

    public String getApplicationId() {
        return APPLICATION_ID.getText();
    }

    public String getMasterSecret() {
        return MASTER_SECRET.getText();
    }

    public int countVariants() {
        return filterVariantRows().size();
    }

    public static enum VARIANT_LINK {
        EDIT, REMOVE, DETAILS_PAGE
    }

    public void pressVariantLink(int rowNum, VARIANT_LINK link) {
        final List<WebElement> anchors = VARIANTS_LIST.get(rowNum).findElements(By.tagName("a"));
        switch (link) {
            case DETAILS_PAGE:
                anchors.get(0).click();
                break;
            case EDIT:
                anchors.get(1).click();
                break;
            case REMOVE:
                anchors.get(2).click();
                break;
            default:
                break;
        }
    }

    public List<AbstractVariant> getVariantList() {
        final List<AbstractVariant> variantList = new ArrayList<AbstractVariant>();
        if (VARIANTS_LIST != null && VARIANTS_LIST.size() > 0) {
            for (WebElement row : VARIANTS_LIST) {
                if (row != null) {
                    final List<WebElement> tableDataList = row.findElements(By.tagName("td"));
                    if (tableDataList != null && tableDataList.size() == 6) {
                        final String name = tableDataList.get(0).getText();
                        final String desc = tableDataList.get(1).getText();
                        final String type = tableDataList.get(2).getText();
                        final int installations = Integer.valueOf(tableDataList.get(3).getText());
                        // System.out.println("name: " + name + " desc: " + desc + " vars " + vars);
                        variantList.add(new AbstractVariant(name, desc, type, installations));
                    }
                }
            }
        }
        return variantList;
    }

    public int findVariantRow(String name) {
        final List<AbstractVariant> variantList = getVariantList();
        if (name != null && variantList != null && !variantList.isEmpty()) {
            for (int i = 0; i < variantList.size(); i++) {
                if (name.equals(variantList.get(i).getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    private List<WebElement> filterVariantRows() {
        try {
            // workaround for travis headless browser testing
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // e.printStackTrace();
        }
        final List<WebElement> rowList = new ArrayList<WebElement>();
        if (VARIANTS_LIST != null && VARIANTS_LIST.size() > 0) {
            for (WebElement row : VARIANTS_LIST) {
                if (row != null) {
                    final List<WebElement> tableDataList = row.findElements(By.tagName("td"));
                    if (tableDataList != null && tableDataList.size() == 6) {
                        rowList.add(row);
                    }
                }
            }
        }
        return rowList;
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().withTimeout(10, TimeUnit.SECONDS).until().element(ADD_VARIANT_BUTTON).is().visible();
    }
}
