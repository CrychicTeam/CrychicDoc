package dev.xkmc.modulargolems.content.modifier.immunes;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class MagicResistanceModifier extends GolemModifier {

    public MagicResistanceModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public void onHurt(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        float factor = (float) Math.max(0.0, 1.0 - (double) level * MGConfig.COMMON.magicResistance.get());
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && event.getSource().is(L2DamageTypes.MAGIC)) {
            event.setAmount(event.getAmount() * factor);
        }
    }

    @Override
    public List<MutableComponent> getDetail(int level) {
        float factor = (float) Math.max(0.0, 1.0 - (double) level * MGConfig.COMMON.magicResistance.get());
        int perc = Math.round(100.0F * factor);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", perc).withStyle(ChatFormatting.GREEN));
    }
}