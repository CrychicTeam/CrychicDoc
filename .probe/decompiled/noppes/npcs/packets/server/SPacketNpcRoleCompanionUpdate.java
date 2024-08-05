package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.constants.EnumCompanionStage;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.roles.RoleCompanion;

public class SPacketNpcRoleCompanionUpdate extends PacketServerBasic {

    private EnumCompanionStage stage;

    public SPacketNpcRoleCompanionUpdate(EnumCompanionStage stage) {
        this.stage = stage;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketNpcRoleCompanionUpdate msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.stage);
    }

    public static SPacketNpcRoleCompanionUpdate decode(FriendlyByteBuf buf) {
        return new SPacketNpcRoleCompanionUpdate(buf.readEnum(EnumCompanionStage.class));
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 6) {
            ((RoleCompanion) this.npc.role).matureTo(this.stage);
            this.npc.updateClient = true;
        }
    }
}