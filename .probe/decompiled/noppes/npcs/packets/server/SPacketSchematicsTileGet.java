package noppes.npcs.packets.server;

import java.util.Vector;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;
import noppes.npcs.packets.client.PacketGuiScrollList;

public class SPacketSchematicsTileGet extends PacketServerBasic {

    private BlockPos pos;

    public SPacketSchematicsTileGet(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand || item.getItem() == CustomBlocks.builder_item || item.getItem() == CustomBlocks.copy_item;
    }

    public static void encode(SPacketSchematicsTileGet msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
    }

    public static SPacketSchematicsTileGet decode(FriendlyByteBuf buf) {
        return new SPacketSchematicsTileGet(buf.readBlockPos());
    }

    @Override
    protected void handle() {
        TileBuilder tile = (TileBuilder) this.player.m_9236_().getBlockEntity(this.pos);
        if (tile != null) {
            Packets.send(this.player, new PacketGuiData(tile.writePartNBT(new CompoundTag())));
            Packets.send(this.player, new PacketGuiScrollList(new Vector(SchematicController.Instance.list())));
            if (tile.hasSchematic()) {
                Packets.send(this.player, new PacketGuiData(tile.getSchematic().getNBTSmall()));
            }
        }
    }
}