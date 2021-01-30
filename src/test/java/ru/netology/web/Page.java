package ru.netology.web;

import com.codeborne.selenide.Condition;
import lombok.Data;
import org.openqa.selenium.By;
import ru.netology.data.Card;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Data
public class Page {

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

    public boolean invalidCardFormat() {
        $(".input__sub").shouldHave(Condition.exactText("Неверный формат"));
        return true;
    }

    public boolean invalidBoundary() {
        $(".input__sub").shouldHave(Condition.exactText("Неверно указан срок действия карты"));
        return true;
    }
}
