package com.simibubi.create.content.fluids.pipes;

import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.client.model.generators.ModelFile;

public class SmartFluidPipeGenerator extends SpecialBlockStateGen {

    @Override
    protected int getXRotation(BlockState state) {
        AttachFace attachFace = (AttachFace) state.m_61143_(SmartFluidPipeBlock.f_53179_);
        return attachFace == AttachFace.CEILING ? 180 : (attachFace == AttachFace.FLOOR ? 0 : 270);
    }

    @Override
    protected int getYRotation(BlockState state) {
        AttachFace attachFace = (AttachFace) state.m_61143_(SmartFluidPipeBlock.f_53179_);
        int angle = this.horizontalAngle((Direction) state.m_61143_(SmartFluidPipeBlock.f_54117_));
        return angle + (attachFace == AttachFace.CEILING ? 180 : 0);
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        return AssetLookup.partialBaseModel(ctx, prov);
    }
}