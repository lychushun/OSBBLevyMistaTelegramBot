package com.osbblevymista.telegram.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Actions {
    BUTTON_HEAD_OF_OSBB("Голова ОСББ \uD83D\uDC2F"),
    BUTTON_ACCOUNTANT("Бухгалтер \uD83D\uDC25"),
    BUTTON_WORKER("Заступник з господарства \uD83D\uDC77"),
    BUTTON_SECURITY("Охорона \uD83D\uDC6E"),
    BUTTON_CITYLIFT("Сіті Ліфт \uD83D\uDED7"),
    BUTTON_LVIVOBLENERGO("Львівобленерго \u26A1"),
    BUTTON_LVIVGAZ("Львівгаз \uD83D\uDD25"),
    BUTTON_REPAIROFBOILERS("Ремонт котлів \uD83D\uDEE0"),
    BUTTON_DOMOFONSERVICE("Домофон сервіс \uD83D\uDCDE"),
    BUTTON_BACK("Назад \uD83D\uDC48"),
    BUTTON_NEXT("Далі \uD83D\uDC49"),
//    BUTTON_BACK("Назад"),


    BUTTON_APPEAL_SIMPLE_CREATE("Створити звичайне \uD83D\uDCD7"),
    BUTTON_APPEAL_URGENT_CREATE("Створити термінове \uD83D\uDCD5"),
    BUTTON_APPEAL_REVIEW("Переглянути звернення \uD83D\uDCD6"),
    BUTTON_BARRIER("Шлагбаум \uD83D\uDEA7"),
    BUTTON_APPEAL_SIMPLE_APPROVE("Підтвердити \u2705"),
    BUTTON_APPEAL_URGENT_APPROVE("Підтвердити \u2705"),

    BUTTON_SEND_MESSAGE_APPROVE("Підтвердити \u2705"),

    BUTTON_CHAT_PUBLIC("Теревені"),
    BUTTON_CHAT_ANNOUNCEMENT("Оголошення"),


    BUTTON_PAYMENT_PRIVAT24("Оплатити Privat24"),
    BUTTON_PAYMENT_IPAY("Оплатити iPAY"),

    BUTTON_CHAT_OSBB_CHAT_TEREVENI("Теревені \"Леви Міста\""),
    BUTTON_CHAT_OSBB_CHAT_ANNOUNCEMENT("Купи\\Продай \"Леви Міста\""),
    BUTTON_CHAT_OSBB_CHAT_BOARD("Правління \"Леви Міста\""),

    BUTTON_CAR_ENTRANCE("Форма для доступу до шлагбаумів."),
    BUTTON_MIYDIM_LINK("Система Мій Дім."),
    BUTTON_WEB_LINK("WEB сайт."),

    BUTTON_RULES_OF_LEAVING("Правила проживання."),
    BUTTON_MEETING_RELUSLT01022022("Підсумки загальних зборів від 1.02.2022."),
    BUTTON_PRESENTATION2023("Презентація ОСББ 'Леви Міста' 2023"),
    BUTTON_DEBTORS("Боржники"),
    BUTTON_TARIFFS("Тарифи 2022"),
    BUTTON_TARIFFS2023("Тарифи 2023"),

    BUTTON_ADMIN_SEND("Розіслати повідомлення \uD83D\uDCE8"),
    BUTTON_ADMIN_NEW_RECEIPT("Розіслати повідомлення про нову квитанцію \uD83D\uDCC4"),

    BUTTON_CONTACT("Контакти \uD83D\uDE01"),
    BUTTON_USEFUL_LINKS("Корисні посилання \uD83C\uDF10"),
    BUTTON_INFO("Інформація \u2139"),
    BUTTON_ARREARS("Заборгованість \uD83D\uDCB0"),
    BUTTON_SETTINGS("Налаштування \uD83D\uDEA9"),
    BUTTON_CHATS("Чати \uD83D\uDCDA"),
    BUTTON_ADMIN("Адмін \uD83D\uDC77"),
    BUTTON_APPEAL("Звернення \uD83D\uDE4B"),
    BUTTON_REPORTS("Документи та звіти \uD83D\uDCC4"),

    BUTTON_VISIT_MIYDIM("Авторизуватись у МійДім"),
    BUTTON_EXIT_MIYDIM("Вийти з МійДім");

    String text;
}
