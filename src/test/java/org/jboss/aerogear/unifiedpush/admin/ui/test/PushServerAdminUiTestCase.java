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
package org.jboss.aerogear.unifiedpush.admin.ui.test;

import static org.jboss.aerogear.unifiedpush.admin.ui.utils.StringUtilities.isEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jboss.aerogear.unifiedpush.admin.ui.model.AbstractVariant;
import org.jboss.aerogear.unifiedpush.admin.ui.model.Installation;
import org.jboss.aerogear.unifiedpush.admin.ui.model.PushApplication;
import org.jboss.aerogear.unifiedpush.admin.ui.model.VariantType;
import org.jboss.aerogear.unifiedpush.admin.ui.page.AndroidVariantEditPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.LoginPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.PasswordChangePage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.PushAppEditPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.PushAppsPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.ReLoginPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.SimplePushVariantEditPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.VariantDetailsPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.VariantRegistrationPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.VariantsPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.iOSVariantEditPage;
import org.jboss.aerogear.unifiedpush.admin.ui.page.PushAppsPage.PUSH_APP_LINK;
import org.jboss.aerogear.unifiedpush.admin.ui.page.VariantsPage.VARIANT_LINK;
import org.jboss.aerogear.unifiedpush.admin.ui.utils.InstallationUtils;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;

public class PushServerAdminUiTestCase extends AbstractPushServerAdminUiTest {

    @Override
    protected String getPagePath() {
        return "";
    }

    @Page
    private LoginPage loginPage;

    @Page
    private ReLoginPage reLoginPage;

    @Page
    private PasswordChangePage passwordChangePage;

    @Page
    private PushAppsPage pushAppsPage;

    @Page
    private PushAppEditPage pushAppEditPage;

    @Page
    private VariantsPage variantsPage;

    @Page
    private VariantRegistrationPage variantRegistrationPage;

    @Page
    private AndroidVariantEditPage androidVariantEditPage;

    @Page
    private iOSVariantEditPage iOSVariantEditPage;

    @Page
    private VariantDetailsPage variantDetailsPage;

    @Page
    private SimplePushVariantEditPage simplePushVariantEditPage;

    @Test
    @InSequence(1)
    public void testUnauthorizedAccess() {
        // initialize page
        initializePageUrl();
        loginPage.waitUntilPageIsLoaded();
        // navigate to push apps page
        navigateToURL(pushAppsPage.getPageURL());
        loginPage.waitUntilPageIsLoaded();
        assertTrue(loginPage.getHeaderTitle() != null && loginPage.getHeaderTitle().contains(loginPage.getExpectedTitle()));
    }

    @Test
    @InSequence(2)
    public void login() {
        // initialize page
        initializePageUrl();
        // wait until login page is loaded
        loginPage.waitUntilPageIsLoaded();
        // perform login
        loginPage.login(ADMIN_USERNAME, DEFAULT_ADMIN_PASSWORD);
        // wait until password change page is loaded
        passwordChangePage.waitUntilPageIsLoaded();
        // change password
        passwordChangePage.changePassword(NEW_ADMIN_PASSWORD);
        reLoginPage.waitUntilPageIsLoaded();
        reLoginPage.login(ADMIN_USERNAME, NEW_ADMIN_PASSWORD);
    }

    @Test
    @InSequence(3)
    public void testPushAppRegistration() {
        // wait until push apps page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // initially there shouldn't exist any push applications
        assertTrue("Initially there are 0 push apps", pushAppsPage.countPushApps() == 0);
        // register a new push application
        pushAppsPage.pressCreateButton();
        // wait until edit page is loaded
        pushAppEditPage.waitUntilPageIsLoaded();
        // register a push application
        pushAppEditPage.registerNewPushApp(PUSH_APP_NAME, PUSH_APP_DESC);
        // navigate to push apps page
        pushAppsPage.waitUntilPageIsLoaded();
        pushAppsPage.waitUntilTableContainsRows(1);
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // there should exist one push application
        assertTrue("There should exist 1 push app", pushAppsList != null && pushAppsList.size() == 1);
        // The push app row should contain the right info name, desc, variants
        assertTrue(pushAppsList != null);
        assertEquals(PUSH_APP_NAME, pushAppsList.get(0).getName());
        assertEquals(PUSH_APP_DESC, pushAppsList.get(0).getDescription());
        assertEquals(0, pushAppsList.get(0).getVariants());
    }

    @Test
    @InSequence(4)
    public void testPushAppCancellation() {
        // wait until push apps page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should exist 1 push app", pushAppsPage.countPushApps() == 1);
        pushAppsPage.pressCreateButton();
        // wait until edit page is loaded
        pushAppEditPage.waitUntilPageIsLoaded();
        // press cancel
        pushAppEditPage.cancel();
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
    }

    @Test
    @InSequence(5)
    public void testPushAppEdit() {
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
        // press the edit link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.EDIT);
        // wait until edit page is loaded
        pushAppEditPage.waitUntilPageIsLoaded();
        // the push app details should be the expected ones
        assertEquals(PUSH_APP_NAME, pushAppEditPage.getName());
        assertEquals(PUSH_APP_DESC, pushAppEditPage.getDescription());
        // update the push application name
        pushAppEditPage.updatePushApp(UPDATED_PUSH_APP_NAME, UPDATED_PUSH_APP_DESC);
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // The push app row should contain the updated info name, desc
        assertTrue(pushAppsList != null);
        assertEquals(UPDATED_PUSH_APP_NAME, pushAppsList.get(0).getName());
        assertEquals(UPDATED_PUSH_APP_DESC, pushAppsList.get(0).getDescription());
        assertEquals(0, pushAppsList.get(0).getVariants());
    }

    @Test
    @InSequence(6)
    public void testAndroidVariantRegistration() {
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
        // press the variants link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        assertTrue(!isEmpty(variantsPage.getApplicationId()) && !isEmpty(variantsPage.getMasterSecret()));
        // initially there are zero variants
        assertTrue("initially there are zero variants", variantsPage.countVariants() == 0);
        // add a new variant
        variantsPage.addVariant();
        // register new android variant
        variantRegistrationPage.registerAndroidVariant(ANDROID_VARIANT_NAME, ANDROID_VARIANT_DESC, ANDROID_VARIANT_GOOGLE_KEY);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // One variant should exist
        final List<AbstractVariant> variantList = variantsPage.getVariantList();
        // one variant should exist
        assertTrue(variantList != null);
        final int variantPositionInList = variantsPage.findVariantRow(ANDROID_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        assertEquals(ANDROID_VARIANT_NAME, variantList.get(variantPositionInList).getName());
        assertEquals(ANDROID_VARIANT_DESC, variantList.get(variantPositionInList).getDescription());
        assertEquals(VariantType.ANDROID, variantList.get(variantPositionInList).getVariantType());
        assertEquals(0, variantList.get(variantPositionInList).getInstallations());
        // go to push apps page
        variantsPage.navigateToPushAppsPage();
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // The variant counter should be updated to 1
        assertTrue(pushAppsList != null);
        assertEquals(UPDATED_PUSH_APP_NAME, pushAppsList.get(0).getName());
        assertEquals(UPDATED_PUSH_APP_DESC, pushAppsList.get(0).getDescription());
        assertEquals(1, pushAppsList.get(0).getVariants());
    }

    @Test
    @InSequence(7)
    public void testAndroidVariantDetailsPage() {
        // press the variants link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        final int variantPositionInList = variantsPage.findVariantRow(ANDROID_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // title should contain the variant name
        assertTrue(variantDetailsPage.getHeaderTitle().contains(ANDROID_VARIANT_NAME));
        // variant id and secre should exist
        assertTrue(!isEmpty(variantDetailsPage.getSecret()) && !isEmpty(variantDetailsPage.getVariantId()));
        // got to variants page
        variantDetailsPage.navigateToVariantsPage();
    }

    @Test
    @InSequence(8)
    public void testAndroidVariantEdit() {
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // there should exist one variant
        assertTrue("There should still exist 1 push app", variantsPage.countVariants() == 1);
        // press the variants edit link
        variantsPage.pressVariantLink(0, VARIANT_LINK.EDIT);
        // wait until page is loaded
        androidVariantEditPage.waitUntilPageIsLoaded();
        // the variant details should be the expected ones
        assertEquals(ANDROID_VARIANT_NAME, androidVariantEditPage.getName());
        assertEquals(ANDROID_VARIANT_DESC, androidVariantEditPage.getDescription());
        assertEquals(ANDROID_VARIANT_GOOGLE_KEY, androidVariantEditPage.getGoogleApiKey());
        // register new android variant
        androidVariantEditPage.updateVariant(UPDATED_ANDROID_VARIANT_NAME, UPDATED_ANDROID_VARIANT_DESC,
                UPDATED_ANDROID_VARIANT_GOOGLE_KEY);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // one variant should exist
        final List<AbstractVariant> variantList = variantsPage.getVariantList();
        assertTrue(variantList != null);
        assertEquals(UPDATED_ANDROID_VARIANT_NAME, variantList.get(0).getName());
        assertEquals(UPDATED_ANDROID_VARIANT_DESC, variantList.get(0).getDescription());
        assertEquals(VariantType.ANDROID, variantList.get(0).getVariantType());
        assertEquals(0, variantList.get(0).getInstallations());
        variantsPage.pressVariantLink(0, VARIANT_LINK.EDIT);
        // wait until page is loaded
        androidVariantEditPage.waitUntilPageIsLoaded();
        // the variant details should be the expected ones
        assertEquals(UPDATED_ANDROID_VARIANT_NAME, androidVariantEditPage.getName());
        assertEquals(UPDATED_ANDROID_VARIANT_DESC, androidVariantEditPage.getDescription());
        assertEquals(UPDATED_ANDROID_VARIANT_GOOGLE_KEY, androidVariantEditPage.getGoogleApiKey());
    }

    @Test
    @InSequence(9)
    public void testAndroidVariantCancellation() {
        // wait until push apps page is loaded
        androidVariantEditPage.waitUntilPageIsLoaded();
        // register a push application
        androidVariantEditPage.cancel();
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // there should exist one variant
        assertTrue("There should still exist 1 variant", variantsPage.countVariants() == 1);
    }

    @Test
    @InSequence(10)
    public void testiOSVariantRegistration() {
        // go to push apps page
        variantsPage.navigateToPushAppsPage();
        // wait until page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // there should exist one push application
        assertTrue("There should still exist 1 push app", pushAppsPage.countPushApps() == 1);
        // press the variants link
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        assertTrue(!isEmpty(variantsPage.getApplicationId()) && !isEmpty(variantsPage.getMasterSecret()));
        // initially there is one variant
        assertTrue("There should exist one variant", variantsPage.countVariants() == 1);
        // add a new variant
        variantsPage.addVariant();
        // wait until page is loaded
        variantRegistrationPage.waitUntilPageIsLoaded();
        // register ios variant
        variantRegistrationPage.registeriOSVariant(IOS_VARIANT_NAME, IOS_VARIANT_DESC, IOS_CERT_PATH, IOS_CERT_PASSPHRASE,
                false);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        assertTrue("There should exist two variants", variantsPage.countVariants() == 2);
    }

    @Test
    @InSequence(11)
    public void testiOSVariantDetailsPage() {
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        final int variantPositionInList = variantsPage.findVariantRow(IOS_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // title should contain the variant name
        assertTrue(variantDetailsPage.getHeaderTitle().contains(IOS_VARIANT_NAME));
        // variant id and secre should exist
        assertTrue(!isEmpty(variantDetailsPage.getSecret()) && !isEmpty(variantDetailsPage.getVariantId()));
        // go to variants page
        variantDetailsPage.navigateToVariantsPage();
    }

    @Test
    @InSequence(12)
    public void testiOSVariantEditPatch() {
        variantsPage.waitUntilPageIsLoaded();
        final int variantPositionInList = variantsPage.findVariantRow(IOS_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // go to ios variant edit page
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.EDIT);
        // wait until page is loaded
        iOSVariantEditPage.waitUntilPageIsLoaded();
        // edit variant
        iOSVariantEditPage.updateVariant(UPDATED_IOS_VARIANT_NAME_PATCH, UPDATED_IOS_VARIANT_DESC, null, null);
        // wait until next page is loaded
        variantsPage.waitUntilPageIsLoaded();
        List<AbstractVariant> variantList = variantsPage.getVariantList();
        assertTrue(variantList != null && variantList.size() == 2);
        assertEquals(variantList.get(1).getName(), UPDATED_IOS_VARIANT_NAME_PATCH);
        assertEquals(variantList.get(1).getDescription(), UPDATED_IOS_VARIANT_DESC);
    }

    @Test
    @InSequence(13)
    public void testiOSVariantEdit() {
        variantsPage.waitUntilPageIsLoaded();
        final int variantPositionInList = variantsPage.findVariantRow(UPDATED_IOS_VARIANT_NAME_PATCH);
        assertTrue(variantPositionInList != -1);
        // go to ios variant edit page
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.EDIT);
        // wait until page is loaded
        iOSVariantEditPage.waitUntilPageIsLoaded();
        // edit variant
        iOSVariantEditPage
                .updateVariant(UPDATED_IOS_VARIANT_NAME, UPDATED_IOS_VARIANT_DESC, IOS_CERT_PATH, IOS_CERT_PASSPHRASE);
        // wait until next page is loaded
        variantsPage.waitUntilPageIsLoaded();
        List<AbstractVariant> variantList = variantsPage.getVariantList();
        assertTrue(variantList != null && variantList.size() == 2);
        assertEquals(variantList.get(1).getName(), UPDATED_IOS_VARIANT_NAME);
        assertEquals(variantList.get(1).getDescription(), UPDATED_IOS_VARIANT_DESC);
    }

    @Test
    @InSequence(14)
    public void testiOSVariantCancellation() {
        final int variantPositionInList = variantsPage.findVariantRow(UPDATED_IOS_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // wait until push apps page is loaded
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.EDIT);
        // wait until page is loaded
        iOSVariantEditPage.waitUntilPageIsLoaded();
        // register a push application
        iOSVariantEditPage.cancel();
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // there should exist one variant
        assertTrue("There should still exist 2 variants", variantsPage.countVariants() == 2);
    }

    @Test
    @InSequence(15)
    public void testSimplePushVariantRegistration() {
        // go to push apps page
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        assertTrue(!isEmpty(variantsPage.getApplicationId()) && !isEmpty(variantsPage.getMasterSecret()));
        // initially there are two variants
        assertTrue("There should exist two variants", variantsPage.countVariants() == 2);
        // add a new variant
        variantsPage.addVariant();
        // wait until page is loaded
        variantRegistrationPage.waitUntilPageIsLoaded();
        // register ios variant
        variantRegistrationPage.registerSimplePushVariant(SIMPLE_PUSH_VARIANT_NAME, SIMPLE_PUSH_VARIANT_DESC,
                SIMPLE_PUSH_VARIANT_NETWORK_URL);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        assertTrue("There should exist three variants", variantsPage.countVariants() == 3);
    }

    @Test
    @InSequence(16)
    public void testSimplePushVariantDetailsPage() {
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        final int variantPositionInList = variantsPage.findVariantRow(SIMPLE_PUSH_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // title should contain the variant name
        assertTrue(variantDetailsPage.getHeaderTitle().contains(SIMPLE_PUSH_VARIANT_NAME));
        // variant id and secre should exist
        assertTrue(!isEmpty(variantDetailsPage.getSecret()) && !isEmpty(variantDetailsPage.getVariantId()));
        // got to push applications page
        variantDetailsPage.navigateToVariantsPage();
    }

    @Test
    @InSequence(17)
    public void testSimplePushVariantEdit() {
        variantsPage.waitUntilPageIsLoaded();
        int variantPositionInList = variantsPage.findVariantRow(SIMPLE_PUSH_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // go to simple push variant edit page
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.EDIT);
        // wait until page is loaded
        simplePushVariantEditPage.waitUntilPageIsLoaded();
        // edit variant
        simplePushVariantEditPage.updateVariant(UPDATED_SIMPLE_PUSH_VARIANT_NAME, UPDATED_SIMPLE_PUSH_VARIANT_DESC,
                UPDATED_SIMPLE_PUSH_VARIANT_NETWORK_URL);
        // wait until next page is loaded
        variantsPage.waitUntilPageIsLoaded();
        variantPositionInList = variantsPage.findVariantRow(SIMPLE_PUSH_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        List<AbstractVariant> variantList = variantsPage.getVariantList();
        assertTrue(variantList != null && variantList.size() == 3);
        assertEquals(variantList.get(variantPositionInList).getName(), UPDATED_SIMPLE_PUSH_VARIANT_NAME);
        assertEquals(variantList.get(variantPositionInList).getDescription(), UPDATED_SIMPLE_PUSH_VARIANT_DESC);
    }

    @Test
    @InSequence(18)
    public void testiOSVariantProductionRegistration() {
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        assertTrue(!isEmpty(variantsPage.getApplicationId()) && !isEmpty(variantsPage.getMasterSecret()));
        // initially there is one variant
        assertTrue("There should exist one variant", variantsPage.countVariants() == 3);
        // add a new variant
        variantsPage.addVariant();
        // wait until page is loaded
        variantRegistrationPage.waitUntilPageIsLoaded();
        // register ios variant
        variantRegistrationPage.registeriOSVariant(IOS_VARIANT_NAME_2, IOS_VARIANT_DESC, IOS_CERT_PATH, IOS_CERT_PASSPHRASE,
                true);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        assertTrue("There should exist four variants", variantsPage.countVariants() == 4);
        final int variantPositionInList = variantsPage.findVariantRow(IOS_VARIANT_NAME_2);
        assertTrue(variantPositionInList != -1);
        // edit the last iOS variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.EDIT);
        // wait until page is loaded
        iOSVariantEditPage.waitUntilPageIsLoaded();
        // wait until page is loaded
        iOSVariantEditPage.waitUntilPageIsLoaded();
        // the variant details should be the expected ones
        assertEquals(IOS_VARIANT_NAME_2, iOSVariantEditPage.getName());
        assertEquals(IOS_VARIANT_DESC, iOSVariantEditPage.getDescription());
        assertEquals(true, iOSVariantEditPage.isProd());
        iOSVariantEditPage.cancel();
        variantsPage.waitUntilPageIsLoaded();
        variantsPage.navigateToPushAppsPage();
        pushAppsPage.waitUntilPageIsLoaded();
    }

    @Test
    @InSequence(19)
    public void registerAndroidInstallations() {
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        final String pushAppId = variantsPage.getApplicationId();
        final String masterSecret = variantsPage.getMasterSecret();
        assertTrue(!isEmpty(pushAppId) && !isEmpty(masterSecret));
        int variantPositionInList = variantsPage.findVariantRow(UPDATED_ANDROID_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // title should contain the variant name
        assertTrue(variantDetailsPage.getHeaderTitle().contains(UPDATED_ANDROID_VARIANT_NAME));
        // variant id and secre should exist
        final String variantId = variantDetailsPage.getVariantId();
        final String secret = variantDetailsPage.getSecret();
        assertTrue(!isEmpty(variantId) && !isEmpty(secret));
        // register installation
        Installation androidInstallation = new Installation(ANDROID_INSTALLATION_TOKEN_ID, ANDROID_INSTALLATION_DEVICE_TYPE,
                ANDROID_INSTALLATION_OS, ANDROID_INSTALLATION_ALIAS, null, null);
        InstallationUtils.registerInstallation(contextRoot.toExternalForm(), variantId, secret, androidInstallation);
        // register second installation
        Installation secondAndroidInstallation = new Installation(ANDROID_INSTALLATION_TOKEN_ID_2,
                ANDROID_INSTALLATION_DEVICE_TYPE, ANDROID_INSTALLATION_OS, ANDROID_INSTALLATION_ALIAS, null, null);
        InstallationUtils.registerInstallation(contextRoot.toExternalForm(), variantId, secret, secondAndroidInstallation);
        // go back to push app page
        variantDetailsPage.navigateToPushAppsPage();
        // select the push app
        pushAppsPage.pressPushAppLink(0, PUSH_APP_LINK.VARIANTS_PAGE);
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // fins the android variant
        variantPositionInList = variantsPage.findVariantRow(UPDATED_ANDROID_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        AbstractVariant variant = variantsPage.getVariantList().get(variantPositionInList);
        assertTrue(variant != null && variant.getInstallations() == 2);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // two installations should exist
        final List<Installation> installationList = variantDetailsPage.getInstallationList();
        assertTrue(installationList != null && installationList.size() == 2);
        // the installations should have the right token ids
        assertTrue(variantDetailsPage.tokenIdExistsInList(ANDROID_INSTALLATION_TOKEN_ID, installationList)
                && variantDetailsPage.tokenIdExistsInList(ANDROID_INSTALLATION_TOKEN_ID_2, installationList));
        // platform should be Android
        assertEquals(ANDROID_PLATFORM, installationList.get(0).getPlatform());
        assertEquals(ANDROID_PLATFORM, installationList.get(1).getPlatform());
        // status should be enabled
        assertEquals(INSTALLATION_STATUS_ENABLED, installationList.get(0).getStatus());
        assertEquals(INSTALLATION_STATUS_ENABLED, installationList.get(1).getStatus());
        variantDetailsPage.navigateToVariantsPage();
    }

    @Test
    @InSequence(20)
    public void registeriOSInstallations() {
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        final String pushAppId = variantsPage.getApplicationId();
        final String masterSecret = variantsPage.getMasterSecret();
        assertTrue(!isEmpty(pushAppId) && !isEmpty(masterSecret));

        int variantPositionInList = variantsPage.findVariantRow(UPDATED_IOS_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // title should contain the variant name
        assertTrue(variantDetailsPage.getHeaderTitle().contains(UPDATED_IOS_VARIANT_NAME));
        // variant id and secre should exist
        final String variantId = variantDetailsPage.getVariantId();
        final String secret = variantDetailsPage.getSecret();
        assertTrue(!isEmpty(variantId) && !isEmpty(secret));
        // register installation
        Installation iosInstallation = new Installation(IOS_INSTALLATION_TOKEN_ID, IOS_INSTALLATION_DEVICE_TYPE,
                IOS_INSTALLATION_OS, IOS_INSTALLATION_ALIAS, null, null);
        InstallationUtils.registerInstallation(contextRoot.toExternalForm(), variantId, secret, iosInstallation);
        // register second installation
        Installation secondiOSInstallation = new Installation(IOS_INSTALLATION_TOKEN_ID_2, IOS_INSTALLATION_DEVICE_TYPE,
                IOS_INSTALLATION_OS, IOS_INSTALLATION_ALIAS, null, null);
        InstallationUtils.registerInstallation(contextRoot.toExternalForm(), variantId, secret, secondiOSInstallation);
        // go back to push app page
        variantDetailsPage.navigateToVariantsPage();
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // fins the android variant
        variantPositionInList = variantsPage.findVariantRow(UPDATED_IOS_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        AbstractVariant variant = variantsPage.getVariantList().get(variantPositionInList);
        assertTrue(variant != null && variant.getInstallations() == 2);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // two installations should exist
        final List<Installation> installationList = variantDetailsPage.getInstallationList();
        assertTrue(installationList != null && installationList.size() == 2);
        // the installations should have the right token ids
        assertTrue(variantDetailsPage.tokenIdExistsInList(IOS_INSTALLATION_TOKEN_ID, installationList)
                && variantDetailsPage.tokenIdExistsInList(IOS_INSTALLATION_TOKEN_ID_2, installationList));
        // platform should be IOS
        assertEquals(IOS_PLATFORM, installationList.get(0).getPlatform());
        assertEquals(IOS_PLATFORM, installationList.get(1).getPlatform());
        // status should be enabled
        assertEquals(INSTALLATION_STATUS_ENABLED, installationList.get(0).getStatus());
        assertEquals(INSTALLATION_STATUS_ENABLED, installationList.get(1).getStatus());
        variantDetailsPage.navigateToVariantsPage();
    }

    @Test
    @InSequence(21)
    public void registerSimplePushInstallations() {
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // assert header title
        assertTrue(variantsPage.getHeaderTitle().contains(UPDATED_PUSH_APP_NAME));
        // application id & master secret should exist
        final String pushAppId = variantsPage.getApplicationId();
        final String masterSecret = variantsPage.getMasterSecret();
        assertTrue(!isEmpty(pushAppId) && !isEmpty(masterSecret));

        int variantPositionInList = variantsPage.findVariantRow(UPDATED_SIMPLE_PUSH_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // title should contain the variant name
        assertTrue(variantDetailsPage.getHeaderTitle().contains(UPDATED_SIMPLE_PUSH_VARIANT_NAME));
        // variant id and secre should exist
        final String variantId = variantDetailsPage.getVariantId();
        final String secret = variantDetailsPage.getSecret();
        assertTrue(!isEmpty(variantId) && !isEmpty(secret));
        // register installation
        Installation spInstallation = new Installation(SIMPLE_PUSH_INSTALLATION_TOKEN_ID, SIMPLE_PUSH_INSTALLATION_DEVICE_TYPE,
                SIMPLE_PUSH_INSTALLATION_OS, SIMPLE_PUSH_INSTALLATION_ALIAS, null, null);
        InstallationUtils.registerInstallation(contextRoot.toExternalForm(), variantId, secret, spInstallation);
        // register second installation
        Installation secondSpInstallation = new Installation(SIMPLE_PUSH_INSTALLATION_TOKEN_ID_2,
                SIMPLE_PUSH_INSTALLATION_DEVICE_TYPE, SIMPLE_PUSH_INSTALLATION_OS, SIMPLE_PUSH_INSTALLATION_ALIAS, null, null);
        InstallationUtils.registerInstallation(contextRoot.toExternalForm(), variantId, secret, secondSpInstallation);
        // go back to variants page
        variantDetailsPage.navigateToVariantsPage();
        // wait until page is loaded
        variantsPage.waitUntilPageIsLoaded();
        // fins the android variant
        variantPositionInList = variantsPage.findVariantRow(UPDATED_SIMPLE_PUSH_VARIANT_NAME);
        assertTrue(variantPositionInList != -1);
        AbstractVariant variant = variantsPage.getVariantList().get(variantPositionInList);
        assertTrue(variant != null && variant.getInstallations() == 2);
        // click on a variant
        variantsPage.pressVariantLink(variantPositionInList, VARIANT_LINK.DETAILS_PAGE);
        // wait until page is loaded
        variantDetailsPage.waitUntilPageIsLoaded();
        // two installations should exist
        final List<Installation> installationList = variantDetailsPage.getInstallationList();
        assertTrue(installationList != null && installationList.size() == 2);
        // the installations should have the right token ids
        assertTrue(variantDetailsPage.tokenIdExistsInList(SIMPLE_PUSH_INSTALLATION_TOKEN_ID, installationList)
                && variantDetailsPage.tokenIdExistsInList(SIMPLE_PUSH_INSTALLATION_TOKEN_ID_2, installationList));
        // platform should be SimplePush
        assertEquals(SIMPLE_PUSH_PLATFORM, installationList.get(0).getPlatform());
        assertEquals(SIMPLE_PUSH_PLATFORM, installationList.get(1).getPlatform());
        // status should be enabled
        assertEquals(INSTALLATION_STATUS_ENABLED, installationList.get(0).getStatus());
        assertEquals(INSTALLATION_STATUS_ENABLED, installationList.get(1).getStatus());
        variantDetailsPage.navigateToPushAppsPage();
    }

    @Test
    @InSequence(22)
    public void testSecondPushAppRegistration() {
        // wait until push apps page is loaded
        pushAppsPage.waitUntilPageIsLoaded();
        // initially there shouldn't exist any push applications
        assertTrue("Initially there is 1 push app", pushAppsPage.countPushApps() == 1);
        // register a new push application
        pushAppsPage.pressCreateButton();
        // wait until edit page is loaded
        pushAppEditPage.waitUntilPageIsLoaded();
        // register a push application
        pushAppEditPage.registerNewPushApp(SECOND_PUSH_APP_NAME, PUSH_APP_DESC);
        // navigate to push apps page
        pushAppsPage.waitUntilPageIsLoaded();
        final List<PushApplication> pushAppsList = pushAppsPage.getPushAppList();
        // there should exist one push application
        assertTrue("There should exist 2 push apps", pushAppsList != null && pushAppsList.size() == 2);
    }

    @Test
    @InSequence(23)
    public void testLogout() {
        // logout
        pushAppsPage.logout();
        // wait until login page is loaded
        loginPage.waitUntilPageIsLoaded();
    }

    /* -- Testing data section -- */

    private static final String ADMIN_USERNAME = "admin";

    private static final String DEFAULT_ADMIN_PASSWORD = "123";

    private static final String NEW_ADMIN_PASSWORD = "aerogear";

    private static final String PUSH_APP_NAME = "MyApp";

    private static final String SECOND_PUSH_APP_NAME = "MyNewApp";

    private static final String PUSH_APP_DESC = "Awesome app!";

    private static final String UPDATED_PUSH_APP_NAME = "MyNewApp";

    private static final String UPDATED_PUSH_APP_DESC = "My new awesome app!";

    private static final String ANDROID_VARIANT_NAME = "MyAndroidVariant";

    private static final String ANDROID_VARIANT_DESC = "My awesome variant!";

    private static final String ANDROID_VARIANT_GOOGLE_KEY = "IDDASDASDSAQ";

    private static final String UPDATED_ANDROID_VARIANT_NAME = "MyNewAndroidVariant";

    private static final String UPDATED_ANDROID_VARIANT_DESC = "My new awesome variant!";

    private static final String UPDATED_ANDROID_VARIANT_GOOGLE_KEY = "IDDASDASDSAQ__1";

    private static final String IOS_VARIANT_NAME = "MyIOSVariant";

    private static final String IOS_VARIANT_NAME_2 = "MyIOSVariant";

    private static final String IOS_VARIANT_DESC = "My awesome IOS variant!";

    private static final String IOS_CERT_PASSPHRASE = "aerogear";

    private static final String IOS_CERT_PATH = "src/test/resources/certs/qaAerogear.p12";

    private static final String UPDATED_IOS_VARIANT_NAME_PATCH = "MyNewIOSVariantPatch";

    private static final String UPDATED_IOS_VARIANT_NAME = "MyNewIOSVariant";

    private static final String UPDATED_IOS_VARIANT_DESC = "My new awesome IOS variant!";

    private static final String SIMPLE_PUSH_VARIANT_NAME = "MySimplePushVariant";

    private static final String SIMPLE_PUSH_VARIANT_DESC = "My awesome SimplePush variant!";

    private static final String SIMPLE_PUSH_VARIANT_NETWORK_URL = "http://localhost:7777/";

    private static final String UPDATED_SIMPLE_PUSH_VARIANT_NAME = "MySimplePushVariant";

    private static final String UPDATED_SIMPLE_PUSH_VARIANT_DESC = "My awesome SimplePush variant!";

    private static final String UPDATED_SIMPLE_PUSH_VARIANT_NETWORK_URL = "http://localhost:7777/endpoint/";

    private static final String ANDROID_INSTALLATION_TOKEN_ID = "QWERTY";

    private static final String ANDROID_INSTALLATION_TOKEN_ID_2 = "QWERTY_2";

    private static final String ANDROID_INSTALLATION_DEVICE_TYPE = "Phone";

    private static final String ANDROID_INSTALLATION_OS = "ANDROID";

    private static final String ANDROID_INSTALLATION_ALIAS = "qa@example.com";

    private static final String IOS_INSTALLATION_TOKEN_ID = "abcd123456";

    private static final String IOS_INSTALLATION_TOKEN_ID_2 = "abcd123321";

    private static final String IOS_INSTALLATION_DEVICE_TYPE = "Phone";

    private static final String IOS_INSTALLATION_OS = "IOS";

    private static final String IOS_INSTALLATION_ALIAS = "qa@example.com";

    private static final String SIMPLE_PUSH_INSTALLATION_TOKEN_ID = "abcd123654";

    private static final String SIMPLE_PUSH_INSTALLATION_TOKEN_ID_2 = "abcd654321";

    private static final String SIMPLE_PUSH_INSTALLATION_DEVICE_TYPE = "WebPhone";

    private static final String SIMPLE_PUSH_INSTALLATION_OS = "MozillaOS";

    private static final String SIMPLE_PUSH_INSTALLATION_ALIAS = "qa@example.com";

    private static final String IOS_PLATFORM = "IOS";

    private static final String ANDROID_PLATFORM = "ANDROID";

    private static final String SIMPLE_PUSH_PLATFORM = "MozillaOS";

    private static final String INSTALLATION_STATUS_ENABLED = "Enabled";

    /* -- Testing data section -- */
}