package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class DamageCapModifier extends GolemModifier {

    public DamageCapModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            float factor = (float) Math.max(0.0, (2.0 - (double) level * 0.2) * MGConfig.COMMON.damageCap.get());
            if (event.getAmount() > factor * entity.m_21233_()) {
                event.setAmount(factor * entity.m_21233_());
                entity.m_9236_().broadcastEntityEvent(entity, (byte) 29);
            }
        }
    }

    @Override
    public List<MutableComponent> getDetail(int level) {
        float factor = (float) Math.max(0.0, (2.0 - (double) level * 0.2) * MGConfig.COMMON.damageCap.get());
        int perc = Math.round(100.0F * factor);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", perc).withStyle(ChatFormatting.GREEN));
    }
}