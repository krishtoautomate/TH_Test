package PageObjects;

import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.ExtentTest;
import com.base.BasePageObjects;


public class SupportPage extends BasePageObjects<SupportPage> {



  public SupportPage(WebDriver driver, Logger log, ExtentTest test) {
    super(driver, log, test);

  }

  By h1 = By.tagName("h1");

  public WebElement get_h1() {
    return getElement(h1, "'header' text");
  }

  By formElements = By.xpath("//div[@class='sc-cSHVUG AsuKO']");

  public List<WebElement> get_formElements() {
    return getElements(formElements, "'All elements'");
  }


  By solutionLabel = By.xpath("//span[@data-testid='selectLabel' and contains(text(),'solution')]");

  public WebElement get_solutionLabel() {
    return getElement(solutionLabel, "'Which solution can we help you with?' text");
  }

  By solutionSelector = By.xpath("//select[contains(@id, 'solution')]");

  public WebElement get_solutionSelector() {
    return getElement(solutionSelector, "Which solution can we help you with?' drop-down selector");
  }

  By needHelpLabel =
      By.xpath("//span[@data-testid='selectLabel' and contains(text(),'need help')]");

  public WebElement get_needHelpLabel() {
    return getElement(needHelpLabel, "'I need help with *' text");
  }

  By needHelpSelector = By.xpath("//select[contains(@id, 'i-need-help-with-')]");

  public WebElement get_needHelpSelector() {
    return getElement(needHelpSelector, "I need help with *' drop-down selector");
  }

  By needHelpErrorMessage = By.xpath("//*[@id='i-need-help-with-_error-message']/p");

  public WebElement get_needHelpErrorMessage() {
    return getElement(needHelpErrorMessage, "'Select the type of help' error message");
  }

  By descLabel = By.xpath("//span[contains(text(),'Description')]");

  public WebElement get_descLabel() {
    return getElement(descLabel, "'Description of issue' text");
  }

  By descTextField = By.id("description-of-issue");

  public WebElement get_descTextField() {
    return getElement(descTextField, "'Description of issue' input text field");
  }

  By descErrorMessage = By.xpath("//*[@id='description-of-issue_error-message']/p");

  public WebElement get_descErrorMessage() {
    return getElement(descErrorMessage, "'Enter a detailed messag' error message");
  }


  // Contact information
  By contactInfo_h2 = By.xpath("//h2[text()='Contact Information']");

  public WebElement get_contactInfo_h2() {
    return getElement(contactInfo_h2, "'Contact Information' h2 text");
  }

  By titleSelector = By.xpath("//select[@id='title']");

  public WebElement get_titleSelector() {
    return getElement(titleSelector, "'title' selector");
  }

  By firstNameTextField = By.xpath("//*[@id='first-name-']");

  public WebElement get_firstNameTextField() {
    return getElement(firstNameTextField, "'first name' text field");
  }

  By firstNameErrorMessage = By.xpath("//*[@id='first-name-_error-message']/p");

  public WebElement get_firstNameErrorMessage() {
    return getElement(firstNameErrorMessage, "'Enter first name' error message");
  }

  By lastNameTextField = By.xpath("//*[@id='last-name-']");

  public WebElement get_lastNameTextField() {
    return getElement(lastNameTextField, "'last name' text field");
  }


  By lastNameErrorMessage = By.xpath("//*[@id='last-name-_error-message']/p");

  public WebElement get_lastNameErrorMessage() {
    return getElement(lastNameErrorMessage, "'Enter last name' error message");
  }

  By emailTextField = By.xpath("//*[@id='email-']");

  public WebElement get_emailTextField() {
    return getElement(emailTextField, "'email' text field");
  }

  By emailErrormessage = By.xpath("//*[@id='email-_error-message']/p");

  public WebElement get_emailErrormessage() {
    return getElement(emailErrormessage, "'Enter email address' error message");
  }

  By telephoneTextField = By.xpath("//*[@id='telephone']");

  public WebElement get_telephoneTextField() {
    return getElement(telephoneTextField, "'telephone' input text field");
  }

  By provinceSelector = By.xpath("//*[@id='province-']");

  public WebElement get_provinceSelector() {
    return getElement(provinceSelector, "'Province' drop-down selector");
  }

  By provinceErrorMessage = By.xpath("//*[@id='province-_error-message']/p");

  public WebElement get_provinceErrorMessage() {
    return getElement(provinceErrorMessage, "'Select province' error message");
  }

  By roleTextField = By.xpath("//*[@id='role']");

  public WebElement get_roleTextField() {
    return getElement(roleTextField, "'Role' input text field");
  }

  By healthOrgNameTextField = By.xpath("//*[@id='clinicpharmacyorganization-name']");

  public WebElement get_healthOrgNameTextFieldn() {
    return getElement(healthOrgNameTextField,
        "'Clinic/pharmacy/organization name' input text field");
  }

  By submit_btn = By.xpath("//button[@type='submit']/span[text()]");

  public WebElement get_submit_btn() {
    return getElement(submit_btn, "'submit' button");
  }



}
