package com.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;


public class BasePageObjects<T> implements ITestBase {
  protected WebDriver driver;
  protected WebDriverWait wait;
  protected Wait<WebDriver> fluentWait;
  protected Logger log;
  protected ExtentTest test;

  protected BasePageObjects(WebDriver driver, Logger log, ExtentTest test) {
    this.driver = driver;
    this.log = log;
    this.test = test;
    wait = new WebDriverWait(driver, 10);
    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    // driver.manage().window().maximize();
  }

  @SuppressWarnings("unchecked")
  public T getPage(String url) {

    driver.get(url);
    new WebDriverWait(driver, 10).until(webDriver -> ((JavascriptExecutor) webDriver)
        .executeScript("return document.readyState").equals("complete"));
    // driver.navigate().to(url);// incase default url is loaded
    return (T) this;
  }

  /**
   * get Title of the Page
   */
  public String getPageTitle() {
    return driver.getTitle();
  }

  /**
   * Clears the text Field
   */
  protected void clear(By element) {
    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);
    driver.findElement(element).clear();
  }

  /**
   * Closed the Tab/Window
   */
  protected void close() {
    driver.close();
  }

  /**
   * Thread sleep for Certain Seconds
   */
  @Override
  public void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (Exception e) {
      log.warn("wait function failed!");
    }
  }



  /*
   * highlight Element
   */
  public void highlightElement(WebElement element) {
    try {
      ((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'",
          element);
    } catch (Exception e) {
      log.warn("Failed to highlight element!");
    }
  }

  public boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (TimeoutException eTO) {
      // ignore
    }
    return false;
  }

  public void waitUntil(By by) {
    wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));

  }

  /**
   * Screen-shot generator
   */
  public File ScreenshotGenerator() {
    File ScreenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
    return ScreenShot;
  }

  /*
   * Scroll to the element to be visible in screen
   */
  public void scrollToView(By locator) {
    // ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();",
    // driver.findElement(locator));

    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoViewIfNeeded(true);",
        driver.findElement(locator));
  }

  public void scrollToView(WebElement Element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", Element);
  }


  public synchronized String takeScreenshot() {
    int indexNo = 0;
    String uuid = UUID.randomUUID().toString();

    File ScreenShot = ScreenshotGenerator();

    // Name the screen-shot
    String imgPath = "img/" + indexNo + "_" + "screenShot" + "_" + uuid + ".PNG";

    do {
      uuid = UUID.randomUUID().toString();
      indexNo++;
      imgPath = "img/" + indexNo + "_" + "screenShot" + "_" + uuid + ".PNG";
    } while (Files.exists(Paths.get(Constants.REPORT_DIR + imgPath)));

    File filePath = new File(Constants.REPORT_DIR + imgPath);

    try {
      FileUtils.moveFile(ScreenShot, filePath);
    } catch (IOException | WebDriverException e) {
      log.error("screenShot not Found!!!");
    }
    return imgPath;
  }

  /**
   * get Screen-shot
   */
  public File getScreenshot() {
    File FilePath = null;
    try {
      File ScreenShot = ScreenshotGenerator();

      String uuid = "";
      int indexNo = 0;
      uuid = UUID.randomUUID().toString();
      String path = "test-output/" + Constants.DATE_NOW + "/img/" + indexNo + "_" + "ScreenCapture"
          + "_" + uuid + ".PNG";
      do {
        uuid = UUID.randomUUID().toString();
        indexNo++;
        path = "test-output/" + Constants.DATE_NOW + "/img/" + indexNo + "_" + "ScreenCapture" + "_"
            + uuid + ".PNG";
      } while (Files.exists(Paths.get(path)));

      FilePath = new File(path);

      FileUtils.moveFile(ScreenShot, FilePath);
    } catch (IOException | WebDriverException e) {
      test.log(Status.WARNING, "Screenshot function failed");
      log.info("Screenshot function failed");
    }

    return FilePath;
  }

  /**
   * Creates logs into Log4j and extent-Report with Screen-shot
   */
  @SuppressWarnings("static-access")
  public synchronized void logmessage(Status Status, String message) {

    log.info(message);

    this.sleep(2);

    try {
      String imgPath = takeScreenshot();

      if (Status == Status.FAIL) {
        test.fail(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
      } else {
        test.pass(message, MediaEntityBuilder.createScreenCaptureFromPath(imgPath).build());
      }
    } catch (IOException e) {
      if (Status == Status.FAIL) {
        test.fail(message);
      } else {
        test.pass(message);
      }
    }
  }

  /**
   * type text in a inputElement
   */
  protected void type(String text, By element) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(element));

    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);

    driver.findElement(element).sendKeys(text);
  }

  /**
   * click on element
   */
  public void click(By by) {
    WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    driver.findElement(by).click();
  }

  /**
   * click on element
   */
  public void click(WebElement element) {
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);

    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
  }

  /**
   * find an element
   *
   */
  protected void find(By element) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    WebElement object = wait.until(ExpectedConditions.visibilityOfElementLocated(element));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", object);
    driver.findElement(element);
  }

  /**
   * wait for visibility of locator and timeINSeconds-to-wait
   */
  protected void waitForVisibilityOf(By locator, Integer... timeOutInSeconds) {
    int attempts = 0;
    while (attempts < 2) {
      try {
        waitFor(ExpectedConditions.visibilityOfElementLocated(locator),
            (timeOutInSeconds.length > 0 ? timeOutInSeconds[0] : null));
        break;
      } catch (StaleElementReferenceException e) {

      }
      attempts++;
    }
  }

  /**
   * wait for expected condition for timeINSeconds-to-wait
   */
  protected void waitFor(ExpectedCondition<WebElement> condition, Integer timeOutInSeconds) {
    timeOutInSeconds = timeOutInSeconds != null ? timeOutInSeconds : 30;
    wait.until(condition);
  }


  public void AssertTrue(int actual, int expected) {

    if (actual != expected) {
      String message = "Verification failed : '" + actual + "' Vs '" + expected + "'";
      logmessage(Status.FAIL, message);
      Assert.fail(message);
    }

  }

  /**
   * Verifies if String contains other String
   */
  public void AssertContains(String Actual, String Expected) {

    Boolean found = false;

    String actual = Actual.replaceAll("\n", " ");
    String[] arrOfExpected = Expected.split("\\|");

    for (String expected : arrOfExpected) {
      if (StringUtils.containsIgnoreCase(actual.trim(), expected.trim())
          | StringUtils.containsIgnoreCase(expected, actual.trim()) && !actual.isEmpty()) {
        logmessage(Status.INFO,
            "Verification Success : '" + actual.trim() + "' Vs '" + expected.trim() + "'");
        found = true;
        break;
      }
    }

    if (!found) {
      String message = "Verification failed : '" + actual + "' Vs '" + Expected + "'";
      logmessage(Status.FAIL, message);
      Assert.fail(message);
    }
  }

  /**
   * Verifies if Expected String exist
   */
  public void checkContent(String Actual, String Expected) {
    if (Actual.trim().contains(Expected.trim())) {
      logmessage(Status.WARNING, "Found : " + Expected);
    } else {
      logmessage(Status.WARNING, "Not Found : " + Expected);
    }
  }

  /**
   * refresh Page
   */
  public void refreshPage() {
    driver.navigate().refresh();
  }

  public void waitForInVisibilityOfLoader() {
    By loader = By.xpath("//*[contains(text(),'Please wait')] | //*[contains(text(),'LOADING')] |"
        + "//*[contains(text(),'Loading')] | "
        + "//div[contains(text(),'Loading data. Please wait...')]");

    wait.until(ExpectedConditions.invisibilityOfElementLocated(loader));
  }

  /*
   * Double Click on WebElement
   */
  public void doubleClick(WebElement element) {
    Actions action = new Actions(driver);
    action.doubleClick(element).build().perform();
  }

  public WebElement getElement(By locator, String locatorDesc) {
    WebElement element = null;
    try {
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
      scrollToView(locator);
      element = driver.findElement(locator);
    } catch (Exception e) {
      String errorMessage = "'" + locatorDesc + "' - not found in " + getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return element;
  }

  public WebElement getElement(By locator, String item, int wait_duration) {
    WebElement element = null;
    try {
      WebDriverWait wait = new WebDriverWait(driver, wait_duration);
      wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
      element = driver.findElement(locator);
    } catch (Exception e) {
      String errorMessage = "'" + item + "' - not found in " + getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return element;
  }

  protected WebElement verify_Element(By locator) {
    WebElement ele = null;
    try {
      ele =
          new WebDriverWait(driver, 2).until(ExpectedConditions.presenceOfElementLocated(locator));
    } catch (Exception e) {
      // ignore
    }
    return ele;
  }

  public List<WebElement> getElements(By locator, String item) {
    List<WebElement> elements = null;
    try {
      elements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    } catch (Exception e) {
      String errorMessage = "'" + item + "' - not found in " + getClass().getName();
      logmessage(Status.FAIL, errorMessage);
      Assert.fail(errorMessage);
    }
    return elements;
  }
}
