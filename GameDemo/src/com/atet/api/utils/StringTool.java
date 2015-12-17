package com.atet.api.utils;

public class StringTool {
    public static String getContent(String src, String startTag, String endTag) {
        String content = src;
        int start = src.indexOf(startTag);
        start += startTag.length();

        try {
            if (endTag != null) {
                int end = src.indexOf(endTag);
                content = src.substring(start, end);
            } else {
                content = src.substring(start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }
}
