package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.containers.ContainerNPCFollower;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;
import noppes.npcs.roles.RoleFollower;

public class SPacketFollowerExtend extends PacketServerBasic {

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketFollowerExtend msg, FriendlyByteBuf buf) {
    }

    public static SPacketFollowerExtend decode(FriendlyByteBuf buf) {
        return new SPacketFollowerExtend();
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 2) {
            AbstractContainerMenu con = this.player.f_36096_;
            if (con != null && con instanceof ContainerNPCFollower container) {
                RoleFollower role = (RoleFollower) this.npc.role;
                SPacketFollowerHire.followerBuy(role, container.currencyMatrix, this.player, this.npc);
                Packets.send(this.player, new PacketGuiData(this.npc.role.save(new CompoundTag())));
            }
        }
    }
}