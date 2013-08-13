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

import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.guardXhr;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.ArrayList;
import java.util.List;

import org.jboss.aerogear.unifiedpush.admin.ui.model.PushApplication;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class PushAppsPage extends PushServerAdminUiPage {

    @FindBy(jquery = "table.rcue-table")
    private WebElement PUSH_APPLICATION_TABLE;

    @FindBy(jquery = "table.rcue-table thead div.topcoat-button a")
    private WebElement CREATE_BUTTON;

    @FindBy(jquery = "table.rcue-table tbody tr")
    private List<WebElement> PUSH_APPLICATION_LIST;

    private final static String PAGE_URL = "#/mobileApps";

    public String getPageURL() {
        return PAGE_URL;
    }

    public int countPushApps() {
        return filterPushApplicationRows().size();
    }

    public void pressCreateButton() {
        guardNoRequest(CREATE_BUTTON).click();
    }

    public static enum PUSH_APP_LINK {
        EDIT, REMOVE, VARIANTS_PAGE
    }

    public void pressPushAppLink(int rowNum, PUSH_APP_LINK link) {
        final List<WebElement> anchors = PUSH_APPLICATION_LIST.get(rowNum).findElements(By.tagName("a"));
        switch (link) {
            case VARIANTS_PAGE:
                guardXhr(anchors.get(0)).click();
                break;
            case EDIT:
                guardNoRequest(anchors.get(1)).click();
                break;
            case REMOVE:
                guardNoRequest(anchors.get(2)).click();
                break;
            default:
                break;
        }
    }

    private List<WebElement> filterPushApplicationRows() {
        final List<WebElement> rowList = new ArrayList<WebElement>();
        for (WebElement row : PUSH_APPLICATION_LIST) {
            if (row.findElements(By.tagName("td")).size() == 5) {
                rowList.add(row);
            }
        }
        return rowList;
    }

    public List<PushApplication> getPushAppList() {
        final List<PushApplication> pushAppList = new ArrayList<PushApplication>();
        for (WebElement row : PUSH_APPLICATION_LIST) {
            final List<WebElement> tableDataList = row.findElements(By.tagName("td"));
            if (tableDataList.size() == 5) {
                final String name = tableDataList.get(0).getText();
                final String desc = tableDataList.get(1).getText();
                final String vars = tableDataList.get(2).getText();
                //System.out.println("name: " + name + " desc: " + desc + " vars " + vars);
                pushAppList.add(new PushApplication(name, desc, Integer.valueOf(vars)));
            }
        }
        return pushAppList;
    }

    public int findPushAppRow(String name) {
        final List<PushApplication> pushAppList = getPushAppList();
        if (name != null && pushAppList != null && !pushAppList.isEmpty()) {
            for (int i = 0; i < pushAppList.size(); i++) {
                if (name.equals(pushAppList.get(i).getName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void waitUntilPageIsLoaded() {
        waitModel().until().element(PUSH_APPLICATION_TABLE).is().visible();
    }

    public void waitUntilTableContainsRows(final int numOfRows) {
        waitModel().until(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver notUsed) {
                return PUSH_APPLICATION_TABLE.findElements(ByJQuery.jquerySelector("tbody tr")).size() == numOfRows;
            }
        });
    }
}
