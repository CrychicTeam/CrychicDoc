package dev.xkmc.modulargolems.content.modifier.common;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ThornModifier extends GolemModifier {

    private static float getPercent() {
        return (float) MGConfig.COMMON.thorn.get().doubleValue();
    }

    public ThornModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (level != 0) {
            DamageSource source = event.getSource();
            if (!source.is(L2DamageTypes.MAGIC) && !source.is(DamageTypes.THORNS)) {
                if (source.getDirectEntity() instanceof LivingEntity living && living.isAlive()) {
                    living.hurt(entity.m_9236_().damageSources().thorns(entity), event.getAmount() * getPercent() * (float) level);
                }
            }
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int reflect = Math.round(getPercent() * (float) v * 100.0F);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", reflect).withStyle(ChatFormatting.GREEN));
    }
}