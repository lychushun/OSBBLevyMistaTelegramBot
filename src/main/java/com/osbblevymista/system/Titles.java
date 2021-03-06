package com.osbblevymista.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Titles {

    SETTINGS("""
            <u>Налаштування.</u>
            Для того щоб ввести дані, будь ласка натисніть спочатку відповідну кнопку, пізніше вводьте та надсилайте дані.
            """),
    CONTACTS("""
            <u>Контакти</u>
            Тут ви знайдете контактні дані основних працівників нашого ОСББ.
            """),
    CHATS("<u>Чати</u>"),
    APPEAL("<u>Звернення</u>"),
    ARREARS("<u>Заборгованість</u>"),
    INFO("""
            <u>Інформація</u>
            Даний бот розробляється цілком безкоштовно, тому інколи можуть виникати проблеми в його роботі.
            Завчасно просимо вибачення за це. Ми будемо виправляти помилки, удосконалювати та впроваджувати новий функціонал як найшвидше.
            Також Ви дуже допоможете нам, якщо виявите некоректну роботу бота та повідомите про неї.
                
            На цій сторінці Ви знайдете контактні дані та документи які стосуються нашого ОСББ.
            """),
    MAIN("<u>Головна</u>"),
    ADMIN("<u>Адмін</u>\n" +
            "Вітаю!!! Ви супер користувач. У Вас є додаткові можливост роботи з ботом.\n\n" +
            "Для того, щоб створити та розіслати повідомлення натисніть спочатку кнопку <b>" + Actions.BUTTON_ADMIN_SEND.getText() + "</b>," +
            " пізніше введіть та надішліть текст. Після цього, повідомлення буде розіслано всім користувачам бота."),
    REPORTS("""
            <u>Документи та звіти</u>
            Тут знаходяться основні звіти та документи які стосуються роботи нашого ОСББ.
            """);

    private String title;
}
