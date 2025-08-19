package app.tests.e2e;

import app.pages.FormsPage;
import app.pages.LoginPage;
import framework.base.BaseTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class E2E_FormValidationAndSuccessTest extends BaseTest {

    @Test(groups = "e2e")
    public void formValidationAndSuccess() {
        new LoginPage(driver).open().loginAs("user", "user123");
        FormsPage forms = new FormsPage(driver).open();
        forms.fillEmail("bad");
        forms.fillPassword("1");
        forms.submit();
        Assertions.assertThat(forms.errorMessage()).isNotEmpty();

        forms.open();
        forms.fillEmail("valid@example.com");
        forms.fillPassword("password123");
        forms.selectPlan("basic");
        forms.setAge("30");
        forms.submit();
        forms.assertSuccessContains("user");
    }
}
