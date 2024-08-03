package com.simibubi.create.content.trains.track;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableObject;

public class CurvedTrackSelectionPacket extends BlockEntityConfigurationPacket<TrackBlockEntity> {

    private BlockPos targetPos;

    private boolean front;

    private int segment;

    private int slot;

    public CurvedTrackSelectionPacket(BlockPos pos, BlockPos targetPos, int segment, boolean front, int slot) {
        super(pos);
        this.targetPos = targetPos;
        this.segment = segment;
        this.front = front;
        this.slot = slot;
    }

    public CurvedTrackSelectionPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.targetPos);
        buffer.writeVarInt(this.segment);
        buffer.writeBoolean(this.front);
        buffer.writeVarInt(this.slot);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        this.targetPos = buffer.readBlockPos();
        this.segment = buffer.readVarInt();
        this.front = buffer.readBoolean();
        this.slot = buffer.readVarInt();
    }

    protected void applySettings(ServerPlayer player, TrackBlockEntity be) {
        if (player.m_150109_().selected == this.slot) {
            ItemStack stack = player.m_150109_().getItem(this.slot);
            if (stack.getItem() instanceof TrackTargetingBlockItem) {
                if (player.m_6144_() && stack.hasTag()) {
                    player.displayClientMessage(Lang.translateDirect("track_target.clear"), true);
                    stack.setTag(null);
                    AllSoundEvents.CONTROLLER_CLICK.play(player.m_9236_(), null, this.pos, 1.0F, 0.5F);
                } else {
                    EdgePointType<?> type = AllBlocks.TRACK_SIGNAL.isIn(stack) ? EdgePointType.SIGNAL : EdgePointType.STATION;
                    MutableObject<TrackTargetingBlockItem.OverlapResult> result = new MutableObject(null);
                    TrackTargetingBlockItem.withGraphLocation(player.m_9236_(), this.pos, this.front, new BezierTrackPointLocation(this.targetPos, this.segment), type, (overlap, location) -> result.setValue(overlap));
                    if (((TrackTargetingBlockItem.OverlapResult) result.getValue()).feedback != null) {
                        player.displayClientMessage(Lang.translateDirect(((TrackTargetingBlockItem.OverlapResult) result.getValue()).feedback).withStyle(ChatFormatting.RED), true);
                        AllSoundEvents.DENY.play(player.m_9236_(), null, this.pos, 0.5F, 1.0F);
                    } else {
                        CompoundTag stackTag = stack.getOrCreateTag();
                        stackTag.put("SelectedPos", NbtUtils.writeBlockPos(this.pos));
                        stackTag.putBoolean("SelectedDirection", this.front);
                        CompoundTag bezierNbt = new CompoundTag();
                        bezierNbt.putInt("Segment", this.segment);
                        bezierNbt.put("Key", NbtUtils.writeBlockPos(this.targetPos));
                        bezierNbt.putBoolean("FromStack", true);
                        stackTag.put("Bezier", bezierNbt);
                        player.displayClientMessage(Lang.translateDirect("track_target.set"), true);
                        stack.setTag(stackTag);
                        AllSoundEvents.CONTROLLER_CLICK.play(player.m_9236_(), null, this.pos, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @Override
    protected int maxRange() {
        return 64;
    }

    protected void applySettings(TrackBlockEntity be) {
    }
}