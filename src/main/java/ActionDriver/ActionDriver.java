package ActionDriver;

import Utilities.PropertyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ActionDriver {

    public WebDriver driver;
    public WebDriverWait wait;

    private static final Logger logger = LogManager.getLogger(ActionDriver.class);

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
    public void EnterText(WebElement element,String text){
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















}
