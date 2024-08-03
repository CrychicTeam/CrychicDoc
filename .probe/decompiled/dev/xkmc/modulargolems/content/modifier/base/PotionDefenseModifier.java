package dev.xkmc.modulargolems.content.modifier.base;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class PotionDefenseModifier extends GolemModifier {

    private final Supplier<MobEffect> effect;

    public PotionDefenseModifier(int maxLevel, Supplier<MobEffect> effect) {
        super(StatFilterType.HEALTH, maxLevel);
        this.effect = effect;
    }

    private MobEffectInstance getIns(int lv) {
        return new MobEffectInstance((MobEffect) this.effect.get(), 40, lv - 1, false, false);
    }

    @Override
    public void onAiStep(AbstractGolemEntity<?, ?> golem, int level) {
        EffectUtil.refreshEffect(golem, this.getIns(level), EffectUtil.AddReason.SELF, golem);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        MobEffectInstance ins = this.getIns(v);
        MutableComponent lang = Component.translatable(ins.getDescriptionId());
        MobEffect mobeffect = ins.getEffect();
        if (ins.getAmplifier() > 0) {
            lang = Component.translatable("potion.withAmplifier", lang, Component.translatable("potion.potency." + ins.getAmplifier()));
        }
        lang = lang.withStyle(mobeffect.getCategory().getTooltipFormatting());
        return List.of(MGLangData.POTION_DEFENSE.get(lang).withStyle(ChatFormatting.GREEN));
    }
}