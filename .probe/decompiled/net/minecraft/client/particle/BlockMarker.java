package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.state.BlockState;

public class BlockMarker extends TextureSheetParticle {

    BlockMarker(ClientLevel clientLevel0, double double1, double double2, double double3, BlockState blockState4) {
        super(clientLevel0, double1, double2, double3);
        this.m_108337_(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState4));
        this.f_107226_ = 0.0F;
        this.f_107225_ = 80;
        this.f_107219_ = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    public float getQuadSize(float float0) {
        return 0.5F;
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {

        public Particle createParticle(BlockParticleOption blockParticleOption0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new BlockMarker(clientLevel1, double2, double3, double4, blockParticleOption0.getState());
        }
    }
}