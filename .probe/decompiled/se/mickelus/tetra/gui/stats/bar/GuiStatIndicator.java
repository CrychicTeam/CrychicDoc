package se.mickelus.tetra.gui.stats.bar;

import java.util.Arrays;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;

@ParametersAreNonnullByDefault
public class GuiStatIndicator extends GuiTexture {

    protected String label;

    protected IStatGetter statGetter;

    protected IStatGetter[] showRequirements = new IStatGetter[0];

    protected ITooltipGetter tooltipGetter;

    public GuiStatIndicator(int x, int y, String label, int textureIndex, IStatGetter statGetter, ITooltipGetter tooltipGetter) {
        this(x, y, label, textureIndex * 8, 160, GuiTextures.glyphs, statGetter, tooltipGetter);
    }

    public GuiStatIndicator(int x, int y, String label, int textureX, int textureY, ResourceLocation textureLocation, IStatGetter statGetter, ITooltipGetter tooltipGetter) {
        super(x, y, 8, 8, textureX, textureY, textureLocation);
        this.label = label;
        this.statGetter = statGetter;
        this.tooltipGetter = tooltipGetter;
    }

    public GuiStatIndicator withShowRequirements(IStatGetter... statGetters) {
        this.showRequirements = statGetters;
        return this;
    }

    public boolean update(Player player, ItemStack currentStack, ItemStack previewStack, @Nullable String slot, @Nullable String improvement) {
        if (this.statGetter.shouldShow(player, currentStack, previewStack) && Arrays.stream(this.showRequirements).allMatch(getter -> getter.shouldShow(player, currentStack, previewStack))) {
            double value;
            double diffValue;
            if (!previewStack.isEmpty()) {
                value = this.statGetter.getValue(player, currentStack);
                diffValue = this.statGetter.getValue(player, previewStack);
            } else {
                value = this.statGetter.getValue(player, currentStack);
                if (slot != null) {
                    diffValue = value;
                    if (improvement != null) {
                        value -= this.statGetter.getValue(player, currentStack, slot, improvement);
                    } else {
                        value -= this.statGetter.getValue(player, currentStack, slot);
                    }
                } else {
                    diffValue = value;
                }
            }
            double baseValue = this.statGetter.getValue(player, ItemStack.EMPTY);
            this.setColor(this.getDiffColor(baseValue, value, diffValue));
            return true;
        } else {
            return false;
        }
    }

    public boolean isActive(Player player, ItemStack itemStack) {
        return this.statGetter.shouldShow(player, itemStack, itemStack) && Arrays.stream(this.showRequirements).allMatch(getter -> getter.shouldShow(player, itemStack, itemStack));
    }

    protected int getDiffColor(double baseValue, double value, double diffValue) {
        if (diffValue > baseValue && value <= baseValue) {
            return 5635925;
        } else if (diffValue <= baseValue && value > baseValue) {
            return 16733525;
        } else {
            return diffValue == value ? 16777215 : 11184895;
        }
    }

    public String getLabel() {
        return I18n.get(this.label);
    }

    public String getTooltipBase(Player player, ItemStack itemStack) {
        return this.tooltipGetter.getTooltipBase(player, itemStack);
    }

    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return this.tooltipGetter.hasExtendedTooltip(player, itemStack);
    }

    public String getTooltipExtension(Player player, ItemStack itemStack) {
        return this.tooltipGetter.getTooltipExtension(player, itemStack);
    }
}