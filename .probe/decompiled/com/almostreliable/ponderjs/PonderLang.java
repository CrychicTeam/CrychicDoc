package com.almostreliable.ponderjs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.ponder.PonderLocalization;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.annotation.Nullable;
import org.apache.commons.io.FileUtils;

public class PonderLang {

    public static final String PATH = "kubejs/assets/ponderjs_generated/lang/%lang%.json";

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean generate(String langName) {
        File file = new File("kubejs/assets/ponderjs_generated/lang/%lang%.json".replace("%lang%", langName));
        JsonObject existingLang = this.read(file);
        JsonObject currentLang = this.createFromLocalization();
        if (currentLang.equals(existingLang)) {
            return false;
        } else {
            PonderJS.LOGGER.info("PonderJS - New lang file differ from existing lang file, generating new lang file.\n Old Lang size: {} \n\n New lang size: {}", existingLang == null ? 0 : existingLang.size(), currentLang.size());
            return this.write(file, currentLang);
        }
    }

    private boolean write(File file, JsonObject currentLang) {
        try {
            String output = GSON.toJson(currentLang);
            FileUtils.writeStringToFile(file, output, StandardCharsets.UTF_8);
            return true;
        } catch (IOException var4) {
            PonderJS.LOGGER.error(var4);
            return false;
        }
    }

    @Nullable
    protected JsonObject read(File file) {
        if (file.exists()) {
            try {
                String s = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
                return (JsonObject) GSON.fromJson(s, JsonObject.class);
            } catch (IOException var3) {
                PonderJS.LOGGER.error(var3);
            }
        }
        return null;
    }

    public JsonObject createFromLocalization() {
        PonderJS.STORIES_MANAGER.compileLang();
        JsonObject object = new JsonObject();
        PonderJS.NAMESPACES.forEach(namespace -> PonderLocalization.provideLang(namespace, object::addProperty));
        return object;
    }
}