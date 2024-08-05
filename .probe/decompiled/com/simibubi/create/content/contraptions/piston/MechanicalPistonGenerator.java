package com.simibubi.create.content.contraptions.piston;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraftforge.client.model.generators.ModelFile;

public class MechanicalPistonGenerator extends SpecialBlockStateGen {

    private final PistonType type;

    public MechanicalPistonGenerator(PistonType type) {
        this.type = type;
    }

    @Override
    protected int getXRotation(BlockState state) {
        Direction facing = (Direction) state.m_61143_(MechanicalPistonBlock.FACING);
        return facing.getAxis().isVertical() ? (facing == Direction.DOWN ? 180 : 0) : 90;
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction facing = (Direction) state.m_61143_(MechanicalPistonBlock.FACING);
        return facing.getAxis().isVertical() ? 0 : this.horizontalAngle(facing) + 180;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        Direction facing = (Direction) state.m_61143_(PistonBaseBlock.f_52588_);
        boolean axisAlongFirst = (Boolean) state.m_61143_(MechanicalPistonBlock.AXIS_ALONG_FIRST_COORDINATE);
        MechanicalPistonBlock.PistonState pistonState = (MechanicalPistonBlock.PistonState) state.m_61143_(MechanicalPistonBlock.STATE);
        String path = "block/mechanical_piston";
        String folder = pistonState == MechanicalPistonBlock.PistonState.RETRACTED ? this.type.getSerializedName() : pistonState.getSerializedName();
        String partial = facing.getAxis() == Direction.Axis.X ^ axisAlongFirst ? "block_rotated" : "block";
        return prov.models().getExistingFile(prov.modLoc(path + "/" + folder + "/" + partial));
    }
}