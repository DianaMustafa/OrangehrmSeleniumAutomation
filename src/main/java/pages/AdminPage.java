package pages;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import io.qameta.allure.Step;
public class AdminPage {

    WebDriver driver;
    WebDriverWait wait;

    public AdminPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    // Locators
    private By usernameInput = By.xpath("//label[text()='Username']/parent::div/following-sibling::div/input");
    private By employeeNameInput = By.cssSelector("input[placeholder='Type for hints...']");
    private By userRoleDropdown = By.xpath("//label[text()='User Role']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text')]");
    private By statusDropdown = By.xpath("//label[text()='Status']/parent::div/following-sibling::div//div[contains(@class,'oxd-select-text')]");
    private By searchButton = By.cssSelector("button[type='submit'].oxd-button--secondary");
    private By resetButton = By.cssSelector("button.oxd-button.oxd-button--ghost");
    private By tableRows = By.cssSelector(".oxd-table-body .oxd-table-card");
    private By noRecords = By.xpath("//span[text()='No Records Found']");
    private By addButton = By.cssSelector("button.oxd-button--secondary");
    private By addUsernameField = By.xpath("//label[text()='Username']/following::input[1]");
    private By adminTab = By.xpath("//span[text()='Admin']");
    private By deleteIconInRow = By.cssSelector("i.bi-trash");
    private By editIconInRow = By.cssSelector("i.bi-pencil-fill");
    private By confirmDeleteButton = By.xpath("//button[normalize-space()='Yes, Delete']");
    private By toastMessage = By.cssSelector(".oxd-toast-content");
    private By editUsernameInput = By.cssSelector("input.oxd-input.oxd-input--active");

    // Actions
    @Step("Admin sekmesine tıklanır")
    public void clickAdminTab() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(adminTab)).click();
    }

    @Step("Kullanıcı adı '{username}' olarak girilir")
    public void enterUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameInput));
        input.clear();
        input.sendKeys(username);
    }

    @Step("Çalışan adı '{name}' olarak girilir ve listeden seçilir")
    public void enterEmployeeName(String name) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameInput));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(name);

        By suggestionLocator = By.xpath("//span[text()='" + name + "']");
        WebElement suggestion = wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionLocator));
        suggestion.click();
    }

    @Step("Çalışan adı '{name}' autocomplete listesinden seçilmeye çalışılıyor")
    public boolean trySelectEmployeeName(String name) {
        try {
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(employeeNameInput));
            input.click();
            input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            input.sendKeys(Keys.DELETE);
            input.sendKeys(name);

            By suggestionLocator = By.xpath("//span[text()='" + name + "']");
            WebElement suggestionElement = wait.until(ExpectedConditions.visibilityOfElementLocated(suggestionLocator));
            suggestionElement.click();

            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Step("User Role '{role}' olarak seçilir")
    public void selectUserRole(String role) {
        wait.until(ExpectedConditions.elementToBeClickable(userRoleDropdown)).click();
        By roleOptionLocator = By.xpath("//div[@role='option']/span[normalize-space()='" + role + "']");
        WebElement roleOption = wait.until(ExpectedConditions.visibilityOfElementLocated(roleOptionLocator));
        roleOption.click();
    }

    @Step("Status '{status}' olarak seçilir")
    public void selectStatus(String status) {
        wait.until(ExpectedConditions.elementToBeClickable(statusDropdown)).click();
        By statusOptionLocator = By.xpath("//div[@role='option']/span[normalize-space()='" + status + "']");
        WebElement statusOption = wait.until(ExpectedConditions.visibilityOfElementLocated(statusOptionLocator));
        statusOption.click();
    }

    @Step("Search butonuna tıklanır")
    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    @Step("Reset butonuna tıklanır")
    public void clickReset() {
        wait.until(ExpectedConditions.elementToBeClickable(resetButton)).click();
    }

    @Step("Add butonuna tıklanır")
    public void clickAddButton() {
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
    }

    @Step("Add kullanıcı sayfası açıldı mı kontrol edilir")
    public boolean isAddPageOpened() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(addUsernameField)).isDisplayed();
    }

    @Step("Kullanıcı tablosu satırları alınır")
    public List<WebElement> getTableRows() {
        try {
            return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(tableRows));
        } catch (TimeoutException e) {
            System.out.println("ESS rolünde kullanıcı bulunamadı veya tablo hiç oluşmadı.");
            return new ArrayList<>();
        }
    }

    @Step("Kayıt bulunamadı mesajı görüntüleniyor mu kontrol edilir")
    public boolean isNoRecordMessageDisplayed() {
        return driver.findElements(noRecords).size() > 0;
    }

    @Step("Tablodan role bilgisi alınır")
    public String getRoleFromRow(WebElement row) {
        return row.findElement(By.cssSelector("div:nth-child(3)")).getText().trim();
    }

    @Step("Tablodan status bilgisi alınır")
    public String getStatusFromRow(WebElement row) {
        return row.findElement(By.cssSelector("div:nth-child(5)")).getText().trim();
    }

    @Step("Tablodan username bilgisi alınır")
    public String getUsernameFromRow(WebElement row) {
        return row.findElement(By.cssSelector("div:nth-child(2)")).getText().trim();
    }

    @Step("Tablodan employee name bilgisi alınır")
    public String getEmployeeNameFromRow(WebElement row) {
        return row.findElement(By.cssSelector("div:nth-child(4)")).getText().trim();
    }

    @Step("Username input alanının değeri alınır")
    public String getUsernameInputValue() {
        return driver.findElement(usernameInput).getAttribute("value");
    }

    @Step("Employee input alanının değeri alınır")
    public String getEmployeeInputValue() {
        return driver.findElement(employeeNameInput).getAttribute("value");
    }

    @Step("Seçili User Role bilgisi alınır")
    public String getSelectedUserRole() {
        return driver.findElement(userRoleDropdown).getText().trim();
    }

    @Step("Seçili Status bilgisi alınır")
    public String getSelectedStatus() {
        return driver.findElement(statusDropdown).getText().trim();
    }

    @Step("Satırdaki silme ikonuna tıklanır")
    public void clickDeleteIconFromRow(WebElement row) {
        row.findElement(deleteIconInRow).click();
    }

    @Step("Satırdaki düzenleme ikonuna tıklanır")
    public void clickEditIconFromRow(WebElement row) {
        row.findElement(editIconInRow).click();
    }

    @Step("Silme işlemi onaylanır")
    public void confirmDelete() {
        wait.until(ExpectedConditions.elementToBeClickable(confirmDeleteButton)).click();
    }

    @Step("Toast mesaj içeriği alınır")
    public String getToastMessageText() {
        try {
            WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMessage));
            return toast.getText().trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    @Step("Edit kullanıcı sayfasındaki username input alanı görünür mü kontrol edilir")
    public boolean isEditUsernameFieldVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(editUsernameInput)).isDisplayed();
    }
}

