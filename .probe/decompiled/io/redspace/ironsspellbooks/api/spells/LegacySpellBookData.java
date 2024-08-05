package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class LegacySpellBookData {

    public static final String ISB_SPELLBOOK = "ISB_spellbook";

    public static final String SPELL_SLOTS = "spellSlots";

    public static final String ACTIVE_SPELL_INDEX = "activeSpellIndex";

    public static final String SPELLS = "spells";

    public static final String LEGACY_ID = "id";

    public static final String ID = "sid";

    public static final String LEVEL = "level";

    public static final String SLOT = "slot";

    public LegacySpellData[] transcribedSpells;

    public int activeSpellIndex = -1;

    public int spellSlots;

    public int spellCount = 0;

    public LegacySpellBookData(ItemStack stack, CompoundTag tag) {
        this.loadFromNBT(stack, tag);
    }

    public LegacySpellBookData(int spellSlots) {
        this.spellSlots = spellSlots;
        this.transcribedSpells = new LegacySpellData[this.spellSlots];
    }

    public void loadFromNBT(ItemStack stack, CompoundTag compound) {
        this.spellSlots = compound.getInt("spellSlots");
        this.transcribedSpells = new LegacySpellData[this.spellSlots];
        this.activeSpellIndex = compound.getInt("activeSpellIndex");
        ListTag listTagSpells = (ListTag) compound.get("spells");
        this.spellCount = 0;
        if (listTagSpells != null && listTagSpells.size() > 0) {
            listTagSpells.forEach(tag -> {
                CompoundTag t = (CompoundTag) tag;
                String id = t.getString("sid");
                int level = t.getInt("level");
                int index = t.getInt("slot");
                this.transcribedSpells[index] = new LegacySpellData(SpellRegistry.getSpell(id), level);
                this.spellCount++;
            });
        }
    }

    public static LegacySpellBookData getSpellBookData(ItemStack stack) {
        if (stack == null) {
            return new LegacySpellBookData(0);
        } else {
            CompoundTag tag = stack.getTagElement("ISB_spellbook");
            return tag != null ? new LegacySpellBookData(stack, tag) : new LegacySpellBookData(0);
        }
    }
}