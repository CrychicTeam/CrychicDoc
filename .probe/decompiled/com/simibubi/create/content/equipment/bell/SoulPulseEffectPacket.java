package com.simibubi.create.content.equipment.bell;

import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SoulPulseEffectPacket extends SimplePacketBase {

    public BlockPos pos;

    public int distance;

    public boolean canOverlap;

    public SoulPulseEffectPacket(BlockPos pos, int distance, boolean overlaps) {
        this.pos = pos;
        this.distance = distance;
        this.canOverlap = overlaps;
    }

    public SoulPulseEffectPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.distance = buffer.readInt();
        this.canOverlap = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.distance);
        buffer.writeBoolean(this.canOverlap);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> CreateClient.SOUL_PULSE_EFFECT_HANDLER.addPulse(new SoulPulseEffect(this.pos, this.distance, this.canOverlap)));
        return true;
    }
}