package dev.xkmc.modulargolems.init.advancement;

import net.minecraft.resources.ResourceLocation;

public class GolemTriggers {

    public static final GolemHotFixTrigger HOT_FIX = new GolemHotFixTrigger(new ResourceLocation("modulargolems", "hot_fix"));

    public static final GolemAnvilFixTrigger ANVIL_FIX = new GolemAnvilFixTrigger(new ResourceLocation("modulargolems", "anvil_fix"));

    public static final PartCraftTrigger PART_CRAFT = new PartCraftTrigger(new ResourceLocation("modulargolems", "part_craft"));

    public static final UpgradeApplyTrigger UPGRADE_APPLY = new UpgradeApplyTrigger(new ResourceLocation("modulargolems", "upgrade_apply"));

    public static final GolemEquipTrigger EQUIP = new GolemEquipTrigger(new ResourceLocation("modulargolems", "equip"));

    public static final GolemBreakToolTrigger BREAK = new GolemBreakToolTrigger(new ResourceLocation("modulargolems", "break"));

    public static final GolemKillTrigger KILL = new GolemKillTrigger(new ResourceLocation("modulargolems", "kill"));

    public static final GolemThunderTrigger THUNDER = new GolemThunderTrigger(new ResourceLocation("modulargolems", "thunder"));

    public static final GolemMassSummonTrigger MAS_SUMMON = new GolemMassSummonTrigger(new ResourceLocation("modulargolems", "mass_summon"));

    public static void register() {
    }
}