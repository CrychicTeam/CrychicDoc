package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.utility.worldWrappers.WrappedWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ContraptionWorld extends WrappedWorld {

    final Contraption contraption;

    private final int minY;

    private final int height;

    public ContraptionWorld(Level world, Contraption contraption) {
        super(world);
        this.contraption = contraption;
        this.minY = nextMultipleOf16(contraption.bounds.minY - 1.0);
        this.height = nextMultipleOf16(contraption.bounds.maxY + 1.0) - this.minY;
    }

    private static int nextMultipleOf16(double a) {
        return ((Math.abs((int) a) - 1 | 15) + 1) * Mth.sign(a);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        StructureTemplate.StructureBlockInfo blockInfo = (StructureTemplate.StructureBlockInfo) this.contraption.getBlocks().get(pos);
        return blockInfo != null ? blockInfo.state() : Blocks.AIR.defaultBlockState();
    }

    @Override
    public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay) {
        this.world.playLocalSound(x, y, z, sound, category, volume, pitch, distanceDelay);
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getMinBuildHeight() {
        return this.minY;
    }
}