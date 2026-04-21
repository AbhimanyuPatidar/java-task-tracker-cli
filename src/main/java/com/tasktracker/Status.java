package com.tasktracker;

public enum Status {
    TODO (1, "todo"),
    IN_PROGRESS (2, "in-progress"),
    DONE (3, "done");

    private final int code;
    private final String displayValue;

    Status(int code, String displayValue) {
        this.code = code;
        this.displayValue = displayValue;
    }

    public int getCode() {
        return code;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    // Static method to find the status by it's number
    public static Status findStatus(int code) {
        for (Status sts : Status.values()) {
            if (sts.code == code) {
                return sts;
            }
        }

        return null;
    }
}
