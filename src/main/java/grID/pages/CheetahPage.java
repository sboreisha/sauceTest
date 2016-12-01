package grID.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class CheetahPage extends Page {

    public CheetahPage(WebDriver webDriver) {
        super(webDriver);
    }

    @FindBy(how = How.CSS, using = ".title.ng-scope")
    private WebElement cheetahResultCount;

    @FindBy(how = How.CSS, using = ".index")
    private WebElement searchResultIndex;

    public int getCheetahSearchCount() {
        WebDriverWait wait = new WebDriverWait(webDriver, 120);
        wait.until(ExpectedConditions.visibilityOf(searchResultIndex));
        String str = cheetahResultCount.getText();
        return Integer.parseInt(str.substring(0, str.indexOf(" ")));
    }

    ;


}
