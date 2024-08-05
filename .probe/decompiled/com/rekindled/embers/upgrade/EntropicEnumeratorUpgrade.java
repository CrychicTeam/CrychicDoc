package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.event.AlchemyResultEvent;
import com.rekindled.embers.api.event.AlchemyStartEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.EntropicEnumeratorBlockEntity;
import com.rekindled.embers.util.Misc;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EntropicEnumeratorUpgrade extends DefaultUpgradeProvider {

    public EntropicEnumeratorUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "entropic_enumerator"), tile);
    }

    @Override
    public int getPriority() {
        return -90;
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (event instanceof AlchemyStartEvent alchemyEvent && alchemyEvent.getRecipe() != null && this.tile instanceof EntropicEnumeratorBlockEntity enumerator && alchemyEvent.getRecipe().getResult(alchemyEvent.context).blackPins != alchemyEvent.getRecipe().getInputs().size()) {
            enumerator.solve(false, 390);
        }
        if (event instanceof AlchemyResultEvent alchemyEvent && this.tile instanceof EntropicEnumeratorBlockEntity enumerator) {
            int blackPins = alchemyEvent.getResult().blackPins;
            int requirement = alchemyEvent.getRecipe().getInputs().size();
            if (blackPins != requirement) {
                if (((UpgradeContext) upgrades.get(0)).upgrade() == this) {
                    if (Misc.random.nextFloat((float) (count + 3)) > 3.0F) {
                        alchemyEvent.setFailure(true);
                    } else {
                        int bonusWhite = Math.min(alchemyEvent.getResult().whitePins, count + 1);
                        int bonusNothing = count / 2;
                        if (alchemyEvent.isFailure() && requirement <= blackPins + bonusWhite + bonusNothing) {
                            alchemyEvent.setFailure(false);
                        }
                    }
                }
                enumerator.restartScramble(Misc.random.nextInt(EntropicEnumeratorBlockEntity.queueTime));
            }
        }
    }
}