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

    @FindBy(how = How.CSS, using = ".wk-icon-menu")
    private WebElement bspSettings;

    @FindBy(how = How.CSS, using = ".wk-spin")
    private WebElement spinIcon;

    @FindBy(how = How.CSS, using = ".wk-checkbox-field input")
    private List<WebElement> bspOptions;

    @FindBy(how = How.CSS, using = ".collapse")
    public WebElement collapseIcon;

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

    public WebElement getBSPSearchItem() {
        boolean flag = false;
        int attempts = 2;

        while (attempts > 0) {
            try {
                bspSearchItem.getText();
                attempts--;
            } catch (StaleElementReferenceException e) {
                attempts--;
                System.out.println(e.toString());
            }
        }
        return bspSearchItem;
    }

    public void doClickSearchSortByDocumentType() {
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.elementToBeClickable(bspSettings));
        bspSettings.click();
        wait.until(ExpectedConditions.elementToBeClickable(bspOptions.get(0)));
        bspOptions.get(0).click();
        waitForJSandJQueryToLoad();
        // wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class).pollingEvery(1, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOf(spinIcon));
        // wait.ignoring(StaleElementReferenceException.class).ignoring(NoSuchElementException.class).until(ExpectedConditions.not(ExpectedConditions.visibilityOf(spinIcon)));
        //bspSettings.click();
        webDriver.switchTo().defaultContent();
    }

    public int getBSPSearchCount() {
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
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
        webDriver.switchTo().defaultContent();
        try {
            googleSearchInput.sendKeys(" next\n");
            googleSearchInput.submit();

            WebDriverWait wait = new WebDriverWait(webDriver, 60);
            wait.until(ExpectedConditions.visibilityOf(googleSearchResult));
        } catch (StaleElementReferenceException e1) {
            Reporter.log("Stale element exception");
        }
        waitForJSandJQueryToLoad();
    }

    public void doYahooSearch(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.visibilityOf(yahooSearchInput));
        yahooSearchInput.sendKeys(text);
        yahooSearchInput.submit();
        wait.until(ExpectedConditions.visibilityOf(yahooSearchResult));
        waitForJSandJQueryToLoad();
    }

    public void doBingSearch(String text) {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        wait.until(ExpectedConditions.visibilityOf(bingSearchInput));
        bingSearchInput.sendKeys(text);
        bingSearchInput.submit();
        wait.until(ExpectedConditions.visibilityOf(bingSearchResult));
        waitForJSandJQueryToLoad();
    }

    public void expandPlugin() {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        webDriver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
        wait.ignoring(StaleElementReferenceException.class).pollingEvery(1, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOf(expandBSP));
        expandBSP.click();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(loginButtonBSP));
        webDriver.switchTo().defaultContent();
    }

    public void showResults() {
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
        webDriver.switchTo().defaultContent();
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(iframe));
        wait.ignoring(StaleElementReferenceException.class).pollingEvery(1, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOf(expandBSP));
        expandBSP.click();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(bspSearchItem));
        webDriver.switchTo().defaultContent();
    }



    public void doLogin() {
        WebDriverWait wait = new WebDriverWait(webDriver, 30);
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(loginButtonBSP));
        //loginInputBSP.sendKeys("legal-wb-stg@wk.com");
        wait.until(ExpectedConditions.visibilityOf(loginInputBSP));
        loginInputBSP.sendKeys("bsp1@ct77.eu");
        passwordInputBSP.sendKeys("password");
        loginButtonBSP.submit();
        webDriver.switchTo().defaultContent();
        webDriver.switchTo().frame(iframe);
        wait.until(ExpectedConditions.visibilityOf(searchWindow));
        webDriver.switchTo().defaultContent();
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
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            WebDriverWait wait = new WebDriverWait(webDriver, 20);
            wait.ignoring(StaleElementReferenceException.class).pollingEvery(1, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOf(el));
            return el.isDisplayed();
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

    public void openNewTab()

    {
        webDriver.findElement(By.cssSelector("body")).sendKeys(Keys.CONTROL + "t");
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
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
        WebDriverWait wait = new WebDriverWait(webDriver, 60);
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
