package com.simibubi.create.content.redstone.thresholdSwitch;

import com.simibubi.create.Create;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.simibubi.create.foundation.utility.Lang;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.client.model.generators.ModelFile;

public class ThresholdSwitchGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return this.horizontalAngle((Direction) state.m_61143_(ThresholdSwitchBlock.f_54117_)) + 180;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        int level = (Integer) state.m_61143_(ThresholdSwitchBlock.LEVEL);
        String path = "threshold_switch/block_" + Lang.asId(((AttachFace) state.m_61143_(ThresholdSwitchBlock.TARGET)).name());
        return prov.models().withExistingParent(path + "_" + level, Create.asResource("block/" + path)).texture("level", Create.asResource("block/threshold_switch/level_" + level));
    }
}