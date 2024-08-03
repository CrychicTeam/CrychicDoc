package me.jellysquid.mods.sodium.mixin.features.model;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.data.ModelData;
import org.embeddedt.embeddium.model.UnwrappableBakedModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ WeightedBakedModel.class })
public class WeightedBakedModelMixin implements UnwrappableBakedModel {

    @Shadow
    @Final
    private List<WeightedEntry.Wrapper<BakedModel>> list;

    @Shadow
    @Final
    private int totalWeight;

    @Overwrite(remap = false)
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, RandomSource random, ModelData modelData, RenderType renderLayer) {
        WeightedEntry.Wrapper<BakedModel> quad = getAt(this.list, Math.abs((int) random.nextLong()) % this.totalWeight);
        return quad != null ? quad.getData().getQuads(state, face, random, modelData, renderLayer) : Collections.emptyList();
    }

    @Overwrite(remap = false)
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        WeightedEntry.Wrapper<BakedModel> quad = getAt(this.list, Math.abs((int) rand.nextLong()) % this.totalWeight);
        return quad != null ? quad.getData().getRenderTypes(state, rand, data) : ChunkRenderTypeSet.none();
    }

    @Unique
    private static <T extends WeightedEntry> T getAt(List<T> pool, int totalWeight) {
        int i = 0;
        int len = pool.size();
        while (i < len) {
            T weighted = (T) pool.get(i++);
            totalWeight -= weighted.getWeight().asInt();
            if (totalWeight < 0) {
                return weighted;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public BakedModel embeddium$getInnerModel(RandomSource rand) {
        WeightedEntry.Wrapper<BakedModel> quad = getAt(this.list, Math.abs((int) rand.nextLong()) % this.totalWeight);
        return quad != null && quad.getData().getClass() == SimpleBakedModel.class ? quad.getData() : null;
    }
}