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
import ru.netology.web.PayCreditPage;
import ru.netology.web.PayPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.data.DataGenerator.*;

public class PayCreditTest {
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
//1
    @Test
    void happyPathCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        PayCreditPage payCredit = new PayCreditPage();
        payCredit.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusCreditApproved());
    }
//2
    @Test
    void declinedCredit() {
        Card card = new Card(declineNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.declinedOperation();
        assertEquals("DECLINED", SqlHelper.getStatusCreditPayDeclined());
    }
//3
    @Test
    void invalidCreditCardNumber() {
        Card card = new Card(invalidNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }

//4.1
    @Test
    void validBoundaryMonthCreditFrom() {
        Card card = new Card(approveNumber(), currentMonth(), currentYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        PayCreditPage payCredit = new PayCreditPage();
        payCredit.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusCreditApproved());
    }
    //4.2
    @Test
    void validBoundaryMonthCreditTo() {
        Card card = new Card(approveNumber(), pastMonth(), yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        PayCreditPage payCredit = new PayCreditPage();
        payCredit.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusCreditApproved());
    }
//5.1
    //5.2 //todo maybe invalid?
    @Test
    void validBoundaryMonthCreditMore() {
        Card card = new Card(approveNumber(), currentMonth(), yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        PayCreditPage payCredit = new PayCreditPage();
        payCredit.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusCreditApproved());
    }

    //5.3 //todo failed, need bug-report
    @Test
    void invalidBoundaryMonthCreditLess() {
        Card card = new Card(approveNumber(), "00", yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }

    //5.4
    @Test
    void invalidBoundaryMonthPayMore() {
        Card card = new Card(approveNumber(), "13", yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }

    //5.5
    @Test
    void invalidBoundaryYearPayMore() {
        Card card = new Card(approveNumber(), anyMonth(), invalidYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }
}
