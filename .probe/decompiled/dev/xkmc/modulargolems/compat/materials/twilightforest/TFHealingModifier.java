package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import twilightforest.init.TFDimensionSettings;

public class TFHealingModifier extends GolemModifier {

    public TFHealingModifier() {
        super(StatFilterType.HEALTH, 5);
    }

    @Override
    public double onInventoryHealTick(double heal, GolemModifier.HealingContext ctx, int level) {
        return ctx.owner().level().dimensionTypeId().equals(TFDimensionSettings.TWILIGHT_DIM_TYPE) ? heal * (1.0 + MGConfig.COMMON.compatTFHealing.get() * (double) level) : heal;
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int bonus = (int) Math.round(MGConfig.COMMON.compatTFHealing.get() * (double) v * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", bonus).withStyle(ChatFormatting.GREEN));
    }
}