package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.controllers.SchematicController;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketSchematicsTileSet extends PacketServerBasic {

    private BlockPos pos;

    private String name;

    public SPacketSchematicsTileSet(BlockPos pos, String name) {
        this.pos = pos;
        this.name = name;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand || item.getItem() == CustomBlocks.builder_item || item.getItem() == CustomBlocks.copy_item;
    }

    public static void encode(SPacketSchematicsTileSet msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeUtf(msg.name);
    }

    public static SPacketSchematicsTileSet decode(FriendlyByteBuf buf) {
        return new SPacketSchematicsTileSet(buf.readBlockPos(), buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        TileBuilder tile = (TileBuilder) this.player.m_9236_().getBlockEntity(this.pos);
        tile.setSchematic(SchematicController.Instance.load(this.name));
        if (tile.hasSchematic()) {
            Packets.send(this.player, new PacketGuiData(tile.getSchematic().getNBTSmall()));
        }
    }
}