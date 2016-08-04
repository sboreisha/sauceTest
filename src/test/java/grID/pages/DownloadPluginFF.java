package grID.pages;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class
DownloadPluginFF extends TestBase {
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
    public void testElementIsPresent(String browserName) throws InterruptedException {
        homepage.downloadPlugin(browserName);
    }
}
