package net.minecraftforge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;

public final class RenderTypeHelper {

    @NotNull
    public static RenderType getEntityRenderType(RenderType chunkRenderType, boolean cull) {
        if (chunkRenderType != RenderType.translucent()) {
            return Sheets.cutoutBlockSheet();
        } else {
            return !cull && Minecraft.useShaderTransparency() ? Sheets.translucentItemSheet() : Sheets.translucentCullBlockSheet();
        }
    }

    @NotNull
    public static RenderType getMovingBlockRenderType(RenderType renderType) {
        return renderType == RenderType.translucent() ? RenderType.translucentMovingBlock() : renderType;
    }

    @NotNull
    public static RenderType getFallbackItemRenderType(ItemStack stack, BakedModel model, boolean cull) {
        if (stack.getItem() instanceof BlockItem blockItem) {
            ChunkRenderTypeSet renderTypes = model.getRenderTypes(blockItem.getBlock().defaultBlockState(), RandomSource.create(42L), ModelData.EMPTY);
            return renderTypes.contains(RenderType.translucent()) ? getEntityRenderType(RenderType.translucent(), cull) : Sheets.cutoutBlockSheet();
        } else {
            return cull ? Sheets.translucentCullBlockSheet() : Sheets.translucentItemSheet();
        }
    }

    private RenderTypeHelper() {
    }
}