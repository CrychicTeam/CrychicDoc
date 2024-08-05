package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.base.effects.EffectUtil;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;

public class TargetEffectTrait extends MobTrait {

    public final Function<Integer, MobEffectInstance> func;

    public TargetEffectTrait(Function<Integer, MobEffectInstance> func) {
        super(() -> ((MobEffectInstance) func.apply(1)).getEffect().getColor());
        this.func = func;
    }

    @Override
    public void postHurtImpl(int level, LivingEntity attacker, LivingEntity target) {
        if (CurioCompat.hasItemInCurio(target, (Item) LHItems.RING_REFLECTION.get())) {
            int radius = LHConfig.COMMON.ringOfReflectionRadius.get();
            for (Entity e : target.m_9236_().m_45933_(target, target.m_20191_().inflate((double) radius))) {
                if (e instanceof Mob) {
                    Mob mob = (Mob) e;
                    if (!(mob.m_20270_(target) > (float) radius)) {
                        EffectUtil.addEffect(mob, (MobEffectInstance) this.func.apply(level), EffectUtil.AddReason.NONE, attacker);
                    }
                }
            }
        } else {
            EffectUtil.addEffect(target, (MobEffectInstance) this.func.apply(level), EffectUtil.AddReason.NONE, attacker);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(LangData.TOOLTIP_TARGET_EFFECT.get());
        list.add(this.mapLevel(e -> {
            MobEffectInstance ins = (MobEffectInstance) this.func.apply(e);
            MutableComponent ans = Component.translatable(ins.getDescriptionId());
            MobEffect mobeffect = ins.getEffect();
            if (ins.getAmplifier() > 0) {
                ans = Component.translatable("potion.withAmplifier", ans, Component.translatable("potion.potency." + ins.getAmplifier()));
            }
            if (!ins.endsWithin(20)) {
                ans = Component.translatable("potion.withDuration", ans, MobEffectUtil.formatDuration(ins, 1.0F));
            }
            return ans.withStyle(mobeffect.getCategory().getTooltipFormatting());
        }));
    }
}