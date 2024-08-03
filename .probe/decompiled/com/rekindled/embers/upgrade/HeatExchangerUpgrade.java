package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.particle.GlowParticleOptions;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public class HeatExchangerUpgrade extends DefaultUpgradeProvider {

    public static double multitplier = 0.9;

    public static double bonus = 300.0;

    public HeatExchangerUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "heat_exchanger"), tile);
    }

    @Override
    public int getPriority() {
        return -90;
    }

    @Override
    public double transformEmberProduction(BlockEntity tile, double ember, int distance, int count) {
        return ember * multitplier + bonus;
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof EmberEvent emberEvent && emberEvent.getType() == EmberEvent.EnumType.PRODUCE && tile.getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3(0.0, 1.0E-6, 0.0), 2.0F, 40), (double) this.tile.getBlockPos().m_123341_() + 0.5, (double) this.tile.getBlockPos().m_123342_() + 0.5, (double) this.tile.getBlockPos().m_123343_() + 0.5, 40, 0.12F, 0.12F, 0.12F, 0.0);
        }
    }
}