package dev.xkmc.l2hostility.init.advancements;

import net.minecraft.resources.ResourceLocation;

public class HostilityTriggers {

    public static final KillTraitsTrigger KILL_TRAITS = new KillTraitsTrigger(new ResourceLocation("l2hostility", "kill_trait"));

    public static final KillTraitLevelTrigger TRAIT_LEVEL = new KillTraitLevelTrigger(new ResourceLocation("l2hostility", "trait_level"));

    public static final KillTraitCountTrigger TRAIT_COUNT = new KillTraitCountTrigger(new ResourceLocation("l2hostility", "trait_count"));

    public static final KillTraitEffectTrigger TRAIT_EFFECT = new KillTraitEffectTrigger(new ResourceLocation("l2hostility", "trait_effect"));

    public static final KillTraitFlameTrigger TRAIT_FLAME = new KillTraitFlameTrigger(new ResourceLocation("l2hostility", "trait_flame"));

    public static void register() {
    }
}