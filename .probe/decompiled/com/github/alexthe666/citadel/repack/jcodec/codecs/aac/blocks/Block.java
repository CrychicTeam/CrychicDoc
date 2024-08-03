package com.github.alexthe666.citadel.repack.jcodec.codecs.aac.blocks;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.BlockType;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;

public abstract class Block {

    private BlockType type;

    public BlockType getType() {
        return this.type;
    }

    public abstract void parse(BitReader var1);
}