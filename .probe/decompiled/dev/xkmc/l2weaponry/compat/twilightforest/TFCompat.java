package dev.xkmc.l2weaponry.compat.twilightforest;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2weaponry.init.materials.LWGenItem;
import net.minecraft.world.item.Item;

public class TFCompat {

    public static final ItemEntry<Item>[][] ITEMS = LWGenItem.generate(TFToolMats.values());

    public static void register() {
    }
}