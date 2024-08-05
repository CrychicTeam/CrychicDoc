package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.containers.ContainerCustomGui;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCustomGuiParts extends PacketServerBasic {

    private final CompoundTag data;

    public SPacketCustomGuiParts(CompoundTag data) {
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketCustomGuiParts msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static SPacketCustomGuiParts decode(FriendlyByteBuf buf) {
        return new SPacketCustomGuiParts(buf.readNbt());
    }

    @Override
    protected void handle() {
        if (this.player.f_36096_ instanceof ContainerCustomGui container) {
            container.customGui.npc.modelData.load(this.data);
            container.customGui.npc.updateClient = true;
        }
    }
}