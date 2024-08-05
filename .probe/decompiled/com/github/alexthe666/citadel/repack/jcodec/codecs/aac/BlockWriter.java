package com.github.alexthe666.citadel.repack.jcodec.codecs.aac;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.blocks.Block;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;

public class BlockWriter {

    public void nextBlock(BitWriter bits, Block block) {
        bits.writeNBit(block.getType().ordinal(), 3);
        if (block.getType() != BlockType.TYPE_END) {
            ;
        }
    }
}