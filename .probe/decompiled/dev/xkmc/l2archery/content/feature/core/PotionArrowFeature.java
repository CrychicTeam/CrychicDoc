package dev.xkmc.l2archery.content.feature.core;

import dev.xkmc.l2archery.content.config.BowArrowStatConfig;
import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public record PotionArrowFeature(List<MobEffectInstance> instances) implements OnHitFeature {

    public static final PotionArrowFeature NULL = new PotionArrowFeature(List.of());

    public static BowArrowFeature fromUpgradeConfig(Upgrade upgrade) {
        return BowArrowStatConfig.get().getUpgradeEffects(upgrade);
    }

    @Override
    public void onHitLivingEntity(GenericArrowEntity arrow, LivingEntity target, EntityHitResult hit) {
    }

    @Override
    public void onHitBlock(GenericArrowEntity arrow, BlockHitResult result) {
    }

    @Override
    public void postHurtEntity(GenericArrowEntity arrow, LivingEntity target) {
        for (MobEffectInstance instance : this.instances) {
            EffectUtil.addEffect(target, instance, EffectUtil.AddReason.PROF, arrow.m_19749_());
        }
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
    }

    public static void addTooltip(List<MobEffectInstance> instances, List<Component> list) {
        if (instances.size() > 5) {
            list.add(LangData.STAT_EFFECT_TOO_MANY.get(instances.size()));
        } else {
            if (instances.size() > 0) {
                list.add(LangData.STAT_EFFECT.get());
            }
            for (MobEffectInstance eff : instances) {
                list.add(getTooltip(eff));
            }
        }
    }

    public static MutableComponent getTooltip(MobEffectInstance eff) {
        MutableComponent comp = Component.translatable(eff.getDescriptionId());
        MobEffect mobeffect = eff.getEffect();
        if (eff.getAmplifier() > 0) {
            comp = Component.translatable("potion.withAmplifier", comp, Component.translatable("potion.potency." + eff.getAmplifier()));
        }
        if (eff.getDuration() > 20) {
            comp = Component.translatable("potion.withDuration", comp, MobEffectUtil.formatDuration(eff, 1.0F));
        }
        return comp.withStyle(mobeffect.getCategory().getTooltipFormatting());
    }

    @Override
    public boolean allowDuplicate() {
        return true;
    }
}