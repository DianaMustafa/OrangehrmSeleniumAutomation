package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {

    WebDriver driver;

    // Constructor
    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    // Locators
    By usernameInput = By.name("username");
    By passwordInput = By.name("password");
    By loginButton = By.cssSelector("button[type='submit']");

    // Actions
    public void login(String username, String password) {
        WebElement userField = driver.findElement(usernameInput);
        WebElement passField = driver.findElement(passwordInput);
        WebElement loginBtn = driver.findElement(loginButton);

        userField.sendKeys(username);
        passField.sendKeys(password);
        loginBtn.click();
    }
}

