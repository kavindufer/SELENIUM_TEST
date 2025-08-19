package app.tests.e2e;

import app.pages.CsrfPage;
import app.pages.DragDropPage;
import app.pages.InfiniteScrollPage;
import framework.base.BaseTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class E2E_InfiniteScroll_DragDrop_CsrfTest extends BaseTest {

    @Test(groups = "e2e")
    public void complexFlows() {
        InfiniteScrollPage scroll = new InfiniteScrollPage(driver).open();
        scroll.loadAll();
        Assertions.assertThat(scroll.status()).contains("end");

        DragDropPage drag = new DragDropPage(driver).open();
        drag.dragIntoDrop();
        drag.assertDropped();

        CsrfPage csrf = new CsrfPage(driver).open();
        csrf.readToken();
        csrf.submitMessage("hello");
        csrf.assertSuccess();
    }
}
