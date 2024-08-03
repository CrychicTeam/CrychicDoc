package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class FieryModifier extends GolemModifier {

    private static float getPercent() {
        return (float) MGConfig.COMMON.fiery.get().doubleValue();
    }

    public FieryModifier() {
        super(StatFilterType.ATTACK, 5);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int reflect = Math.round((1.0F + getPercent() * (float) v) * 100.0F);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", reflect).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (!event.getEntity().m_5825_()) {
            event.getEntity().m_20254_(10);
            event.setAmount(event.getAmount() * (1.0F + getPercent() * (float) level));
        }
    }
}