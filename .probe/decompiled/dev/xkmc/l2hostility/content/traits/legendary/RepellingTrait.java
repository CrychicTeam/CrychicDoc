package dev.xkmc.l2hostility.content.traits.legendary;

import dev.xkmc.l2hostility.init.data.LHConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;

public class RepellingTrait extends PushPullTrait {

    public RepellingTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    protected int getRange() {
        return LHConfig.COMMON.repellRange.get();
    }

    @Override
    protected double getStrength(double dist) {
        return (1.0 - dist) * LHConfig.COMMON.repellStrength.get();
    }

    @Override
    public void onAttackedByOthers(int level, LivingEntity entity, LivingAttackEvent event) {
        if (!event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !event.getSource().is(DamageTypeTags.BYPASSES_EFFECTS) && event.getSource().is(DamageTypeTags.IS_PROJECTILE)) {
            event.setCanceled(true);
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", Component.literal(LHConfig.COMMON.repellRange.get() + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
    }
}