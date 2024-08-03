package net.minecraft.world.entity.boss.enderdragon.phases;

import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;

public class DragonHoverPhase extends AbstractDragonPhaseInstance {

    @Nullable
    private Vec3 targetLocation;

    public DragonHoverPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doServerTick() {
        if (this.targetLocation == null) {
            this.targetLocation = this.f_31176_.m_20182_();
        }
    }

    @Override
    public boolean isSitting() {
        return true;
    }

    @Override
    public void begin() {
        this.targetLocation = null;
    }

    @Override
    public float getFlySpeed() {
        return 1.0F;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonHoverPhase> getPhase() {
        return EnderDragonPhase.HOVERING;
    }
}