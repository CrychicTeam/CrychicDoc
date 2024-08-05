package net.mehvahdjukaar.supplementaries.reg;

import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.List;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.FrameBraceBlock;
import net.mehvahdjukaar.supplementaries.common.block.cauldron.CauldronBehaviorsManager;
import net.mehvahdjukaar.supplementaries.common.block.dispenser.DispenserBehaviorsManager;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FaucetBehaviorsManager;
import net.mehvahdjukaar.supplementaries.common.block.placeable_book.PlaceableBookManager;
import net.mehvahdjukaar.supplementaries.common.block.present.PresentBehaviorsManager;
import net.mehvahdjukaar.supplementaries.common.events.overrides.InteractEventsHandler;
import net.mehvahdjukaar.supplementaries.common.items.loot.CurseLootFunction;
import net.mehvahdjukaar.supplementaries.common.items.loot.RandomArrowFunction;
import net.mehvahdjukaar.supplementaries.common.utils.FlowerPotHandler;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.FireworkStarRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;

public class ModSetup {

    private static boolean hasFinishedSetup = false;

    private static int setupStage = 0;

    private static boolean firstTagLoad = false;

    private static final List<Runnable> MOD_SETUP_WORK = List.of(CompatHandler::setup, FlowerPotHandler::setup, ModSetup::registerCompostables, ModSetup::registerMobFoods, ModSetup::registerFabricFlammable, CauldronBehaviorsManager::registerBehaviors, ModCreativeTabs::setup, FaucetBehaviorsManager::registerBehaviors, (Runnable) () -> FireworkStarRecipe.SHAPE_BY_ITEM.put((Item) ModRegistry.ENDERMAN_SKULL_ITEM.get(), FireworkRocketItem.Shape.CREEPER));

    public static void asyncSetup() {
        PresentBehaviorsManager.registerBehaviors();
        RandomArrowFunction.setup();
        LootTablesInjects.setup();
        registerFrameBlocks();
        CurseLootFunction.setup();
        PlaceableBookManager.setup();
    }

    public static void setup() {
        ArrayList<Long> list = new ArrayList();
        try {
            Stopwatch watch = Stopwatch.createStarted();
            for (int i = 0; i < MOD_SETUP_WORK.size(); i++) {
                setupStage = i;
                ((Runnable) MOD_SETUP_WORK.get(i)).run();
                list.add(watch.elapsed().toMillis());
                watch.reset();
                watch.start();
            }
            hasFinishedSetup = true;
            Supplementaries.LOGGER.info("Finished mod setup in: {} ms", list);
        } catch (Exception var3) {
            terminateWhenSetupFails(var3);
        }
    }

    private static void terminateWhenSetupFails(Exception e) {
        throw new IllegalStateException("Mod setup has failed to complete (" + setupStage + ").\n This might be due to some mod incompatibility or outdated dependencies (check if everything is up to date).\n Refusing to continue loading with a broken modstate. Next step: crashing this game, no survivors", e);
    }

    private static void registerFabricFlammable() {
        RegHelper.registerBlockFlammability((Block) ModRegistry.ROPE.get(), 60, 100);
    }

    private static void registerMobFoods() {
        RegHelper.registerChickenFood((ItemLike) ModRegistry.FLAX_SEEDS_ITEM.get());
        RegHelper.registerHorseFood((ItemLike) ModRegistry.FLAX_BLOCK.get(), (ItemLike) ModRegistry.SUGAR_CUBE.get(), (ItemLike) ModRegistry.FLAX_ITEM.get());
        RegHelper.registerParrotFood((ItemLike) ModRegistry.FLAX_SEEDS_ITEM.get());
    }

    private static void registerFrameBlocks() {
        ModRegistry.TIMBER_FRAME.get().registerFilledBlock(ModRegistry.DAUB.get(), ModRegistry.DAUB_FRAME.get());
        ((FrameBraceBlock) ModRegistry.TIMBER_BRACE.get()).registerFilledBlock(ModRegistry.DAUB.get(), ModRegistry.DAUB_BRACE.get());
        ((FrameBlock) ModRegistry.TIMBER_CROSS_BRACE.get()).registerFilledBlock(ModRegistry.DAUB.get(), ModRegistry.DAUB_CROSS_BRACE.get());
    }

    private static void registerCompostables() {
        ComposterBlock.COMPOSTABLES.put((ItemLike) ModRegistry.FLAX_SEEDS_ITEM.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put((ItemLike) ModRegistry.FLAX_ITEM.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(((Block) ModRegistry.FLAX_WILD.get()).asItem(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(((Block) ModRegistry.FLAX_BLOCK.get()).asItem(), 1.0F);
    }

    public static void tagDependantSetup(RegistryAccess registryAccess) {
        if (!firstTagLoad) {
            Stopwatch watch = Stopwatch.createStarted();
            firstTagLoad = true;
            if (!hasFinishedSetup) {
                try {
                    Supplementaries.LOGGER.error("Something went wrong during mod setup, exiting");
                    ((Runnable) MOD_SETUP_WORK.get(setupStage)).run();
                    Supplementaries.LOGGER.error("No error found. Weird");
                } catch (Exception var3) {
                    terminateWhenSetupFails(var3);
                }
            }
            DispenserBehaviorsManager.registerBehaviors(registryAccess);
            Supplementaries.LOGGER.info("Finished additional setup in {} ms", watch.elapsed().toMillis());
        }
        InteractEventsHandler.setupOverrides();
    }
}