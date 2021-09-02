package com.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.Reporter;
import com.aventstack.extentreports.ExtentTest;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;


/**
 * Created by Krish on 21.05.2018.
 */

public class TLDriverFactory {

  private static OptionsManager optionsManager = new OptionsManager();
  private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
  private static Logger log = Logger.getLogger(Class.class.getName());
  protected ExtentTest test;

  public static Properties prop = new Properties();
  public static InputStream input = null;

  protected synchronized void setDriver() {

    ITestResult iTestResult = Reporter.getCurrentTestResult();
    Map<String, String> testParams =
        iTestResult.getTestContext().getCurrentXmlTest().getAllParameters();
    String browser = testParams.get("browser");
    String udid = testParams.get("udid");

    try {
      input = new FileInputStream("src//main//resources//driver.properties");
      prop.load(input);
    } catch (IOException e) {
      log.info("Driver properties file not found - " + e.toString());
    }

    if (browser.equals("firefox")) {
      tlDriver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));


    } else if (browser.equalsIgnoreCase("chrome")) {
      DesiredCapabilities capabilities = new DesiredCapabilities();
      // identify System O

      // ChromeDriverManager.chromedriver().setup();
      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        System.setProperty("webdriver.chrome.driver",
            Constants.USER_DIR + prop.getProperty("chrome_driver_windows"));
      } else {
        System.setProperty("webdriver.chrome.driver",
            Constants.USER_DIR + prop.getProperty("chrome_driver_mac"));
      }

      capabilities.setCapability(ChromeOptions.CAPABILITY, optionsManager.getChromeOptions());

      capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
      capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

      capabilities.setCapability("chrome.switches",
          Arrays.asList("--ignore-certificate-errors" + "," + "--web-security=false" + ","
              + "--ssl-protocol=any" + "," + "--ignore-ssl-errors=true"));

      ChromeOptions options = new ChromeOptions();
      options.merge(capabilities);
      tlDriver.set(new ChromeDriver(options));
      // tlDriver.set(new ChromeDriver(capabilities));

      log.info("Chrome started!");
    } else if (browser.equalsIgnoreCase("ie")) {


      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        System.setProperty("webdriver.ie.driver",
            System.getProperty("user.dir") + prop.getProperty("ieedge_driver_windows"));
      } else {
        System.setProperty("webdriver.ie.driver",
            System.getProperty("user.dir") + prop.getProperty("ieedge_driver_mac"));
      }

      tlDriver.set(new InternetExplorerDriver());
    } else if (browser.equalsIgnoreCase("iOS")) {

      DesiredCapabilities capabilities = new DesiredCapabilities();
      capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
      capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.2");
      capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
      capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
      capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 12");
      capabilities.setCapability(MobileCapabilityType.UDID, udid);

      try {
        tlDriver.set(
            new IOSDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities));
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    } else if (browser.equalsIgnoreCase("Android")) {

      DesiredCapabilities capabilities = new DesiredCapabilities();
      capabilities.setCapability("platformName", "Android");
      capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.0");
      capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
      capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Chrome");
      capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Pixel");
      capabilities.setCapability(MobileCapabilityType.UDID, udid);

      try {
        tlDriver.set(new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"),
            capabilities));
      } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  protected synchronized WebDriver getDriver() {
    return tlDriver.get();
  }
}
