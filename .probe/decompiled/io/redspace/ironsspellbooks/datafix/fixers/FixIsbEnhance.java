package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import io.redspace.ironsspellbooks.datafix.DataFixerHelpers;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public class FixIsbEnhance extends DataFixerElement {

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("ISBEnhance");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (tag != null && tag.contains("ISBEnhance")) {
            Tag ringTag = tag.get("ISBEnhance");
            if (ringTag instanceof IntTag legacyRingTag) {
                tag.remove("ISBEnhance");
                tag.putString("ISBEnhance", (String) DataFixerHelpers.LEGACY_SPELL_MAPPING.getOrDefault(legacyRingTag.getAsInt(), "irons_spellbooks:none"));
                return true;
            }
            if (ringTag instanceof StringTag stringTag) {
                String newName = (String) DataFixerHelpers.NEW_SPELL_IDS.get(stringTag.getAsString());
                if (newName != null) {
                    tag.putString("ISBEnhance", newName);
                    return true;
                }
            }
        }
        return false;
    }
}