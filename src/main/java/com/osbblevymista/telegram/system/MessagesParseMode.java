package com.osbblevymista.telegram.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessagesParseMode {

    HTML ("HTML"),
    TEXT ("TEXT");

    private final String type;

}
