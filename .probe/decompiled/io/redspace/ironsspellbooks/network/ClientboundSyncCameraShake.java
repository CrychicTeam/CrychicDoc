package io.redspace.ironsspellbooks.network;

import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundSyncCameraShake {

    ArrayList<CameraShakeData> cameraShakeData;

    public ClientboundSyncCameraShake(ArrayList<CameraShakeData> cameraShakeData) {
        this.cameraShakeData = cameraShakeData;
    }

    public ClientboundSyncCameraShake(FriendlyByteBuf buf) {
        this.cameraShakeData = new ArrayList();
        int i = buf.readInt();
        for (int j = 0; j < i; j++) {
            this.cameraShakeData.add(CameraShakeData.deserializeFromBuffer(buf));
        }
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.cameraShakeData.size());
        for (CameraShakeData data : this.cameraShakeData) {
            data.serializeToBuffer(buf);
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = (NetworkEvent.Context) supplier.get();
        ctx.enqueueWork(() -> CameraShakeManager.clientCameraShakeData = this.cameraShakeData);
        return true;
    }
}