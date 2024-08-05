package dev.xkmc.l2artifacts.content.mobeffects;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class EffectDesc {

    public static MutableComponent getDesc(MobEffectInstance ins, boolean showDuration) {
        MutableComponent desc = Component.translatable(ins.getDescriptionId());
        if (ins.getAmplifier() > 0) {
            desc = Component.translatable("potion.withAmplifier", desc, Component.translatable("potion.potency." + ins.getAmplifier()));
        }
        if (showDuration && !ins.endsWithin(20)) {
            desc = Component.translatable("potion.withDuration", desc, MobEffectUtil.formatDuration(ins, 1.0F));
        }
        return desc;
    }
}