package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class SetAttributesFunction extends LootItemConditionalFunction {

    final List<SetAttributesFunction.Modifier> modifiers;

    SetAttributesFunction(LootItemCondition[] lootItemCondition0, List<SetAttributesFunction.Modifier> listSetAttributesFunctionModifier1) {
        super(lootItemCondition0);
        this.modifiers = ImmutableList.copyOf(listSetAttributesFunctionModifier1);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_ATTRIBUTES;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return (Set<LootContextParam<?>>) this.modifiers.stream().flatMap(p_279080_ -> p_279080_.amount.m_6231_().stream()).collect(ImmutableSet.toImmutableSet());
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        RandomSource $$2 = lootContext1.getRandom();
        for (SetAttributesFunction.Modifier $$3 : this.modifiers) {
            UUID $$4 = $$3.id;
            if ($$4 == null) {
                $$4 = UUID.randomUUID();
            }
            EquipmentSlot $$5 = Util.getRandom($$3.slots, $$2);
            itemStack0.addAttributeModifier($$3.attribute, new AttributeModifier($$4, $$3.name, (double) $$3.amount.getFloat(lootContext1), $$3.operation), $$5);
        }
        return itemStack0;
    }

    public static SetAttributesFunction.ModifierBuilder modifier(String string0, Attribute attribute1, AttributeModifier.Operation attributeModifierOperation2, NumberProvider numberProvider3) {
        return new SetAttributesFunction.ModifierBuilder(string0, attribute1, attributeModifierOperation2, numberProvider3);
    }

    public static SetAttributesFunction.Builder setAttributes() {
        return new SetAttributesFunction.Builder();
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetAttributesFunction.Builder> {

        private final List<SetAttributesFunction.Modifier> modifiers = Lists.newArrayList();

        protected SetAttributesFunction.Builder getThis() {
            return this;
        }

        public SetAttributesFunction.Builder withModifier(SetAttributesFunction.ModifierBuilder setAttributesFunctionModifierBuilder0) {
            this.modifiers.add(setAttributesFunctionModifierBuilder0.build());
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetAttributesFunction(this.m_80699_(), this.modifiers);
        }
    }

    static class Modifier {

        final String name;

        final Attribute attribute;

        final AttributeModifier.Operation operation;

        final NumberProvider amount;

        @Nullable
        final UUID id;

        final EquipmentSlot[] slots;

        Modifier(String string0, Attribute attribute1, AttributeModifier.Operation attributeModifierOperation2, NumberProvider numberProvider3, EquipmentSlot[] equipmentSlot4, @Nullable UUID uUID5) {
            this.name = string0;
            this.attribute = attribute1;
            this.operation = attributeModifierOperation2;
            this.amount = numberProvider3;
            this.id = uUID5;
            this.slots = equipmentSlot4;
        }

        public JsonObject serialize(JsonSerializationContext jsonSerializationContext0) {
            JsonObject $$1 = new JsonObject();
            $$1.addProperty("name", this.name);
            $$1.addProperty("attribute", BuiltInRegistries.ATTRIBUTE.getKey(this.attribute).toString());
            $$1.addProperty("operation", operationToString(this.operation));
            $$1.add("amount", jsonSerializationContext0.serialize(this.amount));
            if (this.id != null) {
                $$1.addProperty("id", this.id.toString());
            }
            if (this.slots.length == 1) {
                $$1.addProperty("slot", this.slots[0].getName());
            } else {
                JsonArray $$2 = new JsonArray();
                for (EquipmentSlot $$3 : this.slots) {
                    $$2.add(new JsonPrimitive($$3.getName()));
                }
                $$1.add("slot", $$2);
            }
            return $$1;
        }

        public static SetAttributesFunction.Modifier deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            String $$2 = GsonHelper.getAsString(jsonObject0, "name");
            ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "attribute"));
            Attribute $$4 = BuiltInRegistries.ATTRIBUTE.get($$3);
            if ($$4 == null) {
                throw new JsonSyntaxException("Unknown attribute: " + $$3);
            } else {
                AttributeModifier.Operation $$5 = operationFromString(GsonHelper.getAsString(jsonObject0, "operation"));
                NumberProvider $$6 = GsonHelper.getAsObject(jsonObject0, "amount", jsonDeserializationContext1, NumberProvider.class);
                UUID $$7 = null;
                EquipmentSlot[] $$8;
                if (GsonHelper.isStringValue(jsonObject0, "slot")) {
                    $$8 = new EquipmentSlot[] { EquipmentSlot.byName(GsonHelper.getAsString(jsonObject0, "slot")) };
                } else {
                    if (!GsonHelper.isArrayNode(jsonObject0, "slot")) {
                        throw new JsonSyntaxException("Invalid or missing attribute modifier slot; must be either string or array of strings.");
                    }
                    JsonArray $$9 = GsonHelper.getAsJsonArray(jsonObject0, "slot");
                    $$8 = new EquipmentSlot[$$9.size()];
                    int $$11 = 0;
                    for (JsonElement $$12 : $$9) {
                        $$8[$$11++] = EquipmentSlot.byName(GsonHelper.convertToString($$12, "slot"));
                    }
                    if ($$8.length == 0) {
                        throw new JsonSyntaxException("Invalid attribute modifier slot; must contain at least one entry.");
                    }
                }
                if (jsonObject0.has("id")) {
                    String $$14 = GsonHelper.getAsString(jsonObject0, "id");
                    try {
                        $$7 = UUID.fromString($$14);
                    } catch (IllegalArgumentException var13) {
                        throw new JsonSyntaxException("Invalid attribute modifier id '" + $$14 + "' (must be UUID format, with dashes)");
                    }
                }
                return new SetAttributesFunction.Modifier($$2, $$4, $$5, $$6, $$8, $$7);
            }
        }

        private static String operationToString(AttributeModifier.Operation attributeModifierOperation0) {
            switch(attributeModifierOperation0) {
                case ADDITION:
                    return "addition";
                case MULTIPLY_BASE:
                    return "multiply_base";
                case MULTIPLY_TOTAL:
                    return "multiply_total";
                default:
                    throw new IllegalArgumentException("Unknown operation " + attributeModifierOperation0);
            }
        }

        private static AttributeModifier.Operation operationFromString(String string0) {
            switch(string0) {
                case "addition":
                    return AttributeModifier.Operation.ADDITION;
                case "multiply_base":
                    return AttributeModifier.Operation.MULTIPLY_BASE;
                case "multiply_total":
                    return AttributeModifier.Operation.MULTIPLY_TOTAL;
                default:
                    throw new JsonSyntaxException("Unknown attribute modifier operation " + string0);
            }
        }
    }

    public static class ModifierBuilder {

        private final String name;

        private final Attribute attribute;

        private final AttributeModifier.Operation operation;

        private final NumberProvider amount;

        @Nullable
        private UUID id;

        private final Set<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);

        public ModifierBuilder(String string0, Attribute attribute1, AttributeModifier.Operation attributeModifierOperation2, NumberProvider numberProvider3) {
            this.name = string0;
            this.attribute = attribute1;
            this.operation = attributeModifierOperation2;
            this.amount = numberProvider3;
        }

        public SetAttributesFunction.ModifierBuilder forSlot(EquipmentSlot equipmentSlot0) {
            this.slots.add(equipmentSlot0);
            return this;
        }

        public SetAttributesFunction.ModifierBuilder withUuid(UUID uUID0) {
            this.id = uUID0;
            return this;
        }

        public SetAttributesFunction.Modifier build() {
            return new SetAttributesFunction.Modifier(this.name, this.attribute, this.operation, this.amount, (EquipmentSlot[]) this.slots.toArray(new EquipmentSlot[0]), this.id);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetAttributesFunction> {

        public void serialize(JsonObject jsonObject0, SetAttributesFunction setAttributesFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setAttributesFunction1, jsonSerializationContext2);
            JsonArray $$3 = new JsonArray();
            for (SetAttributesFunction.Modifier $$4 : setAttributesFunction1.modifiers) {
                $$3.add($$4.serialize(jsonSerializationContext2));
            }
            jsonObject0.add("modifiers", $$3);
        }

        public SetAttributesFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            JsonArray $$3 = GsonHelper.getAsJsonArray(jsonObject0, "modifiers");
            List<SetAttributesFunction.Modifier> $$4 = Lists.newArrayListWithExpectedSize($$3.size());
            for (JsonElement $$5 : $$3) {
                $$4.add(SetAttributesFunction.Modifier.deserialize(GsonHelper.convertToJsonObject($$5, "modifier"), jsonDeserializationContext1));
            }
            if ($$4.isEmpty()) {
                throw new JsonSyntaxException("Invalid attribute modifiers array; cannot be empty");
            } else {
                return new SetAttributesFunction(lootItemCondition2, $$4);
            }
        }
    }
}