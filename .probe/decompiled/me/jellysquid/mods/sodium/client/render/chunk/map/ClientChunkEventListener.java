package me.jellysquid.mods.sodium.client.render.chunk.map;

public interface ClientChunkEventListener {

    void updateMapCenter(int var1, int var2);

    void updateLoadDistance(int var1);

    void onChunkStatusAdded(int var1, int var2, int var3);

    void onChunkStatusRemoved(int var1, int var2, int var3);
}