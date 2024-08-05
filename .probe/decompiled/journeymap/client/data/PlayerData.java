package journeymap.client.data;

import com.google.common.cache.CacheLoader;
import journeymap.client.JourneymapClient;
import journeymap.client.log.JMLogger;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.EntityDTO;
import journeymap.common.helper.BiomeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

public class PlayerData extends CacheLoader<Class, EntityDTO> {

    public static boolean playerIsUnderground(Minecraft mc, Player player) {
        if (Level.NETHER.equals(player.m_9236_().dimension())) {
            return true;
        } else {
            int posX = Mth.floor(player.m_20185_());
            int posY = Mth.floor(player.m_20191_().minY);
            int posZ = Mth.floor(player.m_20189_());
            int offset = 1;
            boolean isUnderground = false;
            if (posY < player.m_9236_().m_141937_()) {
                return true;
            } else {
                for (int x = posX - 1; x <= posX + 1; x++) {
                    for (int z = posZ - 1; z <= posZ + 1; z++) {
                        int y = posY + 1;
                        ChunkMD chunkMD = DataCache.INSTANCE.getChunkMD(ChunkPos.asLong(x >> 4, z >> 4));
                        if (chunkMD != null) {
                            if (chunkMD.ceiling(x & 15, z & 15) <= y) {
                                isUnderground = false;
                                return isUnderground;
                            }
                            isUnderground = true;
                        }
                    }
                }
                return isUnderground;
            }
        }
    }

    public EntityDTO load(Class aClass) throws Exception {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        EntityDTO dto = DataCache.INSTANCE.getEntityDTO(player);
        dto.update(player, false);
        dto.biome = this.getPlayerBiome(player);
        dto.underground = playerIsUnderground(mc, player);
        return dto;
    }

    private String getPlayerBiome(Player player) {
        if (player != null) {
            try {
                Biome biome = (Biome) Minecraft.getInstance().level.m_204166_(player.m_20183_()).value();
                if (biome != null) {
                    return BiomeHelper.getTranslatedBiomeName(biome);
                }
            } catch (Exception var3) {
                JMLogger.throwLogOnce("Couldn't get player biome: " + var3.getMessage(), var3);
            }
        }
        return "?";
    }

    public long getTTL() {
        return (long) JourneymapClient.getInstance().getCoreProperties().cachePlayerData.get().intValue();
    }
}