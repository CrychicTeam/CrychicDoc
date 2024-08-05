package software.bernie.geckolib.network.packet;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.util.ClientUtils;

public class BlockEntityAnimDataSyncPacket<D> {

    private final BlockPos pos;

    private final SerializableDataTicket<D> dataTicket;

    private final D data;

    public BlockEntityAnimDataSyncPacket(BlockPos pos, SerializableDataTicket<D> dataTicket, D data) {
        this.pos = pos;
        this.dataTicket = dataTicket;
        this.data = data;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeUtf(this.dataTicket.id());
        this.dataTicket.encode(this.data, buffer);
    }

    public static <D> BlockEntityAnimDataSyncPacket<D> decode(FriendlyByteBuf buffer) {
        BlockPos pos = buffer.readBlockPos();
        SerializableDataTicket<D> dataTicket = (SerializableDataTicket<D>) DataTickets.byName(buffer.readUtf());
        return new BlockEntityAnimDataSyncPacket<>(pos, dataTicket, dataTicket.decode(buffer));
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context handler = (NetworkEvent.Context) context.get();
        handler.enqueueWork(() -> {
            if (ClientUtils.getLevel().getBlockEntity(this.pos) instanceof GeoBlockEntity geoBlockEntity) {
                geoBlockEntity.setAnimData(this.dataTicket, this.data);
            }
        });
        handler.setPacketHandled(true);
    }
}