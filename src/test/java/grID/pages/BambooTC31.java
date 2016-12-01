package grID.pages;

import com.opera.core.systems.OperaDriver;
import com.saucelabs.saucerest.SauceREST;
import grID.util.Browser;
import grID.util.PropertyLoader;
import grID.webdriver.AuthenticatedHtmlUnitDriver;
import grID.webdriver.WebDriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class
BambooTC31 extends TestBase {
    protected WebDriver webDriver;
    protected String gridHubUrl;
    protected HomePage homepage;
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String WINDOWS = "windows";
    public static final String ANDROID = "android";
    public static final String XP = "xp";
    public static final String VISTA = "vista";
    public static final String MAC = "mac";
    public static final String LINUX = "linux";
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
        webDriver = getInstance(gridHubUrl, browser, username,
                password);
        webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

    }

    @Parameters({"browserName"})
    @Test(description = "Plugin is not shown")
    public void test1PluginIsNotShown() {
        homepage = PageFactory.initElements(webDriver, HomePage.class);
        webDriver.get("https://www.google.com/search?q=where");
        homepage.waitForJSandJQueryToLoad();
        Assert.assertFalse(homepage.isElementPresent(homepage.iframe));
    }

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

    public static WebDriver getInstance(String gridHubUrl, Browser browser,
                                        String username, String password) {

        WebDriver webDriver = null;

        DesiredCapabilities capability = new DesiredCapabilities();
        String browserName = browser.getName();
        capability.setJavascriptEnabled(true);

        // In case there is no Hub
        if (gridHubUrl == null || gridHubUrl.length() == 0) {
            return getInstance(browserName, username, password);
        }

        if (CHROME.equals(browserName)) {
            capability = DesiredCapabilities.chrome();
            capability = setChromeCapabilities(capability);

        } else if (FIREFOX.equals(browserName)) {
            capability = DesiredCapabilities.firefox();
            FirefoxProfile ffProfile = setFFProfileNoBSP();

            // Authenication Hack for Firefox
            if (username != null && password != null) {
                ffProfile.setPreference("network.http.phishy-userpass-length",
                        255);
                capability.setCapability(FirefoxDriver.PROFILE, ffProfile);
            }

            capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        }

        capability = setVersionAndPlatform(capability, browser.getVersion(),
                browser.getPlatform());
//TODO set platform automatically from surefire

        capability.setCapability("platform", "Windows 8.1");
        // Create Remote WebDriver
        try {
            webDriver = new RemoteWebDriver(new URL(gridHubUrl), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return webDriver;
    }
    public static WebDriver getInstance(String browser, String username,
                                        String password) {

        WebDriver webDriver = null;

        if (CHROME.equals(browser)) {
            setChromeDriver();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities = setChromeCapabilities(capabilities);
            webDriver = new ChromeDriver(capabilities);
        } else if (FIREFOX.equals(browser)) {

            FirefoxProfile ffProfile = setFFProfileNoBSP();
            webDriver = new FirefoxDriver(ffProfile);

        }

        return webDriver;
    }
    private static DesiredCapabilities setChromeCapabilities(DesiredCapabilities capabilities) {
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("profile.content_settings.pattern_pairs.*.multiple-automatic-downloads", 1);
//Turns off download prompt
        prefs.put("download.prompt_for_download", false);
        prefs.put("extensions.ui.developer_mode", true);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--test-type");

        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return capabilities;
    }
    private static void setChromeDriver() {
        String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
        String chromeBinary = "src/main/resources/drivers/chrome/chromedriver"
                + (os.equals("win") ? ".exe" : "");
        System.setProperty("webdriver.chrome.driver", chromeBinary);
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
    private static DesiredCapabilities setVersionAndPlatform(
            DesiredCapabilities capability, String version, String platform) {
        if (MAC.equalsIgnoreCase(platform)) {
            capability.setPlatform(Platform.MAC);
        } else if (LINUX.equalsIgnoreCase(platform)) {
            capability.setPlatform(Platform.LINUX);
        } else if (XP.equalsIgnoreCase(platform)) {
            capability.setPlatform(Platform.XP);
        } else if (VISTA.equalsIgnoreCase(platform)) {
            capability.setPlatform(Platform.VISTA);
        } else if (WINDOWS.equalsIgnoreCase(platform)) {
            capability.setPlatform(Platform.WINDOWS);
        }

        if (version != null) {
            capability.setVersion(version);
        }
        return capability;
    }
}
