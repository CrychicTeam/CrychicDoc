package dev.xkmc.modulargolems.content.modifier.base;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import java.util.function.Function;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PotionAttackModifier extends GolemModifier {

    private final Function<Integer, MobEffectInstance> func;

    public PotionAttackModifier(StatFilterType type, int maxLevel, Function<Integer, MobEffectInstance> func) {
        super(type, maxLevel);
        this.func = func;
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        this.applyPotion(entity, event.getEntity(), level);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity attacker) {
            this.applyPotion(entity, attacker, level);
        }
    }

    private void applyPotion(AbstractGolemEntity<?, ?> self, LivingEntity target, int level) {
        if (!target.m_9236_().isClientSide()) {
            EffectUtil.addEffect(target, (MobEffectInstance) this.func.apply(level), EffectUtil.AddReason.NONE, self);
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        MobEffectInstance ins = (MobEffectInstance) this.func.apply(v);
        MutableComponent lang = Component.translatable(ins.getDescriptionId());
        MobEffect mobeffect = ins.getEffect();
        if (ins.getAmplifier() > 0) {
            lang = Component.translatable("potion.withAmplifier", lang, Component.translatable("potion.potency." + ins.getAmplifier()));
        }
        if (ins.getDuration() >= 20) {
            lang = Component.translatable("potion.withDuration", lang, MobEffectUtil.formatDuration(ins, 1.0F));
        }
        lang = lang.withStyle(mobeffect.getCategory().getTooltipFormatting());
        return List.of(MGLangData.POTION_ATTACK.get(lang).withStyle(ChatFormatting.GREEN));
    }
}