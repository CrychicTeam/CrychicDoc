package dev.xkmc.l2archery.content.feature.bow;

import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.feature.types.OnPullFeature;
import dev.xkmc.l2archery.content.item.GenericBowItem;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.l2library.util.code.GenericItemStack;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public record PullEffectFeature(List<Supplier<MobEffectInstance>> effects) implements OnPullFeature {

    @Override
    public void onPull(LivingEntity player, GenericItemStack<GenericBowItem> bow) {
        if (player instanceof ServerPlayer) {
            for (Supplier<MobEffectInstance> eff : this.effects) {
                EffectUtil.addEffect(player, (MobEffectInstance) eff.get(), EffectUtil.AddReason.SELF, player);
            }
        }
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_PULL_EFFECT.get());
        for (Supplier<MobEffectInstance> eff : this.effects) {
            list.add(PotionArrowFeature.getTooltip((MobEffectInstance) eff.get()));
        }
    }
}