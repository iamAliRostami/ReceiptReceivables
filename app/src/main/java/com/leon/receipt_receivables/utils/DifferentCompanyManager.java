package com.leon.receipt_receivables.utils;


import com.leon.receipt_receivables.enums.CompanyNames;

public class DifferentCompanyManager {
    private DifferentCompanyManager() {
    }

    public static CompanyNames getCompanyNameEnum(int companyCode) {
        switch (companyCode) {
            case 1:
                return CompanyNames.ESF;
            default:
                throw new UnsupportedOperationException();

        }
    }

    public static String getCompanyName(CompanyNames companyName) {
        switch (companyName) {
            case ESF:
                return "آبفا استان اصفهان";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static int getAccountMinLength() {
        return getActiveCompanyName() == CompanyNames.ESF ? 8 : 10;
    }

    public static String getMapUrl(CompanyNames companyNames) {
        switch (companyNames) {
            case ESF:
                return "http://37.191.92.130/";
            case DEBUG:
                return "http://192.168.43.185:45458/";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static String getBaseUrl(CompanyNames companyNames) {
        switch (companyNames) {
            case ESF:
                return "https://37.191.92.157/";
            case DEBUG:
                return "http://192.168.43.185:45458/";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static String getLocalBaseUrl(CompanyNames companyNames) {
        switch (companyNames) {
            case ESF:
                return "http://172.18.12.14:100";
            case DEBUG:
                return "http://172.18.12.242/osm_tiles/";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static String getEmailTail(CompanyNames companyNames) {
        switch (companyNames) {
            case ESF:
                return "@esf.ir";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static String getCameraUploadUrl(CompanyNames companyNames) {
        switch (companyNames) {
            case ESF:
                return "http://172.18.12.122/Api/api/CRMobileOffLoad/Upload";
            case DEBUG:
                return "";
            default:
                throw new UnsupportedOperationException();
        }
    }

    public static CompanyNames getActiveCompanyName() {
        return CompanyNames.ESF;
    }

    public static String getPrefixName(CompanyNames companyNames) {
        switch (companyNames) {
            case ESF:
                return "@esf.ir";
            default:
                throw new UnsupportedOperationException();
        }
    }
}
