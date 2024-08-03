package com.simibubi.create.content.equipment.zapper;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public abstract class ConfigureZapperPacket extends SimplePacketBase {

    protected InteractionHand hand;

    protected PlacementPatterns pattern;

    public ConfigureZapperPacket(InteractionHand hand, PlacementPatterns pattern) {
        this.hand = hand;
        this.pattern = pattern;
    }

    public ConfigureZapperPacket(FriendlyByteBuf buffer) {
        this.hand = buffer.readEnum(InteractionHand.class);
        this.pattern = buffer.readEnum(PlacementPatterns.class);
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(this.hand);
        buffer.writeEnum(this.pattern);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ItemStack stack = player.m_21120_(this.hand);
                if (stack.getItem() instanceof ZapperItem) {
                    this.configureZapper(stack);
                }
            }
        });
        return true;
    }

    public abstract void configureZapper(ItemStack var1);
}