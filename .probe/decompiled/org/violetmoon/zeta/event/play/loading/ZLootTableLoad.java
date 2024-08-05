package org.violetmoon.zeta.event.play.loading;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.violetmoon.zeta.event.bus.Cancellable;
import org.violetmoon.zeta.event.bus.IZetaPlayEvent;
import org.violetmoon.zeta.mixin.mixins.AccessorLootTable;

public interface ZLootTableLoad extends IZetaPlayEvent, Cancellable {

    ResourceLocation getName();

    LootTable getTable();

    void setTable(LootTable var1);

    default void add(LootPoolEntryContainer entry) {
        LootTable table = this.getTable();
        List<LootPool> pools = ((AccessorLootTable) table).zeta$getPools();
        if (pools != null && !pools.isEmpty()) {
            LootPool firstPool = (LootPool) pools.get(0);
            LootPoolEntryContainer[] entries = firstPool.entries;
            LootPoolEntryContainer[] newEntries = new LootPoolEntryContainer[entries.length + 1];
            System.arraycopy(entries, 0, newEntries, 0, entries.length);
            newEntries[entries.length] = entry;
            firstPool.entries = newEntries;
        }
    }
}