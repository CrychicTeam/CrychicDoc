package com.simibubi.create.content.kinetics.chainDrive;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import java.util.function.BiFunction;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class ChainDriveGenerator extends SpecialBlockStateGen {

    private BiFunction<BlockState, String, ModelFile> modelFunc;

    public ChainDriveGenerator(BiFunction<BlockState, String, ModelFile> modelFunc) {
        this.modelFunc = modelFunc;
    }

    @Override
    protected int getXRotation(BlockState state) {
        ChainDriveBlock.Part part = (ChainDriveBlock.Part) state.m_61143_(ChainDriveBlock.PART);
        boolean connectedAlongFirst = (Boolean) state.m_61143_(ChainDriveBlock.CONNECTED_ALONG_FIRST_COORDINATE);
        Direction.Axis axis = (Direction.Axis) state.m_61143_(ChainDriveBlock.AXIS);
        if (part == ChainDriveBlock.Part.NONE) {
            return axis == Direction.Axis.Y ? 90 : 0;
        } else if (axis == Direction.Axis.X) {
            return (connectedAlongFirst ? 90 : 0) + (part == ChainDriveBlock.Part.START ? 180 : 0);
        } else if (axis == Direction.Axis.Z) {
            return connectedAlongFirst ? 0 : (part == ChainDriveBlock.Part.START ? 270 : 90);
        } else {
            return 0;
        }
    }

    @Override
    protected int getYRotation(BlockState state) {
        ChainDriveBlock.Part part = (ChainDriveBlock.Part) state.m_61143_(ChainDriveBlock.PART);
        boolean connectedAlongFirst = (Boolean) state.m_61143_(ChainDriveBlock.CONNECTED_ALONG_FIRST_COORDINATE);
        Direction.Axis axis = (Direction.Axis) state.m_61143_(ChainDriveBlock.AXIS);
        if (part == ChainDriveBlock.Part.NONE) {
            return axis == Direction.Axis.X ? 90 : 0;
        } else if (axis == Direction.Axis.Z) {
            return connectedAlongFirst && part == ChainDriveBlock.Part.END ? 270 : 90;
        } else {
            boolean flip = part == ChainDriveBlock.Part.END && !connectedAlongFirst || part == ChainDriveBlock.Part.START && connectedAlongFirst;
            return axis == Direction.Axis.Y ? (connectedAlongFirst ? 90 : 0) + (flip ? 180 : 0) : 0;
        }
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return (ModelFile) this.modelFunc.apply(state, this.getModelSuffix(state));
    }

    protected String getModelSuffix(BlockState state) {
        ChainDriveBlock.Part part = (ChainDriveBlock.Part) state.m_61143_(ChainDriveBlock.PART);
        Direction.Axis axis = (Direction.Axis) state.m_61143_(ChainDriveBlock.AXIS);
        if (part == ChainDriveBlock.Part.NONE) {
            return "single";
        } else {
            String orientation = axis == Direction.Axis.Y ? "vertical" : "horizontal";
            String section = part == ChainDriveBlock.Part.MIDDLE ? "middle" : "end";
            return section + "_" + orientation;
        }
    }
}