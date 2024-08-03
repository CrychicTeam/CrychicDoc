package dev.shadowsoffire.attributeslib.api;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;
import dev.shadowsoffire.attributeslib.util.Comparators;
import dev.shadowsoffire.attributeslib.util.ItemAccess;
import java.util.Comparator;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeHelper {

    public static final UUID BASE_ATTACK_DAMAGE = ItemAccess.getBaseAD();

    public static final UUID BASE_ATTACK_SPEED = ItemAccess.getBaseAS();

    public static final UUID BASE_ENTITY_REACH = UUID.fromString("89689aa7-c577-4d97-a03e-791fde1798d4");

    public static final UUID ELYTRA_FLIGHT_UUID = UUID.fromString("72aae561-99a9-4a48-9b14-589a255cb077");

    public static final UUID CREATIVE_FLIGHT_UUID = UUID.fromString("3f54312c-0b60-44ff-bf1e-219091553964");

    public static void modify(LivingEntity entity, Attribute attribute, String name, double value, AttributeModifier.Operation operation) {
        AttributeInstance inst = entity.getAttribute(attribute);
        if (inst != null) {
            inst.addPermanentModifier(new AttributeModifier("attributeslib:" + name, value, operation));
        }
    }

    public static void addToBase(LivingEntity entity, Attribute attribute, String name, double modifier) {
        modify(entity, attribute, name, modifier, AttributeModifier.Operation.ADDITION);
    }

    public static void addXTimesNewBase(LivingEntity entity, Attribute attribute, String name, double modifier) {
        modify(entity, attribute, name, modifier, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    public static void multiplyFinal(LivingEntity entity, Attribute attribute, String name, double modifier) {
        modify(entity, attribute, name, modifier, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static Multimap<Attribute, AttributeModifier> sortedMap() {
        return TreeMultimap.create(Comparators.idComparator(BuiltInRegistries.ATTRIBUTE), modifierComparator());
    }

    public static Comparator<AttributeModifier> modifierComparator() {
        return Comparators.chained(Comparator.comparing(AttributeModifier::m_22217_), Comparator.comparing(AttributeModifier::m_22218_), Comparator.comparing(AttributeModifier::m_22209_));
    }

    public static MutableComponent list() {
        return Component.literal(" ┇ ").withStyle(ChatFormatting.GRAY);
    }
}