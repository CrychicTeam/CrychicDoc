package icyllis.modernui.mc.forge;

import io.netty.buffer.Unpooled;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.server.ServerLifecycleHooks;

public final class PacketBuffer extends FriendlyByteBuf {

    private final ResourceLocation mName;

    PacketBuffer(ResourceLocation name) {
        super(Unpooled.buffer());
        this.mName = name;
    }

    @OnlyIn(Dist.CLIENT)
    public void sendToServer() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            connection.send(new ServerboundCustomPayloadPacket(this.mName, this));
        } else {
            this.release();
        }
    }

    public void sendToPlayer(@Nonnull Player player) {
        ((ServerPlayer) player).connection.send(new ClientboundCustomPayloadPacket(this.mName, this));
    }

    public void sendToPlayer(@Nonnull ServerPlayer player) {
        player.connection.send(new ClientboundCustomPayloadPacket(this.mName, this));
    }

    public void sendToPlayers(@Nonnull Iterable<? extends Player> players) {
        Packet<?> packet = new ClientboundCustomPayloadPacket(this.mName, this);
        for (Player player : players) {
            ((ServerPlayer) player).connection.send(packet);
        }
    }

    public void sendToAll() {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundCustomPayloadPacket(this.mName, this));
    }

    public void sendToDimension(@Nonnull ResourceKey<Level> dimension) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcastAll(new ClientboundCustomPayloadPacket(this.mName, this), dimension);
    }

    public void sendToNear(@Nullable Player excluded, double x, double y, double z, double radius, @Nonnull ResourceKey<Level> dimension) {
        ServerLifecycleHooks.getCurrentServer().getPlayerList().broadcast(excluded, x, y, z, radius, dimension, new ClientboundCustomPayloadPacket(this.mName, this));
    }

    public void sendToTrackingEntity(@Nonnull Entity entity) {
        ((ServerLevel) entity.level()).getChunkSource().broadcast(entity, new ClientboundCustomPayloadPacket(this.mName, this));
    }

    public void sendToTrackingAndSelf(@Nonnull Entity entity) {
        ((ServerLevel) entity.level()).getChunkSource().broadcastAndSend(entity, new ClientboundCustomPayloadPacket(this.mName, this));
    }

    public void sendToTrackingChunk(@Nonnull Level level, @Nonnull BlockPos pos) {
        Packet<?> packet = new ClientboundCustomPayloadPacket(this.mName, this);
        ((ServerLevel) level).getChunkSource().chunkMap.getPlayers(level.m_46865_(pos).getPos(), false).forEach(p -> p.connection.send(packet));
    }

    public void sendToTrackingChunk(@Nonnull LevelChunk chunk) {
        Packet<?> packet = new ClientboundCustomPayloadPacket(this.mName, this);
        ((ServerLevel) chunk.getLevel()).getChunkSource().chunkMap.getPlayers(chunk.m_7697_(), false).forEach(p -> p.connection.send(packet));
    }
}