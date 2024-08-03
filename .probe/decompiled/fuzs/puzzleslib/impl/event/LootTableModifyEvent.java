package fuzs.puzzleslib.impl.event;

import fuzs.puzzleslib.mixin.accessor.LootTableForgeAccessor;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.eventbus.api.Event;

public class LootTableModifyEvent extends Event {

    private final LootDataManager lootDataManager;

    private final ResourceLocation identifier;

    private final LootTable lootTable;

    public LootTableModifyEvent(LootDataManager lootDataManager, ResourceLocation identifier, LootTable lootTable) {
        this.lootDataManager = lootDataManager;
        this.identifier = identifier;
        this.lootTable = lootTable;
    }

    public LootDataManager getLootDataManager() {
        return this.lootDataManager;
    }

    public ResourceLocation getIdentifier() {
        return this.identifier;
    }

    public void addPool(LootPool pool) {
        ((LootTableForgeAccessor) this.lootTable).puzzleslib$getPools().add(pool);
    }

    public boolean removePool(int index) {
        List<LootPool> pools = ((LootTableForgeAccessor) this.lootTable).puzzleslib$getPools();
        if (index >= 0 && index < pools.size()) {
            pools.remove(index);
            return true;
        } else {
            return false;
        }
    }
}