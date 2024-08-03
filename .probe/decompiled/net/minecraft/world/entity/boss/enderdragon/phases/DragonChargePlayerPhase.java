package net.minecraft.world.entity.boss.enderdragon.phases;

import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class DragonChargePlayerPhase extends AbstractDragonPhaseInstance {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int CHARGE_RECOVERY_TIME = 10;

    @Nullable
    private Vec3 targetLocation;

    private int timeSinceCharge;

    public DragonChargePlayerPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
    }

    @Override
    public void doServerTick() {
        if (this.targetLocation == null) {
            LOGGER.warn("Aborting charge player as no target was set.");
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
        } else if (this.timeSinceCharge > 0 && this.timeSinceCharge++ >= 10) {
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.HOLDING_PATTERN);
        } else {
            double $$0 = this.targetLocation.distanceToSqr(this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
            if ($$0 < 100.0 || $$0 > 22500.0 || this.f_31176_.f_19862_ || this.f_31176_.f_19863_) {
                this.timeSinceCharge++;
            }
        }
    }

    @Override
    public void begin() {
        this.targetLocation = null;
        this.timeSinceCharge = 0;
    }

    public void setTarget(Vec3 vec0) {
        this.targetLocation = vec0;
    }

    @Override
    public float getFlySpeed() {
        return 3.0F;
    }

    @Nullable
    @Override
    public Vec3 getFlyTargetLocation() {
        return this.targetLocation;
    }

    @Override
    public EnderDragonPhase<DragonChargePlayerPhase> getPhase() {
        return EnderDragonPhase.CHARGING_PLAYER;
    }
}