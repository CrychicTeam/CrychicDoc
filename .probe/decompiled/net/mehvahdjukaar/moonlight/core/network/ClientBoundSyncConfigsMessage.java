package net.mehvahdjukaar.moonlight.core.network;

import java.io.ByteArrayInputStream;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.network.FriendlyByteBuf;

public class ClientBoundSyncConfigsMessage implements Message {

    public final String fineName;

    public final String modId;

    public final byte[] configData;

    public ClientBoundSyncConfigsMessage(FriendlyByteBuf buf) {
        this.modId = buf.readUtf();
        this.fineName = buf.readUtf();
        this.configData = buf.readByteArray();
    }

    public ClientBoundSyncConfigsMessage(byte[] configFileData, String fileName, String modId) {
        this.modId = modId;
        this.fineName = fileName;
        this.configData = configFileData;
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeUtf(this.modId);
        buf.writeUtf(this.fineName);
        buf.writeByteArray(this.configData);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        ConfigSpec config = ConfigSpec.getSpec(this.modId, ConfigType.COMMON);
        if (config != null) {
            try {
                ByteArrayInputStream stream = new ByteArrayInputStream(this.configData);
                try {
                    config.loadFromBytes(stream);
                    Moonlight.LOGGER.info("Synced {} configs", this.fineName);
                } catch (Throwable var7) {
                    try {
                        stream.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                    throw var7;
                }
                stream.close();
            } catch (Exception var8) {
            }
        } else {
            Moonlight.LOGGER.error("Failed to find config file with name {}", this.fineName);
        }
    }
}