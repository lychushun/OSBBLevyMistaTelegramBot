package com.osbblevymista.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Actions {
    BUTTON_PASS("Пароль \uD83D\uDD11"),
    BUTTON_LOGIN("Логін \uD83D\uDC7B"),
    BUTTON_HEAD_OF_OSBB("Голова ОСББ \uD83D\uDC2F"),
    BUTTON_ACCOUNTANT("Бухгалтер \uD83D\uDC25"),
    BUTTON_WORKER("Заступник з господарства \uD83D\uDC77"),
    BUTTON_SECURITY("Охорона \uD83D\uDC6E"),
    BUTTON_BACK("Назад \uD83D\uDC48"),
//    BUTTON_BACK("Назад"),


    BUTTON_APPEAL_SIMPLE_CREATE("Створити звичайне \uD83D\uDCD7"),
    BUTTON_APPEAL_URGENT_CREATE("Створити термінове \uD83D\uDCD5"),
    BUTTON_APPEAL_REVIEW("Переглянути звернення \uD83D\uDCD6"),

    BUTTON_CHAT_PUBLIC("Теревені"),
    BUTTON_CHAT_ANNOUNCEMENT("Оголошення"),


    BUTTON_PAYMENT_PRIVAT24("Оплатити Privat24"),
    BUTTON_PAYMENT_IPAY("Оплатити iPAY"),

    BUTTON_CHAT_OSBB_CHAT_TEREVENI("Теревені \"Леви Міста\""),
    BUTTON_CHAT_OSBB_CHAT_ANNOUNCEMENT("Купи\\Продай \"Леви Міста\""),
    BUTTON_CHAT_OSBB_CHAT_BOARD("Правління \"Леви Міста\""),

    BUTTON_CAR_ENTRANCE("Форма для доступу до шлагбаунів."),

    BUTTON_RULES_OF_LEAVING("Правила проживання."),
    BUTTON_MEETING_RELUSLT01022022("Підсумки Загальних зборів від 1.02.2022."),
    BUTTON_TARIFFS("Тарифи."),

    BUTTON_ADMIN_SEND("Розіслати повідомлення");

    String text;
}
