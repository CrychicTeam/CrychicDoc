package team.lodestar.lodestone.systems.particle.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraftforge.client.model.data.ModelData;
import team.lodestar.lodestone.systems.particle.world.options.LodestoneItemCrumbsParticleOptions;

public class LodestoneItemCrumbParticle extends LodestoneWorldParticle {

    private final float uo;

    private final float vo;

    public LodestoneItemCrumbParticle(ClientLevel world, LodestoneItemCrumbsParticleOptions data, double x, double y, double z, double xd, double yd, double zd) {
        super(world, data, null, x, y, z, xd, yd, zd);
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(data.stack, world, null, 0);
        this.m_108337_(model.getOverrides().resolve(model, data.stack, world, null, 0).getParticleIcon(ModelData.EMPTY));
        this.f_107663_ /= 2.0F;
        this.uo = this.f_107223_.nextFloat() * 3.0F;
        this.vo = this.f_107223_.nextFloat() * 3.0F;
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
        BlockPos blockpos = new BlockPos((int) this.f_107212_, (int) this.f_107213_, (int) this.f_107214_);
        return this.f_107208_.m_46805_(blockpos) ? LevelRenderer.getLightColor(this.f_107208_, blockpos) : 0;
    }
}