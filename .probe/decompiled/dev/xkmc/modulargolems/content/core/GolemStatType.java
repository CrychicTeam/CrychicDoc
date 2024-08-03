package dev.xkmc.modulargolems.content.core;

import dev.xkmc.l2library.base.NamedEntry;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;

public class GolemStatType extends NamedEntry<GolemStatType> {

    private final Supplier<Attribute> attribute;

    public final GolemStatType.Kind kind;

    public final StatFilterType type;

    public GolemStatType(Supplier<Attribute> attribute, GolemStatType.Kind kind, StatFilterType type) {
        super(GolemTypes.STAT_TYPES);
        this.attribute = attribute;
        this.kind = kind;
        this.type = type;
    }

    public Attribute getAttribute() {
        return (Attribute) this.attribute.get();
    }

    public MutableComponent getAdderTooltip(double val) {
        if (this.kind == GolemStatType.Kind.PERCENT) {
            val *= 100.0;
        }
        String key = "attribute.modifier." + (val < 0.0 ? "take." : "plus.") + (this.kind == GolemStatType.Kind.PERCENT ? 1 : 0);
        return Component.translatable(key, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(val)), Component.translatable(((Attribute) this.attribute.get()).getDescriptionId())).withStyle(ChatFormatting.BLUE);
    }

    public MutableComponent getTotalTooltip(double val) {
        if (this.kind == GolemStatType.Kind.PERCENT) {
            val *= 100.0;
        }
        String key = "attribute.modifier." + (val < 0.0 ? "take." : (this.kind == GolemStatType.Kind.BASE ? "equals." : "plus.")) + (this.kind == GolemStatType.Kind.PERCENT ? 1 : 0);
        return Component.translatable(key, ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(Math.abs(val)), Component.translatable(((Attribute) this.attribute.get()).getDescriptionId())).withStyle(ChatFormatting.BLUE);
    }

    public void applyToEntity(LivingEntity e, double v) {
        AttributeInstance ins = e.getAttribute((Attribute) this.attribute.get());
        if (ins != null) {
            switch(this.kind) {
                case BASE:
                    ins.setBaseValue(v);
                    break;
                case ADD:
                    ins.setBaseValue(ins.getValue() + v);
                    break;
                case PERCENT:
                    ins.setBaseValue(ins.getValue() * (1.0 + v));
            }
        }
    }

    public static enum Kind {

        BASE, ADD, PERCENT
    }
}