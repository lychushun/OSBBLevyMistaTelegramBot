package com.osbblevymista.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SessionProperties {
    INSERTING_LOGIN(1),
    INSERTING_PASS(2),
    CREATING_SIMPLE_APPEAL(3),
    CREATING_URGENT_APPEAL(4),
    SENDING_MESSAGE_TO_ALL(5);

    Integer code;
}
