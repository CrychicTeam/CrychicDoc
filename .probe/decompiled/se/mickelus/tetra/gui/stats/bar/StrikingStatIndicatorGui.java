package se.mickelus.tetra.gui.stats.bar;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterStriking;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterNone;

@ParametersAreNonnullByDefault
public class StrikingStatIndicatorGui extends GuiStatIndicator {

    GuiStatIndicator striking;

    GuiStatIndicator sweeping;

    GuiStatIndicator current;

    public StrikingStatIndicatorGui(ToolAction toolAction) {
        super(0, 0, "", 0, null, null);
        this.striking = new GuiStatIndicator(0, 0, "tetra.stats.tool.striking", 0, new StatGetterStriking(toolAction), new TooltipGetterNone("tetra.stats.tool.striking.tooltip"));
        this.sweeping = new GuiStatIndicator(0, 0, "tetra.stats.tool.sweeping", 1, new StatGetterEffectLevel(ItemEffect.sweepingStrike, 1.0), new TooltipGetterNone("tetra.stats.tool.sweeping.tooltip"));
    }

    @Override
    public boolean update(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        if (this.striking != null && this.striking.update(player, currentStack, previewStack, slot, improvement)) {
            if (this.sweeping.update(player, currentStack, previewStack, slot, improvement)) {
                this.current = this.sweeping;
            } else {
                this.current = this.striking;
            }
            return true;
        } else {
            this.current = null;
            return false;
        }
    }

    @Override
    public boolean isActive(Player player, ItemStack itemStack) {
        return this.current != null && this.current.isActive(player, itemStack);
    }

    @Override
    protected int getDiffColor(double baseValue, double value, double diffValue) {
        return (Integer) Optional.ofNullable(this.current).map(c -> c.getDiffColor(baseValue, value, diffValue)).orElse(16777215);
    }

    @Override
    public String getLabel() {
        return (String) Optional.ofNullable(this.current).map(GuiStatIndicator::getLabel).orElse("");
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        return (String) Optional.ofNullable(this.current).map(c -> c.getTooltipBase(player, itemStack)).orElse("");
    }

    @Override
    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return (Boolean) Optional.ofNullable(this.current).map(c -> c.hasExtendedTooltip(player, itemStack)).orElse(false);
    }

    @Override
    public String getTooltipExtension(Player player, ItemStack itemStack) {
        return (String) Optional.ofNullable(this.current).map(c -> c.getTooltipExtension(player, itemStack)).orElse("");
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.current != null) {
            this.current.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        }
    }
}