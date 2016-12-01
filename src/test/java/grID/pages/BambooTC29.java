package grID.pages;

import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class
BambooTC29 extends TestBase {
    HomePage homepage;
    CheetahPage cheetahPage;
    SoftAssert softAssert = new SoftAssert();

    @Parameters({"path"})
    @BeforeClass
    public void
    testInit(String path) {

        // Load the page in the browser
        webDriver.get(websiteUrl + path);
        homepage = PageFactory.initElements(webDriver, HomePage.class);
        cheetahPage = PageFactory.initElements(webDriver, CheetahPage.class);
    }

   /* @Parameters({"path"})
    @BeforeMethod
    public void goToHomePage(String path) {
        webDriver.get(websiteUrl + path);
    }*/


    @Test(description = "BSP search plugin is shown")
    public void test1BSPPluginIsShown() {
        webDriver.get("https://www.google.com/?gws_rd=ssl#q=what");
        homepage.waitForJSandJQueryToLoad();
        homepage.doGoogleSearch();
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.visibilityOf(homepage.iframe));
        Assert.assertTrue(homepage.isElementPresent(homepage.iframe), "Plugin is shown");
    }

    @Test(description = "BSP search result are shown")
    public void test2BSPSearchResultsAreShown() {
        homepage.expandPlugin();
        homepage.doLogin();
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        wait.until(ExpectedConditions.visibilityOf(homepage.bspSearchItem));
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchItem), "BSP search results are present");
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchInfo), "BSP search result count is present");
        softAssert.assertTrue(homepage.isElementPresent(homepage.wkBrand), "Brand image is present");
        softAssert.assertAll();
    }

    @Test(description = "Search results are grouped by document type")
    public void test3CheetahResultsCount() {
        webDriver.switchTo().defaultContent();
        homepage.doClickSearchSortByDocumentType();
        webDriver.switchTo().frame(homepage.iframe);
        Assert.assertTrue(homepage.isElementPresent(homepage.collapseIcon), "Document groups are present");
    }

    @Test(description = "Search results are identical to search results in the related product: # of results, content and document group types shown")
    public void test4BSPSearchResultsAreShownWithDocGroups() {
        homepage.doGoogleSearch();
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        wait.until(ExpectedConditions.visibilityOf(homepage.bspSearchItem));
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchItem), "BSP search results are present");
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchInfo), "BSP search result count is present");
        softAssert.assertAll();
        Assert.assertTrue(homepage.isElementPresent(homepage.collapseIcon), "Document groups are present");
    }

    @Test(description = "Search results count is the same for BSP and cheetah")
    public void test5CheetahResultsCount() {
        webDriver.switchTo().defaultContent();
        homepage.waitForJSandJQueryToLoad();
        int bspSearchCount = homepage.getBSPSearchCount();
        System.out.println("BSP search count " + bspSearchCount);
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        homepage.goToCheetah();
        webDriver.switchTo().defaultContent();
        homepage.changeTab(1);
        System.out.println("Cheetah search count " + cheetahPage.getCheetahSearchCount());
        Assert.assertEquals(cheetahPage.getCheetahSearchCount(), bspSearchCount, "Search results count is the same for BSP and cheetah");

    }

    @Test(description = "No need to relogin on a new tab")
    public void test6CheckBSPOnaNewTab() {
        homepage.openNewTab();
        homepage.changeTab(2);
        webDriver.get("https://www.google.com/?gws_rd=ssl#q=what");
        homepage.doGoogleSearch();
        homepage.waitForJSandJQueryToLoad();
        homepage.showResults();
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        wait.until(ExpectedConditions.visibilityOf(homepage.bspSearchItem));
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchItem), "BSP search results are present");
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchInfo), "BSP search result count is present");
        softAssert.assertAll();
    }
}
