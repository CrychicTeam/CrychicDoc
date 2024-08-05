package dev.xkmc.l2weaponry.compat.aerial;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2weaponry.init.materials.LWGenItem;
import net.minecraft.world.item.Item;

public class AHCompat {

    public static final ItemEntry<Item>[][] ITEMS = LWGenItem.generate(AHToolMats.values());

    public static void register() {
    }
}