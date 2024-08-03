package com.mna.spells.crafting;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mna.ManaAndArtifice;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiable;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public final class ModifiedSpellPart<T extends ISpellComponent & IModifiable<T>> implements IModifiedSpellPart<T> {

    private final T part;

    private ImmutableList<AttributeValuePair> attributeValues;

    public ModifiedSpellPart(T part) {
        this.part = part;
        this.attributeValues = ImmutableList.copyOf(part.getModifiableAttributes());
    }

    @Override
    public final T getPart() {
        return this.part;
    }

    @Override
    public final float getMaximumValue(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getMaximum();
            }
        }
        return 0.0F;
    }

    @Override
    public final float getMinimumValue(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getMinimum();
            }
        }
        return 0.0F;
    }

    @Override
    public final float getValue(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getValue();
            }
        }
        return 0.0F;
    }

    @Override
    public final float getValueWithoutMultipliers(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getValueWithoutMultipliers();
            }
        }
        return 0.0F;
    }

    @Override
    public final float getMultiplier(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getMultiplier();
            }
        }
        return 1.0F;
    }

    @Override
    public final boolean setValue(Attribute attribute, float value) {
        UnmodifiableIterator var3 = this.attributeValues.iterator();
        while (var3.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var3.next();
            if (pair.getAttribute() == attribute && pair.getValue() != value) {
                pair.setValue(value);
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean setMultiplier(Attribute attribute, float value) {
        UnmodifiableIterator var3 = this.attributeValues.iterator();
        while (var3.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var3.next();
            if (pair.getAttribute() == attribute && pair.getValue() != value) {
                pair.setMultiplier(value);
                return true;
            }
        }
        return false;
    }

    @Override
    public final boolean resetMultiplier(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                pair.setMultiplier(1.0F);
                return true;
            }
        }
        return false;
    }

    @Override
    public final float getDefaultValue(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getDefaultValue();
            }
        }
        return 0.0F;
    }

    @Override
    public final float getStep(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getStep();
            }
        }
        return 1.0F;
    }

    @Override
    public final float getStepComplexity(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.getStepComplexity();
            }
        }
        return 1.0F;
    }

    @Override
    public final void resetValueToDefault(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                pair.setValue(pair.getDefaultValue());
            }
        }
    }

    @Override
    public final float stepUp(Attribute attribute, float maxModifier) {
        UnmodifiableIterator var3 = this.attributeValues.iterator();
        while (var3.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var3.next();
            if (pair.getAttribute() == attribute) {
                return pair.stepUp(maxModifier);
            }
        }
        return 0.0F;
    }

    @Override
    public final float stepUpIgnoreMax(Attribute attribute) {
        UnmodifiableIterator var2 = this.attributeValues.iterator();
        while (var2.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var2.next();
            if (pair.getAttribute() == attribute) {
                return pair.stepUpIgnoreMax();
            }
        }
        return 0.0F;
    }

    @Override
    public final float stepDown(Attribute attribute, float maxModifier) {
        UnmodifiableIterator var3 = this.attributeValues.iterator();
        while (var3.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var3.next();
            if (pair.getAttribute() == attribute) {
                return pair.stepDown(maxModifier);
            }
        }
        return 0.0F;
    }

    @Override
    public final ImmutableList<Attribute> getContainedAttributes() {
        Attribute[] attrs = new Attribute[this.attributeValues.size()];
        for (int i = 0; i < this.attributeValues.size(); i++) {
            attrs[i] = ((AttributeValuePair) this.attributeValues.get(i)).getAttribute();
        }
        return ImmutableList.copyOf(attrs);
    }

    public CompoundTag toNBT() {
        CompoundTag nbt = new CompoundTag();
        if (this.part != null) {
            nbt.putString("resource_location", this.part.getRegistryName().toString());
            nbt.putInt("num_attributes", this.getContainedAttributes().size());
            int count = 0;
            for (UnmodifiableIterator var3 = this.getContainedAttributes().iterator(); var3.hasNext(); count++) {
                Attribute attr = (Attribute) var3.next();
                float value = this.getValueWithoutMultipliers(attr);
                float mult = this.getMultiplier(attr);
                nbt.putString("attribute_" + count, attr.name());
                nbt.putFloat("value_" + count, value);
                nbt.putFloat("mult_" + count, mult);
            }
        } else {
            nbt.putString("resource_location", ManaAndArtifice.EMPTY.toString());
            nbt.putInt("num_attributes", 0);
        }
        return nbt;
    }

    @Override
    public boolean isSame(IModifiedSpellPart<T> other) {
        if (this.getPart() != other.getPart()) {
            return false;
        } else {
            ImmutableList<Attribute> myAttrs = this.getContainedAttributes();
            ImmutableList<Attribute> theirAttrs = other.getContainedAttributes();
            return myAttrs.size() == theirAttrs.size() && !myAttrs.stream().anyMatch(a -> !theirAttrs.contains(a) || this.getValue(a) != other.getValue(a));
        }
    }

    public static <T extends IModifiable<T> & ISpellComponent> ModifiedSpellPart<T> fromNBT(CompoundTag nbt, IForgeRegistry<T> registry) {
        if (!nbt.contains("resource_location")) {
            return null;
        } else {
            ResourceLocation rLoc = new ResourceLocation(nbt.getString("resource_location"));
            if (!registry.containsKey(rLoc)) {
                return null;
            } else {
                T inst = (T) registry.getValue(rLoc);
                ModifiedSpellPart<T> msp = new ModifiedSpellPart<>(inst);
                if (!nbt.contains("num_attributes")) {
                    return msp;
                } else {
                    int attributes = nbt.getInt("num_attributes");
                    for (int i = 0; i < attributes; i++) {
                        if (nbt.contains("attribute_" + i) && nbt.contains("value_" + i)) {
                            try {
                                Attribute attr = Attribute.valueOf(nbt.getString("attribute_" + i));
                                float value = nbt.getFloat("value_" + i);
                                float mult = nbt.getFloat("mult_" + i);
                                msp.setValue(attr, value);
                                msp.setMultiplier(attr, mult);
                            } catch (IllegalArgumentException var10) {
                            }
                        }
                    }
                    return msp;
                }
            }
        }
    }
}