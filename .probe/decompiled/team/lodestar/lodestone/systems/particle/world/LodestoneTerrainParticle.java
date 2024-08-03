package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import team.lodestar.lodestone.systems.particle.world.options.LodestoneTerrainParticleOptions;

public class LodestoneTerrainParticle extends LodestoneWorldParticle {

    private final BlockPos blockPos;

    private final float uo;

    private final float vo;

    public LodestoneTerrainParticle(ClientLevel world, LodestoneTerrainParticleOptions data, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, null, x, y, z, xd, yd, zd);
        this.blockPos = data.blockPos;
        if (!data.blockState.m_60713_(Blocks.GRASS_BLOCK)) {
            int i = Minecraft.getInstance().getBlockColors().getColor(data.blockState, world, data.blockPos, 0);
            this.f_107227_ *= (float) (i >> 16 & 0xFF) / 255.0F;
            this.f_107228_ *= (float) (i >> 8 & 0xFF) / 255.0F;
            this.f_107229_ *= (float) (i & 0xFF) / 255.0F;
        }
        this.f_107663_ /= 2.0F;
        this.uo = this.f_107223_.nextFloat() * 3.0F;
        this.vo = this.f_107223_.nextFloat() * 3.0F;
        this.m_108337_(Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(data.blockState));
    }

    @Override
    public float getU0() {
        return this.f_108321_.getU((double) ((this.uo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public float getU1() {
        return this.f_108321_.getU((double) (this.uo / 4.0F * 16.0F));
    }

    @Override
    public float getV0() {
        return this.f_108321_.getV((double) (this.vo / 4.0F * 16.0F));
    }

    @Override
    public float getV1() {
        return this.f_108321_.getV((double) ((this.vo + 1.0F) / 4.0F * 16.0F));
    }

    @Override
    public int getLightColor(float pPartialTick) {
        int i = super.getLightColor(pPartialTick);
        return i == 0 && this.f_107208_.m_46805_(this.blockPos) ? LevelRenderer.getLightColor(this.f_107208_, this.blockPos) : i;
    }
}