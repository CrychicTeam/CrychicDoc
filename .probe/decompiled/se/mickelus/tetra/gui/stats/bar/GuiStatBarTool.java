package se.mickelus.tetra.gui.stats.bar;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolAction;
import se.mickelus.mutil.gui.GuiAlignment;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.blocks.workbench.gui.GuiTool;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterAttribute;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterEnchantmentLevel;
import se.mickelus.tetra.gui.stats.getter.StatGetterStriking;
import se.mickelus.tetra.gui.stats.getter.StatGetterSum;
import se.mickelus.tetra.gui.stats.getter.StatGetterToolCompoundEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterToolEfficiency;
import se.mickelus.tetra.gui.stats.getter.StatGetterToolLevel;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterInteger;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterNone;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterSweepingFocus;
import se.mickelus.tetra.gui.stats.getter.TooltipGetterTool;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class GuiStatBarTool extends GuiStatBar {

    private static final int efficiencyMax = 50;

    private final GuiTool icon;

    private final IStatGetter levelGetter;

    private final boolean efficiencyVisibility;

    public GuiStatBarTool(int x, int y, int width, ToolAction toolAction) {
        this(x, y, width, toolAction, false, true);
    }

    public GuiStatBarTool(int x, int y, int width, ToolAction toolAction, boolean efficiencyVisibility, boolean includeSpeedModifier) {
        super(x, y, width, null, 0.0, 50.0, false, (IStatGetter) (includeSpeedModifier ? new StatGetterToolCompoundEfficiency(new StatGetterToolEfficiency(toolAction), new StatGetterAttribute(Attributes.ATTACK_SPEED), new StatGetterEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, 1.0)) : new StatGetterSum(new StatGetterToolEfficiency(toolAction), new StatGetterEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, 1.0))), LabelGetterBasic.decimalLabel, new TooltipGetterTool(toolAction, includeSpeedModifier));
        this.efficiencyVisibility = efficiencyVisibility;
        this.bar.setWidth(width - 16);
        this.bar.setX(16);
        this.levelGetter = new StatGetterToolLevel(toolAction);
        this.icon = new GuiTool(-3, -3, toolAction);
        this.addChild(this.icon);
        IStatGetter extractionGetter = new StatGetterEffectLevel(ItemEffect.extraction, 4.5);
        IStatGetter unboundExtractionGetter = new StatGetterEffectLevel(ItemEffect.unboundExtraction, 1.0);
        IStatGetter enchantmentGetter = new StatGetterEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, 1.0);
        IStatGetter actionGetter = new StatGetterStriking(toolAction);
        IStatGetter sweepingGetter = new StatGetterEffectLevel(ItemEffect.sweepingStrike, 1.0);
        IStatGetter truesweepGetter = new StatGetterEffectLevel(ItemEffect.truesweep, 1.0);
        IStatGetter planarSweepGetter = new StatGetterEffectLevel(ItemEffect.planarSweep, 1.0);
        IStatGetter focusGetter = new StatGetterEffectEfficiency(ItemEffect.sweepingFocus, 1.0);
        this.setIndicators(new GuiStatIndicator[] { new StrikingStatIndicatorGui(toolAction), new GuiStatIndicator(0, 0, "tetra.stats.tool.truesweepingStrike", 4, truesweepGetter, new TooltipGetterNone("tetra.stats.tool.truesweepingStrike.tooltip")).withShowRequirements(actionGetter, sweepingGetter), new GuiStatIndicator(0, 0, "tetra.stats.tool.planarSweep", 21, planarSweepGetter, new TooltipGetterNone("tetra.stats.tool.planarSweep.tooltip")).withShowRequirements(actionGetter, sweepingGetter), new GuiStatIndicator(0, 0, "tetra.stats.tool.sweepingFocus", 22, focusGetter, new TooltipGetterSweepingFocus(focusGetter)).withShowRequirements(actionGetter, sweepingGetter), new GuiStatIndicator(0, 0, "tetra.stats.tool.extraction", 7, extractionGetter, new TooltipGetterInteger("tetra.stats.tool.extraction.tooltip", extractionGetter)), new GuiStatIndicator(0, 0, "tetra.stats.tool.unboundExtraction", 20, unboundExtractionGetter, new TooltipGetterInteger("tetra.stats.tool.unboundExtraction.tooltip", unboundExtractionGetter)), new GuiStatIndicator(0, 0, "tetra.stats.tool.efficiency", 17, enchantmentGetter, new TooltipGetterInteger("tetra.stats.tool.efficiency.tooltip", enchantmentGetter)) });
    }

    @Override
    public void update(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        super.update(player, currentStack, previewStack, slot, improvement);
        int level = (int) this.levelGetter.getValue(player, currentStack);
        int color = 16777215;
        if (!previewStack.isEmpty()) {
            int previewLevel = (int) this.levelGetter.getValue(player, previewStack);
            color = this.getDiffColor(level, previewLevel);
            level = previewLevel;
        } else if (slot != null) {
            int previewLevel = level - this.getSlotLevel(player, currentStack, slot, improvement);
            color = this.getDiffColor(previewLevel, level);
        }
        this.icon.update(level, color);
    }

    @Override
    protected void realign() {
        super.realign();
        if (GuiAlignment.left.equals(this.alignment)) {
            this.bar.setX(16);
            this.icon.setX(-3);
        } else {
            this.bar.setX(0);
            this.icon.setX(0);
        }
        this.icon.setAttachment(this.alignment.toAttachment());
        int offset = this.icon.getWidth();
        this.indicatorGroup.setX(GuiAlignment.right.equals(this.alignment) ? -offset : offset);
    }

    @Override
    public boolean shouldShow(Player player, ItemStack currentStack, ItemStack previewStack, String slot, String improvement) {
        return this.levelGetter.getValue(player, currentStack) > 0.0 || this.levelGetter.getValue(player, previewStack) > 0.0 || this.efficiencyVisibility && (this.statGetter.getValue(player, currentStack) > 0.0 || this.statGetter.getValue(player, previewStack) > 0.0);
    }

    protected int getDiffColor(int currentValue, int previewValue) {
        if (previewValue > currentValue) {
            return 5635925;
        } else {
            return previewValue < currentValue ? 16733525 : 16777215;
        }
    }

    protected int getSlotLevel(Player player, ItemStack itemStack, String slot, String improvement) {
        return ((Double) CastOptional.cast(itemStack.getItem(), IModularItem.class).map(item -> improvement != null ? this.levelGetter.getValue(player, itemStack, slot, improvement) : this.levelGetter.getValue(player, itemStack, slot)).orElse(-1.0)).intValue();
    }
}