package net.minecraft.world.item.alchemy;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class PotionUtils {

    public static final String TAG_CUSTOM_POTION_EFFECTS = "CustomPotionEffects";

    public static final String TAG_CUSTOM_POTION_COLOR = "CustomPotionColor";

    public static final String TAG_POTION = "Potion";

    private static final int EMPTY_COLOR = 16253176;

    private static final Component NO_EFFECT = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    public static List<MobEffectInstance> getMobEffects(ItemStack itemStack0) {
        return getAllEffects(itemStack0.getTag());
    }

    public static List<MobEffectInstance> getAllEffects(Potion potion0, Collection<MobEffectInstance> collectionMobEffectInstance1) {
        List<MobEffectInstance> $$2 = Lists.newArrayList();
        $$2.addAll(potion0.getEffects());
        $$2.addAll(collectionMobEffectInstance1);
        return $$2;
    }

    public static List<MobEffectInstance> getAllEffects(@Nullable CompoundTag compoundTag0) {
        List<MobEffectInstance> $$1 = Lists.newArrayList();
        $$1.addAll(getPotion(compoundTag0).getEffects());
        getCustomEffects(compoundTag0, $$1);
        return $$1;
    }

    public static List<MobEffectInstance> getCustomEffects(ItemStack itemStack0) {
        return getCustomEffects(itemStack0.getTag());
    }

    public static List<MobEffectInstance> getCustomEffects(@Nullable CompoundTag compoundTag0) {
        List<MobEffectInstance> $$1 = Lists.newArrayList();
        getCustomEffects(compoundTag0, $$1);
        return $$1;
    }

    public static void getCustomEffects(@Nullable CompoundTag compoundTag0, List<MobEffectInstance> listMobEffectInstance1) {
        if (compoundTag0 != null && compoundTag0.contains("CustomPotionEffects", 9)) {
            ListTag $$2 = compoundTag0.getList("CustomPotionEffects", 10);
            for (int $$3 = 0; $$3 < $$2.size(); $$3++) {
                CompoundTag $$4 = $$2.getCompound($$3);
                MobEffectInstance $$5 = MobEffectInstance.load($$4);
                if ($$5 != null) {
                    listMobEffectInstance1.add($$5);
                }
            }
        }
    }

    public static int getColor(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        if ($$1 != null && $$1.contains("CustomPotionColor", 99)) {
            return $$1.getInt("CustomPotionColor");
        } else {
            return getPotion(itemStack0) == Potions.EMPTY ? 16253176 : getColor(getMobEffects(itemStack0));
        }
    }

    public static int getColor(Potion potion0) {
        return potion0 == Potions.EMPTY ? 16253176 : getColor(potion0.getEffects());
    }

    public static int getColor(Collection<MobEffectInstance> collectionMobEffectInstance0) {
        int $$1 = 3694022;
        if (collectionMobEffectInstance0.isEmpty()) {
            return 3694022;
        } else {
            float $$2 = 0.0F;
            float $$3 = 0.0F;
            float $$4 = 0.0F;
            int $$5 = 0;
            for (MobEffectInstance $$6 : collectionMobEffectInstance0) {
                if ($$6.isVisible()) {
                    int $$7 = $$6.getEffect().getColor();
                    int $$8 = $$6.getAmplifier() + 1;
                    $$2 += (float) ($$8 * ($$7 >> 16 & 0xFF)) / 255.0F;
                    $$3 += (float) ($$8 * ($$7 >> 8 & 0xFF)) / 255.0F;
                    $$4 += (float) ($$8 * ($$7 >> 0 & 0xFF)) / 255.0F;
                    $$5 += $$8;
                }
            }
            if ($$5 == 0) {
                return 0;
            } else {
                $$2 = $$2 / (float) $$5 * 255.0F;
                $$3 = $$3 / (float) $$5 * 255.0F;
                $$4 = $$4 / (float) $$5 * 255.0F;
                return (int) $$2 << 16 | (int) $$3 << 8 | (int) $$4;
            }
        }
    }

    public static Potion getPotion(ItemStack itemStack0) {
        return getPotion(itemStack0.getTag());
    }

    public static Potion getPotion(@Nullable CompoundTag compoundTag0) {
        return compoundTag0 == null ? Potions.EMPTY : Potion.byName(compoundTag0.getString("Potion"));
    }

    public static ItemStack setPotion(ItemStack itemStack0, Potion potion1) {
        ResourceLocation $$2 = BuiltInRegistries.POTION.getKey(potion1);
        if (potion1 == Potions.EMPTY) {
            itemStack0.removeTagKey("Potion");
        } else {
            itemStack0.getOrCreateTag().putString("Potion", $$2.toString());
        }
        return itemStack0;
    }

    public static ItemStack setCustomEffects(ItemStack itemStack0, Collection<MobEffectInstance> collectionMobEffectInstance1) {
        if (collectionMobEffectInstance1.isEmpty()) {
            return itemStack0;
        } else {
            CompoundTag $$2 = itemStack0.getOrCreateTag();
            ListTag $$3 = $$2.getList("CustomPotionEffects", 9);
            for (MobEffectInstance $$4 : collectionMobEffectInstance1) {
                $$3.add($$4.save(new CompoundTag()));
            }
            $$2.put("CustomPotionEffects", $$3);
            return itemStack0;
        }
    }

    public static void addPotionTooltip(ItemStack itemStack0, List<Component> listComponent1, float float2) {
        addPotionTooltip(getMobEffects(itemStack0), listComponent1, float2);
    }

    public static void addPotionTooltip(List<MobEffectInstance> listMobEffectInstance0, List<Component> listComponent1, float float2) {
        List<Pair<Attribute, AttributeModifier>> $$3 = Lists.newArrayList();
        if (listMobEffectInstance0.isEmpty()) {
            listComponent1.add(NO_EFFECT);
        } else {
            for (MobEffectInstance $$4 : listMobEffectInstance0) {
                MutableComponent $$5 = Component.translatable($$4.getDescriptionId());
                MobEffect $$6 = $$4.getEffect();
                Map<Attribute, AttributeModifier> $$7 = $$6.getAttributeModifiers();
                if (!$$7.isEmpty()) {
                    for (Entry<Attribute, AttributeModifier> $$8 : $$7.entrySet()) {
                        AttributeModifier $$9 = (AttributeModifier) $$8.getValue();
                        AttributeModifier $$10 = new AttributeModifier($$9.getName(), $$6.getAttributeModifierValue($$4.getAmplifier(), $$9), $$9.getOperation());
                        $$3.add(new Pair((Attribute) $$8.getKey(), $$10));
                    }
                }
                if ($$4.getAmplifier() > 0) {
                    $$5 = Component.translatable("potion.withAmplifier", $$5, Component.translatable("potion.potency." + $$4.getAmplifier()));
                }
                if (!$$4.endsWithin(20)) {
                    $$5 = Component.translatable("potion.withDuration", $$5, MobEffectUtil.formatDuration($$4, float2));
                }
                listComponent1.add($$5.withStyle($$6.getCategory().getTooltipFormatting()));
            }
        }
        if (!$$3.isEmpty()) {
            listComponent1.add(CommonComponents.EMPTY);
            listComponent1.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
            for (Pair<Attribute, AttributeModifier> $$11 : $$3) {
                AttributeModifier $$12 = (AttributeModifier) $$11.getSecond();
                double $$13 = $$12.getAmount();
                double $$15;
                if ($$12.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && $$12.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                    $$15 = $$12.getAmount();
                } else {
                    $$15 = $$12.getAmount() * 100.0;
                }
                if ($$13 > 0.0) {
                    listComponent1.add(Component.translatable("attribute.modifier.plus." + $$12.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format($$15), Component.translatable(((Attribute) $$11.getFirst()).getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if ($$13 < 0.0) {
                    $$15 *= -1.0;
                    listComponent1.add(Component.translatable("attribute.modifier.take." + $$12.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format($$15), Component.translatable(((Attribute) $$11.getFirst()).getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }
}