package club.iananderson.seasonhud.util;

import club.iananderson.seasonhud.impl.seasons.SeasonList;
import java.awt.Color;
import java.util.HashMap;

public class Rgb {

    public static HashMap<String, Integer> seasonMap(int rgb) {
        HashMap<String, Integer> rgbMap = new HashMap();
        rgbMap.put("r", rgbColor(rgb).getRed());
        rgbMap.put("g", rgbColor(rgb).getGreen());
        rgbMap.put("b", rgbColor(rgb).getBlue());
        rgbMap.put("rgb", rgb);
        return rgbMap;
    }

    public static HashMap<String, Integer> defaultSeasonMap(SeasonList season) {
        HashMap<String, Integer> defaultRgbMap = new HashMap();
        int rgb = season.getDefaultColor();
        defaultRgbMap.put("r", rgbColor(rgb).getRed());
        defaultRgbMap.put("g", rgbColor(rgb).getGreen());
        defaultRgbMap.put("b", rgbColor(rgb).getBlue());
        defaultRgbMap.put("rgb", rgb);
        return defaultRgbMap;
    }

    public static int rgbInt(int r, int g, int b) {
        return 65536 * r + 256 * g + b;
    }

    public static Color rgbColor(int rgb) {
        return new Color(rgb);
    }

    public static void setRgb(SeasonList season, int rgb) {
        season.getRgbMap().put("r", rgbColor(rgb).getRed());
        season.getRgbMap().put("g", rgbColor(rgb).getGreen());
        season.getRgbMap().put("b", rgbColor(rgb).getBlue());
        season.getRgbMap().put("rgb", rgb);
    }

    public static void setRgb(SeasonList season, int r, int g, int b) {
        season.getRgbMap().put("r", r);
        season.getRgbMap().put("g", g);
        season.getRgbMap().put("b", b);
        season.getRgbMap().put("rgb", rgbInt(r, g, b));
    }

    public static int getRgb(SeasonList season) {
        return (Integer) season.getRgbMap().get("rgb");
    }

    public static int getRed(SeasonList season) {
        return (Integer) season.getRgbMap().get("r");
    }

    public static int getGreen(SeasonList season) {
        return (Integer) season.getRgbMap().get("g");
    }

    public static int getBlue(SeasonList season) {
        return (Integer) season.getRgbMap().get("b");
    }

    public static void setRed(SeasonList season) {
        season.getRgbMap().get("r");
    }

    public static void setGreen(SeasonList season) {
        season.getRgbMap().get("g");
    }

    public static void setBlue(SeasonList season) {
        season.getRgbMap().get("b");
    }
}