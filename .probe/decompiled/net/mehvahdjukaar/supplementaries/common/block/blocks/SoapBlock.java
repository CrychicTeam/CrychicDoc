package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SoapBlock extends Block {

    public SoapBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState pState, Level level, BlockPos pos, RandomSource random) {
    }

    @Override
    public boolean triggerEvent(BlockState pState, Level level, BlockPos pos, int pId, int pParam) {
        if (pId != 0) {
            return super.m_8133_(pState, level, pos, pId, pParam);
        } else {
            RandomSource r = level.random;
            for (int i = 0; i < 2; i++) {
                level.addParticle((ParticleOptions) ModParticles.SUDS_PARTICLE.get(), (double) ((float) pos.m_123341_() + r.nextFloat()), (double) pos.m_123342_() + 1.1, (double) ((float) pos.m_123343_() + r.nextFloat()), (0.5 - (double) r.nextFloat()) * 0.13F, (double) (r.nextFloat() * 0.1F), (0.5 - (double) r.nextFloat()) * 0.13F);
            }
            return true;
        }
    }

    @Override
    public void stepOn(Level level, BlockPos pPos, BlockState state, Entity entity) {
        RandomSource rand = level.random;
        if ((!level.isClientSide || entity instanceof LocalPlayer) && !entity.isSteppingCarefully() && (double) rand.nextFloat() < 0.14) {
            Vec3 m = entity.getDeltaMovement();
            m.subtract(0.0, m.y, 0.0);
            if (m.lengthSqr() > 8.0E-4) {
                m = m.normalize().scale(0.1 + (double) (rand.nextFloat() * 0.13F));
                float min = 22.0F;
                float max = 95.0F;
                float angle = 20.0F + rand.nextFloat() * (max - min);
                angle *= rand.nextBoolean() ? -1.0F : 1.0F;
                m = m.yRot(angle * (float) (Math.PI / 180.0));
                entity.setDeltaMovement(entity.getDeltaMovement().add(m.x, 0.0, m.z));
                level.blockEvent(pPos, state.m_60734_(), 0, 0);
            }
        }
    }
}