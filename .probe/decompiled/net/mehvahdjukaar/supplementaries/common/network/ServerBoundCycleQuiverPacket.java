package net.mehvahdjukaar.supplementaries.common.network;

import java.util.Objects;
import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.Message;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class ServerBoundCycleQuiverPacket implements Message {

    private final int amount;

    private final ServerBoundCycleQuiverPacket.Slot slot;

    private final boolean setSlot;

    public ServerBoundCycleQuiverPacket(FriendlyByteBuf buf) {
        this.amount = buf.readInt();
        this.slot = ServerBoundCycleQuiverPacket.Slot.values()[buf.readInt()];
        this.setSlot = buf.readBoolean();
    }

    public ServerBoundCycleQuiverPacket(int amount, ServerBoundCycleQuiverPacket.Slot slot, boolean setSlot) {
        this.amount = amount;
        this.slot = slot;
        this.setSlot = setSlot;
    }

    public ServerBoundCycleQuiverPacket(int amount, ServerBoundCycleQuiverPacket.Slot slot) {
        this(amount, slot, false);
    }

    @Override
    public void writeToBuffer(FriendlyByteBuf buf) {
        buf.writeInt(this.amount);
        buf.writeInt(this.slot.ordinal());
        buf.writeBoolean(this.setSlot);
    }

    @Override
    public void handle(ChannelHandler.Context context) {
        ServerPlayer player = (ServerPlayer) Objects.requireNonNull(context.getSender());
        ItemStack stack = ItemStack.EMPTY;
        if (this.slot == ServerBoundCycleQuiverPacket.Slot.INVENTORY) {
            stack = QuiverItem.getQuiver(player);
        } else if (player.m_7655_() == InteractionHand.MAIN_HAND == (this.slot == ServerBoundCycleQuiverPacket.Slot.MAIN_HAND)) {
            stack = player.m_21211_();
        }
        if (stack.getItem() != ModRegistry.QUIVER_ITEM.get()) {
            Supplementaries.error();
        } else {
            QuiverItem.Data data = QuiverItem.getQuiverData(stack);
            if (this.setSlot) {
                data.setSelectedSlot(this.amount);
            } else {
                data.cycle(this.amount);
            }
        }
    }

    public static enum Slot {

        MAIN_HAND, OFF_HAND, INVENTORY
    }
}