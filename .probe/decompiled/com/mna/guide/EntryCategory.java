package com.mna.guide;

import com.mna.api.tools.RLoc;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class EntryCategory {

    private static final ArrayList<EntryCategory> all_categories = new ArrayList();

    public static final EntryCategory BASICS = new EntryCategory("basics", RLoc.create("guide_book"));

    public static final EntryCategory MANAWEAVING = new EntryCategory("manaweaving", RLoc.create("manaweaver_wand"));

    public static final EntryCategory RITUALS = new EntryCategory("rituals", RLoc.create("wizard_chalk"));

    public static final EntryCategory SORCERY = new EntryCategory("sorcery", RLoc.create("spell"));

    public static final EntryCategory RUNESMITHING = new EntryCategory("runesmithing", RLoc.create("runesmith_hammer"));

    public static final EntryCategory ARTIFICE = new EntryCategory("artifice", RLoc.create("arcane_ash"));

    public static final EntryCategory CONSTRUCTS = new EntryCategory("constructs", RLoc.create("constructs/construct_basic_head_wickerwood"));

    public static final EntryCategory ELDRIN_ALTAR = new EntryCategory("eldrin_altar", RLoc.create("eldrin_altar"));

    public static final EntryCategory ENCHANTMENTS = new EntryCategory("enchantments", RLoc.create("runic_anvil"));

    private final ResourceLocation icon;

    private final String id;

    private ItemStack __cachedStack;

    private final boolean isCustom;

    EntryCategory(String id, ResourceLocation icon) {
        this(id, icon, false);
    }

    EntryCategory(String id, ResourceLocation icon, boolean isCustom) {
        this.id = id;
        this.icon = icon;
        this.isCustom = isCustom;
        all_categories.add(this);
    }

    public static void Register(String id, ResourceLocation icon) {
        if (all_categories.stream().filter(c -> c.id.toLowerCase().equals(id.toLowerCase())).findFirst().isEmpty()) {
            new EntryCategory(id, icon, true);
        }
    }

    public ItemStack getDisplayStack() {
        if (this.__cachedStack == null) {
            Item item = ForgeRegistries.ITEMS.getValue(this.icon);
            if (item != null) {
                this.__cachedStack = new ItemStack(item);
                this.__cachedStack.setHoverName(Component.translatable(String.format("mechanic.%s:%s", "mna", this.id.toLowerCase())));
                this.__cachedStack.getOrCreateTag().putBoolean("hideTier", true);
            } else {
                this.__cachedStack = ItemStack.EMPTY;
            }
        }
        return this.__cachedStack;
    }

    public String name() {
        return this.id;
    }

    public boolean isCustom() {
        return this.isCustom;
    }

    public static EntryCategory valueOf(String id) {
        return (EntryCategory) all_categories.stream().filter(c -> c.id.toLowerCase().equals(id.toLowerCase())).findFirst().orElseGet(() -> BASICS);
    }

    public static EntryCategory[] values() {
        return (EntryCategory[]) all_categories.toArray(new EntryCategory[0]);
    }
}