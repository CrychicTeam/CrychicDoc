package journeymap.client.cartography.color;

import com.google.common.collect.HashBasedTable;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.Since;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.client.log.ChatLog;
import journeymap.client.model.BlockMD;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.Logger;

public class ColorPalette {

    public static final String HELP_PAGE = "http://journeymap.info/Color_Palette";

    public static final String SAMPLE_STANDARD_PATH = ".minecraft/journeymap/";

    public static final String SAMPLE_WORLD_PATH = ".minecraft/journeymap/data/*/worldname/";

    public static final String JSON_FILENAME = "colorpalette.json";

    public static final String HTML_FILENAME = "colorpalette.html";

    public static final String VARIABLE = "var colorpalette=";

    public static final Charset UTF8 = Charset.forName("UTF-8");

    public static final double VERSION = 5.49;

    private static final Logger logger = Journeymap.getLogger();

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(HashBasedTable.class, new ColorPalette.Serializer()).registerTypeAdapter(HashBasedTable.class, new ColorPalette.Deserializer()).create();

    @Since(3.0)
    double version;

    @Since(1.0)
    String name;

    @Since(1.0)
    String generated;

    @Since(1.0)
    String[] description;

    @Since(1.0)
    boolean permanent;

    @Since(1.0)
    String resourcePacks;

    @Since(2.0)
    String modNames;

    @Since(5.49)
    HashBasedTable<String, String, BlockStateColor> table;

    private transient File origin;

    private transient boolean dirty;

    ColorPalette() {
        this.table = HashBasedTable.create(Block.BLOCK_STATE_REGISTRY.size(), 16);
    }

    private ColorPalette(String resourcePacks, String modNames) {
        this.version = 5.49;
        this.name = Constants.getString("jm.colorpalette.file_title");
        this.generated = String.format("Generated using %s for %s on %s", JourneymapClient.MOD_NAME, "1.20.1", new Date());
        this.resourcePacks = resourcePacks;
        this.modNames = modNames;
        ArrayList<String> lines = new ArrayList();
        lines.add(Constants.getString("jm.colorpalette.file_header_1"));
        lines.add(Constants.getString("jm.colorpalette.file_header_2", "colorpalette.html"));
        lines.add(Constants.getString("jm.colorpalette.file_header_3", "colorpalette.json", ".minecraft/journeymap/data/*/worldname/"));
        lines.add(Constants.getString("jm.colorpalette.file_header_4", "colorpalette.json", ".minecraft/journeymap/"));
        lines.add(Constants.getString("jm.config.file_header_5", "http://journeymap.info/Color_Palette"));
        this.description = (String[]) lines.toArray(new String[4]);
        this.table = HashBasedTable.create(Block.BLOCK_STATE_REGISTRY.size(), 16);
    }

    public static ColorPalette getActiveColorPalette() {
        String resourcePacks = ColorManager.getResourcePackNames();
        String modNames = LoaderHooks.getModNames();
        File worldPaletteFile = getWorldPaletteFile();
        if (worldPaletteFile.canRead()) {
            ColorPalette palette = loadFromFile(worldPaletteFile);
            if (palette != null) {
                if (!(palette.version < 5.49)) {
                    return palette;
                }
                logger.warn(String.format("Existing world color palette is obsolete. Required version: %s.  Found version: %s", 5.49, palette.version));
            }
        }
        File standardPaletteFile = getStandardPaletteFile();
        if (standardPaletteFile.canRead()) {
            ColorPalette palette = loadFromFile(standardPaletteFile);
            if (palette != null && palette.version != 5.49) {
                logger.warn(String.format("Existing color palette is unusable. Required version: %s.  Found version: %s", 5.49, palette.version));
                standardPaletteFile.renameTo(new File(standardPaletteFile.getParentFile(), standardPaletteFile.getName() + ".v" + palette.version));
                palette = null;
            }
            if (palette != null) {
                if (palette.isPermanent()) {
                    logger.info("Existing color palette is set to be permanent.");
                    return palette;
                }
                if (resourcePacks.equals(palette.resourcePacks)) {
                    if (modNames.equals(palette.modNames)) {
                        logger.debug("Existing color palette's resource packs and mod names match current loadout.");
                        return palette;
                    }
                    logger.warn("Existing color palette's mods no longer match current loadout.");
                    logger.info(String.format("WAS: %s\nNOW: %s", palette.modNames, modNames));
                } else {
                    logger.warn("Existing color palette's resource packs no longer match current loadout.");
                    logger.info(String.format("WAS: %s\nNOW: %s", palette.resourcePacks, resourcePacks));
                }
            }
        }
        return null;
    }

    public static ColorPalette create(boolean standard, boolean permanent) {
        long start = System.currentTimeMillis();
        ColorPalette palette = null;
        try {
            String resourcePackNames = ColorManager.getResourcePackNames();
            String modPackNames = LoaderHooks.getModNames();
            palette = new ColorPalette(resourcePackNames, modPackNames);
            palette.setPermanent(permanent);
            palette.writeToFile(standard);
            long elapsed = System.currentTimeMillis() - start;
            logger.info(String.format("Color palette file generated for %d blockstates in %dms for: %s", palette.size(), elapsed, palette.getOrigin()));
            return palette;
        } catch (Exception var9) {
            logger.error("Couldn't create ColorPalette: " + LogFormatter.toString(var9));
            return null;
        }
    }

    private static File getWorldPaletteFile() {
        Minecraft mc = Minecraft.getInstance();
        return new File(FileHandler.getJMWorldDir(mc), "colorpalette.json");
    }

    private static File getStandardPaletteFile() {
        return new File(FileHandler.getJourneyMapDir(), "colorpalette.json");
    }

    private static ColorPalette loadFromFile(File file) {
        String jsonString = null;
        try {
            jsonString = Files.toString(file, UTF8).replaceFirst("var colorpalette=", "");
            ColorPalette palette = (ColorPalette) GSON.fromJson(jsonString, ColorPalette.class);
            palette.origin = file;
            palette.getOriginHtml(true, true);
            return palette;
        } catch (Throwable var5) {
            ChatLog.announceError(Constants.getString("jm.colorpalette.file_error", file.getPath()));
            try {
                file.renameTo(new File(file.getParentFile(), file.getName() + ".bad"));
            } catch (Exception var4) {
                logger.error("Couldn't rename bad palette file: " + var4);
            }
            return null;
        }
    }

    private String substituteValueInContents(String contents, String key, Object... params) {
        String token = String.format("\\$%s\\$", key);
        return contents.replaceAll(token, Matcher.quoteReplacement(Constants.getString(key, params)));
    }

    boolean hasBlockStateColor(BlockMD blockMD) {
        return this.table.contains(BlockMD.getBlockId(blockMD), BlockMD.getBlockStateId(blockMD));
    }

    @Nullable
    private BlockStateColor getBlockStateColor(BlockMD blockMD, boolean createIfMissing) {
        BlockStateColor blockStateColor = (BlockStateColor) this.table.get(BlockMD.getBlockId(blockMD), BlockMD.getBlockStateId(blockMD));
        if (blockStateColor == null && createIfMissing && blockMD.hasColor()) {
            blockStateColor = new BlockStateColor(blockMD);
            this.table.put(BlockMD.getBlockId(blockMD), BlockMD.getBlockStateId(blockMD), blockStateColor);
            this.dirty = true;
        }
        return blockStateColor;
    }

    public boolean applyColor(BlockMD blockMD, boolean createIfMissing) {
        boolean preExisting = this.hasBlockStateColor(blockMD);
        BlockStateColor blockStateColor = this.getBlockStateColor(blockMD, createIfMissing);
        if (blockStateColor == null) {
            return false;
        } else {
            if (preExisting && !this.permanent) {
                if (blockMD.hasTransparency()) {
                    blockMD.setAlpha(blockStateColor.alpha != null ? blockStateColor.alpha : 1.0F);
                }
                int color = RGB.hexToInt(blockStateColor.color);
                blockMD.setColor(color);
            }
            return true;
        }
    }

    public int applyColors(Collection<BlockMD> blockMDs, boolean createIfMissing) {
        int hit = 0;
        int miss = 0;
        for (BlockMD blockMD : blockMDs) {
            if (this.applyColor(blockMD, createIfMissing)) {
                hit++;
            } else {
                miss++;
            }
        }
        if (miss > 0) {
            logger.debug(miss + " blockstates didn't have a color in the palette");
        }
        return hit;
    }

    public void writeToFile() {
        this.writeToFile(this.isStandard());
    }

    private boolean writeToFile(boolean standard) {
        File palleteFile = null;
        try {
            palleteFile = standard ? getStandardPaletteFile() : getWorldPaletteFile();
            Files.write("var colorpalette=" + GSON.toJson(this), palleteFile, UTF8);
            this.origin = palleteFile;
            this.dirty = false;
            this.getOriginHtml(true, true);
            return true;
        } catch (Exception var4) {
            logger.error(String.format("Can't save color palette file %s: %s", palleteFile, LogFormatter.toString(var4)));
            return false;
        }
    }

    public File getOrigin() throws IOException {
        return this.origin.getCanonicalFile();
    }

    public File getOriginHtml(boolean createIfMissing, boolean overwriteExisting) {
        try {
            if (this.origin == null) {
                return null;
            } else {
                File htmlFile = new File(this.origin.getParentFile(), "colorpalette.html");
                if (!htmlFile.exists() && createIfMissing || overwriteExisting) {
                    htmlFile = FileHandler.copyColorPaletteHtmlFile(this.origin.getParentFile(), "colorpalette.html");
                    String htmlString = Files.toString(htmlFile, UTF8);
                    htmlString = this.substituteValueInContents(htmlString, "jm.colorpalette.file_title");
                    htmlString = this.substituteValueInContents(htmlString, "jm.colorpalette.file_missing_data", "colorpalette.json");
                    htmlString = this.substituteValueInContents(htmlString, "jm.colorpalette.resource_packs");
                    htmlString = this.substituteValueInContents(htmlString, "jm.colorpalette.mods");
                    htmlString = this.substituteValueInContents(htmlString, "jm.colorpalette.basic_colors");
                    htmlString = this.substituteValueInContents(htmlString, "jm.colorpalette.biome_colors");
                    Files.write(htmlString, htmlFile, UTF8);
                }
                return htmlFile;
            }
        } catch (Throwable var5) {
            logger.error("Can't write colorpalette.html: " + var5);
            return null;
        }
    }

    public boolean isPermanent() {
        return this.permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isStandard() {
        return this.origin != null && this.origin.getParentFile().getAbsoluteFile().equals(FileHandler.getJourneyMapDir().getAbsoluteFile());
    }

    public double getVersion() {
        return this.version;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public int size() {
        return this.table.size();
    }

    public String toString() {
        return "ColorPalette[" + this.resourcePacks + "]";
    }

    private static class Deserializer implements JsonDeserializer<HashBasedTable<String, String, BlockStateColor>> {

        public HashBasedTable<String, String, BlockStateColor> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            HashBasedTable<String, String, BlockStateColor> result = HashBasedTable.create(Block.BLOCK_STATE_REGISTRY.size(), 16);
            JsonObject jsonTable = json.getAsJsonObject();
            for (Entry<String, JsonElement> jsonMod : jsonTable.entrySet()) {
                String modId = (String) jsonMod.getKey();
                for (Entry<String, JsonElement> jsonBlock : ((JsonElement) jsonMod.getValue()).getAsJsonObject().entrySet()) {
                    String blockId = (String) jsonBlock.getKey();
                    for (Entry<String, JsonElement> jsonState : ((JsonElement) jsonBlock.getValue()).getAsJsonObject().entrySet()) {
                        String blockStateId = (String) jsonState.getKey();
                        JsonArray bscArray = ((JsonElement) jsonState.getValue()).getAsJsonArray();
                        String color = bscArray.get(0).getAsString();
                        float alpha = 1.0F;
                        if (bscArray.size() > 1) {
                            alpha = bscArray.get(1).getAsFloat();
                        }
                        BlockStateColor blockStateColor = new BlockStateColor(color, alpha);
                        result.put(modId + ":" + blockId, blockStateId, blockStateColor);
                    }
                }
            }
            return result;
        }
    }

    private static class Serializer implements JsonSerializer<HashBasedTable<String, String, BlockStateColor>> {

        public JsonElement serialize(HashBasedTable<String, String, BlockStateColor> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonTable = new JsonObject();
            for (String blockId : (List) src.rowKeySet().stream().sorted().collect(Collectors.toList())) {
                String[] resource = blockId.split(":");
                String mod = resource[0];
                String block = resource[1];
                JsonObject jsonMod = null;
                if (!jsonTable.has(mod)) {
                    jsonMod = new JsonObject();
                    jsonTable.add(mod, jsonMod);
                } else {
                    jsonMod = jsonTable.getAsJsonObject(mod);
                }
                JsonObject jsonBlock = null;
                if (!jsonMod.has(block)) {
                    jsonBlock = new JsonObject();
                    jsonMod.add(block, jsonBlock);
                } else {
                    jsonBlock = jsonMod.getAsJsonObject(block);
                }
                for (String stateId : (List) src.row(blockId).keySet().stream().sorted().collect(Collectors.toList())) {
                    BlockStateColor blockStateColor = (BlockStateColor) src.get(blockId, stateId);
                    JsonArray bscArray = new JsonArray();
                    bscArray.add(new JsonPrimitive(blockStateColor.color));
                    if (blockStateColor.alpha != null && blockStateColor.alpha != 1.0F) {
                        bscArray.add(new JsonPrimitive(blockStateColor.alpha));
                    }
                    jsonBlock.add(stateId, bscArray);
                }
            }
            return jsonTable;
        }
    }
}