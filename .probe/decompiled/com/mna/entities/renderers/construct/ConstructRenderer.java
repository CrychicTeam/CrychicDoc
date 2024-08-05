package com.mna.entities.renderers.construct;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.IConstructConstruction;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.tools.RLoc;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.models.constructs.ConstructModel;
import com.mna.entities.models.constructs.modular.ConstructMaterialModel;
import com.mna.entities.models.constructs.modular.ConstructModelRegistry;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mna.items.constructs.parts.torso.ConstructPartManaTorso;
import com.mna.items.constructs.parts.torso.ConstructPartTankTorso;
import com.mna.tools.math.MathUtils;
import com.mna.tools.render.ModelUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.util.RenderUtils;

public class ConstructRenderer extends MAGeckoRenderer<Construct> {

    private static final ResourceLocation FISHING_HOOK_TEX = new ResourceLocation("textures/entity/fishing_hook.png");

    private static final RenderType BOBBER_RENDER = RenderType.entityCutout(FISHING_HOOK_TEX);

    public static final ConstructModel constructModel = new ConstructModel();

    public static ConstructRenderer instance;

    public static final ResourceLocation model_brb_mining = RLoc.create("construct/common/brb_mining");

    public static final ResourceLocation model_brb_adventuring = RLoc.create("construct/common/brb_adventuring");

    public static final ResourceLocation model_brb_hunting = RLoc.create("construct/common/brb_hunting");

    private final ItemRenderer itemRenderer;

    private ConstructRenderer.RenderReferenceStack renderStack;

    private float fillPct = 0.0F;

    Matrix4f pos;

    Matrix3f normal;

    Minecraft mc;

    public ConstructRenderer(EntityRendererProvider.Context context) {
        super(context, constructModel);
        this.renderStack = null;
        this.mc = Minecraft.getInstance();
        this.itemRenderer = this.mc.getItemRenderer();
        instance = this;
    }

    public void defaultRender(PoseStack poseStack, Construct animatable, MultiBufferSource bufferSource, RenderType renderType, VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        Color renderColor = this.getRenderColor(animatable, partialTick, packedLight);
        float red = renderColor.getRedFloat();
        float green = renderColor.getGreenFloat();
        float blue = renderColor.getBlueFloat();
        float alpha = renderColor.getAlphaFloat();
        int packedOverlay = this.getPackedOverlay(animatable, 0.0F);
        BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable));
        ConstructRenderer.RenderReferenceStack latest = new ConstructRenderer.RenderReferenceStack(animatable.m_21205_(), animatable.m_21206_(), animatable.getConstructData().getHat(), animatable.getConstructData().getBanner());
        this.renderStack = latest;
        latest.model = (ConstructModel) this.getGeoModel();
        latest.data = animatable.getConstructData();
        latest.model.getBakedModel(latest.model.getModelResource(animatable));
        for (ConstructMaterial matl : latest.data.getComposition()) {
            latest.setVisibilityMatrix(animatable, matl);
            poseStack.pushPose();
            this.renderPass(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight, model, red, green, blue, alpha, packedOverlay);
            poseStack.popPose();
        }
        animatable.getConstructData().getPart(ConstructSlot.TORSO).ifPresent(p -> {
            if (p instanceof ConstructPartManaTorso) {
                this.fillPct = animatable.getManaPct();
                this.renderChargeBar(packedLight, ((float) animatable.f_19797_ + partialTick) / 5.0F, bufferSource);
            } else if (p instanceof ConstructPartTankTorso) {
                this.fillPct = (float) animatable.getStoredFluidAmount() / (float) animatable.getTankCapacity(1);
                this.renderFluidBar(bufferSource, packedLight, partialTick, animatable, poseStack);
            }
        });
        this.renderFishingLine(bufferSource, animatable, partialTick, poseStack);
        this.renderHeldItems(bufferSource);
    }

    private void renderPass(PoseStack poseStack, Construct animatable, MultiBufferSource bufferSource, RenderType renderType, VertexConsumer buffer, float yaw, float partialTick, int packedLight, BakedGeoModel model, float red, float green, float blue, float alpha, int packedOverlay) {
        this.emissivePass = false;
        if (renderType == null) {
            renderType = this.getRenderType(animatable, this.m_5478_(animatable), bufferSource, partialTick);
        }
        if (buffer == null) {
            buffer = bufferSource.getBuffer(renderType);
        }
        poseStack.pushPose();
        this.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (this.firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            this.preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, (float) packedLight, packedLight, packedOverlay);
            this.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            this.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            this.postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            this.firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }
        poseStack.popPose();
        this.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void render(Construct entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        VertexConsumer builder = bufferIn.getBuffer(RenderType.solid());
        int away_type = entityIn.getAwayType();
        if (away_type != 0) {
            matrixStackIn.pushPose();
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(entityIn.yaw_rnd));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(entityIn.pitch_rnd));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(entityIn.roll_rnd));
            switch(entityIn.getAwayType()) {
                case 1:
                    ModelUtils.renderEntityModel(builder, this.mc.level, model_brb_mining, matrixStackIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                    break;
                case 2:
                    ModelUtils.renderEntityModel(builder, this.mc.level, model_brb_adventuring, matrixStackIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                    break;
                case 3:
                    ModelUtils.renderEntityModel(builder, this.mc.level, model_brb_hunting, matrixStackIn, packedLightIn, OverlayTexture.NO_OVERLAY);
            }
            matrixStackIn.popPose();
        }
        if (!entityIn.isRenderDisabled()) {
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    private void renderChargeBar(int packedLight, float partialTicks, MultiBufferSource bufferSource) {
        if (bufferSource != null && this.pos != null && this.normal != null) {
            float alpha = this.fillPct > 0.2F ? 1.0F : Math.abs((float) Math.sin((double) partialTicks));
            float[] fullColor = new float[] { 0.0F, 1.0F, 0.0F, alpha };
            float[] emptyColor = new float[] { 1.0F, 0.0F, 0.0F, alpha };
            float width = 0.028F;
            float min = 0.65F;
            float height = 0.25F;
            VertexConsumer builder = bufferSource.getBuffer(RenderType.endGateway());
            float[] rgba = new float[] { MathUtils.lerpf(emptyColor[0], fullColor[0], this.fillPct), MathUtils.lerpf(emptyColor[1], fullColor[1], this.fillPct), MathUtils.lerpf(emptyColor[2], fullColor[2], this.fillPct), MathUtils.lerpf(emptyColor[3], fullColor[3], this.fillPct) };
            Vector3f nrm = new Vector3f(1.0F, 0.0F, 0.0F);
            nrm.mul(this.normal);
            builder.vertex(this.pos, -width, min, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(0.0F, 0.0F);
            builder.overlayCoords(0);
            builder.uv2(packedLight);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(this.pos, width, min, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(1.0F, 0.0F);
            builder.overlayCoords(0);
            builder.uv2(packedLight);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(this.pos, width, min + height * this.fillPct, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(1.0F, 1.0F);
            builder.overlayCoords(0);
            builder.uv2(packedLight);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
            builder.vertex(this.pos, -width, min + height * this.fillPct, 0.0F);
            builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
            builder.uv(0.0F, 1.0F);
            builder.overlayCoords(0);
            builder.uv2(packedLight);
            builder.normal(nrm.x(), nrm.y(), nrm.z());
            builder.endVertex();
        }
    }

    private void renderFluidBar(MultiBufferSource bufferIn, int packedLight, float partialTicks, Construct entity, PoseStack matrixStackIn) {
        if (this.pos != null && this.normal != null && entity != null && entity.getStoredFluid() != null) {
            float width = 0.14F;
            float min = 0.65F;
            float height = 0.325F;
            FluidType attrs = entity.getStoredFluid().getFluidType();
            IClientFluidTypeExtensions extension = IClientFluidTypeExtensions.of(attrs);
            ResourceLocation fluidTexBase = extension.getStillTexture();
            if (fluidTexBase != null) {
                TextureAtlasSprite sp = (TextureAtlasSprite) Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(fluidTexBase);
                RenderType liquid = RenderType.armorCutoutNoCull(TextureAtlas.LOCATION_BLOCKS);
                VertexConsumer builder = bufferIn.getBuffer(liquid);
                int color = extension.getTintColor();
                float r = (float) (color >> 16 & 0xFF) / 255.0F;
                float g = (float) (color >> 8 & 0xFF) / 255.0F;
                float b = (float) (color >> 0 & 0xFF) / 255.0F;
                float a = (float) (color >> 24 & 0xFF) / 255.0F;
                float[] rgba = new float[] { r, g, b, a };
                Vector3f nrm = new Vector3f(0.0F, 1.0F, 0.0F);
                nrm.mul(this.normal);
                float maxV = sp.getV0() + (sp.getV1() - sp.getV0()) * this.fillPct;
                int light = 15728880;
                builder.vertex(this.pos, -width, min, 0.0F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU0(), sp.getV0());
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(light);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                builder.vertex(this.pos, width, min, 0.0F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU1(), sp.getV0());
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(light);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                builder.vertex(this.pos, width, min + height * this.fillPct, 0.0F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU1(), maxV);
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(light);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                builder.vertex(this.pos, -width, min + height * this.fillPct, 0.0F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU0(), maxV);
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(light);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                float topY = min + height * this.fillPct;
                builder.vertex(this.pos, -width, topY, 0.0F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU0(), sp.getV0());
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(packedLight);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                builder.vertex(this.pos, width, topY, 0.0F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU1(), sp.getV0());
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(packedLight);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                builder.vertex(this.pos, width, topY, -0.2F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU1(), maxV);
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(packedLight);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
                builder.vertex(this.pos, -width, topY, -0.2F);
                builder.color(rgba[0], rgba[1], rgba[2], rgba[3]);
                builder.uv(sp.getU0(), maxV);
                builder.overlayCoords(OverlayTexture.NO_OVERLAY);
                builder.uv2(packedLight);
                builder.normal(nrm.x(), nrm.y(), nrm.z());
                builder.endVertex();
            }
        }
    }

    private void renderHeldItems(MultiBufferSource bufferIn) {
        Level level = this.mc.level;
        if (this.renderStack.offHandStack != null) {
            this.itemRenderer.renderStatic(this.renderStack.offHand, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, this.renderStack.packedLight, this.renderStack.packedOverlay, this.renderStack.offHandStack, bufferIn, level, 0);
        }
        if (this.renderStack.mainHandStack != null) {
            this.itemRenderer.renderStatic(this.renderStack.mainHand, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, this.renderStack.packedLight, this.renderStack.packedOverlay, this.renderStack.mainHandStack, bufferIn, level, 0);
        }
        if (this.renderStack.hatStack != null) {
            this.itemRenderer.renderStatic(this.renderStack.hat, ItemDisplayContext.FIXED, this.renderStack.packedLight, this.renderStack.packedOverlay, this.renderStack.hatStack, bufferIn, level, 0);
        }
        if (this.renderStack.bannerStack != null) {
            this.itemRenderer.renderStatic(this.renderStack.banner, ItemDisplayContext.FIXED, this.renderStack.packedLight, this.renderStack.packedOverlay, this.renderStack.bannerStack, bufferIn, level, 0);
        }
        this.renderStack.mainHandStack = null;
        this.renderStack.offHandStack = null;
    }

    private void renderFishingLine(MultiBufferSource bufferSource, Construct construct, float partialTicks, PoseStack poseStack) {
        if (construct.isFishing()) {
            if (poseStack != null && construct.getFishingTarget() != null && construct.m_20183_().m_123314_(construct.getFishingTarget(), 8.0)) {
                VertexConsumer lineStrips = bufferSource.getBuffer(RenderType.lineStrip());
                float bob = (float) (Math.sin((double) (((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks) / 20.0F)) / 12.0);
                poseStack.pushPose();
                Vec3 forward = construct.m_20156_().normalize();
                Vec3 side = forward.cross(new Vec3(0.0, 1.0, 0.0)).scale(construct.f_20912_ == InteractionHand.OFF_HAND ? 0.175 : -0.275);
                Vec3 sourceOffset = new Vec3(0.0, 1.35, 0.0).add(forward.scale(0.94F)).add(side);
                poseStack.translate(sourceOffset.x, sourceOffset.y, sourceOffset.z);
                Vec3 source = construct.m_20182_().add(sourceOffset);
                Vec3 target = Vec3.upFromBottomCenterOf(construct.getFishingTarget(), 1.0).add(0.0, (double) bob, 0.0);
                Vec3 delta = target.subtract(source);
                PoseStack.Pose lastPose = poseStack.last();
                for (int k = 0; k <= 16; k++) {
                    stringVertex((float) delta.x, (float) delta.y, (float) delta.z, lineStrips, lastPose, fraction(k, 16), fraction(k + 1, 16));
                }
                poseStack.popPose();
                poseStack.pushPose();
                poseStack.translate(sourceOffset.x, sourceOffset.y, sourceOffset.z);
                poseStack.translate(delta.x, delta.y, delta.z);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                poseStack.mulPose(this.f_114476_.cameraOrientation());
                poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                PoseStack.Pose posestack$pose = poseStack.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();
                VertexConsumer vertexconsumer = bufferSource.getBuffer(BOBBER_RENDER);
                vertex(vertexconsumer, matrix4f, matrix3f, this.renderStack.packedLight, 0.0F, 0, 0, 1);
                vertex(vertexconsumer, matrix4f, matrix3f, this.renderStack.packedLight, 1.0F, 0, 1, 1);
                vertex(vertexconsumer, matrix4f, matrix3f, this.renderStack.packedLight, 1.0F, 1, 1, 0);
                vertex(vertexconsumer, matrix4f, matrix3f, this.renderStack.packedLight, 0.0F, 1, 0, 0);
                poseStack.popPose();
            }
        }
    }

    private static void vertex(VertexConsumer bufferBuilder, Matrix4f pose, Matrix3f normal, int packedLight, float x, int int0, int int1, int int2) {
        bufferBuilder.vertex(pose, x - 0.5F, (float) int0 - 0.5F, 0.0F).color(255, 255, 255, 255).uv((float) int1, (float) int2).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normal, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static float fraction(int numerator, int denominator) {
        return (float) numerator / (float) denominator;
    }

    private static void stringVertex(float deltaX, float deltaY, float deltaZ, VertexConsumer vertexBuffer, PoseStack.Pose pose, float pctFrom, float pctTo) {
        float startX = deltaX * pctFrom;
        float startY = deltaY * (pctFrom * pctFrom + pctFrom) * 0.5F + 0.25F;
        float startZ = deltaZ * pctFrom;
        Vec3 normal = new Vec3((double) (deltaX * pctTo - startX), (double) (deltaY * (pctTo * pctTo + pctTo) * 0.5F + 0.25F - startY), (double) (deltaZ * pctTo - startZ)).normalize();
        vertexBuffer.vertex(pose.pose(), startX, startY, startZ).color(187, 191, 191, 255).normal(pose.normal(), (float) normal.x, (float) normal.y, (float) normal.z).endVertex();
    }

    public RenderType getRenderType(Construct animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.solid();
    }

    public void renderRecursively(PoseStack stack, Construct animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Optional<ItemConstructPart> TORSO = animatable.getConstructData().getPart(ConstructSlot.TORSO);
        if (bone.getName().equals("RIGHT_GRAB_POINT") && !this.renderStack.offHand.isEmpty()) {
            Optional<ItemConstructPart> rightArm = animatable.getConstructData().getPart(ConstructSlot.RIGHT_ARM);
            boolean rightHandWickerwood = rightArm.isPresent() && ((ItemConstructPart) rightArm.get()).getMaterial().equals(ConstructMaterial.WICKERWOOD);
            stack.pushPose();
            if (rightHandWickerwood) {
                stack.translate(0.38, 0.2, -0.05);
            } else {
                stack.translate(0.53, 0.23, -0.05);
            }
            stack.scale(0.7F, 0.7F, 0.7F);
            this.renderStack.offHandStack = new PoseStack();
            this.renderStack.offHandStack.poseStack.add(stack.last());
            this.renderStack.packedLight = packedLight;
            this.renderStack.packedOverlay = packedOverlay;
            stack.popPose();
        } else if (bone.getName().equals("LEFT_GRAB_POINT") && !this.renderStack.mainHand.isEmpty()) {
            Optional<ItemConstructPart> leftArm = animatable.getConstructData().getPart(ConstructSlot.LEFT_ARM);
            boolean leftHandWickerwood = leftArm.isPresent() && ((ItemConstructPart) leftArm.get()).getMaterial().equals(ConstructMaterial.WICKERWOOD);
            stack.pushPose();
            if (leftHandWickerwood) {
                stack.translate(-0.38, 0.2, -0.05);
            } else {
                stack.translate(-0.53, 0.23, -0.05);
            }
            stack.scale(0.7F, 0.7F, 0.7F);
            this.renderStack.mainHandStack = new PoseStack();
            this.renderStack.mainHandStack.poseStack.add(stack.last());
            this.renderStack.packedLight = packedLight;
            this.renderStack.packedOverlay = packedOverlay;
            stack.popPose();
        } else if (bone.getName().equals("MANA_TORSO_GAUGE_POS") && TORSO.isPresent() && TORSO.get() instanceof ConstructPartManaTorso) {
            stack.pushPose();
            stack.translate(0.0F, 0.35F, 0.51F);
            this.normal = stack.last().normal();
            this.pos = stack.last().pose();
            stack.popPose();
        } else if (bone.getName().equals("TANK_TORSO_FLUID_POS") && TORSO.isPresent() && TORSO.get() instanceof ConstructPartTankTorso) {
            stack.pushPose();
            stack.translate(0.0F, 0.22F, 0.48F);
            this.normal = stack.last().normal();
            this.pos = stack.last().pose();
            stack.popPose();
        }
        if ((bone.getName().equals("RIGHT_GRAB_POINT") || bone.getName().equals("LEFT_GRAB_POINT")) && animatable.isFishing()) {
            if (animatable.handHasCapability(InteractionHand.MAIN_HAND, ConstructCapability.FISH)) {
                stack.pushPose();
                this.renderStack.mainHandStack = new PoseStack();
                this.renderStack.mainHandStack.poseStack.add(stack.last());
                this.renderStack.packedLight = packedLight;
                this.renderStack.packedOverlay = packedOverlay;
                stack.popPose();
            }
            if (animatable.handHasCapability(InteractionHand.OFF_HAND, ConstructCapability.FISH)) {
                stack.pushPose();
                this.renderStack.mainHandStack = new PoseStack();
                this.renderStack.mainHandStack.poseStack.add(stack.last());
                this.renderStack.packedLight = packedLight;
                this.renderStack.packedOverlay = packedOverlay;
                stack.popPose();
            }
        }
        ItemStack hatStack = animatable.getConstructData().getHat();
        if (bone.getName().equals("HAT") && !hatStack.isEmpty()) {
            stack.pushPose();
            stack.translate(0.0F, 1.775F, 0.0F);
            if (hatStack.getItem() instanceof BlockItem) {
                stack.translate(0.0, -0.05, 0.0);
                stack.scale(0.5F, 0.5F, 0.5F);
            } else if (hatStack.getItem() instanceof ItemConstructPart) {
                stack.translate(0.0, 0.0, 0.125);
                stack.scale(0.5F, 0.5F, 0.5F);
            } else {
                stack.translate(0.0, -0.125, 0.0);
                stack.mulPose(Axis.XP.rotationDegrees(90.0F));
                stack.scale(0.5F, 0.5F, 0.5F);
            }
            this.renderStack.hatStack = new PoseStack();
            this.renderStack.hatStack.poseStack.add(stack.last());
            this.renderStack.packedLight = packedLight;
            this.renderStack.packedOverlay = packedOverlay;
            stack.popPose();
        }
        if (bone.getName().equals("BANNER") && !animatable.getConstructData().getBanner().isEmpty()) {
            stack.pushPose();
            stack.translate(0.0F, 1.35F, 0.25F);
            stack.mulPose(Axis.YP.rotationDegrees(180.0F));
            stack.scale(1.5F, 1.5F, 1.5F);
            this.renderStack.bannerStack = new PoseStack();
            this.renderStack.bannerStack.poseStack.add(stack.last());
            this.renderStack.packedLight = packedLight;
            this.renderStack.packedOverlay = packedOverlay;
            stack.popPose();
        }
        ConstructMaterialModel cmm = ConstructModelRegistry.getMaterialModelFor(this.renderStack.matl);
        List<ResourceLocation> bonemodels = cmm.getForBone(bone.getName(), this.renderStack.model, animatable);
        if (!bone.isHidden()) {
            stack.pushPose();
            RenderUtils.translateMatrixToBone(stack, bone);
            RenderUtils.translateToPivotPoint(stack, bone);
            RenderUtils.rotateMatrixAroundBone(stack, bone);
            RenderUtils.scaleMatrixForBone(stack, bone);
            RenderUtils.translateAwayFromPivotPoint(stack, bone);
            bonemodels.forEach(model -> {
                stack.pushPose();
                ModelUtils.renderEntityModel(buffer, this.mc.level, model, stack, packedLight, packedOverlay);
                stack.popPose();
            });
            for (GeoBone childBone : bone.getChildBones()) {
                this.renderRecursively(stack, animatable, childBone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            }
            stack.popPose();
        }
    }

    public boolean shouldShowName(Construct entity) {
        return entity.isAway() ? false : super.m_6512_(entity);
    }

    protected void renderNameTag(Construct construct, Component name, PoseStack matrixStack, MultiBufferSource buffer, int p_225629_5_) {
        super.m_7649_(construct, name, matrixStack, buffer, p_225629_5_);
    }

    private class RenderReferenceStack {

        public ItemStack mainHand = ItemStack.EMPTY;

        public ItemStack offHand = ItemStack.EMPTY;

        public ItemStack hat = ItemStack.EMPTY;

        public ItemStack banner = ItemStack.EMPTY;

        public ConstructModel model;

        public IConstructConstruction data;

        ConstructMaterial matl;

        private PoseStack mainHandStack = null;

        private PoseStack offHandStack = null;

        private PoseStack hatStack = null;

        private PoseStack bannerStack = null;

        private int packedLight;

        private int packedOverlay;

        public RenderReferenceStack(ItemStack mainHand, ItemStack offHand, ItemStack hat, ItemStack banner) {
            this.mainHand = mainHand;
            this.offHand = offHand;
            this.hat = hat;
            this.banner = banner;
        }

        public void setVisibilityMatrix(Construct entity, ConstructMaterial matl) {
            this.matl = matl;
            if (this.model != null && this.data != null && matl != null && entity != null) {
                this.model.resetMutexVisibility();
                this.model.setActiveMaterial(matl);
                this.model.setOwner(entity.getOwner() != null ? entity.getOwner().m_20148_() : null);
                for (ItemConstructPart part : this.data.getPartsForMaterial(matl)) {
                    this.model.setMutexVisibility(part.getSlot(), part.getModelTypeMutex());
                }
            }
        }
    }
}