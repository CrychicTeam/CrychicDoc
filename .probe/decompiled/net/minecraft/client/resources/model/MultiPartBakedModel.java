package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

public class MultiPartBakedModel implements BakedModel {

    private final List<Pair<Predicate<BlockState>, BakedModel>> selectors;

    protected final boolean hasAmbientOcclusion;

    protected final boolean isGui3d;

    protected final boolean usesBlockLight;

    protected final TextureAtlasSprite particleIcon;

    protected final ItemTransforms transforms;

    protected final ItemOverrides overrides;

    private final Map<BlockState, BitSet> selectorCache = new Object2ObjectOpenCustomHashMap(Util.identityStrategy());

    public MultiPartBakedModel(List<Pair<Predicate<BlockState>, BakedModel>> listPairPredicateBlockStateBakedModel0) {
        this.selectors = listPairPredicateBlockStateBakedModel0;
        BakedModel $$1 = (BakedModel) ((Pair) listPairPredicateBlockStateBakedModel0.iterator().next()).getRight();
        this.hasAmbientOcclusion = $$1.useAmbientOcclusion();
        this.isGui3d = $$1.isGui3d();
        this.usesBlockLight = $$1.usesBlockLight();
        this.particleIcon = $$1.getParticleIcon();
        this.transforms = $$1.getTransforms();
        this.overrides = $$1.getOverrides();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState0, @Nullable Direction direction1, RandomSource randomSource2) {
        if (blockState0 == null) {
            return Collections.emptyList();
        } else {
            BitSet $$3 = (BitSet) this.selectorCache.get(blockState0);
            if ($$3 == null) {
                $$3 = new BitSet();
                for (int $$4 = 0; $$4 < this.selectors.size(); $$4++) {
                    Pair<Predicate<BlockState>, BakedModel> $$5 = (Pair<Predicate<BlockState>, BakedModel>) this.selectors.get($$4);
                    if (((Predicate) $$5.getLeft()).test(blockState0)) {
                        $$3.set($$4);
                    }
                }
                this.selectorCache.put(blockState0, $$3);
            }
            List<BakedQuad> $$6 = Lists.newArrayList();
            long $$7 = randomSource2.nextLong();
            for (int $$8 = 0; $$8 < $$3.length(); $$8++) {
                if ($$3.get($$8)) {
                    $$6.addAll(((BakedModel) ((Pair) this.selectors.get($$8)).getRight()).getQuads(blockState0, direction1, RandomSource.create($$7)));
                }
            }
            return $$6;
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.hasAmbientOcclusion;
    }

    @Override
    public boolean isGui3d() {
        return this.isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return this.usesBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particleIcon;
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.transforms;
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.overrides;
    }

    public static class Builder {

        private final List<Pair<Predicate<BlockState>, BakedModel>> selectors = Lists.newArrayList();

        public void add(Predicate<BlockState> predicateBlockState0, BakedModel bakedModel1) {
            this.selectors.add(Pair.of(predicateBlockState0, bakedModel1));
        }

        public BakedModel build() {
            return new MultiPartBakedModel(this.selectors);
        }
    }
}