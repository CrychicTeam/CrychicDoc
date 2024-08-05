package com.mna.advancements;

import com.mna.advancements.triggers.AffinityTinkerTrigger;
import com.mna.advancements.triggers.ArcaneFurnaceSmeltTrigger;
import com.mna.advancements.triggers.BeOurGuestTrigger;
import com.mna.advancements.triggers.CaptureWellspringTrigger;
import com.mna.advancements.triggers.CastCantripTrigger;
import com.mna.advancements.triggers.CastSpellTrigger;
import com.mna.advancements.triggers.CompleteMultiblockTrigger;
import com.mna.advancements.triggers.CraftSpellTrigger;
import com.mna.advancements.triggers.DefeatBossTrigger;
import com.mna.advancements.triggers.DisenchantTrigger;
import com.mna.advancements.triggers.DrawManaweavingPatternTrigger;
import com.mna.advancements.triggers.EldrinAltarCraftTrigger;
import com.mna.advancements.triggers.FactionJoinTrigger;
import com.mna.advancements.triggers.LightEldrinFumeTrigger;
import com.mna.advancements.triggers.MagicLevelTrigger;
import com.mna.advancements.triggers.ManaweaveAltarCraftTrigger;
import com.mna.advancements.triggers.NotSoEasyTrigger;
import com.mna.advancements.triggers.OpenManaweaveCacheTrigger;
import com.mna.advancements.triggers.PerformRitualTrigger;
import com.mna.advancements.triggers.RunescribingTableCraftTrigger;
import com.mna.advancements.triggers.RunicAnvilCraftTrigger;
import com.mna.advancements.triggers.SetBonusTrigger;
import com.mna.advancements.triggers.ShamanTrigger;
import com.mna.advancements.triggers.StudyDeskRoteTrigger;
import com.mna.advancements.triggers.SummonConstructTrigger;
import com.mna.advancements.triggers.ThrownRunescribingPatternTrigger;
import com.mna.advancements.triggers.TierTrigger;
import com.mna.advancements.triggers.TranscribeSpellTrigger;
import com.mna.advancements.triggers.UseCharmTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class CustomAdvancementTriggers {

    public static final ThrownRunescribingPatternTrigger THROWN_RUNESCRIBE_PATTERN = new ThrownRunescribingPatternTrigger();

    public static final UseCharmTrigger USE_CHARM = new UseCharmTrigger();

    public static final SummonConstructTrigger SUMMON_CONSTRUCT = new SummonConstructTrigger();

    public static final ShamanTrigger SHAMAN = new ShamanTrigger();

    public static final NotSoEasyTrigger NOT_SO_EASY = new NotSoEasyTrigger();

    public static final BeOurGuestTrigger BE_OUR_GUEST = new BeOurGuestTrigger();

    public static final MagicLevelTrigger MAGIC_LEVEL = new MagicLevelTrigger();

    public static final DrawManaweavingPatternTrigger DRAW_MANAWEAVE = new DrawManaweavingPatternTrigger();

    public static final PerformRitualTrigger PERFORM_RITUAL = new PerformRitualTrigger();

    public static final ManaweaveAltarCraftTrigger MANAWEAVE_CRAFT = new ManaweaveAltarCraftTrigger();

    public static final RunescribingTableCraftTrigger RUNESCRIBE = new RunescribingTableCraftTrigger();

    public static final RunicAnvilCraftTrigger RUNIC_ANVIL_CRAFT = new RunicAnvilCraftTrigger();

    public static final ArcaneFurnaceSmeltTrigger ARCANE_FURNACE_SMELT = new ArcaneFurnaceSmeltTrigger();

    public static final EldrinAltarCraftTrigger ELDRIN_ALTAR_CRAFT = new EldrinAltarCraftTrigger();

    public static final CastCantripTrigger CANTRIP_CAST = new CastCantripTrigger();

    public static final CraftSpellTrigger CRAFT_SPELL = new CraftSpellTrigger();

    public static final CastSpellTrigger CAST_SPELL = new CastSpellTrigger();

    public static final TierTrigger TIER_CHANGE = new TierTrigger();

    public static final FactionJoinTrigger FACTION_JOIN = new FactionJoinTrigger();

    public static final SetBonusTrigger SET_BONUS = new SetBonusTrigger();

    public static final AffinityTinkerTrigger AFFINITY_TINKER = new AffinityTinkerTrigger();

    public static final LightEldrinFumeTrigger LIGHT_FUME = new LightEldrinFumeTrigger();

    public static final OpenManaweaveCacheTrigger OPEN_CACHE = new OpenManaweaveCacheTrigger();

    public static final CompleteMultiblockTrigger COMPLETE_MULTIBLOCK = new CompleteMultiblockTrigger();

    public static final DisenchantTrigger DISENCHANT = new DisenchantTrigger();

    public static final StudyDeskRoteTrigger STUDY_DESK_ROTE = new StudyDeskRoteTrigger();

    public static final DefeatBossTrigger DEFEAT_BOSS = new DefeatBossTrigger();

    public static final CaptureWellspringTrigger CAPTURE_WELLSPRING = new CaptureWellspringTrigger();

    public static final TranscribeSpellTrigger TRANSCRIBE_SPELL = new TranscribeSpellTrigger();

    @SubscribeEvent
    public static void commonLoad(FMLCommonSetupEvent event) {
        CriteriaTriggers.register(AFFINITY_TINKER);
        CriteriaTriggers.register(ARCANE_FURNACE_SMELT);
        CriteriaTriggers.register(BE_OUR_GUEST);
        CriteriaTriggers.register(CANTRIP_CAST);
        CriteriaTriggers.register(CAPTURE_WELLSPRING);
        CriteriaTriggers.register(CAST_SPELL);
        CriteriaTriggers.register(COMPLETE_MULTIBLOCK);
        CriteriaTriggers.register(CRAFT_SPELL);
        CriteriaTriggers.register(DEFEAT_BOSS);
        CriteriaTriggers.register(DISENCHANT);
        CriteriaTriggers.register(DRAW_MANAWEAVE);
        CriteriaTriggers.register(ELDRIN_ALTAR_CRAFT);
        CriteriaTriggers.register(FACTION_JOIN);
        CriteriaTriggers.register(LIGHT_FUME);
        CriteriaTriggers.register(MAGIC_LEVEL);
        CriteriaTriggers.register(MANAWEAVE_CRAFT);
        CriteriaTriggers.register(NOT_SO_EASY);
        CriteriaTriggers.register(OPEN_CACHE);
        CriteriaTriggers.register(PERFORM_RITUAL);
        CriteriaTriggers.register(RUNESCRIBE);
        CriteriaTriggers.register(RUNIC_ANVIL_CRAFT);
        CriteriaTriggers.register(SET_BONUS);
        CriteriaTriggers.register(SHAMAN);
        CriteriaTriggers.register(STUDY_DESK_ROTE);
        CriteriaTriggers.register(SUMMON_CONSTRUCT);
        CriteriaTriggers.register(THROWN_RUNESCRIBE_PATTERN);
        CriteriaTriggers.register(TIER_CHANGE);
        CriteriaTriggers.register(TRANSCRIBE_SPELL);
        CriteriaTriggers.register(USE_CHARM);
    }
}