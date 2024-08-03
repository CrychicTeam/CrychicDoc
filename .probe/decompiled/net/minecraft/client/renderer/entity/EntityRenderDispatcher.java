package net.minecraft.client.renderer.entity;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class EntityRenderDispatcher implements ResourceManagerReloadListener {

    private static final RenderType SHADOW_RENDER_TYPE = RenderType.entityShadow(new ResourceLocation("textures/misc/shadow.png"));

    private static final float MAX_SHADOW_RADIUS = 32.0F;

    private static final float SHADOW_POWER_FALLOFF_Y = 0.5F;

    private Map<EntityType<?>, EntityRenderer<?>> renderers = ImmutableMap.of();

    private Map<String, EntityRenderer<? extends Player>> playerRenderers = ImmutableMap.of();

    public final TextureManager textureManager;

    private Level level;

    public Camera camera;

    private Quaternionf cameraOrientation;

    public Entity crosshairPickEntity;

    private final ItemRenderer itemRenderer;

    private final BlockRenderDispatcher blockRenderDispatcher;

    private final ItemInHandRenderer itemInHandRenderer;

    private final Font font;

    public final Options options;

    private final EntityModelSet entityModels;

    private boolean shouldRenderShadow = true;

    private boolean renderHitBoxes;

    public <E extends Entity> int getPackedLightCoords(E e0, float float1) {
        return this.getRenderer(e0).getPackedLightCoords(e0, float1);
    }

    public EntityRenderDispatcher(Minecraft minecraft0, TextureManager textureManager1, ItemRenderer itemRenderer2, BlockRenderDispatcher blockRenderDispatcher3, Font font4, Options options5, EntityModelSet entityModelSet6) {
        this.textureManager = textureManager1;
        this.itemRenderer = itemRenderer2;
        this.itemInHandRenderer = new ItemInHandRenderer(minecraft0, this, itemRenderer2);
        this.blockRenderDispatcher = blockRenderDispatcher3;
        this.font = font4;
        this.options = options5;
        this.entityModels = entityModelSet6;
    }

    public <T extends Entity> EntityRenderer<? super T> getRenderer(T t0) {
        if (t0 instanceof AbstractClientPlayer) {
            String $$1 = ((AbstractClientPlayer) t0).getModelName();
            EntityRenderer<? extends Player> $$2 = (EntityRenderer<? extends Player>) this.playerRenderers.get($$1);
            return (EntityRenderer<? super T>) ($$2 != null ? $$2 : (EntityRenderer) this.playerRenderers.get("default"));
        } else {
            return (EntityRenderer<? super T>) this.renderers.get(t0.getType());
        }
    }

    public void prepare(Level level0, Camera camera1, Entity entity2) {
        this.level = level0;
        this.camera = camera1;
        this.cameraOrientation = camera1.rotation();
        this.crosshairPickEntity = entity2;
    }

    public void overrideCameraOrientation(Quaternionf quaternionf0) {
        this.cameraOrientation = quaternionf0;
    }

    public void setRenderShadow(boolean boolean0) {
        this.shouldRenderShadow = boolean0;
    }

    public void setRenderHitBoxes(boolean boolean0) {
        this.renderHitBoxes = boolean0;
    }

    public boolean shouldRenderHitBoxes() {
        return this.renderHitBoxes;
    }

    public <E extends Entity> boolean shouldRender(E e0, Frustum frustum1, double double2, double double3, double double4) {
        EntityRenderer<? super E> $$5 = this.getRenderer(e0);
        return $$5.shouldRender(e0, frustum1, double2, double3, double4);
    }

    public <E extends Entity> void render(E e0, double double1, double double2, double double3, float float4, float float5, PoseStack poseStack6, MultiBufferSource multiBufferSource7, int int8) {
        EntityRenderer<? super E> $$9 = this.getRenderer(e0);
        try {
            Vec3 $$10 = $$9.getRenderOffset(e0, float5);
            double $$11 = double1 + $$10.x();
            double $$12 = double2 + $$10.y();
            double $$13 = double3 + $$10.z();
            poseStack6.pushPose();
            poseStack6.translate($$11, $$12, $$13);
            $$9.render(e0, float4, float5, poseStack6, multiBufferSource7, int8);
            if (e0.displayFireAnimation()) {
                this.renderFlame(poseStack6, multiBufferSource7, e0);
            }
            poseStack6.translate(-$$10.x(), -$$10.y(), -$$10.z());
            if (this.options.entityShadows().get() && this.shouldRenderShadow && $$9.shadowRadius > 0.0F && !e0.isInvisible()) {
                double $$14 = this.distanceToSqr(e0.getX(), e0.getY(), e0.getZ());
                float $$15 = (float) ((1.0 - $$14 / 256.0) * (double) $$9.shadowStrength);
                if ($$15 > 0.0F) {
                    renderShadow(poseStack6, multiBufferSource7, e0, $$15, float5, this.level, Math.min($$9.shadowRadius, 32.0F));
                }
            }
            if (this.renderHitBoxes && !e0.isInvisible() && !Minecraft.getInstance().showOnlyReducedInfo()) {
                renderHitbox(poseStack6, multiBufferSource7.getBuffer(RenderType.lines()), e0, float5);
            }
            poseStack6.popPose();
        } catch (Throwable var24) {
            CrashReport $$17 = CrashReport.forThrowable(var24, "Rendering entity in world");
            CrashReportCategory $$18 = $$17.addCategory("Entity being rendered");
            e0.fillCrashReportCategory($$18);
            CrashReportCategory $$19 = $$17.addCategory("Renderer details");
            $$19.setDetail("Assigned renderer", $$9);
            $$19.setDetail("Location", CrashReportCategory.formatLocation(this.level, double1, double2, double3));
            $$19.setDetail("Rotation", float4);
            $$19.setDetail("Delta", float5);
            throw new ReportedException($$17);
        }
    }

    private static void renderHitbox(PoseStack poseStack0, VertexConsumer vertexConsumer1, Entity entity2, float float3) {
        AABB $$4 = entity2.getBoundingBox().move(-entity2.getX(), -entity2.getY(), -entity2.getZ());
        LevelRenderer.renderLineBox(poseStack0, vertexConsumer1, $$4, 1.0F, 1.0F, 1.0F, 1.0F);
        if (entity2 instanceof EnderDragon) {
            double $$5 = -Mth.lerp((double) float3, entity2.xOld, entity2.getX());
            double $$6 = -Mth.lerp((double) float3, entity2.yOld, entity2.getY());
            double $$7 = -Mth.lerp((double) float3, entity2.zOld, entity2.getZ());
            for (EnderDragonPart $$8 : ((EnderDragon) entity2).getSubEntities()) {
                poseStack0.pushPose();
                double $$9 = $$5 + Mth.lerp((double) float3, $$8.f_19790_, $$8.m_20185_());
                double $$10 = $$6 + Mth.lerp((double) float3, $$8.f_19791_, $$8.m_20186_());
                double $$11 = $$7 + Mth.lerp((double) float3, $$8.f_19792_, $$8.m_20189_());
                poseStack0.translate($$9, $$10, $$11);
                LevelRenderer.renderLineBox(poseStack0, vertexConsumer1, $$8.m_20191_().move(-$$8.m_20185_(), -$$8.m_20186_(), -$$8.m_20189_()), 0.25F, 1.0F, 0.0F, 1.0F);
                poseStack0.popPose();
            }
        }
        if (entity2 instanceof LivingEntity) {
            float $$12 = 0.01F;
            LevelRenderer.renderLineBox(poseStack0, vertexConsumer1, $$4.minX, (double) (entity2.getEyeHeight() - 0.01F), $$4.minZ, $$4.maxX, (double) (entity2.getEyeHeight() + 0.01F), $$4.maxZ, 1.0F, 0.0F, 0.0F, 1.0F);
        }
        Vec3 $$13 = entity2.getViewVector(float3);
        Matrix4f $$14 = poseStack0.last().pose();
        Matrix3f $$15 = poseStack0.last().normal();
        vertexConsumer1.vertex($$14, 0.0F, entity2.getEyeHeight(), 0.0F).color(0, 0, 255, 255).normal($$15, (float) $$13.x, (float) $$13.y, (float) $$13.z).endVertex();
        vertexConsumer1.vertex($$14, (float) ($$13.x * 2.0), (float) ((double) entity2.getEyeHeight() + $$13.y * 2.0), (float) ($$13.z * 2.0)).color(0, 0, 255, 255).normal($$15, (float) $$13.x, (float) $$13.y, (float) $$13.z).endVertex();
    }

    private void renderFlame(PoseStack poseStack0, MultiBufferSource multiBufferSource1, Entity entity2) {
        TextureAtlasSprite $$3 = ModelBakery.FIRE_0.sprite();
        TextureAtlasSprite $$4 = ModelBakery.FIRE_1.sprite();
        poseStack0.pushPose();
        float $$5 = entity2.getBbWidth() * 1.4F;
        poseStack0.scale($$5, $$5, $$5);
        float $$6 = 0.5F;
        float $$7 = 0.0F;
        float $$8 = entity2.getBbHeight() / $$5;
        float $$9 = 0.0F;
        poseStack0.mulPose(Axis.YP.rotationDegrees(-this.camera.getYRot()));
        poseStack0.translate(0.0F, 0.0F, -0.3F + (float) ((int) $$8) * 0.02F);
        float $$10 = 0.0F;
        int $$11 = 0;
        VertexConsumer $$12 = multiBufferSource1.getBuffer(Sheets.cutoutBlockSheet());
        for (PoseStack.Pose $$13 = poseStack0.last(); $$8 > 0.0F; $$11++) {
            TextureAtlasSprite $$14 = $$11 % 2 == 0 ? $$3 : $$4;
            float $$15 = $$14.getU0();
            float $$16 = $$14.getV0();
            float $$17 = $$14.getU1();
            float $$18 = $$14.getV1();
            if ($$11 / 2 % 2 == 0) {
                float $$19 = $$17;
                $$17 = $$15;
                $$15 = $$19;
            }
            fireVertex($$13, $$12, $$6 - 0.0F, 0.0F - $$9, $$10, $$17, $$18);
            fireVertex($$13, $$12, -$$6 - 0.0F, 0.0F - $$9, $$10, $$15, $$18);
            fireVertex($$13, $$12, -$$6 - 0.0F, 1.4F - $$9, $$10, $$15, $$16);
            fireVertex($$13, $$12, $$6 - 0.0F, 1.4F - $$9, $$10, $$17, $$16);
            $$8 -= 0.45F;
            $$9 -= 0.45F;
            $$6 *= 0.9F;
            $$10 += 0.03F;
        }
        poseStack0.popPose();
    }

    private static void fireVertex(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float float5, float float6) {
        vertexConsumer1.vertex(poseStackPose0.pose(), float2, float3, float4).color(255, 255, 255, 255).uv(float5, float6).overlayCoords(0, 10).uv2(240).normal(poseStackPose0.normal(), 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void renderShadow(PoseStack poseStack0, MultiBufferSource multiBufferSource1, Entity entity2, float float3, float float4, LevelReader levelReader5, float float6) {
        float $$7 = float6;
        if (entity2 instanceof Mob $$8 && $$8.m_6162_()) {
            $$7 = float6 * 0.5F;
        }
        double $$9 = Mth.lerp((double) float4, entity2.xOld, entity2.getX());
        double $$10 = Mth.lerp((double) float4, entity2.yOld, entity2.getY());
        double $$11 = Mth.lerp((double) float4, entity2.zOld, entity2.getZ());
        float $$12 = Math.min(float3 / 0.5F, $$7);
        int $$13 = Mth.floor($$9 - (double) $$7);
        int $$14 = Mth.floor($$9 + (double) $$7);
        int $$15 = Mth.floor($$10 - (double) $$12);
        int $$16 = Mth.floor($$10);
        int $$17 = Mth.floor($$11 - (double) $$7);
        int $$18 = Mth.floor($$11 + (double) $$7);
        PoseStack.Pose $$19 = poseStack0.last();
        VertexConsumer $$20 = multiBufferSource1.getBuffer(SHADOW_RENDER_TYPE);
        BlockPos.MutableBlockPos $$21 = new BlockPos.MutableBlockPos();
        for (int $$22 = $$17; $$22 <= $$18; $$22++) {
            for (int $$23 = $$13; $$23 <= $$14; $$23++) {
                $$21.set($$23, 0, $$22);
                ChunkAccess $$24 = levelReader5.getChunk($$21);
                for (int $$25 = $$15; $$25 <= $$16; $$25++) {
                    $$21.setY($$25);
                    float $$26 = float3 - (float) ($$10 - (double) $$21.m_123342_()) * 0.5F;
                    renderBlockShadow($$19, $$20, $$24, levelReader5, $$21, $$9, $$10, $$11, $$7, $$26);
                }
            }
        }
    }

    private static void renderBlockShadow(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, ChunkAccess chunkAccess2, LevelReader levelReader3, BlockPos blockPos4, double double5, double double6, double double7, float float8, float float9) {
        BlockPos $$10 = blockPos4.below();
        BlockState $$11 = chunkAccess2.m_8055_($$10);
        if ($$11.m_60799_() != RenderShape.INVISIBLE && levelReader3.getMaxLocalRawBrightness(blockPos4) > 3) {
            if ($$11.m_60838_(chunkAccess2, $$10)) {
                VoxelShape $$12 = $$11.m_60808_(chunkAccess2, $$10);
                if (!$$12.isEmpty()) {
                    float $$13 = LightTexture.getBrightness(levelReader3.dimensionType(), levelReader3.getMaxLocalRawBrightness(blockPos4));
                    float $$14 = float9 * 0.5F * $$13;
                    if ($$14 >= 0.0F) {
                        if ($$14 > 1.0F) {
                            $$14 = 1.0F;
                        }
                        AABB $$15 = $$12.bounds();
                        double $$16 = (double) blockPos4.m_123341_() + $$15.minX;
                        double $$17 = (double) blockPos4.m_123341_() + $$15.maxX;
                        double $$18 = (double) blockPos4.m_123342_() + $$15.minY;
                        double $$19 = (double) blockPos4.m_123343_() + $$15.minZ;
                        double $$20 = (double) blockPos4.m_123343_() + $$15.maxZ;
                        float $$21 = (float) ($$16 - double5);
                        float $$22 = (float) ($$17 - double5);
                        float $$23 = (float) ($$18 - double6);
                        float $$24 = (float) ($$19 - double7);
                        float $$25 = (float) ($$20 - double7);
                        float $$26 = -$$21 / 2.0F / float8 + 0.5F;
                        float $$27 = -$$22 / 2.0F / float8 + 0.5F;
                        float $$28 = -$$24 / 2.0F / float8 + 0.5F;
                        float $$29 = -$$25 / 2.0F / float8 + 0.5F;
                        shadowVertex(poseStackPose0, vertexConsumer1, $$14, $$21, $$23, $$24, $$26, $$28);
                        shadowVertex(poseStackPose0, vertexConsumer1, $$14, $$21, $$23, $$25, $$26, $$29);
                        shadowVertex(poseStackPose0, vertexConsumer1, $$14, $$22, $$23, $$25, $$27, $$29);
                        shadowVertex(poseStackPose0, vertexConsumer1, $$14, $$22, $$23, $$24, $$27, $$28);
                    }
                }
            }
        }
    }

    private static void shadowVertex(PoseStack.Pose poseStackPose0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float float5, float float6, float float7) {
        Vector3f $$8 = poseStackPose0.pose().transformPosition(float3, float4, float5, new Vector3f());
        vertexConsumer1.vertex($$8.x(), $$8.y(), $$8.z(), 1.0F, 1.0F, 1.0F, float2, float6, float7, OverlayTexture.NO_OVERLAY, 15728880, 0.0F, 1.0F, 0.0F);
    }

    public void setLevel(@Nullable Level level0) {
        this.level = level0;
        if (level0 == null) {
            this.camera = null;
        }
    }

    public double distanceToSqr(Entity entity0) {
        return this.camera.getPosition().distanceToSqr(entity0.position());
    }

    public double distanceToSqr(double double0, double double1, double double2) {
        return this.camera.getPosition().distanceToSqr(double0, double1, double2);
    }

    public Quaternionf cameraOrientation() {
        return this.cameraOrientation;
    }

    public ItemInHandRenderer getItemInHandRenderer() {
        return this.itemInHandRenderer;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        EntityRendererProvider.Context $$1 = new EntityRendererProvider.Context(this, this.itemRenderer, this.blockRenderDispatcher, this.itemInHandRenderer, resourceManager0, this.entityModels, this.font);
        this.renderers = EntityRenderers.createEntityRenderers($$1);
        this.playerRenderers = EntityRenderers.createPlayerRenderers($$1);
    }
}