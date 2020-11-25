package com.leon.receipt_receivables.enums;

public enum SharedReferenceNames {
    ACCOUNT("com.app.leon.receipt_receivables.account_info");

    private final String value;

    SharedReferenceNames(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
