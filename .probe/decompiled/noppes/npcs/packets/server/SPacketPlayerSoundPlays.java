package noppes.npcs.packets.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import noppes.npcs.EventHooks;
import noppes.npcs.packets.PacketServerBasic;

public class SPacketPlayerSoundPlays extends PacketServerBasic {

    private final String sound;

    private final String category;

    private final boolean looping;

    public SPacketPlayerSoundPlays(String sound, String category, boolean looping) {
        this.sound = sound;
        this.category = category;
        this.looping = looping;
    }

    @Override
    public boolean toolAllowed(ItemStack item) {
        return true;
    }

    public static void encode(SPacketPlayerSoundPlays msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.sound == null ? "" : msg.sound);
        buf.writeUtf(msg.category == null ? "" : msg.category);
        buf.writeBoolean(msg.looping);
    }

    public static SPacketPlayerSoundPlays decode(FriendlyByteBuf buf) {
        return new SPacketPlayerSoundPlays(buf.readUtf(32767), buf.readUtf(32767), buf.readBoolean());
    }

    @Override
    protected void handle() {
        EventHooks.onPlayerPlaySound(this.player, this.sound, this.category, this.looping);
    }
}