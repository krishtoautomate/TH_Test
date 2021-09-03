package com.base;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Created by Krish on 06.06.2018. Modified : 11.06.2018
 */
public class TestBase {

  public WebDriver driver;
  public Map<Long, WebDriver> driverMap = new ConcurrentHashMap<Long, WebDriver>();
  protected TLDriverFactory tlDriverFactory = new TLDriverFactory();
  public static Logger log;
  protected ExtentHtmlReporter htmlReporter;
  protected static ExtentReports extent;
  protected ExtentTest test;

  @BeforeSuite
  public void setupSuit(ITestContext ctx) throws UnknownHostException {

    String suiteName = ctx.getCurrentXmlTest().getSuite().getName();

    // Create Log4J
    log = Logger.getLogger(suiteName);

    // Create Report folder if not exists
    File reportDir = new File(Constants.REPORT_DIR);
    if (!reportDir.exists())
      reportDir.mkdirs();

    // Today's All-Reports Appended to Report
    htmlReporter = new ExtentHtmlReporter(Constants.EXTENT_HTML_REPORT);
    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);

    // Report Settings
    htmlReporter.config().setTheme(Theme.DARK);
    htmlReporter.loadXMLConfig(Constants.EXTENT_REPORT_CONFIG_XML);

    // Environment details Table in Report
    extent.setSystemInfo("OS", System.getProperty("os.name"));
    extent.setSystemInfo("Host Name", Constants.HOST_OS);
    extent.setSystemInfo("Host Address", InetAddress.getLocalHost().getHostAddress());

  }

  @BeforeMethod(alwaysRun = true)
  @Parameters({"browser"})
  public synchronized void setupTest(@Optional String browser, ITestContext Testctx,
      Method method) {

    // get TestName, ClassName and MethodName
    // String testName = Testctx.getCurrentXmlTest().getName();
    String className = this.getClass().getName();
    String methodName = method.getName() + "(" + browser + ")";

    // Set & Get ThreadLocal Driver with Browser
    tlDriverFactory.setDriver();
    driver = tlDriverFactory.getDriver();
    driverMap.put(Thread.currentThread().getId(), tlDriverFactory.getDriver());
    driver = driverMap.get(Long.valueOf(Thread.currentThread().getId()));

    // Create Test in extent-Report
    test = extent.createTest(methodName);

    log.info("Test Started : " + className);

    test.log(Status.INFO, methodName);
  }



  @Parameters({"browser"})
  @AfterMethod(alwaysRun = true)
  public synchronized void getResult(ITestResult result, String browser) {

    if (driver != null) {

      ScreenShotManager screenShotManager = new ScreenShotManager(driver, log, test);

      if (result.getStatus() == ITestResult.FAILURE) {
        // Category as browser/device
        test.assignCategory(browser);

        try {
          String ScreenShot = screenShotManager.generateScreenshot();

          log.error(
              "***Failed Test case : " + result.getMethod().getMethodName() + " ~ " + browser);
          test.fail(
              "Failed Test case : " + result.getMethod().getMethodName() + " -- "
                  + result.getThrowable(),
              MediaEntityBuilder.createScreenCaptureFromPath(ScreenShot).build());

        } catch (IOException e) {
          test.fail("Failed Test case : " + result.getMethod().getMethodName() + " -- "
              + result.getThrowable());
        }
      } else if (result.getStatus() == ITestResult.SKIP) {
        extent.removeTest(test);// removes skipped tests
      } else if (result.getStatus() == ITestResult.SUCCESS) {
        // Category as browser/device
        test.assignCategory(browser);

        test.log(Status.PASS, result.getMethod().getMethodName() + " - Completed as Success");

      }
    }
  }

  @AfterMethod(alwaysRun = true)
  public synchronized void tearDown(ITestContext context) {

    // Removes the skipped tests
    Iterator<ITestResult> skippedTestCases = context.getSkippedTests().getAllResults().iterator();
    while (skippedTestCases.hasNext()) {
      ITestResult skippedTestCase = skippedTestCases.next();
      ITestNGMethod method = skippedTestCase.getMethod();
      if (context.getSkippedTests().getResults(method).size() > 0) {
        log.info("Removing:" + skippedTestCase.getTestClass().toString());
        skippedTestCases.remove();
      }
    }
    if (driver != null) {
      tlDriverFactory.getDriver().quit();
      driver.quit();
      test.log(Status.INFO, "Test Completed : " + context.getCurrentXmlTest().getName());
    }

  }

  @AfterSuite(alwaysRun = true)
  public void endSuit() {
    try {
      extent.flush();
      log.info("AUTOMATION HTML REPORT : " + Constants.EXTENT_HTML_REPORT);
    } catch (Exception e) {
      // ignore
    }

  }

}
