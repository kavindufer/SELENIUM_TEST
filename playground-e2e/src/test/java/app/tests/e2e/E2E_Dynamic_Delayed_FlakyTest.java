package app.tests.e2e;

import app.pages.DynamicPage;
import framework.base.BaseTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class E2E_Dynamic_Delayed_FlakyTest extends BaseTest {

    @Test(groups = "e2e")
    public void dynamicDelayedFlaky() {
        DynamicPage page = new DynamicPage(driver);
        page.openDynamicLoading();
        page.startLoading();
        page.openDelayed();
        Assertions.assertThat(page.delayedText()).isNotEmpty();
        Assertions.assertThat(page.callFlaky()).isNotEmpty();
    }
}
