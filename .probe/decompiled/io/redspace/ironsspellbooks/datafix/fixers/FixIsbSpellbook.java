package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import io.redspace.ironsspellbooks.datafix.DataFixerHelpers;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class FixIsbSpellbook extends DataFixerElement {

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("ISB_spellbook");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        if (tag != null) {
            CompoundTag spellBookTag = (CompoundTag) tag.get("ISB_spellbook");
            if (spellBookTag != null) {
                ListTag listTagSpells = (ListTag) spellBookTag.get("spells");
                if (listTagSpells != null && !listTagSpells.isEmpty()) {
                    boolean fixed = false;
                    if (((CompoundTag) listTagSpells.get(0)).contains("id")) {
                        this.fixSpellbookData(listTagSpells);
                        fixed = true;
                    }
                    if (this.fixSpellbookSpellIds(listTagSpells)) {
                        fixed = true;
                    }
                    return fixed;
                }
            }
        }
        return false;
    }

    private void fixSpellbookData(ListTag listTag) {
        listTag.forEach(tag -> {
            CompoundTag t = (CompoundTag) tag;
            int legacySpellId = t.getInt("id");
            t.putString("sid", (String) DataFixerHelpers.LEGACY_SPELL_MAPPING.getOrDefault(legacySpellId, "irons_spellbooks:none"));
            t.remove("id");
        });
    }

    private boolean fixSpellbookSpellIds(ListTag listTagSpells) {
        AtomicBoolean fixed = new AtomicBoolean(false);
        listTagSpells.forEach(tag -> {
            CompoundTag spellTag = (CompoundTag) tag;
            if (spellTag.contains("sid")) {
                String newName = (String) DataFixerHelpers.NEW_SPELL_IDS.get(spellTag.get("sid").getAsString());
                if (newName != null) {
                    spellTag.putString("sid", newName);
                    fixed.set(true);
                }
            }
        });
        return fixed.get();
    }
}