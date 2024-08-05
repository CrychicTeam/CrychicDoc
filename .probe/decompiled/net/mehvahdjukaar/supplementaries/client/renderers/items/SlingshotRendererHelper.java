package net.mehvahdjukaar.supplementaries.client.renderers.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SlingshotRendererHelper {

    private static ItemStack clientCurrentAmmo = ItemStack.EMPTY;

    private static BlockPos lookPos = null;

    public static ItemStack getAmmoForPreview(ItemStack cannon, @Nullable Level world, Player player) {
        if (world != null && world.getGameTime() % 10L == 0L) {
            clientCurrentAmmo = ItemStack.EMPTY;
            ItemStack findAmmo = player.getProjectile(cannon);
            if (findAmmo.getItem() != Items.ARROW) {
                clientCurrentAmmo = findAmmo;
            }
        }
        return clientCurrentAmmo;
    }

    public static void grabNewLookPos(Player player) {
        float blockRange = 40.0F;
        Level level = player.m_9236_();
        Vec3 start = player.m_20182_().add(0.0, (double) player.m_20192_(), 0.0);
        Vec3 range = player.m_20154_().scale((double) blockRange);
        BlockHitResult raytrace = level.m_45547_(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        if (raytrace.getType() == HitResult.Type.BLOCK && start.distanceToSqr(raytrace.m_82450_()) > (double) Mth.square(Minecraft.getInstance().gameMode.getPickRange())) {
            lookPos = raytrace.getBlockPos().relative(raytrace.getDirection(), 0);
        }
    }

    public static void renderBlockOutline(PoseStack matrixStack, Camera camera, Minecraft mc) {
        if (lookPos != null) {
            Player player = mc.player;
            Level level = player.m_9236_();
            level.getProfiler().popPush("outline");
            BlockPos pos = lookPos;
            BlockState blockstate = level.getBlockState(pos);
            if (!blockstate.m_60795_() && level.getWorldBorder().isWithinBounds(pos)) {
                VertexConsumer builder = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
                Vec3 vector3d = camera.getPosition();
                double pX = vector3d.x();
                double pY = vector3d.y();
                double pZ = vector3d.z();
                int color = (Integer) ClientConfigs.Items.SLINGSHOT_OUTLINE_COLOR.get();
                float r = (float) FastColor.ARGB32.red(color) / 255.0F;
                float g = (float) FastColor.ARGB32.green(color) / 255.0F;
                float b = (float) FastColor.ARGB32.blue(color) / 255.0F;
                float a = (float) FastColor.ARGB32.alpha(color) / 255.0F;
                renderVoxelShape(matrixStack, builder, blockstate.m_60651_(level, pos, CollisionContext.of(camera.getEntity())), (double) pos.m_123341_() - pX, (double) pos.m_123342_() - pY, (double) pos.m_123343_() - pZ, r, g, b, a);
            }
        }
        lookPos = null;
    }

    private static void renderVoxelShape(PoseStack pMatrixStack, VertexConsumer pBuffer, VoxelShape pShape, double pX, double pY, double pZ, float pRed, float pGreen, float pBlue, float pAlpha) {
        PoseStack.Pose last = pMatrixStack.last();
        pShape.forAllEdges((e1, e2, e3, e4, e5, e6) -> {
            float f = (float) (e4 - e1);
            float f1 = (float) (e5 - e2);
            float f2 = (float) (e6 - e3);
            float f3 = Mth.sqrt(f * f + f1 * f1 + f2 * f2);
            f /= f3;
            f1 /= f3;
            f2 /= f3;
            pBuffer.vertex(last.pose(), (float) (e1 + pX), (float) (e2 + pY), (float) (e3 + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(last.normal(), f, f1, f2).endVertex();
            pBuffer.vertex(last.pose(), (float) (e4 + pX), (float) (e5 + pY), (float) (e6 + pZ)).color(pRed, pGreen, pBlue, pAlpha).normal(last.normal(), f, f1, f2).endVertex();
        });
    }
}