package TestCases;

import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;
import com.aventstack.extentreports.Status;
import com.base.ITestBase;
import com.base.TestBase;
import PageObjects.SupportPage;

public class THtest extends TestBase implements ITestBase {

  // execute : mvn clean compile test -Dsurefire.suiteXmlFiles=src/test/resources/TestNG.xml

  @Test(priority = 1)
  public void ContactSupportPageValidation() {

    // data can be mapped to Json or csv files

    String p_url = "https://www.telus.com/en/health/contact/support/employers";

    SupportPage supportPage = new SupportPage(driver, log, test);


    // 1.0 - open url
    supportPage.getPage(p_url);

    // 2.0 - validate Pages
    supportPage.AssertContains(supportPage.getPageTitle(), "Technical support");

    supportPage.AssertContains(supportPage.get_h1().getText(), "Technical support");

    supportPage.AssertTrue(supportPage.get_formElements().size(), 12);

    supportPage.AssertContains(supportPage.get_solutionLabel().getText(),
        "Which solution can we help you with?");

    supportPage.AssertContains(supportPage.get_needHelpLabel().getText(), "I need help with *");

    supportPage.AssertContains(supportPage.get_descLabel().getText(), "Description of issue");

    // 3.0 - Contact information
    supportPage.AssertContains(supportPage.get_contactInfo_h2().getText(), "Contact Information");

    // 4.0 - firstname is displayed
    supportPage.get_firstNameTextField();
    supportPage.logmessage(Status.PASS, "'first-name' text-field is displayed");

    // 5.0 - lastname is displayed
    supportPage.get_lastNameTextField();
    supportPage.logmessage(Status.PASS, "'last-name' text-field is displayed");

    // 6.0 - submit button is displayed
    supportPage.get_submit_btn();
    supportPage.logmessage(Status.PASS, "'Submit' button is displayed");

  }

  @Test(priority = 2)
  public void EmptyFormTest() {

    String p_url = "https://www.telus.com/en/health/contact/support/employers";

    String p_solution = "Other solutions";
    String p_firstName = "first";
    String p_lastName = "user";
    String p_email = p_firstName + p_lastName + "@gmail.com";
    String p_provice = "Quebec";

    SupportPage supportPage = new SupportPage(driver, log, test);

    // 1.0 - open url
    supportPage.getPage(p_url);

    // 2.0 - validate Pages
    supportPage.AssertContains(supportPage.getPageTitle(), "Technical support");

    supportPage.AssertContains(supportPage.get_h1().getText(), "Technical support");

    // 3.0 - submit button is clicked
    supportPage.get_submit_btn().click();
    supportPage.logmessage(Status.PASS, "'Submit' button is clicked");

    // 4.0 - validate errors
    supportPage.AssertContains(supportPage.get_descErrorMessage().getText(),
        "Enter a detailed message");

    supportPage.AssertContains(supportPage.get_firstNameErrorMessage().getText(),
        "Enter first name");

    supportPage.AssertContains(supportPage.get_lastNameErrorMessage().getText(), "Enter last name");

    supportPage.AssertContains(supportPage.get_emailErrormessage().getText(),
        "Enter email address");

    supportPage.AssertContains(supportPage.get_provinceErrorMessage().getText(), "Select province");

    // 5.0 - select solution
    new Select(supportPage.get_solutionSelector()).selectByVisibleText(p_solution);

    // 6.0 - enter contact info
    supportPage.get_firstNameTextField().sendKeys(p_firstName);

    supportPage.get_lastNameTextField().sendKeys(p_lastName);

    supportPage.get_emailTextField().sendKeys(p_email);

    new Select(supportPage.get_provinceSelector()).selectByVisibleText(p_provice);

    // 7.0 - submit button is clicked
    supportPage.get_submit_btn().click();
    supportPage.logmessage(Status.PASS, "'Submit' button is clicked");

    // 8.0 - validate errors
    supportPage.AssertContains(supportPage.get_descErrorMessage().getText(),
        "Enter a detailed message");

  }
}
