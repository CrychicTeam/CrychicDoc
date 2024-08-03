package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.util.EmberInventoryUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IntelligentApparatusAugment extends AugmentBase {

    public IntelligentApparatusAugment(ResourceLocation id) {
        super(id, 4.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onXPDrop(LivingExperienceDropEvent event) {
        if (event.getAttackingPlayer() != null) {
            Player player = event.getAttackingPlayer();
            int level = AugmentUtil.getArmorAugmentLevel(player, this);
            if (level > 0 && EmberInventoryUtil.getEmberTotal(player) >= this.cost) {
                EmberInventoryUtil.removeEmber(player, this.cost);
                event.setDroppedExperience(event.getDroppedExperience() * (level + 1));
            }
        }
    }
}