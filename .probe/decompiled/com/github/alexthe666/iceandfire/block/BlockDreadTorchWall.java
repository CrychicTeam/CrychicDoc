package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockDreadTorchWall extends WallTorchBlock implements IDreadBlock {

    public BlockDreadTorchWall() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava().lightLevel(state -> 5).sound(SoundType.STONE).noOcclusion().dynamicShape().noCollission().dropsLike(IafBlockRegistry.DREAD_TORCH.get()), DustParticleOptions.REDSTONE);
    }

    @Override
    public void animateTick(BlockState stateIn, @NotNull Level worldIn, BlockPos pos, @NotNull RandomSource rand) {
        Direction direction = (Direction) stateIn.m_61143_(f_58119_);
        double d0 = (double) pos.m_123341_() + 0.5;
        double d1 = (double) pos.m_123342_() + 0.7;
        double d2 = (double) pos.m_123343_() + 0.5;
        double d3 = 0.22;
        double d4 = 0.27;
        Direction direction1 = direction.getOpposite();
        IceAndFire.PROXY.spawnParticle(EnumParticles.Dread_Torch, d0 + 0.27 * (double) direction1.getStepX(), d1 + 0.22, d2 + 0.27 * (double) direction1.getStepZ(), 0.0, 0.0, 0.0);
    }
}