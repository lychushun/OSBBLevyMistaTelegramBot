package com.osbblevymista.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Titles {

    SETTINGS("""
            <u>Налаштування.</u>
            Для того щоб ввести дані, будь ласка натисніть спочатку відповідну кнопку, пізнішше вводьте та надсилайте дані.
            """),
    CONTACTS("<u>Контакти</u>"),
    CHATS("<u>Чати</u>"),
    APPEAL("<u>Звернення</u>"),
    ARREARS("<u>Заборгованість</u>"),
    INFO("<u>Інформація</u>"),
    MAIN("<u>Головна</u>"),
    ADMIN("<u>Адмін</u>"),
    REPORTS("<u>Звіти</u>");

    private String title;
}
