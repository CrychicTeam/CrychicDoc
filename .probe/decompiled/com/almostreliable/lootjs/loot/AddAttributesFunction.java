package com.almostreliable.lootjs.loot;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class AddAttributesFunction implements LootItemFunction {

    private static final Function<ItemStack, EquipmentSlot[]> SLOTS_BY_ITEM = itemStack -> new EquipmentSlot[] { LivingEntity.getEquipmentSlotForItem(itemStack) };

    private final boolean preserveDefaultModifier;

    private final List<AddAttributesFunction.Modifier> modifiers;

    public AddAttributesFunction(boolean preserveDefaultModifier, List<AddAttributesFunction.Modifier> modifiers) {
        this.preserveDefaultModifier = preserveDefaultModifier;
        this.modifiers = modifiers;
    }

    public ItemStack apply(ItemStack itemStack, LootContext context) {
        for (AddAttributesFunction.Modifier modifier : this.modifiers) {
            if (context.getRandom().nextFloat() < modifier.probability) {
                AttributeModifier am = modifier.createAttributeModifier(context);
                for (EquipmentSlot slot : (EquipmentSlot[]) modifier.slots.apply(itemStack)) {
                    if (this.preserveDefaultModifier) {
                        this.preserveDefaultAttributes(itemStack, slot);
                    }
                    itemStack.addAttributeModifier(modifier.attribute, am, slot);
                }
            }
        }
        return itemStack;
    }

    public void preserveDefaultAttributes(ItemStack itemStack, EquipmentSlot slot) {
        if (!itemStack.hasTag() || !itemStack.getTag().contains("AttributeModifiers", 9)) {
            Multimap<Attribute, AttributeModifier> defaultAttributeModifiers = itemStack.getItem().getDefaultAttributeModifiers(slot);
            for (Entry<Attribute, AttributeModifier> entry : defaultAttributeModifiers.entries()) {
                itemStack.addAttributeModifier((Attribute) entry.getKey(), (AttributeModifier) entry.getValue(), slot);
            }
        }
    }

    @Override
    public LootItemFunctionType getType() {
        throw new UnsupportedOperationException("Do not call");
    }

    public static class Builder implements LootItemFunction.Builder {

        private final List<AddAttributesFunction.Modifier> modifiers = new ArrayList();

        private boolean preserveDefaults = true;

        public AddAttributesFunction.Builder preserveDefaults(boolean flag) {
            this.preserveDefaults = flag;
            return this;
        }

        public AddAttributesFunction.Builder simple(Attribute attribute, NumberProvider amount) {
            return this.simple(1.0F, attribute, amount);
        }

        public AddAttributesFunction.Builder simple(float probability, Attribute attribute, NumberProvider amount) {
            return this.add(attribute, amount, m -> m.setProbability(probability));
        }

        public AddAttributesFunction.Builder forSlots(Attribute attribute, NumberProvider amount, EquipmentSlot[] slots) {
            return this.add(attribute, amount, m -> m.setSlots(slots));
        }

        public AddAttributesFunction.Builder forSlots(float probability, Attribute attribute, NumberProvider amount, EquipmentSlot[] slots) {
            return this.add(attribute, amount, m -> {
                m.setProbability(probability);
                m.setSlots(slots);
            });
        }

        public AddAttributesFunction.Builder add(Attribute attribute, NumberProvider amount, Consumer<AddAttributesFunction.Modifier.Builder> action) {
            AddAttributesFunction.Modifier.Builder builder = new AddAttributesFunction.Modifier.Builder(attribute, amount);
            action.accept(builder);
            return this.add(builder.build());
        }

        public AddAttributesFunction.Builder add(AddAttributesFunction.Modifier modifier) {
            Objects.requireNonNull(modifier);
            this.modifiers.add(modifier);
            return this;
        }

        public AddAttributesFunction build() {
            return new AddAttributesFunction(this.preserveDefaults, this.modifiers);
        }
    }

    public static class Modifier {

        protected final Attribute attribute;

        protected final float probability;

        protected final AttributeModifier.Operation operation;

        protected final NumberProvider amount;

        protected final String name;

        protected final Function<ItemStack, EquipmentSlot[]> slots;

        @Nullable
        protected UUID uuid;

        public Modifier(float probability, Attribute attribute, AttributeModifier.Operation operation, NumberProvider amount, String name, Function<ItemStack, EquipmentSlot[]> slots, @Nullable UUID uuid) {
            this.attribute = attribute;
            this.probability = probability;
            this.operation = operation;
            this.amount = amount;
            this.name = name;
            this.slots = slots;
            this.uuid = uuid;
        }

        public AttributeModifier createAttributeModifier(LootContext context) {
            return new AttributeModifier(UUID.randomUUID(), this.name, (double) this.amount.getFloat(context), this.operation);
        }

        public static class Builder {

            protected final Attribute attribute;

            protected final NumberProvider amount;

            protected float probability;

            protected AttributeModifier.Operation operation;

            protected Function<ItemStack, EquipmentSlot[]> slots;

            @Nullable
            protected UUID uuid;

            @Nullable
            protected String name;

            public Builder(Attribute attribute, NumberProvider amount) {
                this.attribute = attribute;
                this.amount = amount;
                this.probability = 1.0F;
                this.operation = AttributeModifier.Operation.ADDITION;
                this.slots = AddAttributesFunction.SLOTS_BY_ITEM;
            }

            public void setProbability(float probability) {
                this.probability = probability;
            }

            public void setOperation(AttributeModifier.Operation operation) {
                this.operation = operation;
            }

            public void setSlots(EquipmentSlot[] slots) {
                this.slots = itemStack -> slots;
            }

            public void setName(@Nullable String name) {
                this.name = name;
            }

            public void setUuid(@Nullable UUID uuid) {
                this.uuid = uuid;
            }

            public AddAttributesFunction.Modifier build() {
                if (this.name == null) {
                    this.name = "lootjs." + this.attribute.getDescriptionId() + "." + this.operation.name().toLowerCase();
                }
                return new AddAttributesFunction.Modifier(this.probability, this.attribute, this.operation, this.amount, this.name, this.slots, this.uuid);
            }
        }
    }
}