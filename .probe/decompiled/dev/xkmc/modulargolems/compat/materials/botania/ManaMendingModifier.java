package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ManaMendingModifier extends ManaModifier {

    public ManaMendingModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public double onHealTick(double heal, AbstractGolemEntity<?, ?> le, int level) {
        double healthDiff = (double) (le.m_21233_() - le.m_21223_()) - heal;
        Integer cost = MGConfig.COMMON.manaMendingCost.get();
        int maxHeal = (int) Math.floor(Math.min(healthDiff, MGConfig.COMMON.manaMendingVal.get() * (double) level));
        if (maxHeal <= 0) {
            return heal;
        } else {
            int maxCost = maxHeal * cost;
            int consume = new BotUtils(le).consumeMana(maxCost);
            double toHeal = 1.0 * (double) consume / (double) cost.intValue();
            return heal + toHeal;
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int eff = MGConfig.COMMON.manaMendingCost.get();
        double val = MGConfig.COMMON.manaMendingVal.get() * (double) v;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", val, eff).withStyle(ChatFormatting.GREEN));
    }
}