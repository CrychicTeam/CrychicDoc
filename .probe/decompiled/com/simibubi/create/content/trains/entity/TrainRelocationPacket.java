package com.simibubi.create.content.trains.entity;

import com.simibubi.create.AllPackets;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.ContraptionRelocationPacket;
import com.simibubi.create.content.trains.track.BezierTrackPointLocation;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class TrainRelocationPacket extends SimplePacketBase {

    UUID trainId;

    BlockPos pos;

    Vec3 lookAngle;

    int entityId;

    private boolean direction;

    private BezierTrackPointLocation hoveredBezier;

    public TrainRelocationPacket(FriendlyByteBuf buffer) {
        this.trainId = buffer.readUUID();
        this.pos = buffer.readBlockPos();
        this.lookAngle = VecHelper.read(buffer);
        this.entityId = buffer.readInt();
        this.direction = buffer.readBoolean();
        if (buffer.readBoolean()) {
            this.hoveredBezier = new BezierTrackPointLocation(buffer.readBlockPos(), buffer.readInt());
        }
    }

    public TrainRelocationPacket(UUID trainId, BlockPos pos, BezierTrackPointLocation hoveredBezier, boolean direction, Vec3 lookAngle, int entityId) {
        this.trainId = trainId;
        this.pos = pos;
        this.hoveredBezier = hoveredBezier;
        this.direction = direction;
        this.lookAngle = lookAngle;
        this.entityId = entityId;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.trainId);
        buffer.writeBlockPos(this.pos);
        VecHelper.write(this.lookAngle, buffer);
        buffer.writeInt(this.entityId);
        buffer.writeBoolean(this.direction);
        buffer.writeBoolean(this.hoveredBezier != null);
        if (this.hoveredBezier != null) {
            buffer.writeBlockPos(this.hoveredBezier.curveTarget());
            buffer.writeInt(this.hoveredBezier.segment());
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            Train train = (Train) Create.RAILWAYS.trains.get(this.trainId);
            Entity entity = sender.m_9236_().getEntity(this.entityId);
            String messagePrefix = sender.m_7755_().getString() + " could not relocate Train ";
            if (train != null && entity instanceof CarriageContraptionEntity cce) {
                if (train.id.equals(cce.trainId)) {
                    int verifyDistance = AllConfigs.server().trains.maxTrackPlacementLength.get() * 2;
                    if (!sender.m_20182_().closerThan(Vec3.atCenterOf(this.pos), (double) verifyDistance)) {
                        Create.LOGGER.warn(messagePrefix + train.name.getString() + ": player too far from clicked pos");
                    } else if (!sender.m_20182_().closerThan(cce.m_20182_(), (double) verifyDistance + cce.m_20191_().getXsize() / 2.0)) {
                        Create.LOGGER.warn(messagePrefix + train.name.getString() + ": player too far from carriage entity");
                    } else if (TrainRelocator.relocate(train, sender.m_9236_(), this.pos, this.hoveredBezier, this.direction, this.lookAngle, false)) {
                        sender.displayClientMessage(Lang.translateDirect("train.relocate.success").withStyle(ChatFormatting.GREEN), true);
                        train.carriages.forEach(c -> c.forEachPresentEntity(e -> {
                            e.nonDamageTicks = 10;
                            AllPackets.getChannel().send(PacketDistributor.TRACKING_ENTITY.with(() -> e), new ContraptionRelocationPacket(e.m_19879_()));
                        }));
                    } else {
                        Create.LOGGER.warn(messagePrefix + train.name.getString() + ": relocation failed server-side");
                    }
                }
            } else {
                Create.LOGGER.warn(messagePrefix + train.id.toString().substring(0, 5) + ": not present on server");
            }
        });
        return true;
    }
}