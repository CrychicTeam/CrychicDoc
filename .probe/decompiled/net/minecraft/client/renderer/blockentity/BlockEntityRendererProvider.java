package net.minecraft.client.renderer.blockentity;

import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

@FunctionalInterface
public interface BlockEntityRendererProvider<T extends BlockEntity> {

    BlockEntityRenderer<T> create(BlockEntityRendererProvider.Context var1);

    public static class Context {

        private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

        private final BlockRenderDispatcher blockRenderDispatcher;

        private final ItemRenderer itemRenderer;

        private final EntityRenderDispatcher entityRenderer;

        private final EntityModelSet modelSet;

        private final Font font;

        public Context(BlockEntityRenderDispatcher blockEntityRenderDispatcher0, BlockRenderDispatcher blockRenderDispatcher1, ItemRenderer itemRenderer2, EntityRenderDispatcher entityRenderDispatcher3, EntityModelSet entityModelSet4, Font font5) {
            this.blockEntityRenderDispatcher = blockEntityRenderDispatcher0;
            this.blockRenderDispatcher = blockRenderDispatcher1;
            this.itemRenderer = itemRenderer2;
            this.entityRenderer = entityRenderDispatcher3;
            this.modelSet = entityModelSet4;
            this.font = font5;
        }

        public BlockEntityRenderDispatcher getBlockEntityRenderDispatcher() {
            return this.blockEntityRenderDispatcher;
        }

        public BlockRenderDispatcher getBlockRenderDispatcher() {
            return this.blockRenderDispatcher;
        }

        public EntityRenderDispatcher getEntityRenderer() {
            return this.entityRenderer;
        }

        public ItemRenderer getItemRenderer() {
            return this.itemRenderer;
        }

        public EntityModelSet getModelSet() {
            return this.modelSet;
        }

        public ModelPart bakeLayer(ModelLayerLocation modelLayerLocation0) {
            return this.modelSet.bakeLayer(modelLayerLocation0);
        }

        public Font getFont() {
            return this.font;
        }
    }
}