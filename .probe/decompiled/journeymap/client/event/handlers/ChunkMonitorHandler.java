package journeymap.client.event.handlers;

import journeymap.client.data.DataCache;
import journeymap.client.model.ChunkMD;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public class ChunkMonitorHandler {

    private LevelAccessor world;

    private static ChunkMonitorHandler instance;

    private ChunkMonitorHandler() {
    }

    public static ChunkMonitorHandler getInstance() {
        if (instance == null) {
            instance = new ChunkMonitorHandler();
        }
        return instance;
    }

    public void reset() {
        this.world = null;
    }

    public void resetRenderTimes(long pos) {
        ChunkMD chunkMD = DataCache.INSTANCE.getChunkMD(pos);
        if (chunkMD != null && Minecraft.getInstance().player != null && chunkMD.getChunk().getLevel() == Minecraft.getInstance().player.m_9236_()) {
            chunkMD.resetRenderTimes();
        }
    }

    public void onChunkLoad(LevelAccessor world, ChunkAccess chunkAccess) {
        if (world != null && world.m_5776_() && this.isLevelCurrent()) {
            if (this.world == null) {
                this.world = world;
            }
            LevelChunk chunk = (LevelChunk) chunkAccess;
            if (chunk != null && Minecraft.getInstance().player != null && chunk.getLevel() == Minecraft.getInstance().player.m_9236_()) {
                DataCache.INSTANCE.addChunkMD(new ChunkMD(chunk));
            }
        }
    }

    public void onWorldUnload(LevelAccessor world) {
        try {
            if (world.equals(this.world)) {
                this.reset();
            }
        } catch (Exception var3) {
            Journeymap.getLogger().error("Error handling WorldEvent.Unload", var3);
        }
    }

    public void onBlockUpdate(LevelAccessor world, BlockPos pos) {
        if (world != null && this.isLevelCurrent()) {
            int chunkX = pos.m_123341_() >> 4;
            int chunkZ = pos.m_123343_() >> 4;
            this.markBlockRangeForRenderUpdate(chunkX * 16, chunkZ * 16, chunkX * 16 + 15, chunkZ * 16 + 15);
        }
    }

    public void onChunkUpdate(LevelAccessor world, ChunkPos pos) {
        if (world != null && this.isLevelCurrent()) {
            this.resetRenderTimes(pos.toLong());
        }
    }

    private void markBlockRangeForRenderUpdate(int x1, int z1, int x2, int z2) {
        int cx1 = x1 >> 4;
        int cz1 = z1 >> 4;
        int cx2 = x2 >> 4;
        int cz2 = z2 >> 4;
        if (cx1 == cx2 && cz1 == cz2) {
            this.resetRenderTimes(ChunkPos.asLong(cx1, cz1));
        } else {
            for (int chunkXPos = cx1; chunkXPos < cx2; chunkXPos++) {
                for (int chunkZPos = cz1; chunkZPos < cz2; chunkZPos++) {
                    this.resetRenderTimes(ChunkPos.asLong(chunkXPos, chunkZPos));
                }
            }
        }
    }

    private boolean isLevelCurrent() {
        Minecraft mc = Minecraft.getInstance();
        return mc.level != null && mc.player != null && mc.level == mc.player.m_9236_();
    }
}