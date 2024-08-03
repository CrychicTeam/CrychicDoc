package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

public interface LootEvent {

    Event<LootEvent.ModifyLootTable> MODIFY_LOOT_TABLE = EventFactory.createLoop();

    @NonExtendable
    public interface LootTableModificationContext {

        void addPool(LootPool var1);

        default void addPool(LootPool.Builder pool) {
            this.addPool(pool.build());
        }
    }

    @FunctionalInterface
    public interface ModifyLootTable {

        void modifyLootTable(@Nullable LootDataManager var1, ResourceLocation var2, LootEvent.LootTableModificationContext var3, boolean var4);
    }
}