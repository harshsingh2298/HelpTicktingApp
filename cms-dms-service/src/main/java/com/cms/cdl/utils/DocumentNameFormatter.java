package com.cms.cdl.utils;

import org.springframework.stereotype.Component;

@Component
public class DocumentNameFormatter {
    public static String formatItemName(String itemName) {
        // Split the itemName by "-" and take the first and last parts
        String[] parts = itemName.split("-");
        if (parts.length >= 3) {
            return parts[0] + "-" + parts[parts.length - 1];
        }
        // Return the original itemName if it doesn't match the expected format
        return itemName;
    }

}
