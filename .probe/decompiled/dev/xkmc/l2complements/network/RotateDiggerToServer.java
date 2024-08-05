package dev.xkmc.l2complements.network;

import dev.xkmc.l2complements.content.enchantment.digging.DiggerHelper;
import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class RotateDiggerToServer extends SerialPacketBase {

    @SerialField
    public boolean reverse;

    public RotateDiggerToServer() {
    }

    public RotateDiggerToServer(boolean reverse) {
        this.reverse = reverse;
    }

    public void handle(NetworkEvent.Context context) {
        ServerPlayer pl = context.getSender();
        if (pl != null) {
            DiggerHelper.rotateDigger(pl.m_21205_(), this.reverse);
        }
    }
}