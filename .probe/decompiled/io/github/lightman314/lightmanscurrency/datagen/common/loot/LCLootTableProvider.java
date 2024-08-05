package io.github.lightman314.lightmanscurrency.datagen.common.loot;

import io.github.lightman314.lightmanscurrency.common.loot.LCLootTables;
import io.github.lightman314.lightmanscurrency.common.loot.LootManager;
import io.github.lightman314.lightmanscurrency.datagen.common.loot.packs.BlockDropLoot;
import io.github.lightman314.lightmanscurrency.datagen.common.loot.packs.ChestAddonLoot;
import io.github.lightman314.lightmanscurrency.datagen.common.loot.packs.EntityAddonLoot;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

public class LCLootTableProvider {

    public static LootTableProvider create(@Nonnull PackOutput output) {
        return new LootTableProvider(output, LCLootTables.all(), List.of(new LootTableProvider.SubProviderEntry(EntityAddonLoot::new, LootManager.ENTITY_PARAMS), new LootTableProvider.SubProviderEntry(ChestAddonLoot::new, LootContextParamSets.EMPTY), new LootTableProvider.SubProviderEntry(BlockDropLoot::new, LootContextParamSets.BLOCK)));
    }
}