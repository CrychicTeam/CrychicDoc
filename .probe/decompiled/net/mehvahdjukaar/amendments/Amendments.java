package net.mehvahdjukaar.amendments;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.mehvahdjukaar.amendments.common.FlowerPotHandler;
import net.mehvahdjukaar.amendments.common.network.ModNetwork;
import net.mehvahdjukaar.amendments.configs.ClientConfigs;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.events.behaviors.CauldronConversion;
import net.mehvahdjukaar.amendments.events.behaviors.InteractEvents;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Amendments {

    public static final String MOD_ID = "amendments";

    public static final Logger LOGGER = LogManager.getLogger("Amendments");

    public static final List<String> OLD_MODS = List.of("supplementaries", "carpeted", "betterlily", "betterjukebox");

    private static boolean hasRun = false;

    public static ResourceLocation res(String name) {
        return new ResourceLocation("amendments", name);
    }

    public static void init() {
        CommonConfigs.init();
        ModRegistry.init();
        ModNetwork.init();
        if (PlatHelper.getPhysicalSide().isClient()) {
            ClientConfigs.init();
            AmendmentsClient.init();
        }
        PlatHelper.addCommonSetupAsync(Amendments::setupAsync);
        PlatHelper.addCommonSetup(Amendments::setup);
        RegHelper.registerSimpleRecipeCondition(res("flag"), CommonConfigs::isFlagOn);
    }

    private static void setup() {
        if (CommonConfigs.INVERSE_POTIONS.get() == null) {
            throw new IllegalStateException("Inverse potions config is null. How??");
        } else {
            if (CompatHandler.SUPPLEMENTARIES) {
                SuppCompat.setup();
            }
        }
    }

    private static void setupAsync() {
        FlowerPotHandler.setup();
    }

    public static void onCommonTagUpdate(RegistryAccess registryAccess, boolean client) {
        InteractEvents.setupOverrides();
        if (!hasRun) {
            hasRun = true;
            for (SoftFluid f : SoftFluidRegistry.getRegistry(registryAccess)) {
                registerFluidBehavior(f);
            }
        }
        if (client) {
            AmendmentsClient.afterTagSetup();
        }
    }

    public static void registerFluidBehavior(SoftFluid f) {
        Set<Item> itemSet = new HashSet();
        for (FluidContainerList.Category c : f.getContainerList().getCategories()) {
            for (Item full : c.getFilledItems()) {
                if (full != Items.AIR && !itemSet.contains(full)) {
                    DispenserHelper.registerCustomBehavior(new CauldronConversion.DispenserBehavior(full));
                    itemSet.add(full);
                }
            }
        }
    }

    public static boolean isSupportingCeiling(BlockPos pos, LevelReader world) {
        return isSupportingCeiling(world.m_8055_(pos), pos, world);
    }

    public static boolean isSupportingCeiling(BlockState upState, BlockPos pos, LevelReader world) {
        return CompatHandler.SUPPLEMENTARIES ? SuppCompat.isSupportingCeiling(upState, pos, world) : Block.canSupportCenter(world, pos, Direction.DOWN);
    }

    public static boolean canConnectDown(BlockState neighborState, LevelAccessor level, BlockPos pos) {
        return CompatHandler.SUPPLEMENTARIES ? SuppCompat.canConnectDown(neighborState) : neighborState.m_60659_(level, pos, Direction.UP, SupportType.CENTER);
    }
}