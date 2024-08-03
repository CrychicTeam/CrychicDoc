package com.simibubi.create.content.equipment.bell;

import com.jozufozu.flywheel.backend.ShadersModHandler;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class CustomRotationParticle extends SimpleAnimatedParticle {

    protected boolean mirror;

    protected int loopLength;

    public CustomRotationParticle(ClientLevel worldIn, double x, double y, double z, SpriteSet spriteSet, float yAccel) {
        super(worldIn, x, y, z, spriteSet, yAccel);
    }

    public void selectSpriteLoopingWithAge(SpriteSet sprite) {
        int loopFrame = this.f_107224_ % this.loopLength;
        this.m_108337_(sprite.get(loopFrame, this.loopLength));
    }

    public Quaternionf getCustomRotation(Camera camera, float partialTicks) {
        Quaternionf quaternion = new Quaternionf(camera.rotation());
        if (this.f_107231_ != 0.0F) {
            float angle = Mth.lerp(partialTicks, this.f_107204_, this.f_107231_);
            quaternion.mul(Axis.ZP.rotation(angle));
        }
        return quaternion;
    }

    @Override
    public void render(VertexConsumer builder, Camera camera, float partialTicks) {
        Vec3 cameraPos = camera.getPosition();
        float originX = (float) (Mth.lerp((double) partialTicks, this.f_107209_, this.f_107212_) - cameraPos.x());
        float originY = (float) (Mth.lerp((double) partialTicks, this.f_107210_, this.f_107213_) - cameraPos.y());
        float originZ = (float) (Mth.lerp((double) partialTicks, this.f_107211_, this.f_107214_) - cameraPos.z());
        Vector3f[] vertices = new Vector3f[] { new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F) };
        float scale = this.m_5902_(partialTicks);
        Quaternionf rotation = this.getCustomRotation(camera, partialTicks);
        for (int i = 0; i < 4; i++) {
            Vector3f vertex = vertices[i];
            vertex.rotate(rotation);
            vertex.mul(scale);
            vertex.add(originX, originY, originZ);
        }
        float minU = this.mirror ? this.m_5952_() : this.m_5970_();
        float maxU = this.mirror ? this.m_5970_() : this.m_5952_();
        float minV = this.m_5951_();
        float maxV = this.m_5950_();
        int brightness = ShadersModHandler.isShaderPackInUse() ? LightTexture.pack(12, 15) : this.m_6355_(partialTicks);
        builder.vertex((double) vertices[0].x(), (double) vertices[0].y(), (double) vertices[0].z()).uv(maxU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(brightness).endVertex();
        builder.vertex((double) vertices[1].x(), (double) vertices[1].y(), (double) vertices[1].z()).uv(maxU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(brightness).endVertex();
        builder.vertex((double) vertices[2].x(), (double) vertices[2].y(), (double) vertices[2].z()).uv(minU, minV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(brightness).endVertex();
        builder.vertex((double) vertices[3].x(), (double) vertices[3].y(), (double) vertices[3].z()).uv(minU, maxV).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv2(brightness).endVertex();
    }
}