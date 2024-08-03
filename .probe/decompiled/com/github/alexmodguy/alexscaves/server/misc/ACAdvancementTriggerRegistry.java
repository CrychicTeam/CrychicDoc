package com.github.alexmodguy.alexscaves.server.misc;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;

public class ACAdvancementTriggerRegistry {

    public static final ACAdvancementTrigger KILL_MOB_WITH_GALENA_GAUNTLET = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "kill_mob_with_galena_gauntlet"));

    public static final ACAdvancementTrigger FINISHED_QUARRY = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "finished_quarry"));

    public static final ACAdvancementTrigger DINOSAURS_MINECART = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "dinosaurs_minecart"));

    public static final ACAdvancementTrigger CAVE_PAINTING = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "cave_painting"));

    public static final ACAdvancementTrigger MYSTERY_CAVE_PAINTING = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "mystery_cave_painting"));

    public static final ACAdvancementTrigger SUMMON_LUXTRUCTOSAURUS = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "summon_luxtructosaurus"));

    public static final ACAdvancementTrigger ATLATITAN_STOMP = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "atlatitan_stomp"));

    public static final ACAdvancementTrigger ENTER_ACID_WITH_ARMOR = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "enter_acid_with_armor"));

    public static final ACAdvancementTrigger ACID_CREATE_RUST = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "acid_create_rust"));

    public static final ACAdvancementTrigger REMOTE_DETONATION = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "remote_detonation"));

    public static final ACAdvancementTrigger STOP_NUCLEAR_FURNACE_MELTDOWN = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "stop_nuclear_furnace_meltdown"));

    public static final ACAdvancementTrigger HATCH_TREMORZILLA_EGG = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "hatch_tremorzilla_egg"));

    public static final ACAdvancementTrigger TREMORZILLA_KILL_BEAM = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "tremorzilla_kill_beam"));

    public static final ACAdvancementTrigger STALKED_BY_DEEP_ONE = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "stalked_by_deep_one"));

    public static final ACAdvancementTrigger DEEP_ONE_TRADE = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "deep_one_trade"));

    public static final ACAdvancementTrigger DEEP_ONE_NEUTRAL = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "deep_one_neutral"));

    public static final ACAdvancementTrigger DEEP_ONE_HELPFUL = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "deep_one_helpful"));

    public static final ACAdvancementTrigger UNDERZEALOT_SACRIFICE = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "underzealot_sacrifice"));

    public static final ACAdvancementTrigger BEHOLDER_FAR_AWAY = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "beholder_far_away"));

    public static final ACAdvancementTrigger EAT_DARKENED_APPLE = new ACAdvancementTrigger(new ResourceLocation("alexscaves", "eat_darkened_apple"));

    public static void setup() {
        CriteriaTriggers.register(KILL_MOB_WITH_GALENA_GAUNTLET);
        CriteriaTriggers.register(DINOSAURS_MINECART);
        CriteriaTriggers.register(CAVE_PAINTING);
        CriteriaTriggers.register(MYSTERY_CAVE_PAINTING);
        CriteriaTriggers.register(SUMMON_LUXTRUCTOSAURUS);
        CriteriaTriggers.register(ATLATITAN_STOMP);
        CriteriaTriggers.register(FINISHED_QUARRY);
        CriteriaTriggers.register(ENTER_ACID_WITH_ARMOR);
        CriteriaTriggers.register(ACID_CREATE_RUST);
        CriteriaTriggers.register(REMOTE_DETONATION);
        CriteriaTriggers.register(STOP_NUCLEAR_FURNACE_MELTDOWN);
        CriteriaTriggers.register(HATCH_TREMORZILLA_EGG);
        CriteriaTriggers.register(TREMORZILLA_KILL_BEAM);
        CriteriaTriggers.register(STALKED_BY_DEEP_ONE);
        CriteriaTriggers.register(DEEP_ONE_TRADE);
        CriteriaTriggers.register(DEEP_ONE_NEUTRAL);
        CriteriaTriggers.register(DEEP_ONE_HELPFUL);
        CriteriaTriggers.register(UNDERZEALOT_SACRIFICE);
        CriteriaTriggers.register(BEHOLDER_FAR_AWAY);
        CriteriaTriggers.register(EAT_DARKENED_APPLE);
    }
}