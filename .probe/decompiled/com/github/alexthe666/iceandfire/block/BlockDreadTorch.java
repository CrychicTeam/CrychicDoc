package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockDreadTorch extends TorchBlock implements IDreadBlock, IWallBlock {

    public BlockDreadTorch() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava().lightLevel(state -> 5).sound(SoundType.STONE).noOcclusion().dynamicShape().noCollission(), DustParticleOptions.REDSTONE);
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, BlockPos pos, @NotNull RandomSource rand) {
        double d0 = (double) pos.m_123341_() + 0.5;
        double d1 = (double) pos.m_123342_() + 0.6;
        double d2 = (double) pos.m_123343_() + 0.5;
        double d3 = 0.22;
        double d4 = 0.27;
        IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d0, d1, d2, 0.0, 0.0, 0.0);
    }

    @Override
    public Block wallBlock() {
        return IafBlockRegistry.DREAD_TORCH_WALL.get();
    }
}