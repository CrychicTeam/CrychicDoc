package net.minecraft.world.entity.ai.attributes;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

public class AttributeInstance {

    private final Attribute attribute;

    private final Map<AttributeModifier.Operation, Set<AttributeModifier>> modifiersByOperation = Maps.newEnumMap(AttributeModifier.Operation.class);

    private final Map<UUID, AttributeModifier> modifierById = new Object2ObjectArrayMap();

    private final Set<AttributeModifier> permanentModifiers = new ObjectArraySet();

    private double baseValue;

    private boolean dirty = true;

    private double cachedValue;

    private final Consumer<AttributeInstance> onDirty;

    public AttributeInstance(Attribute attribute0, Consumer<AttributeInstance> consumerAttributeInstance1) {
        this.attribute = attribute0;
        this.onDirty = consumerAttributeInstance1;
        this.baseValue = attribute0.getDefaultValue();
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public double getBaseValue() {
        return this.baseValue;
    }

    public void setBaseValue(double double0) {
        if (double0 != this.baseValue) {
            this.baseValue = double0;
            this.setDirty();
        }
    }

    public Set<AttributeModifier> getModifiers(AttributeModifier.Operation attributeModifierOperation0) {
        return (Set<AttributeModifier>) this.modifiersByOperation.computeIfAbsent(attributeModifierOperation0, p_22124_ -> Sets.newHashSet());
    }

    public Set<AttributeModifier> getModifiers() {
        return ImmutableSet.copyOf(this.modifierById.values());
    }

    @Nullable
    public AttributeModifier getModifier(UUID uUID0) {
        return (AttributeModifier) this.modifierById.get(uUID0);
    }

    public boolean hasModifier(AttributeModifier attributeModifier0) {
        return this.modifierById.get(attributeModifier0.getId()) != null;
    }

    private void addModifier(AttributeModifier attributeModifier0) {
        AttributeModifier $$1 = (AttributeModifier) this.modifierById.putIfAbsent(attributeModifier0.getId(), attributeModifier0);
        if ($$1 != null) {
            throw new IllegalArgumentException("Modifier is already applied on this attribute!");
        } else {
            this.getModifiers(attributeModifier0.getOperation()).add(attributeModifier0);
            this.setDirty();
        }
    }

    public void addTransientModifier(AttributeModifier attributeModifier0) {
        this.addModifier(attributeModifier0);
    }

    public void addPermanentModifier(AttributeModifier attributeModifier0) {
        this.addModifier(attributeModifier0);
        this.permanentModifiers.add(attributeModifier0);
    }

    protected void setDirty() {
        this.dirty = true;
        this.onDirty.accept(this);
    }

    public void removeModifier(AttributeModifier attributeModifier0) {
        this.getModifiers(attributeModifier0.getOperation()).remove(attributeModifier0);
        this.modifierById.remove(attributeModifier0.getId());
        this.permanentModifiers.remove(attributeModifier0);
        this.setDirty();
    }

    public void removeModifier(UUID uUID0) {
        AttributeModifier $$1 = this.getModifier(uUID0);
        if ($$1 != null) {
            this.removeModifier($$1);
        }
    }

    public boolean removePermanentModifier(UUID uUID0) {
        AttributeModifier $$1 = this.getModifier(uUID0);
        if ($$1 != null && this.permanentModifiers.contains($$1)) {
            this.removeModifier($$1);
            return true;
        } else {
            return false;
        }
    }

    public void removeModifiers() {
        for (AttributeModifier $$0 : this.getModifiers()) {
            this.removeModifier($$0);
        }
    }

    public double getValue() {
        if (this.dirty) {
            this.cachedValue = this.calculateValue();
            this.dirty = false;
        }
        return this.cachedValue;
    }

    private double calculateValue() {
        double $$0 = this.getBaseValue();
        for (AttributeModifier $$1 : this.getModifiersOrEmpty(AttributeModifier.Operation.ADDITION)) {
            $$0 += $$1.getAmount();
        }
        double $$2 = $$0;
        for (AttributeModifier $$3 : this.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_BASE)) {
            $$2 += $$0 * $$3.getAmount();
        }
        for (AttributeModifier $$4 : this.getModifiersOrEmpty(AttributeModifier.Operation.MULTIPLY_TOTAL)) {
            $$2 *= 1.0 + $$4.getAmount();
        }
        return this.attribute.sanitizeValue($$2);
    }

    private Collection<AttributeModifier> getModifiersOrEmpty(AttributeModifier.Operation attributeModifierOperation0) {
        return (Collection<AttributeModifier>) this.modifiersByOperation.getOrDefault(attributeModifierOperation0, Collections.emptySet());
    }

    public void replaceFrom(AttributeInstance attributeInstance0) {
        this.baseValue = attributeInstance0.baseValue;
        this.modifierById.clear();
        this.modifierById.putAll(attributeInstance0.modifierById);
        this.permanentModifiers.clear();
        this.permanentModifiers.addAll(attributeInstance0.permanentModifiers);
        this.modifiersByOperation.clear();
        attributeInstance0.modifiersByOperation.forEach((p_22107_, p_22108_) -> this.getModifiers(p_22107_).addAll(p_22108_));
        this.setDirty();
    }

    public CompoundTag save() {
        CompoundTag $$0 = new CompoundTag();
        $$0.putString("Name", BuiltInRegistries.ATTRIBUTE.getKey(this.attribute).toString());
        $$0.putDouble("Base", this.baseValue);
        if (!this.permanentModifiers.isEmpty()) {
            ListTag $$1 = new ListTag();
            for (AttributeModifier $$2 : this.permanentModifiers) {
                $$1.add($$2.save());
            }
            $$0.put("Modifiers", $$1);
        }
        return $$0;
    }

    public void load(CompoundTag compoundTag0) {
        this.baseValue = compoundTag0.getDouble("Base");
        if (compoundTag0.contains("Modifiers", 9)) {
            ListTag $$1 = compoundTag0.getList("Modifiers", 10);
            for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
                AttributeModifier $$3 = AttributeModifier.load($$1.getCompound($$2));
                if ($$3 != null) {
                    this.modifierById.put($$3.getId(), $$3);
                    this.getModifiers($$3.getOperation()).add($$3);
                    this.permanentModifiers.add($$3);
                }
            }
        }
        this.setDirty();
    }
}