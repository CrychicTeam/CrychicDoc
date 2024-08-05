package se.mickelus.tetra.gui.stats.bar;

import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringSmall;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.gui.stats.getter.ILabelGetter;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterIntegrity;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class GuiStatBarIntegrity extends GuiStatBase {

    protected double max = 9.0;

    protected GuiString labelString = new GuiStringSmall(0, 0, I18n.get("tetra.stats.integrity"));

    protected GuiString valueString;

    protected GuiBar barPositive;

    protected GuiBar barNegative;

    protected List<Component> tooltip;

    protected IStatGetter statGetter;

    protected ILabelGetter labelGetter;

    protected ITooltipGetter tooltipGetterPositive;

    protected ITooltipGetter tooltipGetterNegative;

    public GuiStatBarIntegrity(int x, int y) {
        super(x, y, 59, 12);
        this.addChild(this.labelString);
        this.valueString = new GuiStringSmall(0, 0, "");
        this.valueString.setAttachment(GuiAttachment.topRight);
        this.addChild(this.valueString);
        this.barNegative = new GuiBarSegmented(-1, 0, 27, 0.0, this.max, true);
        this.barNegative.setAlignment(GuiAlignment.right);
        this.addChild(this.barNegative);
        this.barPositive = new GuiBarSegmented(1, 0, 27, 0.0, this.max);
        this.barPositive.setAttachment(GuiAttachment.topRight);
        this.addChild(this.barPositive);
        this.addChild(new GuiRect(29, 5, 1, 3, 8355711));
        this.statGetter = new StatGetterIntegrity();
        this.labelGetter = LabelGetterBasic.integerLabel;
        this.tooltipGetterPositive = new TooltipGetterInteger("tetra.stats.integrity.tooltip_positive", this.statGetter);
        this.tooltipGetterNegative = new TooltipGetterInteger("tetra.stats.integrity.tooltip_negative", this.statGetter, true);
    }

    @Override
    public void setAlignment(GuiAlignment alignment) {
    }

    @Override
    public void update(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        double value;
        double diffValue;
        if (!previewStack.isEmpty()) {
            value = this.statGetter.getValue(player, currentStack);
            diffValue = this.statGetter.getValue(player, previewStack);
        } else {
            value = this.statGetter.getValue(player, currentStack);
            if (slot != null) {
                diffValue = value;
                value -= this.getSlotValue(player, currentStack, slot, improvement);
            } else {
                diffValue = value;
            }
        }
        if (value < 0.0) {
            this.tooltip = Collections.singletonList(Component.literal(this.tooltipGetterNegative.getTooltip(player, previewStack)));
        } else {
            this.tooltip = Collections.singletonList(Component.literal(this.tooltipGetterPositive.getTooltip(player, previewStack)));
        }
        this.updateValue(value, diffValue);
        this.labelString.setString(I18n.get("tetra.stats.integrity"));
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        return this.statGetter.shouldShow(player, currentStack, previewStack);
    }

    protected double getSlotValue(Player player, ItemStack itemStack, String slot, String improvement) {
        return (Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> improvement != null ? this.statGetter.getValue(player, itemStack, slot, improvement) : this.statGetter.getValue(player, itemStack, slot)).orElse(0.0);
    }

    public void updateValue(double value, double diffValue) {
        this.barNegative.setValue(value > 0.0 ? 0.0 : -value, diffValue > 0.0 ? 0.0 : -diffValue);
        this.barPositive.setValue(value < 0.0 ? 0.0 : value, diffValue < 0.0 ? 0.0 : diffValue);
        this.updateValueLabel(value, diffValue);
    }

    private void updateValueLabel(double value, double diffValue) {
        this.valueString.setString(this.labelGetter.getLabel(value, diffValue, false));
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : super.getTooltipLines();
    }
}