package github.pitbox46.fightnbtintegration.network;

import github.pitbox46.fightnbtintegration.Config;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SSyncConfig implements IPacket {

    public String json;

    public SSyncConfig(String json) {
        this.json = json;
    }

    public SSyncConfig() {
    }

    public SSyncConfig readPacketData(FriendlyByteBuf buf) {
        this.json = buf.readUtf();
        return this;
    }

    @Override
    public void writePacketData(FriendlyByteBuf buf) {
        buf.writeUtf(this.json, this.json.length());
    }

    @Override
    public void processPacket(NetworkEvent.Context ctx) {
        Config.readConfig(this.json);
    }
}