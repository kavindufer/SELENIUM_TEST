package app.tests.e2e;

import app.pages.LoginPage;
import app.pages.TablePage;
import framework.base.BaseTest;
import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

public class E2E_Table_FilterSortPaginateTest extends BaseTest {

    @Test(groups = "e2e")
    public void tableInteractions() {
        new LoginPage(driver).open().loginAs("user", "user123");
        TablePage table = new TablePage(driver).open();
        table.filter("foo");
        table.sortBy("name");
        table.sortBy("price");
        table.nextPage();
        table.prevPage();
        Assertions.assertThat(table.totalLabel()).isNotEmpty();
    }
}
