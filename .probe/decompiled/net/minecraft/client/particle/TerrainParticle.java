package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TerrainParticle extends TextureSheetParticle {

    private final BlockPos pos;

    private final float uo;

    private final float vo;

    public TerrainParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, BlockState blockState7) {
        this(clientLevel0, double1, double2, double3, double4, double5, double6, blockState7, BlockPos.containing(double1, double2, double3));
    }

    public TerrainParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, BlockState blockState7, BlockPos blockPos8) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.pos = blockPos8;
        this.m_108337_(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(blockState7));
        this.f_107226_ = 1.0F;
        this.f_107227_ = 0.6F;
        this.f_107228_ = 0.6F;
        this.f_107229_ = 0.6F;
        if (!blockState7.m_60713_(Blocks.GRASS_BLOCK)) {
            int $$9 = Minecraft.getInstance().getBlockColors().getColor(blockState7, clientLevel0, blockPos8, 0);
            this.f_107227_ *= (float) ($$9 >> 16 & 0xFF) / 255.0F;
            this.f_107228_ *= (float) ($$9 >> 8 & 0xFF) / 255.0F;
            this.f_107229_ *= (float) ($$9 & 0xFF) / 255.0F;
        }
        this.f_107663_ /= 2.0F;
        this.uo = this.f_107223_.nextFloat() * 3.0F;
        this.vo = this.f_107223_.nextFloat() * 3.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    @Override
    protected float getU0() {
        return this.f_108321_.getU((double) ((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    protected float getU1() {
        return this.f_108321_.getU((double) (this.uo / 4.0F * 16.0F));
    }

    @Override
    protected float getV0() {
        return this.f_108321_.getV((double) (this.vo / 4.0F * 16.0F));
    }

    @Override
    protected float getV1() {
        return this.f_108321_.getV((double) ((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public int getLightColor(float float0) {
        int $$1 = super.m_6355_(float0);
        return $$1 == 0 && this.f_107208_.m_46805_(this.pos) ? LevelRenderer.getLightColor(this.f_107208_, this.pos) : $$1;
    }

    public static class Provider implements ParticleProvider<BlockParticleOption> {

        public Particle createParticle(BlockParticleOption blockParticleOption0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            BlockState $$8 = blockParticleOption0.getState();
            return !$$8.m_60795_() && !$$8.m_60713_(Blocks.MOVING_PISTON) ? new TerrainParticle(clientLevel1, double2, double3, double4, double5, double6, double7, $$8) : null;
        }
    }
}