package Utilities;

import Base.BaseClass;

import org.testng.IAnnotationTransformer;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ListenerManager extends BaseClass implements ITestListener, IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

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
        ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_FailCase(getDriver(),result.getName()+"_failed_point_screenshot");
    }

    @Override
    public void onTestSkipped(ITestResult result){
        ExtentReporterUtility.test.skip(result.getName()+ "Test Case Skipped");
        ExtentReporterUtility.captureScreenshotAsBase64_ReportOnly_SkipCase(getDriver(),result.getName()+"_skipped_point_screenshot");

    }




}
