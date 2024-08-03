package dev.shadowsoffire.placebo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.codec.PlaceboCodecs;
import dev.shadowsoffire.placebo.util.StepFunction;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public record RandomAttributeModifier(Attribute attribute, AttributeModifier.Operation op, StepFunction value) {

    public static Codec<RandomAttributeModifier> CODEC = RecordCodecBuilder.create(inst -> inst.group(ForgeRegistries.ATTRIBUTES.getCodec().fieldOf("attribute").forGetter(a -> a.attribute), PlaceboCodecs.enumCodec(AttributeModifier.Operation.class).fieldOf("operation").forGetter(a -> a.op), StepFunction.CODEC.fieldOf("value").forGetter(a -> a.value)).apply(inst, RandomAttributeModifier::new));

    public static Codec<RandomAttributeModifier> CONSTANT_CODEC = RecordCodecBuilder.create(inst -> inst.group(ForgeRegistries.ATTRIBUTES.getCodec().fieldOf("attribute").forGetter(a -> a.attribute), PlaceboCodecs.enumCodec(AttributeModifier.Operation.class).fieldOf("operation").forGetter(a -> a.op), StepFunction.CONSTANT_CODEC.fieldOf("value").forGetter(a -> a.value)).apply(inst, RandomAttributeModifier::new));

    public void apply(RandomSource rand, LivingEntity entity) {
        if (entity == null) {
            throw new RuntimeException("Attempted to apply a random attribute modifier to a null entity!");
        } else {
            AttributeModifier modif = this.create(rand);
            AttributeInstance inst = entity.getAttribute(this.attribute);
            if (inst == null) {
                Placebo.LOGGER.trace(String.format("Attempted to apply a random attribute modifier to an entity (%s) that does not have that attribute (%s)!", EntityType.getKey(entity.m_6095_()), ForgeRegistries.ATTRIBUTES.getKey(this.attribute)));
            } else {
                inst.addPermanentModifier(modif);
            }
        }
    }

    public AttributeModifier create(RandomSource rand) {
        return new AttributeModifier(UUID.randomUUID(), "placebo_random_modifier_" + this.attribute.getDescriptionId(), (double) this.value.get(rand.nextFloat()), this.op);
    }

    public AttributeModifier create(String name, RandomSource rand) {
        return new AttributeModifier(name, (double) this.value.get(rand.nextFloat()), this.op);
    }

    public AttributeModifier createDeterministic() {
        return new AttributeModifier(UUID.randomUUID(), "placebo_random_modifier_" + this.attribute.getDescriptionId(), (double) this.value.min(), this.op);
    }

    public AttributeModifier createDeterministic(String name) {
        return new AttributeModifier(UUID.randomUUID(), name, (double) this.value.min(), this.op);
    }

    public Attribute getAttribute() {
        return this.attribute;
    }

    public AttributeModifier.Operation getOp() {
        return this.op;
    }

    public StepFunction getValue() {
        return this.value;
    }

    @Deprecated(forRemoval = true)
    public static class Deserializer implements JsonDeserializer<RandomAttributeModifier>, JsonSerializer<RandomAttributeModifier> {

        public RandomAttributeModifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String _attribute = obj.get("attribute").getAsString();
            AttributeModifier.Operation op = (AttributeModifier.Operation) ctx.deserialize(obj.get("operation"), AttributeModifier.Operation.class);
            StepFunction value;
            if (obj.get("value").isJsonObject()) {
                JsonObject valueObj = GsonHelper.getAsJsonObject(obj, "value");
                value = (StepFunction) ctx.deserialize(valueObj, StepFunction.class);
            } else {
                float v = GsonHelper.getAsFloat(obj, "value");
                value = new StepFunction(v, 1, 0.0F);
            }
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(new ResourceLocation(_attribute));
            if (attribute != null && value != null && op != null) {
                return new RandomAttributeModifier(attribute, op, value);
            } else {
                throw new JsonParseException("Attempted to deserialize invalid RandomAttributeModifier: " + json.toString());
            }
        }

        public JsonElement serialize(RandomAttributeModifier src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("attribute", ForgeRegistries.ATTRIBUTES.getKey(src.attribute).toString());
            obj.addProperty("operation", src.op.name());
            StepFunction range = src.value;
            if (range.min() == range.max()) {
                obj.addProperty("value", range.min());
            } else {
                obj.add("value", context.serialize(src.value));
            }
            return obj;
        }
    }
}