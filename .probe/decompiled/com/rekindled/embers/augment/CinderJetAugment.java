package com.rekindled.embers.augment;

import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.util.EmberInventoryUtil;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CinderJetAugment extends AugmentBase {

    public static Map<UUID, Boolean> sprintingClient = new HashMap();

    public static Map<UUID, Boolean> sprintingServer = new HashMap();

    public CinderJetAugment(ResourceLocation id) {
        super(id, 2.0);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Map<UUID, Boolean> getSprinting(Level level) {
        return level.isClientSide() ? sprintingClient : sprintingServer;
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && (!entity.m_9236_().isClientSide() || entity == Minecraft.getInstance().player)) {
            UUID id = entity.m_20148_();
            if (getSprinting(entity.m_9236_()).containsKey(id)) {
                if (entity.m_20142_() && !(Boolean) getSprinting(entity.m_9236_()).get(id)) {
                    int level = AugmentUtil.getArmorAugmentLevel(player, this);
                    float dashStrength = (float) (2.0 * (Math.atan(0.6 * (double) level) / 1.25));
                    if (dashStrength > 0.0F && entity.m_20096_() && EmberInventoryUtil.getEmberTotal(player) > this.cost) {
                        EmberInventoryUtil.removeEmber(player, this.cost);
                        entity.m_20256_(entity.m_20184_().add(new Vec3(2.0 * entity.m_20154_().x * (double) dashStrength, 0.4, 2.0 * entity.m_20154_().z * (double) dashStrength)));
                        entity.m_9236_().playSound(player, entity, EmbersSounds.CINDER_JET.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        if (entity.m_9236_() instanceof ServerLevel serverLevel) {
                            serverLevel.sendParticles(SmokeParticleOptions.BIG_SMOKE, entity.m_20185_() - entity.m_20154_().x * 0.5, entity.m_20186_() + (double) (entity.m_20206_() / 4.0F), entity.m_20189_() - (double) ((float) entity.m_20154_().z * 0.5F), 40, 0.1, 0.1, 0.1, 1.0);
                        }
                    }
                }
                getSprinting(entity.m_9236_()).replace(id, entity.m_20142_());
            } else {
                getSprinting(entity.m_9236_()).put(id, entity.m_20142_());
            }
        }
    }
}