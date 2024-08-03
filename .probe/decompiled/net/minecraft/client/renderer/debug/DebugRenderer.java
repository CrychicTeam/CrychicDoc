package net.minecraft.client.renderer.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class DebugRenderer {

    public final PathfindingRenderer pathfindingRenderer = new PathfindingRenderer();

    public final DebugRenderer.SimpleDebugRenderer waterDebugRenderer;

    public final DebugRenderer.SimpleDebugRenderer chunkBorderRenderer;

    public final DebugRenderer.SimpleDebugRenderer heightMapRenderer;

    public final DebugRenderer.SimpleDebugRenderer collisionBoxRenderer;

    public final DebugRenderer.SimpleDebugRenderer supportBlockRenderer;

    public final DebugRenderer.SimpleDebugRenderer neighborsUpdateRenderer;

    public final StructureRenderer structureRenderer;

    public final DebugRenderer.SimpleDebugRenderer lightDebugRenderer;

    public final DebugRenderer.SimpleDebugRenderer worldGenAttemptRenderer;

    public final DebugRenderer.SimpleDebugRenderer solidFaceRenderer;

    public final DebugRenderer.SimpleDebugRenderer chunkRenderer;

    public final BrainDebugRenderer brainDebugRenderer;

    public final VillageSectionsDebugRenderer villageSectionsDebugRenderer;

    public final BeeDebugRenderer beeDebugRenderer;

    public final RaidDebugRenderer raidDebugRenderer;

    public final GoalSelectorDebugRenderer goalSelectorRenderer;

    public final GameTestDebugRenderer gameTestDebugRenderer;

    public final GameEventListenerRenderer gameEventListenerRenderer;

    public final LightSectionDebugRenderer skyLightSectionDebugRenderer;

    private boolean renderChunkborder;

    public DebugRenderer(Minecraft minecraft0) {
        this.waterDebugRenderer = new WaterDebugRenderer(minecraft0);
        this.chunkBorderRenderer = new ChunkBorderRenderer(minecraft0);
        this.heightMapRenderer = new HeightMapRenderer(minecraft0);
        this.collisionBoxRenderer = new CollisionBoxRenderer(minecraft0);
        this.supportBlockRenderer = new SupportBlockRenderer(minecraft0);
        this.neighborsUpdateRenderer = new NeighborsUpdateRenderer(minecraft0);
        this.structureRenderer = new StructureRenderer(minecraft0);
        this.lightDebugRenderer = new LightDebugRenderer(minecraft0);
        this.worldGenAttemptRenderer = new WorldGenAttemptRenderer();
        this.solidFaceRenderer = new SolidFaceRenderer(minecraft0);
        this.chunkRenderer = new ChunkDebugRenderer(minecraft0);
        this.brainDebugRenderer = new BrainDebugRenderer(minecraft0);
        this.villageSectionsDebugRenderer = new VillageSectionsDebugRenderer();
        this.beeDebugRenderer = new BeeDebugRenderer(minecraft0);
        this.raidDebugRenderer = new RaidDebugRenderer(minecraft0);
        this.goalSelectorRenderer = new GoalSelectorDebugRenderer(minecraft0);
        this.gameTestDebugRenderer = new GameTestDebugRenderer();
        this.gameEventListenerRenderer = new GameEventListenerRenderer(minecraft0);
        this.skyLightSectionDebugRenderer = new LightSectionDebugRenderer(minecraft0, LightLayer.SKY);
    }

    public void clear() {
        this.pathfindingRenderer.m_5630_();
        this.waterDebugRenderer.clear();
        this.chunkBorderRenderer.clear();
        this.heightMapRenderer.clear();
        this.collisionBoxRenderer.clear();
        this.supportBlockRenderer.clear();
        this.neighborsUpdateRenderer.clear();
        this.structureRenderer.clear();
        this.lightDebugRenderer.clear();
        this.worldGenAttemptRenderer.clear();
        this.solidFaceRenderer.clear();
        this.chunkRenderer.clear();
        this.brainDebugRenderer.clear();
        this.villageSectionsDebugRenderer.clear();
        this.beeDebugRenderer.clear();
        this.raidDebugRenderer.m_5630_();
        this.goalSelectorRenderer.clear();
        this.gameTestDebugRenderer.clear();
        this.gameEventListenerRenderer.m_5630_();
        this.skyLightSectionDebugRenderer.m_5630_();
    }

    public boolean switchRenderChunkborder() {
        this.renderChunkborder = !this.renderChunkborder;
        return this.renderChunkborder;
    }

    public void render(PoseStack poseStack0, MultiBufferSource.BufferSource multiBufferSourceBufferSource1, double double2, double double3, double double4) {
        if (this.renderChunkborder && !Minecraft.getInstance().showOnlyReducedInfo()) {
            this.chunkBorderRenderer.render(poseStack0, multiBufferSourceBufferSource1, double2, double3, double4);
        }
        this.gameTestDebugRenderer.render(poseStack0, multiBufferSourceBufferSource1, double2, double3, double4);
    }

    public static Optional<Entity> getTargetedEntity(@Nullable Entity entity0, int int1) {
        if (entity0 == null) {
            return Optional.empty();
        } else {
            Vec3 $$2 = entity0.getEyePosition();
            Vec3 $$3 = entity0.getViewVector(1.0F).scale((double) int1);
            Vec3 $$4 = $$2.add($$3);
            AABB $$5 = entity0.getBoundingBox().expandTowards($$3).inflate(1.0);
            int $$6 = int1 * int1;
            Predicate<Entity> $$7 = p_113447_ -> !p_113447_.isSpectator() && p_113447_.isPickable();
            EntityHitResult $$8 = ProjectileUtil.getEntityHitResult(entity0, $$2, $$4, $$5, $$7, (double) $$6);
            if ($$8 == null) {
                return Optional.empty();
            } else {
                return $$2.distanceToSqr($$8.m_82450_()) > (double) $$6 ? Optional.empty() : Optional.of($$8.getEntity());
            }
        }
    }

    public static void renderFilledBox(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2, BlockPos blockPos3, float float4, float float5, float float6, float float7) {
        Camera $$8 = Minecraft.getInstance().gameRenderer.getMainCamera();
        if ($$8.isInitialized()) {
            Vec3 $$9 = $$8.getPosition().reverse();
            AABB $$10 = new AABB(blockPos2, blockPos3).move($$9);
            renderFilledBox(poseStack0, multiBufferSource1, $$10, float4, float5, float6, float7);
        }
    }

    public static void renderFilledBox(PoseStack poseStack0, MultiBufferSource multiBufferSource1, BlockPos blockPos2, float float3, float float4, float float5, float float6, float float7) {
        Camera $$8 = Minecraft.getInstance().gameRenderer.getMainCamera();
        if ($$8.isInitialized()) {
            Vec3 $$9 = $$8.getPosition().reverse();
            AABB $$10 = new AABB(blockPos2).move($$9).inflate((double) float3);
            renderFilledBox(poseStack0, multiBufferSource1, $$10, float4, float5, float6, float7);
        }
    }

    public static void renderFilledBox(PoseStack poseStack0, MultiBufferSource multiBufferSource1, AABB aABB2, float float3, float float4, float float5, float float6) {
        renderFilledBox(poseStack0, multiBufferSource1, aABB2.minX, aABB2.minY, aABB2.minZ, aABB2.maxX, aABB2.maxY, aABB2.maxZ, float3, float4, float5, float6);
    }

    public static void renderFilledBox(PoseStack poseStack0, MultiBufferSource multiBufferSource1, double double2, double double3, double double4, double double5, double double6, double double7, float float8, float float9, float float10, float float11) {
        VertexConsumer $$12 = multiBufferSource1.getBuffer(RenderType.debugFilledBox());
        LevelRenderer.addChainedFilledBoxVertices(poseStack0, $$12, double2, double3, double4, double5, double6, double7, float8, float9, float10, float11);
    }

    public static void renderFloatingText(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, int int3, int int4, int int5, int int6) {
        renderFloatingText(poseStack0, multiBufferSource1, string2, (double) int3 + 0.5, (double) int4 + 0.5, (double) int5 + 0.5, int6);
    }

    public static void renderFloatingText(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, double double3, double double4, double double5, int int6) {
        renderFloatingText(poseStack0, multiBufferSource1, string2, double3, double4, double5, int6, 0.02F);
    }

    public static void renderFloatingText(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, double double3, double double4, double double5, int int6, float float7) {
        renderFloatingText(poseStack0, multiBufferSource1, string2, double3, double4, double5, int6, float7, true, 0.0F, false);
    }

    public static void renderFloatingText(PoseStack poseStack0, MultiBufferSource multiBufferSource1, String string2, double double3, double double4, double double5, int int6, float float7, boolean boolean8, float float9, boolean boolean10) {
        Minecraft $$11 = Minecraft.getInstance();
        Camera $$12 = $$11.gameRenderer.getMainCamera();
        if ($$12.isInitialized() && $$11.getEntityRenderDispatcher().options != null) {
            Font $$13 = $$11.font;
            double $$14 = $$12.getPosition().x;
            double $$15 = $$12.getPosition().y;
            double $$16 = $$12.getPosition().z;
            poseStack0.pushPose();
            poseStack0.translate((float) (double3 - $$14), (float) (double4 - $$15) + 0.07F, (float) (double5 - $$16));
            poseStack0.mulPoseMatrix(new Matrix4f().rotation($$12.rotation()));
            poseStack0.scale(-float7, -float7, float7);
            float $$17 = boolean8 ? (float) (-$$13.width(string2)) / 2.0F : 0.0F;
            $$17 -= float9 / float7;
            $$13.drawInBatch(string2, $$17, 0.0F, int6, false, poseStack0.last().pose(), multiBufferSource1, boolean10 ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, 15728880);
            poseStack0.popPose();
        }
    }

    public interface SimpleDebugRenderer {

        void render(PoseStack var1, MultiBufferSource var2, double var3, double var5, double var7);

        default void clear() {
        }
    }
}