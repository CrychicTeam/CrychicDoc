package dev.xkmc.modulargolems.compat.materials.twilightforest;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import twilightforest.init.TFDimensionSettings;

public class TFDamageModifier extends GolemModifier {

    public TFDamageModifier() {
        super(StatFilterType.ATTACK, 5);
    }

    @Override
    public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
        if (entity.m_9236_().dimensionTypeId().equals(TFDimensionSettings.TWILIGHT_DIM_TYPE)) {
            double dmg = MGConfig.COMMON.compatTFDamage.get() * (double) level;
            cache.addHurtModifier(DamageModifier.multTotal(1.0F + (float) dmg));
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int bonus = (int) Math.round(MGConfig.COMMON.compatTFDamage.get() * (double) v * 100.0);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", bonus).withStyle(ChatFormatting.GREEN));
    }
}