package Base;

import ActionDriver.ActionDriver;
import Utilities.ExtentReporterUtility;
import Utilities.LoggerManager;
import Utilities.PropertyFileReader;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class BaseClass {

    protected static WebDriver driver;
    private static ActionDriver actionDriver;

    private static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void beforeSuite(){

        ExtentReporterUtility.startReporter();

//        File logDir = new File("logs");
//        File logFile = new File(logDir, "Automation.log");

        String logFilePath = "logs/Automation.log";
        File logFile = new File(logFilePath);

        try {
            if (logFile.exists()) {
                FileUtils.write(logFile, "", false);
                logger.info("Log file cleaned: " + logFilePath);
            } else {
                FileUtils.touch(logFile);
                logger.info("Log file created hence not exist: " + logFilePath);
            }
        } catch (IOException e) {
            logger.info("error cleaning or creating the log file: " + e.getMessage());
        }
    }

    String browser = PropertyFileReader.getInstance().getProperty("config", "browser");
    long wait = Long.parseLong(PropertyFileReader.getInstance().getProperty("config", "impWait"));
    String url = PropertyFileReader.getInstance().getProperty("config", "url");

    @BeforeMethod
    public void openPage(){
        System.out.println("Setting up webDriver for: " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        logger.info("WebDriver initialized and browser maximized");

        // Initialize the actionDriver only once
        if (actionDriver == null) {
            actionDriver = new ActionDriver(driver);
            logger.info("ActionDriver instance is created");
        }
    }

    private void launchBrowser(){
        switch (browser.toLowerCase()){
            case "chrome":
                driver = new ChromeDriver(); break;
            case "edge":
                driver = new EdgeDriver(); break;
            case "firefox":
                driver = new FirefoxDriver(); break;
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        logger.info("Test cases automating with: " + browser);
    }

    private void configureBrowser(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        try {
            driver.get(url);
        } catch (Exception e) {
            logger.error("Failed to navigate to the url: " + e.getMessage());
        }
        logger.info("Navigated to the page: " + url);
    }

    @AfterMethod
    public void closeBrowser(){
        if (driver!=null){
            try {
                driver.quit();
            } catch (Exception e) {
                System.out.println("Unable to quit the browser: " + e.getMessage());
            }
        }
        logger.info("WebDriver instance is closed");

        driver = null;
        actionDriver = null;
    }

    // Driver getter method
    public static WebDriver getDriver(){
        if (driver == null){
            System.out.println("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver;
    }

    // ActionDriver getter method
    public static ActionDriver getActionDriver(){
        if (actionDriver == null){
            //actionDriver = new ActionDriver(driver);
            System.out.println("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver;
    }



    // Driver setter method
    public void setDriver(WebDriver driver){
        this.driver = driver;
    }

    // Static wait for pause
    public void staticWait(int seconds){
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
    }

    @AfterSuite
    public void afterSuite(){
        ExtentReporterUtility.endReport();

    }
}
