package com.leon.receipt_receivables.enums;

/**
 * Created by saeid on 2/16/2017.
 */
public enum CompanyNames {
    ESF(1),
    DEBUG(111);

    private final int value;

    CompanyNames(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

}
