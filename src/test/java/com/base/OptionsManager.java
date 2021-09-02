package com.base;


import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 * Created by Krish on 21.05.2018.
 */
public class OptionsManager {

  // Get Chrome Options
  public ChromeOptions getChromeOptions() {

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");
    options.addArguments("--ignore-certificate-errors");
    options.addArguments("--disable-popup-blocking");
    options.addArguments("--incognito");
    options.addArguments("user-agent=");// for user over-ride
    options.addArguments("disable-infobars");
    options.setAcceptInsecureCerts(true);

    return options;
    /*
     * ChromeDriverService service = new ChromeDriverService.Builder() .usingAnyFreePort() .build();
     * ChromeDriver driver = new ChromeDriver(service, options);
     */
  }

  // Get Firefox Options
  public FirefoxOptions getFirefoxOptions() {
    FirefoxOptions options = new FirefoxOptions();
    options.addArguments("-private");
    FirefoxProfile profile = new FirefoxProfile();

    // Accept Untrusted Certificates
    profile.setAcceptUntrustedCertificates(true);
    profile.setAssumeUntrustedCertificateIssuer(false);

    // Set Firefox profile to capabilities
    options.setCapability(FirefoxDriver.PROFILE, profile);
    options.setCapability("marionette", true);
    return options;
  }
}
