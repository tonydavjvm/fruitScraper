/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.fruitscraper.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 *
 * @author tonydavid
 */
public class FruitScraperUtil {

    public static BigDecimal extractNumberFromString(String expression) {
        if (expression == null) {
            throw new IllegalArgumentException("parameters cannot be null");
        }
        try {
            char[] chars = expression.toCharArray();
            String newDecimal = "";
            for (int i = 0; i < chars.length; i++) {
                String valToTest = String.valueOf(chars[i]);
                if (isNum(valToTest) || valToTest.equals(".")) {
                    newDecimal += valToTest;
                }
            }
            return new BigDecimal(newDecimal);
        } catch (Exception e) {
            throw new IllegalArgumentException("This chain does not contain a number");
        }
    }
    
    private static boolean isNum(String strNum) {
        boolean ret = true;
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException e) {
            ret = false;
        }
        return ret;
    }

    public static String buildPageSize(int byteSize) {
        double res = Double.valueOf(byteSize) / 1000;
        DecimalFormat df = new DecimalFormat("#");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(res) + "KB";
    }
}
