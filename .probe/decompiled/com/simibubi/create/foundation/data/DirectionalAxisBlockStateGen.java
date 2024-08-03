package com.simibubi.create.foundation.data;

import com.simibubi.create.content.kinetics.gauge.GaugeBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public abstract class DirectionalAxisBlockStateGen extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        Direction direction = (Direction) state.m_61143_(GaugeBlock.FACING);
        boolean alongFirst = (Boolean) state.m_61143_(GaugeBlock.AXIS_ALONG_FIRST_COORDINATE);
        if (direction == Direction.DOWN) {
            return 180;
        } else if (direction == Direction.UP) {
            return 0;
        } else {
            return direction.getAxis() == Direction.Axis.X == alongFirst ? 90 : 0;
        }
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction direction = (Direction) state.m_61143_(GaugeBlock.FACING);
        boolean alongFirst = (Boolean) state.m_61143_(GaugeBlock.AXIS_ALONG_FIRST_COORDINATE);
        if (direction.getAxis().isVertical()) {
            return alongFirst ? 90 : 0;
        } else {
            return this.horizontalAngle(direction) + 90;
        }
    }

    public abstract <T extends Block> String getModelPrefix(DataGenContext<Block, T> var1, RegistrateBlockstateProvider var2, BlockState var3);

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        boolean vertical = ((Direction) state.m_61143_(GaugeBlock.FACING)).getAxis().isVertical();
        String partial = vertical ? "" : "_wall";
        return prov.models().getExistingFile(prov.modLoc(this.getModelPrefix(ctx, prov, state) + partial));
    }
}