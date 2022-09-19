package com.esoft.hotelmanagementsystem.enums;

public enum ReservationStatus {
    PENDING, OPEN, CHECKED_IN, CHECKED_OUT, CANCELED, COMPLETED;

    public static boolean contains(String s)
    {
        for(ReservationStatus choice:values())
            if (choice.name().equals(s))
                return true;
        return false;
    }
    }
