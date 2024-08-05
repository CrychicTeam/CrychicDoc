package me.jellysquid.mods.sodium.mixin.features.render.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.caffeinemc.mods.sodium.api.util.ColorABGR;
import net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter;
import net.caffeinemc.mods.sodium.api.vertex.format.common.ParticleVertex;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SingleQuadParticle.class })
public abstract class BillboardParticleMixin extends Particle {

    @Shadow
    public abstract float getQuadSize(float var1);

    @Shadow
    protected abstract float getU0();

    @Shadow
    protected abstract float getU1();

    @Shadow
    protected abstract float getV0();

    @Shadow
    protected abstract float getV1();

    protected BillboardParticleMixin(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Inject(method = { "render" }, at = { @At("HEAD") }, cancellable = true)
    private void buildGeometryFast(VertexConsumer vertexConsumer, Camera camera, float tickDelta, CallbackInfo ci) {
        VertexBufferWriter writer = VertexBufferWriter.tryOf(vertexConsumer);
        if (writer != null) {
            ci.cancel();
            Vec3 vec3d = camera.getPosition();
            float x = (float) (Mth.lerp((double) tickDelta, this.f_107209_, this.f_107212_) - vec3d.x());
            float y = (float) (Mth.lerp((double) tickDelta, this.f_107210_, this.f_107213_) - vec3d.y());
            float z = (float) (Mth.lerp((double) tickDelta, this.f_107211_, this.f_107214_) - vec3d.z());
            Quaternionf quaternion;
            if (this.f_107231_ == 0.0F) {
                quaternion = camera.rotation();
            } else {
                float angle = Mth.lerp(tickDelta, this.f_107204_, this.f_107231_);
                quaternion = new Quaternionf(camera.rotation());
                quaternion.rotateZ(angle);
            }
            float size = this.getQuadSize(tickDelta);
            int light = this.m_6355_(tickDelta);
            float minU = this.getU0();
            float maxU = this.getU1();
            float minV = this.getV0();
            float maxV = this.getV1();
            int color = ColorABGR.pack(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_);
            MemoryStack stack = MemoryStack.stackPush();
            try {
                long buffer = stack.nmalloc(112);
                writeVertex(buffer, quaternion, -1.0F, -1.0F, x, y, z, maxU, maxV, color, light, size);
                long ptr = buffer + 28L;
                writeVertex(ptr, quaternion, -1.0F, 1.0F, x, y, z, maxU, minV, color, light, size);
                ptr += 28L;
                writeVertex(ptr, quaternion, 1.0F, 1.0F, x, y, z, minU, minV, color, light, size);
                ptr += 28L;
                writeVertex(ptr, quaternion, 1.0F, -1.0F, x, y, z, minU, maxV, color, light, size);
                ptr += 28L;
                writer.push(stack, buffer, 4, ParticleVertex.FORMAT);
            } catch (Throwable var24) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var23) {
                        var24.addSuppressed(var23);
                    }
                }
                throw var24;
            }
            if (stack != null) {
                stack.close();
            }
        }
    }

    @Unique
    private static void writeVertex(long buffer, Quaternionf rotation, float posX, float posY, float originX, float originY, float originZ, float u, float v, int color, int light, float size) {
        float q0x = rotation.x();
        float q0y = rotation.y();
        float q0z = rotation.z();
        float q0w = rotation.w();
        float q1x = q0w * posX - q0z * posY;
        float q1y = q0w * posY + q0z * posX;
        float q1w = q0x * posY - q0y * posX;
        float q1z = -(q0x * posX) - q0y * posY;
        float q2x = -q0x;
        float q2y = -q0y;
        float q2z = -q0z;
        float q3x = q1z * q2x + q1x * q0w + q1y * q2z - q1w * q2y;
        float q3y = q1z * q2y - q1x * q2z + q1y * q0w + q1w * q2x;
        float q3z = q1z * q2z + q1x * q2y - q1y * q2x + q1w * q0w;
        float fx = q3x * size + originX;
        float fy = q3y * size + originY;
        float fz = q3z * size + originZ;
        ParticleVertex.put(buffer, fx, fy, fz, u, v, color, light);
    }
}