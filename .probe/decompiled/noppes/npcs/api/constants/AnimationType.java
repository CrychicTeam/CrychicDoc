package noppes.npcs.api.constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AnimationType {

    public static final int NONE = 0;

    public static final int SIT = 1;

    public static final int SLEEP = 2;

    public static final int HUG = 3;

    public static final int CROUCH = 4;

    public static final int DANCE = 5;

    public static final int AIM = 6;

    public static final int CRAWL = 7;

    public static final int POINT = 8;

    public static final int CRY = 9;

    public static final int WAVE = 10;

    public static final int BOW = 11;

    public static final int NO = 12;

    public static final int YES = 13;

    public static final int DEATH = 14;

    public static final int WALK = 15;

    public static final int IDLE = 16;

    public static final int FLY = 17;

    public static final int FLY_IDLE = 18;

    public static final int STATIC = 19;

    public static final int SWIM = 20;

    public static final int WAG = 21;

    public static Map<String, Integer> ALL = new HashMap();

    public static int valueOf(String name) {
        return ALL.containsKey(name.toUpperCase()) ? (Integer) ALL.get(name.toUpperCase()) : 0;
    }

    public static String nameOf(int animation) {
        for (Entry<String, Integer> en : ALL.entrySet()) {
            if ((Integer) en.getValue() == animation) {
                return (String) en.getKey();
            }
        }
        return null;
    }

    static {
        ALL.put("NONE", 0);
        ALL.put("SIT", 1);
        ALL.put("SLEEP", 2);
        ALL.put("HUG", 3);
        ALL.put("CROUCH", 4);
        ALL.put("DANCE", 5);
        ALL.put("AIM", 6);
        ALL.put("CRAWL", 7);
        ALL.put("POINT", 8);
        ALL.put("CRY", 9);
        ALL.put("WAVE", 10);
        ALL.put("BOW", 11);
        ALL.put("NO", 12);
        ALL.put("YES", 13);
        ALL.put("DEATH", 14);
        ALL.put("WALK", 15);
        ALL.put("IDLE", 16);
        ALL.put("FLY", 17);
        ALL.put("FLY_IDLE", 18);
        ALL.put("STATIC", 19);
        ALL.put("SWIM", 20);
        ALL.put("WAG", 21);
    }
}