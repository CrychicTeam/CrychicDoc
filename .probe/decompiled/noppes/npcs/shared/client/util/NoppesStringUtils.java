package noppes.npcs.shared.client.util;

import java.util.Arrays;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.entity.EntityNPCInterface;

public class NoppesStringUtils {

    static final int[] illegalChars = new int[] { 34, 60, 62, 124, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 58, 42, 63, 92, 47 };

    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-zA-Z0-9]");

    public static String cleanFileName(String badFileName) {
        StringBuilder cleanName = new StringBuilder();
        for (int i = 0; i < badFileName.length(); i++) {
            int c = badFileName.charAt(i);
            if (Arrays.binarySearch(illegalChars, c) < 0) {
                cleanName.append((char) c);
            }
        }
        return cleanName.toString();
    }

    public static String removeHidden(String text) {
        StringBuilder newString = new StringBuilder(text.length());
        int offset = 0;
        while (offset < text.length()) {
            int codePoint = text.codePointAt(offset);
            offset += Character.charCount(codePoint);
            switch(Character.getType(codePoint)) {
                case 0:
                case 16:
                case 18:
                case 19:
                    break;
                default:
                    newString.append(Character.toChars(codePoint));
            }
        }
        return newString.toString();
    }

    public static String formatText(Component text, Object... obs) {
        return formatText(text.getString(), obs);
    }

    public static String formatText(String text, Object... obs) {
        if (text != null && !text.isEmpty()) {
            text = translate(text);
            for (Object ob : obs) {
                if (ob instanceof Player) {
                    String username = ((Player) ob).getDisplayName().getString();
                    text = text.replace("{player}", username);
                    text = text.replace("@p", username);
                } else if (ob instanceof EntityNPCInterface) {
                    text = text.replace("@npc", ((EntityNPCInterface) ob).getName().getString());
                }
            }
            return text.replace("&", Character.toChars(167)[0] + "");
        } else {
            return "";
        }
    }

    public static void setClipboardContents(String aString) {
        Minecraft.getInstance().keyboardHandler.setClipboard(aString);
    }

    public static String getClipboardContents() {
        return Minecraft.getInstance().keyboardHandler.getClipboard();
    }

    public static String stripSpecialCharacters(String in) {
        return NON_ALPHANUMERIC.matcher(in).replaceAll("");
    }

    public static String cleanResource(String s) {
        return s.toLowerCase().replaceAll("[^a-z0-9_.\\-/:]", "");
    }

    public static String translate(Object... arr) {
        Language languagemap = Language.getInstance();
        String s = "";
        for (Object str : arr) {
            s = s + languagemap.getOrDefault(str.toString());
        }
        return s;
    }

    public static String[] splitLines(String s) {
        return s.split("\r\n|\r|\n");
    }

    public static String newLine() {
        return System.getProperty("line.separator");
    }

    public static int parseInt(String s, int i) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException var3) {
            return i;
        }
    }

    public static boolean areEqual(String s1, String s2) {
        if (s1 == s2) {
            return true;
        } else {
            return s1 != null && s2 != null ? s1.equalsIgnoreCase(s2) : false;
        }
    }

    public static boolean areEqual(ResourceLocation s1, ResourceLocation s2) {
        if (s1 == s2) {
            return true;
        } else {
            return s1 != null && s2 != null ? s1.toString().equalsIgnoreCase(s2.toString()) : false;
        }
    }

    static {
        Arrays.sort(illegalChars);
    }
}