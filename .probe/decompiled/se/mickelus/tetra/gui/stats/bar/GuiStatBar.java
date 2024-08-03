package se.mickelus.tetra.gui.stats.bar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.gui.stats.getter.ILabelGetter;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class GuiStatBar extends GuiStatBase {

    protected double min;

    protected double max;

    protected String labelKey;

    protected GuiString labelString;

    protected GuiString valueString;

    protected GuiBar bar;

    protected GuiHorizontalLayoutGroup indicatorGroup;

    protected GuiStatIndicator[] indicators = new GuiStatIndicator[0];

    protected List<Component> tooltip;

    protected List<Component> extendedTooltip;

    protected GuiAlignment alignment = GuiAlignment.left;

    protected boolean inverted;

    protected IStatGetter statGetter;

    protected ILabelGetter labelGetter;

    protected ITooltipGetter tooltipGetter;

    public GuiStatBar(int x, int y, int barLength, String labelKey, double min, double max, boolean segmented, IStatGetter statGetter, ILabelGetter labelGetter, ITooltipGetter tooltipGetter) {
        this(x, y, barLength, labelKey, min, max, segmented, false, false, statGetter, labelGetter, tooltipGetter);
    }

    public GuiStatBar(int x, int y, int barLength, String labelKey, double min, double max, boolean segmented, boolean split, boolean inverted, IStatGetter statGetter, ILabelGetter labelGetter, ITooltipGetter tooltipGetter) {
        super(x, y, barLength, 12);
        this.min = min;
        this.max = max;
        this.labelKey = labelKey;
        this.labelString = new GuiStringSmall(0, 0, "");
        this.valueString = new GuiStringSmall(0, 0, "");
        if (segmented) {
            this.bar = new GuiBarSegmented(0, 0, barLength + 1, min, max, inverted);
        } else if (split) {
            this.bar = new GuiBarSplit(0, 0, barLength, max, inverted);
        } else {
            this.bar = new GuiBar(0, 0, barLength, min, max, inverted);
        }
        this.indicatorGroup = new GuiHorizontalLayoutGroup(0, -2, 7, 1);
        this.addChild(this.labelString);
        this.addChild(this.valueString);
        this.addChild(this.bar);
        this.addChild(this.indicatorGroup);
        this.statGetter = statGetter;
        this.labelGetter = labelGetter;
        this.tooltipGetter = tooltipGetter;
        this.inverted = inverted;
    }

    public GuiStatBar setIndicators(GuiStatIndicator... indicators) {
        this.indicators = indicators;
        return this;
    }

    @Override
    public void setAlignment(GuiAlignment alignment) {
        this.alignment = alignment;
        this.realign();
    }

    protected void realign() {
        this.bar.setAlignment(this.alignment);
        this.labelString.setAttachment(this.alignment.toAttachment());
        this.valueString.setAttachment(this.alignment.toAttachment().flipHorizontal());
        this.indicatorGroup.setAttachment(this.alignment.toAttachment());
        int offset = this.labelString.getWidth() + 2;
        this.indicatorGroup.setX(GuiAlignment.right.equals(this.alignment) ? -offset : offset);
    }

    @Override
    public void update(Player player, ItemStack currentStack, ItemStack previewStack, @Nullable String slot, @Nullable String improvement) {
        if (this.labelKey != null) {
            this.labelString.setString(I18n.get(this.labelKey));
        }
        this.labelString.setVisible(this.labelKey != null);
        double value;
        double diffValue;
        if (!previewStack.isEmpty()) {
            value = this.statGetter.getValue(player, currentStack);
            diffValue = this.statGetter.getValue(player, previewStack);
            this.tooltip = this.getCombinedTooltip(player, previewStack);
            this.extendedTooltip = this.getCombinedTooltipExtended(player, previewStack);
        } else {
            value = this.statGetter.getValue(player, currentStack);
            if (slot != null) {
                diffValue = value;
                value -= this.getSlotValue(player, currentStack, slot, improvement);
            } else {
                diffValue = value;
            }
            this.tooltip = this.getCombinedTooltip(player, currentStack);
            this.extendedTooltip = this.getCombinedTooltipExtended(player, currentStack);
        }
        this.updateValue(value, diffValue);
        this.updateIndicators(player, currentStack, previewStack, slot, improvement);
    }

    protected void updateIndicators(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        this.indicatorGroup.clearChildren();
        for (int i = 0; i < this.indicators.length; i++) {
            if (this.indicators[i].update(player, currentStack, previewStack, slot, improvement)) {
                this.indicatorGroup.addChild(this.indicators[i]);
            }
        }
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        return this.statGetter.shouldShow(player, currentStack, previewStack);
    }

    protected double getSlotValue(Player player, ItemStack itemStack, @Nullable String slot, @Nullable String improvement) {
        return (Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> improvement != null ? this.statGetter.getValue(player, itemStack, slot, improvement) : this.statGetter.getValue(player, itemStack, slot)).orElse(0.0);
    }

    public void updateValue(double value, double diffValue) {
        this.bar.setValue(value, diffValue);
        this.updateValueLabel(value, diffValue);
    }

    private void updateValueLabel(double value, double diffValue) {
        this.valueString.setString(this.labelGetter.getLabel(value, diffValue, this.alignment == GuiAlignment.right));
    }

    @Override
    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return Screen.hasShiftDown() ? this.extendedTooltip : this.tooltip;
        } else {
            return super.getTooltipLines();
        }
    }

    protected List<GuiStatIndicator> getActiveIndicators() {
        return this.indicatorGroup.getChildren(GuiStatIndicator.class);
    }

    protected String getCombinedTooltipBase(Player player, ItemStack itemStack) {
        String tooltip = this.tooltipGetter.getTooltipBase(player, itemStack);
        return tooltip + ((String) this.getActiveIndicators().stream().filter(indicator -> indicator.isActive(player, itemStack)).map(indicator -> ChatFormatting.YELLOW + indicator.getLabel() + "\n" + ChatFormatting.GRAY + indicator.getTooltipBase(player, itemStack)).map(string -> "\n \n" + string).collect(Collectors.joining())).replace(ChatFormatting.RESET.toString(), ChatFormatting.GRAY.toString());
    }

    protected List<Component> getCombinedTooltip(Player player, ItemStack itemStack) {
        List<Component> result = new ArrayList();
        Arrays.stream(this.getCombinedTooltipBase(player, itemStack).split("\\\\n")).map(Component::m_237113_).forEach(result::add);
        if (this.tooltipGetter.hasExtendedTooltip(player, itemStack) || this.getActiveIndicators().stream().anyMatch(ind -> ind.hasExtendedTooltip(player, itemStack))) {
            result.add(Component.literal(" "));
            result.add(Tooltips.expand);
        }
        return result;
    }

    protected List<Component> getCombinedTooltipExtended(Player player, ItemStack itemStack) {
        String tooltip = this.getCombinedTooltipBase(player, itemStack);
        List<Component> result = new ArrayList();
        Arrays.stream(this.getCombinedTooltipBase(player, itemStack).split("\\\\n")).map(Component::m_237113_).forEach(result::add);
        boolean hasExtendedTooltip = this.tooltipGetter.hasExtendedTooltip(player, itemStack);
        if (hasExtendedTooltip || this.getActiveIndicators().stream().anyMatch(ind -> ind.hasExtendedTooltip(player, itemStack))) {
            result.add(Component.literal(" "));
            result.add(Tooltips.expanded);
            if (hasExtendedTooltip) {
                Arrays.stream(this.tooltipGetter.getTooltipExtension(player, itemStack).split("\\\\n")).map(Component::m_237113_).map(component -> component.withStyle(ChatFormatting.GRAY)).forEach(result::add);
            }
            boolean isFirst = true;
            for (GuiStatIndicator indicator : this.getActiveIndicators()) {
                if (indicator.hasExtendedTooltip(player, itemStack)) {
                    if (!isFirst || hasExtendedTooltip) {
                        result.add(Component.literal(" "));
                    }
                    result.add(Component.literal(indicator.getTooltipExtension(player, itemStack)).withStyle(ChatFormatting.GRAY));
                    isFirst = false;
                }
            }
        }
        return result;
    }
}