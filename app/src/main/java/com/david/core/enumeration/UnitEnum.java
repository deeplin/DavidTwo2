package com.david.core.enumeration;

public enum UnitEnum {
    mmHg, kPa, percentage("%");

    private final String text;

    UnitEnum() {
        this.text = this.name();
    }

    UnitEnum(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
