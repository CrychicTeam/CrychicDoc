package dev.xkmc.l2hostility.content.traits.base;

import dev.xkmc.l2hostility.content.logic.TraitManager;
import java.util.List;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeTrait extends MobTrait {

    private final AttributeTrait.AttributeEntry[] entries;

    public AttributeTrait(ChatFormatting style, AttributeTrait.AttributeEntry... entries) {
        super(style);
        this.entries = entries;
    }

    @Override
    public void initialize(LivingEntity le, int level) {
        for (AttributeTrait.AttributeEntry e : this.entries) {
            TraitManager.addAttribute(le, (Attribute) e.attribute.get(), e.name(), e.factor.getAsDouble() * (double) level, e.op());
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        for (AttributeTrait.AttributeEntry e : this.entries) {
            double val = e.factor.getAsDouble();
            if (val != 0.0) {
                list.add(this.mapLevel(i -> (e.op == AttributeModifier.Operation.ADDITION ? Component.literal("+" + Math.round(val * (double) i.intValue())) : Component.literal("+" + Math.round(val * (double) i.intValue() * 100.0) + "%")).withStyle(ChatFormatting.AQUA)).append(CommonComponents.SPACE).append(Component.translatable(((Attribute) e.attribute.get()).getDescriptionId()).withStyle(ChatFormatting.BLUE)));
            }
        }
    }

    public static record AttributeEntry(String name, Supplier<Attribute> attribute, DoubleSupplier factor, AttributeModifier.Operation op) {
    }
}