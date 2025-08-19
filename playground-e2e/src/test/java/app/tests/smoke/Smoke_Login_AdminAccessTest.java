package app.tests.smoke;

import app.pages.AdminPage;
import app.pages.LoginPage;
import app.pages.ProfilePage;
import framework.base.BaseTest;
import org.testng.annotations.Test;

public class Smoke_Login_AdminAccessTest extends BaseTest {

    @Test(groups = "smoke")
    public void adminCanAccessAdminPage() {
        new LoginPage(driver).open().loginAs("admin", "admin123");
        new ProfilePage(driver).assertRole("admin");
        new AdminPage(driver).open();
        new AdminPage(driver).assertUrlContains("/admin");
    }
}
