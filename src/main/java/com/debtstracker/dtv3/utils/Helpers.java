package com.debtstracker.dtv3.utils;

import java.util.Map;

public class Helpers {

    public static String getSessionIdFromHeaders(Map<String, String> headers) {
        return headers.get("authorization").replace("Basic ", "");
//        String base64EncodedAuth = headers.get("authorization").replace("Basic ", "");
//        return new String(Base64.getDecoder().decode(base64EncodedAuth)).replace(":", "");
    }

}
