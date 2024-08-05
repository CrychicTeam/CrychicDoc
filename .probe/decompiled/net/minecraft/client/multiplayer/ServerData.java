package net.minecraft.client.multiplayer;

import com.mojang.logging.LogUtils;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import org.slf4j.Logger;

public class ServerData {

    private static final Logger LOGGER = LogUtils.getLogger();

    public String name;

    public String ip;

    public Component status;

    public Component motd;

    @Nullable
    public ServerStatus.Players players;

    public long ping;

    public int protocol = SharedConstants.getCurrentVersion().getProtocolVersion();

    public Component version = Component.literal(SharedConstants.getCurrentVersion().getName());

    public boolean pinged;

    public List<Component> playerList = Collections.emptyList();

    private ServerData.ServerPackStatus packStatus = ServerData.ServerPackStatus.PROMPT;

    @Nullable
    private byte[] iconBytes;

    private boolean lan;

    private boolean enforcesSecureChat;

    public ServerData(String string0, String string1, boolean boolean2) {
        this.name = string0;
        this.ip = string1;
        this.lan = boolean2;
    }

    public CompoundTag write() {
        CompoundTag $$0 = new CompoundTag();
        $$0.putString("name", this.name);
        $$0.putString("ip", this.ip);
        if (this.iconBytes != null) {
            $$0.putString("icon", Base64.getEncoder().encodeToString(this.iconBytes));
        }
        if (this.packStatus == ServerData.ServerPackStatus.ENABLED) {
            $$0.putBoolean("acceptTextures", true);
        } else if (this.packStatus == ServerData.ServerPackStatus.DISABLED) {
            $$0.putBoolean("acceptTextures", false);
        }
        return $$0;
    }

    public ServerData.ServerPackStatus getResourcePackStatus() {
        return this.packStatus;
    }

    public void setResourcePackStatus(ServerData.ServerPackStatus serverDataServerPackStatus0) {
        this.packStatus = serverDataServerPackStatus0;
    }

    public static ServerData read(CompoundTag compoundTag0) {
        ServerData $$1 = new ServerData(compoundTag0.getString("name"), compoundTag0.getString("ip"), false);
        if (compoundTag0.contains("icon", 8)) {
            try {
                $$1.setIconBytes(Base64.getDecoder().decode(compoundTag0.getString("icon")));
            } catch (IllegalArgumentException var3) {
                LOGGER.warn("Malformed base64 server icon", var3);
            }
        }
        if (compoundTag0.contains("acceptTextures", 1)) {
            if (compoundTag0.getBoolean("acceptTextures")) {
                $$1.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
            } else {
                $$1.setResourcePackStatus(ServerData.ServerPackStatus.DISABLED);
            }
        } else {
            $$1.setResourcePackStatus(ServerData.ServerPackStatus.PROMPT);
        }
        return $$1;
    }

    @Nullable
    public byte[] getIconBytes() {
        return this.iconBytes;
    }

    public void setIconBytes(@Nullable byte[] byte0) {
        this.iconBytes = byte0;
    }

    public boolean isLan() {
        return this.lan;
    }

    public void setEnforcesSecureChat(boolean boolean0) {
        this.enforcesSecureChat = boolean0;
    }

    public boolean enforcesSecureChat() {
        return this.enforcesSecureChat;
    }

    public void copyNameIconFrom(ServerData serverData0) {
        this.ip = serverData0.ip;
        this.name = serverData0.name;
        this.iconBytes = serverData0.iconBytes;
    }

    public void copyFrom(ServerData serverData0) {
        this.copyNameIconFrom(serverData0);
        this.setResourcePackStatus(serverData0.getResourcePackStatus());
        this.lan = serverData0.lan;
        this.enforcesSecureChat = serverData0.enforcesSecureChat;
    }

    public static enum ServerPackStatus {

        ENABLED("enabled"), DISABLED("disabled"), PROMPT("prompt");

        private final Component name;

        private ServerPackStatus(String p_105399_) {
            this.name = Component.translatable("addServer.resourcePack." + p_105399_);
        }

        public Component getName() {
            return this.name;
        }
    }
}