package dev.ftb.mods.ftbxmodcompat.ftbchunks.kubejs;

import dev.ftb.mods.ftbchunks.api.ClaimedChunk;
import dev.latvian.mods.kubejs.entity.EntityEventJS;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class AfterEventJS extends EntityEventJS {

    public final CommandSourceStack source;

    public final ClaimedChunk chunk;

    public AfterEventJS(CommandSourceStack s, ClaimedChunk c) {
        this.source = s;
        this.chunk = c;
    }

    @Override
    public Level getLevel() {
        return this.source.getLevel();
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return this.source.getEntity();
    }

    @Nullable
    public ServerPlayer getPlayer() {
        return this.source.getEntity() instanceof ServerPlayer sp ? sp : null;
    }

    public BlockPos getClaimPos() {
        return this.chunk.getPos().getChunkPos().getWorldPosition();
    }
}