package ru.netology.web;

import com.codeborne.selenide.Condition;

import static com.codeborne.selenide.Selenide.$;

public class PayCreditPage {
    public void successOperation () {
        $(".notification_status_ok").waitUntil(Condition.visible, 15000);
    }
}
