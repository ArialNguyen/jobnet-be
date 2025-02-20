package com.jobnet.post.utils;

import java.math.BigInteger;
import java.util.List;

public class SalaryUtils {
    private final static List<String> CURRENCIES = List.of("triệu", "tr");
    private final static String isNumericString = "\\d+";

    public static BigInteger parseSalary(String salaryString) {
        if (salaryString == null || salaryString.isEmpty())
            throw new IllegalStateException("Salary string cannot be null or empty.");
        String cleanedSalaryString = salaryString;
        boolean containsCurrency = CURRENCIES.stream()
                                       .anyMatch(currency -> salaryString.toLowerCase().contains(currency));
        if (containsCurrency){
            cleanedSalaryString = salaryString.substring(0, salaryString.indexOf("tr") - 1) + "000000";
        }
        if (salaryString.length() <= 6 && salaryString.matches(isNumericString)){
            throw new IllegalStateException("Salary string muse be at least 6 digits or contains 'triệu', 'tr'.");
        }
        return new BigInteger(cleanedSalaryString);
    }

    public static boolean checkFormat(String salaryString) {
        return salaryString.length() > 6;
    }
}
