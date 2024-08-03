package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class SimpleBakedModel implements BakedModel {

    protected final List<BakedQuad> unculledFaces;

    protected final Map<Direction, List<BakedQuad>> culledFaces;

    protected final boolean hasAmbientOcclusion;

    protected final boolean isGui3d;

    protected final boolean usesBlockLight;

    protected final TextureAtlasSprite particleIcon;

    protected final ItemTransforms transforms;

    protected final ItemOverrides overrides;

    public SimpleBakedModel(List<BakedQuad> listBakedQuad0, Map<Direction, List<BakedQuad>> mapDirectionListBakedQuad1, boolean boolean2, boolean boolean3, boolean boolean4, TextureAtlasSprite textureAtlasSprite5, ItemTransforms itemTransforms6, ItemOverrides itemOverrides7) {
        this.unculledFaces = listBakedQuad0;
        this.culledFaces = mapDirectionListBakedQuad1;
        this.hasAmbientOcclusion = boolean2;
        this.isGui3d = boolean4;
        this.usesBlockLight = boolean3;
        this.particleIcon = textureAtlasSprite5;
        this.transforms = itemTransforms6;
        this.overrides = itemOverrides7;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState0, @Nullable Direction direction1, RandomSource randomSource2) {
        return direction1 == null ? this.unculledFaces : (List) this.culledFaces.get(direction1);
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

        private final List<BakedQuad> unculledFaces = Lists.newArrayList();

        private final Map<Direction, List<BakedQuad>> culledFaces = Maps.newEnumMap(Direction.class);

        private final ItemOverrides overrides;

        private final boolean hasAmbientOcclusion;

        private TextureAtlasSprite particleIcon;

        private final boolean usesBlockLight;

        private final boolean isGui3d;

        private final ItemTransforms transforms;

        public Builder(BlockModel blockModel0, ItemOverrides itemOverrides1, boolean boolean2) {
            this(blockModel0.hasAmbientOcclusion(), blockModel0.getGuiLight().lightLikeBlock(), boolean2, blockModel0.getTransforms(), itemOverrides1);
        }

        private Builder(boolean boolean0, boolean boolean1, boolean boolean2, ItemTransforms itemTransforms3, ItemOverrides itemOverrides4) {
            for (Direction $$5 : Direction.values()) {
                this.culledFaces.put($$5, Lists.newArrayList());
            }
            this.overrides = itemOverrides4;
            this.hasAmbientOcclusion = boolean0;
            this.usesBlockLight = boolean1;
            this.isGui3d = boolean2;
            this.transforms = itemTransforms3;
        }

        public SimpleBakedModel.Builder addCulledFace(Direction direction0, BakedQuad bakedQuad1) {
            ((List) this.culledFaces.get(direction0)).add(bakedQuad1);
            return this;
        }

        public SimpleBakedModel.Builder addUnculledFace(BakedQuad bakedQuad0) {
            this.unculledFaces.add(bakedQuad0);
            return this;
        }

        public SimpleBakedModel.Builder particle(TextureAtlasSprite textureAtlasSprite0) {
            this.particleIcon = textureAtlasSprite0;
            return this;
        }

        public SimpleBakedModel.Builder item() {
            return this;
        }

        public BakedModel build() {
            if (this.particleIcon == null) {
                throw new RuntimeException("Missing particle!");
            } else {
                return new SimpleBakedModel(this.unculledFaces, this.culledFaces, this.hasAmbientOcclusion, this.usesBlockLight, this.isGui3d, this.particleIcon, this.transforms, this.overrides);
            }
        }
    }
}