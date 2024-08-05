package net.minecraft.stats;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.RecipeBookType;

public final class RecipeBookSettings {

    private static final Map<RecipeBookType, Pair<String, String>> TAG_FIELDS = ImmutableMap.of(RecipeBookType.CRAFTING, Pair.of("isGuiOpen", "isFilteringCraftable"), RecipeBookType.FURNACE, Pair.of("isFurnaceGuiOpen", "isFurnaceFilteringCraftable"), RecipeBookType.BLAST_FURNACE, Pair.of("isBlastingFurnaceGuiOpen", "isBlastingFurnaceFilteringCraftable"), RecipeBookType.SMOKER, Pair.of("isSmokerGuiOpen", "isSmokerFilteringCraftable"));

    private final Map<RecipeBookType, RecipeBookSettings.TypeSettings> states;

    private RecipeBookSettings(Map<RecipeBookType, RecipeBookSettings.TypeSettings> mapRecipeBookTypeRecipeBookSettingsTypeSettings0) {
        this.states = mapRecipeBookTypeRecipeBookSettingsTypeSettings0;
    }

    public RecipeBookSettings() {
        this(Util.make(Maps.newEnumMap(RecipeBookType.class), p_12740_ -> {
            for (RecipeBookType $$1 : RecipeBookType.values()) {
                p_12740_.put($$1, new RecipeBookSettings.TypeSettings(false, false));
            }
        }));
    }

    public boolean isOpen(RecipeBookType recipeBookType0) {
        return ((RecipeBookSettings.TypeSettings) this.states.get(recipeBookType0)).open;
    }

    public void setOpen(RecipeBookType recipeBookType0, boolean boolean1) {
        ((RecipeBookSettings.TypeSettings) this.states.get(recipeBookType0)).open = boolean1;
    }

    public boolean isFiltering(RecipeBookType recipeBookType0) {
        return ((RecipeBookSettings.TypeSettings) this.states.get(recipeBookType0)).filtering;
    }

    public void setFiltering(RecipeBookType recipeBookType0, boolean boolean1) {
        ((RecipeBookSettings.TypeSettings) this.states.get(recipeBookType0)).filtering = boolean1;
    }

    public static RecipeBookSettings read(FriendlyByteBuf friendlyByteBuf0) {
        Map<RecipeBookType, RecipeBookSettings.TypeSettings> $$1 = Maps.newEnumMap(RecipeBookType.class);
        for (RecipeBookType $$2 : RecipeBookType.values()) {
            boolean $$3 = friendlyByteBuf0.readBoolean();
            boolean $$4 = friendlyByteBuf0.readBoolean();
            $$1.put($$2, new RecipeBookSettings.TypeSettings($$3, $$4));
        }
        return new RecipeBookSettings($$1);
    }

    public void write(FriendlyByteBuf friendlyByteBuf0) {
        for (RecipeBookType $$1 : RecipeBookType.values()) {
            RecipeBookSettings.TypeSettings $$2 = (RecipeBookSettings.TypeSettings) this.states.get($$1);
            if ($$2 == null) {
                friendlyByteBuf0.writeBoolean(false);
                friendlyByteBuf0.writeBoolean(false);
            } else {
                friendlyByteBuf0.writeBoolean($$2.open);
                friendlyByteBuf0.writeBoolean($$2.filtering);
            }
        }
    }

    public static RecipeBookSettings read(CompoundTag compoundTag0) {
        Map<RecipeBookType, RecipeBookSettings.TypeSettings> $$1 = Maps.newEnumMap(RecipeBookType.class);
        TAG_FIELDS.forEach((p_12750_, p_12751_) -> {
            boolean $$4 = compoundTag0.getBoolean((String) p_12751_.getFirst());
            boolean $$5 = compoundTag0.getBoolean((String) p_12751_.getSecond());
            $$1.put(p_12750_, new RecipeBookSettings.TypeSettings($$4, $$5));
        });
        return new RecipeBookSettings($$1);
    }

    public void write(CompoundTag compoundTag0) {
        TAG_FIELDS.forEach((p_12745_, p_12746_) -> {
            RecipeBookSettings.TypeSettings $$3 = (RecipeBookSettings.TypeSettings) this.states.get(p_12745_);
            compoundTag0.putBoolean((String) p_12746_.getFirst(), $$3.open);
            compoundTag0.putBoolean((String) p_12746_.getSecond(), $$3.filtering);
        });
    }

    public RecipeBookSettings copy() {
        Map<RecipeBookType, RecipeBookSettings.TypeSettings> $$0 = Maps.newEnumMap(RecipeBookType.class);
        for (RecipeBookType $$1 : RecipeBookType.values()) {
            RecipeBookSettings.TypeSettings $$2 = (RecipeBookSettings.TypeSettings) this.states.get($$1);
            $$0.put($$1, $$2.copy());
        }
        return new RecipeBookSettings($$0);
    }

    public void replaceFrom(RecipeBookSettings recipeBookSettings0) {
        this.states.clear();
        for (RecipeBookType $$1 : RecipeBookType.values()) {
            RecipeBookSettings.TypeSettings $$2 = (RecipeBookSettings.TypeSettings) recipeBookSettings0.states.get($$1);
            this.states.put($$1, $$2.copy());
        }
    }

    public boolean equals(Object object0) {
        return this == object0 || object0 instanceof RecipeBookSettings && this.states.equals(((RecipeBookSettings) object0).states);
    }

    public int hashCode() {
        return this.states.hashCode();
    }

    static final class TypeSettings {

        boolean open;

        boolean filtering;

        public TypeSettings(boolean boolean0, boolean boolean1) {
            this.open = boolean0;
            this.filtering = boolean1;
        }

        public RecipeBookSettings.TypeSettings copy() {
            return new RecipeBookSettings.TypeSettings(this.open, this.filtering);
        }

        public boolean equals(Object object0) {
            if (this == object0) {
                return true;
            } else {
                return !(object0 instanceof RecipeBookSettings.TypeSettings $$1) ? false : this.open == $$1.open && this.filtering == $$1.filtering;
            }
        }

        public int hashCode() {
            int $$0 = this.open ? 1 : 0;
            return 31 * $$0 + (this.filtering ? 1 : 0);
        }

        public String toString() {
            return "[open=" + this.open + ", filtering=" + this.filtering + "]";
        }
    }
}