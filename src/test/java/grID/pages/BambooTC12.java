package grID.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class
BambooTC12 extends TestBase {
    HomePage homepage;

    @Parameters({"path"})
    @BeforeClass
    public void
    testInit(String path) {

        // Load the page in the browser
        webDriver.get(websiteUrl + path);
        homepage = PageFactory.initElements(webDriver, HomePage.class);

    }

    @Parameters({"path"})
    @BeforeMethod
    public void goToHomePage(String path) {
        webDriver.get(websiteUrl + path);
    }


    @Test(description = "Check no BSP search result are shown without loging in")
    public void test1SearchResultNoLogin() {
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webDriver.switchTo().defaultContent();
        WebDriverWait wait = new WebDriverWait(webDriver, 90);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        homepage.expandPlugin();

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        wait.until(ExpectedConditions.visibilityOf(homepage.loginInputBSP));
        Assert.assertFalse(homepage.isElementPresent(homepage.searchResult));
    }

    @Test(description = "Check BSP search result are not present after search")
    public void test2BSPResultsAfterSearch() {
        webDriver.switchTo().defaultContent();
        homepage.doGoogleSearch();

        //homepage.expandPlugin();
        WebDriverWait wait = new WebDriverWait(webDriver, 90);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        Assert.assertFalse(homepage.isElementPresent(homepage.searchResult));
    }
}
