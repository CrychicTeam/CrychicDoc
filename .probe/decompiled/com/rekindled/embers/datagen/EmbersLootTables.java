package com.rekindled.embers.datagen;

import java.util.List;
import java.util.Set;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class EmbersLootTables extends LootTableProvider {

    public EmbersLootTables(PackOutput output) {
        super(output, Set.of(), List.of(new LootTableProvider.SubProviderEntry(EmbersBlockLootTables::new, LootContextParamSets.BLOCK), new LootTableProvider.SubProviderEntry(EmbersEntityLootTables::new, LootContextParamSets.ENTITY)));
    }
}