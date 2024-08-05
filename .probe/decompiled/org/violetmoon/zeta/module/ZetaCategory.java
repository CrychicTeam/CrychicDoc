package org.violetmoon.zeta.module;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;

public class ZetaCategory {

    public final String name;

    public final Supplier<ItemStack> icon;

    @Nullable
    public final String requiredMod;

    public ZetaCategory(String name, Supplier<ItemStack> icon, @Nullable String requiredMod) {
        this.name = name;
        this.icon = icon;
        this.requiredMod = requiredMod;
    }

    public ZetaCategory(String name, Item icon, @Nullable String requiredMod) {
        this(name, () -> new ItemStack(icon), requiredMod);
    }

    public ZetaCategory(String name, Item icon) {
        this(name, icon, null);
    }

    public static ZetaCategory unknownCategory(String id) {
        return new ZetaCategory(id, () -> new ItemStack(Items.PAPER), null);
    }

    public boolean isAddon() {
        return this.requiredMod != null && !this.requiredMod.isEmpty();
    }

    public boolean requiredModsLoaded(Zeta z) {
        return !this.isAddon() || z.isModLoaded(this.requiredMod);
    }

    public String toString() {
        return "ZetaCategory{" + this.name + "}";
    }
}