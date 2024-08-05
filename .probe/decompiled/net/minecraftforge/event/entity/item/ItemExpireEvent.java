package net.minecraftforge.event.entity.item;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ItemExpireEvent extends ItemEvent {

    private int extraLife;

    public ItemExpireEvent(ItemEntity entityItem, int extraLife) {
        super(entityItem);
        this.setExtraLife(extraLife);
    }

    public int getExtraLife() {
        return this.extraLife;
    }

    public void setExtraLife(int extraLife) {
        this.extraLife = extraLife;
    }
}