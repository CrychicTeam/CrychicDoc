package software.bernie.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import net.minecraftforge.common.MinecraftForge;
import org.joml.Matrix4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import software.bernie.geckolib.util.RenderUtils;

public class GeoReplacedEntityRenderer<E extends Entity, T extends GeoAnimatable> extends EntityRenderer<E> implements GeoRenderer<T> {

    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);

    protected final GeoModel<T> model;

    protected final T animatable;

    protected E currentEntity;

    protected float scaleWidth = 1.0F;

    protected float scaleHeight = 1.0F;

    protected Matrix4f entityRenderTranslations = new Matrix4f();

    protected Matrix4f modelRenderTranslations = new Matrix4f();

    public GeoReplacedEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, T animatable) {
        super(renderManager);
        this.model = model;
        this.animatable = animatable;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return this.model;
    }

    @Override
    public T getAnimatable() {
        return this.animatable;
    }

    public E getCurrentEntity() {
        return this.currentEntity;
    }

    @Override
    public long getInstanceId(T animatable) {
        return (long) this.currentEntity.getId();
    }

    @Override
    public ResourceLocation getTextureLocation(E entity) {
        return GeoRenderer.super.getTextureLocation(this.animatable);
    }

    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoReplacedEntityRenderer<E, T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);
        return this;
    }

    public GeoReplacedEntityRenderer<E, T> withScale(float scale) {
        return this.withScale(scale, scale);
    }

    public GeoReplacedEntityRenderer<E, T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        return this;
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.entityRenderTranslations = poseStack.last().pose();
        this.scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
    }

    @Override
    public void render(E entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        this.currentEntity = entity;
        this.defaultRender(poseStack, this.animatable, bufferSource, null, null, entityYaw, partialTick, packedLight);
    }

    @Override
    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        LivingEntity livingEntity = this.currentEntity instanceof LivingEntity entity ? entity : null;
        boolean shouldSit = this.currentEntity.isPassenger() && this.currentEntity.getVehicle() != null && this.currentEntity.getVehicle().shouldRiderSit();
        float lerpBodyRot = livingEntity == null ? 0.0F : Mth.rotLerp(partialTick, livingEntity.yBodyRotO, livingEntity.yBodyRot);
        float lerpHeadRot = livingEntity == null ? 0.0F : Mth.rotLerp(partialTick, livingEntity.yHeadRotO, livingEntity.yHeadRot);
        float netHeadYaw = lerpHeadRot - lerpBodyRot;
        if (shouldSit && this.currentEntity.getVehicle() instanceof LivingEntity livingentity) {
            lerpBodyRot = Mth.rotLerp(partialTick, livingentity.yBodyRotO, livingentity.yBodyRot);
            netHeadYaw = lerpHeadRot - lerpBodyRot;
            float clampedHeadYaw = Mth.clamp(Mth.wrapDegrees(netHeadYaw), -85.0F, 85.0F);
            lerpBodyRot = lerpHeadRot - clampedHeadYaw;
            if (clampedHeadYaw * clampedHeadYaw > 2500.0F) {
                lerpBodyRot += clampedHeadYaw * 0.2F;
            }
            netHeadYaw = lerpHeadRot - lerpBodyRot;
        }
        if (this.currentEntity.getPose() == Pose.SLEEPING && livingEntity != null) {
            Direction bedDirection = livingEntity.getBedOrientation();
            if (bedDirection != null) {
                float eyePosOffset = livingEntity.m_20236_(Pose.STANDING) - 0.1F;
                poseStack.translate((float) (-bedDirection.getStepX()) * eyePosOffset, 0.0F, (float) (-bedDirection.getStepZ()) * eyePosOffset);
            }
        }
        float ageInTicks = (float) this.currentEntity.tickCount + partialTick;
        float limbSwingAmount = 0.0F;
        float limbSwing = 0.0F;
        this.applyRotations(animatable, poseStack, ageInTicks, lerpBodyRot, partialTick);
        if (!shouldSit && this.currentEntity.isAlive() && livingEntity != null) {
            limbSwingAmount = livingEntity.walkAnimation.speed(partialTick);
            limbSwing = livingEntity.walkAnimation.position(partialTick);
            if (livingEntity.isBaby()) {
                limbSwing *= 3.0F;
            }
            if (limbSwingAmount > 1.0F) {
                limbSwingAmount = 1.0F;
            }
        }
        float headPitch = Mth.lerp(partialTick, this.currentEntity.xRotO, this.currentEntity.getXRot());
        float motionThreshold = this.getMotionAnimThreshold(animatable);
        boolean isMoving;
        if (livingEntity != null) {
            Vec3 velocity = livingEntity.m_20184_();
            float avgVelocity = (float) (Math.abs(velocity.x) + Math.abs(velocity.z)) / 2.0F;
            isMoving = avgVelocity >= motionThreshold && limbSwingAmount != 0.0F;
        } else {
            isMoving = limbSwingAmount <= -motionThreshold || limbSwingAmount >= motionThreshold;
        }
        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState<>(animatable, limbSwing, limbSwingAmount, partialTick, isMoving);
            long instanceId = this.getInstanceId(animatable);
            GeoModel<T> currentModel = this.getGeoModel();
            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(DataTickets.ENTITY_MODEL_DATA, new EntityModelData(shouldSit, livingEntity != null && livingEntity.isBaby(), -netHeadYaw, -headPitch));
            currentModel.addAdditionalStateData(animatable, instanceId, animationState::setData);
            currentModel.handleAnimations(animatable, instanceId, animationState);
        }
        poseStack.translate(0.0F, 0.01F, 0.0F);
        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());
        if (this.currentEntity.isInvisibleTo(Minecraft.getInstance().player)) {
            if (Minecraft.getInstance().shouldEntityAppearGlowing(this.currentEntity)) {
                buffer = bufferSource.getBuffer(renderType = RenderType.outline(this.getTextureLocation(animatable)));
            } else {
                renderType = null;
            }
        }
        if (renderType != null) {
            GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
        poseStack.popPose();
    }

    @Override
    public void applyRenderLayers(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (!this.currentEntity.isSpectator()) {
            GeoRenderer.super.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }
    }

    @Override
    public void renderFinal(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(this.currentEntity, 0.0F, partialTick, poseStack, bufferSource, packedLight);
        if (this.currentEntity instanceof Mob mob) {
            Entity leashHolder = mob.getLeashHolder();
            if (leashHolder != null) {
                this.renderLeash(mob, partialTick, poseStack, bufferSource, leashHolder);
            }
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        RenderUtils.translateMatrixToBone(poseStack, bone);
        RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.rotateMatrixAroundBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);
            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtils.translateMatrix(localMatrix, this.m_7860_(this.currentEntity, 1.0F).toVector3f()));
            bone.setWorldSpaceMatrix(RenderUtils.translateMatrix(new Matrix4f(localMatrix), this.currentEntity.position().toVector3f()));
        }
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        this.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        if (!isReRender) {
            this.applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }
        this.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        Pose pose = this.currentEntity.getPose();
        LivingEntity livingEntity = this.currentEntity instanceof LivingEntity entity ? entity : null;
        if (this.isShaking(animatable)) {
            rotationYaw += (float) (Math.cos((double) this.currentEntity.tickCount * 3.25) * Math.PI * 0.4);
        }
        if (pose != Pose.SLEEPING) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        }
        if (livingEntity != null && livingEntity.deathTime > 0) {
            float deathRotation = ((float) livingEntity.deathTime + partialTick - 1.0F) / 20.0F * 1.6F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(Mth.sqrt(deathRotation), 1.0F) * this.getDeathMaxRotation(animatable)));
        } else if (livingEntity != null && livingEntity.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - livingEntity.m_146909_()));
            poseStack.mulPose(Axis.YP.rotationDegrees(((float) livingEntity.f_19797_ + partialTick) * -75.0F));
        } else if (livingEntity != null && pose == Pose.SLEEPING) {
            Direction bedOrientation = livingEntity.getBedOrientation();
            poseStack.mulPose(Axis.YP.rotationDegrees(bedOrientation != null ? RenderUtils.getDirectionAngle(bedOrientation) : rotationYaw));
            poseStack.mulPose(Axis.ZP.rotationDegrees(this.getDeathMaxRotation(animatable)));
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (this.currentEntity.hasCustomName() || this.currentEntity instanceof Player) {
            String name = this.currentEntity.getName().getString();
            if (this.currentEntity instanceof Player player) {
                if (!player.isModelPartShown(PlayerModelPart.CAPE)) {
                    return;
                }
            } else {
                name = ChatFormatting.stripFormatting(name);
            }
            if (name != null && (name.equals("Dinnerbone") || name.equalsIgnoreCase("Grumm"))) {
                poseStack.translate(0.0F, this.currentEntity.getBbHeight() + 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
        }
    }

    protected float getDeathMaxRotation(T animatable) {
        return 90.0F;
    }

    public double getNameRenderCutoffDistance(E entity, T animatable) {
        return entity.isDiscrete() ? 32.0 : 64.0;
    }

    @Override
    public boolean shouldShowName(E entity) {
        if (!(entity instanceof LivingEntity)) {
            return super.shouldShowName(entity);
        } else {
            double nameRenderCutoff = this.getNameRenderCutoffDistance(entity, this.animatable);
            if (this.f_114476_.distanceToSqr(entity) >= nameRenderCutoff * nameRenderCutoff) {
                return false;
            } else if (!(entity instanceof Mob) || entity.shouldShowName() || entity.hasCustomName() && entity == this.f_114476_.crosshairPickEntity) {
                Minecraft minecraft = Minecraft.getInstance();
                boolean visibleToClient = !entity.isInvisibleTo(minecraft.player);
                Team entityTeam = entity.getTeam();
                if (entityTeam == null) {
                    return Minecraft.renderNames() && entity != minecraft.getCameraEntity() && visibleToClient && !entity.isVehicle();
                } else {
                    Team playerTeam = minecraft.player.m_5647_();
                    return switch(entityTeam.getNameTagVisibility()) {
                        case ALWAYS ->
                            visibleToClient;
                        case NEVER ->
                            false;
                        case HIDE_FOR_OTHER_TEAMS ->
                            playerTeam == null ? visibleToClient : entityTeam.isAlliedTo(playerTeam) && (entityTeam.canSeeFriendlyInvisibles() || visibleToClient);
                        case HIDE_FOR_OWN_TEAM ->
                            playerTeam == null ? visibleToClient : !entityTeam.isAlliedTo(playerTeam) && visibleToClient;
                    };
                }
            } else {
                return false;
            }
        }
    }

    @Override
    public int getPackedOverlay(T animatable, float u) {
        return !(this.currentEntity instanceof LivingEntity entity) ? OverlayTexture.NO_OVERLAY : OverlayTexture.pack(OverlayTexture.u(u), OverlayTexture.v(entity.hurtTime > 0 || entity.deathTime > 0));
    }

    @Override
    public int getPackedOverlay(T animatable, float u, float partialTick) {
        return this.getPackedOverlay(animatable, u);
    }

    public boolean isShaking(T animatable) {
        return this.currentEntity.isFullyFrozen();
    }

    public <H extends Entity, M extends Mob> void renderLeash(M mob, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, H leashHolder) {
        double lerpBodyAngle = (double) (Mth.lerp(partialTick, mob.f_20884_, mob.f_20883_) * (float) (Math.PI / 180.0) + (float) (Math.PI / 2));
        Vec3 leashOffset = mob.m_7939_();
        double xAngleOffset = Math.cos(lerpBodyAngle) * leashOffset.z + Math.sin(lerpBodyAngle) * leashOffset.x;
        double zAngleOffset = Math.sin(lerpBodyAngle) * leashOffset.z - Math.cos(lerpBodyAngle) * leashOffset.x;
        double lerpOriginX = Mth.lerp((double) partialTick, mob.f_19854_, mob.m_20185_()) + xAngleOffset;
        double lerpOriginY = Mth.lerp((double) partialTick, mob.f_19855_, mob.m_20186_()) + leashOffset.y;
        double lerpOriginZ = Mth.lerp((double) partialTick, mob.f_19856_, mob.m_20189_()) + zAngleOffset;
        Vec3 ropeGripPosition = leashHolder.getRopeHoldPosition(partialTick);
        float xDif = (float) (ropeGripPosition.x - lerpOriginX);
        float yDif = (float) (ropeGripPosition.y - lerpOriginY);
        float zDif = (float) (ropeGripPosition.z - lerpOriginZ);
        float offsetMod = Mth.invSqrt(xDif * xDif + zDif * zDif) * 0.025F / 2.0F;
        float xOffset = zDif * offsetMod;
        float zOffset = xDif * offsetMod;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.leash());
        BlockPos entityEyePos = BlockPos.containing(mob.m_20299_(partialTick));
        BlockPos holderEyePos = BlockPos.containing(leashHolder.getEyePosition(partialTick));
        int entityBlockLight = this.m_6086_(mob, entityEyePos);
        int holderBlockLight = leashHolder.isOnFire() ? 15 : leashHolder.level().m_45517_(LightLayer.BLOCK, holderEyePos);
        int entitySkyLight = mob.m_9236_().m_45517_(LightLayer.SKY, entityEyePos);
        int holderSkyLight = mob.m_9236_().m_45517_(LightLayer.SKY, holderEyePos);
        poseStack.pushPose();
        poseStack.translate(xAngleOffset, leashOffset.y, zAngleOffset);
        Matrix4f posMatrix = new Matrix4f(poseStack.last().pose());
        for (int segment = 0; segment <= 24; segment++) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight, entitySkyLight, holderSkyLight, 0.025F, 0.025F, xOffset, zOffset, segment, false);
        }
        for (int segment = 24; segment >= 0; segment--) {
            renderLeashPiece(vertexConsumer, posMatrix, xDif, yDif, zDif, entityBlockLight, holderBlockLight, entitySkyLight, holderSkyLight, 0.025F, 0.0F, xOffset, zOffset, segment, true);
        }
        poseStack.popPose();
    }

    private static void renderLeashPiece(VertexConsumer buffer, Matrix4f positionMatrix, float xDif, float yDif, float zDif, int entityBlockLight, int holderBlockLight, int entitySkyLight, int holderSkyLight, float width, float yOffset, float xOffset, float zOffset, int segment, boolean isLeashKnot) {
        float piecePosPercent = (float) segment / 24.0F;
        int lerpBlockLight = (int) Mth.lerp(piecePosPercent, (float) entityBlockLight, (float) holderBlockLight);
        int lerpSkyLight = (int) Mth.lerp(piecePosPercent, (float) entitySkyLight, (float) holderSkyLight);
        int packedLight = LightTexture.pack(lerpBlockLight, lerpSkyLight);
        float knotColourMod = segment % 2 == (isLeashKnot ? 1 : 0) ? 0.7F : 1.0F;
        float red = 0.5F * knotColourMod;
        float green = 0.4F * knotColourMod;
        float blue = 0.3F * knotColourMod;
        float x = xDif * piecePosPercent;
        float y = yDif > 0.0F ? yDif * piecePosPercent * piecePosPercent : yDif - yDif * (1.0F - piecePosPercent) * (1.0F - piecePosPercent);
        float z = zDif * piecePosPercent;
        buffer.vertex(positionMatrix, x - xOffset, y + yOffset, z + zOffset).color(red, green, blue, 1.0F).uv2(packedLight).endVertex();
        buffer.vertex(positionMatrix, x + xOffset, y + width - yOffset, z - zOffset).color(red, green, blue, 1.0F).uv2(packedLight).endVertex();
    }

    @Override
    public void updateAnimatedTextureFrame(T animatable) {
        AnimatableTexture.setAndUpdate(this.getTextureLocation(animatable));
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        MinecraftForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.CompileRenderLayers(this));
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !MinecraftForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.Pre(this, poseStack, model, bufferSource, partialTick, packedLight));
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        MinecraftForge.EVENT_BUS.post(new GeoRenderEvent.ReplacedEntity.Post(this, poseStack, model, bufferSource, partialTick, packedLight));
    }
}