package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import ru.netology.data.Card;
import ru.netology.data.DataGenerator;
import ru.netology.web.Page;
import ru.netology.web.PayPage;

import static com.codeborne.selenide.Selenide.open;

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
    }

    @Test
    void happyPath() {
        Card card = new Card();
        card.setNumber(DataGenerator.approveNumber());
        card.setMonth(DataGenerator.anyMonth());
        card.setYear(DataGenerator.validYear());
        card.setName(DataGenerator.name());
        card.setCodeCVC(DataGenerator.codeCVC());

        Page page = new Page();
        page.callPayPage();
        page.data(card);
        page.buttonContinue();
        PayPage payPage = new PayPage();
        payPage.successOperation();
    }
}
