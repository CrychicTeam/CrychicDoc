package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketTileEntityGet extends PacketServerBasic {

    private BlockPos pos;

    public SPacketTileEntityGet(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketTileEntityGet msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static SPacketTileEntityGet decode(FriendlyByteBuf buf) {
        return new SPacketTileEntityGet(buf.readBlockPos());
    }

    @Override
    protected void handle() {
        BlockEntity tile = this.player.m_9236_().getBlockEntity(this.pos);
        Packets.send(this.player, new PacketGuiData(tile.saveWithFullMetadata()));
    }
}