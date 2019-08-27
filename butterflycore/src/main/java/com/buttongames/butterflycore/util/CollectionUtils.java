package com.buttongames.butterflycore.util;

import java.util.ArrayList;

/**
 * Simple class with methods to interact with collections (mainly bytes).
 * @author skogaby (skogabyskogaby@gmail.com)
 */
public class CollectionUtils {

    /**
     * Convert an {@code ArrayList<Byte>} to a {@code byte[]}
     * @param list
     * @return
     */
    public static byte[] arrayListToArray(final ArrayList<Byte> list) {
        final byte[] ret = new byte[list.size()];

        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i);
        }

        return ret;
    }

    /**
     * Converts a hex string into a byte array.
     * @param s
     * @return
     */
    public static byte[] hexStringToByteArray(final String s) {
        final int len = s.length();
        final byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    /**
     * Converts a hex string into an integer array, where each int
     * is an unsigned byte value.
     * @param s
     * @return
     */
    public static int[] hexStringToUnsignedByteArray(final String s) {
        final int len = s.length();
        final int[] data = new int[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16);
        }

        return data;
    }



    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}

