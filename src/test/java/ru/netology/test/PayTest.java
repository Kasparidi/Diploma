package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.Card;
import ru.netology.data.DataGenerator;
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
    void setUp() {
        open("http://localhost:8080");
        SqlHelper.clean();
    }
//1
    @Test
    void happyPathPay() throws SQLException {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPay());
    }
//2 failed
    @Test
    void declinedPay() throws SQLException {
        Card card = new Card(declineNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.declinedOperation();
        assertEquals("DECLINED", SqlHelper.getStatusPay());
    }
//3
    @Test
    void invalidCardNumberPay() {
        Card card = new Card(invalidNumber(), anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
//4.1
    @Test
    void validBoundaryMonthPayFrom() throws SQLException {
        Card card = new Card(approveNumber(), currentMonth(), currentYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPay());
    }
    //4.2
    @Test
    void validBoundaryMonthPayTo() throws SQLException {
        Card card = new Card(approveNumber(), pastMonth(), yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPay());
    }
    //5.1
    @Test
    void invalidBoundaryMonthYearPayLess() {
        Card card = new Card(approveNumber(), pastMonth(), "", name(), codeCVC());
        if (pastMonth().equals("12")) {
            card.setYear(DataGenerator.pastYear());
        } else {
            card.setYear(DataGenerator.currentYear());
        }
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.invalidBoundary();
    }

    //5.2 failed
    @Test
    void invalidBoundaryMonthPayMore() {
        Card card = new Card(approveNumber(), currentMonth(), yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.invalidBoundary();
    }
    //5.3 failed
    @Test
    void invalidBoundaryMonthPayLess() {
        Card card = new Card(approveNumber(), "00", yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }
    //5.4
    @Test
    void invalidBoundaryMonthPay() {
        Card card = new Card(approveNumber(), "13", yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }

    //5.5
    @Test
    void invalidBoundaryYearPayMore() {
        Card card = new Card(approveNumber(), anyMonth(), invalidYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidBoundary();
    }
    //6.1 failed
    @Test
    void invalidNameRusPay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "КАСПАРИДИ НАТАЛЬЯ", codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //6.2 failed
    @Test
    void invalidNameOnlyLowCasePay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "eve", codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //6.3 failed
    @Test
    void invalidLongNamePay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), longName(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //6.4 failed
    @Test
    void invalidShortNamePay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "n", codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //7.1 failed
    @Test
    void invalidCVCPay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), "000");
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //7.2
    @Test
    void invalidShortCVCPay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), "23");
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidCardFormat();
    }
    //8.1 failed
    @Test
    void invalidEmptyNumberPay() {
        Card card = new Card("", anyMonth(), validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.2 failed
    @Test
    void invalidEmptyMonthPay() {
        Card card = new Card(approveNumber(), "", validYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.3 failed
    @Test
    void invalidEmptyYearPay() {
        Card card = new Card(approveNumber(), anyMonth(), "", name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.4
    @Test
    void invalidEmptyNamePay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), "", codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.5
    @Test
    void invalidEmptyCVCPay() {
        Card card = new Card(approveNumber(), anyMonth(), validYear(), name(), "");
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
    //8.5 failed
    @Test
    void invalidEmptyAllFieldPay() {
        Card card = new Card("", "", "", "", "");
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        page.invalidEmptyField();
    }
}

