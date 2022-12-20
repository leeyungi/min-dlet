package com.i3e3.mindlet.global.constant.dandelion;

public enum DandelionConst {

    MAX_USING_DANDELION_COUNT(5);

    private final int value;

    DandelionConst(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
