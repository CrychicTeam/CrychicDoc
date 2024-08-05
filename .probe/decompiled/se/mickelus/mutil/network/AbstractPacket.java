package se.mickelus.mutil.network;

import java.io.IOException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public abstract class AbstractPacket {

    public abstract void toBytes(FriendlyByteBuf var1);

    public abstract void fromBytes(FriendlyByteBuf var1);

    public abstract void handle(Player var1);

    protected static String readString(FriendlyByteBuf buffer) throws IOException {
        String string = "";
        for (char c = buffer.readChar(); c != 0; c = buffer.readChar()) {
            string = string + c;
        }
        return string;
    }

    protected static void writeString(String string, FriendlyByteBuf buffer) throws IOException {
        for (int i = 0; i < string.length(); i++) {
            buffer.writeChar(string.charAt(i));
        }
        buffer.writeChar(0);
    }
}