package noppes.npcs.packets.server;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.event.RoleEvent;
import noppes.npcs.containers.ContainerMail;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerMailSend extends PacketServerBasic {

    private final CompoundTag data;

    private final String username;

    public SPacketPlayerMailSend(String username, CompoundTag data) {
        this.username = username;
        this.data = data;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketPlayerMailSend msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.username);
        buf.writeNbt(msg.data);
    }

    public static SPacketPlayerMailSend decode(FriendlyByteBuf buf) {
        return new SPacketPlayerMailSend(buf.readUtf(32767), buf.readNbt());
    }

    @Override
    protected void handle() {
        String username = PlayerDataController.instance.hasPlayer(this.username);
        if (username.isEmpty()) {
            NoppesUtilServer.sendGuiError(this.player, 0);
        } else {
            PlayerMail mail = new PlayerMail();
            String s = this.player.m_5446_().getString();
            if (!s.equals(this.player.m_7755_().getString())) {
                s = s + "(" + this.player.m_7755_().getString() + ")";
            }
            mail.readNBT(this.data);
            mail.sender = s;
            if (mail.subject.isEmpty()) {
                NoppesUtilServer.sendGuiError(this.player, 1);
            } else {
                mail.items = ((ContainerMail) this.player.f_36096_).mail.items;
                CompoundTag comp = new CompoundTag();
                comp.putString("username", username);
                NoppesUtilServer.sendGuiClose(this.player, 1, comp);
                this.player.closeContainer();
                EntityNPCInterface npc = NoppesUtilServer.getEditingNpc(this.player);
                if (npc == null || !EventHooks.onNPCRole(npc, new RoleEvent.MailmanEvent(this.player, npc.wrappedNPC, mail))) {
                    PlayerDataController.instance.addPlayerMessage(this.player.m_20194_(), username, mail);
                }
            }
        }
    }
}