package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.event.HeatCoilVisualEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.HearthCoilBlockEntity;
import com.rekindled.embers.particle.SmokeParticleOptions;
import java.util.List;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CharInstillerUpgrade extends DefaultUpgradeProvider {

    protected static Random random = new Random();

    public CharInstillerUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "char_instiller"), tile);
    }

    @Override
    public int getPriority() {
        return -80;
    }

    @Override
    public int getLimit(BlockEntity tile) {
        return tile instanceof HearthCoilBlockEntity ? 1 : 0;
    }

    @Override
    public double getSpeed(BlockEntity tile, double speed, int distance, int count) {
        return 2.0;
    }

    @Override
    public <T> T getOtherParameter(BlockEntity tile, String type, T value, int distance, int count) {
        return (T) (type.equals("recipe_type") && value instanceof RecipeType ? RecipeType.SMOKING : value);
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof HeatCoilVisualEvent visualEvent) {
            for (int i = 0; i < visualEvent.getParticles() / 3; i++) {
                tile.getLevel().addParticle(SmokeParticleOptions.BIG_SMOKE, (double) ((float) tile.getBlockPos().m_123341_() - 0.2F + random.nextFloat() * 1.4F), (double) ((float) tile.getBlockPos().m_123342_() + 1.275F), (double) ((float) tile.getBlockPos().m_123343_() - 0.2F + random.nextFloat() * 1.4F), (Math.random() * 2.0 - 1.0) * 0.2, (double) (random.nextFloat() * 1000.0F), (Math.random() * 2.0 - 1.0) * 0.2);
            }
            visualEvent.setParticles(visualEvent.getParticles() / 2);
        }
    }
}