package com.osbblevymista.telegram.system;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Commands {

    URGENT_APPEAL("/urgentAppeal"),
    SIMPLE_APPEAL("/simpleAppeal");

    private final String command;

    public static Commands fromString(String text) {
        for (Commands b : Commands.values()) {
            if (b.command.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
