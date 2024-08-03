package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.model.BakedModelWrapperWithData;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public abstract class CopycatModel extends BakedModelWrapperWithData {

    public static final ModelProperty<BlockState> MATERIAL_PROPERTY = new ModelProperty<>();

    private static final ModelProperty<CopycatModel.OcclusionData> OCCLUSION_PROPERTY = new ModelProperty<>();

    private static final ModelProperty<ModelData> WRAPPED_DATA_PROPERTY = new ModelProperty<>();

    public CopycatModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        BlockState material = getMaterial(blockEntityData);
        if (material == null) {
            return builder;
        } else {
            builder.with(MATERIAL_PROPERTY, material);
            if (state.m_60734_() instanceof CopycatBlock copycatBlock) {
                CopycatModel.OcclusionData var10 = new CopycatModel.OcclusionData();
                this.gatherOcclusionData(world, pos, state, material, var10, copycatBlock);
                builder.with(OCCLUSION_PROPERTY, var10);
                ModelData wrappedData = getModelOf(material).getModelData(new FilteredBlockAndTintGetter(world, targetPos -> copycatBlock.canConnectTexturesToward(world, pos, targetPos, state)), pos, material, ModelData.EMPTY);
                return builder.with(WRAPPED_DATA_PROPERTY, wrappedData);
            } else {
                return builder;
            }
        }
    }

    private void gatherOcclusionData(BlockAndTintGetter world, BlockPos pos, BlockState state, BlockState material, CopycatModel.OcclusionData occlusionData, CopycatBlock copycatBlock) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction face : Iterate.directions) {
            BlockPos.MutableBlockPos neighbourPos = mutablePos.setWithOffset(pos, face);
            BlockState neighbourState = world.m_8055_(neighbourPos);
            if (state.supportsExternalFaceHiding() && neighbourState.hidesNeighborFace(world, neighbourPos, state, face.getOpposite())) {
                occlusionData.occlude(face);
            } else if (copycatBlock.canFaceBeOccluded(state, face) && !Block.shouldRenderFace(material, world, pos, face, neighbourPos)) {
                occlusionData.occlude(face);
            }
        }
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        if (side != null && state.m_60734_() instanceof CopycatBlock ccb && ccb.shouldFaceAlwaysRender(state, side)) {
            return Collections.emptyList();
        } else {
            BlockState material = getMaterial(data);
            if (material == null) {
                return super.getQuads(state, side, rand, data, renderType);
            } else {
                CopycatModel.OcclusionData occlusionData = data.get(OCCLUSION_PROPERTY);
                if (occlusionData != null && occlusionData.isOccluded(side)) {
                    return super.getQuads(state, side, rand, data, renderType);
                } else {
                    ModelData wrappedData = data.get(WRAPPED_DATA_PROPERTY);
                    if (wrappedData == null) {
                        wrappedData = ModelData.EMPTY;
                    }
                    if (renderType != null && !Minecraft.getInstance().getBlockRenderer().getBlockModel(material).getRenderTypes(material, rand, wrappedData).contains(renderType)) {
                        return super.getQuads(state, side, rand, data, renderType);
                    } else {
                        List<BakedQuad> croppedQuads = this.getCroppedQuads(state, side, rand, material, wrappedData, renderType);
                        if (side == null && state.m_60734_() instanceof CopycatBlock ccb) {
                            boolean immutable = true;
                            for (Direction nonOcclusionSide : Iterate.directions) {
                                if (ccb.shouldFaceAlwaysRender(state, nonOcclusionSide)) {
                                    if (immutable) {
                                        croppedQuads = new ArrayList(croppedQuads);
                                        immutable = false;
                                    }
                                    croppedQuads.addAll(this.getCroppedQuads(state, nonOcclusionSide, rand, material, wrappedData, renderType));
                                }
                            }
                        }
                        return croppedQuads;
                    }
                }
            }
        }
    }

    protected abstract List<BakedQuad> getCroppedQuads(BlockState var1, Direction var2, RandomSource var3, BlockState var4, ModelData var5, RenderType var6);

    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        BlockState material = getMaterial(data);
        if (material == null) {
            return super.getParticleIcon(data);
        } else {
            ModelData wrappedData = data.get(WRAPPED_DATA_PROPERTY);
            if (wrappedData == null) {
                wrappedData = ModelData.EMPTY;
            }
            return getModelOf(material).getParticleIcon(wrappedData);
        }
    }

    @Nullable
    public static BlockState getMaterial(ModelData data) {
        BlockState material = data == null ? null : data.get(MATERIAL_PROPERTY);
        return material == null ? AllBlocks.COPYCAT_BASE.getDefaultState() : material;
    }

    public static BakedModel getModelOf(BlockState state) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModel(state);
    }

    private static class OcclusionData {

        private final boolean[] occluded = new boolean[6];

        public OcclusionData() {
        }

        public void occlude(Direction face) {
            this.occluded[face.get3DDataValue()] = true;
        }

        public boolean isOccluded(Direction face) {
            return face == null ? false : this.occluded[face.get3DDataValue()];
        }
    }
}