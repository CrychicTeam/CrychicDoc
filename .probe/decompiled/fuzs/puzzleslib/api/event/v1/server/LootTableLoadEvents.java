package fuzs.puzzleslib.api.event.v1.server;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

public final class LootTableLoadEvents {

    public static final EventInvoker<LootTableLoadEvents.Replace> REPLACE = EventInvoker.lookup(LootTableLoadEvents.Replace.class);

    public static final EventInvoker<LootTableLoadEvents.Modify> MODIFY = EventInvoker.lookup(LootTableLoadEvents.Modify.class);

    private LootTableLoadEvents() {
    }

    @FunctionalInterface
    public interface Modify {

        void onModifyLootTable(LootDataManager var1, ResourceLocation var2, Consumer<LootPool> var3, IntPredicate var4);
    }

    @FunctionalInterface
    public interface Replace {

        void onReplaceLootTable(ResourceLocation var1, MutableValue<LootTable> var2);
    }
}