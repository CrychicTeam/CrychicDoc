package org.embeddedt.modernfix.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SimpleItemModelView implements BakedModel {

    private BakedModel wrappedItem;

    private FastItemRenderType type;

    private final List<BakedQuad> nullQuadList = new ObjectArrayList();

    public void setItem(BakedModel model) {
        this.wrappedItem = model;
    }

    public void setType(FastItemRenderType type) {
        this.type = type;
    }

    private boolean isCorrectDirectionForType(Direction direction) {
        return this.type == FastItemRenderType.SIMPLE_ITEM ? direction == Direction.SOUTH : direction == Direction.UP || direction == Direction.EAST || direction == Direction.NORTH;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        boolean isWholeListValid = this.isCorrectDirectionForType(side);
        List<BakedQuad> realList = this.wrappedItem.getQuads(state, side, rand);
        if (isWholeListValid) {
            return realList;
        } else {
            this.nullQuadList.clear();
            for (int i = 0; i < realList.size(); i++) {
                BakedQuad quad = (BakedQuad) realList.get(i);
                if (this.isCorrectDirectionForType(quad.getDirection())) {
                    this.nullQuadList.add(quad);
                }
            }
            return this.nullQuadList;
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.wrappedItem.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.wrappedItem.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.wrappedItem.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.wrappedItem.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.wrappedItem.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return this.wrappedItem.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return this.wrappedItem.getOverrides();
    }
}