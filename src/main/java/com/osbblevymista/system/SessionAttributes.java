package com.osbblevymista.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SessionAttributes {
    IS_ADDED("is-added", Boolean.class),
    LOGIN("login", String.class),
    PASS("pass", String.class);

    String name;
    Class typeOf;
}
