package com.quickmpay.extras;

import java.util.Random;

public class OTPGenerator {
    private static final String DIGITS = "0123456789";
    private static final int OTP_LENGTH = 6; // You can adjust the length of the OTP as needed

    public static String generateOTP() {
        Random random = new Random();
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(DIGITS.length());
            otp.append(DIGITS.charAt(index));
        }

        return otp.toString();
    }
}

