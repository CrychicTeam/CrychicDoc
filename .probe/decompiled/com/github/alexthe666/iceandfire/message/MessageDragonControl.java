package com.github.alexthe666.iceandfire.message;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityAmphithere;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityHippocampus;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageDragonControl {

    public int dragonId;

    public byte controlState;

    public int armor_type;

    private double posX;

    private double posY;

    private double posZ;

    public MessageDragonControl(int dragonId, byte controlState, double posX, double posY, double posZ) {
        this.dragonId = dragonId;
        this.controlState = controlState;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public MessageDragonControl() {
    }

    public static MessageDragonControl read(FriendlyByteBuf buf) {
        return new MessageDragonControl(buf.readInt(), buf.readByte(), buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static void write(MessageDragonControl message, FriendlyByteBuf buf) {
        buf.writeInt(message.dragonId);
        buf.writeByte(message.controlState);
        buf.writeDouble(message.posX);
        buf.writeDouble(message.posY);
        buf.writeDouble(message.posZ);
    }

    private double getPosX() {
        return this.posX;
    }

    private double getPosY() {
        return this.posY;
    }

    private double getPosZ() {
        return this.posZ;
    }

    public static class Handler {

        public static void handle(MessageDragonControl message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = (NetworkEvent.Context) contextSupplier.get();
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = IceAndFire.PROXY.getClientSidePlayer();
                }
                if (player != null) {
                    Entity entity = player.m_9236_().getEntity(message.dragonId);
                    if (ServerEvents.isRidingOrBeingRiddenBy(entity, player)) {
                        if (entity instanceof EntityDragonBase dragon) {
                            if (dragon.m_21830_(player)) {
                                dragon.setControlState(message.controlState);
                            }
                        } else if (entity instanceof EntityHippogryph hippogryph) {
                            if (hippogryph.m_21830_(player)) {
                                hippogryph.setControlState(message.controlState);
                            }
                        } else if (entity instanceof EntityHippocampus hippo) {
                            if (hippo.m_21830_(player)) {
                                hippo.setControlState(message.controlState);
                            }
                            hippo.m_6034_(message.getPosX(), message.getPosY(), message.getPosZ());
                        } else if (entity instanceof EntityDeathWorm deathWorm) {
                            deathWorm.setControlState(message.controlState);
                            deathWorm.m_6034_(message.getPosX(), message.getPosY(), message.getPosZ());
                        } else if (entity instanceof EntityAmphithere amphithere) {
                            if (amphithere.m_21830_(player)) {
                                amphithere.setControlState(message.controlState);
                            }
                            amphithere.m_6034_(message.getPosX(), message.getPosY(), message.getPosZ());
                        }
                    }
                }
            });
            context.setPacketHandled(true);
        }
    }
}