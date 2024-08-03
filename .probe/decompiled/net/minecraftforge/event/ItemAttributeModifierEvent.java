package net.minecraftforge.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Collection;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.Nullable;

public class ItemAttributeModifierEvent extends Event {

    private final ItemStack stack;

    private final EquipmentSlot slotType;

    private final Multimap<Attribute, AttributeModifier> originalModifiers;

    private Multimap<Attribute, AttributeModifier> unmodifiableModifiers;

    @Nullable
    private Multimap<Attribute, AttributeModifier> modifiableModifiers;

    public ItemAttributeModifierEvent(ItemStack stack, EquipmentSlot slotType, Multimap<Attribute, AttributeModifier> modifiers) {
        this.stack = stack;
        this.slotType = slotType;
        this.unmodifiableModifiers = this.originalModifiers = modifiers;
    }

    public Multimap<Attribute, AttributeModifier> getModifiers() {
        return this.unmodifiableModifiers;
    }

    public Multimap<Attribute, AttributeModifier> getOriginalModifiers() {
        return this.originalModifiers;
    }

    private Multimap<Attribute, AttributeModifier> getModifiableMap() {
        if (this.modifiableModifiers == null) {
            this.modifiableModifiers = HashMultimap.create(this.originalModifiers);
            this.unmodifiableModifiers = Multimaps.unmodifiableMultimap(this.modifiableModifiers);
        }
        return this.modifiableModifiers;
    }

    public boolean addModifier(Attribute attribute, AttributeModifier modifier) {
        return this.getModifiableMap().put(attribute, modifier);
    }

    public boolean removeModifier(Attribute attribute, AttributeModifier modifier) {
        return this.getModifiableMap().remove(attribute, modifier);
    }

    public Collection<AttributeModifier> removeAttribute(Attribute attribute) {
        return this.getModifiableMap().removeAll(attribute);
    }

    public void clearModifiers() {
        this.getModifiableMap().clear();
    }

    public EquipmentSlot getSlotType() {
        return this.slotType;
    }

    public ItemStack getItemStack() {
        return this.stack;
    }
}