package se.mickelus.tetra.gui.stats.getter;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

@ParametersAreNonnullByDefault
public class TooltipGetterTool implements ITooltipGetter {

    private final IStatGetter levelGetter;

    private final IStatGetter baseEfficiencyGetter;

    private final IStatGetter attackSpeedGetter;

    private final IStatGetter enchantmentGetter;

    private final IStatGetter totalEfficiencyGetter;

    private final String localizationKey;

    private final boolean includeSpeedModifier;

    public TooltipGetterTool(ToolAction tool, boolean includeSpeedModifier) {
        this.localizationKey = "tetra.stats." + tool.name() + ".tooltip";
        this.includeSpeedModifier = includeSpeedModifier;
        this.levelGetter = new StatGetterToolLevel(tool);
        this.baseEfficiencyGetter = new StatGetterToolEfficiency(tool);
        this.attackSpeedGetter = new StatGetterAttribute(Attributes.ATTACK_SPEED);
        this.enchantmentGetter = new StatGetterEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, 1.0);
        if (includeSpeedModifier) {
            this.totalEfficiencyGetter = new StatGetterToolCompoundEfficiency(this.baseEfficiencyGetter, this.attackSpeedGetter, this.enchantmentGetter);
        } else {
            this.totalEfficiencyGetter = new StatGetterSum(this.baseEfficiencyGetter, this.enchantmentGetter);
        }
    }

    @Override
    public String getTooltipBase(Player player, ItemStack itemStack) {
        double enchantmentBonus = ItemModularHandheld.getEfficiencyEnchantmentBonus((int) this.enchantmentGetter.getValue(player, itemStack));
        double speedMultiplier = ItemModularHandheld.getAttackSpeedHarvestModifier(this.attackSpeedGetter.getValue(player, itemStack));
        String speedString = ChatFormatting.DARK_GRAY + I18n.get("tetra.not_available");
        if (this.includeSpeedModifier) {
            if (speedMultiplier < 1.0) {
                speedString = ChatFormatting.RED.toString();
            } else if (speedMultiplier > 1.0) {
                speedString = ChatFormatting.GREEN.toString();
            } else {
                speedString = ChatFormatting.YELLOW.toString();
            }
            speedString = speedString + String.format("%.2fx", speedMultiplier);
        }
        return I18n.get(this.localizationKey) + "\n \n" + I18n.get("tetra.stats.tool.breakdown", (int) this.levelGetter.getValue(player, itemStack), String.format("%.2f", this.totalEfficiencyGetter.getValue(player, itemStack)), String.format("%.2f", this.baseEfficiencyGetter.getValue(player, itemStack)), speedString, enchantmentBonus > 0.0 ? I18n.get("tetra.stats.tool.enchantment_bonus", String.format("%.0f", enchantmentBonus)) : "");
    }

    @Override
    public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
        return true;
    }

    @Override
    public String getTooltipExtension(Player player, ItemStack itemStack) {
        return I18n.get("tetra.stats.tool.tooltip_extended");
    }
}