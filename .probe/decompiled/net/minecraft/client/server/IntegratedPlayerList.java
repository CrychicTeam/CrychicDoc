package net.minecraft.client.server;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;

public class IntegratedPlayerList extends PlayerList {

    private CompoundTag playerData;

    public IntegratedPlayerList(IntegratedServer integratedServer0, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer1, PlayerDataStorage playerDataStorage2) {
        super(integratedServer0, layeredRegistryAccessRegistryLayer1, playerDataStorage2, 8);
        this.m_11217_(10);
    }

    @Override
    protected void save(ServerPlayer serverPlayer0) {
        if (this.getServer().isSingleplayerOwner(serverPlayer0.m_36316_())) {
            this.playerData = serverPlayer0.m_20240_(new CompoundTag());
        }
        super.save(serverPlayer0);
    }

    @Override
    public Component canPlayerLogin(SocketAddress socketAddress0, GameProfile gameProfile1) {
        return (Component) (this.getServer().isSingleplayerOwner(gameProfile1) && this.m_11255_(gameProfile1.getName()) != null ? Component.translatable("multiplayer.disconnect.name_taken") : super.canPlayerLogin(socketAddress0, gameProfile1));
    }

    public IntegratedServer getServer() {
        return (IntegratedServer) super.getServer();
    }

    @Override
    public CompoundTag getSingleplayerData() {
        return this.playerData;
    }
}