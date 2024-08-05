package com.simibubi.create.foundation.ponder;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;

public class PonderWorldParticles {

    private final Map<ParticleRenderType, Queue<Particle>> byType = Maps.newIdentityHashMap();

    private final Queue<Particle> queue = Queues.newArrayDeque();

    PonderWorld world;

    public PonderWorldParticles(PonderWorld world) {
        this.world = world;
    }

    public void addParticle(Particle p) {
        this.queue.add(p);
    }

    public void tick() {
        this.byType.forEach((p_228347_1_, p_228347_2_) -> this.tickParticleList(p_228347_2_));
        if (!this.queue.isEmpty()) {
            Particle particle;
            while ((particle = (Particle) this.queue.poll()) != null) {
                ((Queue) this.byType.computeIfAbsent(particle.getRenderType(), $ -> EvictingQueue.create(16384))).add(particle);
            }
        }
    }

    private void tickParticleList(Collection<Particle> p_187240_1_) {
        if (!p_187240_1_.isEmpty()) {
            Iterator<Particle> iterator = p_187240_1_.iterator();
            while (iterator.hasNext()) {
                Particle particle = (Particle) iterator.next();
                particle.tick();
                if (!particle.isAlive()) {
                    iterator.remove();
                }
            }
        }
    }

    public void renderParticles(PoseStack ms, MultiBufferSource buffer, Camera renderInfo, float pt) {
        Minecraft mc = Minecraft.getInstance();
        LightTexture lightTexture = mc.gameRenderer.lightTexture();
        lightTexture.turnOnLightLayer();
        RenderSystem.enableDepthTest();
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.mulPoseMatrix(ms.last().pose());
        RenderSystem.applyModelViewMatrix();
        for (ParticleRenderType iparticlerendertype : this.byType.keySet()) {
            if (iparticlerendertype != ParticleRenderType.NO_RENDER) {
                Iterable<Particle> iterable = (Iterable<Particle>) this.byType.get(iparticlerendertype);
                if (iterable != null) {
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.setShader(GameRenderer::m_172829_);
                    Tesselator tessellator = Tesselator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuilder();
                    iparticlerendertype.begin(bufferbuilder, mc.textureManager);
                    for (Particle particle : iterable) {
                        particle.render(bufferbuilder, renderInfo, pt);
                    }
                    iparticlerendertype.end(tessellator);
                }
            }
        }
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        lightTexture.turnOffLightLayer();
    }

    public void clearEffects() {
        this.byType.clear();
    }
}