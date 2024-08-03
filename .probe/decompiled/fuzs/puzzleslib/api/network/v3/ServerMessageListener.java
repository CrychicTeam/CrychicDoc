package fuzs.puzzleslib.api.network.v3;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public abstract class ServerMessageListener<T extends Record> {

    public abstract void handle(T var1, MinecraftServer var2, ServerGamePacketListenerImpl var3, ServerPlayer var4, ServerLevel var5);
}