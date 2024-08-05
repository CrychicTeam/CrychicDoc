package net.mehvahdjukaar.moonlight.api.client.model;

import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface CustomBakedModel extends BakedModel {

    List<BakedQuad> getBlockQuads(BlockState var1, Direction var2, RandomSource var3, @Nullable RenderType var4, ExtraModelData var5);

    TextureAtlasSprite getBlockParticle(ExtraModelData var1);

    @Internal
    @Override
    default List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    @Override
    default TextureAtlasSprite getParticleIcon() {
        return this.getBlockParticle(ExtraModelData.EMPTY);
    }

    default ExtraModelData getModelData(@NotNull ExtraModelData tileData, BlockPos pos, BlockState state, BlockAndTintGetter level) {
        return tileData;
    }

    default ExtraModelData getModelData(@NotNull ExtraModelData originalData, ItemStack stack) {
        return originalData;
    }
}