package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.event.HeatCoilVisualEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.HearthCoilBlockEntity;
import java.awt.Color;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AtmosphericBellowsUpgrade extends DefaultUpgradeProvider {

    public AtmosphericBellowsUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "atmospheric_bellows"), tile);
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
        return (T) (type.equals("recipe_type") && value instanceof RecipeType ? RecipeType.BLASTING : value);
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof HeatCoilVisualEvent visualEvent) {
            Color color = visualEvent.getColor();
            visualEvent.setColor(new Color(color.getBlue(), color.getGreen(), color.getRed()));
            visualEvent.setParticles((int) ((double) visualEvent.getParticles() * 1.5));
        }
    }
}