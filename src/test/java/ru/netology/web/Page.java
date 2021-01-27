package ru.netology.web;

import com.codeborne.selenide.SelenideElement;
import lombok.Data;
import org.openqa.selenium.By;
import ru.netology.data.Card;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Data
public class Page {

    private SelenideElement successOperation = $(".notification_status_ok");
    private SelenideElement errorOperation =  $(".notification_status_error");

    public void callPayPage () {
        $(byText("Купить")).click();
    }

    public void callCreditPage () {
        $(byText("Купить в кредит")).click();
    }

    public void buttonContinue () {
        $(byText("Продолжить")).click();
    }

    public void data (Card card) {
        $("[maxlength='19']").setValue(card.getNumber());
        $("[placeholder='08']").setValue(card.getMonth());
        $("[placeholder='22']").setValue(card.getYear());
        $(By.xpath("//div//fieldset/div[3]//span[1]/span/span/span[2]/input")).setValue(card.getName());
        $("[placeholder='999']").setValue(card.getCodeCVC());
    }
}
