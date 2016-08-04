package grID.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;

public class
BambooTC14 extends TestBase {
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

    @Parameters({"browserName"})
    @Test
    public void test1SearchResultNoLogin() {
        homepage.expandPlugin();
        homepage.doLogin();
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchInfo), "Element is present");
        WebDriver firefox = homepage.getFF();
        firefox.get("https://www.google.com/search?q=where");
        WebDriverWait wait = new WebDriverWait(firefox, 30);
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(homepage.iframe));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".settings_btn")));
        WebElement login = firefox.findElement(By.cssSelector(".settings_btn"));
        softAssert.assertTrue(login.isDisplayed(), "Logged in");
        firefox.close();
        softAssert.assertTrue(homepage.isElementPresent(homepage.bspSearchInfo), "Element is present");
        softAssert.assertAll();
    }
}
