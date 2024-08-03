package com.mna.entities.constructs.ai.base;

import com.mna.ManaAndArtifice;
import com.mna.Registries;
import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.tools.RLoc;
import com.mna.entities.constructs.ai.ConstructActivate;
import com.mna.entities.constructs.ai.ConstructAdventure;
import com.mna.entities.constructs.ai.ConstructBreakBlocks;
import com.mna.entities.constructs.ai.ConstructBreed;
import com.mna.entities.constructs.ai.ConstructButcher;
import com.mna.entities.constructs.ai.ConstructCastSpellAtTarget;
import com.mna.entities.constructs.ai.ConstructChop;
import com.mna.entities.constructs.ai.ConstructCollectFluid;
import com.mna.entities.constructs.ai.ConstructCollectItems;
import com.mna.entities.constructs.ai.ConstructCommandFollowAndGuard;
import com.mna.entities.constructs.ai.ConstructCommandFollowLodestar;
import com.mna.entities.constructs.ai.ConstructCommandReturnToTable;
import com.mna.entities.constructs.ai.ConstructCommandStay;
import com.mna.entities.constructs.ai.ConstructCraft;
import com.mna.entities.constructs.ai.ConstructCrush;
import com.mna.entities.constructs.ai.ConstructDropItem;
import com.mna.entities.constructs.ai.ConstructDuel;
import com.mna.entities.constructs.ai.ConstructEatItem;
import com.mna.entities.constructs.ai.ConstructFish;
import com.mna.entities.constructs.ai.ConstructGuard;
import com.mna.entities.constructs.ai.ConstructHarvest;
import com.mna.entities.constructs.ai.ConstructHunt;
import com.mna.entities.constructs.ai.ConstructMilkCow;
import com.mna.entities.constructs.ai.ConstructMine;
import com.mna.entities.constructs.ai.ConstructMove;
import com.mna.entities.constructs.ai.ConstructPatrol;
import com.mna.entities.constructs.ai.ConstructPlaceBlock;
import com.mna.entities.constructs.ai.ConstructPlaceFluidInTank;
import com.mna.entities.constructs.ai.ConstructPlaceFluidInWorld;
import com.mna.entities.constructs.ai.ConstructPlaceItem;
import com.mna.entities.constructs.ai.ConstructPlant;
import com.mna.entities.constructs.ai.ConstructRuneforge;
import com.mna.entities.constructs.ai.ConstructRunescribe;
import com.mna.entities.constructs.ai.ConstructShear;
import com.mna.entities.constructs.ai.ConstructTakeFluid;
import com.mna.entities.constructs.ai.ConstructTakeItem;
import com.mna.entities.constructs.ai.ConstructUseItemOnBlock;
import com.mna.entities.constructs.ai.ConstructWait;
import com.mna.entities.constructs.ai.ConstructWaterCrops;
import com.mna.entities.constructs.ai.ConstructWearItem;
import com.mna.entities.constructs.ai.conditionals.ConstructHasFluidLevel;
import com.mna.entities.constructs.ai.conditionals.ConstructHasHealthLevel;
import com.mna.entities.constructs.ai.conditionals.ConstructHasItemInHand;
import com.mna.entities.constructs.ai.conditionals.ConstructHasManaLevel;
import com.mna.entities.constructs.ai.conditionals.ConstructHasName;
import com.mna.entities.constructs.ai.conditionals.ConstructIsAreaEmpty;
import com.mna.entities.constructs.ai.conditionals.ConstructIsBlockAtPosition;
import com.mna.entities.constructs.ai.conditionals.ConstructIsBlockRedstonePowered;
import com.mna.entities.constructs.ai.conditionals.ConstructIsContainerEmtpty;
import com.mna.entities.constructs.ai.conditionals.ConstructIsEntityInArea;
import com.mna.entities.constructs.ai.conditionals.ConstructIsFluidInContainer;
import com.mna.entities.constructs.ai.conditionals.ConstructIsItemInContainer;
import com.mna.entities.constructs.ai.conditionals.ConstructIsRaining;
import com.mna.entities.constructs.ai.conditionals.ConstructIsTimeOfDayWithin;
import com.mna.entities.constructs.ai.conditionals.ConstructRandomChance;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class ConstructTasks {

    public static final ConstructTask TAKE = new ConstructTask(RLoc.create("textures/gui/construct/task/take_item.png"), ConstructTakeItem.class, true, true);

    public static final ConstructTask PLACE_ITEM = new ConstructTask(RLoc.create("textures/gui/construct/task/place_item.png"), ConstructPlaceItem.class, true, true);

    public static final ConstructTask RUNESCRIBE = new ConstructTask(RLoc.create("textures/gui/construct/task/scribe_rune.png"), ConstructRunescribe.class, true, false);

    public static final ConstructTask RUNEFORGE = new ConstructTask(RLoc.create("textures/gui/construct/task/forge_rune.png"), ConstructRuneforge.class, true, false);

    public static final ConstructTask CHOP = new ConstructTask(RLoc.create("textures/gui/construct/task/chop_wood.png"), ConstructChop.class, true, true);

    public static final ConstructTask HARVEST = new ConstructTask(RLoc.create("textures/gui/construct/task/harvest_crops.png"), ConstructHarvest.class, true, true);

    public static final ConstructTask GATHER_FLUID = new ConstructTask(RLoc.create("textures/gui/construct/task/gather_fluid.png"), ConstructCollectFluid.class, true, false);

    public static final ConstructTask PLACE_FLUID = new ConstructTask(RLoc.create("textures/gui/construct/task/place_fluid.png"), ConstructPlaceFluidInWorld.class, true, false);

    public static final ConstructTask WATER = new ConstructTask(RLoc.create("textures/gui/construct/task/water_crops.png"), ConstructWaterCrops.class, true, false);

    public static final ConstructTask PLANT = new ConstructTask(RLoc.create("textures/gui/construct/task/plant_crops.png"), ConstructPlant.class, true, true);

    public static final ConstructTask ACTIVATE = new ConstructTask(RLoc.create("textures/gui/construct/task/activate.png"), ConstructActivate.class, true, true);

    public static final ConstructTask PATROL = new ConstructTask(RLoc.create("textures/gui/construct/task/patrol.png"), ConstructPatrol.class, true, true);

    public static final ConstructTask MOVE = new ConstructTask(RLoc.create("textures/gui/construct/task/move.png"), ConstructMove.class, true, true);

    public static final ConstructTask MINE = new ConstructTask(RLoc.create("textures/gui/construct/task/mine.png"), ConstructMine.class, true, false);

    public static final ConstructTask ADVENTURE = new ConstructTask(RLoc.create("textures/gui/construct/task/adventure.png"), ConstructAdventure.class, true, false);

    public static final ConstructTask WAIT = new ConstructTask(RLoc.create("textures/gui/construct/task/wait.png"), ConstructWait.class, true, true);

    public static final ConstructTask BUTCHER = new ConstructTask(RLoc.create("textures/gui/construct/task/butcher.png"), ConstructButcher.class, true, false);

    public static final ConstructTask BREED = new ConstructTask(RLoc.create("textures/gui/construct/task/breed.png"), ConstructBreed.class, true, false);

    public static final ConstructTask SHEAR = new ConstructTask(RLoc.create("textures/gui/construct/task/shear.png"), ConstructShear.class, true, false);

    public static final ConstructTask PLACE_BLOCK = new ConstructTask(RLoc.create("textures/gui/construct/task/place_block.png"), ConstructPlaceBlock.class, true, true);

    public static final ConstructTask DROP_ITEM = new ConstructTask(RLoc.create("textures/gui/construct/task/drop_item_at_location.png"), ConstructDropItem.class, true, true);

    public static final ConstructTask CRAFT = new ConstructTask(RLoc.create("textures/gui/construct/task/craft.png"), ConstructCraft.class, true, false);

    public static final ConstructTask CRUSH = new ConstructTask(RLoc.create("textures/gui/construct/task/crush.png"), ConstructCrush.class, true, false);

    public static final ConstructTask GATHER_ITEMS = new ConstructTask(RLoc.create("textures/gui/construct/task/gather_items.png"), ConstructCollectItems.class, true, true);

    public static final ConstructTask EAT_ITEM = new ConstructTask(RLoc.create("textures/gui/construct/task/eat_item.png"), ConstructEatItem.class, true, true);

    public static final ConstructTask FISH = new ConstructTask(RLoc.create("textures/gui/construct/task/fish.png"), ConstructFish.class, true, true);

    public static final ConstructTask TAKE_FLUID_FROM_CONTAINER = new ConstructTask(RLoc.create("textures/gui/construct/task/take_fluid_from_container.png"), ConstructTakeFluid.class, true, false);

    public static final ConstructTask PLACE_FLUID_IN_CONTAINER = new ConstructTask(RLoc.create("textures/gui/construct/task/place_fluid_in_container.png"), ConstructPlaceFluidInTank.class, true, false);

    public static final ConstructTask USE_ITEM_ON_BLOCK = new ConstructTask(RLoc.create("textures/gui/construct/task/use_item_on_block.png"), ConstructUseItemOnBlock.class, true, false);

    public static final ConstructTask DUEL = new ConstructTask(RLoc.create("textures/gui/construct/task/duel.png"), ConstructDuel.class, true, false);

    public static final ConstructTask CAST_SPELL_AT_TARGET = new ConstructTask(RLoc.create("textures/gui/construct/task/cast_spell.png"), ConstructCastSpellAtTarget.class, true, false);

    public static final ConstructTask BREAK_BLOCKS = new ConstructTask(RLoc.create("textures/gui/construct/task/break_blocks.png"), ConstructBreakBlocks.class, true, false);

    public static final ConstructTask GUARD = new ConstructTask(RLoc.create("textures/gui/construct/task/guard.png"), ConstructGuard.class, true, true);

    public static final ConstructTask HUNT = new ConstructTask(RLoc.create("textures/gui/construct/task/hunt.png"), ConstructHunt.class, true, false);

    public static final ConstructTask MILK = new ConstructTask(RLoc.create("textures/gui/construct/task/milk.png"), ConstructMilkCow.class, true, false);

    public static final ConstructTask STAY = new ConstructTask(RLoc.create("textures/gui/construct/task/stay.png"), ConstructCommandStay.class, false, false);

    public static final ConstructTask MODIFY = new ConstructTask(RLoc.create("textures/gui/construct/task/modify.png"), ConstructCommandReturnToTable.class, false, false);

    public static final ConstructTask FOLLOW_DEFEND = new ConstructTask(RLoc.create("textures/gui/construct/task/follow.png"), ConstructCommandFollowAndGuard.class, false, false);

    public static final ConstructTask LODESTAR = new ConstructTask(RLoc.create("textures/gui/construct/task/execute_other_lodestar.png"), ConstructCommandFollowLodestar.class, false, false);

    public static final ConstructTask WEAR_ITEM = new ConstructTask(RLoc.create("textures/gui/construct/task/wear_item.png"), ConstructWearItem.class, false, false);

    @SubscribeEvent
    public static void registerTasks(RegisterEvent event) {
        event.register(((IForgeRegistry) Registries.ConstructTasks.get()).getRegistryKey(), helper -> {
            helper.register(RLoc.create("modify"), MODIFY);
            helper.register(RLoc.create("follow_defend"), FOLLOW_DEFEND);
            helper.register(RLoc.create("lodestar"), LODESTAR);
            helper.register(RLoc.create("take_item"), TAKE);
            helper.register(RLoc.create("place_item"), PLACE_ITEM);
            helper.register(RLoc.create("runescribe"), RUNESCRIBE);
            helper.register(RLoc.create("runeforge"), RUNEFORGE);
            helper.register(RLoc.create("chop"), CHOP);
            helper.register(RLoc.create("harvest"), HARVEST);
            helper.register(RLoc.create("gather_fluid"), GATHER_FLUID);
            helper.register(RLoc.create("place_fluid"), PLACE_FLUID);
            helper.register(RLoc.create("water"), WATER);
            helper.register(RLoc.create("plant"), PLANT);
            helper.register(RLoc.create("activate"), ACTIVATE);
            helper.register(RLoc.create("patrol"), PATROL);
            helper.register(RLoc.create("move"), MOVE);
            helper.register(RLoc.create("mine"), MINE);
            helper.register(RLoc.create("adventure"), ADVENTURE);
            helper.register(RLoc.create("wait"), WAIT);
            helper.register(RLoc.create("butcher"), BUTCHER);
            helper.register(RLoc.create("breed"), BREED);
            helper.register(RLoc.create("shear"), SHEAR);
            helper.register(RLoc.create("place_block"), PLACE_BLOCK);
            helper.register(RLoc.create("drop_item"), DROP_ITEM);
            helper.register(RLoc.create("craft"), CRAFT);
            helper.register(RLoc.create("crush"), CRUSH);
            helper.register(RLoc.create("stay"), STAY);
            helper.register(RLoc.create("gather_items"), GATHER_ITEMS);
            helper.register(RLoc.create("eat_item"), EAT_ITEM);
            helper.register(RLoc.create("fish"), FISH);
            helper.register(RLoc.create("take_fluid_from_container"), TAKE_FLUID_FROM_CONTAINER);
            helper.register(RLoc.create("place_fluid_in_container"), PLACE_FLUID_IN_CONTAINER);
            helper.register(RLoc.create("use_item_on_block"), USE_ITEM_ON_BLOCK);
            helper.register(RLoc.create("wear_item"), WEAR_ITEM);
            helper.register(RLoc.create("duel"), DUEL);
            helper.register(RLoc.create("cast_spell"), CAST_SPELL_AT_TARGET);
            helper.register(RLoc.create("break_blocks"), BREAK_BLOCKS);
            helper.register(RLoc.create("guard"), GUARD);
            helper.register(RLoc.create("hunt"), HUNT);
            helper.register(RLoc.create("milk"), MILK);
            helper.register(RLoc.create("has_item"), ConstructTasks.Conditions.HAS_ITEM);
            helper.register(RLoc.create("has_mana_level"), ConstructTasks.Conditions.HAS_MANA);
            helper.register(RLoc.create("has_fluid_level"), ConstructTasks.Conditions.HAS_FLUID);
            helper.register(RLoc.create("find_entity"), ConstructTasks.Conditions.FIND_ENTITY);
            helper.register(RLoc.create("find_block"), ConstructTasks.Conditions.FIND_BLOCK);
            helper.register(RLoc.create("container_empty"), ConstructTasks.Conditions.CONTAINER_EMPTY);
            helper.register(RLoc.create("container_find"), ConstructTasks.Conditions.CONTAINER_FIND);
            helper.register(RLoc.create("container_fluid"), ConstructTasks.Conditions.CONTAINER_FLUID);
            helper.register(RLoc.create("time_of_day"), ConstructTasks.Conditions.TIME_OF_DAY);
            helper.register(RLoc.create("random_chance"), ConstructTasks.Conditions.RANDOM_CHANCE);
            helper.register(RLoc.create("redstone_powered"), ConstructTasks.Conditions.REDSTONE_POWERED);
            helper.register(RLoc.create("has_health"), ConstructTasks.Conditions.HAS_HEALTH);
            helper.register(RLoc.create("has_name"), ConstructTasks.Conditions.HAS_NAME);
            helper.register(RLoc.create("area_empty"), ConstructTasks.Conditions.AREA_EMPTY);
            helper.register(RLoc.create("raining"), ConstructTasks.Conditions.RAINING_STORMING);
        });
        ManaAndArtifice.LOGGER.info("Mana and Artifice >> Registered Construct Tasks");
    }

    public static class Conditions {

        public static final ConstructTask HAS_ITEM = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/has_item.png"), ConstructHasItemInHand.class, true, false, true);

        public static final ConstructTask HAS_MANA = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/has_mana_at_level.png"), ConstructHasManaLevel.class, true, false, true);

        public static final ConstructTask HAS_HEALTH = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/has_health_at_level.png"), ConstructHasHealthLevel.class, true, false, true);

        public static final ConstructTask HAS_NAME = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/has_name.png"), ConstructHasName.class, true, false, true);

        public static final ConstructTask AREA_EMPTY = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/area_empty.png"), ConstructIsAreaEmpty.class, true, false, true);

        public static final ConstructTask HAS_FLUID = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/has_fluid_at_level.png"), ConstructHasFluidLevel.class, true, false, true);

        public static final ConstructTask FIND_ENTITY = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/find_entity.png"), ConstructIsEntityInArea.class, true, false, true);

        public static final ConstructTask FIND_BLOCK = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/find_block.png"), ConstructIsBlockAtPosition.class, true, false, true);

        public static final ConstructTask CONTAINER_EMPTY = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/container_empty.png"), ConstructIsContainerEmtpty.class, true, false, true);

        public static final ConstructTask CONTAINER_FIND = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/container_find.png"), ConstructIsItemInContainer.class, true, false, true);

        public static final ConstructTask CONTAINER_FLUID = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/container_fluid.png"), ConstructIsFluidInContainer.class, true, false, true);

        public static final ConstructTask TIME_OF_DAY = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/time_of_day.png"), ConstructIsTimeOfDayWithin.class, true, false, true);

        public static final ConstructTask RANDOM_CHANCE = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/random_chance.png"), ConstructRandomChance.class, true, false, true);

        public static final ConstructTask REDSTONE_POWERED = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/redstone_powered.png"), ConstructIsBlockRedstonePowered.class, true, false, true);

        public static final ConstructTask RAINING_STORMING = new ConstructTask(RLoc.create("textures/gui/construct/task/condition/raining_storming.png"), ConstructIsRaining.class, true, false);
    }
}