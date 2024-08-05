package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public interface ISpellContainer extends INBTSerializable<CompoundTag> {

    @NotNull
    SpellData[] getAllSpells();

    @NotNull
    List<SpellData> getActiveSpells();

    int getMaxSpellCount();

    void setMaxSpellCount(int var1);

    int getActiveSpellCount();

    int getNextAvailableIndex();

    boolean mustEquip();

    boolean spellWheel();

    @NotNull
    SpellData getSpellAtIndex(int var1);

    int getIndexForSpell(AbstractSpell var1);

    boolean addSpellAtIndex(AbstractSpell var1, int var2, int var3, boolean var4, ItemStack var5);

    boolean addSpell(AbstractSpell var1, int var2, boolean var3, ItemStack var4);

    boolean removeSpellAtIndex(int var1, ItemStack var2);

    boolean removeSpell(AbstractSpell var1, ItemStack var2);

    boolean isEmpty();

    void save(ItemStack var1);

    static boolean isSpellContainer(ItemStack itemStack) {
        if (itemStack != null && itemStack.getCount() >= 1) {
            CompoundTag tag = itemStack.getTag();
            return tag != null && (tag.contains("ISB_Spells") || SpellContainer.isLegacyTagFormat(tag));
        } else {
            return false;
        }
    }

    static ISpellContainer create(int maxSpells, boolean addsToSpellWheel, boolean mustBeEquipped) {
        return new SpellContainer(maxSpells, addsToSpellWheel, mustBeEquipped);
    }

    static ISpellContainer createScrollContainer(AbstractSpell spell, int spellLevel, ItemStack itemStack) {
        ISpellContainer spellContainer = create(1, false, false);
        spellContainer.addSpellAtIndex(spell, spellLevel, 0, true, itemStack);
        return spellContainer;
    }

    static ISpellContainer createImbuedContainer(AbstractSpell spell, int spellLevel, ItemStack itemStack) {
        ISpellContainer spellContainer = create(1, true, itemStack.getItem() instanceof ArmorItem || itemStack.getItem() instanceof ICurioItem);
        spellContainer.addSpellAtIndex(spell, spellLevel, 0, true, itemStack);
        return spellContainer;
    }

    static ISpellContainer get(ItemStack itemStack) {
        return new SpellContainer(itemStack);
    }

    static ISpellContainer getOrCreate(ItemStack itemStack) {
        return isSpellContainer(itemStack) ? new SpellContainer(itemStack) : new SpellContainer(1, true, false);
    }
}