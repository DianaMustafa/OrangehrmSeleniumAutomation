package base;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.LoginPage;
import data.Data;

public class BaseTest extends BaseLibrary {

    @BeforeMethod
    public void setUp() {
        startBrowser();
        goToURL("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(Data.username, Data.password);
    }

    @AfterMethod
    public void tearDown() {
        closeBrowser();
    }
}

