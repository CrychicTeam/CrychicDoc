package dev.xkmc.modulargolems.content.modifier.special;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGTagGen;
import java.util.HashSet;
import java.util.List;

public class TalentMetaModifier extends GolemModifier {

    public TalentMetaModifier() {
        super(StatFilterType.HEALTH, 1);
    }

    @Override
    public int addSlot(List<UpgradeItem> upgrades, int lv) {
        int ans = 0;
        for (UpgradeItem item : new HashSet(upgrades)) {
            if (item.m_7968_().is(MGTagGen.BLUE_UPGRADES)) {
                ans++;
            }
        }
        return Math.min(4, ans);
    }
}