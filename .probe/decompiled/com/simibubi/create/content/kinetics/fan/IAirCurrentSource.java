package com.simibubi.create.content.kinetics.fan;

import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CKinetics;
import javax.annotation.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;

@MethodsReturnNonnullByDefault
public interface IAirCurrentSource {

    @Nullable
    AirCurrent getAirCurrent();

    @Nullable
    Level getAirCurrentWorld();

    BlockPos getAirCurrentPos();

    float getSpeed();

    Direction getAirflowOriginSide();

    @Nullable
    Direction getAirFlowDirection();

    default float getMaxDistance() {
        float speed = Math.abs(this.getSpeed());
        CKinetics config = AllConfigs.server().kinetics;
        float distanceFactor = Math.min(speed / (float) config.fanRotationArgmax.get().intValue(), 1.0F);
        float pushDistance = Mth.lerp(distanceFactor, 3.0F, (float) config.fanPushDistance.get().intValue());
        float pullDistance = Mth.lerp(distanceFactor, 3.0F, (float) config.fanPullDistance.get().intValue());
        return this.getSpeed() > 0.0F ? pushDistance : pullDistance;
    }

    boolean isSourceRemoved();
}