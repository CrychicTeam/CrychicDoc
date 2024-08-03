package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import io.redspace.ironsspellbooks.datafix.DataFixerHelpers;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class FixUpgradeType extends DataFixerElement {

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("ISBUpgrades");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (tag != null && tag.contains("ISBUpgrades")) {
            for (Tag t : tag.getList("ISBUpgrades", 10)) {
                CompoundTag upgrade = (CompoundTag) t;
                String upgradeKey = upgrade.getString("id");
                String newKey = (String) DataFixerHelpers.LEGACY_UPGRADE_TYPE_IDS.get(upgradeKey);
                if (newKey != null) {
                    upgrade.putString("id", newKey);
                    return true;
                }
            }
        }
        return false;
    }
}