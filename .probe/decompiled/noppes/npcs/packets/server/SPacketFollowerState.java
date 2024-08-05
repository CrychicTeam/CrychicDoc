package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;
import noppes.npcs.roles.RoleFollower;

public class SPacketFollowerState extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketFollowerState msg, FriendlyByteBuf buf) {
    }

    public static SPacketFollowerState decode(FriendlyByteBuf buf) {
        return new SPacketFollowerState();
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 2) {
            RoleFollower role = (RoleFollower) this.npc.role;
            if (role.owner != null && role.owner.getName().equals(this.player.m_7755_())) {
                role.isFollowing = !role.isFollowing;
                Packets.send(this.player, new PacketGuiData(this.npc.role.save(new CompoundTag())));
            }
        }
    }
}