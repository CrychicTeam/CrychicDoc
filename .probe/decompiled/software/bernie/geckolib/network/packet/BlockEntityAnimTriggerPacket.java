package software.bernie.geckolib.network.packet;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.util.ClientUtils;

public class BlockEntityAnimTriggerPacket<D> {

    private final BlockPos pos;

    private final String controllerName;

    private final String animName;

    public BlockEntityAnimTriggerPacket(BlockPos pos, @Nullable String controllerName, String animName) {
        this.pos = pos;
        this.controllerName = controllerName == null ? "" : controllerName;
        this.animName = animName;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeUtf(this.controllerName);
        buffer.writeUtf(this.animName);
    }

    public static <D> BlockEntityAnimTriggerPacket<D> decode(FriendlyByteBuf buffer) {
        return new BlockEntityAnimTriggerPacket<>(buffer.readBlockPos(), buffer.readUtf(), buffer.readUtf());
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context handler = (NetworkEvent.Context) context.get();
        handler.enqueueWork(() -> {
            if (ClientUtils.getLevel().getBlockEntity(this.pos) instanceof GeoBlockEntity getBlockEntity) {
                getBlockEntity.triggerAnim(this.controllerName.isEmpty() ? null : this.controllerName, this.animName);
            }
        });
        handler.setPacketHandled(true);
    }
}