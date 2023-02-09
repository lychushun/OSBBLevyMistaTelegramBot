package com.osbblevymista.telegram.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SessionProperties {
    CREATING_SIMPLE_APPEAL(3),
    CREATING_URGENT_APPEAL(4),
    SENDING_MESSAGE_TO_ALL(5),

    MIY_DIM_APPEAL_INFO(6),
    SEND_MESSAGE_INFO(7);

    Integer code;
}
