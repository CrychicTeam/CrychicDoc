package net.minecraft.world.effect;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class MobEffectUtil {

    public static Component formatDuration(MobEffectInstance mobEffectInstance0, float float1) {
        if (mobEffectInstance0.isInfiniteDuration()) {
            return Component.translatable("effect.duration.infinite");
        } else {
            int $$2 = Mth.floor((float) mobEffectInstance0.getDuration() * float1);
            return Component.literal(StringUtil.formatTickDuration($$2));
        }
    }

    public static boolean hasDigSpeed(LivingEntity livingEntity0) {
        return livingEntity0.hasEffect(MobEffects.DIG_SPEED) || livingEntity0.hasEffect(MobEffects.CONDUIT_POWER);
    }

    public static int getDigSpeedAmplification(LivingEntity livingEntity0) {
        int $$1 = 0;
        int $$2 = 0;
        if (livingEntity0.hasEffect(MobEffects.DIG_SPEED)) {
            $$1 = livingEntity0.getEffect(MobEffects.DIG_SPEED).getAmplifier();
        }
        if (livingEntity0.hasEffect(MobEffects.CONDUIT_POWER)) {
            $$2 = livingEntity0.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
        }
        return Math.max($$1, $$2);
    }

    public static boolean hasWaterBreathing(LivingEntity livingEntity0) {
        return livingEntity0.hasEffect(MobEffects.WATER_BREATHING) || livingEntity0.hasEffect(MobEffects.CONDUIT_POWER);
    }

    public static List<ServerPlayer> addEffectToPlayersAround(ServerLevel serverLevel0, @Nullable Entity entity1, Vec3 vec2, double double3, MobEffectInstance mobEffectInstance4, int int5) {
        MobEffect $$6 = mobEffectInstance4.getEffect();
        List<ServerPlayer> $$7 = serverLevel0.getPlayers(p_267925_ -> p_267925_.gameMode.isSurvival() && (entity1 == null || !entity1.isAlliedTo(p_267925_)) && vec2.closerThan(p_267925_.m_20182_(), double3) && (!p_267925_.m_21023_($$6) || p_267925_.m_21124_($$6).getAmplifier() < mobEffectInstance4.getAmplifier() || p_267925_.m_21124_($$6).endsWithin(int5 - 1)));
        $$7.forEach(p_238232_ -> p_238232_.m_147207_(new MobEffectInstance(mobEffectInstance4), entity1));
        return $$7;
    }
}