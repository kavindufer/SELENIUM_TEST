package app.tests.e2e;

import app.pages.IframePage;
import app.pages.ShadowDomPage;
import framework.base.BaseTest;
import org.testng.annotations.Test;

public class E2E_Iframe_ShadowDomTest extends BaseTest {

    @Test(groups = "e2e")
    public void iframeAndShadowDom() {
        IframePage iframe = new IframePage(driver).open();
        iframe.clickButtonInFrame();
        iframe.assertTitle("Frame Done");

        ShadowDomPage shadow = new ShadowDomPage(driver).open();
        shadow.setField("play");
        shadow.assertPill("play");
    }
}
