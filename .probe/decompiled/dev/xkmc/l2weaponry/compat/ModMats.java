package dev.xkmc.l2weaponry.compat;

import dev.xkmc.l2damagetracker.contents.materials.api.IMatToolType;
import dev.xkmc.l2damagetracker.contents.materials.api.ITool;
import dev.xkmc.l2damagetracker.contents.materials.api.IToolStats;
import dev.xkmc.l2damagetracker.contents.materials.api.ToolConfig;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.GenItemVanillaType;
import net.minecraft.world.item.Tier;

public record ModMats(Tier tier, ExtraToolConfig config) implements IMatToolType, IToolStats {

    public Tier getTier() {
        return this.tier;
    }

    public IToolStats getToolStats() {
        return this;
    }

    public ToolConfig getToolConfig() {
        return GenItemVanillaType.TOOL_GEN;
    }

    public ExtraToolConfig getExtraToolConfig() {
        return this.config;
    }

    public int durability() {
        return this.tier.getUses();
    }

    public int speed() {
        return Math.round(this.tier.getSpeed());
    }

    public int enchant() {
        return this.tier.getEnchantmentValue();
    }

    public int getDamage(ITool tool) {
        return tool.getDamage(Math.round(this.tier.getAttackDamageBonus()) + 4);
    }

    public float getSpeed(ITool tool) {
        return tool.getSpeed(1.0F);
    }
}