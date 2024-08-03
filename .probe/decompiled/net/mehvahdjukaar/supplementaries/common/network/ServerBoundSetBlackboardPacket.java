package net.mehvahdjukaar.supplementaries.common.network;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.common.block.tiles.BlackboardBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ServerBoundSetBlackboardPacket implements Message {

    private final BlockPos pos;

    private final byte[][] pixels;

    public ServerBoundSetBlackboardPacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.pixels = new byte[16][16];
        for (int i = 0; i < this.pixels.length; i++) {
            this.pixels[i] = buf.readByteArray();
        }
    }

    public ServerBoundSetBlackboardPacket(BlockPos pos, byte[][] pixels) {
        this.pos = pos;
        this.pixels = pixels;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        for (byte[] pixel : this.pixels) {
            buf.writeByteArray(pixel);
        }
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        Level level = ((Player) Objects.requireNonNull(context.getSender())).m_9236_();
        BlockPos pos = this.pos;
        if (level.m_46805_(pos) && level.getBlockEntity(pos) instanceof BlackboardBlockTile board) {
            level.playSound(null, this.pos, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0F, 0.8F);
            board.setPixels(this.pixels);
            board.setChanged();
        }
    }
}