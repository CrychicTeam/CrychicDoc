package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcs;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.entity.data.DataScenes;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketSceneReset extends PacketServerBasic {

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.SCENES;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketSceneReset msg, FriendlyByteBuf buf) {
    }

    public static SPacketSceneReset decode(FriendlyByteBuf buf) {
        return new SPacketSceneReset();
    }

    @Override
    protected void handle() {
        if (CustomNpcs.SceneButtonsEnabled) {
            DataScenes.Reset(this.player.m_20203_(), null);
        }
    }
}