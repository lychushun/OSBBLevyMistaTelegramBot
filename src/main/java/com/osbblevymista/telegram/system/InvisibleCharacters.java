package com.osbblevymista.telegram.system;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvisibleCharacters {

    _200C("\u200C"),
    _200B("\u200B"),
    _200D("\u200D");
    private String val;

}
