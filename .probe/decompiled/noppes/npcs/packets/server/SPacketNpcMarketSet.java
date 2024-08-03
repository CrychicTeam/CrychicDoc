package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.server.permission.nodes.PermissionNode;
import noppes.npcs.CustomNpcsPermissions;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.roles.RoleTrader;

public class SPacketNpcMarketSet extends PacketServerBasic {

    private String market;

    private boolean save;

    public SPacketNpcMarketSet(String market, boolean save) {
        this.market = market;
        this.save = save;
    }

    @Override
    public boolean requiresNpc() {
        return true;
    }

    @Override
    public PermissionNode<Boolean> getPermission() {
        return CustomNpcsPermissions.NPC_ADVANCED;
    }

    public static void encode(SPacketNpcMarketSet msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.market);
        buf.writeBoolean(msg.save);
    }

    public static SPacketNpcMarketSet decode(FriendlyByteBuf buf) {
        return new SPacketNpcMarketSet(buf.readUtf(32767), buf.readBoolean());
    }

    @Override
    protected void handle() {
        if (this.npc.role instanceof RoleTrader) {
            if (this.save) {
                RoleTrader.save((RoleTrader) this.npc.role, this.market);
            } else {
                RoleTrader.setMarket(this.npc, this.market);
            }
        }
    }
}