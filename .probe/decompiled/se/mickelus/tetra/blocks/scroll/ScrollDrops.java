package se.mickelus.tetra.blocks.scroll;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ScrollDrops {

    Map<ResourceLocation, ResourceLocation> basicExtensions = new HashMap();

    public ScrollDrops() {
        this.basicExtensions.put(BuiltInLootTables.BASTION_BRIDGE, new ResourceLocation("tetra", "bastion_scrolls"));
        this.basicExtensions.put(BuiltInLootTables.BASTION_HOGLIN_STABLE, new ResourceLocation("tetra", "bastion_scrolls"));
        this.basicExtensions.put(BuiltInLootTables.BASTION_OTHER, new ResourceLocation("tetra", "bastion_scrolls"));
        this.basicExtensions.put(BuiltInLootTables.BASTION_TREASURE, new ResourceLocation("tetra", "bastion_scrolls"));
        this.basicExtensions.put(BuiltInLootTables.NETHER_BRIDGE, new ResourceLocation("tetra", "chests/nether_bridge_extended"));
        this.basicExtensions.put(BuiltInLootTables.SIMPLE_DUNGEON, new ResourceLocation("tetra", "chests/simple_dungeon_extended"));
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if (this.basicExtensions.containsKey(event.getName())) {
            event.getTable().addPool(LootPool.lootPool().name("tetra:" + event.getName().getPath() + "_extended").add(LootTableReference.lootTableReference((ResourceLocation) this.basicExtensions.get(event.getName()))).build());
        }
    }
}