package net.mehvahdjukaar.amendments.mixins;

import net.mehvahdjukaar.amendments.common.block.ModCauldronBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LavaCauldronBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ LavaCauldronBlock.class })
public abstract class LavaCauldronMixin extends Block {

    public LavaCauldronMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
        super.animateTick(state, level, pos, rand);
        if (rand.nextInt(4) == 0) {
            BlockPos blockPos = pos.above();
            if (level.getBlockState(blockPos).m_60795_() && !level.getBlockState(blockPos).m_60804_(level, blockPos)) {
                Vec3 c = pos.getCenter();
                if (rand.nextInt(20) == 0) {
                    ModCauldronBlock.addSurfaceParticles(ParticleTypes.LAVA, level, pos, 1, 0.9375, rand, 0.0F, 0.0F, 0.0F);
                    level.playLocalSound(c.x, 0.9375, c.z, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
                }
                if (rand.nextInt(40) == 0) {
                    level.playLocalSound(c.x, 0.9375, c.z, SoundEvents.LAVA_AMBIENT, SoundSource.BLOCKS, 0.2F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);
                }
            }
        }
    }
}