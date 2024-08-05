package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.MiniBoilerBlockEntity;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class MiniBoilerUpgrade extends DefaultUpgradeProvider {

    public MiniBoilerUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "mini_boiler"), tile);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (this.tile instanceof MiniBoilerBlockEntity && event instanceof EmberEvent emberEvent && emberEvent.getType() != EmberEvent.EnumType.TRANSFER) {
            double multiplier = 1.0;
            if (distance > 1) {
                multiplier /= (double) distance * 0.75;
            }
            if (count > 3) {
                multiplier /= ((double) count - 2.0) * 0.75;
            }
            if (emberEvent.getType() == EmberEvent.EnumType.PRODUCE) {
                multiplier *= 0.25;
            }
            ((MiniBoilerBlockEntity) this.tile).boil(emberEvent.getAmount() * multiplier);
        }
    }
}