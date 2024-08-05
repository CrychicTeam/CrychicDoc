package com.simibubi.create.foundation.block.connected;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.model.BakedQuadHelper;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class CTModel extends BakedModelWrapperWithData {

    private static final ModelProperty<CTModel.CTData> CT_PROPERTY = new ModelProperty<>();

    private final ConnectedTextureBehaviour behaviour;

    public CTModel(BakedModel originalModel, ConnectedTextureBehaviour behaviour) {
        super(originalModel);
        this.behaviour = behaviour;
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        return builder.with(CT_PROPERTY, this.createCTData(world, pos, state));
    }

    protected CTModel.CTData createCTData(BlockAndTintGetter world, BlockPos pos, BlockState state) {
        CTModel.CTData data = new CTModel.CTData();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {
            BlockState actualState = world.m_8055_(pos);
            if (this.behaviour.buildContextForOccludedDirections() || Block.shouldRenderFace(state, world, pos, face, mutablePos.setWithOffset(pos, face)) || actualState.m_60734_() instanceof CopycatBlock ufb && !ufb.canFaceBeOccluded(actualState, face)) {
                CTType dataType = this.behaviour.getDataType(world, pos, state, face);
                if (dataType != null) {
                    ConnectedTextureBehaviour.CTContext context = this.behaviour.buildContext(world, pos, state, face, dataType.getContextRequirement());
                    data.put(face, dataType.getTextureIndex(context));
                }
            }
        }
        return data;
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        List<BakedQuad> quads = super.getQuads(state, side, rand, extraData, renderType);
        if (!extraData.has(CT_PROPERTY)) {
            return quads;
        } else {
            CTModel.CTData data = extraData.get(CT_PROPERTY);
            List<BakedQuad> var17 = new ArrayList(quads);
            for (int i = 0; i < var17.size(); i++) {
                BakedQuad quad = (BakedQuad) var17.get(i);
                int index = data.get(quad.getDirection());
                if (index != -1) {
                    CTSpriteShiftEntry spriteShift = this.behaviour.getShift(state, quad.getDirection(), quad.getSprite());
                    if (spriteShift != null && quad.getSprite() == spriteShift.getOriginal()) {
                        BakedQuad newQuad = BakedQuadHelper.clone(quad);
                        int[] vertexData = newQuad.getVertices();
                        for (int vertex = 0; vertex < 4; vertex++) {
                            float u = BakedQuadHelper.getU(vertexData, vertex);
                            float v = BakedQuadHelper.getV(vertexData, vertex);
                            BakedQuadHelper.setU(vertexData, vertex, spriteShift.getTargetU(u, index));
                            BakedQuadHelper.setV(vertexData, vertex, spriteShift.getTargetV(v, index));
                        }
                        var17.set(i, newQuad);
                    }
                }
            }
            return var17;
        }
    }

    private static class CTData {

        private final int[] indices = new int[6];

        public CTData() {
            Arrays.fill(this.indices, -1);
        }

        public void put(Direction face, int texture) {
            this.indices[face.get3DDataValue()] = texture;
        }

        public int get(Direction face) {
            return this.indices[face.get3DDataValue()];
        }
    }
}