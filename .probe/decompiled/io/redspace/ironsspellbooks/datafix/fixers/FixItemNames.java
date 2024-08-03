package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import io.redspace.ironsspellbooks.datafix.DataFixerHelpers;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public class FixItemNames extends DataFixerElement {

    @Override
    public List<String> preScanValuesToMatch() {
        return DataFixerHelpers.LEGACY_ITEM_IDS.keySet().stream().toList();
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (tag != null && tag.contains("id", 8)) {
            String itemName = tag.getString("id");
            String newName = (String) DataFixerHelpers.LEGACY_ITEM_IDS.get(itemName);
            if (newName != null) {
                tag.putString("id", newName);
                return true;
            }
        }
        return false;
    }
}