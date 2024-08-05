package com.simibubi.create.content.redstone.link;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class RedstoneLinkGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        Direction facing = (Direction) state.m_61143_(RedstoneLinkBlock.f_52588_);
        return facing == Direction.UP ? 0 : (facing == Direction.DOWN ? 180 : 270);
    }

    @Override
    protected int getYRotation(BlockState state) {
        Direction facing = (Direction) state.m_61143_(RedstoneLinkBlock.f_52588_);
        return facing.getAxis().isVertical() ? 180 : this.horizontalAngle(facing);
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String variant = state.m_61143_(RedstoneLinkBlock.RECEIVER) ? "receiver" : "transmitter";
        if (((Direction) state.m_61143_(RedstoneLinkBlock.f_52588_)).getAxis().isHorizontal()) {
            variant = variant + "_vertical";
        }
        if ((Boolean) state.m_61143_(RedstoneLinkBlock.POWERED)) {
            variant = variant + "_powered";
        }
        return prov.models().getExistingFile(prov.modLoc("block/redstone_link/" + variant));
    }
}