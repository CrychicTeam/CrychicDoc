package com.simibubi.create.content.decoration.copycat;

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
import net.minecraftforge.client.model.data.ModelData;

public class CopycatBarsModel extends CopycatModel {

    public CopycatBarsModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    protected List<BakedQuad> getCroppedQuads(BlockState state, Direction side, RandomSource rand, BlockState material, ModelData wrappedData, RenderType renderType) {
        BakedModel model = getModelOf(material);
        List<BakedQuad> superQuads = this.originalModel.getQuads(state, side, rand, wrappedData, renderType);
        TextureAtlasSprite targetSprite = model.getParticleIcon(wrappedData);
        boolean vertical = ((Direction) state.m_61143_(CopycatPanelBlock.FACING)).getAxis() == Direction.Axis.Y;
        if (side != null && (vertical || side.getAxis() == Direction.Axis.Y)) {
            List<BakedQuad> templateQuads = model.getQuads(material, null, rand, wrappedData, renderType);
            for (int i = 0; i < templateQuads.size(); i++) {
                BakedQuad quad = (BakedQuad) templateQuads.get(i);
                if (quad.getDirection() == Direction.UP) {
                    targetSprite = quad.getSprite();
                    break;
                }
            }
        }
        if (targetSprite == null) {
            return superQuads;
        } else {
            List<BakedQuad> quads = new ArrayList();
            for (int ix = 0; ix < superQuads.size(); ix++) {
                BakedQuad quad = (BakedQuad) superQuads.get(ix);
                TextureAtlasSprite original = quad.getSprite();
                BakedQuad newQuad = BakedQuadHelper.clone(quad);
                int[] vertexData = newQuad.getVertices();
                for (int vertex = 0; vertex < 4; vertex++) {
                    BakedQuadHelper.setU(vertexData, vertex, targetSprite.getU((double) SpriteShiftEntry.getUnInterpolatedU(original, BakedQuadHelper.getU(vertexData, vertex))));
                    BakedQuadHelper.setV(vertexData, vertex, targetSprite.getV((double) SpriteShiftEntry.getUnInterpolatedV(original, BakedQuadHelper.getV(vertexData, vertex))));
                }
                quads.add(newQuad);
            }
            return quads;
        }
    }
}