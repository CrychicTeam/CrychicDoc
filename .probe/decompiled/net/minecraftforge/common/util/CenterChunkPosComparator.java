package net.minecraftforge.common.util;

import java.util.Comparator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public class CenterChunkPosComparator implements Comparator<ChunkPos> {

    private int x;

    private int z;

    public CenterChunkPosComparator(ServerPlayer entityplayer) {
        this.x = (int) entityplayer.m_20185_() >> 4;
        this.z = (int) entityplayer.m_20189_() >> 4;
    }

    public int compare(ChunkPos a, ChunkPos b) {
        if (a.equals(b)) {
            return 0;
        } else {
            int ax = a.x - this.x;
            int az = a.z - this.z;
            int bx = b.x - this.x;
            int bz = b.z - this.z;
            int result = (ax - bx) * (ax + bx) + (az - bz) * (az + bz);
            if (result != 0) {
                return result;
            } else if (ax < 0) {
                return bx < 0 ? bz - az : -1;
            } else {
                return bx < 0 ? 1 : az - bz;
            }
        }
    }
}