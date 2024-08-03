package net.blay09.mods.balm.api.client.rendering;

import java.util.function.Supplier;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface BalmRenderers {

    ModelLayerLocation registerModel(ResourceLocation var1, Supplier<LayerDefinition> var2);

    <T extends Entity> void registerEntityRenderer(Supplier<EntityType<T>> var1, EntityRendererProvider<? super T> var2);

    <T extends BlockEntity> void registerBlockEntityRenderer(Supplier<BlockEntityType<T>> var1, BlockEntityRendererProvider<? super T> var2);

    void registerBlockColorHandler(BlockColor var1, Supplier<Block[]> var2);

    void registerItemColorHandler(ItemColor var1, Supplier<ItemLike[]> var2);

    @Deprecated
    void setBlockRenderType(Supplier<Block> var1, RenderType var2);
}