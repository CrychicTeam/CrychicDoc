package io.redspace.ironsspellbooks.item.curios;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class SimpleDescriptiveCurio extends CurioBaseItem {

    @Nullable
    final String slotIdentifier;

    Style descriptionStyle;

    boolean showHeader;

    public SimpleDescriptiveCurio(Item.Properties properties, String slotIdentifier) {
        super(properties);
        this.slotIdentifier = slotIdentifier;
        this.showHeader = true;
        this.descriptionStyle = Style.EMPTY.withColor(ChatFormatting.YELLOW);
    }

    public SimpleDescriptiveCurio(Item.Properties properties) {
        this(properties, null);
    }

    @Override
    public List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        if (this.slotIdentifier != null) {
            MutableComponent title = Component.translatable("curios.modifiers." + this.slotIdentifier).withStyle(ChatFormatting.GOLD);
            if (this.showHeader) {
                tooltips.add(Component.empty());
                tooltips.add(title);
            }
            tooltips.addAll(this.getDescriptionLines(stack));
        }
        return super.getSlotsTooltip(tooltips, stack);
    }

    public List<Component> getDescriptionLines(ItemStack stack) {
        return List.of(this.getDescription(stack));
    }

    public Component getDescription(ItemStack stack) {
        return Component.literal(" ").append(Component.translatable(this.m_5524_() + ".desc")).withStyle(this.descriptionStyle);
    }
}