package me.jellysquid.mods.sodium.client.render.chunk.compile.tasks;

import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;

public abstract class ChunkBuilderTask<OUTPUT> {

    public abstract OUTPUT execute(ChunkBuildContext var1, CancellationToken var2);
}