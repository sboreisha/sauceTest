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
BambooTC15Google extends TestBase {
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


    @Test(description = "BSP test results are shown for Google.com")
    public void test1SearchResultLogin() {
        webDriver.get("https://www.google.com/?gws_rd=ssl#q=what");
        homepage.threadSleep(5000);
        homepage.doGoogleSearch();
        homepage.expandPlugin();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(homepage.iframe);
        homepage.doLogin();
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        webDriver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        Assert.assertTrue(homepage.isElementPresent(homepage.searchResult), "BSP Search results are shown");
    }

    @Test(description = "BSP search result format is correct")
    public void test2BSPResultsCount() {
        webDriver.switchTo().defaultContent();
        homepage.doGoogleSearch();
        //homepage.expandPlugin();
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        wait.until(ExpectedConditions.visibilityOf(homepage.getbspSearchItem()));
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchInfo), "BSP search result count is present");
        softAssert.assertTrue(homepage.isElementPresent(homepage.wkBrand), "Brand image is present");
        softAssert.assertAll();
    }

    @Test(description = "Search result is the same for BSP/Cheetah")
    public void test3CheetahResultsCount() {
        webDriver.switchTo().defaultContent();
        homepage.doGoogleSearch();
        homepage.threadSleep(1500);
        int bspSearchCount = homepage.getBSPSearchCount();
        System.out.println("BSP search count " + bspSearchCount);
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        homepage.goToCheetah();
        webDriver.switchTo().defaultContent();
        homepage.changeTab(1);
        System.out.println("Cheetah search count " + cheetahPage.getCheetahSearchCount());
        Assert.assertEquals(cheetahPage.getCheetahSearchCount(), bspSearchCount, "Search results count is the same for BSP and cheetah");
        homepage.changeTab(0);
    }
}
