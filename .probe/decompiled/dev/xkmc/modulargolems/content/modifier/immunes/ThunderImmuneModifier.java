package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.l2library.base.effects.EffectUtil;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.advancement.GolemTriggers;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class ThunderImmuneModifier extends GolemModifier {

    public ThunderImmuneModifier() {
        super(StatFilterType.HEALTH, 2);
    }

    @Override
    public void onAttacked(AbstractGolemEntity<?, ?> entity, LivingAttackEvent event, int level) {
        if (level > 0 && event.getSource().is(DamageTypeTags.IS_LIGHTNING)) {
            event.setCanceled(true);
            EffectUtil.addEffect(entity, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200), EffectUtil.AddReason.SELF, entity);
            entity.m_5634_((float) (MGConfig.COMMON.thunderHeal.get() * level));
            if (entity.getOwner() instanceof ServerPlayer pl) {
                GolemTriggers.THUNDER.trigger(pl);
            }
        }
    }

    @Override
    public void onRegisterFlag(Consumer<GolemFlags> addFlag) {
        addFlag.accept(GolemFlags.THUNDER_IMMUNE);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int heal = MGConfig.COMMON.thunderHeal.get() * v;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", heal).withStyle(ChatFormatting.GREEN));
    }
}