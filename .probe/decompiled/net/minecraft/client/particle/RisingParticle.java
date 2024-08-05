package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public abstract class RisingParticle extends TextureSheetParticle {

    protected RisingParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.f_172258_ = 0.96F;
        this.f_107215_ = this.f_107215_ * 0.01F + double4;
        this.f_107216_ = this.f_107216_ * 0.01F + double5;
        this.f_107217_ = this.f_107217_ * 0.01F + double6;
        this.f_107212_ = this.f_107212_ + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 0.05F);
        this.f_107213_ = this.f_107213_ + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 0.05F);
        this.f_107214_ = this.f_107214_ + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 0.05F);
        this.f_107225_ = (int) (8.0 / (Math.random() * 0.8 + 0.2)) + 4;
    }
}