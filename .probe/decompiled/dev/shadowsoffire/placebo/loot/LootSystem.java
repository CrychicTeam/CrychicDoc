package dev.shadowsoffire.placebo.loot;

import dev.shadowsoffire.placebo.Placebo;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraftforge.registries.ForgeRegistries;

public class LootSystem {

    public static final Map<LootDataId<LootTable>, LootTable> PLACEBO_TABLES = new HashMap();

    public static void registerLootTable(ResourceLocation key, LootTable table) {
        LootDataId<LootTable> trueKey = new LootDataId<>(LootDataType.TABLE, key);
        if (!PLACEBO_TABLES.containsKey(trueKey)) {
            table.setLootTableId(key);
            PLACEBO_TABLES.put(trueKey, table);
        } else {
            Placebo.LOGGER.warn("Duplicate loot entry detected, this is not allowed!  Key: " + key);
        }
    }

    public static LootTable.Builder tableBuilder() {
        return new LootTable.Builder();
    }

    public static PoolBuilder poolBuilder(int minRolls, int maxRolls) {
        return new PoolBuilder(minRolls, maxRolls);
    }

    public static void defaultBlockTable(Block b) {
        LootTable.Builder builder = tableBuilder();
        builder.withPool(poolBuilder(1, 1).addEntries(new StackLootEntry(new ItemStack(b))).m_79080_(ExplosionCondition.survivesExplosion()));
        registerLootTable(new ResourceLocation(ForgeRegistries.BLOCKS.getKey(b).getNamespace(), "blocks/" + ForgeRegistries.BLOCKS.getKey(b).getPath()), builder.build());
    }
}