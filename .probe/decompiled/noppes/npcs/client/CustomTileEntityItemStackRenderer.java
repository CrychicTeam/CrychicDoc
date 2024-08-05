package noppes.npcs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import noppes.npcs.items.ItemNpcBlock;

public class CustomTileEntityItemStackRenderer extends BlockEntityWithoutLevelRenderer {

    private static CustomTileEntityItemStackRenderer i = null;

    public static IClientItemExtensions itemRenderProperties = new IClientItemExtensions() {

        public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
            return CustomTileEntityItemStackRenderer.instance();
        }
    };

    private HashMap<Block, BlockEntity> data = new HashMap();

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public CustomTileEntityItemStackRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet model) {
        super(dispatcher, model);
        this.blockEntityRenderDispatcher = dispatcher;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext p_239207_2_, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (stack.getItem() instanceof ItemNpcBlock) {
            ItemNpcBlock item = (ItemNpcBlock) stack.getItem();
            BlockEntity tile = (BlockEntity) this.data.get(item.block);
            if (tile == null) {
                this.data.put(item.block, tile = ((BaseEntityBlock) item.block).m_142194_(BlockPos.ZERO, null));
            }
            this.blockEntityRenderDispatcher.renderItem(tile, matrixStack, buffer, combinedLight, combinedOverlay);
        }
    }

    public static CustomTileEntityItemStackRenderer instance() {
        if (i != null) {
            return i;
        } else {
            Minecraft mc = Minecraft.getInstance();
            i = new CustomTileEntityItemStackRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
            return i;
        }
    }
}