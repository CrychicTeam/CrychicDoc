package snownee.kiwi.datagen;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider.TranslationBuilder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import snownee.kiwi.config.ConfigHandler;
import snownee.kiwi.config.ConfigUI;
import snownee.kiwi.config.KiwiConfigManager;
import snownee.kiwi.util.Util;

public class KiwiLanguageProvider extends FabricLanguageProvider {

    protected final FabricDataOutput dataOutput;

    protected final String languageCode;

    public KiwiLanguageProvider(FabricDataOutput dataOutput) {
        this(dataOutput, "en_us");
    }

    public KiwiLanguageProvider(FabricDataOutput dataOutput, String languageCode) {
        super(dataOutput, languageCode);
        this.dataOutput = dataOutput;
        this.languageCode = languageCode;
    }

    public void generateTranslations(TranslationBuilder translationBuilder) {
    }

    public Path createPath(String path) {
        return this.dataOutput.getForgeModContainer().getOwningFile().getFile().findResource(new String[] { "assets/%s/lang/%s.json".formatted(this.dataOutput.getModId(), path) });
    }

    public void putExistingTranslations(TranslationBuilder translationBuilder) {
        this.putExistingTranslations(translationBuilder, this.languageCode + ".existing");
    }

    public void putExistingTranslations(TranslationBuilder translationBuilder, String path) {
        try {
            Path existingFilePath = this.createPath(path);
            translationBuilder.add(existingFilePath);
        } catch (Exception var4) {
            throw new RuntimeException("Failed to add existing language file!", var4);
        }
    }

    public CompletableFuture<?> run(CachedOutput writer) {
        TreeMap<String, String> translationEntries = new TreeMap();
        if ("en_us".equals(this.languageCode)) {
            this.generateConfigEntries(translationEntries);
            this.generateTranslations((key, value) -> {
                Objects.requireNonNull(key);
                Objects.requireNonNull(value);
                if (translationEntries.containsKey(key)) {
                    throw new RuntimeException("Existing translation key found - " + key + " - Duplicate will be ignored.");
                } else {
                    translationEntries.put(key, value);
                }
            });
            this.putExistingTranslations((key, value) -> {
                Objects.requireNonNull(key);
                Objects.requireNonNull(value);
                translationEntries.put(key, value);
            });
        }
        JsonObject langEntryJson = new JsonObject();
        for (Entry<String, String> entry : translationEntries.entrySet()) {
            langEntryJson.addProperty((String) entry.getKey(), (String) entry.getValue());
        }
        return DataProvider.saveStable(writer, langEntryJson, this.getLangFilePath(this.languageCode));
    }

    private void generateConfigEntries(Map<String, String> translationEntries) {
        Joiner joiner = Joiner.on('.');
        for (ConfigHandler handler : KiwiConfigManager.allConfigs) {
            if (Objects.equals(handler.getModId(), this.dataOutput.getModId()) && !handler.getFileName().equals("test") && !handler.getFileName().equals("kiwi-modules")) {
                String key = handler.getTranslationKey();
                if (Objects.equals(key, handler.getFileName())) {
                    translationEntries.put("kiwi.config." + key, Util.friendlyText(key));
                }
                Set<String> subCats = Sets.newHashSet();
                for (ConfigHandler.Value<?> value : handler.getValueMap().values()) {
                    ConfigUI.Hide hide = value.getAnnotation(ConfigUI.Hide.class);
                    if (hide == null) {
                        List<String> path = Lists.newArrayList(value.path.split("\\."));
                        String title = Util.friendlyText((String) path.remove(path.size() - 1));
                        String subCatKey = joiner.join(path);
                        if (!path.isEmpty() && !subCats.contains(subCatKey)) {
                            subCats.add(subCatKey);
                            translationEntries.put(handler.getModId() + ".config." + subCatKey, Util.friendlyText((String) path.get(path.size() - 1)));
                        }
                        translationEntries.put(value.translation, title);
                        translationEntries.put(value.translation + ".desc", "");
                    }
                }
            }
        }
    }

    private Path getLangFilePath(String code) {
        return this.dataOutput.m_245269_(PackOutput.Target.RESOURCE_PACK, "lang").json(new ResourceLocation(this.dataOutput.getModId(), code));
    }
}