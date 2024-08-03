package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.l2hostility.content.traits.common.RegenTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class RegenModifier extends GolemModifier {

    public RegenModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public double onInventoryHealTick(double heal, GolemModifier.HealingContext ctx, int level) {
        return heal + (double) ctx.maxHealth() * LHConfig.COMMON.regen.get() * (double) level;
    }

    @Override
    public List<MutableComponent> getDetail(int i) {
        List<MutableComponent> ans = new ArrayList();
        ans.add(Component.translatable(((RegenTrait) LHTraits.REGEN.get()).getDescriptionId() + ".desc", Component.literal((int) Math.round(LHConfig.COMMON.regen.get() * 100.0 * (double) i) + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GREEN));
        return ans;
    }
}