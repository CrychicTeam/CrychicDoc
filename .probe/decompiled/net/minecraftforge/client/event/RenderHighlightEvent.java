package net.minecraftforge.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
public abstract class RenderHighlightEvent extends Event {

    private final LevelRenderer levelRenderer;

    private final Camera camera;

    private final HitResult target;

    private final float partialTick;

    private final PoseStack poseStack;

    private final MultiBufferSource multiBufferSource;

    @Internal
    protected RenderHighlightEvent(LevelRenderer levelRenderer, Camera camera, HitResult target, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource) {
        this.levelRenderer = levelRenderer;
        this.camera = camera;
        this.target = target;
        this.partialTick = partialTick;
        this.poseStack = poseStack;
        this.multiBufferSource = multiBufferSource;
    }

    public LevelRenderer getLevelRenderer() {
        return this.levelRenderer;
    }

    public Camera getCamera() {
        return this.camera;
    }

    public HitResult getTarget() {
        return this.target;
    }

    public float getPartialTick() {
        return this.partialTick;
    }

    public PoseStack getPoseStack() {
        return this.poseStack;
    }

    public MultiBufferSource getMultiBufferSource() {
        return this.multiBufferSource;
    }

    @Cancelable
    public static class Block extends RenderHighlightEvent {

        @Internal
        public Block(LevelRenderer levelRenderer, Camera camera, BlockHitResult target, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
            super(levelRenderer, camera, target, partialTick, poseStack, bufferSource);
        }

        public BlockHitResult getTarget() {
            return (BlockHitResult) super.target;
        }
    }

    public static class Entity extends RenderHighlightEvent {

        @Internal
        public Entity(LevelRenderer levelRenderer, Camera camera, EntityHitResult target, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
            super(levelRenderer, camera, target, partialTick, poseStack, bufferSource);
        }

        public EntityHitResult getTarget() {
            return (EntityHitResult) super.target;
        }
    }
}