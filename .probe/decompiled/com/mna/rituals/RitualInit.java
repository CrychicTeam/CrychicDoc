package com.mna.rituals;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.tools.RLoc;
import com.mna.rituals.effects.RitualEffectAlteration;
import com.mna.rituals.effects.RitualEffectAncientCouncil;
import com.mna.rituals.effects.RitualEffectAncientStone;
import com.mna.rituals.effects.RitualEffectArcana;
import com.mna.rituals.effects.RitualEffectAurora;
import com.mna.rituals.effects.RitualEffectBroker;
import com.mna.rituals.effects.RitualEffectBurningHells;
import com.mna.rituals.effects.RitualEffectCircleOfPower;
import com.mna.rituals.effects.RitualEffectClearSkies;
import com.mna.rituals.effects.RitualEffectColdDark;
import com.mna.rituals.effects.RitualEffectConsumeCrystalSoul;
import com.mna.rituals.effects.RitualEffectDeepOcean;
import com.mna.rituals.effects.RitualEffectEndlessVoid;
import com.mna.rituals.effects.RitualEffectEventide;
import com.mna.rituals.effects.RitualEffectFaerieCourts;
import com.mna.rituals.effects.RitualEffectFlatLands;
import com.mna.rituals.effects.RitualEffectForgottenLore;
import com.mna.rituals.effects.RitualEffectGate;
import com.mna.rituals.effects.RitualEffectHole;
import com.mna.rituals.effects.RitualEffectHomestead;
import com.mna.rituals.effects.RitualEffectLocate;
import com.mna.rituals.effects.RitualEffectMonsoon;
import com.mna.rituals.effects.RitualEffectReturn;
import com.mna.rituals.effects.RitualEffectSearingInferno;
import com.mna.rituals.effects.RitualEffectShadowSoul;
import com.mna.rituals.effects.RitualEffectShiftingSeasons;
import com.mna.rituals.effects.RitualEffectStaircase;
import com.mna.rituals.effects.RitualEffectSummon;
import com.mna.rituals.effects.RitualEffectSummonOdin;
import com.mna.rituals.effects.RitualEffectUntamedWind;
import com.mna.rituals.effects.RitualEffectVisit;
import com.mna.rituals.effects.RitualEffectWanderingWizard;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class RitualInit {

    @SubscribeEvent
    public static void registerRitualEffects(RegisterEvent event) {
        event.register(((IForgeRegistry) Registries.RitualEffect.get()).getRegistryKey(), helper -> {
            helper.register(RLoc.create("ritual-effect-portal"), new RitualEffectReturn(RLoc.create("rituals/return")));
            helper.register(RLoc.create("ritual-effect-gate"), new RitualEffectGate(RLoc.create("rituals/gate")));
            helper.register(RLoc.create("ritual-effect-homestead"), new RitualEffectHomestead(RLoc.create("rituals/homestead")));
            helper.register(RLoc.create("ritual-effect-aurora"), new RitualEffectAurora(RLoc.create("rituals/aurora")));
            helper.register(RLoc.create("ritual-effect-eventide"), new RitualEffectEventide(RLoc.create("rituals/eventide")));
            helper.register(RLoc.create("ritual-effect-visit"), new RitualEffectVisit(RLoc.create("rituals/visit")));
            helper.register(RLoc.create("ritual-effect-summon"), new RitualEffectSummon(RLoc.create("rituals/summon")));
            helper.register(RLoc.create("ritual-effect-arcana"), new RitualEffectArcana(RLoc.create("rituals/arcana")));
            helper.register(RLoc.create("ritual-effect-ancient-stone"), new RitualEffectAncientStone(RLoc.create("rituals/ancient_stone")));
            helper.register(RLoc.create("ritual-effect-deep-ocean"), new RitualEffectDeepOcean(RLoc.create("rituals/deep_ocean")));
            helper.register(RLoc.create("ritual-effect-endless-void"), new RitualEffectEndlessVoid(RLoc.create("rituals/endless_void")));
            helper.register(RLoc.create("ritual-effect-forgotten-lore"), new RitualEffectForgottenLore(RLoc.create("rituals/forgotten_lore")));
            helper.register(RLoc.create("ritual-effect-searing-inferno"), new RitualEffectSearingInferno(RLoc.create("rituals/searing_inferno")));
            helper.register(RLoc.create("ritual-effect-untamed-wind"), new RitualEffectUntamedWind(RLoc.create("rituals/untamed_wind")));
            helper.register(RLoc.create("ritual-effect-flat-lands"), new RitualEffectFlatLands(RLoc.create("rituals/flat_lands")));
            helper.register(RLoc.create("ritual-effect-clear-skies"), new RitualEffectClearSkies(RLoc.create("rituals/clear_skies")));
            helper.register(RLoc.create("ritual-effect-monsoon"), new RitualEffectMonsoon(RLoc.create("rituals/monsoon")));
            helper.register(RLoc.create("ritual-effect-burning-hells"), new RitualEffectBurningHells(RLoc.create("rituals/burning_hells")));
            helper.register(RLoc.create("ritual-effect-faerie-courts"), new RitualEffectFaerieCourts(RLoc.create("rituals/faerie_courts")));
            helper.register(RLoc.create("ritual-effect-ancient-council"), new RitualEffectAncientCouncil(RLoc.create("rituals/ancient_council")));
            helper.register(RLoc.create("ritual-effect-locating"), new RitualEffectLocate(RLoc.create("rituals/locating")));
            helper.register(RLoc.create("ritual-effect-hole"), new RitualEffectHole(RLoc.create("rituals/hole")));
            helper.register(RLoc.create("ritual-effect-staircase"), new RitualEffectStaircase(RLoc.create("rituals/staircase")));
            helper.register(RLoc.create("ritual-effect-wandering-wizard"), new RitualEffectWanderingWizard(RLoc.create("rituals/wandering_wizard")));
            helper.register(RLoc.create("ritual-effect-alteration"), new RitualEffectAlteration(RLoc.create("rituals/alteration")));
            helper.register(RLoc.create("ritual-effect-broker"), new RitualEffectBroker(RLoc.create("rituals/broker")));
            helper.register(RLoc.create("ritual-effect-circle-of-power"), new RitualEffectCircleOfPower(RLoc.create("rituals/circle_of_power")));
            helper.register(RLoc.create("ritual-effect-cold-dark"), new RitualEffectColdDark(RLoc.create("rituals/cold_dark")));
            helper.register(RLoc.create("ritual-effect-odins-call"), new RitualEffectSummonOdin(RLoc.create("rituals/odins_call")));
            helper.register(RLoc.create("ritual-effect-shadow-soul"), new RitualEffectShadowSoul(RLoc.create("rituals/shadow_soul")));
            helper.register(RLoc.create("ritual-effect-shifting-seasons"), new RitualEffectShiftingSeasons(RLoc.create("rituals/shifting_seasons")));
            helper.register(RLoc.create("ritual-effect-consume-crystal-soul"), new RitualEffectConsumeCrystalSoul(RLoc.create("rituals/consume_crystal_soul")));
        });
        ManaAndArtifice.LOGGER.info("M&A -> Ritual Effects Registered");
    }
}