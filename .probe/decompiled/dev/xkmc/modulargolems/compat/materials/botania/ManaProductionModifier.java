package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ManaProductionModifier extends ManaModifier {

    public ManaProductionModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public double onHealTick(double heal, AbstractGolemEntity<?, ?> entity, int level) {
        int prod = MGConfig.COMMON.manaProductionVal.get() * level;
        new BotUtils(entity).generateMana(prod);
        return heal;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int prod = MGConfig.COMMON.manaProductionVal.get() * v;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", prod).withStyle(ChatFormatting.GREEN));
    }
}