package info.journeymap.shaded.ar.com.hjg.pngj;

import info.journeymap.shaded.ar.com.hjg.pngj.chunks.ChunkRaw;
import info.journeymap.shaded.ar.com.hjg.pngj.chunks.PngChunk;

public interface IChunkFactory {

    PngChunk createChunk(ChunkRaw var1, ImageInfo var2);
}