/*
 * Copyright (c) 2016. Seedlabs LLC All Rights Reserved.
 */

package com.seedlabs.donuts.api.utils;

import java.util.ArrayList;

/**
 * Created by mp2526 on 3/23/16.
 */
public class FieldUtils {
    static public ArrayList<String> parseFields(String input) {
        if(input == null)
            return null;

        ArrayList<String> result = new ArrayList<>();
        int start = 0;
        int inBraces = 0;

        for (int current = 0; current < input.length(); current++) {
            if (input.charAt(current) == '{') {
                inBraces++;
            } else if (input.charAt(current) == '}') {
                inBraces--;
            }

            boolean atLastChar = (current == input.length() - 1);
            if(atLastChar) result.add(input.substring(start));
            else if (input.charAt(current) == ',' && !(inBraces > 0)) {
                result.add(input.substring(start, current));
                start = current + 1;
            }
        }

        return result;
    }
}
