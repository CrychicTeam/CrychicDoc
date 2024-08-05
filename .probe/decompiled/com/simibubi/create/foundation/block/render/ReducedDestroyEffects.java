package com.simibubi.create.foundation.block.render;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import org.apache.commons.lang3.mutable.MutableInt;

public class ReducedDestroyEffects implements IClientBlockExtensions {

    @Override
    public boolean addDestroyEffects(BlockState state, Level worldIn, BlockPos pos, ParticleEngine manager) {
        if (!(worldIn instanceof ClientLevel world)) {
            return true;
        } else {
            VoxelShape voxelshape = state.m_60808_(world, pos);
            MutableInt amtBoxes = new MutableInt(0);
            voxelshape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> amtBoxes.increment());
            double chance = 1.0 / (double) amtBoxes.getValue().intValue();
            if (state.m_60795_()) {
                return true;
            } else {
                voxelshape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
                    double w = x2 - x1;
                    double h = y2 - y1;
                    double l = z2 - z1;
                    int xParts = Math.max(2, Mth.ceil(Math.min(1.0, w) * 4.0));
                    int yParts = Math.max(2, Mth.ceil(Math.min(1.0, h) * 4.0));
                    int zParts = Math.max(2, Mth.ceil(Math.min(1.0, l) * 4.0));
                    for (int xIndex = 0; xIndex < xParts; xIndex++) {
                        for (int yIndex = 0; yIndex < yParts; yIndex++) {
                            for (int zIndex = 0; zIndex < zParts; zIndex++) {
                                if (!(world.f_46441_.nextDouble() > chance)) {
                                    double d4 = ((double) xIndex + 0.5) / (double) xParts;
                                    double d5 = ((double) yIndex + 0.5) / (double) yParts;
                                    double d6 = ((double) zIndex + 0.5) / (double) zParts;
                                    double x = (double) pos.m_123341_() + d4 * w + x1;
                                    double y = (double) pos.m_123342_() + d5 * h + y1;
                                    double z = (double) pos.m_123343_() + d6 * l + z1;
                                    manager.add(new TerrainParticle(world, x, y, z, d4 - 0.5, d5 - 0.5, d6 - 0.5, state, pos).updateSprite(state, pos));
                                }
                            }
                        }
                    }
                });
                return true;
            }
        }
    }
}