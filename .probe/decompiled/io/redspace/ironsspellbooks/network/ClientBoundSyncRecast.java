package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientBoundSyncRecast {

    private final RecastInstance recastInstance;

    public ClientBoundSyncRecast(RecastInstance recastInstance) {
        this.recastInstance = recastInstance;
    }

    public ClientBoundSyncRecast(FriendlyByteBuf buf) {
        this.recastInstance = new RecastInstance();
        this.recastInstance.readFromBuffer(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        if (this.recastInstance != null) {
            this.recastInstance.writeToBuffer(buf);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.getRecasts().forceAddRecast(this.recastInstance));
        return true;
    }
}