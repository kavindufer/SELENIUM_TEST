package app.tests.e2e;

import app.pages.AlertsPage;
import app.pages.ModalPage;
import framework.base.BaseTest;
import org.testng.annotations.Test;

public class E2E_Modal_AlertsTest extends BaseTest {

    @Test(groups = "e2e")
    public void modalAndAlerts() {
        ModalPage modal = new ModalPage(driver).openPage();
        modal.confirm();
        modal.assertResult("OK");
        modal.cancel();
        modal.assertResult("Cancel");

        AlertsPage alerts = new AlertsPage(driver).open();
        alerts.triggerAlert();
        alerts.triggerConfirm(true);
        alerts.triggerPrompt("hello");
        alerts.assertResult("hello");
    }
}
