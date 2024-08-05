package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;

public class RegenTrait extends MobTrait {

    public RegenTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void tick(LivingEntity mob, int level) {
        if (!mob.m_9236_().isClientSide()) {
            if (mob.f_19797_ % 20 == 0) {
                mob.heal((float) ((double) mob.getMaxHealth() * LHConfig.COMMON.regen.get() * (double) level));
            }
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal((int) Math.round(LHConfig.COMMON.regen.get() * 100.0 * (double) i.intValue()) + "").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean allow(LivingEntity le, int difficulty, int maxModLv) {
        return this.validTarget(le) && super.allow(le, difficulty, maxModLv);
    }

    public boolean validTarget(LivingEntity le) {
        return le instanceof EnderDragon ? false : le.canBeAffected(new MobEffectInstance((MobEffect) LCEffects.CURSE.get(), 100));
    }
}