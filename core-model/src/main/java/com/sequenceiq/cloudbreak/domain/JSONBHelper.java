package com.sequenceiq.cloudbreak.domain;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONBHelper {

    private JSONBHelper() {
    }

    public static String toString(Map<String, String> m) {
        String json = "";
        if (m.isEmpty()) {
            return json;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(m);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return json;
    }

    public static Map<String, String> toMap(String json) {
        Map<String, String> map = new HashMap<>();
        if (!hasText(json)) {
            return map;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    private static boolean hasText(String str) {
        if (!hasLength(str)) {
            return false;
        } else {
            int strLen = str.length();

            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean hasLength(String str) {
        return str != null && str.length() > 0;
    }
}
