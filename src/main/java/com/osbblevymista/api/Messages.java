package com.osbblevymista.api;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Messages {

    MIY_DIM_AUTH_SUCCESS("Ви успішно авторизувались у системі Мій Дім", Type.STATUS_SUCCESS),
    MIY_DIM_AUTH_ERROR("Упс, щось трапилось. Перевірте логин та пароль, або спробуйте пізніше.", Type.STATUS_ERROR);

    private final String message;
    private final Type type;

    enum Type {
        MESSAGE,
        STATUS_ERROR,
        STATUS_SUCCESS
    }
}
