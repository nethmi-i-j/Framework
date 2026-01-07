package Utilities;

import Base.BaseClass;
import org.testng.IAnnotationTransformer;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ListenerManager extends BaseClass implements ITestListener, IAnnotationTransformer {



    // implements unimplemented methods belongs to the ITestListener interface, then used @Override
    @Override
    public void onTestStart(ITestResult result){
        String testName = result.getTestClass().getName() + "-" + result.getMethod().getMethodName();
        ExtentReporterUtility.createTest(testName);
        ExtentReporterUtility.test.info("Started Test Execution: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result){
        ExtentReporterUtility.test.pass("TestCase Passed: "+ result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result){
        ExtentReporterUtility.test.fail(result.getName() + "Test Case Failed");
        ExtentReporterUtility.test.fail(result.getThrowable());  // fail reason
        ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_FailCase(driver,result.getName()+"_failed_point_screenshot");
    }

    @Override
    public void onTestSkipped(ITestResult result){
        ExtentReporterUtility.test.skip(result.getName()+ "Test Case Skipped");
        ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_SkipCase(driver,result.getName()+"_skipped_point_screenshot");

    }




}
