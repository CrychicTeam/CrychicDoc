package com.simibubi.create.content.kinetics.saw;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class SawGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return state.m_61143_(SawBlock.FACING) == Direction.DOWN ? 180 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction facing = (Direction) state.m_61143_(SawBlock.FACING);
        boolean axisAlongFirst = (Boolean) state.m_61143_(SawBlock.AXIS_ALONG_FIRST_COORDINATE);
        return facing.getAxis().isVertical() ? (axisAlongFirst ? 270 : 0) + (state.m_61143_(SawBlock.FLIPPED) ? 180 : 0) : this.horizontalAngle(facing);
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String path = "block/" + ctx.getName() + "/";
        String orientation = ((Direction) state.m_61143_(SawBlock.FACING)).getAxis().isVertical() ? "vertical" : "horizontal";
        return prov.models().getExistingFile(prov.modLoc(path + orientation));
    }
}