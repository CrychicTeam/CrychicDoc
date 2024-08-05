package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;

public class UraniumFullBlock extends RotatedPillarBlock {

    public UraniumFullBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(3.5F).lightLevel(state -> 4).emissiveRendering((state, level, pos) -> true).sound(ACSoundTypes.URANIUM));
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        if (randomSource.nextInt(80) == 0) {
            level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, ACSoundRegistry.URANIUM_HUM.get(), SoundSource.BLOCKS, 0.5F, randomSource.nextFloat() * 0.4F + 0.8F, false);
        }
        if (randomSource.nextInt(13) == 0) {
            Vec3 center = Vec3.upFromBottomCenterOf(pos, 0.5);
            level.addParticle(ACParticleRegistry.PROTON.get(), center.x, center.y, center.z, center.x, center.y, center.z);
        }
    }
}