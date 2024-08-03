package net.mehvahdjukaar.supplementaries.common.block.faucet;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.reg.ModSoftFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

class SappyLogInteraction implements FaucetSource.BlState {

    public FluidOffer getProvidedFluid(Level level, BlockPos pos, Direction dir, BlockState state) {
        Block backBlock = state.m_60734_();
        return backBlock != CompatObjects.SAPPY_MAPLE_LOG.get() && backBlock != CompatObjects.SAPPY_MAPLE_WOOD.get() ? null : FluidOffer.of(ModSoftFluids.SAP.getHolder());
    }

    public void drain(Level level, BlockPos pos, Direction dir, BlockState state, int amount) {
        Optional<Block> log = BuiltInRegistries.BLOCK.m_6612_(new ResourceLocation(Utils.getID(state.m_60734_()).toString().replace("sappy", "stripped")));
        log.ifPresent(block -> level.setBlock(pos, block.withPropertiesOf(state), 3));
    }
}