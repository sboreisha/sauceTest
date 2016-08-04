package grID.pages;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.Parameter;
import com.saucelabs.saucerest.SauceREST;
import org.apache.commons.io.FileUtils;
import org.apache.commons.jxpath.functions.MethodFunction;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import grID.util.PropertyLoader;
import grID.util.Browser;
import grID.webdriver.WebDriverFactory;

public class TestBase {
    private static final String SCREENSHOT_FOLDER = "target/screenshots/";
    private static final String SCREENSHOT_FORMAT = ".png";

    protected WebDriver webDriver;

    protected String gridHubUrl;

    protected String websiteUrl;

    protected Browser browser;

    @Parameters({"browserName", "browserVersion"})
    @BeforeClass
    public void init(String browserName, String browserVersion) {
        websiteUrl = PropertyLoader.loadProperty("site.url");
        gridHubUrl = PropertyLoader.loadProperty("grid2.hub");

        browser = new Browser();

        browser.setName(browserName);
        browser.setVersion(browserVersion);
        browser.setPlatform(PropertyLoader.loadProperty("browser.platform"));

        String username = PropertyLoader.loadProperty("user.username");
        String password = PropertyLoader.loadProperty("user.password");

        webDriver = WebDriverFactory.getInstance(gridHubUrl, browser, username,
                password);
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    /*@AfterSuite(alwaysRun = true)
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }*/

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }

    @AfterMethod
    public void setScreenshot(ITestResult result, Method method) {
        String testDescription = method.getAnnotation(org.testng.annotations.Test.class).description();
        String className = result.getTestClass().getRealClass().getName();
        String browserName = ((RemoteWebDriver) webDriver).getCapabilities().getBrowserName();
        String jobID = ((RemoteWebDriver) webDriver).getSessionId().toString();
        SauceREST client = new SauceREST("WK-Sergei", "ae7faa3c-25ae-499c-a647-ef64451c7a81");
        Map<String, Object> sauceJob = new HashMap<String, Object>();
        sauceJob.put("name", "" + className.substring(className.lastIndexOf(".")+1, className.length()) + "  " + testDescription + " for browser: " + browserName);
        if (result.isSuccess()) {
            client.jobPassed(jobID);
        } else {
            client.jobFailed(jobID);
            catchExceptions(result, testDescription);
        }
        client.updateJobInfo(jobID, sauceJob);
    }


    public void catchExceptions(ITestResult result, String testDescription) {
        BufferedWriter writer = null;
        String methodName = result.getName();
        String className = result.getTestClass().getRealClass().getName();

        if (!result.isSuccess()) {
            try {
                String failureImageFileName = methodName;
                File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(failureImageFileName));
                String userDirector = System.getProperty("user.dir") + "/";
                String s1 = "<table><tr><td><font style=\"text-decoration: underline;\" size=\"3\" face=\"Comic sans MS\" color=\"green\"><b>" + methodName + "  "+testDescription + "</b></font></td></tr> ";
                 Reporter.log(s1);
                Reporter.log("<tr><td><a href=\"" + userDirector + failureImageFileName + "\"><img src=\"file:///" + userDirector + failureImageFileName + "\" alt=\"\"" + "height=’120′ width=’120′/></td></tr> ");
                Reporter.setCurrentTestResult(null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
