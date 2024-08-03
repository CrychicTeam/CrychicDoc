package net.blay09.mods.waystones.client;

import java.util.Objects;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.waystones.block.ModBlocks;
import net.blay09.mods.waystones.block.SharestoneBlock;
import net.blay09.mods.waystones.block.entity.ModBlockEntities;
import net.blay09.mods.waystones.client.render.PortstoneModel;
import net.blay09.mods.waystones.client.render.PortstoneRenderer;
import net.blay09.mods.waystones.client.render.SharestoneModel;
import net.blay09.mods.waystones.client.render.SharestoneRenderer;
import net.blay09.mods.waystones.client.render.WaystoneModel;
import net.blay09.mods.waystones.client.render.WaystoneRenderer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

public class ModRenderers {

    public static ModelLayerLocation portstoneModel;

    public static ModelLayerLocation sharestoneModel;

    public static ModelLayerLocation waystoneModel;

    public static void initialize(BalmRenderers renderers) {
        portstoneModel = renderers.registerModel(new ResourceLocation("waystones", "portstone"), () -> PortstoneModel.createLayer(CubeDeformation.NONE));
        sharestoneModel = renderers.registerModel(new ResourceLocation("waystones", "sharestone"), () -> SharestoneModel.createLayer(CubeDeformation.NONE));
        waystoneModel = renderers.registerModel(new ResourceLocation("waystones", "waystone"), () -> WaystoneModel.createLayer(CubeDeformation.NONE));
        renderers.registerBlockEntityRenderer(ModBlockEntities.waystone::get, WaystoneRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.sharestone::get, SharestoneRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.portstone::get, PortstoneRenderer::new);
        renderers.registerBlockColorHandler((state, view, pos, tintIndex) -> ((DyeColor) Objects.requireNonNull(((SharestoneBlock) state.m_60734_()).getColor())).getTextColor(), () -> ModBlocks.scopedSharestones);
        renderers.registerItemColorHandler((stack, tintIndex) -> ((DyeColor) Objects.requireNonNull(((SharestoneBlock) Block.byItem(stack.getItem())).getColor())).getTextColor(), () -> ModBlocks.scopedSharestones);
        renderers.setBlockRenderType(() -> ModBlocks.sharestone, RenderType.cutout());
    }
}