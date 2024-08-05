package net.minecraftforge.client.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.RenderTypeHelper;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IForgeBakedModel {

    private BakedModel self() {
        return (BakedModel) this;
    }

    @NotNull
    default List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        return this.self().getQuads(state, side, rand);
    }

    default boolean useAmbientOcclusion(BlockState state) {
        return this.self().useAmbientOcclusion();
    }

    default boolean useAmbientOcclusion(BlockState state, RenderType renderType) {
        return this.self().useAmbientOcclusion(state);
    }

    default BakedModel applyTransform(ItemDisplayContext transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
        this.self().getTransforms().getTransform(transformType).apply(applyLeftHandTransform, poseStack);
        return this.self();
    }

    @NotNull
    default ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        return modelData;
    }

    default TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return this.self().getParticleIcon();
    }

    default ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        return ItemBlockRenderTypes.getRenderLayers(state);
    }

    default List<RenderType> getRenderTypes(ItemStack itemStack, boolean fabulous) {
        return List.of(RenderTypeHelper.getFallbackItemRenderType(itemStack, this.self(), fabulous));
    }

    default List<BakedModel> getRenderPasses(ItemStack itemStack, boolean fabulous) {
        return List.of(this.self());
    }
}