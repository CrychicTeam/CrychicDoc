package com.simibubi.create.content.kinetics.transmission.sequencer;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class SequencedGearshiftGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        return state.m_61143_(SequencedGearshiftBlock.VERTICAL) ? 90 : 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return state.m_61143_(SequencedGearshiftBlock.HORIZONTAL_AXIS) == Direction.Axis.X ? 90 : 0;
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String variant = "idle";
        int seq = (Integer) state.m_61143_(SequencedGearshiftBlock.STATE);
        if (seq > 0) {
            variant = "seq_" + seq;
        }
        return prov.models().getExistingFile(prov.modLoc("block/" + ctx.getName() + "/" + variant));
    }
}