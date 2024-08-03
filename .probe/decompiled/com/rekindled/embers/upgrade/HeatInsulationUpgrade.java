package com.rekindled.embers.upgrade;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.api.event.HeatCoilVisualEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.HearthCoilBlockEntity;
import com.rekindled.embers.util.Misc;
import java.awt.Color;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;

public class HeatInsulationUpgrade extends DefaultUpgradeProvider {

    public HeatInsulationUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "heat_insulation"), tile);
    }

    @Override
    public int getPriority() {
        return -90;
    }

    @Override
    public int getLimit(BlockEntity tile) {
        return tile instanceof HearthCoilBlockEntity ? Integer.MAX_VALUE : 0;
    }

    @Override
    public double getOtherParameter(BlockEntity tile, String type, double value, int distance, int count) {
        if (distance == 0) {
            distance = 1;
        }
        distance *= distance;
        if (type.equals("max_heat")) {
            return value + 75.0 / (double) distance;
        } else {
            return type.equals("cooling_speed") ? value * (1.0 - 0.3 / (double) distance) : value;
        }
    }

    @Override
    public double transformEmberConsumption(BlockEntity tile, double ember, int distance, int count) {
        if (distance == 0) {
            distance = 1;
        }
        distance *= distance;
        return ember * (1.0 - 0.2 / (double) distance);
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof HeatCoilVisualEvent visualEvent && tile instanceof HearthCoilBlockEntity) {
            double heat = ((HearthCoilBlockEntity) tile).heat;
            double overheat = heat - ConfigManager.HEARTH_COIL_MAX_HEAT.get();
            visualEvent.setColor(Misc.lerpColor(visualEvent.getColor(), new Color(255, 192, 64), Mth.clamp(overheat / 300.0, 0.0, 1.0)));
            visualEvent.setVerticalSpeed((float) Mth.clampedLerp((double) visualEvent.getVerticalSpeed(), Math.max((double) visualEvent.getVerticalSpeed(), 0.9), overheat / 300.0));
        }
    }
}