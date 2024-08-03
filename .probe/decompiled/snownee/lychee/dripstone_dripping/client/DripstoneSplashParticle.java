package snownee.lychee.dripstone_dripping.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.world.level.material.Fluid;

public class DripstoneSplashParticle extends DripParticle {

    public DripstoneSplashParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, Fluid fluid) {
        super(clientWorld, d, e, f, fluid);
        this.f_107215_ *= 0.3F;
        this.f_107216_ = Math.random() * 0.2F + 0.1F;
        this.f_107217_ *= 0.3F;
        this.f_107226_ = 0.04F;
        if (h == 0.0 && (g != 0.0 || i != 0.0)) {
            this.f_107215_ = g;
            this.f_107216_ = 0.1;
            this.f_107217_ = i;
        }
        this.f_107225_ = (int) (8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        } else {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ *= 0.98F;
            this.f_107216_ *= 0.98F;
            this.f_107217_ *= 0.98F;
            if (this.f_107218_) {
                if (Math.random() < 0.5) {
                    this.m_107274_();
                }
                this.f_107215_ *= 0.7F;
                this.f_107217_ *= 0.7F;
            }
        }
    }
}