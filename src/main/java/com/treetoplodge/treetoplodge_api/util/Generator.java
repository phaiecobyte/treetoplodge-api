package com.treetoplodge.treetoplodge_api.util;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public class Generator {
    private static int counter = 1;

    public static synchronized String generateUniqueAccommodationId() {
        String paddedNumber = String.format("%04d", counter++);
        return "ACC-" + paddedNumber;
    }

    public static synchronized String generateUniqueAccommodationId(String accommodationId) {
        String paddedNumber = String.format("%04d", counter++);
        return "ACC-" + paddedNumber;
    }

    public static synchronized String generateUniqueFoodBeverageCode(){
        String paddedNumber = String.format("%04d",counter++);
        return "FOO-"+paddedNumber;
    }

    public static @Size(max = 50) String generateUserId() {
        String paddedNumber = String.format("%04d", counter++);
        return "USER-"+paddedNumber;
    }
}
