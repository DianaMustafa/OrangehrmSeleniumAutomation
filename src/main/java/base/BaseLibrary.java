package base;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class BaseLibrary {
    protected WebDriver driver;

    @Step("Tarayıcı başlatılıyor")
    public void startBrowser() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Step("URL'e gidiliyor: {url}")
    public void goToURL(String url) {
        driver.get(url);
    }

    @Step("Tarayıcı kapatılıyor")
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
