package com.simibubi.create.content.decoration.girder;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.block.connected.CTModel;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;

public class ConnectedGirderModel extends CTModel {

    protected static final ModelProperty<ConnectedGirderModel.ConnectionData> CONNECTION_PROPERTY = new ModelProperty<>();

    public ConnectedGirderModel(BakedModel originalModel) {
        super(originalModel, new GirderCTBehaviour());
    }

    @Override
    protected ModelData.Builder gatherModelData(ModelData.Builder builder, BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData blockEntityData) {
        super.gatherModelData(builder, world, pos, state, blockEntityData);
        ConnectedGirderModel.ConnectionData connectionData = new ConnectedGirderModel.ConnectionData();
        for (Direction d : Iterate.horizontalDirections) {
            connectionData.setConnected(d, GirderBlock.isConnected(world, pos, state, d));
        }
        return builder.with(CONNECTION_PROPERTY, connectionData);
    }

    @Override
    public List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource rand, ModelData extraData, RenderType renderType) {
        List<BakedQuad> superQuads = super.getQuads(state, side, rand, extraData, renderType);
        if (side == null && extraData.has(CONNECTION_PROPERTY)) {
            List<BakedQuad> quads = new ArrayList(superQuads);
            ConnectedGirderModel.ConnectionData data = extraData.get(CONNECTION_PROPERTY);
            for (Direction d : Iterate.horizontalDirections) {
                if (data.isConnected(d)) {
                    quads.addAll(((PartialModel) AllPartialModels.METAL_GIRDER_BRACKETS.get(d)).get().getQuads(state, side, rand, extraData, renderType));
                }
            }
            return quads;
        } else {
            return superQuads;
        }
    }

    private static class ConnectionData {

        boolean[] connectedFaces = new boolean[4];

        public ConnectionData() {
            Arrays.fill(this.connectedFaces, false);
        }

        void setConnected(Direction face, boolean connected) {
            this.connectedFaces[face.get2DDataValue()] = connected;
        }

        boolean isConnected(Direction face) {
            return this.connectedFaces[face.get2DDataValue()];
        }
    }
}