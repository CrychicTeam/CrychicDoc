package team.lodestar.lodestone.systems.network;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public abstract class LodestoneTwoWayNBTPacket extends LodestoneTwoWayPacket {

    protected CompoundTag data;

    public LodestoneTwoWayNBTPacket(CompoundTag data) {
        this.data = data;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.data);
    }

    @Override
    public final void serverExecute(Supplier<NetworkEvent.Context> context) {
        this.serverExecute(context, this.data);
    }

    @Override
    public final void clientExecute(Supplier<NetworkEvent.Context> context) {
        this.clientExecute(context, this.data);
    }

    public void serverExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }

    @OnlyIn(Dist.CLIENT)
    public void clientExecute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }
}