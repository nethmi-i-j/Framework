package ActionDriver;

import Base.BaseClass;
import Utilities.ExtentReporterUtility;
import Utilities.LoggerManager;
import Utilities.PropertyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class ActionDriver {

    private WebDriver driver;
    private WebDriverWait wait;

    private static final Logger logger = LoggerManager.getLogger(ActionDriver.class);

    public ActionDriver(WebDriver driver) {
        this.driver = driver;
        long exWait = Long.parseLong(PropertyFileReader.getInstance().getProperty("config", "expWait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(exWait));
    }


    private void waitForElementToBeClickable(WebElement element){
        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            logger.error("Element is not clickable: " + e.getMessage());
        }
    }

    private void waitForElementToBeVisible(WebElement element){
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            logger.error("Element is not visible: " + e.getMessage());
        }
    }

    // Method to click an element
    public void click(WebElement element){
        try {
            waitForElementToBeClickable(element);
            element.click();
        } catch (Exception e) {
            logger.error("Unable to click element: " + e.getMessage());
        }
    }

    // Method to clear text
    public void clearText(WebElement element){
        try {
            waitForElementToBeVisible(element);
            element.clear();
        } catch (Exception e) {
            logger.error("Unable to clear text: " + e.getMessage());
        }
    }

    // Method to enter text
    public void enterText(WebElement element,String text){
        try {
            waitForElementToBeVisible(element);
            element.sendKeys(text);
        } catch (Exception e) {
            logger.error("Unable to enter text: " + e.getMessage());
        }
    }

    // Method to append text
    public void AppendText(WebElement element,String text){
        try {
            waitForElementToBeVisible(element);
            element.sendKeys(" " + text);
        } catch (Exception e) {
            logger.error("Unable to append text: " + e.getMessage());
        }
    }


    // Method to get text
    public String getText(WebElement element){
        try {
            waitForElementToBeVisible(element);
            return element.getText();
        } catch (Exception e) {
            logger.error("Unable to get text: " + e.getMessage());
            return "";
        }
    }

    // method to ensure the keyboard actions with the texts
    public void EnterValuesUsingKeyboardActions(WebElement textBox, String value1, String value2){
        try {
            waitForElementToBeVisible(textBox);
            textBox.sendKeys(value1 + Keys.TAB + value2);
            logger.info("Successfully done the key board actions");
        } catch (Exception e) {
            logger.error("Unable to perform key board actions" + e.getMessage());
        }
    }

    // Method to compare 2 texts
    public boolean compareTexts(WebElement element, String expectedText) {
        try {
            waitForElementToBeVisible(element);
            String actualText = element.getText();
            if (actualText.equals(expectedText)){
                logger.info("Texts are matching: " + actualText + "equals" + expectedText);
                return true;
            }else {
                logger.info("Texts are not matching: " + actualText + "not equals" + expectedText);
                return false;
            }
        } catch (Exception e) {
            logger.error("Unable to compare: " + e.getMessage());
        }
        return false;
    }

    // Method to check is element displayed
    public boolean isDisplayed(WebElement element){
        try {
            waitForElementToBeVisible(element);
            return element.isDisplayed();
        } catch (Exception e) {
            logger.error("Element is not displayed: " + e.getMessage());
            return false;
        }
    }

    // Method to check is element enabled
    public boolean isEnabled(WebElement element){
        try {
            waitForElementToBeVisible(element);
            return element.isEnabled();
        } catch (Exception e) {
            logger.error("Element is not enabled: " + e.getMessage());
            return false;
        }
    }

    // Method to check is element selected
    public boolean isSelected(WebElement element){
        try {
            waitForElementToBeVisible(element);
            return element.isSelected();
        } catch (Exception e) {
            logger.error("Element is not selected: " + e.getMessage());
            return false;
        }
    }

    // Method to check a string value is not null and empty
    public boolean isEmpty(String value){
        try {
            return value !=null && value.isEmpty();
        } catch (Exception e) {
            logger.error("String is null and empty: " + e.getMessage());
            return false;
        }
    }

    //Method to truncate string
    public String truncate(String value, int maxLength){
        if (value == null || value.length() <= maxLength){
            return  value;
        }
        return  value.substring(0, maxLength) + "___";
    }


    // ==================== Select Methods ===================

    // Method to select a dropdown by visible text
    public void selectByVisibleText(WebElement element, String text){
        try {
            new Select(element).selectByVisibleText(text);
            logger.info("Dropdown selected by visible text: " + text);
        } catch (Exception e) {
            logger.error("Unable to select by visible text: " + text,e);
        }
    }

    // Method to select a dropdown by value
    public void selectByValue(WebElement element, String value){
        try {
            new Select(element).selectByValue(value);
            logger.info("Dropdown selected by actual value: " + value);
        } catch (Exception e) {
            logger.error("Unable to select by actual value: " + value,e);
        }
    }

    // Method to select a dropdown by index
    public void selectByIndex(WebElement element, int index){
        try {
            new Select(element).selectByIndex(index);
            logger.info("Dropdown selected by index: " + index);
        } catch (Exception e) {
            logger.error("Unable to select by index: " + index,e);
        }
    }

    // Method to get all options in dropdown list
    public List<String> getDropdownOptions(WebElement dropdown){
        List<String> optionList = new ArrayList<>();
        try {
            Select select = new Select(dropdown);
            for (WebElement option : select.getOptions()){
                optionList.add(option.getText());
            }
        } catch (Exception e) {
            logger.error("Unable to get dropdown options : " + e.getMessage());
        }
        return optionList;
    }

    // ==================== JavaScript utility Methods ===================

    // Wait for the page to load
    public void waitForPageLoad(int timeOutInSec){
        try {
            wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor)WebDriver)
                    .executeScript("return document.readyState").equals("complete"));
            logger.info("Page loaded successfully");
        } catch (Exception e) {
            logger.error("Page did not load within: " + timeOutInSec + "seconds. Exception: " + e.getMessage());
        }
    }

    // Scroll to an element -- Added a ; at the end of script string
    public void scrollToElement(WebElement element){
        try{
            //applyBorder(element,"green");
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("argument[0].scrollToView(true);",element);
        } catch (Exception e) {
            //applyBorder(element,"red");
            logger.error("Unable to locate element: " + e.getMessage());
        }
    }

    // Method to scroll to the bottom of the page
    public void scrollToBottom(){
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
            logger.info("Scrolled to the bottom of the page");
        } catch (Exception e) {
            logger.error("Unable to scroll to the bottom",e);
        }
    }

    // Method to boarder an element
    public void applyBorder(WebElement element, String colour) {
        try{
            String script = "argument[0].style.border='3px solid " + colour + "'";
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript(script,element);
        } catch (Exception e) {
            throw e;
        }
    }

    // Method to click using JavaScript
    public void clickUsingJS(WebElement element){
        try {
            waitForElementToBeVisible(element);
            ((JavascriptExecutor) driver).executeScript("argument[0].click();",element);
        } catch (Exception e) {
            logger.error("Unable to click using Javascript",e);
        }
    }

    // Method to highlight an element using JavaScript
    public void highlightElementJS(WebElement element){
        try {
            ((JavascriptExecutor) driver).executeScript("argument[0].style.border='3px solid yellow'",element);
        } catch (Exception e) {
            logger.error("Unable to highlight an element using js",e);
        }
    }


    // =========================== Window and Frame handling =========================

    public void closeCurrentBrowserTabAndSwitchToNewTab(){
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 1) {
            driver.switchTo().window(tabs.get(0));
            driver.close();
            driver.switchTo().window(tabs.get(1));
        }
    }

    public void closeCurrentBrowserTabAndSwitchToOldTab(){
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        if (tabs.size() > 1) {
            driver.close();
            driver.switchTo().window(tabs.get(0));
        }
    }

    public void closeAllWindowExceptPrimary(String oldWindowReference){
        Set<String> newWindowHandles = driver.getWindowHandles();
        for (String allWindows : newWindowHandles) {
            if (!allWindows.equals(oldWindowReference)) {
                driver.switchTo().window(allWindows);
                driver.close();
            }
        }
        driver.switchTo().window(oldWindowReference);
    }

    //Method to switch between browser windows
    public void switchToWindows(String windowTitle){
        try {
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                driver.switchTo().window(window);
                if (driver.getTitle().equals(windowTitle)) {
                    logger.info("Switched to window: " + windowTitle);
                    return;
                }
            }
            logger.warn("Window with title " + windowTitle + "not found");
        } catch (Exception e) {
            logger.error("Unable to switch window", e);
        }
    }

    // Method to switch to an iframe
    public void switchToFrameByElement(WebElement element){
        try {
            //waitForElementToBeVisible(element);
            driver.switchTo().frame(element);
        } catch (Exception e){
            logger.error("Unable to switch to iframe");
        }
    }

    public boolean switchToFrameByIndex(int index){
        boolean flag = false;
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//iframe")));
            driver.switchTo().frame(index);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with index \"" + index + "\" is selected ");
            } else {
                System.out.println("Frame with index \"" + index + "\" is not selected ");
            }
        }
    }

    public boolean switchToFrameByID(String idValue){
        boolean flag = false;
        try {
            driver.switchTo().frame(idValue);
            flag = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with id \"" + idValue + "\" is selected ");
            } else {
                System.out.println("Frame with id \"" + idValue + "\" is not selected ");
            }
        }
    }

    public boolean switchToFrameByName(String nameValue){
        boolean flag = false;
        try {
            driver.switchTo().frame(nameValue);
            flag = true;
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (flag) {
                System.out.println("Frame with name \"" + nameValue + "\" is selected ");
            } else {
                System.out.println("Frame with name \"" + nameValue + "\" is not selected ");
            }
        }
    }

    // Method to switch back to the default content  (back into main html DOM)
    public void switchToDefaultContent(){
        driver.switchTo().defaultContent();
        logger.info("Switched back to default content");
    }

    // ========================== Alert Handling ==================================

    // Method to accept an alert popup
    public void acceptAlert(){
        try {
            driver.switchTo().alert().accept();
            logger.info("Alert accepted");
        } catch (Exception e) {
            logger.error("No alert found to accept", e);
        }
    }

    // Method to dismiss an alert popup
    public void dismissAlert(){
        try {
            driver.switchTo().alert().dismiss();
            logger.info("Alert dismissed");
        } catch (Exception e) {
            logger.error("No alert found to dismiss", e);
        }
    }

    // Method to get alert text
    public String getAlertText(){
        try {
            return driver.switchTo().alert().getText();
        } catch (Exception e) {
            logger.error("No alert text found ", e);
            return "";
        }
    }

    // Method to enter text into alert box
    public void enterTextToAlertBox(String textValue){
        try {
            driver.switchTo().alert().sendKeys(textValue);
            acceptAlert();
        } catch (Exception e) {
            logger.error("Unable to enter text into alert box ", e);
        }
    }

    // ===================== Browser Actions =========================

    public void refreshPage(){
        try {
            driver.navigate().refresh();
            ExtentReporterUtility.stepInfo("Page refreshed successfully");
            logger.info("page refreshed successfully");
        } catch (Exception e) {
            ExtentReporterUtility.failureInfo("page refreshed fail");
            logger.info("Unable to refresh page" + e.getMessage());
        }
    }

    public String getCurrentURL(){
        try {
            String url = driver.getCurrentUrl();
            logger.info("Current URL fetched" + url);
            return url;
        } catch (Exception e) {
            logger.info("Unable to fetch current URL" + e.getMessage());
            return null;
        }
    }

    public void maximizeWindow(){
        try {
            driver.manage().window().maximize();
            ExtentReporterUtility.stepInfo("Browse window maximized");
            logger.info("Browse window maximized");
        } catch (Exception e) {
            ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_FailCase(driver,"maximize_window_failed");
            logger.info("Unable to maximize window" + e.getMessage());
        }
    }

    public void maximizeWindowToWantedSize(int width, int height){
        try {
            Dimension dimension = new Dimension(width,height);
            driver.manage().window().setSize(dimension);
            ExtentReporterUtility.stepInfo("Browse window maximized to width: " + width + "Height: " + height);
            logger.info("Browse window maximized to width: " + width + "Height: " + height);
        } catch (Exception e) {
            ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_FailCase(driver,"maximize_window_failed");
            logger.info("Unable to maximize window with given sizes" + e.getMessage());
        }
    }

    // ========================== Advanced WebElement Actions ==============================

    public void moveToElement(WebElement element){
        try {
            Actions actions = new Actions(driver);
            actions.moveToElement(element).perform();
        } catch (Exception e) {
            logger.error("Unable to move to element" + e.getMessage());
        }
    }

    public void dragAndDrop(WebElement source, WebElement target){
        try {
            Actions actions = new Actions(driver);
            actions.dragAndDrop(source, target).perform();
        } catch (Exception e) {
            logger.error("Unable to drag and drop" + e.getMessage());
        }
    }

    public void doubleClick(WebElement element){
        try {
            Actions actions = new Actions(driver);
            actions.doubleClick(element).perform();
        } catch (Exception e) {
            logger.error("Unable to double-click element   " + e.getMessage());
        }
    }

    public void rightClick(WebElement element){
        try {
            Actions actions = new Actions(driver);
            actions.contextClick(element).perform();
        } catch (Exception e) {
            logger.error("Unable to right-click element   " + e.getMessage());
        }
    }

    public void sendKeysWithActions(WebElement element, String value){
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(element, value).perform();
        } catch (Exception e) {
            logger.error("Unable to send keys to element   " + e.getMessage());
        }
    }

    public void actionsClick(WebElement element){
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click(element).perform();
    }

    // Method to upload a file
    public void uploadFile(WebElement element, String filePath){
        try {
            element.sendKeys(filePath);
            logger.info("Uploaded file:" + filePath);
        } catch (Exception e) {
            logger.info("Unable to uploaded file:" + e.getMessage());
        }
    }

    // Method to upload a file - window dialog box
    public void uploadFileInToWindowsBasedDialogBox(String filePath) throws InterruptedException, AWTException {
        StringSelection selection = new StringSelection(filePath);
        // Coping the file path to clipBoard
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,null);

        Thread.sleep(4000);
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);

        Thread.sleep(3000);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    // Get current date
    public String getCurrentTime(){
        String currentDate = new SimpleDateFormat("yyyy-MM-dd-hhmmss").format(new Date());
        return currentDate;
    }

    // ======================== Links ===============================

    // Method to find link destination
    public String linkDestination(WebElement linkElement){
        try {
            waitForElementToBeVisible(linkElement);
            String linkedAttribute = linkElement.getAttribute("href");
            logger.info("Link attribute fetched" + linkedAttribute);
            return linkedAttribute;
        } catch (Exception e) {
            logger.error("Unable to fetch link attribute" + e.getMessage());
            return null;
        }
    }

    // links count

    // Method to check is link direct to expected destination using getTitle
    public void isLinkValidByGetTitle(WebElement link, String expectedTitle){
        try {
            waitForElementToBeClickable(link);
            link.click();
            String pageTitle = driver.getTitle();
            if (expectedTitle.equals(pageTitle)){
                logger.info("Link is valid: " + pageTitle);
                ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_StepInfo(driver,"broken_link_page");
            }else {
                logger.info("Link is not valid: " + pageTitle);
            }
        } catch (Exception e) {
            logger.error("");
        }
    }

    // Method to check isLinkBroken of all links

    public void isLinkBrokenAllLinks(List<WebElement> linkList) throws IOException {
        int brokenLinks = 0;
        for (WebElement element : linkList) {
            String url = element.getAttribute("href");
            if (url == null || url.isEmpty()) {
                logger.warn("URL is empty");
                continue;
            }
            URL link = new URL(url);
            try {
                HttpURLConnection httpConn = (HttpURLConnection) link.openConnection();
                httpConn.connect();
                if (httpConn.getResponseCode()>=400){
                    logger.info(url + "is broken");
                    brokenLinks++;
                } else{
                    logger.info(url + "is a valid link");
                }
            } catch (Exception e) {
                logger.error("Unable to capture the URLs" + e.getMessage());
            }
        }
        logger.info("Number of broken links: " + brokenLinks);
    }


    // ========================= Buttons ===============================

    // Method to get button position
    public void getBtnPosition(WebElement element){
        try {
            waitForElementToBeVisible(element);
            Point xyPoint = element.getLocation();
            int x = xyPoint.getX();
            int y = xyPoint.getY();
            logger.info("x position is: " + x + "," + "y position is: " + y);
        } catch (Exception e) {
            logger.error("Unable to get location of the button");
        }
    }

    // Method to get button colour
    public void getBtnColour(WebElement element){
        try {
            waitForElementToBeVisible(element);
            element.getCssValue("background-color");
            logger.info("");
        } catch (Exception e) {
            logger.error("Unable to get colour of the button");
        }
    }






















}
