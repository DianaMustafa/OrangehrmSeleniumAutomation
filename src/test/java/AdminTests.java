import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.AdminPage;

import java.util.List;

public class AdminTests extends BaseTest {

    AdminPage adminPage;

    @BeforeMethod
    public void setUpAdminPage() {
        adminPage = new AdminPage(driver);
        adminPage.clickAdminTab();
    }
    @Test
    public void TC21_adminSayfasinaBasariliGirisKontrolu() {
        String actualTitle = driver.getTitle();
        Assert.assertTrue(actualTitle.contains("OrangeHRM"), "Sayfa başlığı hatalı!");
    }

    @Test
    public void TC22_kullaniciAdinaGoreArama() {
        List<WebElement> rows = adminPage.getTableRows();

        if (rows.isEmpty()) {
            throw new SkipException("Hiç kullanıcı bulunamadı. Test atlandı.");
        }

        String dynamicUsername = adminPage.getUsernameFromRow(rows.get(0));
        adminPage.enterUsername(dynamicUsername);
        adminPage.clickSearch();

        List<WebElement> filteredRows = adminPage.getTableRows();
        Assert.assertTrue(filteredRows.size() > 0, "Kullanıcı araması sonrası sonuç bulunamadı!");

        String actualUsername = adminPage.getUsernameFromRow(filteredRows.get(0));
        Assert.assertTrue(actualUsername.contains(dynamicUsername), "Beklenen kullanıcı bulunamadı!");
    }

    @Test
    public void TC23_kullaniciRoluneGoreFiltreleme() {
        adminPage.selectUserRole("Admin");
        adminPage.clickSearch();

        if (adminPage.isNoRecordMessageDisplayed()) {
            throw new SkipException("Admin rolünde kullanıcı bulunamadı. Test atlandı.");
        }

        List<WebElement> rows = adminPage.getTableRows();
        for (WebElement row : rows) {
            String role = adminPage.getRoleFromRow(row);
            Assert.assertEquals(role, "Admin", "Filtre sonrası kullanıcı rolü hatalı!");
        }
    }

    @Test
    public void TC24_calisanIsmineGoreFiltrelemeKontrolu() {
        List<WebElement> rows = adminPage.getTableRows();

        if (rows.isEmpty()) {
            throw new SkipException("Kullanıcı listesi boş, test atlandı.");
        }
        boolean filtrelemeYapildi = false;

        for (WebElement row : rows) {
            String employeeName = adminPage.getEmployeeNameFromRow(row);
            if (adminPage.trySelectEmployeeName(employeeName)) {
                System.out.println("Filtreleme yapılacak çalışan adı bulundu: " + employeeName);
                adminPage.clickSearch();
                if (adminPage.isNoRecordMessageDisplayed()) {
                    Assert.fail("Beklenen sonuç bulunamadı: " + employeeName);
                }
                List<WebElement> filteredRows = adminPage.getTableRows();
                for (WebElement result : filteredRows) {
                    String actualName = adminPage.getEmployeeNameFromRow(result);
                    Assert.assertEquals(actualName, employeeName, "Farklı çalışan adı bulundu: " + actualName);
                }
                filtrelemeYapildi = true;
                break;
            }
        }
        if (!filtrelemeYapildi) {
            throw new SkipException("Autocomplete listesinden seçim yapılamadı. Test atlandı.");
        }
    }

    @Test
    public void TC25_kullaniciDurumuFiltrelemeKontrolu() {
        adminPage.selectStatus("Enabled");
        adminPage.clickSearch();

        if (adminPage.isNoRecordMessageDisplayed()) {
            throw new SkipException("Enabled kullanıcı yok, test atlandı.");
        }

        List<WebElement> rows = adminPage.getTableRows();
        for (WebElement row : rows) {
            String status = adminPage.getStatusFromRow(row);
            Assert.assertEquals(status, "Enabled", "'Enabled' olmayan kullanıcı bulundu!");
        }
    }

    @Test
    public void TC26_resetButonuIleAlanlarTemizlenmeli() {
        adminPage.enterUsername("demo");
        adminPage.selectUserRole("Admin");
        adminPage.selectStatus("Enabled");
        adminPage.clickReset();

        Assert.assertEquals(adminPage.getUsernameInputValue(), "", "Username temizlenmedi!");
        Assert.assertEquals(adminPage.getSelectedUserRole(), "-- Select --", "User Role temizlenmedi!");
        Assert.assertEquals(adminPage.getSelectedStatus(), "-- Select --", "Status temizlenmedi!");
        Assert.assertEquals(adminPage.getEmployeeInputValue(), "", "Employee name temizlenmedi!");
    }

    @Test
    public void TC27_addButonuSayfaAcmaKontrolu() throws InterruptedException {
        adminPage.clickAddButton();
        Assert.assertTrue(adminPage.isAddPageOpened(), "Add sayfası açılmadı!");
    }

    @Test
    public void Tc28_silmeIkonuIleKullaniciSilmeKontrolu() {
        adminPage.selectUserRole("ESS");
        adminPage.clickSearch();

        List<WebElement> rows = adminPage.getTableRows();

        if (rows.size() == 0) {
            System.out.println("ESS kullanıcı yok, test atlanıyor.");
            return;
        }
        boolean essKullaniciSilindi = false;

        for (WebElement row : rows) {
            String rol = adminPage.getRoleFromRow(row);
            if (rol.equals("ESS")) {
                String silinecekKullanici = adminPage.getUsernameFromRow(row);
                adminPage.clickDeleteIconFromRow(row);
                adminPage.confirmDelete();

                adminPage.enterUsername(silinecekKullanici);
                adminPage.clickSearch();

                essKullaniciSilindi = adminPage.isNoRecordMessageDisplayed();
                break;
            }
        }
        Assert.assertTrue(essKullaniciSilindi, "ESS rolünde kullanıcı bulunamadı veya silinemedi!");
    }

    @Test
    public void TC29_duzenlemeIkonuIleBilgiErisimKontrolu() throws InterruptedException {
        List<WebElement> rows = adminPage.getTableRows();

        if (rows.isEmpty()) {
            throw new SkipException("Tabloda kullanıcı bulunamadı. Test atlandı.");
        }

        WebElement firstRow = rows.get(0);
        adminPage.clickEditIconFromRow(firstRow);

        Assert.assertTrue(adminPage.isEditUsernameFieldVisible(),
                "Düzenleme ekranındaki username input alanı görüntülenemedi!");
    }

    @Test
    public void TC30_kayitBulunamadiMesajiKontrolu() {
        String gecersizKullanici = "asdfghjkl";
        adminPage.enterUsername(gecersizKullanici);
        adminPage.clickSearch();

        // Tablo içinde "No Records Found" kontrolü
        Assert.assertTrue(adminPage.isNoRecordMessageDisplayed(), "'No Records Found' mesajı tabloda görünmedi!");

        // Sol alttaki info kutusu mesajı kontrolü
        String toastText = adminPage.getToastMessageText();
        Assert.assertTrue(toastText.contains("No Records Found"), "Toast mesajı beklenen gibi değil: " + toastText);
    }

    @Test
    public void TC31_varsayilanKullaniciListesiYuklenmeKontrolu() {

        List<WebElement> rows = adminPage.getTableRows();

        Assert.assertFalse(rows.isEmpty(), "Varsayılan kullanıcı listesi boş geldi!");
    }
}






