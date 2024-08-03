package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class LivingPackSizeEvent extends LivingEvent {

    private int maxPackSize;

    public LivingPackSizeEvent(Mob entity) {
        super(entity);
    }

    public int getMaxPackSize() {
        return this.maxPackSize;
    }

    public void setMaxPackSize(int maxPackSize) {
        this.maxPackSize = maxPackSize;
    }
}