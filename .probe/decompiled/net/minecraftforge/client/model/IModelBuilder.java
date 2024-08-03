package net.minecraftforge.client.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraftforge.client.RenderTypeGroup;

public interface IModelBuilder<T extends IModelBuilder<T>> {

    static IModelBuilder<?> of(boolean hasAmbientOcclusion, boolean usesBlockLight, boolean isGui3d, ItemTransforms transforms, ItemOverrides overrides, TextureAtlasSprite particle, RenderTypeGroup renderTypes) {
        return new IModelBuilder.Simple(hasAmbientOcclusion, usesBlockLight, isGui3d, transforms, overrides, particle, renderTypes);
    }

    static IModelBuilder<?> collecting(List<BakedQuad> quads) {
        return new IModelBuilder.Collecting(quads);
    }

    T addCulledFace(Direction var1, BakedQuad var2);

    T addUnculledFace(BakedQuad var1);

    BakedModel build();

    public static class Collecting implements IModelBuilder<IModelBuilder.Collecting> {

        private final List<BakedQuad> quads;

        private Collecting(List<BakedQuad> quads) {
            this.quads = quads;
        }

        public IModelBuilder.Collecting addCulledFace(Direction facing, BakedQuad quad) {
            this.quads.add(quad);
            return this;
        }

        public IModelBuilder.Collecting addUnculledFace(BakedQuad quad) {
            this.quads.add(quad);
            return this;
        }

        @Override
        public BakedModel build() {
            return EmptyModel.BAKED;
        }
    }

    public static class Simple implements IModelBuilder<IModelBuilder.Simple> {

        private final SimpleBakedModel.Builder builder;

        private final RenderTypeGroup renderTypes;

        private Simple(boolean hasAmbientOcclusion, boolean usesBlockLight, boolean isGui3d, ItemTransforms transforms, ItemOverrides overrides, TextureAtlasSprite particle, RenderTypeGroup renderTypes) {
            this.builder = new SimpleBakedModel.Builder(hasAmbientOcclusion, usesBlockLight, isGui3d, transforms, overrides).particle(particle);
            this.renderTypes = renderTypes;
        }

        public IModelBuilder.Simple addCulledFace(Direction facing, BakedQuad quad) {
            this.builder.addCulledFace(facing, quad);
            return this;
        }

        public IModelBuilder.Simple addUnculledFace(BakedQuad quad) {
            this.builder.addUnculledFace(quad);
            return this;
        }

        @Deprecated
        @Override
        public BakedModel build() {
            return this.builder.build(this.renderTypes);
        }
    }
}