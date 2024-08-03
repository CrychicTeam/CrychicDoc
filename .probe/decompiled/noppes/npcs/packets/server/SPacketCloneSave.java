package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomItems;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketCloneSave extends PacketServerBasic {

    private String name;

    private int tab;

    public SPacketCloneSave(String name, int tab) {
        this.name = name;
        this.tab = tab;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return item.getItem() == CustomItems.cloner;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_CLONE;
    }

    public static void encode(SPacketCloneSave msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.name);
        buf.writeInt(msg.tab);
    }

    public static SPacketCloneSave decode(FriendlyByteBuf buf) {
        return new SPacketCloneSave(buf.readUtf(32767), buf.readInt());
    }

    @Override
    protected void handle() {
        PlayerData data = PlayerData.get(this.player);
        if (data.cloned != null) {
            ServerCloneController.Instance.addClone(data.cloned, this.name, this.tab);
        }
    }
}