package com.simibubi.create.content.fluids.tank;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class FluidTankGenerator extends SpecialBlockStateGen {

    private String prefix;

    public FluidTankGenerator() {
        this("");
    }

    public FluidTankGenerator(String prefix) {
        this.prefix = prefix;
    }

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        Boolean top = (Boolean) state.m_61143_(FluidTankBlock.TOP);
        Boolean bottom = (Boolean) state.m_61143_(FluidTankBlock.BOTTOM);
        FluidTankBlock.Shape shape = (FluidTankBlock.Shape) state.m_61143_(FluidTankBlock.SHAPE);
        String shapeName = "middle";
        if (top && bottom) {
            shapeName = "single";
        } else if (top) {
            shapeName = "top";
        } else if (bottom) {
            shapeName = "bottom";
        }
        String modelName = shapeName + (shape == FluidTankBlock.Shape.PLAIN ? "" : "_" + shape.getSerializedName());
        return (ModelFile) (!this.prefix.isEmpty() ? prov.models().withExistingParent(this.prefix + modelName, prov.modLoc("block/fluid_tank/block_" + modelName)).texture("0", prov.modLoc("block/" + this.prefix + "casing")).texture("1", prov.modLoc("block/" + this.prefix + "fluid_tank")).texture("3", prov.modLoc("block/" + this.prefix + "fluid_tank_window")).texture("4", prov.modLoc("block/" + this.prefix + "casing")).texture("5", prov.modLoc("block/" + this.prefix + "fluid_tank_window_single")).texture("particle", prov.modLoc("block/" + this.prefix + "fluid_tank")) : AssetLookup.partialBaseModel(ctx, prov, modelName));
    }
}