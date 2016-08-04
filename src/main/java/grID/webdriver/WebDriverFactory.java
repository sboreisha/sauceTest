package grID.webdriver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Platform;
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

import com.opera.core.systems.OperaDriver;
import grID.util.Browser;
import grID.webdriver.AuthenticatedHtmlUnitDriver;


public class WebDriverFactory {

    /* Browsers constants */
    public static final String CHROME = "chrome";
    public static final String FIREFOX = "firefox";
    public static final String OPERA = "opera";
    public static final String INTERNET_EXPLORER = "ie";
    public static final String SAFARI = "safari";
    public static final String HTML_UNIT = "htmlunit";
    public static final String IPHONE = "iphone";

    /* Platform constants */
    public static final String WINDOWS = "windows";
    public static final String ANDROID = "android";
    public static final String XP = "xp";
    public static final String VISTA = "vista";
    public static final String MAC = "mac";
    public static final String LINUX = "linux";

    private WebDriverFactory() {
    }

    /*
     * Factory method to return a RemoteWebDriver instance given the url of the
     * Grid hub and a Browser instance.
     *
     * @param gridHubUrl : grid hub URI
     *
     * @param browser : Browser object containing info around the browser to hit
     *
     * @param username : username for BASIC authentication on the page to test
     *
     * @param password : password for BASIC authentication on the page to test
     *
     * @return RemoteWebDriver
     */
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

            FirefoxProfile ffProfile = setFFProfile();

            // Authenication Hack for Firefox
            if (username != null && password != null) {
                ffProfile.setPreference("network.http.phishy-userpass-length",
                        255);
                capability.setCapability(FirefoxDriver.PROFILE, ffProfile);
            }

            capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        } else if (INTERNET_EXPLORER.equals(browserName)) {

            capability = DesiredCapabilities.internetExplorer();
            capability
                    .setCapability(
                            InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                            true);
            capability.setCapability("prerun", "sauce-storage:bspCheetah.exe");
        } else if (OPERA.equals(browserName)) {
            capability = DesiredCapabilities.operaBlink();
        } else if (SAFARI.equals(browserName)) {
            capability = DesiredCapabilities.safari();
        } else if (ANDROID.equals(browserName)) {
            capability = DesiredCapabilities.android();
        } else if (IPHONE.equals(browserName)) {
            capability = DesiredCapabilities.iphone();
        } else {

            capability = DesiredCapabilities.htmlUnit();
            // HTMLunit Check
            if (username != null && password != null) {
                webDriver = (HtmlUnitDriver) AuthenticatedHtmlUnitDriver
                        .create(username, password);
            } else {
                webDriver = new HtmlUnitDriver(true);
            }

            return webDriver;
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

    /*
     * Factory method to return a WebDriver instance given the browser to hit
     *
     * @param browser : String representing the local browser to hit
     *
     * @param username : username for BASIC authentication on the page to test
     *
     * @param password : password for BASIC authentication on the page to test
     *
     * @return WebDriver instance
     */
    private static String defaultDir = "../bsp/BuildInstaller/bin/";

    public static WebDriver getInstance(String browser, String username,
                                        String password) {

        WebDriver webDriver = null;

        if (CHROME.equals(browser)) {
            setChromeDriver();
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities = setChromeCapabilities(capabilities);
            webDriver = new ChromeDriver(capabilities);
        } else if (FIREFOX.equals(browser)) {

            FirefoxProfile ffProfile = setFFProfile();
            webDriver = new FirefoxDriver(ffProfile);

        } else if (INTERNET_EXPLORER.equals(browser)) {
            isSupportedPlatform(browser);
            webDriver = new InternetExplorerDriver();

        } else if (OPERA.equals(browser)) {
            webDriver = new OperaDriver();

        } else if (SAFARI.equals(browser)) {
            isSupportedPlatform(browser);
            webDriver = new SafariDriver();

        } else if (IPHONE.equals(browser)) {
            webDriver = new RemoteWebDriver(DesiredCapabilities.iphone());

        } else if (ANDROID.equals(browser)) {
            webDriver = new RemoteWebDriver(DesiredCapabilities.android());

        } else {

            // HTMLunit Check
            if (username != null && password != null) {
                webDriver = (HtmlUnitDriver) AuthenticatedHtmlUnitDriver
                        .create(username, password);
            } else {
                webDriver = new HtmlUnitDriver(true);
            }
        }

        return webDriver;
    }

    /*
     * Helper method to set version and platform for a specific browser
     *
     * @param capability : DesiredCapabilities object coming from the selected
     * browser
     *
     * @param version : browser version
     *
     * @param platform : browser platform
     *
     * @return DesiredCapabilities
     */

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
        } else if (ANDROID.equalsIgnoreCase(platform)) {
            capability.setPlatform(Platform.ANDROID);
        } else {
            capability.setPlatform(Platform.ANY);
        }

        if (version != null) {
            capability.setVersion(version);
        }
        return capability;
    }

    /*
     * Helper method to set ChromeDriver location into the right ststem property
     */
    private static void setChromeDriver() {
        String os = System.getProperty("os.name").toLowerCase().substring(0, 3);
        String chromeBinary = "src/main/resources/drivers/chrome/chromedriver"
                + (os.equals("win") ? ".exe" : "");
        System.setProperty("webdriver.chrome.driver", chromeBinary);
    }

    private static void isSupportedPlatform(String browser) {
        boolean is_supported = true;
        Platform current = Platform.getCurrent();
        if (INTERNET_EXPLORER.equals(browser)) {
            is_supported = Platform.WINDOWS.is(current);
        } else if (SAFARI.equals(browser)) {
            is_supported = Platform.MAC.is(current) || Platform.WINDOWS.is(current);
        }
        assert is_supported : "Platform is not supported by " + browser.toUpperCase() + " browser";
    }

    private static DesiredCapabilities setChromeCapabilities(DesiredCapabilities capabilities) {
        ChromeOptions options = new ChromeOptions();
        File ext2 = new File(defaultDir + "bspCheetah.crx");
        if (ext2.exists() && !ext2.isDirectory()) {
            options.addExtensions(ext2);
        }
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", 0);
        prefs.put("download.default_directory", defaultDir);
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

    private static FirefoxProfile setFFProfile() {
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
        ffProfile.setPreference("browser.download.dir", defaultDir);
        ffProfile.setPreference("browser.search.defaultenginename", "Google");
        ffProfile.setPreference("browser.search.defaultenginename.US", "data:text/plain,browser.search.defaultenginename.US=Google");
        ffProfile.setPreference("browser.search.countryCode", "US");
        ffProfile.setPreference("browser.startup.homepage", "https://www.google.com/webhp?lr=&ie=UTF-8&oe=UTF-8&gws_rd=cr,ssl&ei=xoc0V7ybNMOnsAHJxpCwBg");

        try {
            File ext = new File(defaultDir + "bspCheetah.xpi");
            if (ext.exists() && !ext.isDirectory()) {
                ffProfile.addExtension(ext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       /* // Authenication Hack for Firefox
        if (username != null && password != null) {
            ffProfile.setPreference("network.http.phishy-userpass-length",
                    255);
        }*/
        return ffProfile;
    }
}
