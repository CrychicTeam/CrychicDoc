package team.lodestar.lodestone.events;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.joml.Matrix4f;
import team.lodestar.lodestone.LodestoneLib;
import team.lodestar.lodestone.capability.LodestonePlayerDataCapability;
import team.lodestar.lodestone.handlers.GhostBlockHandler;
import team.lodestar.lodestone.handlers.PlacementAssistantHandler;
import team.lodestar.lodestone.handlers.RenderHandler;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.handlers.WorldEventHandler;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.client.ClientTickCounter;

@EventBusSubscriber(value = { Dist.CLIENT }, bus = Bus.FORGE)
public class ClientRuntimeEvents {

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END)) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.level != null) {
                ClientTickCounter.clientTick();
                if (minecraft.isPaused()) {
                    return;
                }
                Camera camera = minecraft.gameRenderer.getMainCamera();
                GhostBlockHandler.tickGhosts();
                WorldEventHandler.tick(minecraft.level);
                PlacementAssistantHandler.tick(minecraft.player, minecraft.hitResult);
                ScreenshakeHandler.clientTick(camera, LodestoneLib.RANDOM);
                LodestonePlayerDataCapability.ClientOnly.clientTick(event);
                ScreenParticleHandler.tickParticles();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderFog(ViewportEvent.RenderFog event) {
        RenderHandler.cacheFogData(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void fogColors(ViewportEvent.ComputeFogColor event) {
        RenderHandler.cacheFogData(event);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void renderStages(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.getPosition();
        float partial = event.getPartialTick();
        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            GhostBlockHandler.renderGhosts(poseStack);
            WorldEventHandler.ClientOnly.renderWorldEvents(poseStack, partial);
        }
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_PARTICLES)) {
            RenderHandler.MATRIX4F = new Matrix4f(RenderSystem.getModelViewMatrix());
        }
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) {
            RenderHandler.endBatches();
        }
        poseStack.popPose();
    }

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        ScreenParticleHandler.renderTick(event);
        ClientTickCounter.renderTick(event);
    }
}