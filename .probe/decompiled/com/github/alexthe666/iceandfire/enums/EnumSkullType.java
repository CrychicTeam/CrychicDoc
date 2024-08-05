package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.github.alexthe666.iceandfire.item.ItemMobSkull;
import java.util.Locale;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public enum EnumSkullType {

    HIPPOGRYPH,
    CYCLOPS,
    COCKATRICE,
    STYMPHALIAN,
    TROLL,
    AMPHITHERE,
    SEASERPENT,
    HYDRA;

    public String itemResourceName = this.name().toLowerCase(Locale.ROOT) + "_skull";

    public RegistryObject<Item> skull_item;

    public static void initItems() {
        for (EnumSkullType skull : values()) {
            skull.skull_item = IafItemRegistry.registerItem(skull.itemResourceName, () -> new ItemMobSkull(skull));
        }
    }
}