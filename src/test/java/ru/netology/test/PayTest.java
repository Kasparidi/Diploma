package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
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
    void setUp() throws SQLException {
        open("http://localhost:8080");
        SqlHelper.set();
    }
//1
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
//2
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
    void validBoundaryMonthPayFrom() {
        Card card = new Card(approveNumber(), currentMonth(), currentYear(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPayApproved());
    }
    //4.2
    @Test
    void validBoundaryMonthPayTo() {
        Card card = new Card(approveNumber(), pastMonth(), yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPayApproved());
    }
    //TODO как протестировать, если прошлый месяц это декабрь прошлого года?
    //5.1
    @Test
    void invalidBoundaryMonthYearPayLess() {
        Card card = new Card(approveNumber(), pastMonth(), currentYear(), name(), codeCVC());
//        if (card.setMonth(DataGenerator.pastMonth()) == "12") {
//            card.setYear(DataGenerator.pastYear());
//        } else {
//            card.setYear(DataGenerator.currentYear());
//        }
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.invalidBoundary();
    }

    //5.2 //todo maybe invalid?
    @Test
    void validBoundaryMonthPayMore() {
        Card card = new Card(approveNumber(), currentMonth(), yearPlusFive(), name(), codeCVC());
        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
        assertEquals("APPROVED", SqlHelper.getStatusPayApproved());
    }

    //5.3 //todo failed, need bug-report
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
    void invalidBoundaryMonthPayMore() {
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
    //6.1 failed //todo need bug-report
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
    //7.1 failed //todo need bug-report
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
    //8.1 failed //todo bug-report
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
    //8.5 //todo bug-report
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

