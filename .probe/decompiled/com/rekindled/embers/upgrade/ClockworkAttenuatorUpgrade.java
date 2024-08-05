package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.ClockworkAttenuatorBlockEntity;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ClockworkAttenuatorUpgrade extends DefaultUpgradeProvider {

    public ClockworkAttenuatorUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "clockwork_attenuator"), tile);
    }

    @Override
    public int getPriority() {
        return -100;
    }

    @Override
    public int getLimit(BlockEntity tile) {
        return 1;
    }

    @Override
    public double getSpeed(BlockEntity tile, double speed, int distance, int count) {
        return speed * this.getSpeedModifier();
    }

    private double getSpeedModifier() {
        return this.tile instanceof ClockworkAttenuatorBlockEntity ? ((ClockworkAttenuatorBlockEntity) this.tile).getSpeed() : 0.0;
    }

    @Override
    public boolean doWork(BlockEntity tile, List<UpgradeContext> upgrades, int distance, int count) {
        return this.getSpeedModifier() == 0.0;
    }
}