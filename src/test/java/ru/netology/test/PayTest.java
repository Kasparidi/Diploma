package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.Card;
import ru.netology.data.SqlHelper;
import ru.netology.web.Page;
import ru.netology.web.PayPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;

public class PayTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUp() throws SQLException {
        open("http://localhost:8080");
        SqlHelper.set();
    }

    @Test
    void happyPathPay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPayApproved());
    }

    @Test
    void declinedPay() {
        Card card = new Card(declineNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.declinedOperation();
        assertEquals("DECLINED", SqlHelper.getStatusPayDeclined());
    }
}
