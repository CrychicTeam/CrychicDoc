package software.bernie.geckolib.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class BlockAndItemGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

    protected final BiFunction<GeoBone, T, ItemStack> stackForBone;

    protected final BiFunction<GeoBone, T, BlockState> blockForBone;

    public BlockAndItemGeoLayer(GeoRenderer<T> renderer) {
        this(renderer, (bone, animatable) -> null, (bone, animatable) -> null);
    }

    public BlockAndItemGeoLayer(GeoRenderer<T> renderer, BiFunction<GeoBone, T, ItemStack> stackForBone, BiFunction<GeoBone, T, BlockState> blockForBone) {
        super(renderer);
        this.stackForBone = stackForBone;
        this.blockForBone = blockForBone;
    }

    @Nullable
    protected ItemStack getStackForBone(GeoBone bone, T animatable) {
        return (ItemStack) this.stackForBone.apply(bone, animatable);
    }

    @Nullable
    protected BlockState getBlockForBone(GeoBone bone, T animatable) {
        return (BlockState) this.blockForBone.apply(bone, animatable);
    }

    protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
        return ItemDisplayContext.NONE;
    }

    @Override
    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        ItemStack stack = this.getStackForBone(bone, animatable);
        BlockState blockState = this.getBlockForBone(bone, animatable);
        if (stack != null || blockState != null) {
            poseStack.pushPose();
            RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);
            if (stack != null) {
                this.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
            if (blockState != null) {
                this.renderBlockForBone(poseStack, bone, blockState, animatable, bufferSource, partialTick, packedLight, packedOverlay);
            }
            buffer = bufferSource.getBuffer(renderType);
            poseStack.popPose();
        }
    }

    protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        if (animatable instanceof LivingEntity livingEntity) {
            Minecraft.getInstance().getItemRenderer().renderStatic(livingEntity, stack, this.getTransformTypeForStack(bone, stack, animatable), false, poseStack, bufferSource, livingEntity.m_9236_(), packedLight, packedOverlay, livingEntity.m_19879_());
        } else {
            Minecraft.getInstance().getItemRenderer().renderStatic(stack, this.getTransformTypeForStack(bone, stack, animatable), packedLight, packedOverlay, poseStack, bufferSource, Minecraft.getInstance().level, (int) this.renderer.getInstanceId(animatable));
        }
    }

    protected void renderBlockForBone(PoseStack poseStack, GeoBone bone, BlockState state, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(-0.25F, -0.25F, -0.25F);
        poseStack.scale(0.5F, 0.5F, 0.5F);
        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, bufferSource, packedLight, packedOverlay, ModelData.EMPTY, null);
        poseStack.popPose();
    }
}