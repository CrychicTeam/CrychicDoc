package com.simibubi.create.content.trains.track;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class PlaceExtendedCurvePacket extends SimplePacketBase {

    boolean mainHand;

    boolean ctrlDown;

    public PlaceExtendedCurvePacket(boolean mainHand, boolean ctrlDown) {
        this.mainHand = mainHand;
        this.ctrlDown = ctrlDown;
    }

    public PlaceExtendedCurvePacket(FriendlyByteBuf buffer) {
        this.mainHand = buffer.readBoolean();
        this.ctrlDown = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.mainHand);
        buffer.writeBoolean(this.ctrlDown);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            ItemStack stack = sender.m_21120_(this.mainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
            if (AllTags.AllBlockTags.TRACKS.matches(stack) && stack.hasTag()) {
                CompoundTag tag = stack.getTag();
                tag.putBoolean("ExtendCurve", true);
                stack.setTag(tag);
            }
        });
        return true;
    }
}