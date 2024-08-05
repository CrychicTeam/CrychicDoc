package io.redspace.ironsspellbooks.capabilities.magic;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.LegacySpellBookData;
import io.redspace.ironsspellbooks.api.spells.LegacySpellData;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.Scroll;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.UniqueItem;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

public class SpellContainer implements ISpellContainer {

    public static final String SPELL_SLOT_CONTAINER = "ISB_Spells";

    public static final String SPELL_DATA = "data";

    public static final String MAX_SLOTS = "maxSpells";

    public static final String MUST_EQUIP = "mustEquip";

    public static final String SPELL_WHEEL = "spellWheel";

    public static final String SLOT_INDEX = "index";

    public static final String SPELL_ID = "id";

    public static final String SPELL_LEVEL = "level";

    public static final String SPELL_LOCKED = "locked";

    private SpellData[] slots;

    private int maxSpells = 0;

    private int activeSlots = 0;

    private boolean spellWheel = false;

    private boolean mustEquip = true;

    public SpellContainer() {
    }

    public SpellContainer(int maxSpells, boolean spellWheel, boolean mustEquip) {
        this.maxSpells = maxSpells;
        this.slots = new SpellData[this.maxSpells];
        this.spellWheel = spellWheel;
        this.mustEquip = mustEquip;
    }

    public SpellContainer(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTagElement("ISB_Spells");
        if (tag != null) {
            this.deserializeNBT(tag);
        } else {
            this.convertLegacyData(itemStack);
        }
    }

    @Override
    public int getMaxSpellCount() {
        return this.maxSpells;
    }

    @Override
    public void setMaxSpellCount(int maxSpells) {
        this.maxSpells = maxSpells;
        this.slots = (SpellData[]) Arrays.copyOf(this.slots, maxSpells);
    }

    @Override
    public int getActiveSpellCount() {
        return this.activeSlots;
    }

    @Override
    public boolean isEmpty() {
        return this.activeSlots == 0;
    }

    @Override
    public void save(ItemStack stack) {
        if (stack != null) {
            stack.addTagElement("ISB_Spells", this.serializeNBT());
        }
    }

    @Override
    public SpellData[] getAllSpells() {
        SpellData[] result = new SpellData[this.maxSpells];
        if (this.maxSpells > 0) {
            System.arraycopy(this.slots, 0, result, 0, this.slots.length);
        }
        return result;
    }

    @NotNull
    @Override
    public List<SpellData> getActiveSpells() {
        return (List<SpellData>) Arrays.stream(this.slots).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public int getNextAvailableIndex() {
        return ArrayUtils.indexOf(this.slots, null);
    }

    @Override
    public boolean mustEquip() {
        return this.mustEquip;
    }

    @Override
    public boolean spellWheel() {
        return this.spellWheel;
    }

    @NotNull
    @Override
    public SpellData getSpellAtIndex(int index) {
        if (index >= 0 && index < this.maxSpells) {
            SpellData result = this.slots[index];
            if (result != null) {
                return this.slots[index];
            }
        }
        return SpellData.EMPTY;
    }

    @Override
    public int getIndexForSpell(AbstractSpell spell) {
        for (int i = 0; i < this.maxSpells; i++) {
            SpellData s = this.slots[i];
            if (s != null && s.getSpell().equals(spell)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean addSpellAtIndex(AbstractSpell spell, int level, int index, boolean locked, ItemStack itemStack) {
        if (index > -1 && index < this.maxSpells && this.slots[index] == null && Arrays.stream(this.slots).noneMatch(s -> s != null && s.getSpell().equals(spell))) {
            this.slots[index] = new SpellData(spell, level, locked);
            this.activeSlots++;
            this.save(itemStack);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addSpell(AbstractSpell spell, int level, boolean locked, ItemStack itemStack) {
        return this.addSpellAtIndex(spell, level, this.getNextAvailableIndex(), locked, itemStack);
    }

    @Override
    public boolean removeSpellAtIndex(int index, ItemStack itemStack) {
        if (index > -1 && index < this.maxSpells && this.slots[index] != null) {
            this.slots[index] = null;
            this.activeSlots--;
            this.save(itemStack);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean removeSpell(AbstractSpell spell, ItemStack itemStack) {
        if (spell == null) {
            return false;
        } else {
            int i = 0;
            if (i < this.maxSpells) {
                SpellData spellData = this.slots[i];
                if (spellData != null && spell.equals(spellData.getSpell())) {
                    return this.removeSpellAtIndex(i, itemStack);
                }
            }
            return false;
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag rootTag = new CompoundTag();
        rootTag.putInt("maxSpells", this.maxSpells);
        rootTag.putBoolean("mustEquip", this.mustEquip);
        rootTag.putBoolean("spellWheel", this.spellWheel);
        ListTag listTag = new ListTag();
        for (int i = 0; i < this.maxSpells; i++) {
            SpellData spellData = this.slots[i];
            if (spellData != null) {
                CompoundTag slotTag = new CompoundTag();
                slotTag.putString("id", spellData.getSpell().getSpellId());
                slotTag.putInt("level", spellData.getLevel());
                slotTag.putBoolean("locked", spellData.isLocked());
                slotTag.putInt("index", i);
                listTag.add(slotTag);
            }
        }
        rootTag.put("data", listTag);
        return rootTag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.maxSpells = nbt.getInt("maxSpells");
        this.mustEquip = nbt.getBoolean("mustEquip");
        this.spellWheel = nbt.getBoolean("spellWheel");
        this.slots = new SpellData[this.maxSpells];
        this.activeSlots = 0;
        ListTag listTagSpells = (ListTag) nbt.get("data");
        if (listTagSpells != null && !listTagSpells.isEmpty()) {
            listTagSpells.forEach(tagSlot -> {
                CompoundTag t = (CompoundTag) tagSlot;
                String id = t.getString("id");
                int level = t.getInt("level");
                boolean locked = t.getBoolean("locked");
                int index = t.getInt("index");
                if (index < this.slots.length) {
                    this.slots[index] = new SpellData(SpellRegistry.getSpell(id), level, locked);
                    this.activeSlots++;
                } else {
                    boolean var7 = false;
                }
            });
        }
    }

    public static boolean isLegacyTagFormat(CompoundTag tag) {
        return tag.contains("ISB_spell") || tag.contains("ISB_spellbook");
    }

    private void convertLegacyData(ItemStack itemStack) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && isLegacyTagFormat(tag)) {
            convertTag(tag, itemStack);
            CompoundTag convertedTag = itemStack.getTagElement("ISB_Spells");
            if (convertedTag != null) {
                this.deserializeNBT(convertedTag);
            }
        }
    }

    private static void convertTag(CompoundTag tag, ItemStack itemStack) {
        if (tag.contains("ISB_spell")) {
            LegacySpellData legacySpellData = LegacySpellData.getSpellData(itemStack);
            SpellContainer spellContainer = new SpellContainer(1, !(itemStack.getItem() instanceof Scroll), false);
            spellContainer.addSpellAtIndex(legacySpellData.spell, legacySpellData.spellLevel, 0, itemStack.getItem() instanceof UniqueItem, null);
            itemStack.addTagElement("ISB_Spells", spellContainer.serializeNBT());
            itemStack.removeTagKey("ISB_spell");
        } else if (tag.contains("ISB_spellbook")) {
            if (itemStack.getItem() instanceof SpellBook spellBookItem) {
                LegacySpellBookData legcySpellBookData = LegacySpellBookData.getSpellBookData(itemStack);
                int newSize = spellBookItem.getMaxSpellSlots();
                SpellContainer spellContainer = new SpellContainer(newSize, true, true);
                boolean unique = itemStack.getItem() instanceof UniqueItem;
                for (int i = 0; i < legcySpellBookData.transcribedSpells.length; i++) {
                    LegacySpellData legacySpellData = legcySpellBookData.transcribedSpells[i];
                    if (legacySpellData != null) {
                        spellContainer.addSpellAtIndex(legacySpellData.spell, legacySpellData.spellLevel, i, unique, null);
                    }
                }
                itemStack.addTagElement("ISB_Spells", spellContainer.serializeNBT());
            }
            itemStack.removeTagKey("ISB_spellbook");
        }
    }
}