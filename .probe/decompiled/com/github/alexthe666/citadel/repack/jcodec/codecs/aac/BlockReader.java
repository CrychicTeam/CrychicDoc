package com.github.alexthe666.citadel.repack.jcodec.codecs.aac;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.blocks.Block;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;

public class BlockReader {

    public Block nextBlock(BitReader bits) {
        BlockType type = BlockType.values()[(int) ((long) bits.readNBit(3))];
        if (type == BlockType.TYPE_END) {
            return null;
        } else {
            int id = bits.readNBit(4);
            return null;
        }
    }
}