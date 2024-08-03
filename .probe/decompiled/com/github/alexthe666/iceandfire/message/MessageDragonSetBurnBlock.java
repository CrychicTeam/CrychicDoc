package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageDragonSetBurnBlock {

    public int dragonId;

    public boolean breathingFire;

    public int posX;

    public int posY;

    public int posZ;

    public MessageDragonSetBurnBlock(int dragonId, boolean breathingFire, BlockPos pos) {
        this.dragonId = dragonId;
        this.breathingFire = breathingFire;
        this.posX = pos.m_123341_();
        this.posY = pos.m_123342_();
        this.posZ = pos.m_123343_();
    }

    public static MessageDragonSetBurnBlock read(FriendlyByteBuf buf) {
        return new MessageDragonSetBurnBlock(buf.readInt(), buf.readBoolean(), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
    }

    public static void write(MessageDragonSetBurnBlock message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeBoolean(message.breathingFire);
        buf.writeInt(message.posX);
        buf.writeInt(message.posY);
        buf.writeInt(message.posZ);
    }

    public static class Handler {

        public static void handle(MessageDragonSetBurnBlock message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null && player.m_9236_().getEntity(message.dragonId) instanceof EntityDragonBase dragon) {
                    dragon.setBreathingFire(message.breathingFire);
                    dragon.burningTarget = new BlockPos(message.posX, message.posY, message.posZ);
                }
            });
            context.setPacketHandled(true);
        }
    }
}