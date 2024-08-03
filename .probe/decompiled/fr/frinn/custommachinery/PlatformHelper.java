package fr.frinn.custommachinery;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import fr.frinn.custommachinery.common.component.EnergyMachineComponent;
import fr.frinn.custommachinery.common.component.handler.FluidComponentHandler;
import fr.frinn.custommachinery.common.component.handler.ItemComponentHandler;
import fr.frinn.custommachinery.common.init.CustomMachineTile;
import fr.frinn.custommachinery.common.util.transfer.ICommonEnergyHandler;
import fr.frinn.custommachinery.common.util.transfer.ICommonFluidHandler;
import fr.frinn.custommachinery.common.util.transfer.ICommonItemHandler;
import fr.frinn.custommachinery.common.util.transfer.IEnergyHelper;
import fr.frinn.custommachinery.common.util.transfer.IFluidHelper;
import fr.frinn.custommachinery.forge.PlatformHelperImpl;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;

public class PlatformHelper {

    @ExpectPlatform
    @Transformed
    public static ICommonEnergyHandler createEnergyHandler(EnergyMachineComponent component) {
        return PlatformHelperImpl.createEnergyHandler(component);
    }

    @ExpectPlatform
    @Transformed
    public static ICommonFluidHandler createFluidHandler(FluidComponentHandler handler) {
        return PlatformHelperImpl.createFluidHandler(handler);
    }

    @ExpectPlatform
    @Transformed
    public static ICommonItemHandler createItemHandler(ItemComponentHandler handler) {
        return PlatformHelperImpl.createItemHandler(handler);
    }

    @ExpectPlatform
    @Transformed
    public static CustomMachineTile createMachineTile(BlockPos pos, BlockState state) {
        return PlatformHelperImpl.createMachineTile(pos, state);
    }

    @ExpectPlatform
    @Transformed
    public static List<LootPool> getPoolsFromTable(LootTable table) {
        return PlatformHelperImpl.getPoolsFromTable(table);
    }

    @ExpectPlatform
    @Transformed
    public static IEnergyHelper energy() {
        return PlatformHelperImpl.energy();
    }

    @ExpectPlatform
    @Transformed
    public static IFluidHelper fluid() {
        return PlatformHelperImpl.fluid();
    }

    @ExpectPlatform
    @Transformed
    public static boolean hasCorrectToolsForDrops(Player player, BlockState state) {
        return PlatformHelperImpl.hasCorrectToolsForDrops(player, state);
    }
}