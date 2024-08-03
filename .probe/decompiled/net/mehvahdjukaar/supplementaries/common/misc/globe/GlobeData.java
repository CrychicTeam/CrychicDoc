package net.mehvahdjukaar.supplementaries.common.misc.globe;

import net.mehvahdjukaar.supplementaries.client.GlobeManager;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSyncGlobeDataPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class GlobeData extends SavedData {

    private static final int TEXTURE_H = 16;

    private static final int TEXTURE_W = 32;

    public static final String DATA_NAME = "globe_data";

    public final byte[][] globePixels;

    public final long seed;

    private static GlobeData CLIENT_SIDE_INSTANCE = null;

    public GlobeData(long seed) {
        this.seed = seed;
        this.globePixels = GlobeTextureGenerator.generate(this.seed);
    }

    public GlobeData(CompoundTag tag) {
        this.globePixels = new byte[32][16];
        for (int i = 0; i < 32; i++) {
            this.globePixels[i] = tag.getByteArray("colors_" + i);
        }
        this.seed = tag.getLong("seed");
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        for (int i = 0; i < this.globePixels.length; i++) {
            nbt.putByteArray("colors_" + i, this.globePixels[i]);
        }
        nbt.putLong("seed", this.seed);
        return nbt;
    }

    public void sendToClient(Level world) {
        this.m_77762_();
        if (!world.isClientSide) {
            ModNetwork.CHANNEL.sendToAllClientPlayers(new ClientBoundSyncGlobeDataPacket(this));
        }
    }

    @Nullable
    public static GlobeData get(Level world) {
        return world instanceof ServerLevel server ? world.getServer().overworld().getDataStorage().computeIfAbsent(GlobeData::new, () -> new GlobeData(server.getSeed()), "globe_data") : CLIENT_SIDE_INSTANCE;
    }

    public static void set(ServerLevel level, GlobeData pData) {
        level.getServer().overworld().getDataStorage().set("globe_data", pData);
    }

    public static void setClientData(GlobeData data) {
        CLIENT_SIDE_INSTANCE = data;
        GlobeManager.refreshTextures();
    }

    public static void sendDataToClient(ServerPlayer player) {
        GlobeData data = get(player.m_9236_());
        if (data != null) {
            ModNetwork.CHANNEL.sendToClientPlayer(player, new ClientBoundSyncGlobeDataPacket(data));
        }
    }
}