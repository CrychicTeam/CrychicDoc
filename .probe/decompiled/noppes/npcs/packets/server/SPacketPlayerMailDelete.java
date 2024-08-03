package noppes.npcs.packets.server;

import java.util.Iterator;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.controllers.data.PlayerMail;
import noppes.npcs.controllers.data.PlayerMailData;
import noppes.npcs.packets.PacketServerBasic;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketGuiData;

public class SPacketPlayerMailDelete extends PacketServerBasic {

    private final long time;

    private final String username;

    public SPacketPlayerMailDelete(long time, String username) {
        this.time = time;
        this.username = username;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketPlayerMailDelete msg, FriendlyByteBuf buf) {
        buf.writeLong(msg.time);
        buf.writeUtf(msg.username);
    }

    public static SPacketPlayerMailDelete decode(FriendlyByteBuf buf) {
        return new SPacketPlayerMailDelete(buf.readLong(), buf.readUtf(32767));
    }

    @Override
    protected void handle() {
        PlayerMailData data = PlayerData.get(this.player).mailData;
        Iterator<PlayerMail> it = data.playermail.iterator();
        while (it.hasNext()) {
            PlayerMail mail = (PlayerMail) it.next();
            if (mail.time == this.time && mail.sender.equals(this.username)) {
                it.remove();
            }
        }
        Packets.send(this.player, new PacketGuiData(data.saveNBTData(new CompoundTag())));
    }
}