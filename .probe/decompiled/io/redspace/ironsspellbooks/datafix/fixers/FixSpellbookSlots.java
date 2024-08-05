package io.redspace.ironsspellbooks.datafix.fixers;

import io.redspace.ironsspellbooks.datafix.DataFixerElement;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public class FixSpellbookSlots extends DataFixerElement {

    @Override
    public List<String> preScanValuesToMatch() {
        return List.of("irons_spellbooks:netherite_spell_book", "irons_spellbooks:diamond_spell_book", "irons_spellbooks:gold_spell_book", "irons_spellbooks:iron_spell_book", "irons_spellbooks:copper_spell_book", "irons_spellbooks:rotten_spell_book", "irons_spellbooks:blaze_spell_book", "irons_spellbooks:dragonskin_spell_book", "irons_spellbooks:druidic_spell_book", "irons_spellbooks:villager_spell_book");
    }

    @Override
    public boolean runFixer(CompoundTag tag) {
        return false;
    }
}