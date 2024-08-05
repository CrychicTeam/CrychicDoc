package com.github.alexthe666.iceandfire.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class BlockDreadWoodLog extends RotatedPillarBlock implements IDragonProof, IDreadBlock {

    public BlockDreadWoodLog() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).ignitedByLava().strength(2.0F, 10000.0F).sound(SoundType.WOOD));
    }
}