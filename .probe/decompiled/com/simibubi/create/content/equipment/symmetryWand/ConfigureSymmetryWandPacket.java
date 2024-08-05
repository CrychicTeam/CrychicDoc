package com.simibubi.create.content.equipment.symmetryWand;

import com.simibubi.create.content.equipment.symmetryWand.mirror.SymmetryMirror;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ConfigureSymmetryWandPacket extends SimplePacketBase {

    protected InteractionHand hand;

    protected SymmetryMirror mirror;

    public ConfigureSymmetryWandPacket(InteractionHand hand, SymmetryMirror mirror) {
        this.hand = hand;
        this.mirror = mirror;
    }

    public ConfigureSymmetryWandPacket(FriendlyByteBuf buffer) {
        this.hand = buffer.readEnum(InteractionHand.class);
        this.mirror = SymmetryMirror.fromNBT(buffer.readNbt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.hand);
        buffer.writeNbt(this.mirror.writeToNbt());
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack stack = player.m_21120_(this.hand);
                if (stack.getItem() instanceof SymmetryWandItem) {
                    SymmetryWandItem.configureSettings(stack, this.mirror);
                }
            }
        });
        return true;
    }
}