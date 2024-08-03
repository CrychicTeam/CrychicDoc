package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketRemoteMenuOpen extends PacketServerBasic {

    private int entityId;

    public SPacketRemoteMenuOpen(int entityId) {
        this.entityId = entityId;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketRemoteMenuOpen msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static SPacketRemoteMenuOpen decode(FriendlyByteBuf buf) {
        return new SPacketRemoteMenuOpen(buf.readInt());
    }

    @Override
    protected void handle() {
        Entity entity = this.player.m_9236_().getEntity(this.entityId);
        if (entity != null && entity instanceof EntityNPCInterface) {
            NoppesUtilServer.sendOpenGui(this.player, EnumGuiType.MainMenuDisplay, (EntityNPCInterface) entity);
        }
    }
}