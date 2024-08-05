package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.AugmentUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EldritchInsigniaAugment extends AugmentBase {

    public EldritchInsigniaAugment(ResourceLocation id) {
        super(id, 0.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityTarget(LivingChangeTargetEvent event) {
        if (event.getNewTarget() instanceof Player player) {
            int level = AugmentUtil.getArmorAugmentLevel(player, this);
            if ((event.getEntity().getLastDamageSource() == null || event.getEntity().getLastDamageSource().getEntity() == null || event.getEntity().getLastDamageSource().getEntity().getUUID().compareTo(event.getNewTarget().m_20148_()) != 0) && event.getEntity().m_19879_() % (3 + level) >= 2 && level > 0 && !(event.getEntity() instanceof Player)) {
                event.setCanceled(true);
            }
        }
    }
}