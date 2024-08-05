package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.github.alexthe666.iceandfire.client.render.pathfinding.PathfindingDebugRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;

public class WorldEventContext {

    public static final WorldEventContext INSTANCE = new WorldEventContext();

    public MultiBufferSource.BufferSource bufferSource;

    public PoseStack poseStack;

    public float partialTicks;

    public ClientLevel clientLevel;

    public LocalPlayer clientPlayer;

    public ItemStack mainHandItem;

    int clientRenderDist;

    private WorldEventContext() {
    }

    public void renderWorldLastEvent(RenderLevelStageEvent event) {
        this.bufferSource = WorldRenderMacros.getBufferSource();
        this.poseStack = event.getPoseStack();
        this.partialTicks = event.getPartialTick();
        this.clientLevel = Minecraft.getInstance().level;
        this.clientPlayer = Minecraft.getInstance().player;
        this.mainHandItem = this.clientPlayer.m_21205_();
        this.clientRenderDist = Minecraft.getInstance().options.renderDistance().get();
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        this.poseStack.pushPose();
        this.poseStack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_CUTOUT_MIPPED_BLOCKS_BLOCKS) {
            PathfindingDebugRenderer.render(this);
            this.bufferSource.endBatch();
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
            this.bufferSource.endBatch();
        }
        this.poseStack.popPose();
    }
}