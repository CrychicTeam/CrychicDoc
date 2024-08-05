package com.simibubi.create.content.materials;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ExperienceBlock extends Block {

    public ExperienceBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRand) {
        if (pRand.nextInt(5) == 0) {
            Vec3 vec3 = VecHelper.clampComponentWise(VecHelper.offsetRandomly(Vec3.ZERO, pRand, 0.75F), 0.55F).add(VecHelper.getCenterOf(pPos));
            pLevel.addParticle(ParticleTypes.END_ROD, vec3.x, vec3.y, vec3.z, pRand.nextGaussian() * 0.005, pRand.nextGaussian() * 0.005, pRand.nextGaussian() * 0.005);
        }
    }
}