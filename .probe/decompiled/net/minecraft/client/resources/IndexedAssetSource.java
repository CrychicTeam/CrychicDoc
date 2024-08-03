package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.server.packs.linkfs.LinkFileSystem;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

public class IndexedAssetSource {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Splitter PATH_SPLITTER = Splitter.on('/');

    public static Path createIndexFs(Path path0, String string1) {
        Path $$2 = path0.resolve("objects");
        LinkFileSystem.Builder $$3 = LinkFileSystem.builder();
        Path $$4 = path0.resolve("indexes/" + string1 + ".json");
        try {
            BufferedReader $$5 = Files.newBufferedReader($$4, StandardCharsets.UTF_8);
            try {
                JsonObject $$6 = GsonHelper.parse($$5);
                JsonObject $$7 = GsonHelper.getAsJsonObject($$6, "objects", null);
                if ($$7 != null) {
                    for (Entry<String, JsonElement> $$8 : $$7.entrySet()) {
                        JsonObject $$9 = (JsonObject) $$8.getValue();
                        String $$10 = (String) $$8.getKey();
                        List<String> $$11 = PATH_SPLITTER.splitToList($$10);
                        String $$12 = GsonHelper.getAsString($$9, "hash");
                        Path $$13 = $$2.resolve($$12.substring(0, 2) + "/" + $$12);
                        $$3.put($$11, $$13);
                    }
                }
            } catch (Throwable var16) {
                if ($$5 != null) {
                    try {
                        $$5.close();
                    } catch (Throwable var15) {
                        var16.addSuppressed(var15);
                    }
                }
                throw var16;
            }
            if ($$5 != null) {
                $$5.close();
            }
        } catch (JsonParseException var17) {
            LOGGER.error("Unable to parse resource index file: {}", $$4);
        } catch (IOException var18) {
            LOGGER.error("Can't open the resource index file: {}", $$4);
        }
        return $$3.build("index-" + string1).getPath("/");
    }
}