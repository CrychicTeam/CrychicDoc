package icyllis.modernui.mc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import icyllis.modernui.graphics.text.Emoji;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class EmojiDataGen {

    public static void main(String[] args) {
        MemoryStack stack = MemoryStack.stackPush();
        String iam_cal;
        String joy_pixels;
        String google_fonts;
        String output;
        try {
            PointerBuffer filters = stack.mallocPointer(1);
            stack.nUTF8("*.json", true);
            filters.put(stack.getPointerAddress());
            filters.rewind();
            iam_cal = TinyFileDialogs.tinyfd_openFileDialog("Open IamCal", null, filters, "JSON File", false);
            joy_pixels = TinyFileDialogs.tinyfd_openFileDialog("Open JoyPixels", null, filters, "JSON File", false);
            google_fonts = TinyFileDialogs.tinyfd_openFileDialog("Open GoogleFonts", null, filters, "JSON File", false);
            output = TinyFileDialogs.tinyfd_saveFileDialog(null, "emoji_data.json", filters, "JSON File");
        } catch (Throwable var24) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var20) {
                    var24.addSuppressed(var20);
                }
            }
            throw var24;
        }
        if (stack != null) {
            stack.close();
        }
        if (iam_cal != null && joy_pixels != null && google_fonts != null && output != null) {
            Gson gson = new Gson();
            EmojiDataGen.EmojiEntry[] iam_cal_data = read(gson, iam_cal, EmojiDataGen.EmojiEntry[].class);
            JsonObject joy_pixels_data = read(gson, joy_pixels, JsonObject.class);
            JsonArray google_fonts_data = read(gson, google_fonts, JsonArray.class);
            LinkedHashMap<String, EmojiDataGen.EmojiEntry> map = (LinkedHashMap<String, EmojiDataGen.EmojiEntry>) Arrays.stream(iam_cal_data).collect(Collectors.toMap(ex -> ex.unified.toLowerCase(Locale.ROOT), Function.identity(), (x, y) -> x, LinkedHashMap::new));
            for (Entry<String, JsonElement> e : joy_pixels_data.entrySet()) {
                JsonObject o = ((JsonElement) e.getValue()).getAsJsonObject();
                JsonObject ci = o.getAsJsonObject("code_points");
                EmojiDataGen.EmojiEntry emoji = (EmojiDataGen.EmojiEntry) map.computeIfAbsent(ci.get("fully_qualified").getAsString(), EmojiDataGen.EmojiEntry::new);
                emoji.short_names.add(stripColons(o.get("shortname").getAsString()));
                for (JsonElement s : o.get("shortname_alternates").getAsJsonArray()) {
                    emoji.short_names.add(stripColons(s.getAsString()));
                }
            }
            for (JsonElement e : google_fonts_data) {
                for (JsonElement e1 : e.getAsJsonObject().get("emoji").getAsJsonArray()) {
                    JsonObject o = e1.getAsJsonObject();
                    JsonArray ci = o.get("base").getAsJsonArray();
                    int[] cps = new int[ci.size()];
                    for (int i = 0; i < cps.length; i++) {
                        cps[i] = Integer.parseInt(ci.get(i).getAsString());
                    }
                    EmojiDataGen.EmojiEntry emoji = (EmojiDataGen.EmojiEntry) map.computeIfAbsent((String) Arrays.stream(cps).mapToObj(Integer::toHexString).collect(Collectors.joining("-")), EmojiDataGen.EmojiEntry::new);
                    for (JsonElement s : o.get("shortcodes").getAsJsonArray()) {
                        emoji.short_names.add(stripColons(s.getAsString()));
                    }
                }
            }
            List<Object[]> output_data = (List<Object[]>) map.values().stream().filter(EmojiDataGen.EmojiEntry::validate).map(EmojiDataGen.EmojiEntry::flatten).collect(Collectors.toList());
            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8));
                try {
                    gson.toJson(output_data, writer);
                } catch (Throwable var22) {
                    try {
                        writer.close();
                    } catch (Throwable var21) {
                        var22.addSuppressed(var21);
                    }
                    throw var22;
                }
                writer.close();
            } catch (IOException var23) {
                var23.printStackTrace();
            }
        }
    }

    private static <T> T read(Gson gson, String file, Class<T> type) {
        try {
            Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            Object var4;
            try {
                var4 = gson.fromJson(reader, type);
            } catch (Throwable var7) {
                try {
                    reader.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            reader.close();
            return (T) var4;
        } catch (IOException var8) {
            throw new RuntimeException(var8);
        }
    }

    private static String stripColons(String s) {
        return s.substring(1, s.length() - 1);
    }

    public static class EmojiEntry {

        public String unified;

        public String name;

        public Set<String> short_names;

        public String category;

        public String subcategory;

        public int sort_order;

        public String added_in;

        public EmojiEntry(String unified) {
            this.unified = unified;
            this.short_names = new LinkedHashSet();
        }

        public boolean validate() {
            return !Emoji.isRegionalIndicatorSymbol(Integer.parseInt(this.unified.split("-")[0], 16));
        }

        public Object[] flatten() {
            int[] cps = Arrays.stream(this.unified.split("-")).mapToInt(c -> Integer.parseInt(c, 16)).toArray();
            return new Object[] { new String(cps, 0, cps.length), this.name, this.short_names, this.category, this.subcategory, this.sort_order, this.added_in };
        }
    }
}