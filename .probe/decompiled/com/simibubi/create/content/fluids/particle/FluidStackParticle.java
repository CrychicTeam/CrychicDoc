package com.simibubi.create.content.fluids.particle;

import com.simibubi.create.AllParticleTypes;
import com.simibubi.create.content.fluids.potion.PotionFluid;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

public class FluidStackParticle extends TextureSheetParticle {

    private final float uo;

    private final float vo;

    private FluidStack fluid;

    private IClientFluidTypeExtensions clientFluid;

    public static FluidStackParticle create(ParticleType<FluidParticleData> type, ClientLevel world, FluidStack fluid, double x, double y, double z, double vx, double vy, double vz) {
        return (FluidStackParticle) (type == AllParticleTypes.BASIN_FLUID.get() ? new BasinFluidParticle(world, fluid, x, y, z, vx, vy, vz) : new FluidStackParticle(world, fluid, x, y, z, vx, vy, vz));
    }

    public FluidStackParticle(ClientLevel world, FluidStack fluid, double x, double y, double z, double vx, double vy, double vz) {
        super(world, x, y, z, vx, vy, vz);
        this.clientFluid = IClientFluidTypeExtensions.of(fluid.getFluid());
        this.fluid = fluid;
        this.m_108337_((TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(this.clientFluid.getStillTexture(fluid)));
        this.f_107226_ = 1.0F;
        this.f_107227_ = 0.8F;
        this.f_107228_ = 0.8F;
        this.f_107229_ = 0.8F;
        this.multiplyColor(this.clientFluid.getTintColor(fluid));
        this.f_107215_ = vx;
        this.f_107216_ = vy;
        this.f_107217_ = vz;
        this.f_107663_ /= 2.0F;
        this.uo = this.f_107223_.nextFloat() * 3.0F;
        this.vo = this.f_107223_.nextFloat() * 3.0F;
    }

    @Override
    protected int getLightColor(float p_189214_1_) {
        int brightnessForRender = super.m_6355_(p_189214_1_);
        int skyLight = brightnessForRender >> 20;
        int blockLight = brightnessForRender >> 4 & 15;
        blockLight = Math.max(blockLight, this.fluid.getFluid().getFluidType().getLightLevel(this.fluid));
        return skyLight << 20 | blockLight << 4;
    }

    protected void multiplyColor(int color) {
        this.f_107227_ *= (float) (color >> 16 & 0xFF) / 255.0F;
        this.f_107228_ *= (float) (color >> 8 & 0xFF) / 255.0F;
        this.f_107229_ *= (float) (color & 0xFF) / 255.0F;
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
    public void tick() {
        super.m_5989_();
        if (this.canEvaporate()) {
            if (this.f_107218_) {
                this.m_107274_();
            }
            if (this.f_107220_) {
                if (this.f_107218_ || !(this.f_107208_.f_46441_.nextFloat() < 0.125F)) {
                    Color color = new Color(this.clientFluid.getTintColor(this.fluid));
                    this.f_107208_.addParticle(ParticleTypes.ENTITY_EFFECT, this.f_107212_, this.f_107213_, this.f_107214_, (double) color.getRedAsFloat(), (double) color.getGreenAsFloat(), (double) color.getBlueAsFloat());
                }
            }
        }
    }

    protected boolean canEvaporate() {
        return this.fluid.getFluid() instanceof PotionFluid;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }
}