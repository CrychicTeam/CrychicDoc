package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class SingleQuadParticle extends Particle {

    protected float quadSize = 0.1F * (this.f_107223_.nextFloat() * 0.5F + 0.5F) * 2.0F;

    protected SingleQuadParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3);
    }

    protected SingleQuadParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
    }

    @Override
    public void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
        Vec3 $$3 = camera1.getPosition();
        float $$4 = (float) (Mth.lerp((double) float2, this.f_107209_, this.f_107212_) - $$3.x());
        float $$5 = (float) (Mth.lerp((double) float2, this.f_107210_, this.f_107213_) - $$3.y());
        float $$6 = (float) (Mth.lerp((double) float2, this.f_107211_, this.f_107214_) - $$3.z());
        Quaternionf $$7;
        if (this.f_107231_ == 0.0F) {
            $$7 = camera1.rotation();
        } else {
            $$7 = new Quaternionf(camera1.rotation());
            $$7.rotateZ(Mth.lerp(float2, this.f_107204_, this.f_107231_));
        }
        Vector3f[] $$9 = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float $$10 = this.getQuadSize(float2);
        for (int $$11 = 0; $$11 < 4; $$11++) {
            Vector3f $$12 = $$9[$$11];
            $$12.rotate($$7);
            $$12.mul($$10);
            $$12.add($$4, $$5, $$6);
        }
        float $$13 = this.getU0();
        float $$14 = this.getU1();
        float $$15 = this.getV0();
        float $$16 = this.getV1();
        int $$17 = this.m_6355_(float2);
        vertexConsumer0.vertex((double) $$9[0].x(), (double) $$9[0].y(), (double) $$9[0].z()).uv($$14, $$16).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$17).endVertex();
        vertexConsumer0.vertex((double) $$9[1].x(), (double) $$9[1].y(), (double) $$9[1].z()).uv($$14, $$15).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$17).endVertex();
        vertexConsumer0.vertex((double) $$9[2].x(), (double) $$9[2].y(), (double) $$9[2].z()).uv($$13, $$15).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$17).endVertex();
        vertexConsumer0.vertex((double) $$9[3].x(), (double) $$9[3].y(), (double) $$9[3].z()).uv($$13, $$16).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2($$17).endVertex();
    }

    public float getQuadSize(float float0) {
        return this.quadSize;
    }

    @Override
    public Particle scale(float float0) {
        this.quadSize *= float0;
        return super.scale(float0);
    }

    protected abstract float getU0();

    protected abstract float getU1();

    protected abstract float getV0();

    protected abstract float getV1();
}