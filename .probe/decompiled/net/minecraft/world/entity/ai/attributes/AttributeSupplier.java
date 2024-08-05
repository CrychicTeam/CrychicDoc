package net.minecraft.world.entity.ai.attributes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;

public class AttributeSupplier {

    private final Map<Attribute, AttributeInstance> instances;

    public AttributeSupplier(Map<Attribute, AttributeInstance> mapAttributeAttributeInstance0) {
        this.instances = ImmutableMap.copyOf(mapAttributeAttributeInstance0);
    }

    private AttributeInstance getAttributeInstance(Attribute attribute0) {
        AttributeInstance $$1 = (AttributeInstance) this.instances.get(attribute0);
        if ($$1 == null) {
            throw new IllegalArgumentException("Can't find attribute " + BuiltInRegistries.ATTRIBUTE.getKey(attribute0));
        } else {
            return $$1;
        }
    }

    public double getValue(Attribute attribute0) {
        return this.getAttributeInstance(attribute0).getValue();
    }

    public double getBaseValue(Attribute attribute0) {
        return this.getAttributeInstance(attribute0).getBaseValue();
    }

    public double getModifierValue(Attribute attribute0, UUID uUID1) {
        AttributeModifier $$2 = this.getAttributeInstance(attribute0).getModifier(uUID1);
        if ($$2 == null) {
            throw new IllegalArgumentException("Can't find modifier " + uUID1 + " on attribute " + BuiltInRegistries.ATTRIBUTE.getKey(attribute0));
        } else {
            return $$2.getAmount();
        }
    }

    @Nullable
    public AttributeInstance createInstance(Consumer<AttributeInstance> consumerAttributeInstance0, Attribute attribute1) {
        AttributeInstance $$2 = (AttributeInstance) this.instances.get(attribute1);
        if ($$2 == null) {
            return null;
        } else {
            AttributeInstance $$3 = new AttributeInstance(attribute1, consumerAttributeInstance0);
            $$3.replaceFrom($$2);
            return $$3;
        }
    }

    public static AttributeSupplier.Builder builder() {
        return new AttributeSupplier.Builder();
    }

    public boolean hasAttribute(Attribute attribute0) {
        return this.instances.containsKey(attribute0);
    }

    public boolean hasModifier(Attribute attribute0, UUID uUID1) {
        AttributeInstance $$2 = (AttributeInstance) this.instances.get(attribute0);
        return $$2 != null && $$2.getModifier(uUID1) != null;
    }

    public static class Builder {

        private final Map<Attribute, AttributeInstance> builder = Maps.newHashMap();

        private boolean instanceFrozen;

        private AttributeInstance create(Attribute attribute0) {
            AttributeInstance $$1 = new AttributeInstance(attribute0, p_258260_ -> {
                if (this.instanceFrozen) {
                    throw new UnsupportedOperationException("Tried to change value for default attribute instance: " + BuiltInRegistries.ATTRIBUTE.getKey(attribute0));
                }
            });
            this.builder.put(attribute0, $$1);
            return $$1;
        }

        public AttributeSupplier.Builder add(Attribute attribute0) {
            this.create(attribute0);
            return this;
        }

        public AttributeSupplier.Builder add(Attribute attribute0, double double1) {
            AttributeInstance $$2 = this.create(attribute0);
            $$2.setBaseValue(double1);
            return this;
        }

        public AttributeSupplier build() {
            this.instanceFrozen = true;
            return new AttributeSupplier(this.builder);
        }
    }
}