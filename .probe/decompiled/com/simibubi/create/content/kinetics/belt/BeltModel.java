package com.simibubi.create.content.kinetics.belt;

import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class BeltModel extends BakedModelWrapper<BakedModel> {

    public static final ModelProperty<BeltBlockEntity.CasingType> CASING_PROPERTY = new ModelProperty<>();

    public static final ModelProperty<Boolean> COVER_PROPERTY = new ModelProperty<>();

    private static final SpriteShiftEntry SPRITE_SHIFT = AllSpriteShifts.ANDESIDE_BELT_CASING;

    public BeltModel(BakedModel template) {
        super(template);
    }

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        if (!data.has(CASING_PROPERTY)) {
            return super.getParticleIcon(data);
        } else {
            BeltBlockEntity.CasingType type = data.get(CASING_PROPERTY);
            return type != BeltBlockEntity.CasingType.NONE && type != BeltBlockEntity.CasingType.BRASS ? AllSpriteShifts.ANDESITE_CASING.getOriginal() : super.getParticleIcon(data);
        }
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, extraData, renderType);
        if (!extraData.has(CASING_PROPERTY)) {
            return quads;
        } else {
            boolean cover = extraData.get(COVER_PROPERTY);
            BeltBlockEntity.CasingType type = extraData.get(CASING_PROPERTY);
            boolean brassCasing = type == BeltBlockEntity.CasingType.BRASS;
            if (type != BeltBlockEntity.CasingType.NONE && (!brassCasing || cover)) {
                List<BakedQuad> var18 = new ArrayList(quads);
                if (cover) {
                    boolean alongX = ((Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING)).getAxis() == Direction.Axis.X;
                    BakedModel coverModel = (brassCasing ? (alongX ? AllPartialModels.BRASS_BELT_COVER_X : AllPartialModels.BRASS_BELT_COVER_Z) : (alongX ? AllPartialModels.ANDESITE_BELT_COVER_X : AllPartialModels.ANDESITE_BELT_COVER_Z)).get();
                    var18.addAll(coverModel.getQuads(state, side, rand, extraData, renderType));
                }
                if (brassCasing) {
                    return var18;
                } else {
                    for (int i = 0; i < var18.size(); i++) {
                        BakedQuad quad = (BakedQuad) var18.get(i);
                        TextureAtlasSprite original = quad.getSprite();
                        if (original == SPRITE_SHIFT.getOriginal()) {
                            BakedQuad newQuad = BakedQuadHelper.clone(quad);
                            int[] vertexData = newQuad.getVertices();
                            for (int vertex = 0; vertex < 4; vertex++) {
                                float u = BakedQuadHelper.getU(vertexData, vertex);
                                float v = BakedQuadHelper.getV(vertexData, vertex);
                                BakedQuadHelper.setU(vertexData, vertex, SPRITE_SHIFT.getTargetU(u));
                                BakedQuadHelper.setV(vertexData, vertex, SPRITE_SHIFT.getTargetV(v));
                            }
                            var18.set(i, newQuad);
                        }
                    }
                    return var18;
                }
            } else {
                return quads;
            }
        }
    }
}