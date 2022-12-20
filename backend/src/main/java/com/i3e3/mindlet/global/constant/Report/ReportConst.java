package com.i3e3.mindlet.global.constant.Report;

public enum ReportConst {

    REPORT_STATUS_CHANGE_T0_PENDING_COUNT(3);

    private final int value;

    ReportConst(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
