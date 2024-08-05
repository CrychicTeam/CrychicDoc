package com.rekindled.embers.render;

import com.rekindled.embers.blockentity.PipeBlockEntityBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class BakedPipeModel implements BakedModel {

    private final BakedModel centerModel;

    private BakedModel[] connectionModel;

    private BakedModel[] endModel;

    public static final List<BakedQuad> EMPTY = new ArrayList();

    public final List<BakedQuad>[] QUAD_CACHE = new List[729];

    public BakedPipeModel(BakedModel centerModel, BakedModel[] connectionModel, BakedModel[] endModel) {
        this.centerModel = centerModel;
        this.connectionModel = connectionModel;
        this.endModel = endModel;
    }

    public static int getCacheIndex(int[] data) {
        return ((((data[0] * 3 + data[1]) * 3 + data[2]) * 3 + data[3]) * 3 + data[4]) * 3 + data[5];
    }

    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        if (side != null) {
            return EMPTY;
        } else {
            int[] sides = data.get(PipeBlockEntityBase.DATA_TYPE);
            if (sides != null) {
                List<BakedQuad> quads = this.QUAD_CACHE[getCacheIndex(sides)];
                if (quads != null) {
                    return quads;
                } else {
                    List<BakedQuad> var9 = new ArrayList();
                    var9.addAll(this.centerModel.getQuads(state, side, rand, data, renderType));
                    if (var9.isEmpty()) {
                        return var9;
                    } else {
                        for (int i = 0; i < sides.length; i++) {
                            if (sides[i] == 1) {
                                var9.addAll(this.connectionModel[i].getQuads(state, side, rand, data, renderType));
                            } else if (sides[i] == 2) {
                                var9.addAll(this.endModel[i].getQuads(state, side, rand, data, renderType));
                            }
                        }
                        if (!var9.isEmpty()) {
                            this.QUAD_CACHE[getCacheIndex(sides)] = new ArrayList(var9);
                        }
                        return var9;
                    }
                }
            } else {
                return this.centerModel.getQuads(state, side, rand, data, renderType);
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand) {
        return this.centerModel.getQuads(state, side, rand);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.centerModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.centerModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.centerModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return this.centerModel.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.centerModel.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}