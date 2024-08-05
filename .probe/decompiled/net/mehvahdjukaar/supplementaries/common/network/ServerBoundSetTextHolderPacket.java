package net.mehvahdjukaar.supplementaries.common.network;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.common.block.ITextHolderProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ServerBoundSetTextHolderPacket implements Message {

    private final BlockPos pos;

    public final String[][] textHolderLines;

    public ServerBoundSetTextHolderPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.textHolderLines = new String[buf.readVarInt()][];
        for (int i = 0; i < this.textHolderLines.length; i++) {
            String[] lines = new String[buf.readVarInt()];
            for (int j = 0; j < lines.length; j++) {
                lines[j] = buf.readUtf();
            }
            this.textHolderLines[i] = lines;
        }
    }

    public ServerBoundSetTextHolderPacket(BlockPos pos, String[][] holderLines) {
        this.pos = pos;
        this.textHolderLines = holderLines;
    }

    public ServerBoundSetTextHolderPacket(BlockPos pos, String[] lines) {
        this(pos, new String[][] { lines });
    }

    public ServerBoundSetTextHolderPacket(BlockPos pos, String line) {
        this(pos, new String[] { line });
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeVarInt(this.textHolderLines.length);
        for (String[] l : this.textHolderLines) {
            buf.writeVarInt(l.length);
            for (String v : l) {
                buf.writeUtf(v);
            }
        }
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        ServerPlayer sender = (ServerPlayer) context.getSender();
        CompletableFuture.supplyAsync(() -> Stream.of(this.textHolderLines).map(line -> Stream.of(line).map(ChatFormatting::m_126649_).toList()).map(innerList -> sender.connection.filterTextPacket(innerList)).map(CompletableFuture::join).toList()).thenAcceptAsync(l -> this.updateSignText(sender, l), sender.server);
    }

    private void updateSignText(ServerPlayer player, List<List<FilteredText>> filteredText) {
        player.resetLastActionTime();
        Level level = player.m_9236_();
        if (level.m_46805_(this.pos) && level.getBlockEntity(this.pos) instanceof ITextHolderProvider te && te.tryAcceptingClientText(this.pos, player, filteredText)) {
            BlockEntity be = (BlockEntity) te;
            be.setChanged();
            level.sendBlockUpdated(be.getBlockPos(), be.getBlockState(), be.getBlockState(), 3);
        }
    }
}