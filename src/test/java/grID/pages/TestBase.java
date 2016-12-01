package grID.pages;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
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
        webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
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
        if (gridHubUrl.length() != 0) {
            SauceREST client = new SauceREST("WK-Sergei", "ae7faa3c-25ae-499c-a647-ef64451c7a81");
            Map<String, Object> sauceJob = new HashMap<String, Object>();
            sauceJob.put("name", "" + className.substring(className.lastIndexOf(".") + 1, className.length()) + "  " + testDescription + " for browser: " + browserName);
            if (result.isSuccess()) {
                client.jobPassed(jobID);
            } else {
                client.jobFailed(jobID);
                catchExceptions(result, testDescription);
            }
            client.updateJobInfo(jobID, sauceJob);
        } else {
            if (!result.isSuccess()) {
                catchExceptions(result, testDescription);
            }
        }

    }


    public void catchExceptions(ITestResult result, String testDescription) {
        BufferedWriter writer = null;
        String methodName = result.getName();
        String browserName = ((RemoteWebDriver) webDriver).getCapabilities().getBrowserName();
        if (!result.isSuccess()) {
            try {
                String failureImageFileName = methodName;
                File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                String userDirector = System.getProperty("user.dir").replace("\\", "/") + "/target/surefire-reports/html/";
                FileUtils.copyFile(scrFile, new File(userDirector + failureImageFileName));
                String s1 = "<table><tr><td><font size=\"2\" face=\"Comic sans MS\" ><b>" + browserName + " " + methodName + "  " + testDescription + "</b></font></td></tr> ";
                Reporter.log(s1);
                String s2 = "<tr><td><a href=\"" + failureImageFileName + "\"><img src=\"" + failureImageFileName + "\" alt=\"\"" + "height=120 width=120/></td></tr> ";
                Reporter.log(s2);
                Reporter.setCurrentTestResult(null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


}
