package software.bernie.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.cache.object.GeoQuad;
import software.bernie.geckolib.cache.object.GeoVertex;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.util.RenderUtils;

public abstract class DynamicGeoEntityRenderer<T extends Entity & GeoAnimatable> extends GeoEntityRenderer<T> {

    protected static Map<ResourceLocation, IntIntPair> TEXTURE_DIMENSIONS_CACHE = new Object2ObjectOpenHashMap();

    protected ResourceLocation textureOverride = null;

    public DynamicGeoEntityRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
    }

    @Nullable
    protected ResourceLocation getTextureOverrideForBone(GeoBone bone, T animatable, float partialTick) {
        return null;
    }

    @Nullable
    protected RenderType getRenderTypeOverrideForBone(GeoBone bone, T animatable, ResourceLocation texturePath, MultiBufferSource bufferSource, float partialTick) {
        return null;
    }

    protected boolean boneRenderOverride(PoseStack poseStack, GeoBone bone, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        return false;
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
            localMatrix.translate(new Vector3f(this.m_7860_(this.animatable, 1.0F).toVector3f()));
            bone.setLocalSpaceMatrix(localMatrix);
            Matrix4f worldState = new Matrix4f(localMatrix);
            worldState.translate(new Vector3f(this.animatable.position().toVector3f()));
            bone.setWorldSpaceMatrix(worldState);
        }
        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);
        this.textureOverride = this.getTextureOverrideForBone(bone, this.animatable, partialTick);
        ResourceLocation texture = this.textureOverride == null ? this.m_5478_(this.animatable) : this.textureOverride;
        RenderType renderTypeOverride = this.getRenderTypeOverrideForBone(bone, this.animatable, texture, bufferSource, partialTick);
        if (texture != null && renderTypeOverride == null) {
            renderTypeOverride = this.getRenderType(this.animatable, texture, bufferSource, partialTick);
        }
        if (renderTypeOverride != null) {
            buffer = bufferSource.getBuffer(renderTypeOverride);
        }
        if (!this.boneRenderOverride(poseStack, bone, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha)) {
            super.renderCubesOfBone(poseStack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }
        if (renderTypeOverride != null) {
            buffer = bufferSource.getBuffer(this.getRenderType(this.animatable, this.m_5478_(this.animatable), bufferSource, partialTick));
        }
        if (!isReRender) {
            this.applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }
        super.renderChildBones(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        poseStack.popPose();
    }

    public void postRender(PoseStack poseStack, T animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.textureOverride = null;
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void createVerticesOfQuad(GeoQuad quad, Matrix4f poseState, Vector3f normal, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.textureOverride == null) {
            super.createVerticesOfQuad(quad, poseState, normal, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            IntIntPair boneTextureSize = this.computeTextureSize(this.textureOverride);
            IntIntPair entityTextureSize = this.computeTextureSize(this.m_5478_(this.animatable));
            if (boneTextureSize != null && entityTextureSize != null) {
                for (GeoVertex vertex : quad.vertices()) {
                    Vector4f vector4f = poseState.transform(new Vector4f(vertex.position().x(), vertex.position().y(), vertex.position().z(), 1.0F));
                    float texU = vertex.texU() * (float) entityTextureSize.firstInt() / (float) boneTextureSize.firstInt();
                    float texV = vertex.texV() * (float) entityTextureSize.secondInt() / (float) boneTextureSize.secondInt();
                    buffer.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, texU, texV, packedOverlay, packedLight, normal.x(), normal.y(), normal.z());
                }
            } else {
                super.createVerticesOfQuad(quad, poseState, normal, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            }
        }
    }

    protected IntIntPair computeTextureSize(ResourceLocation texture) {
        return (IntIntPair) TEXTURE_DIMENSIONS_CACHE.computeIfAbsent(texture, RenderUtils::getTextureDimensions);
    }
}