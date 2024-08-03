package top.theillusivec4.curios.api;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public record SlotContext(String identifier, LivingEntity entity, int index, boolean cosmetic, boolean visible) {

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    public String getIdentifier() {
        return this.identifier;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    public int getIndex() {
        return this.index;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    public LivingEntity getWearer() {
        return this.entity;
    }
}