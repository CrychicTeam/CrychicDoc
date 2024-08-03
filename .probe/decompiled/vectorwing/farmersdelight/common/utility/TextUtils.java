package vectorwing.farmersdelight.common.utility;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;

public class TextUtils {

    private static final MutableComponent NO_EFFECTS = Component.translatable("effect.none").withStyle(ChatFormatting.GRAY);

    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable("farmersdelight." + key, args);
    }

    public static void addFoodEffectTooltip(ItemStack itemIn, List<Component> lores, float durationFactor) {
        FoodProperties foodStats = itemIn.getItem().getFoodProperties();
        if (foodStats != null) {
            List<Pair<MobEffectInstance, Float>> effectList = foodStats.getEffects();
            List<Pair<Attribute, AttributeModifier>> attributeList = Lists.newArrayList();
            if (effectList.isEmpty()) {
                lores.add(NO_EFFECTS);
            } else {
                for (Pair<MobEffectInstance, Float> effectPair : effectList) {
                    MobEffectInstance instance = (MobEffectInstance) effectPair.getFirst();
                    MutableComponent iformattabletextcomponent = Component.translatable(instance.getDescriptionId());
                    MobEffect effect = instance.getEffect();
                    Map<Attribute, AttributeModifier> attributeMap = effect.getAttributeModifiers();
                    if (!attributeMap.isEmpty()) {
                        for (Entry<Attribute, AttributeModifier> entry : attributeMap.entrySet()) {
                            AttributeModifier rawModifier = (AttributeModifier) entry.getValue();
                            AttributeModifier modifier = new AttributeModifier(rawModifier.getName(), effect.getAttributeModifierValue(instance.getAmplifier(), rawModifier), rawModifier.getOperation());
                            attributeList.add(new Pair((Attribute) entry.getKey(), modifier));
                        }
                    }
                    if (instance.getAmplifier() > 0) {
                        iformattabletextcomponent = Component.translatable("potion.withAmplifier", iformattabletextcomponent, Component.translatable("potion.potency." + instance.getAmplifier()));
                    }
                    if (instance.getDuration() > 20) {
                        iformattabletextcomponent = Component.translatable("potion.withDuration", iformattabletextcomponent, MobEffectUtil.formatDuration(instance, durationFactor));
                    }
                    lores.add(iformattabletextcomponent.withStyle(effect.getCategory().getTooltipFormatting()));
                }
            }
            if (!attributeList.isEmpty()) {
                lores.add(CommonComponents.EMPTY);
                lores.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));
                for (Pair<Attribute, AttributeModifier> pair : attributeList) {
                    AttributeModifier modifier = (AttributeModifier) pair.getSecond();
                    double amount = modifier.getAmount();
                    double formattedAmount;
                    if (modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_BASE && modifier.getOperation() != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                        formattedAmount = modifier.getAmount();
                    } else {
                        formattedAmount = modifier.getAmount() * 100.0;
                    }
                    if (amount > 0.0) {
                        lores.add(Component.translatable("attribute.modifier.plus." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute) pair.getFirst()).getDescriptionId())).withStyle(ChatFormatting.BLUE));
                    } else if (amount < 0.0) {
                        formattedAmount *= -1.0;
                        lores.add(Component.translatable("attribute.modifier.take." + modifier.getOperation().toValue(), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount), Component.translatable(((Attribute) pair.getFirst()).getDescriptionId())).withStyle(ChatFormatting.RED));
                    }
                }
            }
        }
    }
}