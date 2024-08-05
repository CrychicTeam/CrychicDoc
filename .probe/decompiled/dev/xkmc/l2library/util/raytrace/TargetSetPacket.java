package dev.xkmc.l2library.util.raytrace;

import dev.xkmc.l2serial.network.SerialPacketBase;
import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraftforge.network.NetworkEvent;

@SerialClass
public class TargetSetPacket extends SerialPacketBase {

    @SerialField
    public UUID player;

    @SerialField
    public UUID target;

    public TargetSetPacket(UUID player, @Nullable UUID target) {
        this.player = player;
        this.target = target;
    }

    @Deprecated
    public TargetSetPacket() {
    }

    public void handle(NetworkEvent.Context context) {
        RayTraceUtil.sync(this);
    }
}