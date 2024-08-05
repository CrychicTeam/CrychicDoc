package io.redspace.ironsspellbooks.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class UniqueSpellBook extends SimpleAttributeSpellBook implements UniqueItem {

    List<SpellData> spellData = null;

    SpellDataRegistryHolder[] spellDataRegistryHolders;

    public UniqueSpellBook(SpellRarity rarity, SpellDataRegistryHolder[] spellDataRegistryHolders, Supplier<Multimap<Attribute, AttributeModifier>> defaultModifiers) {
        super(spellDataRegistryHolders.length, rarity, (Multimap<Attribute, AttributeModifier>) defaultModifiers.get());
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }

    public UniqueSpellBook(SpellRarity rarity, SpellDataRegistryHolder[] spellDataRegistryHolders, int additionalSlots, Supplier<Multimap<Attribute, AttributeModifier>> defaultModifiers) {
        super(spellDataRegistryHolders.length + additionalSlots, rarity, (Multimap<Attribute, AttributeModifier>) defaultModifiers.get());
        this.spellDataRegistryHolders = spellDataRegistryHolders;
    }

    public UniqueSpellBook(SpellRarity rarity, SpellDataRegistryHolder[] spellDataRegistryHolders) {
        this(rarity, spellDataRegistryHolders, HashMultimap::create);
    }

    public UniqueSpellBook(SpellRarity rarity, SpellDataRegistryHolder[] spellDataRegistryHolders, int additionalSlots) {
        this(rarity, spellDataRegistryHolders, additionalSlots, HashMultimap::create);
    }

    public List<SpellData> getSpells() {
        if (this.spellData == null) {
            this.spellData = Arrays.stream(this.spellDataRegistryHolders).map(SpellDataRegistryHolder::getSpellData).toList();
            this.spellDataRegistryHolders = null;
        }
        return this.spellData;
    }

    @Override
    public Component getName(ItemStack pStack) {
        Component name = super.m_7626_(pStack);
        return (Component) (pStack.hasTag() && pStack.getTag().getBoolean("Improved") ? Component.translatable("tooltip.irons_spellbooks.improved_format", name) : name);
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    public ISpellContainer initializeSpellContainerOLD(ItemStack itemStack) {
        if (itemStack == null) {
            return new SpellContainer();
        } else if (ISpellContainer.isSpellContainer(itemStack)) {
            return ISpellContainer.get(itemStack);
        } else {
            ISpellContainer spellContainer = ISpellContainer.create(this.getMaxSpellSlots(), true, true);
            this.getSpells().forEach(spellSlot -> spellContainer.addSpell(spellSlot.getSpell(), spellSlot.getLevel(), true, null));
            spellContainer.save(itemStack);
            return spellContainer;
        }
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                ISpellContainer spellContainer = ISpellContainer.create(this.getMaxSpellSlots(), true, true);
                this.getSpells().forEach(spellSlot -> spellContainer.addSpell(spellSlot.getSpell(), spellSlot.getLevel(), true, null));
                spellContainer.save(itemStack);
            }
        }
    }
}