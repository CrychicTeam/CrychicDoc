package com.simibubi.create.foundation.ponder;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.infrastructure.ponder.PonderIndex;
import com.tterrag.registrate.AbstractRegistrate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;

public class PonderLocalization {

    static final Map<ResourceLocation, String> SHARED = new HashMap();

    static final Map<ResourceLocation, Couple<String>> TAG = new HashMap();

    static final Map<ResourceLocation, String> CHAPTER = new HashMap();

    static final Map<ResourceLocation, Map<String, String>> SPECIFIC = new HashMap();

    public static final String LANG_PREFIX = "ponder.";

    private static boolean sceneLangGenerated = false;

    public static void registerShared(ResourceLocation key, String enUS) {
        SHARED.put(key, enUS);
    }

    public static void registerTag(ResourceLocation key, String enUS, String description) {
        TAG.put(key, Couple.create(enUS, description));
    }

    public static void registerChapter(ResourceLocation key, String enUS) {
        CHAPTER.put(key, enUS);
    }

    public static void registerSpecific(ResourceLocation sceneId, String key, String enUS) {
        ((Map) SPECIFIC.computeIfAbsent(sceneId, $ -> new HashMap())).put(key, enUS);
    }

    protected static String langKeyForShared(ResourceLocation k) {
        return k.getNamespace() + ".ponder.shared." + k.getPath();
    }

    protected static String langKeyForTag(ResourceLocation k) {
        return k.getNamespace() + ".ponder.tag." + k.getPath();
    }

    protected static String langKeyForTagDescription(ResourceLocation k) {
        return k.getNamespace() + ".ponder.tag." + k.getPath() + ".description";
    }

    protected static String langKeyForChapter(ResourceLocation k) {
        return k.getNamespace() + ".ponder.chapter." + k.getPath();
    }

    protected static String langKeyForSpecific(ResourceLocation sceneId, String k) {
        return sceneId.getNamespace() + ".ponder." + sceneId.getPath() + "." + k;
    }

    public static String getShared(ResourceLocation key) {
        if (PonderIndex.editingModeActive()) {
            return SHARED.containsKey(key) ? (String) SHARED.get(key) : "unregistered shared entry: " + key;
        } else {
            return I18n.get(langKeyForShared(key));
        }
    }

    public static String getTag(ResourceLocation key) {
        if (PonderIndex.editingModeActive()) {
            return TAG.containsKey(key) ? (String) ((Couple) TAG.get(key)).getFirst() : "unregistered tag entry: " + key;
        } else {
            return I18n.get(langKeyForTag(key));
        }
    }

    public static String getTagDescription(ResourceLocation key) {
        if (PonderIndex.editingModeActive()) {
            return TAG.containsKey(key) ? (String) ((Couple) TAG.get(key)).getSecond() : "unregistered tag entry: " + key;
        } else {
            return I18n.get(langKeyForTagDescription(key));
        }
    }

    public static String getChapter(ResourceLocation key) {
        if (PonderIndex.editingModeActive()) {
            return CHAPTER.containsKey(key) ? (String) CHAPTER.get(key) : "unregistered chapter entry: " + key;
        } else {
            return I18n.get(langKeyForChapter(key));
        }
    }

    public static String getSpecific(ResourceLocation sceneId, String k) {
        return PonderIndex.editingModeActive() ? (String) ((Map) SPECIFIC.get(sceneId)).get(k) : I18n.get(langKeyForSpecific(sceneId, k));
    }

    public static void generateSceneLang() {
        if (!sceneLangGenerated) {
            sceneLangGenerated = true;
            PonderRegistry.ALL.forEach((id, list) -> {
                for (int i = 0; i < list.size(); i++) {
                    PonderRegistry.compileScene(i, (PonderStoryBoardEntry) list.get(i), null);
                }
            });
        }
    }

    public static void provideLang(String namespace, BiConsumer<String, String> consumer) {
        SHARED.forEach((k, v) -> {
            if (k.getNamespace().equals(namespace)) {
                consumer.accept(langKeyForShared(k), v);
            }
        });
        TAG.forEach((k, v) -> {
            if (k.getNamespace().equals(namespace)) {
                consumer.accept(langKeyForTag(k), (String) v.getFirst());
                consumer.accept(langKeyForTagDescription(k), (String) v.getSecond());
            }
        });
        CHAPTER.forEach((k, v) -> {
            if (k.getNamespace().equals(namespace)) {
                consumer.accept(langKeyForChapter(k), v);
            }
        });
        SPECIFIC.entrySet().stream().filter(entry -> ((ResourceLocation) entry.getKey()).getNamespace().equals(namespace)).sorted(Entry.comparingByKey()).forEach(entry -> ((Map) entry.getValue()).entrySet().stream().sorted(Entry.comparingByKey()).forEach(subEntry -> consumer.accept(langKeyForSpecific((ResourceLocation) entry.getKey(), (String) subEntry.getKey()), (String) subEntry.getValue())));
    }

    @Deprecated(forRemoval = true)
    public static void record(String namespace, JsonObject object) {
        provideLang(namespace, object::addProperty);
    }

    public static void provideRegistrateLang(AbstractRegistrate<?> registrate) {
        generateSceneLang();
        provideLang(registrate.getModid(), registrate::addRawLang);
    }
}