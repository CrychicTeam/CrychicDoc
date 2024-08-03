package dev.xkmc.modulargolems.content.config;

import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.modulargolems.content.core.GolemStatType;
import dev.xkmc.modulargolems.content.core.IGolemPart;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.item.upgrade.UpgradeItem;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record GolemMaterial(HashMap<GolemStatType, Double> stats, HashMap<GolemModifier, Integer> modifiers, ResourceLocation id, Item part) {

    public static final ResourceLocation EMPTY = new ResourceLocation("modulargolems", "empty");

    public static Map<Attribute, Pair<GolemStatType, Double>> collectAttributes(List<GolemMaterial> list, List<UpgradeItem> upgrades) {
        HashMap<Attribute, Map<GolemStatType, Double>> values = new LinkedHashMap();
        for (GolemStatType type : GolemTypes.STAT_TYPES.get().getValues()) {
            appendStat(values, type, 0.0);
        }
        for (GolemMaterial stats : list) {
            stats.stats.forEach((k, v) -> appendStat(values, k, v));
        }
        for (Entry<GolemModifier, Integer> entry : collectModifiers(list, upgrades).entrySet()) {
            Object sorted = entry.getKey();
            if (sorted instanceof AttributeGolemModifier) {
                AttributeGolemModifier attr = (AttributeGolemModifier) sorted;
                for (AttributeGolemModifier.AttrEntry ent : attr.entries) {
                    appendStat(values, (GolemStatType) ent.type().get(), ent.getValue((Integer) entry.getValue()));
                }
            }
        }
        HashMap<Attribute, Pair<GolemStatType, Double>> ans = new LinkedHashMap();
        for (Entry<Attribute, Map<GolemStatType, Double>> ent : values.entrySet()) {
            HashMap<GolemStatType.Kind, Pair<GolemStatType, Double>> sorted = new LinkedHashMap();
            for (Entry<GolemStatType, Double> entryx : ((Map) ent.getValue()).entrySet()) {
                sorted.compute(((GolemStatType) entryx.getKey()).kind, (k, old) -> Pair.of((GolemStatType) entry.getKey(), (old == null ? 0.0 : (Double) old.getSecond()) + (Double) entry.getValue()));
            }
            if (sorted.size() != 0) {
                if (sorted.size() == 1) {
                    ans.put((Attribute) ent.getKey(), (Pair) sorted.values().stream().findFirst().get());
                } else {
                    if (!sorted.containsKey(GolemStatType.Kind.BASE)) {
                        throw new IllegalStateException("Only attributes with BASE modification allows multi-operation. Attribute: " + ((Attribute) ent.getKey()).getDescriptionId());
                    }
                    Pair<GolemStatType, Double> candidate = (Pair<GolemStatType, Double>) sorted.get(GolemStatType.Kind.BASE);
                    GolemStatType type = (GolemStatType) candidate.getFirst();
                    double val = (Double) candidate.getSecond();
                    if (sorted.containsKey(GolemStatType.Kind.ADD)) {
                        val += ((Pair) sorted.get(GolemStatType.Kind.ADD)).getSecond();
                    }
                    if (sorted.containsKey(GolemStatType.Kind.PERCENT)) {
                        val *= 1.0 + ((Pair) sorted.get(GolemStatType.Kind.PERCENT)).getSecond();
                    }
                    ans.put((Attribute) ent.getKey(), Pair.of(type, val));
                }
            }
        }
        return ans;
    }

    private static void appendStat(Map<Attribute, Map<GolemStatType, Double>> values, GolemStatType k, double v) {
        ((Map) values.computeIfAbsent(k.getAttribute(), e -> new LinkedHashMap())).compute(k, (e, old) -> (old == null ? 0.0 : old) + v);
    }

    public static HashMap<GolemModifier, Integer> collectModifiers(Collection<GolemMaterial> list, Collection<UpgradeItem> upgrades) {
        HashMap<GolemModifier, Integer> values = new LinkedHashMap();
        for (GolemMaterial stats : list) {
            stats.modifiers.forEach((k, v) -> values.compute(k, (a, old) -> Math.min(a.maxLevel, (old == null ? 0 : old) + v)));
        }
        upgrades.stream().flatMap(e -> e.get().stream()).forEach(e -> values.compute(e.mod(), (a, old) -> Math.min(a.maxLevel, (old == null ? 0 : old) + e.level())));
        return values;
    }

    public static <T extends AbstractGolemEntity<T, P>, P extends IGolemPart<P>> void addAttributes(List<GolemMaterial> list, List<UpgradeItem> upgrades, T entity) {
        AttributeSupplier map = DefaultAttributes.getSupplier((EntityType<? extends LivingEntity>) Wrappers.cast(entity.getType()));
        Map<Attribute, Pair<GolemStatType, Double>> attrs = collectAttributes(list, upgrades);
        attrs.keySet().forEach(e -> {
            AttributeInstance attr = entity.m_21051_(e);
            if (attr != null) {
                attr.setBaseValue(map.getBaseValue(e));
            }
        });
        attrs.forEach((k, v) -> ((GolemStatType) v.getFirst()).applyToEntity(entity, (Double) v.getSecond()));
    }

    public static Optional<ResourceLocation> getMaterial(ItemStack stack) {
        for (Entry<ResourceLocation, Ingredient> ent : GolemMaterialConfig.get().ingredients.entrySet()) {
            if (((Ingredient) ent.getValue()).test(stack)) {
                return Optional.of((ResourceLocation) ent.getKey());
            }
        }
        return Optional.empty();
    }

    public MutableComponent getDesc() {
        return Component.translatable("golem_material." + this.id.getNamespace() + "." + this.id.getPath()).withStyle(ChatFormatting.GOLD);
    }

    public GolemPart<?, ?> getPart() {
        return (GolemPart<?, ?>) Wrappers.cast(this.part());
    }
}