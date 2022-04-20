package com.offerxp.offerxp_sdk.api;

import com.example.offerxp.OfferXP;

public class OfferXpSdkApiConstants {
    public static final String ENV_PRODUCTION = "prod";
    public static final String ENV_DEVELOPMENT = "dev";



    static String getBaseUrl() {

        switch (OfferXP.INSTANCE.getEnv()) {
            case ENV_PRODUCTION:
                return "https://a351-103-161-144-183.ngrok.io/";
            case ENV_DEVELOPMENT:
                return "https://a351-103-161-144-183.ngrok.io/";
            default:
                return "https://a351-103-161-144-183.ngrok.io/";
        }
    }

    public static final int STATUS_OK = 200;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_NEED_UPGRADE = 426;
    public static final int STATUS_INTERNAL_SERVER_ERROR = 500;
    public static final int STATUS_FORBIDDEN = 403;

    public static final String CUSTOM_HEADER_KEY_AT = "@";
    public static final String CUSTOM_HEADER_VALUE_NO_AUTH = "NoAuth";
    public static final String CUSTOM_HEADER_NO_AUTH = CUSTOM_HEADER_KEY_AT + ":" + CUSTOM_HEADER_VALUE_NO_AUTH;
    public static final String CUSTOM_TOKEN_KEY = "*";
}
