package noppes.npcs.packets.server;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketNbtBookBlockSave extends PacketServerBasic {

    private BlockPos pos;

    private CompoundTag data;

    public SPacketNbtBookBlockSave(BlockPos pos, CompoundTag data) {
        this.pos = pos;
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.nbt_book;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.TOOL_NBTBOOK;
    }

    public static void encode(SPacketNbtBookBlockSave msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeNbt(msg.data);
    }

    public static SPacketNbtBookBlockSave decode(FriendlyByteBuf buf) {
        return new SPacketNbtBookBlockSave(buf.readBlockPos(), buf.readNbt());
    }

    @Override
    protected void handle() {
        BlockEntity tile = this.player.m_9236_().getBlockEntity(this.pos);
        if (tile != null) {
            tile.load(this.data);
            tile.setChanged();
        }
    }
}