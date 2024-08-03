package journeymap.client.io.nbt;

import com.mojang.datafixers.DataFixer;
import java.util.Optional;
import journeymap.client.model.ChunkMD;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import org.apache.logging.log4j.Logger;

public class JMChunkLoader {

    private static Logger logger = Journeymap.getLogger();

    public static ChunkMD getChunkMD(ChunkStorage loader, Minecraft mc, ChunkPos coord, boolean forceRetain) {
        if (RegionLoader.getRegionFile(mc, coord.x, coord.z).exists()) {
            IntegratedServer integratedServer = mc.getSingleplayerServer();
            ServerLevel serverWorld = integratedServer.m_129880_(mc.player.m_9236_().dimension());
            return getChunkFromRegion(serverWorld, coord, loader, forceRetain);
        } else {
            logger.warn("Region doesn't exist for chunk: " + coord);
            return null;
        }
    }

    private static ChunkMD getChunkFromRegion(ServerLevel world, ChunkPos coord, ChunkStorage loader, boolean forceRetain) {
        try {
            CompoundTag nbt = (CompoundTag) ((Optional) loader.read(coord).get()).get();
            if (nbt != null) {
                return getChunkFromNBT(world, coord, nbt, forceRetain);
            }
        } catch (Exception var5) {
        }
        return null;
    }

    public static ChunkMD getChunkFromNBT(ServerLevel world, ChunkPos coord, CompoundTag nbt, boolean forceRetain) {
        DataFixer fixer = Minecraft.getInstance().getFixerUpper();
        int dataVersion = ChunkStorage.getVersion(nbt);
        nbt = DataFixTypes.CHUNK.updateToCurrentVersion(fixer, nbt, dataVersion);
        CustomChunkReader.ProcessedChunk processedChunk = CustomChunkReader.read(world, world.getPoiManager(), coord, nbt);
        return processedChunk != null && processedChunk.chunk() != null ? new ChunkMD(processedChunk.chunk(), forceRetain, processedChunk.light()) : null;
    }

    public static ChunkMD getChunkMdFromMemory(Level level, int chunkX, int chunkZ) {
        if (level != null && level == Minecraft.getInstance().player.m_9236_()) {
            ChunkSource provider = level.m_7726_();
            if (provider != null) {
                LevelChunk theChunk = level.getChunk(chunkX, chunkZ);
                if (theChunk != null && !(theChunk instanceof EmptyLevelChunk) && theChunk.getLevel() == Minecraft.getInstance().player.m_9236_()) {
                    return new ChunkMD(theChunk);
                }
            }
        }
        return null;
    }
}