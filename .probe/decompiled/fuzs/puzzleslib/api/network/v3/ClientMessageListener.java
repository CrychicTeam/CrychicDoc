package fuzs.puzzleslib.api.network.v3;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;

public abstract class ClientMessageListener<T extends Record> {

    public abstract void handle(T var1, Minecraft var2, ClientPacketListener var3, LocalPlayer var4, ClientLevel var5);
}