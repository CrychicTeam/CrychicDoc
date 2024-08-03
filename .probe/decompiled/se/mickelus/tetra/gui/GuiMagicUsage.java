package se.mickelus.tetra.gui;

import java.util.Arrays;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.gui.stats.bar.GuiBar;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class GuiMagicUsage extends GuiElement {

    protected GuiString valueString;

    protected GuiBar bar;

    protected List<Component> tooltip;

    protected List<Component> tooltipExtended;

    public GuiMagicUsage(int x, int y, int barLength) {
        super(x, y, barLength, 12);
        this.addChild(new GuiStringSmall(0, 0, I18n.get("item.tetra.modular.magic_capacity.label")));
        this.valueString = new GuiStringSmall(0, 0, "");
        this.valueString.setAttachment(GuiAttachment.topRight);
        this.addChild(this.valueString);
        this.bar = new GuiBar(0, 0, barLength, 0.0, 0.0);
        this.addChild(this.bar);
    }

    private static int getGain(ItemStack itemStack, String slot) {
        return (Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getMagicCapacityGain(itemStack)).orElse(0);
    }

    private static int getCost(ItemStack itemStack, String slot) {
        return (Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getMagicCapacityCost(itemStack)).orElse(0);
    }

    private static float getDestabilizeChance(ItemStack itemStack, String slot) {
        return (Float) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getDestabilizationChance(itemStack, 1.0F)).orElse(0.0F);
    }

    private static int getExperienceCost(ItemStack itemStack, String slot) {
        return (Integer) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> item.getModuleFromSlot(itemStack, slot)).map(module -> module.getRepairExperienceCost(itemStack, ItemStack.EMPTY)).orElse(0);
    }

    public void update(ItemStack itemStack, ItemStack previewStack, String slot) {
        if (!previewStack.isEmpty()) {
            int value = getCost(itemStack, slot);
            int diffValue = getCost(previewStack, slot) - value;
            int max = getGain(itemStack, slot);
            int diffMax = getGain(previewStack, slot) - max;
            int risk = Math.round(getDestabilizeChance(previewStack, slot) * 100.0F);
            int xpCost = getExperienceCost(previewStack, slot);
            this.bar.setMax((double) Math.max(max + diffMax, max));
            this.tooltip = Arrays.asList(Component.translatable("item.tetra.modular.magic_capacity.description", max, value + diffValue, xpCost, risk), Component.literal(""), Tooltips.expand);
            this.tooltipExtended = Arrays.asList(Component.translatable("item.tetra.modular.magic_capacity.description", max, value + diffValue, xpCost, risk), Component.literal(""), Tooltips.expanded, Component.translatable("item.tetra.modular.magic_capacity.description_extended"));
            if (diffMax != 0) {
                this.bar.setValue((double) max, (double) (max + diffMax));
                this.valueString.setString(String.format("%s(%+d)%s %d/%d", diffMax < 0 ? ChatFormatting.RED : ChatFormatting.GREEN, diffMax, ChatFormatting.RESET, max + diffMax, max + diffMax));
            } else if (diffValue != 0) {
                this.bar.setValue((double) (max - value), (double) (max - value - diffValue));
                this.valueString.setString(String.format("%s(%+d)%s %d/%d", diffValue > 0 ? ChatFormatting.RED : ChatFormatting.GREEN, -diffValue, ChatFormatting.RESET, max - value - diffValue, max));
            } else {
                this.bar.setValue((double) (max - value), (double) (max - value));
                this.valueString.setString(String.format("%d/%d", max - value, max));
            }
        } else {
            int value = getCost(itemStack, slot);
            int max = getGain(itemStack, slot);
            int risk = Math.round(getDestabilizeChance(itemStack, slot) * 100.0F);
            int xpCost = getExperienceCost(itemStack, slot);
            this.tooltip = Arrays.asList(Component.translatable("item.tetra.modular.magic_capacity.description", max, value, xpCost, risk), Component.literal(""), Tooltips.expand);
            this.tooltipExtended = Arrays.asList(Component.translatable("item.tetra.modular.magic_capacity.description", max, value, xpCost, risk), Component.literal(""), Tooltips.expanded, Component.translatable("item.tetra.modular.magic_capacity.description_extended"));
            this.valueString.setString(String.format("%d/%d", max - value, max));
            this.bar.setMax((double) max);
            this.bar.setValue((double) (max - value), (double) (max - value));
        }
    }

    public boolean hasChanged(ItemStack itemStack, ItemStack previewStack, String slot) {
        return !previewStack.isEmpty() && (getCost(itemStack, slot) != getCost(previewStack, slot) || getGain(itemStack, slot) != getGain(previewStack, slot));
    }

    public boolean providesCapacity(ItemStack itemStack, ItemStack previewStack, String slot) {
        return getGain(itemStack, slot) > 0 || getGain(previewStack, slot) > 0;
    }

    @Override
    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return Screen.hasShiftDown() ? this.tooltipExtended : this.tooltip;
        } else {
            return super.getTooltipLines();
        }
    }
}