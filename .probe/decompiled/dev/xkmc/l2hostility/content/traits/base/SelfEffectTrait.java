package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public class SelfEffectTrait extends MobTrait {

    public final Supplier<MobEffect> effect;

    public SelfEffectTrait(Supplier<MobEffect> effect) {
        super(() -> ((MobEffect) effect.get()).getColor());
        this.effect = effect;
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (!mob.m_9236_().isClientSide()) {
            EffectUtil.refreshEffect(mob, new MobEffectInstance((MobEffect) this.effect.get(), 200, level - 1), EffectUtil.AddReason.FORCE, mob);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(LangData.TOOLTIP_SELF_EFFECT.get());
        ChatFormatting color = ((MobEffect) this.effect.get()).getCategory().getTooltipFormatting();
        if (this.getMaxLevel() == 1) {
            list.add(((MobEffect) this.effect.get()).getDisplayName().copy().withStyle(color));
        } else {
            list.add(this.mapLevel(e -> Component.translatable("potion.withAmplifier", ((MobEffect) this.effect.get()).getDisplayName(), Component.translatable("potion.potency." + (e - 1))).withStyle(color)));
        }
    }
}