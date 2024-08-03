package yesman.epicfight.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelData;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.world.level.block.entity.FractureBlockEntity;

@OnlyIn(Dist.CLIENT)
public class FractureBlockRenderer implements BlockEntityRenderer<FractureBlockEntity> {

    private final BlockRenderDispatcher blockRenderDispatcher;

    public FractureBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    public boolean shouldRender(FractureBlockEntity fractureBlockEntity0, Vec3 vec1) {
        return Vec3.atCenterOf(fractureBlockEntity0.m_58899_()).closerThan(vec1, (double) this.m_142163_());
    }

    public void render(FractureBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource multiBufferSource, int lightColor, int overlayColor) {
        float turnBackTime = 5.0F;
        float lerpAmount = Mth.clamp(partialTicks * (1.0F / turnBackTime) + (turnBackTime - (float) (blockEntity.getMaxLifeTime() - blockEntity.getLifeTime())) * (1.0F / turnBackTime), 0.0F, 1.0F);
        Vector3f translate = (float) blockEntity.getMaxLifeTime() > (float) blockEntity.getLifeTime() + turnBackTime ? blockEntity.getTranslate() : MathUtils.lerpMojangVector(blockEntity.getTranslate(), new Vector3f(), lerpAmount);
        Quaternionf rotate = (float) blockEntity.getMaxLifeTime() > (float) blockEntity.getLifeTime() + turnBackTime ? blockEntity.getRotation() : MathUtils.lerpQuaternion(blockEntity.getRotation(), new Quaternionf(), lerpAmount);
        double BOUNCE_MAX_HEIGHT = blockEntity.getBouncing();
        double TIME = Math.max(BOUNCE_MAX_HEIGHT * 8.0, 8.0);
        double EXTENDER = 1.0 / Math.pow(TIME * 0.5, 2.0);
        double MOVE_GRAPH = Math.sqrt(BOUNCE_MAX_HEIGHT / EXTENDER);
        double bouncingAnimation = Math.max(-EXTENDER * Math.pow((double) ((float) blockEntity.getLifeTime() + partialTicks) - MOVE_GRAPH, 2.0) + BOUNCE_MAX_HEIGHT, 0.0);
        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(rotate);
        poseStack.translate((double) translate.x(), (double) translate.y() + bouncingAnimation, (double) translate.z());
        poseStack.translate(-0.5, -0.5, -0.5);
        this.blockRenderDispatcher.renderBreakingTexture(blockEntity.getOriginalBlockState(), blockEntity.m_58899_().above(), blockEntity.m_58904_(), poseStack, multiBufferSource.getBuffer(RenderType.cutout()), ModelData.EMPTY);
        poseStack.popPose();
    }
}