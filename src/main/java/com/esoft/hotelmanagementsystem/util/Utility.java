package com.esoft.hotelmanagementsystem.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author ShanilErosh
 */
public class Utility {

    public static String formatCurrency(BigDecimal value) {
        return formatCurrency(value.doubleValue());
    }

    public static String formatCurrency(double value) {
        DecimalFormat df = new DecimalFormat("###,##0.00;(###,##0.00)");
        return df.format(value);
    }

}
