package dev.architectury.event.forge;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import dev.architectury.event.events.common.LootEvent;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

final class LootTableModificationContextImpl implements LootEvent.LootTableModificationContext {

    private final LootTable table;

    private final List<LootPool> pools;

    LootTableModificationContextImpl(LootTable table) {
        this.table = table;
        List<LootPool> pools = null;
        try {
            Field field = LootTable.class.getDeclaredField("f_79109_");
            field.setAccessible(true);
            try {
                pools = (List<LootPool>) field.get(table);
            } catch (IllegalAccessException var12) {
                throw new RuntimeException(var12);
            }
        } catch (NoSuchFieldException var14) {
            try {
                String remapped = ObfuscationReflectionHelper.remapName(Domain.FIELD, "f_79109_");
                Field field = LootTable.class.getDeclaredField(remapped);
                field.setAccessible(true);
                try {
                    pools = (List<LootPool>) field.get(table);
                } catch (IllegalAccessException var11) {
                    throw new RuntimeException(var11);
                }
            } catch (NoSuchFieldException var13) {
                for (Field field : LootTable.class.getDeclaredFields()) {
                    if (field.getType().equals(List.class)) {
                        field.setAccessible(true);
                        try {
                            pools = (List<LootPool>) field.get(table);
                        } catch (IllegalAccessException var10) {
                            throw new RuntimeException(var10);
                        }
                    }
                }
                if (pools == null) {
                    throw new RuntimeException("Unable to find pools field in LootTable!");
                }
            }
        }
        this.pools = pools;
    }

    @Override
    public void addPool(LootPool pool) {
        this.pools.add(pool);
    }

    @Override
    public void addPool(LootPool.Builder pool) {
        this.pools.add(pool.build());
    }
}