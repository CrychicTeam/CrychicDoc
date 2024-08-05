package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import noppes.npcs.CustomBlocks;
import noppes.npcs.CustomItems;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketTileEntitySave extends PacketServerBasic {

    private CompoundTag data;

    public SPacketTileEntitySave(CompoundTag data) {
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.wand || item.getItem() == CustomBlocks.border_item || item.getItem() == CustomBlocks.copy_item || item.getItem() == CustomBlocks.redstone_item || item.getItem() == CustomBlocks.scripted_item || item.getItem() == CustomBlocks.waypoint_item;
    }

    public static void encode(SPacketTileEntitySave msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static SPacketTileEntitySave decode(FriendlyByteBuf buf) {
        return new SPacketTileEntitySave(buf.readNbt());
    }

    @Override
    protected void handle() {
        saveTileEntity(this.player, this.data);
    }

    public static BlockEntity saveTileEntity(ServerPlayer player, CompoundTag compound) {
        int x = compound.getInt("x");
        int y = compound.getInt("y");
        int z = compound.getInt("z");
        BlockEntity tile = player.m_9236_().getBlockEntity(new BlockPos(x, y, z));
        if (tile != null) {
            tile.load(compound);
        }
        return tile;
    }
}