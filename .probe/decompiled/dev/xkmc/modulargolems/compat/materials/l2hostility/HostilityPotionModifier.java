package dev.xkmc.modulargolems.compat.materials.l2hostility;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HostilityPotionModifier extends GolemModifier {

    public HostilityPotionModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public int addSlot(List<UpgradeItem> upgrades, int lv) {
        Set<UpgradeItem> set = new HashSet();
        for (UpgradeItem e : upgrades) {
            if (e.m_7968_().is(MGTagGen.POTION_UPGRADES)) {
                set.add(e);
            }
        }
        return set.size();
    }
}