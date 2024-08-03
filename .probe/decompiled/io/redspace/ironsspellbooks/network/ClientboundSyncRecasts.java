package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.capabilities.magic.PlayerRecasts;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncRecasts {

    private final Map<String, RecastInstance> recastLookup;

    public ClientboundSyncRecasts(Map<String, RecastInstance> recastLookup) {
        this.recastLookup = recastLookup;
    }

    public ClientboundSyncRecasts(FriendlyByteBuf buf) {
        this.recastLookup = buf.readMap(ClientboundSyncRecasts::readSpellID, ClientboundSyncRecasts::readRecastInstance);
    }

    public static String readSpellID(FriendlyByteBuf buffer) {
        return buffer.readUtf();
    }

    public static RecastInstance readRecastInstance(FriendlyByteBuf buffer) {
        RecastInstance tmp = new RecastInstance();
        tmp.readFromBuffer(buffer);
        return tmp;
    }

    public static void writeSpellId(FriendlyByteBuf buf, String spellId) {
        buf.writeUtf(spellId);
    }

    public static void writeRecastInstance(FriendlyByteBuf buf, RecastInstance recastInstance) {
        recastInstance.writeToBuffer(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeMap(this.recastLookup, ClientboundSyncRecasts::writeSpellId, ClientboundSyncRecasts::writeRecastInstance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> ClientMagicData.setRecasts(new PlayerRecasts(this.recastLookup)));
        return true;
    }
}