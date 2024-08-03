package com.mna.api.sound;

import com.mna.api.affinity.Affinity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public final class SFX {

    @SubscribeEvent
    public static void onRegisterSounds(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.SOUND_EVENTS, helper -> {
            registerSound(helper, "buff_arcane");
            registerSound(helper, "buff_earth");
            registerSound(helper, "buff_ender");
            registerSound(helper, "buff_fire");
            registerSound(helper, "buff_ice");
            registerSound(helper, "buff_water");
            registerSound(helper, "buff_wind");
            registerSound(helper, "impact_arcane");
            registerSound(helper, "impact_earth");
            registerSound(helper, "impact_ender");
            registerSound(helper, "impact_fire");
            registerSound(helper, "impact_ice");
            registerSound(helper, "impact_water");
            registerSound(helper, "impact_wind");
            registerSound(helper, "impact_lightning");
            registerSound(helper, "impact_arcane_aoe");
            registerSound(helper, "impact_earth_aoe");
            registerSound(helper, "impact_fire_aoe");
            registerSound(helper, "impact_wind_aoe");
            registerSound(helper, "impact_lightning_aoe");
            registerSound(helper, "cast_arcane");
            registerSound(helper, "cast_brian");
            registerSound(helper, "cast_brian_2");
            registerSound(helper, "cast_earth");
            registerSound(helper, "cast_ender");
            registerSound(helper, "cast_fire");
            registerSound(helper, "cast_ice");
            registerSound(helper, "cast_lightning");
            registerSound(helper, "cast_water");
            registerSound(helper, "cast_wind");
            registerSound(helper, "cast_kablam");
            registerSound(helper, "event_ritual_started");
            registerSound(helper, "event_ritual_point_appear");
            registerSound(helper, "event_eldrin_altar_item_inhale");
            registerSound(helper, "event_eldrin_altar_complete");
            registerSound(helper, "loop_air");
            registerSound(helper, "loop_arcane");
            registerSound(helper, "loop_earth");
            registerSound(helper, "loop_earth_rumble");
            registerSound(helper, "loop_ender");
            registerSound(helper, "loop_fire");
            registerSound(helper, "loop_water");
            registerSound(helper, "loop_ice");
            registerSound(helper, "loop_lightning");
            registerSound(helper, "loop_manaweaving");
            registerSound(helper, "loop_demon_summon");
            registerSound(helper, "loop_eldrin_altar");
            registerSound(helper, "loop_shadow_copy");
            registerSound(helper, "loop_altar_of_arcana");
            registerSound(helper, "gui_page_flip");
            registerSound(helper, "gui_chisel");
            registerSound(helper, "gui_pencil_write");
            registerSound(helper, "gui_charcoal_scribble");
            registerSound(helper, "event_arcane_sentry_charge");
            registerSound(helper, "event_manaweave_craft");
            registerSound(helper, "event_manaweave_cache_open");
            registerSound(helper, "event_gantry_summon");
            registerSound(helper, "event_magic_unlocked");
            registerSound(helper, "event_magic_level_up");
            registerSound(helper, "event_spell_created");
            registerSound(helper, "event_manaweave_pattern_match");
            registerSound(helper, "event_wtfboom");
            registerSound(helper, "event_demon_summon_end");
            registerSound(helper, "event_demon_summon_channel");
            registerSound(helper, "event_faerie_blow_kiss");
            registerSound(helper, "event_faerie_summon");
            registerSound(helper, "event_faerie_giggle");
            registerSound(helper, "event_faerie_leave");
            registerSound(helper, "event_faerie_imbue");
            registerSound(helper, "event_ancient_summon");
            registerSound(helper, "event_ancient_imbue");
            registerSound(helper, "event_ancient_imbue_1");
            registerSound(helper, "event_ancient_imbue_2");
            registerSound(helper, "event_ancient_imbue_3");
            registerSound(helper, "event_cold_dark");
            registerSound(helper, "event_iron_bell");
            registerSound(helper, "event_meteor_jump");
            registerSound(helper, "event_demon_armor_sprint_start");
            registerSound(helper, "event_altar_of_arcana_start");
            registerSound(helper, "event_altar_of_arcana_complete");
            registerSound(helper, "event_faction_raid_demons");
            registerSound(helper, "event_faction_raid_fey");
            registerSound(helper, "event_faction_raid_council");
            registerSound(helper, "event_summon_ally_demons");
            registerSound(helper, "event_summon_ally_council");
            registerSound(helper, "event_summon_ally_fey");
            registerSound(helper, "event_summon_ally_undead");
            registerSound(helper, "entity_generic_woosh");
            registerSound(helper, "entity_construct_horn");
            registerSound(helper, "entity_construct_spray_generic");
            registerSound(helper, "entity_construct_spray_fire");
            registerSound(helper, "entity_imp_attack");
            registerSound(helper, "entity_imp_death");
            registerSound(helper, "entity_imp_idle");
            registerSound(helper, "entity_imp_leap");
            registerSound(helper, "entity_lantern_wraith_death");
            registerSound(helper, "entity_lantern_wraith_hurt");
            registerSound(helper, "entity_lantern_wraith_idle");
            registerSound(helper, "entity_pixie_attack");
            registerSound(helper, "entity_pixie_death");
            registerSound(helper, "entity_pixie_hurt");
            registerSound(helper, "entity_spellbreaker_rally");
            registerSound(helper, "entity_spellbreaker_shield_bash");
            registerSound(helper, "entity_broker_deal");
            registerSound(helper, "entity_spectral_horse_summon");
            registerSound(helper, "entity_skeleton_assassin_shuriken_throw");
            registerSound(helper, "entity_skeleton_assassin_shuriken_impact");
            registerSound(helper, "entity_skeleton_assassin_bolo_throw");
            registerSound(helper, "entity_skeleton_assassin_bolo_impact");
            registerSound(helper, "entity_skeleton_assassin_smoke_bomb");
            registerSound(helper, "entity_hulking_zombie_attack");
            registerSound(helper, "entity_hulking_zombie_death");
            registerSound(helper, "entity_hulking_zombie_idle");
            registerSound(helper, "entity_hulking_zombie_hurt");
            registerSound(helper, "entity_hulking_zombie_leap");
            registerSound(helper, "entity_hulking_zombie_roar");
            registerSound(helper, "entity_pumpkin_king_attack");
            registerSound(helper, "entity_pumpkin_king_death");
            registerSound(helper, "entity_pumpkin_king_idle");
            registerSound(helper, "entity_pumpkin_king_hit");
            registerSound(helper, "entity_pumpkin_king_entangle");
            registerSound(helper, "entity_pumpkin_king_incinerate");
            registerSound(helper, "entity_pumpkin_king_spawn");
            registerSound(helper, "entity_odin_bifrost");
            registerSound(helper, "entity_odin_attack");
            registerSound(helper, "entity_odin_draw");
            registerSound(helper, "entity_odin_enrage");
            registerSound(helper, "entity_odin_hurt");
            registerSound(helper, "entity_demonlord_attack");
            registerSound(helper, "entity_demonlord_hit");
            registerSound(helper, "entity_demonlord_idle");
            registerSound(helper, "entity_demonlord_drawswords");
            registerSound(helper, "entity_demonlord_summonstaff");
            registerSound(helper, "entity_demonlord_scream");
            registerSound(helper, "entity_demonlord_leap");
            registerSound(helper, "entity_demonlord_death");
            registerSound(helper, "entity_lich_attack");
            registerSound(helper, "entity_lich_hit");
            registerSound(helper, "entity_lich_idle");
            registerSound(helper, "entity_lich_breathattack");
            registerSound(helper, "entity_lich_cast");
            registerSound(helper, "entity_lich_death");
            registerSound(helper, "entity_lich_summon");
            registerSound(helper, "entity_lich_pwdeath");
            registerSound(helper, "entity_oink");
            registerSound(helper, "entity_aranai");
            registerSound(helper, "entity_wandering_wizard_death");
            registerSound(helper, "entity_wandering_wizard_hit");
            registerSound(helper, "entity_wandering_wizard_idle");
            registerSound(helper, "entity_wandering_wizard_no");
            registerSound(helper, "entity_wandering_wizard_yes");
            registerSound(helper, "entity_chatterer_clack");
        });
    }

    private static void registerSound(RegisterEvent.RegisterHelper<SoundEvent> registry, String name) {
        registry.register(new ResourceLocation("mna", name), SoundEvent.createVariableRangeEvent(new ResourceLocation("mna", name)));
    }

    public static final class Entity {

        public static final class Aranai {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_aranai")
            public static final SoundEvent GRUMBLE = null;
        }

        public static final class Broker {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_broker_deal")
            public static final SoundEvent DEAL = null;
        }

        public static final class Chatterer {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_chatterer_clack")
            public static final SoundEvent CLACK = null;
        }

        public static final class Construct {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_construct_horn")
            public static final SoundEvent HORN = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_construct_spray_generic")
            public static final SoundEvent SPRAY_GENERIC = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_construct_spray_fire")
            public static final SoundEvent SPRAY_FIRE = null;
        }

        public static final class DemonLord {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_attack")
            public static final SoundEvent ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_hit")
            public static final SoundEvent HIT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_idle")
            public static final SoundEvent IDLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_drawswords")
            public static final SoundEvent DRAW_SWORDS = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_summonstaff")
            public static final SoundEvent SUMMON_STAFF = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_scream")
            public static final SoundEvent SCREAM = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_leap")
            public static final SoundEvent LEAP = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_demonlord_death")
            public static final SoundEvent DEATH = null;
        }

        public static final class Generic {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_generic_woosh")
            public static final SoundEvent WOOSH = null;
        }

        public static final class HulkingZombie {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_hulking_zombie_attack")
            public static final SoundEvent ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_hulking_zombie_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_hulking_zombie_hurt")
            public static final SoundEvent HURT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_hulking_zombie_idle")
            public static final SoundEvent IDLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_hulking_zombie_leap")
            public static final SoundEvent LEAP = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_hulking_zombie_roar")
            public static final SoundEvent ROAR = null;
        }

        public static final class Imp {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_imp_attack")
            public static final SoundEvent ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_imp_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_imp_idle")
            public static final SoundEvent IDLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_imp_leap")
            public static final SoundEvent LEAP = null;
        }

        public static final class LanternWraith {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lantern_wraith_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lantern_wraith_hurt")
            public static final SoundEvent HURT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lantern_wraith_idle")
            public static final SoundEvent IDLE = null;
        }

        public static final class Odin {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_odin_bifrost")
            public static final SoundEvent BIFROST = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_odin_attack")
            public static final SoundEvent ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_odin_draw")
            public static final SoundEvent DRAW = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_odin_enrage")
            public static final SoundEvent ENRAGE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_odin_hurt")
            public static final SoundEvent HIT = null;
        }

        public static final class Oink {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_oink")
            public static final SoundEvent OINK = null;
        }

        public static final class Pixie {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pixie_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pixie_hurt")
            public static final SoundEvent HURT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pixie_attack")
            public static final SoundEvent ATTACK = null;
        }

        public static final class PumpkinKing {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_attack")
            public static final SoundEvent ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_hit")
            public static final SoundEvent HURT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_idle")
            public static final SoundEvent IDLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_entangle")
            public static final SoundEvent ENTANGLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_incinerate")
            public static final SoundEvent INCINERATE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_pumpkin_king_spawn")
            public static final SoundEvent SPAWN = null;
        }

        public static final class SkeletonAssassin {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_skeleton_assassin_shuriken_throw")
            public static final SoundEvent SHURIKEN_THROW = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_skeleton_assassin_shuriken_impact")
            public static final SoundEvent SHURIKEN_IMPACT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_skeleton_assassin_bolo_throw")
            public static final SoundEvent BOLO_THROW = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_skeleton_assassin_bolo_impact")
            public static final SoundEvent BOLO_IMPACT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_skeleton_assassin_smoke_bomb")
            public static final SoundEvent SMOKE_BOMB = null;
        }

        public static final class SpectralHorse {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_spectral_horse_summon")
            public static final SoundEvent SUMMON = null;
        }

        public static final class Spellbreaker {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_spellbreaker_rally")
            public static final SoundEvent RALLY = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_spellbreaker_shield_bash")
            public static final SoundEvent SHIELD_BASH = null;
        }

        public static final class WanderingWizard {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_wandering_wizard_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_wandering_wizard_hit")
            public static final SoundEvent HIT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_wandering_wizard_idle")
            public static final SoundEvent IDLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_wandering_wizard_no")
            public static final SoundEvent NO = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_wandering_wizard_yes")
            public static final SoundEvent YES = null;
        }

        public static final class WitherLich {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_attack")
            public static final SoundEvent ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_hit")
            public static final SoundEvent HIT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_idle")
            public static final SoundEvent IDLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_breathattack")
            public static final SoundEvent BREATH_ATTACK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_cast")
            public static final SoundEvent CAST = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_death")
            public static final SoundEvent DEATH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_summon")
            public static final SoundEvent SUMMON = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:entity_lich_pwdeath")
            public static final SoundEvent PW_DEATH = null;
        }
    }

    public static final class Event {

        public static final class AltarOfArcana {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_altar_of_arcana_start")
            public static final SoundEvent START = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_altar_of_arcana_complete")
            public static final SoundEvent COMPLETE = null;
        }

        public static final class Artifact {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_demon_armor_sprint_start")
            public static final SoundEvent DEMON_ARMOR_SPRINT_START = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_meteor_jump")
            public static final SoundEvent METEOR_JUMP = null;
        }

        public static final class Block {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_arcane_sentry_charge")
            public static final SoundEvent ARCANE_SENTRY_CHARGE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_manaweave_craft")
            public static final SoundEvent MANAWEAVE_ALTAR_CRAFT = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_manaweave_cache_open")
            public static final SoundEvent MANAWEAVE_CACHE_OPEN = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_gantry_summon")
            public static final SoundEvent GANTRY_SUMMON = null;
        }

        public static final class Eldrin {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_eldrin_altar_item_inhale")
            public static final SoundEvent DRAW_IN_ITEM = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_eldrin_altar_complete")
            public static final SoundEvent CRAFT_COMPLETE = null;
        }

        public static final class Faction {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faction_raid_demons")
            public static final SoundEvent FACTION_RAID_DEMONS = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faction_raid_fey")
            public static final SoundEvent FACTION_RAID_FEY = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faction_raid_council")
            public static final SoundEvent FACTION_RAID_COUNCIL = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_summon_ally_council")
            public static final SoundEvent FACTION_HORN_COUNCIL = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_summon_ally_demons")
            public static final SoundEvent FACTION_HORN_DEMONS = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_summon_ally_fey")
            public static final SoundEvent FACTION_HORN_FEY = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_summon_ally_undead")
            public static final SoundEvent FACTION_HORN_UNDEAD = null;
        }

        public static final class Player {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_magic_unlocked")
            public static final SoundEvent MAGIC_UNLOCKED = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_magic_level_up")
            public static final SoundEvent MAGIC_LEVEL_UP = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_spell_created")
            public static final SoundEvent SPELL_CREATED = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_manaweave_pattern_match")
            public static final SoundEvent MANAWEAVE_PATTERN_MATCH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_wtfboom")
            public static final SoundEvent WTF_BOOM = null;
        }

        public static final class Ritual {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_demon_summon_end")
            public static final SoundEvent DEMON_SUMMON_END = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_demon_summon_channel")
            public static final SoundEvent DEMON_SUMMON_CHANNEL = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faerie_blow_kiss")
            public static final SoundEvent FAERIE_BLOW_KISS = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faerie_summon")
            public static final SoundEvent FAERIE_SUMMON = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faerie_giggle")
            public static final SoundEvent FAERIE_GIGGLE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faerie_leave")
            public static final SoundEvent FAERIE_LEAVE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_faerie_imbue")
            public static final SoundEvent FAERIE_IMBUE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ancient_summon")
            public static final SoundEvent ANCIENT_SUMMON = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ancient_imbue")
            public static final SoundEvent ANCIENT_IMBUE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ancient_imbue_1")
            public static final SoundEvent ANCIENT_IMBUE_1 = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ancient_imbue_2")
            public static final SoundEvent ANCIENT_IMBUE_2 = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ancient_imbue_3")
            public static final SoundEvent ANCIENT_IMBUE_3 = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_cold_dark")
            public static final SoundEvent COLD_DARK = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_iron_bell")
            public static final SoundEvent IRON_BELL = null;
        }
    }

    public static final class Gui {

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:gui_page_flip")
        public static final SoundEvent PAGE_FLIP = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:gui_chisel")
        public static final SoundEvent CHISEL = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:gui_pencil_write")
        public static final SoundEvent PENCIL_WRITE = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:gui_charcoal_scribble")
        public static final SoundEvent CHARCOAL_SCRIBBLE = null;
    }

    public static final class Loops {

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_air")
        public static final SoundEvent AIR = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_arcane")
        public static final SoundEvent ARCANE = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_earth")
        public static final SoundEvent EARTH = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_earth_rumble")
        public static final SoundEvent EARTH_RUMBLE = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_ender")
        public static final SoundEvent ENDER = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_fire")
        public static final SoundEvent FIRE = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_lightning")
        public static final SoundEvent LIGHTNING = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_water")
        public static final SoundEvent WATER = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_ice")
        public static final SoundEvent ICE = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_manaweaving")
        public static final SoundEvent MANAWEAVING = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_demon_summon")
        public static final SoundEvent DEMON_SUMMON = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_eldrin_altar")
        public static final SoundEvent ELDRIN_ALTAR = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_shadow_copy")
        public static final SoundEvent SHADOW_COPY = null;

        @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:loop_altar_of_arcana")
        public static final SoundEvent ALTAR_OF_ARCANA = null;
    }

    public static final class Ritual {

        public static final class Effects {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ritual_started")
            public static final SoundEvent RITUAL_STARTED = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:event_ritual_point_appear")
            public static final SoundEvent RITUAL_POINT_APPEAR = null;
        }
    }

    public static final class Spell {

        public static final class Buff {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_arcane")
            public static final SoundEvent ARCANE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_earth")
            public static final SoundEvent EARTH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_ender")
            public static final SoundEvent ENDER = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_fire")
            public static final SoundEvent FIRE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_ice")
            public static final SoundEvent ICE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_water")
            public static final SoundEvent WATER = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:buff_wind")
            public static final SoundEvent WIND = null;
        }

        public static final class Cast {

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_arcane")
            public static final SoundEvent ARCANE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_brian")
            public static final SoundEvent BRIAN = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_brian_2")
            public static final SoundEvent BRIAN_2 = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_earth")
            public static final SoundEvent EARTH = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_ender")
            public static final SoundEvent ENDER = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_fire")
            public static final SoundEvent FIRE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_ice")
            public static final SoundEvent ICE = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_lightning")
            public static final SoundEvent LIGHTNING = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_water")
            public static final SoundEvent WATER = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_wind")
            public static final SoundEvent WIND = null;

            @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:cast_kablam")
            public static final SoundEvent KABLAM = null;

            public static SoundEvent ForAffinity(Affinity aff) {
                switch(aff) {
                    case ARCANE:
                        return ARCANE;
                    case EARTH:
                        return EARTH;
                    case ENDER:
                        return ENDER;
                    case FIRE:
                        return FIRE;
                    case WATER:
                        return WATER;
                    case WIND:
                        return WIND;
                    case LIGHTNING:
                        return LIGHTNING;
                    case ICE:
                        return ICE;
                    case UNKNOWN:
                    default:
                        return ARCANE;
                }
            }
        }

        public static final class Impact {

            public static final class AoE {

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_arcane_aoe")
                public static final SoundEvent ARCANE = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_earth_aoe")
                public static final SoundEvent EARTH = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_fire_aoe")
                public static final SoundEvent FIRE = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_wind_aoe")
                public static final SoundEvent WIND = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_lightning_aoe")
                public static final SoundEvent LIGHTNING = null;
            }

            public static final class Single {

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_arcane")
                public static final SoundEvent ARCANE = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_earth")
                public static final SoundEvent EARTH = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_ender")
                public static final SoundEvent ENDER = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_fire")
                public static final SoundEvent FIRE = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_lightning")
                public static final SoundEvent LIGHTNING = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_ice")
                public static final SoundEvent ICE = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_water")
                public static final SoundEvent WATER = null;

                @ObjectHolder(registryName = "minecraft:sound_event", value = "mna:impact_wind")
                public static final SoundEvent WIND = null;
            }
        }
    }
}