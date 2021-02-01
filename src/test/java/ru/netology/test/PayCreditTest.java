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
    @Test
    void invalidBoundaryMonthYearCreditLess() {
        Card card = new Card(approveNumber(), pastMonth(), currentYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }
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
    //5.3 failed
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
    //6.1 failed
    @Test
    void invalidNameRusCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "КАСПАРИДИ НАТАЛЬЯ", codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //6.2 failed
    @Test
    void invalidNameOnlyLowCaseCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "nata", codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //6.3 failed
    @Test
    void invalidLongNameCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), longName(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //6.4 failed
    @Test
    void invalidShortNameCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "z", codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //7.1
    @Test
    void invalidCVCCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), "000");
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //7.2
    @Test
    void invalidShortCVCCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), "56");
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //8.1 failed
    @Test
    void invalidEmptyNumberCredit() {
        Card card = new Card("", anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.2 failed
    @Test
    void invalidEmptyMonthCredit() {
        Card card = new Card(approveNumber(), "", validYear(), name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.3 failed
    @Test
    void invalidEmptyYearCredit() {
        Card card = new Card(approveNumber(), anyMonth(), "", name(), codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.4
    @Test
    void invalidEmptyNameCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "", codeCVC());
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.5 failed
    @Test
    void invalidEmptyCVCCredit() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), "");
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.6 failed
    @Test
    void invalidEmptyAllFieldCredit() {
        Card card = new Card("", "", "", "", "");
        Page page = new Page();
        page.callCreditPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
}
