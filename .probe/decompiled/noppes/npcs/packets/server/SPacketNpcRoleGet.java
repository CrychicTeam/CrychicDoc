package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketNpcRoleGet extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketNpcRoleGet msg, FriendlyByteBuf buf) {
    }

    public static SPacketNpcRoleGet decode(FriendlyByteBuf buf) {
        return new SPacketNpcRoleGet();
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() != 0) {
            CompoundTag compound = new CompoundTag();
            compound.putBoolean("RoleData", true);
            Packets.send(this.player, new PacketGuiData(this.npc.role.save(compound)));
        }
    }
}