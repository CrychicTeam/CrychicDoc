package vazkii.patchouli.client.base;

import com.google.common.base.Charsets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.util.SerializationUtil;

public final class PersistentData {

    private static final Path saveFile = Paths.get("patchouli_data.json");

    public static PersistentData.DataHolder data = new PersistentData.DataHolder(new JsonObject());

    public static void setup() {
        try {
            BufferedReader r = Files.newBufferedReader(saveFile, Charsets.UTF_8);
            try {
                JsonObject root = (JsonObject) SerializationUtil.RAW_GSON.fromJson(r, JsonObject.class);
                data = new PersistentData.DataHolder(root);
            } catch (Throwable var4) {
                if (r != null) {
                    try {
                        r.close();
                    } catch (Throwable var3) {
                        var4.addSuppressed(var3);
                    }
                }
                throw var4;
            }
            if (r != null) {
                r.close();
            }
        } catch (IOException var5) {
            if (!(var5 instanceof NoSuchFileException)) {
                PatchouliAPI.LOGGER.warn("Unable to load patchouli_data.json, replacing with default", var5);
            }
            data = new PersistentData.DataHolder(new JsonObject());
            save();
        } catch (Exception var6) {
            PatchouliAPI.LOGGER.warn("Corrupted patchouli_data.json, replacing with default", var6);
            data = new PersistentData.DataHolder(new JsonObject());
            save();
        }
    }

    public static void save() {
        JsonObject json = data.serialize();
        try {
            BufferedWriter w = Files.newBufferedWriter(saveFile, Charsets.UTF_8);
            try {
                SerializationUtil.PRETTY_GSON.toJson(json, w);
            } catch (Throwable var5) {
                if (w != null) {
                    try {
                        w.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (w != null) {
                w.close();
            }
        } catch (IOException var6) {
            PatchouliAPI.LOGGER.warn("Unable to save patchouli_data.json", var6);
        }
    }

    public static final class BookData {

        public final List<ResourceLocation> viewedEntries = new ArrayList();

        public final List<PersistentData.Bookmark> bookmarks = new ArrayList();

        public final List<ResourceLocation> history = new ArrayList();

        public final List<ResourceLocation> completedManualQuests = new ArrayList();

        public BookData(JsonObject root) {
            JsonArray emptyArray = new JsonArray();
            for (JsonElement e : GsonHelper.getAsJsonArray(root, "viewedEntries", emptyArray)) {
                this.viewedEntries.add(new ResourceLocation(e.getAsString()));
            }
            for (JsonElement e : GsonHelper.getAsJsonArray(root, "bookmarks", emptyArray)) {
                this.bookmarks.add(new PersistentData.Bookmark(e.getAsJsonObject()));
            }
            for (JsonElement e : GsonHelper.getAsJsonArray(root, "history", emptyArray)) {
                this.history.add(new ResourceLocation(e.getAsString()));
            }
            for (JsonElement e : GsonHelper.getAsJsonArray(root, "completedManualQuests", emptyArray)) {
                this.completedManualQuests.add(new ResourceLocation(e.getAsString()));
            }
        }

        public JsonObject serialize() {
            JsonObject ret = new JsonObject();
            JsonArray viewed = new JsonArray();
            this.viewedEntries.stream().map(Object::toString).forEach(viewed::add);
            ret.add("viewedEntries", viewed);
            JsonArray bookmarks = new JsonArray();
            this.bookmarks.stream().map(PersistentData.Bookmark::serialize).forEach(bookmarks::add);
            ret.add("bookmarks", bookmarks);
            JsonArray completed = new JsonArray();
            this.completedManualQuests.stream().map(Object::toString).forEach(completed::add);
            ret.add("completedManualQuests", completed);
            return ret;
        }
    }

    public static final class Bookmark {

        public final ResourceLocation entry;

        public final int spread;

        public Bookmark(ResourceLocation entry, int spread) {
            this.entry = entry;
            this.spread = spread;
        }

        public Bookmark(JsonObject root) {
            this.entry = new ResourceLocation(GsonHelper.getAsString(root, "entry"));
            this.spread = GsonHelper.getAsInt(root, "page");
        }

        public BookEntry getEntry(Book book) {
            return (BookEntry) book.getContents().entries.get(this.entry);
        }

        public JsonObject serialize() {
            JsonObject ret = new JsonObject();
            ret.addProperty("entry", this.entry.toString());
            ret.addProperty("page", this.spread);
            return ret;
        }
    }

    public static final class DataHolder {

        public int bookGuiScale;

        public boolean clickedVisualize;

        private final Map<ResourceLocation, PersistentData.BookData> bookData = new HashMap();

        public DataHolder(JsonObject root) {
            this.bookGuiScale = GsonHelper.getAsInt(root, "bookGuiScale", 0);
            this.clickedVisualize = GsonHelper.getAsBoolean(root, "clickedVisualize", false);
            JsonObject obj = GsonHelper.getAsJsonObject(root, "bookData", new JsonObject());
            for (Entry<String, JsonElement> e : obj.entrySet()) {
                this.bookData.put(new ResourceLocation((String) e.getKey()), new PersistentData.BookData(((JsonElement) e.getValue()).getAsJsonObject()));
            }
        }

        public PersistentData.BookData getBookData(Book book) {
            return (PersistentData.BookData) this.bookData.computeIfAbsent(book.id, k -> new PersistentData.BookData(new JsonObject()));
        }

        public JsonObject serialize() {
            JsonObject ret = new JsonObject();
            ret.addProperty("bookGuiScale", this.bookGuiScale);
            ret.addProperty("clickedVisualize", this.clickedVisualize);
            JsonObject books = new JsonObject();
            for (Entry<ResourceLocation, PersistentData.BookData> e : this.bookData.entrySet()) {
                books.add(((ResourceLocation) e.getKey()).toString(), ((PersistentData.BookData) e.getValue()).serialize());
            }
            ret.add("bookData", books);
            return ret;
        }
    }
}