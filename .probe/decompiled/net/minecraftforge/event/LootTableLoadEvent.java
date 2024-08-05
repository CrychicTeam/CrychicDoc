package net.minecraftforge.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class LootTableLoadEvent extends Event {

    private final ResourceLocation name;

    private LootTable table;

    public LootTableLoadEvent(ResourceLocation name, LootTable table) {
        this.name = name;
        this.table = table;
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public LootTable getTable() {
        return this.table;
    }

    public void setTable(LootTable table) {
        this.table = table;
    }
}