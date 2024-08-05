package dev.xkmc.l2weaponry.compat.dragons;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2weaponry.init.materials.LWGenItem;
import net.minecraft.world.item.Item;

public class DragonCompat {

    public static final ItemEntry<Item>[][] ITEMS = LWGenItem.generate(DragonToolMats.values());

    public static void register() {
    }
}