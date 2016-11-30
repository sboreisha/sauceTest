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
        if (!result.isSuccess()) {
            try {
                String failureImageFileName = methodName;
                File scrFile = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(scrFile, new File(failureImageFileName));
                String userDirector = System.getProperty("user.dir") + "/";
                String s1 = "<table><tr><td><font style=\"text-decoration: underline;\" size=\"3\" face=\"Comic sans MS\" color=\"green\"><b>" + methodName + "  " + testDescription + "</b></font></td></tr> ";
                Reporter.log(s1);
                Reporter.log("<tr><td><a href=\"" + userDirector + failureImageFileName + "\"><img src=\"file:///" + userDirector + failureImageFileName + "\" alt=\"\"" + "height=’120′ width=’120′/></td></tr> ");
                Reporter.setCurrentTestResult(null);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public WebDriver getFF() {
        DesiredCapabilities capability = new DesiredCapabilities();
        capability = DesiredCapabilities.firefox();
        capability.setCapability("platform", "Windows 8.1");
        capability.setCapability("version", "47.0");
        try {
            webDriver = new RemoteWebDriver(new URL("http://WK-Sergei:ae7faa3c-25ae-499c-a647-ef64451c7a81@ondemand.saucelabs.com:80/wd/hub"), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return webDriver;
    }

    public static FirefoxProfile setFFProfileNoBSP() {
        FirefoxProfile ffProfile = new FirefoxProfile();
        ffProfile.setPreference("intl.accept_languages", "en");
        ffProfile.setAcceptUntrustedCertificates(true);
        ffProfile.setPreference("browser.download.folderList", 2);
        ffProfile.setPreference("browser.download.manager.showWhenStarting", false);
        ffProfile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/x-xpinstall .xpi");
        ffProfile.setPreference("browser.helperApps.neverAsk.openFile", "application/x-xpinstall .xpi");
        ffProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
        ffProfile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        ffProfile.setPreference("browser.download.manager.focusWhenStarting", false);
        ffProfile.setPreference("browser.download.manager.useWindow", false);
        ffProfile.setPreference("browser.download.manager.showAlertOnComplete", false);
        ffProfile.setPreference("browser.download.manager.closeWhenDone", false);
        ffProfile.setPreference("browser.search.defaultenginename", "Google");
        ffProfile.setPreference("browser.search.defaultenginename.US", "data:text/plain,browser.search.defaultenginename.US=Google");
        ffProfile.setPreference("browser.search.countryCode", "US");
        ffProfile.setPreference("browser.startup.homepage", "https://www.google.com/webhp?lr=&ie=UTF-8&oe=UTF-8&gws_rd=cr,ssl&ei=xoc0V7ybNMOnsAHJxpCwBg");
        return ffProfile;
    }
}
