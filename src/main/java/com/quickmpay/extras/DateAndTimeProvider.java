package com.quickmpay.extras;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateAndTimeProvider {
    
    public static String getDateAndTime() {
    
        LocalDateTime currentDateTime = LocalDateTime.now();
        // Format the current date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String formattedDateTime = currentDateTime.format(formatter);
        
        // Remove non-digit characters
        String digitsOnly = formattedDateTime.replaceAll("\\D", "");
        
        return digitsOnly;
    }
}
