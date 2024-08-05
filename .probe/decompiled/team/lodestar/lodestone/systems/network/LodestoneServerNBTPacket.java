package team.lodestar.lodestone.systems.network;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public abstract class LodestoneServerNBTPacket extends LodestoneServerPacket {

    protected CompoundTag data;

    public LodestoneServerNBTPacket(CompoundTag data) {
        this.data = data;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeNbt(this.data);
    }

    @Override
    public final void execute(Supplier<NetworkEvent.Context> context) {
        this.execute(context, this.data);
    }

    @OnlyIn(Dist.CLIENT)
    public void execute(Supplier<NetworkEvent.Context> context, CompoundTag data) {
    }
}