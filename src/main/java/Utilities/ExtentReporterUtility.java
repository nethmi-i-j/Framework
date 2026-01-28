package Utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ExtentReporterUtility {

    public static ExtentSparkReporter sparkReporter;
    public static ExtentReports extent;
    public static ExtentTest test;

    public static void startReporter(){
        sparkReporter = new ExtentSparkReporter(System.getProperty("user.dir")+"//TestOutputs//ExtentReports//ExtentReport.html");
        sparkReporter.config().setDocumentTitle("Automation Test Report");
        sparkReporter.config().setReportName("Extent Report");
        sparkReporter.config().setTheme(Theme.DARK);

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Environment","QA");
        extent.setSystemInfo("Project Name","Test Automation");
        extent.setSystemInfo("Browser","Chrome");
        extent.setSystemInfo("Operating System",System.getProperty("os.name"));
        extent.setSystemInfo("Java Version",System.getProperty("java.version"));
        extent.setSystemInfo("User Name",System.getProperty("user.name"));
    }

    public static void createTest(String testName){
        test = extent.createTest(testName);
    }

    public static void endReport(){
        extent.flush();
    }

    public static void captureScreenshotAsBase64_ReportOnly_StepInfo(WebDriver driver, String message){
        String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        test.info(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
    }

    public static void stepInfo(String message){
        test.info(message);
    }

    public static void captureScreenshotAsBase64_ReportOnly_FailCase(WebDriver driver, String message){
        String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        test.fail(message, MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
    }

    public static void failureInfo(String message){
        test.fail(message);
    }



    public static void captureScreenshotAsBase64_ReportOnly_SkipCase(WebDriver driver, String message){
        String base64 = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        test.skip(message,MediaEntityBuilder.createScreenCaptureFromBase64String(base64).build());
    }
}
