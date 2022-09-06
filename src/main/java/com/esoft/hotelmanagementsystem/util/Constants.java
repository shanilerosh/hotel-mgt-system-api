package com.esoft.hotelmanagementsystem.util;

/**
 * @author ShanilErosh
 */
public class Constants {

    //new customer email
    public final static String NEW_CUSTOMER_EMAIL_BODY = "" +
            "<p>Dear ,%s</p> <br> <p>Thank you for registering as a new user. Please click the following button to confirm your account</p> <br>\n" +
            "\n" +
            "<button style=\"background-color: purple; color: white; margin-bottom: 30px\"><a style=\"color: white; margin-bottom: 30px\" href=\"http://localhost:9008/api/customer/conf/{%s}\">Confirm My Account</a></button>\n" +
            "    \n" +
            "\n" +
            " <p>Thanks & regards</p> <br><br><br><br><p style=\"font-size:11px;text-align:center\">This is an automatically generated e-mail from our System. Please do not reply to this e-mail.</p>";

    public final static String NEW_CUSTOMER_EMAIL_SUBJECT = "Welcome, %s";

}
