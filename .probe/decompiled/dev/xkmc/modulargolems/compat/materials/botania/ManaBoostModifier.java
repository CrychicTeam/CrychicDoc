package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2damagetracker.contents.attack.DamageModifier;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ManaBoostModifier extends ManaModifier {

    public ManaBoostModifier() {
        super(StatFilterType.ATTACK, 5);
    }

    @Override
    public void modifyDamage(AttackCache cache, AbstractGolemEntity<?, ?> entity, int level) {
        int manaCost = MGConfig.COMMON.manaBoostingCost.get() * level;
        double damageBoost = MGConfig.COMMON.manaBoostingDamage.get() * (double) level;
        int consumed = new BotUtils(entity).consumeMana(manaCost);
        if (consumed > 0) {
            if (consumed < manaCost) {
                damageBoost = damageBoost * (double) consumed / (double) manaCost;
            }
            cache.addHurtModifier(DamageModifier.multTotal(1.0F + (float) damageBoost));
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int bonus = (int) Math.round(MGConfig.COMMON.manaBoostingDamage.get() * (double) v * 100.0);
        int manaCost = MGConfig.COMMON.manaBoostingCost.get() * v;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", bonus, manaCost).withStyle(ChatFormatting.GREEN));
    }
}