package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.EmberInventoryUtil;
import com.rekindled.embers.util.Misc;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class SuperheaterAugment extends AugmentBase {

    public SuperheaterAugment(ResourceLocation id) {
        super(id, 2.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private double getBurnBonus(double resonance) {
        return resonance > 1.0 ? 1.0 + (resonance - 1.0) * 0.5 : resonance;
    }

    private double getDamageBonus(double resonance) {
        return resonance > 1.0 ? 1.0 + (resonance - 1.0) * 1.0 : resonance;
    }

    @SubscribeEvent
    public void onHit(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack s = player.m_21205_();
            if (AugmentUtil.hasHeat(s)) {
                int level = AugmentUtil.getAugmentLevel(s, this);
                if (level > 0 && EmberInventoryUtil.getEmberTotal(player) >= this.cost) {
                    double resonance = Misc.getEmberResonance(s);
                    int burnTime = (int) (Math.pow(2.0, (double) (level - 1)) * 5.0 * this.getBurnBonus(resonance));
                    float extraDamage = (float) ((double) level * this.getDamageBonus(resonance));
                    if (event.getEntity().m_20094_() < burnTime) {
                        event.getEntity().m_7311_(burnTime);
                    }
                    if (event.getEntity().m_9236_() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(GlowParticleOptions.EMBER, event.getEntity().m_20185_(), event.getEntity().m_20186_() + (double) event.getEntity().m_20192_() / 1.5, event.getEntity().m_20189_(), 30, 0.15, 0.15, 0.15, 0.3);
                    }
                    EmberInventoryUtil.removeEmber(player, this.cost);
                    event.setAmount(event.getAmount() + extraDamage);
                }
            }
        }
    }
}