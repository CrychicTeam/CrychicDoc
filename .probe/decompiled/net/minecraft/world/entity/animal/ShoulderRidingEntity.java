package net.minecraft.world.entity.animal;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;

public abstract class ShoulderRidingEntity extends TamableAnimal {

    private static final int RIDE_COOLDOWN = 100;

    private int rideCooldownCounter;

    protected ShoulderRidingEntity(EntityType<? extends ShoulderRidingEntity> entityTypeExtendsShoulderRidingEntity0, Level level1) {
        super(entityTypeExtendsShoulderRidingEntity0, level1);
    }

    public boolean setEntityOnShoulder(ServerPlayer serverPlayer0) {
        CompoundTag $$1 = new CompoundTag();
        $$1.putString("id", this.m_20078_());
        this.m_20240_($$1);
        if (serverPlayer0.m_36360_($$1)) {
            this.m_146870_();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        this.rideCooldownCounter++;
        super.m_8119_();
    }

    public boolean canSitOnShoulder() {
        return this.rideCooldownCounter > 100;
    }
}