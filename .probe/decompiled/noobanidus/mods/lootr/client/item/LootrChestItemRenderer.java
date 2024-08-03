package noobanidus.mods.lootr.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import noobanidus.mods.lootr.block.entities.LootrChestBlockEntity;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.init.ModBlocks;

public class LootrChestItemRenderer extends BlockEntityWithoutLevelRenderer {

    private static LootrChestItemRenderer INSTANCE = null;

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    private final LootrChestBlockEntity tile;

    public LootrChestItemRenderer(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        this.blockEntityRenderDispatcher = pBlockEntityRenderDispatcher;
        this.tile = new LootrChestBlockEntity(BlockPos.ZERO, ModBlocks.CHEST.get().m_49966_());
    }

    public LootrChestItemRenderer() {
        this(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    public static LootrChestItemRenderer getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LootrChestItemRenderer();
        }
        return INSTANCE;
    }

    @Override
    public void renderByItem(ItemStack itemStack0, ItemDisplayContext itemDisplayContext1, PoseStack poseStack2, MultiBufferSource multiBufferSource3, int int4, int int5) {
        this.blockEntityRenderDispatcher.renderItem(this.tile, poseStack2, multiBufferSource3, int4, int5);
    }

    public void renderByMinecart(LootrChestMinecartEntity entity, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        boolean open = this.tile.isOpened();
        this.tile.setOpened(entity.isOpened());
        this.blockEntityRenderDispatcher.renderItem(this.tile, matrixStack, buffer, combinedLight, OverlayTexture.NO_OVERLAY);
        this.tile.setOpened(open);
    }
}