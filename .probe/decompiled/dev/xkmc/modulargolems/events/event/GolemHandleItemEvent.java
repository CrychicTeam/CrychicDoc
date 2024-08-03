package dev.xkmc.modulargolems.events.event;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import net.minecraft.world.entity.item.ItemEntity;

public class GolemHandleItemEvent extends GolemEvent {

    private final ItemEntity item;

    public GolemHandleItemEvent(AbstractGolemEntity<?, ?> golem, ItemEntity item) {
        super(golem);
        this.item = item;
    }

    public ItemEntity getItem() {
        return this.item;
    }
}