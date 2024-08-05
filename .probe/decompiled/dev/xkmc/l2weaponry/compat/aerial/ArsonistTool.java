package dev.xkmc.l2weaponry.compat.aerial;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import dev.xkmc.l2weaponry.init.data.LangData;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ArsonistTool extends ExtraToolConfig implements LWExtraConfig {

    @Nullable
    @Override
    public DamageSource getReflectSource(Player player) {
        return player.m_9236_().damageSources().thorns(player);
    }

    @Override
    public double onShieldReflect(ItemStack stack, LivingEntity user, LivingEntity entity, double original, double reflect) {
        if (reflect > 0.0 && entity.m_9236_() instanceof ServerLevel sl) {
            entity.m_20256_(entity.m_20184_().add(0.0, 1.0, 0.0));
            sl.sendParticles(ParticleTypes.EXPLOSION, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), 1, 0.0, 0.0, 0.0, 1.0);
            sl.m_247517_(null, entity.m_20183_(), SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS);
        }
        if (!entity.m_5825_()) {
            reflect += 5.0;
            entity.m_9236_().m_46796_(1009, entity.m_20183_(), 0);
        }
        return reflect;
    }

    public void onDamage(AttackCache cache, ItemStack stack) {
        if (!cache.getAttackTarget().m_5825_() && !(stack.getItem() instanceof BaseShieldItem)) {
            cache.getAttackTarget().m_20254_(LWConfig.COMMON.fieryDuration.get());
        }
    }

    public void addTooltip(ItemStack stack, List<Component> list) {
        if (stack.getItem() instanceof BaseShieldItem) {
            list.add(LangData.MATS_AH_ARSON_SHIELD.get(5));
        } else {
            list.add(LangData.MATS_AH_ARSON.get(LWConfig.COMMON.fieryDuration.get()));
        }
    }
}