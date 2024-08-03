package dev.shadowsoffire.attributeslib.impl;

import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;

public class BooleanAttribute extends Attribute implements IFormattableAttribute {

    public BooleanAttribute(String pDescriptionId, boolean defaultValue) {
        super(pDescriptionId, defaultValue ? 1.0 : 0.0);
    }

    @Override
    public MutableComponent toValueComponent(@Nullable AttributeModifier.Operation op, double value, TooltipFlag flag) {
        if (op == null) {
            return Component.translatable("attributeslib.value.boolean." + (value > 0.0 ? "enabled" : "disabled"));
        } else if (op == AttributeModifier.Operation.ADDITION && (int) value == 1) {
            return Component.translatable("attributeslib.value.boolean.enable");
        } else {
            return op == AttributeModifier.Operation.MULTIPLY_TOTAL && (int) value == -1 ? Component.translatable("attributeslib.value.boolean.force_disable") : Component.translatable("attributeslib.value.boolean.invalid");
        }
    }

    @Override
    public MutableComponent toComponent(AttributeModifier modif, TooltipFlag flag) {
        Attribute attr = this.ths();
        double value = modif.getAmount();
        MutableComponent comp;
        if (value > 0.0) {
            comp = Component.translatable("attributeslib.modifier.bool", this.toValueComponent(modif.getOperation(), value, flag), Component.translatable(attr.getDescriptionId())).withStyle(ChatFormatting.BLUE);
        } else {
            value *= -1.0;
            comp = Component.translatable("attributeslib.modifier.bool", this.toValueComponent(modif.getOperation(), value, flag), Component.translatable(attr.getDescriptionId())).withStyle(ChatFormatting.RED);
        }
        return comp.append(this.getDebugInfo(modif, flag));
    }

    @Override
    public double sanitizeValue(double value) {
        return Math.max(value, 0.0);
    }
}