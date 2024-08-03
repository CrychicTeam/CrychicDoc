package fr.lucreeper74.createmetallurgy.data.lang;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.FilesHelper;
import com.tterrag.registrate.providers.RegistrateLangProvider;
import java.util.Map.Entry;
import java.util.function.BiConsumer;

public class CMLangGen {

    public static void generate(RegistrateLangProvider provider) {
        BiConsumer<String, String> langConsumer = provider::add;
        provideDefaultLang("tooltips", langConsumer);
        provideDefaultLang("interface", langConsumer);
    }

    private static void provideDefaultLang(String fileName, BiConsumer<String, String> consumer) {
        String path = "assets/createmetallurgy/lang/default/" + fileName + ".json";
        JsonElement jsonElement = FilesHelper.loadJsonResource(path);
        if (jsonElement == null) {
            throw new IllegalStateException(String.format("Could not find default lang file: %s", path));
        } else {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            for (Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String key = (String) entry.getKey();
                String value = ((JsonElement) entry.getValue()).getAsString();
                consumer.accept(key, value);
            }
        }
    }
}