package com.github.alexmodguy.alexscaves.server.potion;

import com.github.alexmodguy.alexscaves.server.entity.util.DarknessIncarnateUserAccessor;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;

public class DarknessIncarnateEffect extends MobEffect {

    private int lastDuration = -1;

    private int firstDuration = -1;

    protected DarknessIncarnateEffect() {
        super(MobEffectCategory.BENEFICIAL, 5312014);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        this.toggleFlight(entity, true);
        if (entity.m_20096_()) {
            entity.m_20256_(entity.m_20184_().add(0.0, 0.1, 0.0));
        }
        if ((entity.f_19797_ + entity.m_19879_() * 5) % 50 == 0 && entity.getRandom().nextInt(2) == 0) {
            entity.m_216990_(ACSoundRegistry.DARKNESS_INCARNATE_IDLE.get());
        }
    }

    public List<ItemStack> getCurativeItems() {
        return List.of();
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap attributeMap, int i) {
        this.lastDuration = -1;
        this.firstDuration = -1;
        super.removeAttributeModifiers(living, attributeMap, i);
        this.toggleFlight(living, false);
    }

    public int getActiveTime() {
        return this.firstDuration - this.lastDuration;
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        this.lastDuration = -1;
        this.firstDuration = -1;
        super.addAttributeModifiers(entity, map, i);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        this.lastDuration = duration;
        if (duration <= 0) {
            this.lastDuration = -1;
            this.firstDuration = -1;
        }
        if (this.firstDuration == -1) {
            this.firstDuration = duration;
        }
        return duration > 0;
    }

    public void toggleFlight(LivingEntity living, boolean flight) {
        if (!living.m_9236_().isClientSide && living instanceof ServerPlayer player) {
            boolean prevFlying = player.m_150110_().flying;
            boolean trueFlight = this.isCreativePlayer(living) || flight;
            player.m_150110_().mayfly = trueFlight;
            player.m_150110_().flying = trueFlight;
            float defaultFlightSpeed = 0.05F;
            if (flight) {
                player.m_150110_().setFlyingSpeed(defaultFlightSpeed * 4.0F);
            } else {
                player.m_150110_().setFlyingSpeed(defaultFlightSpeed);
                if (!player.isSpectator()) {
                    player.m_150110_().flying = false;
                    if (!player.isCreative()) {
                        player.m_150110_().mayfly = false;
                    }
                    if (player instanceof DarknessIncarnateUserAccessor darknessIncarnateUserAccessor) {
                        darknessIncarnateUserAccessor.setSlowFallingFlag(true);
                    }
                }
            }
            if (prevFlying != flight) {
                player.onUpdateAbilities();
            }
        }
        living.f_19789_ = 0.0F;
    }

    public static float getIntensity(LivingEntity player, float partialTicks, float scaleBy) {
        MobEffectInstance instance = player.getEffect(ACEffectRegistry.DARKNESS_INCARNATE.get());
        if (instance == null) {
            return 0.0F;
        } else if (instance.isInfiniteDuration()) {
            return scaleBy;
        } else {
            DarknessIncarnateEffect effect = (DarknessIncarnateEffect) instance.getEffect();
            float j = (float) effect.getActiveTime() + partialTicks;
            int duration = instance.getDuration();
            return Math.min(scaleBy, Math.min(j, (float) duration + partialTicks)) / scaleBy;
        }
    }

    public static boolean isInLight(LivingEntity living, int threshold) {
        BlockPos samplePos = living.m_20201_().blockPosition();
        int lightLevel = living.m_9236_().m_45517_(LightLayer.BLOCK, samplePos);
        float timeOfDay = living.m_9236_().m_46942_(1.0F);
        if (living.m_9236_().m_45527_(samplePos) && ((double) timeOfDay < 0.259 || (double) timeOfDay > 0.74)) {
            lightLevel = 15;
        }
        return lightLevel >= threshold;
    }

    private boolean isCreativePlayer(LivingEntity living) {
        if (living instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return true;
        }
        return false;
    }
}