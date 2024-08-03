package com.github.alexmodguy.alexscaves.server.misc;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;

public class CaveBookProgress {

    public static final String PLAYER_CAVE_BOOK_PROGRESS_TAG = "AlexsCavesBookProgress";

    private Map<String, CaveBookProgress.Subcategory> unlockedPages = new HashMap();

    private CaveBookProgress(CompoundTag tag) {
        if (tag.contains("Pages")) {
            ListTag listTag = tag.getList("Pages", 10);
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag compoundtag = listTag.getCompound(i);
                this.unlockedPages.put(compoundtag.getString("Category"), CaveBookProgress.Subcategory.getByOrdinal(compoundtag.getInt("SubCategory")));
            }
        }
    }

    public static CaveBookProgress getCaveBookProgress(Player player) {
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(player);
        CompoundTag tag1 = tag.contains("AlexsCavesBookProgress") ? tag.getCompound("AlexsCavesBookProgress") : new CompoundTag();
        return new CaveBookProgress(tag1);
    }

    public static void saveCaveBookProgress(CaveBookProgress caveBookProgress, Player player) {
        CompoundTag savedTag = caveBookProgress.save();
        CompoundTag tag = CitadelEntityData.getOrCreateCitadelTag(player);
        tag.put("AlexsCavesBookProgress", savedTag);
        CitadelEntityData.setCitadelTag(player, tag);
        if (!player.m_9236_().isClientSide) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelTagUpdate", tag, player.m_19879_()));
        } else {
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelTagUpdate", tag, player.m_19879_()));
        }
    }

    private CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();
        for (Entry<String, CaveBookProgress.Subcategory> entry : this.unlockedPages.entrySet()) {
            CompoundTag innerTag = new CompoundTag();
            innerTag.putString("Category", (String) entry.getKey());
            innerTag.putInt("SubCategory", ((CaveBookProgress.Subcategory) entry.getValue()).ordinal());
            listTag.add(innerTag);
        }
        tag.put("Pages", listTag);
        return tag;
    }

    public boolean unlockNextFor(String biomeCategory) {
        int prev = ((CaveBookProgress.Subcategory) this.unlockedPages.getOrDefault(biomeCategory, CaveBookProgress.Subcategory.EMPTY)).ordinal();
        if (prev >= CaveBookProgress.Subcategory.values().length - 1) {
            return false;
        } else if (CaveBookProgress.Subcategory.canUnlockNext(biomeCategory, prev)) {
            CaveBookProgress.Subcategory unlocked = AlexsCaves.COMMON_CONFIG.onlyOneResearchNeeded.get() ? CaveBookProgress.Subcategory.getLastUnlockableFor(biomeCategory) : CaveBookProgress.Subcategory.getByOrdinal(prev + 1);
            this.unlockedPages.put(biomeCategory, unlocked);
            return true;
        } else {
            return false;
        }
    }

    public boolean isUnlockedFor(String biomeCategory, CaveBookProgress.Subcategory subcategory) {
        int prev = ((CaveBookProgress.Subcategory) this.unlockedPages.getOrDefault(biomeCategory, CaveBookProgress.Subcategory.EMPTY)).ordinal();
        return subcategory.ordinal() <= prev;
    }

    public CaveBookProgress.Subcategory getLastUnlockedCategory(String biomeCategory) {
        return (CaveBookProgress.Subcategory) this.unlockedPages.getOrDefault(biomeCategory, CaveBookProgress.Subcategory.EMPTY);
    }

    public boolean isUnlockedFor(String key) {
        CaveBookProgress.Subcategory subcategory = this.getSubcategoryFromPage(key);
        String biomeCategory = this.getBiomeFromPage(key);
        return this.isUnlockedFor(biomeCategory, subcategory);
    }

    public String getBiomeFromPage(String key) {
        int lastIndexOfUnderscore = key.lastIndexOf("_");
        return lastIndexOfUnderscore >= 0 && lastIndexOfUnderscore + 1 < key.length() ? key.substring(0, lastIndexOfUnderscore) : "";
    }

    public CaveBookProgress.Subcategory getSubcategoryFromPage(String key) {
        int lastIndexOfUnderscore = key.lastIndexOf("_");
        if (lastIndexOfUnderscore >= 0 && lastIndexOfUnderscore + 1 < key.length()) {
            String subCatStr = key.substring(lastIndexOfUnderscore + 1);
            return CaveBookProgress.Subcategory.valueOf(subCatStr.toUpperCase(Locale.ROOT));
        } else {
            return CaveBookProgress.Subcategory.EMPTY;
        }
    }

    public static enum Subcategory {

        EMPTY,
        GENERAL,
        RESOURCES,
        MOBS,
        UTILITIES,
        SECRETS(ACBiomeRegistry.PRIMORDIAL_CAVES, ACBiomeRegistry.TOXIC_CAVES);

        private final ResourceKey<Biome>[] limitedTo;

        private Subcategory(ResourceKey<Biome>... limitedTo) {
            this.limitedTo = limitedTo;
        }

        public static CaveBookProgress.Subcategory getByOrdinal(int subCategory) {
            return values()[Mth.clamp(subCategory, 0, values().length - 1)];
        }

        public static boolean canUnlockNext(String category, int currentLevel) {
            CaveBookProgress.Subcategory next = getByOrdinal(currentLevel + 1);
            return next.limitedTo.length == 0 || Arrays.stream(next.limitedTo).anyMatch(biomeResourceKey -> biomeResourceKey.location().toString().equals(category));
        }

        public static CaveBookProgress.Subcategory getLastUnlockableFor(String category) {
            CaveBookProgress.Subcategory subcategory = SECRETS;
            return subcategory.limitedTo.length > 0 && Arrays.stream(subcategory.limitedTo).anyMatch(biomeResourceKey -> biomeResourceKey.location().toString().equals(category)) ? subcategory : getByOrdinal(subcategory.ordinal() - 1);
        }
    }
}