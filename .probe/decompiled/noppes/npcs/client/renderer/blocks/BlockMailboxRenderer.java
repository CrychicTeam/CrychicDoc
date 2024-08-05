package noppes.npcs.client.renderer.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import noppes.npcs.blocks.tiles.TileMailbox;
import noppes.npcs.client.model.blocks.ModelMailboxUS;
import noppes.npcs.client.model.blocks.ModelMailboxWow;

public class BlockMailboxRenderer<T extends TileMailbox> implements BlockEntityRenderer<T> {

    private final ModelMailboxUS model = new ModelMailboxUS();

    private final ModelMailboxWow model2 = new ModelMailboxWow();

    private static final ResourceLocation text1 = new ResourceLocation("customnpcs", "textures/models/mailbox1.png");

    private static final ResourceLocation text2 = new ResourceLocation("customnpcs", "textures/models/mailbox2.png");

    private static final ResourceLocation text3 = new ResourceLocation("customnpcs", "textures/models/mailbox3.png");

    private static final RenderType type1 = RenderType.entityCutout(text1);

    private static final RenderType type2 = RenderType.entityCutout(text2);

    private static final RenderType type3 = RenderType.entityCutout(text3);

    public BlockMailboxRenderer(BlockEntityRendererProvider.Context dispatcher) {
    }

    public void render(TileMailbox te, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        int meta = 0;
        int type = te.getModel();
        matrixStack.pushPose();
        matrixStack.translate(0.5F, 1.5F, 0.5F);
        matrixStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Axis.YP.rotationDegrees((float) (90 * meta)));
        if (type == 0) {
            this.model.renderToBuffer(matrixStack, buffer.getBuffer(type1), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        } else if (type == 1) {
            this.model2.renderToBuffer(matrixStack, buffer.getBuffer(type2), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        } else if (type == 2) {
            this.model2.renderToBuffer(matrixStack, buffer.getBuffer(type3), light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        matrixStack.popPose();
    }
}