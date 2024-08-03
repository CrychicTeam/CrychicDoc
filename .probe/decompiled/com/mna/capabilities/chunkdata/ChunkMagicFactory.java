package com.mna.capabilities.chunkdata;

import com.mna.api.capabilities.IChunkMagic;
import java.util.concurrent.Callable;

public class ChunkMagicFactory implements Callable<IChunkMagic> {

    public IChunkMagic call() throws Exception {
        return new ChunkMagic();
    }
}