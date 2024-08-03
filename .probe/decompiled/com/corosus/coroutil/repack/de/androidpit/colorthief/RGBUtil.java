package com.corosus.coroutil.repack.de.androidpit.colorthief;

public class RGBUtil {

    public static int packRGB(int[] rgb) {
        if (rgb.length != 3) {
            throw new IllegalArgumentException("RGB array should contain exactly 3 values.");
        } else {
            return rgb[0] << 16 | rgb[1] << 8 | rgb[2];
        }
    }

    public static int[] unpackRGB(int packedRgb) {
        return new int[] { packedRgb >> 16 & 0xFF, packedRgb >> 8 & 0xFF, packedRgb & 0xFF };
    }

    public static int[] packRGBArray(int[][] rgbArray) {
        int[] packedArray = new int[rgbArray.length];
        for (int n = 0; n < rgbArray.length; n++) {
            packedArray[n] = packRGB(rgbArray[n]);
        }
        return packedArray;
    }

    public static int[][] unpackRGBArray(int[] packedRgbArray) {
        int[][] rgbArray = new int[packedRgbArray.length][3];
        for (int n = 0; n < packedRgbArray.length; n++) {
            rgbArray[n] = unpackRGB(packedRgbArray[n]);
        }
        return rgbArray;
    }
}