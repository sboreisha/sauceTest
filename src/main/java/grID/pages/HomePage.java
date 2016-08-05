package grID.pages;

import grID.util.MyFileReader;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;


public class HomePage extends Page {

    @FindBy(how = How.CSS, using = ".settings_btn")
    private WebElement expandBSP;

    @FindBy(how = How.ID, using = "wkbsContainer")
    private WebElement bspDiv;

    @FindBy(how = How.CSS, using = ".scroll-content")
    private WebElement searchWindow;

    @FindBy(how = How.CSS, using = ".search-item-content")
    public WebElement bspSearchItem;

    @FindBy(how = How.CSS, using = "a.title.ng-binding")
    public WebElement searchResult;

    @FindBy(how = How.CSS, using = ".wk-button.small")
    private WebElement goToCheetahButton;

    @FindBy(how = How.ID, using = "vLogin_UserEmail")
    public WebElement loginInputBSP;

    @FindBy(how = How.ID, using = "vLogin_Password")
    private WebElement passwordInputBSP;

    @FindBy(how = How.ID, using = "vLogin")
    private WebElement loginButtonBSP;

    @FindBy(how = How.NAME, using = "q")
    private WebElement googleSearchInput;

    @FindBy(how = How.ID, using = "resultStats")
    private WebElement googleSearchResult;

    @FindBy(how = How.CSS, using = ".wk-brand-light")
    public WebElement wkBrand;

    @FindBy(how = How.ID, using = "wkbsContainer")
    public WebElement bspContainer;

    @FindBy(how = How.CSS, using = ".search-info")
    public WebElement bspSearchInfo;

    @FindBy(how = How.TAG_NAME, using = "iframe")
    public List<WebElement> iframes;

    @FindBy(how = How.ID, using = "wkbsIfarme")
    public WebElement iframe;

    @FindBy(how = How.TAG_NAME, using = "*")
    public List<WebElement> allElements;

    @FindBy(how = How.CSS, using = ".gb_Ac.gb_Rc")
    public WebElement closeGoogleEnginePopup;

    @FindBy(how = How.NAME, using = "p")
    private WebElement yahooSearchInput;

    @FindBy(how = How.CSS, using = ".searchCenterMiddle")
    private WebElement yahooSearchResult;

    @FindBy(how = How.ID, using = "b_results")
    private WebElement bingSearchResult;

    @FindBy(how = How.NAME, using = "q")
    private WebElement bingSearchInput;

    public void getAllElements() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.visibilityOf(bspContainer));
        for (WebElement el : allElements) {
            System.out.println("All ell tag +" + el.getAttribute("tag"));
            System.out.println("El name " + el.getAttribute("class"));
            System.out.println("El text " + el.getText());
            System.out.println("El name " + el.getAttribute("name"));
            System.out.println("---------------------------");
        }
    }

    public void getAllFrames() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        for (WebElement el : iframes) {
            System.out.println("EL+src " + el.getAttribute("src"));
            System.out.println("EL+ class " + el.getAttribute("class"));
            System.out.println("El+ id " + el.getAttribute("id"));
            System.out.println("----------------------");
        }
    }

    public int getBSPSearchCount() {
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        wait.until(ExpectedConditions.visibilityOf(bspSearchItem));
        String search = bspSearchInfo.getText();
        webDriver.switchTo().defaultContent();
        return Integer.parseInt(search.substring(2, search.indexOf(" ", 2)));
    }

    String PAGE_TEMPLATES_PATH = new File(getClass().getResource("/categories.txt").getFile()).getAbsolutePath();

    public HomePage(WebDriver webDriver) {
        super(webDriver);
    }

    public void threadSleep(int timout) {
        try {
            Thread.sleep(timout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void doGoogleSearch() {
        try {
            googleSearchInput.sendKeys(" next\n");
            googleSearchInput.submit();
            }catch (StaleElementReferenceException e1){
            Reporter.log("Stale element exception");
        }
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.visibilityOf(googleSearchResult));
        threadSleep(5000);
    }

    public void doYahooSearch(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.visibilityOf(yahooSearchInput));
        yahooSearchInput.sendKeys(text);
        yahooSearchInput.submit();
        wait.until(ExpectedConditions.visibilityOf(yahooSearchResult));
        threadSleep(5000);
    }

    public void doBingSearch(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.visibilityOf(bingSearchInput));
        bingSearchInput.sendKeys(text);
        bingSearchInput.submit();
        wait.until(ExpectedConditions.visibilityOf(bingSearchResult));
        threadSleep(5000);
    }

    public void expandPlugin() {
        webDriver.switchTo().defaultContent();
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        threadSleep(500);

        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));

        threadSleep(1500);
        if (isElementPresent(expandBSP)) {
            expandBSP.click();
            webDriver.switchTo().defaultContent();
        } else {
            System.out.println("Expand button is not displayed");
        }
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(loginButtonBSP));
        webDriver.switchTo().defaultContent();
    }

    public WebDriver getFF() {
        DesiredCapabilities capability = new DesiredCapabilities();
        capability = DesiredCapabilities.firefox();
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
        ffProfile.setPreference("browser.download.dir", "C:\\");
        ffProfile.setPreference("browser.search.defaultenginename", "Google");
        ffProfile.setPreference("browser.search.defaultenginename.US", "data:text/plain,browser.search.defaultenginename.US=Google");
        ffProfile.setPreference("browser.search.countryCode", "US");
        ffProfile.setPreference("browser.startup.homepage", "https://www.google.com/webhp?lr=&ie=UTF-8&oe=UTF-8&gws_rd=cr,ssl&ei=xoc0V7ybNMOnsAHJxpCwBg");

        try {
            File ext = new File("../bsp/BuildInstaller/bin/" + "bspCheetah.xpi");
            if (ext.exists() && !ext.isDirectory()) {
                ffProfile.addExtension(ext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Authenication Hack for Firefox
        capability.setCapability("platform", "Windows 8.1");
        capability.setCapability(FirefoxDriver.PROFILE, ffProfile);
        capability.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
        try {
            webDriver = new RemoteWebDriver(new URL("http://WK-Sergei:ae7faa3c-25ae-499c-a647-ef64451c7a81@ondemand.saucelabs.com:80/wd/hub"), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return webDriver;
    }

    public void doLogin() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(loginButtonBSP));
        //loginInputBSP.sendKeys("legal-wb-stg@wk.com");
        loginButtonBSP.sendKeys("bsp1@ct77.eu");
        passwordInputBSP.sendKeys("password");
        loginButtonBSP.submit();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(searchWindow));
    }

    private void doLoginCI() {
        webDriver.get("https://ci.wolterskluwer.io/browse/BSP-REL4/latest/artifact/shared/bin/");
        getWebDriver().findElement(By.id("loginForm_os_username")).sendKeys("Sergei.Boreisha");
        getWebDriver().findElement(By.id("loginForm_os_password")).sendKeys("Facecheck1!");
        getWebDriver().findElement(By.id("loginForm_save")).submit();
    }

    public void downloadPlugin(String browserName) throws InterruptedException {
        doLoginCI();
        switch (browserName) {
            case "firefox":
                webDriver.get("https://ci.wolterskluwer.io/browse/BSP-DEV1-3/artifact/INST/bin/bspCheetah.xpi");
                Thread.sleep(5000);
                Robot robo = null;
                try {
                    robo = new Robot();
                } catch (AWTException e) {
                    e.printStackTrace();
                }
                robo.keyPress(KeyEvent.VK_ENTER);
                robo.keyRelease(KeyEvent.VK_ENTER);
                break;
            case "chrome":
                webDriver.get("https://ci.wolterskluwer.io/browse/BSP-DEV1-3/artifact/INST/bin/bspCheetah_chrome.zip");
                Thread.sleep(5000);
                File oldFile = new File("c:\\bspCheetah_chrome.zip");
                File newFile = new File("c:\\bspCheetah.crx");
                oldFile.renameTo(newFile);
                break;
            default:
                System.out.println("No such browser");
        }

    }

    public String getSearchResult() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        webDriver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
        return searchResult.getText();
    }

    public void goToCheetah() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(goToCheetahButton));
        goToCheetahButton.click();
    }


    private void clickElementByLinkText(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText(text)));
        webDriver.findElement(By.partialLinkText(text)).click();
    }

    public boolean isElementPresent(WebElement el) {
        try {
            el.isDisplayed();
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public static boolean waitForNewTab(WebDriver driver, int timeout) {
        boolean check = false;
        int count = 0;
        while (!check) {
            try {
                Set<String> winHandle = driver.getWindowHandles();
                if (winHandle.size() > 1) {
                    check = true;
                    return check;
                }
                Thread.sleep(1000);
                count++;
                if (count > timeout) {
                    return check;
                }
            } catch (Exception e) {
            }
        }
        return check;
    }


    public void changeTab(Integer tab) {
        waitForNewTab(webDriver, 30);
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        if (tabs.size() > 1) {
            webDriver.switchTo().window(tabs.get(tab));
        }
    }


    public boolean isElementVisible(String cssLocator) {
        return webDriver.findElement(By.cssSelector(cssLocator)).isDisplayed();
    }

    private List<String> getPaths() {
        return MyFileReader.readFromFile(PAGE_TEMPLATES_PATH);
    }


    public void navigateToProducts(int pathIndex) {
        String pathString = getPaths().get(pathIndex);
        String[] pathArray = pathString.split(",");
        for (String linkName : pathArray) {
            clickElementByLinkText(linkName);
        }
    }

    public int returnNumberOfPages() {
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".b-pagination-list .p-item")));
        return webDriver.findElements(By.cssSelector(".b-pagination-list .p-item")).size();
    }

    public boolean waitForJSandJQueryToLoad() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long) ((JavascriptExecutor) getWebDriver()).executeScript("return jQuery.active") == 0);
                } catch (Exception e) {
                    // no jQuery present
                    return true;
                }
            }
        };
        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) getWebDriver()).executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }
}
