package net.minecraft.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;

public class NoRenderParticle extends Particle {

    protected NoRenderParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3);
    }

    protected NoRenderParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
    }

    @Override
    public final void render(VertexConsumer vertexConsumer0, Camera camera1, float float2) {
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.NO_RENDER;
    }
}