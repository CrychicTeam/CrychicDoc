package com.simibubi.create.content.trains.schedule;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ScheduleEditPacket extends SimplePacketBase {

    private Schedule schedule;

    public ScheduleEditPacket(Schedule schedule) {
        this.schedule = schedule;
    }

    public ScheduleEditPacket(FriendlyByteBuf buffer) {
        this.schedule = Schedule.fromTag(buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeNbt(this.schedule.write());
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            ItemStack mainHandItem = sender.m_21205_();
            if (AllItems.SCHEDULE.isIn(mainHandItem)) {
                CompoundTag tag = mainHandItem.getOrCreateTag();
                if (this.schedule.entries.isEmpty()) {
                    tag.remove("Schedule");
                    if (tag.isEmpty()) {
                        mainHandItem.setTag(null);
                    }
                } else {
                    tag.put("Schedule", this.schedule.write());
                }
                sender.m_36335_().addCooldown(mainHandItem.getItem(), 5);
            }
        });
        return true;
    }
}