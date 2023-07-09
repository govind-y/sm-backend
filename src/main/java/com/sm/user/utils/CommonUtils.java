package com.sm.user.utils;


import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

public  class  CommonUtils {

    public static  String generateUUID(){

        return UUID.randomUUID().toString();
    }
    public static  String getCurrentSessionYear(){

        return  String.valueOf(LocalDate.now().getYear());
    }
}
