package me.jellysquid.mods.sodium.client.render.chunk;

import me.jellysquid.mods.sodium.client.gl.device.CommandList;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderListIterable;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.TerrainRenderPass;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexType;
import me.jellysquid.mods.sodium.client.render.viewport.CameraTransform;

public interface ChunkRenderer {

    void render(ChunkRenderMatrices var1, CommandList var2, ChunkRenderListIterable var3, TerrainRenderPass var4, CameraTransform var5);

    void delete(CommandList var1);

    ChunkVertexType getVertexType();
}