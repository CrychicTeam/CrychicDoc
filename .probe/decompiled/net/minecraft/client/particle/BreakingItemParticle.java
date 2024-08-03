package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BreakingItemParticle extends TextureSheetParticle {

    private final float uo;

    private final float vo;

    BreakingItemParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, ItemStack itemStack7) {
        this(clientLevel0, double1, double2, double3, itemStack7);
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107215_ += double4;
        this.f_107216_ += double5;
        this.f_107217_ += double6;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.TERRAIN_SHEET;
    }

    protected BreakingItemParticle(ClientLevel clientLevel0, double double1, double double2, double double3, ItemStack itemStack4) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.m_108337_(Minecraft.getInstance().getItemRenderer().getModel(itemStack4, clientLevel0, null, 0).getParticleIcon());
        this.f_107226_ = 1.0F;
        this.f_107663_ /= 2.0F;
        this.uo = this.f_107223_.nextFloat() * 3.0F;
        this.vo = this.f_107223_.nextFloat() * 3.0F;
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

    public static class Provider implements ParticleProvider<ItemParticleOption> {

        public Particle createParticle(ItemParticleOption itemParticleOption0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new BreakingItemParticle(clientLevel1, double2, double3, double4, double5, double6, double7, itemParticleOption0.getItem());
        }
    }

    public static class SlimeProvider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new BreakingItemParticle(clientLevel1, double2, double3, double4, new ItemStack(Items.SLIME_BALL));
        }
    }

    public static class SnowballProvider implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new BreakingItemParticle(clientLevel1, double2, double3, double4, new ItemStack(Items.SNOWBALL));
        }
    }
}