package com.quicktransfer.fundstransfer.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtil() {
    }

    public static ObjectMapper getMapper() {
        return mapper;
    }



}
