package se.mickelus.tetra.properties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import se.mickelus.tetra.items.modular.ModularItem;

@ParametersAreNonnullByDefault
public class AttributeHelper {

    public static final Multimap<Attribute, AttributeModifier> emptyMap = ImmutableMultimap.of();

    private static final Map<String, UUID> attributeIdMap = new HashMap();

    public static Multimap<Attribute, AttributeModifier> overwrite(Multimap<Attribute, AttributeModifier> a, Multimap<Attribute, AttributeModifier> b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            ArrayListMultimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
            result.putAll(a);
            b.asMap().forEach((x$0, x$1) -> result.replaceValues(x$0, x$1));
            return result;
        }
    }

    public static Multimap<Attribute, AttributeModifier> merge(Collection<Multimap<Attribute, AttributeModifier>> modifiers) {
        return (Multimap<Attribute, AttributeModifier>) modifiers.stream().reduce(null, AttributeHelper::merge);
    }

    public static Multimap<Attribute, AttributeModifier> merge(Multimap<Attribute, AttributeModifier> a, Multimap<Attribute, AttributeModifier> b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            ArrayListMultimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
            result.putAll(a);
            b.forEach((x$0, x$1) -> result.put(x$0, x$1));
            return result;
        }
    }

    public static Multimap<Attribute, AttributeModifier> retainMax(Multimap<Attribute, AttributeModifier> modifiers, Attribute... attributes) {
        return retainMax(modifiers, Arrays.asList(attributes));
    }

    public static Multimap<Attribute, AttributeModifier> retainMax(Multimap<Attribute, AttributeModifier> modifiers, Collection<Attribute> attributes) {
        return modifiers == null ? null : (Multimap) modifiers.asMap().entrySet().stream().collect(Multimaps.flatteningToMultimap(Entry::getKey, entry -> attributes.contains(entry.getKey()) ? retainMax((Collection<AttributeModifier>) entry.getValue()).stream() : ((Collection) entry.getValue()).stream(), ArrayListMultimap::create));
    }

    public static Collection<AttributeModifier> retainMax(Collection<AttributeModifier> modifiers) {
        return (Collection<AttributeModifier>) ((Map) modifiers.stream().collect(Collectors.groupingBy(AttributeModifier::m_22217_, Collectors.maxBy(Comparator.comparing(AttributeModifier::m_22218_))))).values().stream().map(Optional::get).collect(Collectors.toList());
    }

    public static Collection<AttributeModifier> collapse(Collection<AttributeModifier> modifiers) {
        return (Collection<AttributeModifier>) Stream.of(Optional.of(getAdditionAmount(modifiers)).filter(amount -> amount != 0.0).map(amount -> new AttributeModifier("tetra.stats.addition", amount, AttributeModifier.Operation.ADDITION)), Optional.of(getMultiplyAmount(modifiers)).map(amount -> amount - 1.0).filter(amount -> amount != 0.0).map(amount -> new AttributeModifier("tetra.stats.multiply", amount, AttributeModifier.Operation.MULTIPLY_TOTAL))).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
    }

    public static double getMergedAmount(Collection<AttributeModifier> modifiers) {
        return getMergedAmount(modifiers, 0.0);
    }

    public static double getMergedAmount(Collection<AttributeModifier> modifiers, double base) {
        return (getAdditionAmount(modifiers) + base) * getMultiplyAmount(modifiers);
    }

    public static double getAdditionAmount(Collection<AttributeModifier> modifiers) {
        double base = modifiers.stream().filter(modifier -> modifier.getOperation().equals(AttributeModifier.Operation.ADDITION)).mapToDouble(AttributeModifier::m_22218_).sum();
        return base + modifiers.stream().filter(modifier -> modifier.getOperation().equals(AttributeModifier.Operation.MULTIPLY_BASE)).mapToDouble(AttributeModifier::m_22218_).map(amount -> amount * Math.abs(base)).sum();
    }

    public static double getMultiplyAmount(Collection<AttributeModifier> modifiers) {
        return modifiers.stream().filter(modifier -> modifier.getOperation().equals(AttributeModifier.Operation.MULTIPLY_TOTAL)).mapToDouble(AttributeModifier::m_22218_).map(amount -> amount + 1.0).reduce(1.0, (a, b) -> a * b);
    }

    public static Multimap<Attribute, AttributeModifier> multiplyModifiers(Multimap<Attribute, AttributeModifier> modifiers, double multiplier) {
        return (Multimap<Attribute, AttributeModifier>) Optional.ofNullable(modifiers).map(Multimap::entries).map(Collection::stream).map(entries -> (ArrayListMultimap) entries.collect(Multimaps.toMultimap(Entry::getKey, entry -> multiplyModifier((AttributeModifier) entry.getValue(), multiplier), ArrayListMultimap::create))).orElse(null);
    }

    public static AttributeModifier multiplyModifier(AttributeModifier modifier, double multiplier) {
        return new AttributeModifier(modifier.getId(), modifier.getName(), modifier.getAmount() * multiplier, modifier.getOperation());
    }

    public static Multimap<Attribute, AttributeModifier> collapseRound(Multimap<Attribute, AttributeModifier> modifiers) {
        return (Multimap<Attribute, AttributeModifier>) Optional.ofNullable(modifiers).map(Multimap::asMap).map(Map::entrySet).map(Collection::stream).map(entries -> (ArrayListMultimap) entries.collect(Multimaps.flatteningToMultimap(Entry::getKey, entry -> collapse((Collection<AttributeModifier>) entry.getValue()).stream(), ArrayListMultimap::create))).map(AttributeHelper::round).orElse(null);
    }

    public static Multimap<Attribute, AttributeModifier> round(Multimap<Attribute, AttributeModifier> modifiers) {
        return (Multimap<Attribute, AttributeModifier>) ((Stream) Optional.ofNullable(modifiers).map(Multimap::entries).map(Collection::stream).orElseGet(Stream::empty)).collect(Multimaps.toMultimap(Entry::getKey, e -> round((Attribute) e.getKey(), (AttributeModifier) e.getValue()), ArrayListMultimap::create));
    }

    private static AttributeModifier round(Attribute attribute, AttributeModifier mod) {
        double multiplier = (Attributes.ATTACK_DAMAGE.equals(attribute) || Attributes.ARMOR.equals(attribute) || Attributes.ARMOR_TOUGHNESS.equals(attribute) || TetraAttributes.drawStrength.get().equals(attribute) || TetraAttributes.abilityDamage.get().equals(attribute)) && mod.getOperation() == AttributeModifier.Operation.ADDITION ? 2.0 : 20.0;
        return new AttributeModifier(mod.getId(), mod.getName(), (double) Math.round(mod.getAmount() * multiplier) / multiplier, mod.getOperation());
    }

    public static String getAttributeKey(Attribute attribute, AttributeModifier.Operation operation) {
        return attribute.getDescriptionId() + operation.ordinal();
    }

    private static UUID getAttributeId(Attribute attribute, AttributeModifier.Operation operation) {
        return (UUID) attributeIdMap.computeIfAbsent(getAttributeKey(attribute, operation), k -> Mth.createInsecureUUID());
    }

    public static AttributeModifier fixIdentifiers(Attribute attribute, AttributeModifier modifier) {
        return new AttributeModifier(getAttributeId(attribute, modifier.getOperation()), modifier.getName(), modifier.getAmount(), modifier.getOperation());
    }

    public static Multimap<Attribute, AttributeModifier> fixIdentifiers(Multimap<Attribute, AttributeModifier> modifiers) {
        return (Multimap<Attribute, AttributeModifier>) Optional.ofNullable(modifiers).map(Multimap::entries).map(Collection::stream).map(entries -> (ArrayListMultimap) entries.collect(Multimaps.toMultimap(Entry::getKey, entry -> fixIdentifiers((Attribute) entry.getKey(), (AttributeModifier) entry.getValue()), ArrayListMultimap::create))).orElse(null);
    }

    static {
        attributeIdMap.put(getAttributeKey(Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION), ModularItem.attackDamageModifier);
        attributeIdMap.put(getAttributeKey(Attributes.ATTACK_SPEED, AttributeModifier.Operation.ADDITION), ModularItem.attackSpeedModifier);
    }
}