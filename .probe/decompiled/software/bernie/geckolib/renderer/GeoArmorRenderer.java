package software.bernie.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.texture.AnimatableTexture;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayersContainer;
import software.bernie.geckolib.util.RenderUtils;

public class GeoArmorRenderer<T extends Item & GeoItem> extends HumanoidModel implements GeoRenderer<T> {

    protected final GeoRenderLayersContainer<T> renderLayers = new GeoRenderLayersContainer<>(this);

    protected final GeoModel<T> model;

    protected T animatable;

    protected HumanoidModel<?> baseModel;

    protected float scaleWidth = 1.0F;

    protected float scaleHeight = 1.0F;

    protected Matrix4f entityRenderTranslations = new Matrix4f();

    protected Matrix4f modelRenderTranslations = new Matrix4f();

    protected BakedGeoModel lastModel = null;

    protected GeoBone head = null;

    protected GeoBone body = null;

    protected GeoBone rightArm = null;

    protected GeoBone leftArm = null;

    protected GeoBone rightLeg = null;

    protected GeoBone leftLeg = null;

    protected GeoBone rightBoot = null;

    protected GeoBone leftBoot = null;

    protected Entity currentEntity = null;

    protected ItemStack currentStack = null;

    protected EquipmentSlot currentSlot = null;

    public GeoArmorRenderer(GeoModel<T> model) {
        super(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        this.model = model;
        this.f_102610_ = false;
    }

    @Override
    public GeoModel<T> getGeoModel() {
        return this.model;
    }

    public T getAnimatable() {
        return this.animatable;
    }

    public Entity getCurrentEntity() {
        return this.currentEntity;
    }

    public ItemStack getCurrentStack() {
        return this.currentStack;
    }

    public EquipmentSlot getCurrentSlot() {
        return this.currentSlot;
    }

    public long getInstanceId(T animatable) {
        return GeoItem.getId(this.currentStack) + (long) this.currentEntity.getId();
    }

    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.armorCutoutNoCull(texture);
    }

    @Override
    public List<GeoRenderLayer<T>> getRenderLayers() {
        return this.renderLayers.getRenderLayers();
    }

    public GeoArmorRenderer<T> addRenderLayer(GeoRenderLayer<T> renderLayer) {
        this.renderLayers.addLayer(renderLayer);
        return this;
    }

    public GeoArmorRenderer<T> withScale(float scale) {
        return this.withScale(scale, scale);
    }

    public GeoArmorRenderer<T> withScale(float scaleWidth, float scaleHeight) {
        this.scaleWidth = scaleWidth;
        this.scaleHeight = scaleHeight;
        return this;
    }

    @javax.annotation.Nullable
    public GeoBone getHeadBone() {
        return (GeoBone) this.model.getBone("armorHead").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getBodyBone() {
        return (GeoBone) this.model.getBone("armorBody").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getRightArmBone() {
        return (GeoBone) this.model.getBone("armorRightArm").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getLeftArmBone() {
        return (GeoBone) this.model.getBone("armorLeftArm").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getRightLegBone() {
        return (GeoBone) this.model.getBone("armorRightLeg").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getLeftLegBone() {
        return (GeoBone) this.model.getBone("armorLeftLeg").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getRightBootBone() {
        return (GeoBone) this.model.getBone("armorRightBoot").orElse(null);
    }

    @javax.annotation.Nullable
    public GeoBone getLeftBootBone() {
        return (GeoBone) this.model.getBone("armorLeftBoot").orElse(null);
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, @javax.annotation.Nullable MultiBufferSource bufferSource, @javax.annotation.Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());
        this.applyBaseModel(this.baseModel);
        this.grabRelevantBones(this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(this.animatable)));
        this.applyBaseTransformations(this.baseModel);
        this.scaleModelForBaby(poseStack, animatable, partialTick, isReRender);
        this.scaleModelForRender(this.scaleWidth, this.scaleHeight, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        if (!(this.currentEntity instanceof GeoAnimatable)) {
            this.applyBoneVisibilityBySlot(this.currentSlot);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource bufferSource = mc.levelRenderer.renderBuffers.bufferSource();
        if (mc.levelRenderer.shouldShowEntityOutlines() && mc.shouldEntityAppearGlowing(this.currentEntity)) {
            bufferSource = mc.levelRenderer.renderBuffers.outlineBufferSource();
        }
        float partialTick = mc.getFrameTime();
        RenderType renderType = this.getRenderType(this.animatable, this.getTextureLocation(this.animatable), bufferSource, partialTick);
        buffer = ItemRenderer.getArmorFoilBuffer(bufferSource, renderType, false, this.currentStack.hasFoil());
        this.defaultRender(poseStack, this.animatable, bufferSource, null, buffer, 0.0F, partialTick, packedLight);
    }

    public void actuallyRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.5F, 0.0F);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        if (!isReRender) {
            AnimationState<T> animationState = new AnimationState<>(animatable, 0.0F, 0.0F, partialTick, false);
            long instanceId = this.getInstanceId(animatable);
            animationState.setData(DataTickets.TICK, animatable.getTick(this.currentEntity));
            animationState.setData(DataTickets.ITEMSTACK, this.currentStack);
            animationState.setData(DataTickets.ENTITY, this.currentEntity);
            animationState.setData(DataTickets.EQUIPMENT_SLOT, this.currentSlot);
            this.model.addAdditionalStateData(animatable, instanceId, animationState::setData);
            this.model.handleAnimations(animatable, instanceId, animationState);
        }
        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());
        GeoRenderer.super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    public void renderRecursively(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations));
        }
        GeoRenderer.super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    protected void grabRelevantBones(BakedGeoModel bakedModel) {
        if (this.lastModel != bakedModel) {
            this.lastModel = bakedModel;
            this.head = this.getHeadBone();
            this.body = this.getBodyBone();
            this.rightArm = this.getRightArmBone();
            this.leftArm = this.getLeftArmBone();
            this.rightLeg = this.getRightLegBone();
            this.leftLeg = this.getLeftLegBone();
            this.rightBoot = this.getRightBootBone();
            this.leftBoot = this.getLeftBootBone();
        }
    }

    public void prepForRender(@javax.annotation.Nullable Entity entity, ItemStack stack, @javax.annotation.Nullable EquipmentSlot slot, @javax.annotation.Nullable HumanoidModel<?> baseModel) {
        if (entity != null && slot != null && baseModel != null) {
            this.baseModel = baseModel;
            this.currentEntity = entity;
            this.currentStack = stack;
            this.animatable = (T) stack.getItem();
            this.currentSlot = slot;
        }
    }

    protected void applyBaseModel(HumanoidModel<?> baseModel) {
        this.f_102610_ = baseModel.f_102610_;
        this.f_102817_ = baseModel.crouching;
        this.f_102609_ = baseModel.f_102609_;
        this.f_102816_ = baseModel.rightArmPose;
        this.f_102815_ = baseModel.leftArmPose;
    }

    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        this.setAllVisible(false);
        switch(currentSlot) {
            case HEAD:
                this.setBoneVisible(this.head, true);
                break;
            case CHEST:
                this.setBoneVisible(this.body, true);
                this.setBoneVisible(this.rightArm, true);
                this.setBoneVisible(this.leftArm, true);
                break;
            case LEGS:
                this.setBoneVisible(this.rightLeg, true);
                this.setBoneVisible(this.leftLeg, true);
                break;
            case FEET:
                this.setBoneVisible(this.rightBoot, true);
                this.setBoneVisible(this.leftBoot, true);
        }
    }

    public void applyBoneVisibilityByPart(EquipmentSlot currentSlot, ModelPart currentPart, HumanoidModel<?> model) {
        this.setAllVisible(false);
        currentPart.visible = true;
        GeoBone bone = null;
        if (currentPart == model.hat || currentPart == model.head) {
            bone = this.head;
        } else if (currentPart == model.body) {
            bone = this.body;
        } else if (currentPart == model.leftArm) {
            bone = this.leftArm;
        } else if (currentPart == model.rightArm) {
            bone = this.rightArm;
        } else if (currentPart == model.leftLeg) {
            bone = currentSlot == EquipmentSlot.FEET ? this.leftBoot : this.leftLeg;
        } else if (currentPart == model.rightLeg) {
            bone = currentSlot == EquipmentSlot.FEET ? this.rightBoot : this.rightLeg;
        }
        if (bone != null) {
            bone.setHidden(false);
        }
    }

    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        if (this.head != null) {
            ModelPart headPart = baseModel.head;
            RenderUtils.matchModelPartRot(headPart, this.head);
            this.head.updatePosition(headPart.x, -headPart.y, headPart.z);
        }
        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;
            RenderUtils.matchModelPartRot(bodyPart, this.body);
            this.body.updatePosition(bodyPart.x, -bodyPart.y, bodyPart.z);
        }
        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;
            RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
            this.rightArm.updatePosition(rightArmPart.x + 5.0F, 2.0F - rightArmPart.y, rightArmPart.z);
        }
        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;
            RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
            this.leftArm.updatePosition(leftArmPart.x - 5.0F, 2.0F - leftArmPart.y, leftArmPart.z);
        }
        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;
            RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
            this.rightLeg.updatePosition(rightLegPart.x + 2.0F, 12.0F - rightLegPart.y, rightLegPart.z);
            if (this.rightBoot != null) {
                RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
                this.rightBoot.updatePosition(rightLegPart.x + 2.0F, 12.0F - rightLegPart.y, rightLegPart.z);
            }
        }
        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;
            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
            this.leftLeg.updatePosition(leftLegPart.x - 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            if (this.leftBoot != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
                this.leftBoot.updatePosition(leftLegPart.x - 2.0F, 12.0F - leftLegPart.y, leftLegPart.z);
            }
        }
    }

    @Override
    public void setAllVisible(boolean pVisible) {
        super.setAllVisible(pVisible);
        this.setBoneVisible(this.head, pVisible);
        this.setBoneVisible(this.body, pVisible);
        this.setBoneVisible(this.rightArm, pVisible);
        this.setBoneVisible(this.leftArm, pVisible);
        this.setBoneVisible(this.rightLeg, pVisible);
        this.setBoneVisible(this.leftLeg, pVisible);
        this.setBoneVisible(this.rightBoot, pVisible);
        this.setBoneVisible(this.leftBoot, pVisible);
    }

    public void scaleModelForBaby(PoseStack poseStack, T animatable, float partialTick, boolean isReRender) {
        if (this.f_102610_ && !isReRender) {
            if (this.currentSlot == EquipmentSlot.HEAD) {
                if (this.baseModel.f_102007_) {
                    float headScale = 1.5F / this.baseModel.f_102010_;
                    poseStack.scale(headScale, headScale, headScale);
                }
                poseStack.translate(0.0F, this.baseModel.f_170338_ / 16.0F, this.baseModel.f_170339_ / 16.0F);
            } else {
                float bodyScale = 1.0F / this.baseModel.f_102011_;
                poseStack.scale(bodyScale, bodyScale, bodyScale);
                poseStack.translate(0.0F, this.baseModel.f_102012_ / 16.0F, 0.0F);
            }
        }
    }

    protected void setBoneVisible(@javax.annotation.Nullable GeoBone bone, boolean visible) {
        if (bone != null) {
            bone.setHidden(!visible);
        }
    }

    public void updateAnimatedTextureFrame(T animatable) {
        if (this.currentEntity != null) {
            AnimatableTexture.setAndUpdate(this.getTextureLocation(animatable));
        }
    }

    @Override
    public void fireCompileRenderLayersEvent() {
        MinecraftForge.EVENT_BUS.post(new GeoRenderEvent.Armor.CompileRenderLayers(this));
    }

    @Override
    public boolean firePreRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        return !MinecraftForge.EVENT_BUS.post(new GeoRenderEvent.Armor.Pre(this, poseStack, model, bufferSource, partialTick, packedLight));
    }

    @Override
    public void firePostRenderEvent(PoseStack poseStack, BakedGeoModel model, MultiBufferSource bufferSource, float partialTick, int packedLight) {
        MinecraftForge.EVENT_BUS.post(new GeoRenderEvent.Armor.Post(this, poseStack, model, bufferSource, partialTick, packedLight));
    }
}