package dev.xkmc.l2weaponry.content.client;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.xkmc.l2weaponry.content.item.types.NunchakuItem;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;

public class WeaponBEWLR extends BlockEntityWithoutLevelRenderer {

    public static final Supplier<BlockEntityWithoutLevelRenderer> INSTANCE = Suppliers.memoize(() -> new WeaponBEWLR(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels()));

    public static final IClientItemExtensions EXTENSIONS = new IClientItemExtensions() {

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            return (BlockEntityWithoutLevelRenderer) WeaponBEWLR.INSTANCE.get();
        }
    };

    public WeaponBEWLR(BlockEntityRenderDispatcher dispatcher, EntityModelSet set) {
        super(dispatcher, set);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext type, PoseStack pose, MultiBufferSource bufferSource, int light, int overlay) {
        if (stack.getItem() instanceof NunchakuItem nunchaku) {
            this.renderNunchaku(nunchaku, stack, type, pose, bufferSource, light, overlay);
        }
    }

    public void renderNunchaku(NunchakuItem nunchaku, ItemStack stack, ItemDisplayContext type, PoseStack pose, MultiBufferSource bufferSource, int light, int overlay) {
        ModelManager manager = Minecraft.getInstance().getModelManager();
        this.render(stack, type, false, pose, bufferSource, light, overlay, manager.getModel(ForgeRegistries.ITEMS.getKey(nunchaku).withPath((UnaryOperator<String>) (e -> "item/" + e + "_unroll"))), WeaponBEWLR::nunchakuUnroll);
        this.render(stack, type, false, pose, bufferSource, light, overlay, manager.getModel(ForgeRegistries.ITEMS.getKey(nunchaku).withPath((UnaryOperator<String>) (e -> "item/" + e + "_roll"))), WeaponBEWLR::nunchakuRoll);
    }

    private static void nunchakuUnroll(PoseStack pose) {
    }

    private static void nunchakuRoll(PoseStack pose) {
        float tick = Minecraft.getInstance().getPartialTick() + (float) Minecraft.getInstance().player.f_19797_;
        pose.translate(1.0F, 1.0F, 0.0F);
        pose.rotateAround(Axis.ZP.rotationDegrees(tick * 72.0F), 0.0F, 0.0F, 0.0F);
    }

    public void render(ItemStack stack, ItemDisplayContext ctx, boolean offhand, PoseStack pose, MultiBufferSource buffer, int light, int overlay, BakedModel baked, Consumer<PoseStack> transform) {
        ItemRenderer ir = Minecraft.getInstance().getItemRenderer();
        if (!stack.isEmpty()) {
            pose.pushPose();
            transform.accept(pose);
            boolean flag1 = true;
            for (BakedModel model : baked.getRenderPasses(stack, flag1)) {
                for (RenderType rendertype : model.getRenderTypes(stack, flag1)) {
                    VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, stack.hasFoil());
                    ir.renderModelLists(model, stack, light, overlay, pose, vertexconsumer);
                }
            }
            pose.popPose();
        }
    }
}