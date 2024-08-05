package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import io.redspace.ironsspellbooks.datafix.DataFixerHelpers;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public class FixIsbSpell extends DataFixerElement {

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("ISB_spell");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (tag != null) {
            CompoundTag spellTag = (CompoundTag) tag.get("ISB_spell");
            if (spellTag != null) {
                boolean fixed = false;
                if (spellTag.contains("type")) {
                    this.fixScrollData(spellTag);
                    fixed = true;
                }
                if (spellTag.contains("id")) {
                    String newName = (String) DataFixerHelpers.NEW_SPELL_IDS.get(spellTag.get("id").getAsString());
                    if (newName != null) {
                        spellTag.putString("id", newName);
                        fixed = true;
                    }
                }
                return fixed;
            }
        }
        return false;
    }

    private void fixScrollData(CompoundTag tag) {
        int legacySpellId = tag.getInt("type");
        tag.remove("type");
        tag.putString("id", (String) DataFixerHelpers.LEGACY_SPELL_MAPPING.getOrDefault(legacySpellId, "irons_spellbooks:none"));
    }
}