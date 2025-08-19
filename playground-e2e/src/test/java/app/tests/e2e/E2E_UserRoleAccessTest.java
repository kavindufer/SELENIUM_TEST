package app.tests.e2e;

import app.pages.AdminPage;
import app.pages.LoginPage;
import app.pages.ProfilePage;
import framework.base.BaseTest;
import org.testng.annotations.Test;

public class E2E_UserRoleAccessTest extends BaseTest {

    @Test(groups = "e2e")
    public void userAndAdminAccess() {
        new LoginPage(driver).open().loginAs("admin", "admin123");
        new ProfilePage(driver).assertRole("admin");
        new AdminPage(driver).open();

        new LoginPage(driver).open().loginAs("user", "user123");
        new AdminPage(driver).open().assertForbidden();
    }
}
