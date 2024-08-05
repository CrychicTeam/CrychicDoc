package com.simibubi.create.content.kinetics.mechanicalArm;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.Collection;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ArmPlacementPacket extends SimplePacketBase {

    private Collection<ArmInteractionPoint> points;

    private ListTag receivedTag;

    private BlockPos pos;

    public ArmPlacementPacket(Collection<ArmInteractionPoint> points, BlockPos pos) {
        this.points = points;
        this.pos = pos;
    }

    public ArmPlacementPacket(FriendlyByteBuf buffer) {
        CompoundTag nbt = buffer.readNbt();
        this.receivedTag = nbt.getList("Points", 10);
        this.pos = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        CompoundTag nbt = new CompoundTag();
        ListTag pointsNBT = new ListTag();
        this.points.stream().map(aip -> aip.serialize(this.pos)).forEach(pointsNBT::add);
        nbt.put("Points", pointsNBT);
        buffer.writeNbt(nbt);
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                Level world = player.m_9236_();
                if (world != null && world.isLoaded(this.pos)) {
                    if (world.getBlockEntity(this.pos) instanceof ArmBlockEntity arm) {
                        arm.interactionPointTag = this.receivedTag;
                    }
                }
            }
        });
        return true;
    }

    public static class ClientBoundRequest extends SimplePacketBase {

        BlockPos pos;

        public ClientBoundRequest(BlockPos pos) {
            this.pos = pos;
        }

        public ClientBoundRequest(FriendlyByteBuf buffer) {
            this.pos = buffer.readBlockPos();
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            buffer.writeBlockPos(this.pos);
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ArmInteractionPointHandler.flushSettings(this.pos)));
            return true;
        }
    }
}