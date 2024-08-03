package net.minecraft.server.dedicated;

import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.slf4j.Logger;

public class DedicatedPlayerList extends PlayerList {

    private static final Logger LOGGER = LogUtils.getLogger();

    public DedicatedPlayerList(DedicatedServer dedicatedServer0, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer1, PlayerDataStorage playerDataStorage2) {
        super(dedicatedServer0, layeredRegistryAccessRegistryLayer1, playerDataStorage2, dedicatedServer0.getProperties().maxPlayers);
        DedicatedServerProperties $$3 = dedicatedServer0.getProperties();
        this.m_11217_($$3.viewDistance);
        this.m_184211_($$3.simulationDistance);
        super.setUsingWhiteList($$3.whiteList.get());
        this.loadUserBanList();
        this.saveUserBanList();
        this.loadIpBanList();
        this.saveIpBanList();
        this.loadOps();
        this.loadWhiteList();
        this.saveOps();
        if (!this.m_11305_().m_11385_().exists()) {
            this.saveWhiteList();
        }
    }

    @Override
    public void setUsingWhiteList(boolean boolean0) {
        super.setUsingWhiteList(boolean0);
        this.getServer().storeUsingWhiteList(boolean0);
    }

    @Override
    public void op(GameProfile gameProfile0) {
        super.op(gameProfile0);
        this.saveOps();
    }

    @Override
    public void deop(GameProfile gameProfile0) {
        super.deop(gameProfile0);
        this.saveOps();
    }

    @Override
    public void reloadWhiteList() {
        this.loadWhiteList();
    }

    private void saveIpBanList() {
        try {
            this.m_11299_().m_11398_();
        } catch (IOException var2) {
            LOGGER.warn("Failed to save ip banlist: ", var2);
        }
    }

    private void saveUserBanList() {
        try {
            this.m_11295_().m_11398_();
        } catch (IOException var2) {
            LOGGER.warn("Failed to save user banlist: ", var2);
        }
    }

    private void loadIpBanList() {
        try {
            this.m_11299_().m_11399_();
        } catch (IOException var2) {
            LOGGER.warn("Failed to load ip banlist: ", var2);
        }
    }

    private void loadUserBanList() {
        try {
            this.m_11295_().m_11399_();
        } catch (IOException var2) {
            LOGGER.warn("Failed to load user banlist: ", var2);
        }
    }

    private void loadOps() {
        try {
            this.m_11307_().m_11399_();
        } catch (Exception var2) {
            LOGGER.warn("Failed to load operators list: ", var2);
        }
    }

    private void saveOps() {
        try {
            this.m_11307_().m_11398_();
        } catch (Exception var2) {
            LOGGER.warn("Failed to save operators list: ", var2);
        }
    }

    private void loadWhiteList() {
        try {
            this.m_11305_().m_11399_();
        } catch (Exception var2) {
            LOGGER.warn("Failed to load white-list: ", var2);
        }
    }

    private void saveWhiteList() {
        try {
            this.m_11305_().m_11398_();
        } catch (Exception var2) {
            LOGGER.warn("Failed to save white-list: ", var2);
        }
    }

    @Override
    public boolean isWhiteListed(GameProfile gameProfile0) {
        return !this.m_11311_() || this.m_11303_(gameProfile0) || this.m_11305_().isWhiteListed(gameProfile0);
    }

    public DedicatedServer getServer() {
        return (DedicatedServer) super.getServer();
    }

    @Override
    public boolean canBypassPlayerLimit(GameProfile gameProfile0) {
        return this.m_11307_().canBypassPlayerLimit(gameProfile0);
    }
}