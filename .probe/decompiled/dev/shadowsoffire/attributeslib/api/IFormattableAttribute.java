package dev.shadowsoffire.attributeslib.api;

import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

public interface IFormattableAttribute {

    default MutableComponent toValueComponent(@Nullable AttributeModifier.Operation op, double value, TooltipFlag flag) {
        if (this == Attributes.KNOCKBACK_RESISTANCE || this == ForgeMod.SWIM_SPEED.get()) {
            return Component.translatable("attributeslib.value.percent", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value * 100.0));
        } else if (this == Attributes.MOVEMENT_SPEED && isNullOrAddition(op)) {
            return Component.translatable("attributeslib.value.percent", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value * 1000.0));
        } else {
            String key = isNullOrAddition(op) ? "attributeslib.value.flat" : "attributeslib.value.percent";
            return Component.translatable(key, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(isNullOrAddition(op) ? value : value * 100.0));
        }
    }

    default MutableComponent toComponent(AttributeModifier modif, TooltipFlag flag) {
        Attribute attr = this.ths();
        double value = modif.getAmount();
        MutableComponent comp;
        if (value > 0.0) {
            comp = Component.translatable("attributeslib.modifier.plus", this.toValueComponent(modif.getOperation(), value, flag), Component.translatable(attr.getDescriptionId())).withStyle(ChatFormatting.BLUE);
        } else {
            value *= -1.0;
            comp = Component.translatable("attributeslib.modifier.take", this.toValueComponent(modif.getOperation(), value, flag), Component.translatable(attr.getDescriptionId())).withStyle(ChatFormatting.RED);
        }
        return comp.append(this.getDebugInfo(modif, flag));
    }

    default Component getDebugInfo(AttributeModifier modif, TooltipFlag flag) {
        Component debugInfo = CommonComponents.EMPTY;
        if (flag.isAdvanced()) {
            double advValue = (double) (modif.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL ? 1 : 0) + modif.getAmount();
            String valueStr = ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(advValue);
            String txt = switch(modif.getOperation()) {
                case ADDITION ->
                    advValue > 0.0 ? String.format("[+%s]", valueStr) : String.format("[%s]", valueStr);
                case MULTIPLY_BASE ->
                    advValue > 0.0 ? String.format("[+%sx]", valueStr) : String.format("[%sx]", valueStr);
                case MULTIPLY_TOTAL ->
                    String.format("[x%s]", valueStr);
            };
            debugInfo = Component.literal(" ").append(Component.literal(txt).withStyle(ChatFormatting.GRAY));
        }
        return debugInfo;
    }

    @Nullable
    default UUID getBaseUUID() {
        if (this == Attributes.ATTACK_DAMAGE) {
            return AttributeHelper.BASE_ATTACK_DAMAGE;
        } else if (this == Attributes.ATTACK_SPEED) {
            return AttributeHelper.BASE_ATTACK_SPEED;
        } else {
            return this == ForgeMod.ENTITY_REACH.get() ? AttributeHelper.BASE_ENTITY_REACH : null;
        }
    }

    default MutableComponent toBaseComponent(double value, double entityBase, boolean merged, TooltipFlag flag) {
        Attribute attr = this.ths();
        Component debugInfo = CommonComponents.EMPTY;
        if (flag.isAdvanced() && !merged) {
            debugInfo = Component.literal(" ").append(Component.translatable("attributeslib.adv.base", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(entityBase), ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value - entityBase)).withStyle(ChatFormatting.GRAY));
        }
        MutableComponent comp = Component.translatable("attribute.modifier.equals.0", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value), Component.translatable(attr.getDescriptionId()));
        return comp.append(debugInfo);
    }

    default double getBonusBaseValue(ItemStack stack) {
        return this == Attributes.ATTACK_DAMAGE ? (double) EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED) : 0.0;
    }

    default void addBonusTooltips(ItemStack stack, Consumer<Component> tooltip, TooltipFlag flag) {
        if (this == Attributes.ATTACK_DAMAGE) {
            float sharpness = EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
            Component debugInfo = CommonComponents.EMPTY;
            if (flag.isAdvanced()) {
                debugInfo = Component.literal(" ").append(Component.translatable("attributeslib.adv.sharpness_bonus", sharpness).withStyle(ChatFormatting.GRAY));
            }
            MutableComponent comp = AttributeHelper.list().append(Component.translatable("attribute.modifier.plus.0", ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format((double) sharpness), Component.translatable(this.ths().getDescriptionId())).withStyle(ChatFormatting.BLUE));
            tooltip.accept(comp.append(debugInfo));
        }
    }

    default Attribute ths() {
        return (Attribute) this;
    }

    static MutableComponent toComponent(Attribute attr, AttributeModifier modif, TooltipFlag flag) {
        return ((IFormattableAttribute) attr).toComponent(modif, flag);
    }

    static MutableComponent toValueComponent(Attribute attr, AttributeModifier.Operation op, double value, TooltipFlag flag) {
        return ((IFormattableAttribute) attr).toValueComponent(op, value, flag);
    }

    static MutableComponent toBaseComponent(Attribute attr, double value, double entityBase, boolean merged, TooltipFlag flag) {
        return ((IFormattableAttribute) attr).toBaseComponent(value, entityBase, merged, flag);
    }

    static boolean isNullOrAddition(@Nullable AttributeModifier.Operation op) {
        return op == null || op == AttributeModifier.Operation.ADDITION;
    }
}