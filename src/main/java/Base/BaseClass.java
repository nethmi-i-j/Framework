package Base;

import Utilities.PropertyFileReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

public class BaseClass {

    private static final Logger logger = LogManager.getLogger(BaseClass.class);

    public static WebDriver driver;

    @BeforeMethod
    public void openPage(){

        String browser = PropertyFileReader.getInstance().getProperty("config", "browser");
        long wait = Long.parseLong(PropertyFileReader.getInstance().getProperty("config", "impWait"));
        String url = PropertyFileReader.getInstance().getProperty("config", "url");

        switch (browser.toLowerCase()){
            case "chrome":
                driver = new ChromeDriver(); break;
            case "edge":
                driver = new EdgeDriver(); break;
            case "firefox":
                driver = new FirefoxDriver(); break;
            default:
                System.out.println(browser + "not supported");
        }
        logger.info("Test cases automating with: " + browser);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(wait));
        driver.get(url);
    }


    @AfterMethod
    public void closeBrowser(){
        driver.quit();
    }
}
