package com.example.couphoneserver.config;

import java.util.HashSet;
import java.util.Set;

public class permitAllUrl {
    private static final Set<String> urlSet;

    static {
        urlSet = new HashSet<>();
        urlSet.add("/auth/login");
    }

    public static boolean of(String url) {
        return urlSet.contains(url);
    }
}
