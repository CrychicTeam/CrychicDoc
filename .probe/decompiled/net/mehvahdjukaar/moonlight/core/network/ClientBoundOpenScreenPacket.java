package net.mehvahdjukaar.moonlight.core.network;

import net.mehvahdjukaar.moonlight.api.client.IScreenProvider;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientBoundOpenScreenPacket implements Message {

    public final BlockPos pos;

    private final Direction dir;

    public ClientBoundOpenScreenPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.dir = Direction.from3DDataValue(buffer.readVarInt());
    }

    public ClientBoundOpenScreenPacket(BlockPos pos, Direction hitFace) {
        this.pos = pos;
        this.dir = hitFace;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeVarInt(this.dir.get3DDataValue());
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        handleOpenScreenPacket(this);
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleOpenScreenPacket(ClientBoundOpenScreenPacket message) {
        ClientLevel level = Minecraft.getInstance().level;
        LocalPlayer p = Minecraft.getInstance().player;
        if (level != null && p != null) {
            BlockPos pos = message.pos;
            if (level.m_7702_(pos) instanceof IScreenProvider tile) {
                tile.openScreen(level, pos, p, message.dir);
            }
        }
    }
}