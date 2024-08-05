package com.simibubi.create.foundation.data;

import com.google.common.hash.HashCode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.Create;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;

@Deprecated(forRemoval = true)
public class LangMerger implements DataProvider {

    static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private static final String CATEGORY_HEADER = "\t\"_\": \"->------------------------]  %s  [------------------------<-\",";

    private final String modid;

    private final String displayName;

    private final LangPartial[] langPartials;

    private List<Object> mergedLangData;

    private List<String> langIgnore;

    private PackOutput output;

    public <T extends LangPartial> LangMerger(PackOutput output, String modid, String displayName, T[] allLangPartials) {
        this.output = output;
        this.modid = modid;
        this.displayName = displayName;
        this.langPartials = allLangPartials;
        this.mergedLangData = new ArrayList();
        this.langIgnore = new ArrayList();
        this.populateLangIgnore();
    }

    protected void populateLangIgnore() {
        this.langIgnore.add("create.ponder.debug_");
        this.langIgnore.add("create.gui.chromatic_projector");
    }

    private boolean shouldIgnore(String key) {
        for (String string : this.langIgnore) {
            if (key.startsWith(string)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return this.displayName + "'s lang merger";
    }

    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        Path path = this.output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "lang").json(new ResourceLocation(this.modid, "en_us"));
        return CompletableFuture.runAsync(() -> {
            try {
                this.collectExistingEntries(path);
                this.collectEntries();
                if (this.mergedLangData.isEmpty()) {
                    return;
                }
                this.save(pOutput, this.mergedLangData, path, "Merging en_us.json with hand-written lang entries...");
            } catch (IOException var4) {
                f_252483_.error("Failed to run LangMerger", var4);
            }
        }, Util.backgroundExecutor());
    }

    private void collectExistingEntries(Path path) throws IOException {
        if (!Files.exists(path, new LinkOption[0])) {
            Create.LOGGER.warn("Nothing to merge! It appears no lang was generated before me.");
        } else {
            BufferedReader reader = Files.newBufferedReader(path);
            try {
                JsonObject jsonobject = GsonHelper.fromJson(GSON, reader, JsonObject.class);
                Set<String> keysToRemove = new HashSet();
                MutableBoolean startErasing = new MutableBoolean();
                jsonobject.entrySet().stream().forEachOrdered(entry -> {
                    String key = (String) entry.getKey();
                    if (key.startsWith("advancement")) {
                        startErasing.setTrue();
                    }
                    if (!startErasing.isFalse()) {
                        keysToRemove.add(key);
                    }
                });
                jsonobject.remove("_");
                keysToRemove.forEach(jsonobject::remove);
                this.addAll("Game Elements", jsonobject);
                reader.close();
            } catch (Throwable var7) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    protected void addAll(String header, JsonObject jsonobject) {
        if (jsonobject != null) {
            header = String.format("\t\"_\": \"->------------------------]  %s  [------------------------<-\",", header);
            this.writeData("\n");
            this.writeData(header);
            this.writeData("\n\n");
            MutableObject<String> previousKey = new MutableObject("");
            jsonobject.entrySet().stream().forEachOrdered(entry -> {
                String key = (String) entry.getKey();
                if (!this.shouldIgnore(key)) {
                    String value = ((JsonElement) entry.getValue()).getAsString();
                    if (!((String) previousKey.getValue()).isEmpty() && this.shouldAddLineBreak(key, (String) previousKey.getValue())) {
                        this.writeData("\n");
                    }
                    this.writeEntry(key, value);
                    previousKey.setValue(key);
                }
            });
            this.writeData("\n");
        }
    }

    private void writeData(String data) {
        this.mergedLangData.add(data);
    }

    private void writeEntry(String key, String value) {
        this.mergedLangData.add(new LangEntry(key, value));
    }

    protected boolean shouldAddLineBreak(String key, String previousKey) {
        if (key.endsWith(".tooltip")) {
            return true;
        } else if (key.startsWith(this.modid + ".ponder") && key.endsWith("header")) {
            return true;
        } else {
            key = key.replaceFirst("\\.", "");
            previousKey = previousKey.replaceFirst("\\.", "");
            String[] split = key.split("\\.");
            String[] split2 = previousKey.split("\\.");
            return split.length != 0 && split2.length != 0 ? !split[0].equals(split2[0]) : false;
        }
    }

    private void collectEntries() {
        for (LangPartial partial : this.langPartials) {
            this.addAll(partial.getDisplayName(), partial.provide().getAsJsonObject());
        }
    }

    private void save(CachedOutput cache, List<Object> dataIn, Path target, String message) throws IOException {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(bytearrayoutputstream, StandardCharsets.UTF_8);
        writer.append(this.createString(dataIn));
        writer.close();
        CachedOutput.NO_CACHE.writeIfNeeded(target, bytearrayoutputstream.toByteArray(), HashCode.fromInt(0));
        Create.LOGGER.info(message);
    }

    protected String createString(List<Object> data) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        data.forEach(builder::append);
        builder.append("\t\"_\": \"Thank you for translating ").append(this.displayName).append("!\"\n\n");
        builder.append("}");
        return builder.toString();
    }
}