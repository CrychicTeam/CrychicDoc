package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.entity.data.DataScenes;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketSceneStart extends PacketServerBasic {

    private int scene;

    public SPacketSceneStart(int scene) {
        this.scene = scene;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.SCENES;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketSceneStart msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.scene);
    }

    public static SPacketSceneStart decode(FriendlyByteBuf buf) {
        return new SPacketSceneStart(buf.readInt());
    }

    @Override
    protected void handle() {
        if (CustomNpcs.SceneButtonsEnabled) {
            DataScenes.Toggle(this.player.m_20194_(), this.scene + "btn");
        }
    }
}