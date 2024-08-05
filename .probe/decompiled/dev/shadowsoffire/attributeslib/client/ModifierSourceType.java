package dev.shadowsoffire.attributeslib.client;

import dev.shadowsoffire.attributeslib.api.AttributeHelper;
import dev.shadowsoffire.attributeslib.util.Comparators;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public abstract class ModifierSourceType<T> {

    private static final List<ModifierSourceType<?>> SOURCE_TYPES = new ArrayList();

    public static final ModifierSourceType<ItemStack> EQUIPMENT = register(new ModifierSourceType<ItemStack>() {

        @Override
        public void extract(LivingEntity entity, BiConsumer<AttributeModifier, ModifierSource<?>> map) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack item = entity.getItemBySlot(slot);
                item.getAttributeModifiers(slot).values().forEach(modif -> map.accept(modif, new ModifierSource.ItemModifierSource(item)));
            }
        }

        @Override
        public int getPriority() {
            return 0;
        }
    });

    public static final ModifierSourceType<MobEffectInstance> MOB_EFFECT = register(new ModifierSourceType<MobEffectInstance>() {

        @Override
        public void extract(LivingEntity entity, BiConsumer<AttributeModifier, ModifierSource<?>> map) {
            for (MobEffectInstance effectInst : entity.getActiveEffects()) {
                effectInst.getEffect().getAttributeModifiers().values().forEach(modif -> map.accept(modif, new ModifierSource.EffectModifierSource(effectInst)));
            }
        }

        @Override
        public int getPriority() {
            return 100;
        }
    });

    public static Collection<ModifierSourceType<?>> getTypes() {
        return Collections.unmodifiableCollection(SOURCE_TYPES);
    }

    public static <T extends ModifierSourceType<?>> T register(T type) {
        SOURCE_TYPES.add(type);
        return type;
    }

    public static Comparator<AttributeModifier> compareBySource(Map<UUID, ModifierSource<?>> sources) {
        Comparator<AttributeModifier> comp = Comparators.chained(Comparator.comparingInt(a -> ((ModifierSource) sources.get(a.getId())).getType().getPriority()), Comparator.comparing(a -> (ModifierSource) sources.get(a.getId())), AttributeHelper.modifierComparator());
        return (a1, a2) -> {
            ModifierSource<?> src1 = (ModifierSource<?>) sources.get(a1.getId());
            ModifierSource<?> src2 = (ModifierSource<?>) sources.get(a2.getId());
            if (src1 != null && src2 != null) {
                return comp.compare(a1, a2);
            } else {
                return src1 != null ? -1 : (src2 != null ? 1 : 0);
            }
        };
    }

    public abstract void extract(LivingEntity var1, BiConsumer<AttributeModifier, ModifierSource<?>> var2);

    public abstract int getPriority();
}