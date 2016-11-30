package grID.pages;

import grID.webdriver.WebDriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;


public class BSPTest extends TestBase {
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


    @Test
    public void testExpandBSP() {

        homepage.expandPlugin();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(0);
        Assert.assertTrue(webDriver.findElement(By.id("vLogin_UserEmail")).isDisplayed());
    }

    @Test
    public void testLogin() throws InterruptedException {
        homepage.expandPlugin();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(0);
        homepage.doLogin();
        //Assert.assertTrue(webDriver.findElement(By.id("error_message")).isDisplayed());
    }
    @Test
    public void testSearchResults() throws InterruptedException {
        homepage.expandPlugin();
        Assert.assertTrue(homepage.getSearchResult().contains("What"),"Invalid search result");
        //Assert.assertTrue(webDriver.findElement(By.id("error_message")).isDisplayed());
    }
    @Test
    public void testGoToCheetah() throws InterruptedException {
        homepage.expandPlugin();
        homepage.goToCheetah();
        Thread.sleep(5000);
        //Assert.assertTrue(webDriver.findElement(By.id("error_message")).isDisplayed());
    }
}
