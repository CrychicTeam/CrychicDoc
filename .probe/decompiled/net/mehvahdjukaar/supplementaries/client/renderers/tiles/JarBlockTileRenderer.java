package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.util.RotHlpr;
import net.mehvahdjukaar.moonlight.api.client.util.VertexUtil;
import net.mehvahdjukaar.moonlight.api.fluids.BuiltInSoftFluids;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.client.block_models.JarBakedModel;
import net.mehvahdjukaar.supplementaries.client.renderers.VertexUtils;
import net.mehvahdjukaar.supplementaries.common.block.tiles.JarBlockTile;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.MobContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class JarBlockTileRenderer extends CageBlockTileRenderer<JarBlockTile> {

    private final ItemRenderer itemRenderer;

    private static final boolean USE_MODEL = false;

    public JarBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        Minecraft minecraft = Minecraft.getInstance();
        this.itemRenderer = minecraft.getItemRenderer();
    }

    public static void renderFluid(float percentageFill, int color, int luminosity, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferIn, int light, int combinedOverlayIn) {
        poseStack.pushPose();
        if (luminosity != 0) {
            light = light & 15728640 | luminosity << 4;
        }
        VertexConsumer builder = ModMaterials.get(texture).buffer(bufferIn, RenderType::m_110470_);
        Vector3f dimensions = JarBakedModel.getJarLiquidDimensions();
        poseStack.translate(0.5, (double) dimensions.z(), 0.5);
        VertexUtil.addCube(builder, poseStack, dimensions.x(), percentageFill * dimensions.y(), light, color);
        poseStack.popPose();
    }

    public void render(JarBlockTile tile, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        long r = tile.m_58899_().asLong();
        RandomSource rand = RandomSource.create(r);
        AtomicInteger i = new AtomicInteger();
        renderCookies(poseStack, bufferIn, rand, combinedLightIn, combinedOverlayIn, () -> {
            int j = i.getAndIncrement();
            return j < tile.m_6643_() ? tile.m_8020_(j) : ItemStack.EMPTY;
        });
        MobContainer.MobNBTData data = tile.mobContainer.getData();
        if (data != null) {
            if (data.is2DFish()) {
                poseStack.pushPose();
                long time = System.currentTimeMillis() + r;
                float angle = (float) (time % 28800L) / 80.0F;
                float angle2 = (float) (time % 1080L) / 3.0F;
                float angle3 = (float) (time % 126000L) / 350.0F;
                float wo = 0.015F * Mth.sin((float) ((Math.PI * 2) * (double) angle2 / 360.0));
                float ho = 0.1F * Mth.sin((float) ((Math.PI * 2) * (double) angle3 / 360.0));
                poseStack.translate(0.5, 0.5, 0.5);
                Quaternionf rotation = Axis.YP.rotationDegrees(-angle);
                poseStack.mulPose(rotation);
                poseStack.scale(0.625F, 0.625F, 0.625F);
                Vector3f dimensions = JarBakedModel.getJarLiquidDimensions();
                poseStack.translate(0.0, -0.2, -0.335 * (double) (dimensions.x() / 0.5F));
                int fishType = data.getFishTexture();
                VertexUtils.renderFish(bufferIn, poseStack, wo, ho, fishType, combinedLightIn);
                poseStack.popPose();
            } else {
                super.render(tile, partialTicks, poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
            }
            Optional<Holder<SoftFluid>> fluid = tile.mobContainer.shouldRenderWithFluid();
            if (fluid != null && fluid.isPresent()) {
                if (((Holder) fluid.get()).is(BuiltInSoftFluids.WATER.getID())) {
                    poseStack.pushPose();
                    Vector3f dimensions = JarBakedModel.getJarLiquidDimensions();
                    poseStack.translate(0.5, 0.0015 + (double) dimensions.z(), 0.5);
                    VertexConsumer builder = ModMaterials.SAND_MATERIAL.buffer(bufferIn, RenderType::m_110452_);
                    VertexUtil.addCube(builder, poseStack, 0.99F * dimensions.x(), dimensions.y() / 12.0F, combinedLightIn, -1);
                    poseStack.popPose();
                }
                poseStack.pushPose();
                SoftFluid s = (SoftFluid) ((Holder) fluid.get()).value();
                renderFluid(0.75F, s.getTintColor(), 0, s.getStillTexture(), poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
                poseStack.popPose();
            }
        }
        SoftFluidTank tank = tile.fluidHolder;
        if (!tank.isEmpty()) {
            SoftFluid fluid = tank.getFluidValue();
            renderFluid(tank.getHeight(1.0F), tank.getCachedStillColor(tile.m_58904_(), tile.m_58899_()), fluid.getLuminosity(), fluid.getStillTexture(), poseStack, bufferIn, combinedLightIn, combinedOverlayIn);
        }
    }

    public static void renderCookies(PoseStack poseStack, MultiBufferSource buffer, RandomSource rand, int light, int overlay, Supplier<ItemStack> itemIterator) {
        ItemStack cookieStack = (ItemStack) itemIterator.get();
        if (!cookieStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.5, 0.5);
            poseStack.mulPose(RotHlpr.XN90);
            poseStack.translate(0.0, 0.0, -0.5);
            float scale = 0.5714286F;
            poseStack.scale(scale, scale, scale);
            do {
                poseStack.mulPose(Axis.ZP.rotationDegrees((float) rand.nextInt(360)));
                poseStack.translate(0.0F, 0.0F, 1.0F / (16.0F * scale));
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                BakedModel model = itemRenderer.getModel(cookieStack, null, null, 0);
                itemRenderer.render(cookieStack, ItemDisplayContext.FIXED, true, poseStack, buffer, light, overlay, model);
                cookieStack = (ItemStack) itemIterator.get();
            } while (!cookieStack.isEmpty());
            poseStack.popPose();
        }
    }
}