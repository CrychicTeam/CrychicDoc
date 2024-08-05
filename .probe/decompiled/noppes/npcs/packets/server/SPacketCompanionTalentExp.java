package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import noppes.npcs.constants.EnumCompanionTalent;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.roles.RoleCompanion;

public class SPacketCompanionTalentExp extends PacketServerBasic {

    private final EnumCompanionTalent talent;

    private final int exp;

    public SPacketCompanionTalentExp(EnumCompanionTalent talent, int exp) {
        this.talent = talent;
        this.exp = exp;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    public static void encode(SPacketCompanionTalentExp msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.talent);
        buf.writeInt(msg.exp);
    }

    public static SPacketCompanionTalentExp decode(FriendlyByteBuf buf) {
        return new SPacketCompanionTalentExp(buf.readEnum(EnumCompanionTalent.class), buf.readInt());
    }

    @Override
    protected void handle() {
        if (this.npc.role.getType() == 6 && this.player == this.npc.getOwner()) {
            RoleCompanion role = (RoleCompanion) this.npc.role;
            if (this.exp > 0 && role.canAddExp(-this.exp)) {
                role.addExp(-this.exp);
                role.addTalentExp(this.talent, this.exp);
            }
        }
    }
}