package com.leon.receipt_receivables.enums;

public enum BundleEnum {
    TERMINAL_NO("terminal_No"),
    MERCHANT_ID("merchant_Id"),
    MESKED_PAN("masked_Pan"),
    TRACK_NUMBER("track_Number"),
    RRN("rrn"),
    REF("ref"),
    AMOUNT("amount"),
    TXN_DATE("txn_Date"),
    TXN_TIME("txn_Time"),
    PAYMENT_ID("payment_Id"),
    BILL_ID("bill_Id"),
    THEME("theme"),
    TYPE("type"),
    RESULT("resultValues");

    private final String value;

    BundleEnum(final String newValue) {
        value = newValue;
    }

    public String getValue() {
        return value;
    }
}
