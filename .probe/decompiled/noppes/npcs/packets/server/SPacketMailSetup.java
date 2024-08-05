package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.constants.EnumGuiType;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketMailSetup extends PacketServerBasic {

    private CompoundTag data;

    public SPacketMailSetup(CompoundTag data) {
        this.data = data;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_GUI;
    }

    public static void encode(SPacketMailSetup msg, FriendlyByteBuf buf) {
        buf.writeNbt(msg.data);
    }

    public static SPacketMailSetup decode(FriendlyByteBuf buf) {
        return new SPacketMailSetup(buf.readNbt());
    }

    @Override
    protected void handle() {
        PlayerMail mail = new PlayerMail();
        mail.readNBT(this.data);
        ContainerMail.staticmail = mail;
        NoppesUtilServer.openContainerGui(this.player, EnumGuiType.PlayerMailman, buf -> {
            buf.writeBoolean(true);
            buf.writeBoolean(false);
        });
    }
}