package net.minecraft.client.renderer.entity;

import net.minecraft.client.gui.Font;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface EntityRendererProvider<T extends Entity> {

    EntityRenderer<T> create(EntityRendererProvider.Context var1);

    public static class Context {

        private final EntityRenderDispatcher entityRenderDispatcher;

        private final ItemRenderer itemRenderer;

        private final BlockRenderDispatcher blockRenderDispatcher;

        private final ItemInHandRenderer itemInHandRenderer;

        private final ResourceManager resourceManager;

        private final EntityModelSet modelSet;

        private final Font font;

        public Context(EntityRenderDispatcher entityRenderDispatcher0, ItemRenderer itemRenderer1, BlockRenderDispatcher blockRenderDispatcher2, ItemInHandRenderer itemInHandRenderer3, ResourceManager resourceManager4, EntityModelSet entityModelSet5, Font font6) {
            this.entityRenderDispatcher = entityRenderDispatcher0;
            this.itemRenderer = itemRenderer1;
            this.blockRenderDispatcher = blockRenderDispatcher2;
            this.itemInHandRenderer = itemInHandRenderer3;
            this.resourceManager = resourceManager4;
            this.modelSet = entityModelSet5;
            this.font = font6;
        }

        public EntityRenderDispatcher getEntityRenderDispatcher() {
            return this.entityRenderDispatcher;
        }

        public ItemRenderer getItemRenderer() {
            return this.itemRenderer;
        }

        public BlockRenderDispatcher getBlockRenderDispatcher() {
            return this.blockRenderDispatcher;
        }

        public ItemInHandRenderer getItemInHandRenderer() {
            return this.itemInHandRenderer;
        }

        public ResourceManager getResourceManager() {
            return this.resourceManager;
        }

        public EntityModelSet getModelSet() {
            return this.modelSet;
        }

        public ModelManager getModelManager() {
            return this.blockRenderDispatcher.getBlockModelShaper().getModelManager();
        }

        public ModelPart bakeLayer(ModelLayerLocation modelLayerLocation0) {
            return this.modelSet.bakeLayer(modelLayerLocation0);
        }

        public Font getFont() {
            return this.font;
        }
    }
}