package com.i3e3.mindlet.global.enums;

public enum Community {

    WORLD("world"),
    KOREA("korea");

    private final String description;

    Community(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
