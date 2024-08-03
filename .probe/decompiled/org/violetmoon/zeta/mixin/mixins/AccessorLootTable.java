package org.violetmoon.zeta.mixin.mixins;

import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ LootTable.class })
public interface AccessorLootTable {

    @Accessor("pools")
    List<LootPool> zeta$getPools();
}