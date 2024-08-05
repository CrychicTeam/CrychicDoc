package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.blocks.tiles.TileBuilder;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketSchematicsTileSave extends PacketServerBasic {

    private BlockPos pos;

    private CompoundTag data;

    public SPacketSchematicsTileSave(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand || item.getItem() == CustomBlocks.builder_item || item.getItem() == CustomBlocks.copy_item;
    }

    public static void encode(SPacketSchematicsTileSave msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeNbt(msg.data);
    }

    public static SPacketSchematicsTileSave decode(FriendlyByteBuf buf) {
        return new SPacketSchematicsTileSave(buf.readBlockPos(), buf.readNbt());
    }

    @Override
    protected void handle() {
        TileBuilder tile = (TileBuilder) this.player.m_9236_().getBlockEntity(this.pos);
        if (tile != null) {
            tile.readPartNBT(this.data);
        }
    }
}