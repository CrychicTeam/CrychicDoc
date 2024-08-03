package dev.xkmc.l2library.init.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.datafixers.util.Pair;
import dev.xkmc.l2library.base.effects.ClientEffectCap;
import dev.xkmc.l2library.base.effects.EffectToClient;
import dev.xkmc.l2library.base.effects.api.ClientRenderEffect;
import dev.xkmc.l2library.base.effects.api.DelayedEntityRender;
import dev.xkmc.l2library.base.effects.api.FirstPlayerRenderEffect;
import dev.xkmc.l2library.base.effects.api.IconRenderRegion;
import dev.xkmc.l2library.util.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "l2library", bus = Bus.FORGE)
public class ClientEffectRenderEvents {

    private static final ArrayList<DelayedEntityRender> ICONS = new ArrayList();

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            AbstractClientPlayer player = Proxy.getClientPlayer();
            if (player != null) {
                for (Entry<MobEffect, MobEffectInstance> entry : player.m_21221_().entrySet()) {
                    if (entry.getKey() instanceof FirstPlayerRenderEffect effect) {
                        effect.onClientLevelRender(player, (MobEffectInstance) entry.getValue());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void levelRenderLast(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            LevelRenderer renderer = event.getLevelRenderer();
            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            PoseStack stack = event.getPoseStack();
            PoseStack posestack = RenderSystem.getModelViewStack();
            PoseStack.Pose last = posestack.last();
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.disableDepthTest();
            for (DelayedEntityRender icon : ICONS) {
                renderIcon(stack, buffers, icon, event.getPartialTick(), camera, renderer.entityRenderDispatcher);
            }
            buffers.endBatch();
            posestack.pushPose();
            posestack.setIdentity();
            posestack.last().pose().mul(last.pose());
            posestack.last().normal().mul(last.normal());
            RenderSystem.applyModelViewMatrix();
            RenderSystem.enableDepthTest();
            ICONS.clear();
        }
    }

    @SubscribeEvent
    public static void onLivingEntityRender(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        onLivingRenderEvents(entity);
    }

    public static void onLivingRenderEvents(LivingEntity entity) {
        if (ClientEffectCap.HOLDER.isProper(entity)) {
            if (!entity.m_19880_().contains("ClientOnly")) {
                ClientEffectCap cap = (ClientEffectCap) ClientEffectCap.HOLDER.get(entity);
                List<Pair<ClientRenderEffect, Integer>> l0 = new ArrayList();
                for (Entry<MobEffect, Integer> entry : cap.map.entrySet()) {
                    if (entry.getKey() instanceof ClientRenderEffect effect) {
                        l0.add(Pair.of(effect, (Integer) entry.getValue()));
                    }
                }
                if (!l0.isEmpty()) {
                    List<Pair<Integer, DelayedEntityRender>> l1 = new ArrayList();
                    int index = 0;
                    for (Pair<ClientRenderEffect, Integer> e : l0) {
                        int lv = (Integer) e.getSecond();
                        int size = l1.size();
                        int I = index;
                        ((ClientRenderEffect) e.getFirst()).render(entity, lv, x -> l1.add(Pair.of(I, x)));
                        if (l1.size() > size) {
                            index++;
                        }
                    }
                    int n = index;
                    int w = (int) Math.ceil(Math.sqrt((double) index));
                    int h = (int) Math.ceil((double) index * 1.0 / (double) w);
                    for (Pair<Integer, DelayedEntityRender> ex : l1) {
                        int i = (Integer) ex.getFirst();
                        int iy = i / w;
                        int iw = Math.min(w, n - iy * w);
                        int ix = i - iy * w;
                        ICONS.add(((DelayedEntityRender) ex.getSecond()).resize(IconRenderRegion.of(w, ix, iy, iw, h)));
                    }
                }
            }
        }
    }

    private static void renderIcon(PoseStack pose, MultiBufferSource buffer, DelayedEntityRender icon, float partial, Camera camera, EntityRenderDispatcher dispatcher) {
        LivingEntity entity = icon.entity();
        float f = entity.m_20206_() / 2.0F;
        double x0 = Mth.lerp((double) partial, entity.f_19790_, entity.m_20185_());
        double y0 = Mth.lerp((double) partial, entity.f_19791_, entity.m_20186_());
        double z0 = Mth.lerp((double) partial, entity.f_19792_, entity.m_20189_());
        Vec3 offset = dispatcher.getRenderer(entity).getRenderOffset(entity, partial);
        Vec3 cam_pos = camera.getPosition();
        double d2 = x0 - cam_pos.x + offset.x();
        double d3 = y0 - cam_pos.y + offset.y();
        double d0 = z0 - cam_pos.z + offset.z();
        pose.pushPose();
        pose.translate(d2, d3 + (double) f, d0);
        pose.mulPose(camera.rotation());
        PoseStack.Pose entry = pose.last();
        VertexConsumer ivertexbuilder = buffer.getBuffer(get2DIcon(icon.rl()));
        float ix0 = -0.5F + icon.region().x();
        float ix1 = ix0 + icon.region().scale();
        float iy0 = -0.5F + icon.region().y();
        float iy1 = iy0 + icon.region().scale();
        float u0 = icon.tx();
        float v0 = icon.ty();
        float u1 = icon.tx() + icon.tw();
        float v1 = icon.ty() + icon.th();
        iconVertex(entry, ivertexbuilder, ix1, iy0, u0, v1);
        iconVertex(entry, ivertexbuilder, ix0, iy0, u1, v1);
        iconVertex(entry, ivertexbuilder, ix0, iy1, u1, v0);
        iconVertex(entry, ivertexbuilder, ix1, iy1, u0, v0);
        pose.popPose();
    }

    private static void iconVertex(PoseStack.Pose entry, VertexConsumer builder, float x, float y, float u, float v) {
        builder.vertex(entry.pose(), x, y, 0.0F).uv(u, v).normal(entry.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static RenderType get2DIcon(ResourceLocation rl) {
        return RenderType.create("entity_body_icon", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RenderStateShard.RENDERTYPE_ENTITY_GLINT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(rl, false, false)).setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY).setDepthTestState(RenderStateShard.NO_DEPTH_TEST).createCompositeState(false));
    }

    public static void sync(EffectToClient eff) {
        if (Minecraft.getInstance().level != null) {
            if (Minecraft.getInstance().level.getEntity(eff.entity) instanceof LivingEntity le) {
                if (ClientEffectCap.HOLDER.isProper(le)) {
                    ClientEffectCap cap = (ClientEffectCap) ClientEffectCap.HOLDER.get(le);
                    if (eff.exist) {
                        cap.map.put(eff.effect, eff.level);
                    } else {
                        cap.map.remove(eff.effect);
                    }
                }
            }
        }
    }
}