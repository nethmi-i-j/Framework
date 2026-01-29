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

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

    private static final Logger logger = LoggerManager.getLogger(BaseClass.class);

    @BeforeSuite
    public void beforeSuite(){

        ExtentReporterUtility.startReporter();

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
        logger.info("Setting up webDriver for: " + this.getClass().getSimpleName());
        launchBrowser();
        configureBrowser();
        staticWait(2);

        logger.info("WebDriver initialized and browser maximized");

        // Initialize ActionDriver for the current thread
        actionDriver.set(new ActionDriver(getDriver()));
        logger.info("ActionDriver initialized for thread: " + Thread.currentThread().threadId());
    }

    private void launchBrowser(){
        switch (browser.toLowerCase()){
            case "chrome":
                driver.set(new ChromeDriver()); break;

            case "edge":
                driver.set(new EdgeDriver()); break;

            case "firefox":
                driver.set(new FirefoxDriver()); break;

            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        logger.info("Test cases automating with: " + browser);
    }

    private void configureBrowser(){
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));

        try {
            getDriver().get(url);
        } catch (Exception e) {
            logger.error("Failed to navigate to the url: " + e.getMessage());
        }
        logger.info("Navigated to the page: " + url);
    }

    @AfterMethod
    public void closeBrowser(){
        if (getDriver() != null){
            try {
                getDriver().quit();
            } catch (Exception e) {
                System.out.println("Unable to quit the browser: " + e.getMessage());
            }
        }
        logger.info("WebDriver instance is closed");

        driver.remove();
        actionDriver.remove();
    }

    // ========= Getters ==========

    // Driver getter method
    public static WebDriver getDriver(){
        if (driver.get() == null){
            logger.error("WebDriver is not initialized");
            throw new IllegalStateException("WebDriver is not initialized");
        }
        return driver.get();
    }

    // ActionDriver getter method
    public static ActionDriver getActionDriver(){
        if (actionDriver.get() == null){
            logger.error("ActionDriver is not initialized");
            throw new IllegalStateException("ActionDriver is not initialized");
        }
        return actionDriver.get();
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
