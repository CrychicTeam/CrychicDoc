package com.simibubi.create.infrastructure.gametest.tests;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.infrastructure.gametest.CreateGameTestHelper;
import com.simibubi.create.infrastructure.gametest.GameTestGroup;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.IItemHandler;

@GameTestGroup(path = "processing")
public class TestProcessing {

    @GameTest(template = "brass_mixing", timeoutTicks = 200)
    public static void brassMixing(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(2, 3, 2);
        BlockPos chest = new BlockPos(7, 3, 1);
        helper.m_177421_(lever);
        helper.m_177361_(() -> helper.assertContainerContains(chest, (Item) AllItems.BRASS_INGOT.get()));
    }

    @GameTest(template = "brass_mixing_2", timeoutTicks = 400)
    public static void brassMixing2(CreateGameTestHelper helper) {
        BlockPos basinLever = new BlockPos(3, 3, 1);
        BlockPos armLever = new BlockPos(3, 3, 5);
        BlockPos output = new BlockPos(1, 2, 3);
        helper.m_177421_(armLever);
        helper.whenSecondsPassed(7, () -> helper.m_177421_(armLever));
        helper.whenSecondsPassed(10, () -> helper.m_177421_(basinLever));
        helper.m_177361_(() -> helper.assertContainerContains(output, (Item) AllItems.BRASS_INGOT.get()));
    }

    @GameTest(template = "crushing_wheel_crafting", timeoutTicks = 200)
    public static void crushingWheelCrafting(CreateGameTestHelper helper) {
        BlockPos chest = new BlockPos(1, 4, 3);
        List<BlockPos> levers = List.of(new BlockPos(2, 3, 2), new BlockPos(6, 3, 2), new BlockPos(3, 7, 3));
        levers.forEach(helper::m_177421_);
        ItemStack expected = new ItemStack((ItemLike) AllBlocks.CRUSHING_WHEEL.get(), 2);
        helper.m_177361_(() -> helper.assertContainerContains(chest, expected));
    }

    @GameTest(template = "precision_mechanism_crafting", timeoutTicks = 400)
    public static void precisionMechanismCrafting(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(6, 3, 6);
        BlockPos output = new BlockPos(11, 3, 1);
        helper.m_177421_(lever);
        SequencedAssemblyRecipe recipe = (SequencedAssemblyRecipe) helper.m_177100_().getRecipeManager().byKey(Create.asResource("sequenced_assembly/precision_mechanism")).orElseThrow(() -> new GameTestAssertException("Precision Mechanism recipe not found"));
        Item result = recipe.getResultItem(helper.m_177100_().m_9598_()).getItem();
        Item[] possibleResults = (Item[]) recipe.resultPool.stream().map(ProcessingOutput::getStack).map(ItemStack::m_41720_).filter(item -> item != result).toArray(Item[]::new);
        helper.m_177361_(() -> {
            helper.assertContainerContains(output, result);
            helper.assertAnyContained(output, possibleResults);
        });
    }

    @GameTest(template = "sand_washing", timeoutTicks = 200)
    public static void sandWashing(CreateGameTestHelper helper) {
        BlockPos leverPos = new BlockPos(5, 3, 1);
        helper.m_177421_(leverPos);
        BlockPos chestPos = new BlockPos(8, 3, 2);
        helper.m_177361_(() -> helper.assertContainerContains(chestPos, Items.CLAY_BALL));
    }

    @GameTest(template = "stone_cobble_sand_crushing", timeoutTicks = 200)
    public static void stoneCobbleSandCrushing(CreateGameTestHelper helper) {
        BlockPos chest = new BlockPos(1, 6, 2);
        BlockPos lever = new BlockPos(2, 3, 1);
        helper.m_177421_(lever);
        ItemStack expected = new ItemStack(Items.SAND, 5);
        helper.m_177361_(() -> helper.assertContainerContains(chest, expected));
    }

    @GameTest(template = "track_crafting", timeoutTicks = 200)
    public static void trackCrafting(CreateGameTestHelper helper) {
        BlockPos output = new BlockPos(7, 3, 2);
        BlockPos lever = new BlockPos(2, 3, 1);
        helper.m_177421_(lever);
        ItemStack expected = new ItemStack((ItemLike) AllBlocks.TRACK.get(), 6);
        helper.m_177361_(() -> {
            helper.assertContainerContains(output, expected);
            IItemHandler handler = helper.itemStorageAt(output);
            ItemHelper.extract(handler, ItemHelper.sameItemPredicate(expected), 6, false);
            helper.assertContainerEmpty(output);
        });
    }

    @GameTest(template = "water_filling_bottle")
    public static void waterFillingBottle(CreateGameTestHelper helper) {
        BlockPos lever = new BlockPos(3, 3, 3);
        BlockPos output = new BlockPos(2, 2, 4);
        ItemStack expected = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
        helper.m_177421_(lever);
        helper.m_177361_(() -> helper.assertContainerContains(output, expected));
    }

    @GameTest(template = "wheat_milling")
    public static void wheatMilling(CreateGameTestHelper helper) {
        BlockPos output = new BlockPos(1, 2, 1);
        BlockPos lever = new BlockPos(1, 7, 1);
        helper.m_177421_(lever);
        ItemStack expected = new ItemStack((ItemLike) AllItems.WHEAT_FLOUR.get(), 3);
        helper.m_177361_(() -> helper.assertContainerContains(output, expected));
    }
}