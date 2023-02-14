package com.osbblevymista.telegram.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Messages {
    MISSING_COOKIE(100, """
            Ви не авторизовані у системі Мій Дім. Будь ласка авторизуйтесь у системі <u>Мій Дім</u>.\s
            Якщо у Вас немає доступу до системи Мій Дім перейдіть за посилання https://miydimonline.com.ua/ та зареєструйтесь.
            Якщо у Вас виникнули труднощі із реєстрацією зверніться до правління ОСББ.
            """),

    INSERT_LOGIN(101, "Будь ласка введіть логін до системи Мій Дім\n"),
    INSERT_PASS(102, "Будь ласка введіть пароль до системи Мій Дім\n"),
    GETTING_ARREARS_DATA_FROM_MYIDIM(103, "Отримую дані про заборгованість. Зазвичай це займає до 1хв.\n"),
    CONTACT_HEAD_OF_OSBB(104, """
            <b>Дзяма Василь Васильович</b>
            (067) 252 01 55
            
            <u>Прийомні години: </u> 
            Пн, Ср, Пт: 18.00-19.00
            Нд:               вихідний
            """),
    CONTACT_ACCOUNTANT(105, """
            <b>Коваленко Арсеній Олександрович</b>
            (067) 641 73 11
            
            <u>Прийомні години: </u>
            Вт, Ср: 15.00-18.00
            Сб:       12.00-15.00
            Нд:       вихідний
            """),

    CONTACT_BUTTON_WORKER(106, """
            <b>Маїк Юрій Михайлович</b>
            (097) 979 90 99
            
            <u>Прийомні години:</u>
            Вт,Ср: 15.00-18.00
            Сб:       12.00-15.00
            Нд:       вихідний
            """),

    CONTACT_BUTTON_SECURITY(106, """
            (050) 69 28 470
            
            <u>Прийомні години: </u> 
            цілодобово
            """),

    INSERT_SIMPLE_REQUEST_DATA_FOR_MYIDIM(107, """
            Введіть будь ласка Ваш запит.
            Формування звернення зазвичай займає до 1хв. Дочекайтесь будь ласка відповіді.
            Звернення опрацьовується правлінням протягом 3х робочих днів.
            """),

    CREATING_REQUEST_DATA_FOR_MYIDIM(108, "Створюю запит. Зазвичай це займає до 1хв.\n"),

    RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM(109, "Ви успішно створили нове звернення. " +
            "Звернення опрацьовується правлінням протягом 3х робочих днів. Ми опрацюємо його як найшвидше. "),

    RESPONSE_URGENT_REQUEST_DATA_FOR_MYIDIM(111, "Ви успішно створили термінове звернення. " +
            "Ми опрацюємо його протягом одного дня.") ,

    RESPONSE_ERROR_REQUEST_DATA_FOR_MYIDIM(112, "Упс, щось трапилось. " +
            "Запит не був творений. Попробуйте трохи пізніше, або зверніться до правління ОСББ."),

    INSERT_URGENT_REQUEST_DATA_FOR_MYIDIM(113, """
            Введіть будь ласка Ваш запит.
            Формування звернення зазвичай займає до 1хв. Дочекайтесь будь ласка відповіді.
            Звернення опрацьовується правлінням протягом одного дня.
            """),

    GET_REQUEST_DATA_FOR_MYIDIM(114, """
            Отримую інформацію по Ваших зверненнях. Зазвичай це займає до 1хв.
            """),

    CHATS_BASE(115, """
            OSBBLevyMista_теревені - призначений для обговорення питань, проблем та життєдіяльності ЖК \"Леви Міста\".
            OSBBLevyMista_купи - призначений для оголошень купівлі, продажу, оренди майна та надання послуг мешканцями ЖК \"Леви Міста\".
            """),

    CHATS_BOARD(116, """
            ВІТАЮ!!!\n
            Якщо Ви бачите це повідомлення тоді Ви один із членів правління ЖК і у Вас є доступ до чату правління.
            """),

    POLL(117, """
            Нижче знаходяться список інформаційних форм, заповнення яких надасть інформацію правлінню ОСББ швидше та якісніше обробляти Ваші запити.
            Якщо потрібно надіслати індивідуальний запит, будь ласка натисніть кнопнку \"Створити звичайне\" або \"Створити термінове\" надішліть текст та натисніть кнопку підтвердження.
            """),

    SENT_MESSAGE_TO_ALL(118, """
            Введіть повідомлення яке буде розіслане всім користувачам.
            """),

    DEFAULT_ANSWER(119, "Мені дуже шкода, але на даний момент я не маю відповіді!"),

    REPORTS(120, "Тут Ви знайдете документи та звіти ОСББ \'Леви Міста\'."),
    NEW_MESSAGE_NOTIFICATION_BOARD(121, "<u>НОВЕ ЗВЕРНЕННЯ!!!</u>"),
    START_NEW_PERSON(122, """
            ВІТАЮ!!!
            Ви доєднались до чат боту ОСББ 'Леви Міста'. Тут Ви знайдете всю необхідну інформацію про життєдіяльність ЖК.
            """),
    SUCCESS_INSERT_LOGIN(123, """
            Ви успішно ввели логін. 
            """),
    SUCCESS_INSERT_PASS(123, """
            Ви успішно ввели пароль. 
            """),

    SEND_OUT_NEW_RECEIPT(124, """
            Розсилаю інвормацію про квитанції.
            """),

    NEW_RECEIPT_INFO(125, """
            <b>Нарахована нова квитанція за оплату ОСББ.</b>
            Будь ласка перейдіть у розділ <u>""" + Actions.BUTTON_ARREARS.getText() +
            "</u> та оплатіть послугу.\n"),

    UNRECOGNIZED_COMMAND(126, """
                        Дуже шкода, але мені не вдалось розпізнати вашу команду. \n 
                        Повторіть будь ласка ще раз, або перейдіть на головну сторінку."""),

    LOG_OUT_MIYDIM(127, """
            Ви успішно вийшли із системи Мій Дім.
            """),

    CREATING_APPEAL(129, """
            Звернення в процесі створення. Це може заняти деякий час. Будь ласка зачекайте.
            """),

    INSERT_APPEAL(130, """
            Будь ласка введіть звернення.
            """),

    CONTACT_BUTTON_CITYLIFT(131, """
            (063) 200 09 77
            (099) 200 09 77
            (096) 200 09 77
            """),

    CONTACT_BUTTON_LVIVOBLENERGO(132, """
            0 800 30 15 68
            (067) 333 15 68
            (093) 170 15 68
            (050) 460 15 68
            """),

    CONTACT_BUTTON_LVIVGAZ(133, """
            (096) 448 00 06
            """),

    CONTACT_BUTTON_REPAIROFBOILERS(134, """
            (096) 919 14 32
            (063) 799 30 08
            """),

    CONTACT_BUTTON_DOMOFONSERVICE(135, """
            (098) 244 44 34
            """),

    SENDING_MESSAGE(136, """
            Розсилання повідомлень. Це може зайняти декілька хвилин.
            """),

    SENT_MESSAGE(137, """
            Повідомлення було надіслано %s з %s користувачам.
            """),

    USFUL_LINKS(138, """
            Корисні посилання.
            """),

    LAST_APPEAL(139, "Для того, щоб перевірити статус виконання Ви можите перейти у розділ '"+ Actions.BUTTON_APPEAL_REVIEW.getText() +
            "' або перейти за посиланням."),

    GO_MAIN(140, "Перейти на головну."),

    SUCCESS_LOGIN(141, "Ви успішно залогінились.");

    Integer id;
    String message;

    public String format(String v1){
        return String.format(this.message, v1);
    }

    public String format(String v1, String v2){
        return String.format(this.message, v1, v2);
    }

}
