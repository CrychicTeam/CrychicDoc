package com.rekindled.embers.network.message;

import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.util.Misc;
import java.util.Random;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

public class MessageEmberRayFX {

    public static Random random = new Random();

    double posX = 0.0;

    double posY = 0.0;

    double posZ = 0.0;

    double dX = 0.0;

    double dY = 0.0;

    double dZ = 0.0;

    double hitDistance = Double.POSITIVE_INFINITY;

    int packedColor;

    public MessageEmberRayFX() {
    }

    public MessageEmberRayFX(double x, double y, double z, double dX, double dY, double dZ, double hitDistance, int packedColor) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.dX = dX;
        this.dY = dY;
        this.dZ = dZ;
        this.hitDistance = hitDistance;
        this.packedColor = packedColor;
    }

    public static void encode(MessageEmberRayFX msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.posX);
        buf.writeDouble(msg.posY);
        buf.writeDouble(msg.posZ);
        buf.writeDouble(msg.dX);
        buf.writeDouble(msg.dY);
        buf.writeDouble(msg.dZ);
        buf.writeDouble(msg.hitDistance);
        buf.writeInt(msg.packedColor);
    }

    public static MessageEmberRayFX decode(FriendlyByteBuf buf) {
        return new MessageEmberRayFX(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt());
    }

    public static void handle(MessageEmberRayFX msg, Supplier<NetworkEvent.Context> ctx) {
        if (((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isClient()) {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> spawnParticles(msg));
        }
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void spawnParticles(MessageEmberRayFX msg) {
        Level world = Minecraft.getInstance().level;
        double distance = Math.sqrt(msg.dX * msg.dX + msg.dY * msg.dY + msg.dZ * msg.dZ);
        double segments = distance * 4.0;
        Vector3f color = Misc.colorFromInt(msg.packedColor);
        GlowParticleOptions options = new GlowParticleOptions(color, 2.0F);
        for (double i = 0.0; i < segments; i++) {
            if (i >= msg.hitDistance * 4.0) {
                for (int k = 0; k < 80; k++) {
                    world.addParticle(options, msg.posX, msg.posY, msg.posZ, (double) (1.125F * (random.nextFloat() - 0.5F)), (double) (1.125F * (random.nextFloat() - 0.5F)), (double) (1.125F * (random.nextFloat() - 0.5F)));
                }
                break;
            }
            for (int j = 0; j < 5; j++) {
                msg.posX = msg.posX + 0.2 * msg.dX / segments;
                msg.posY = msg.posY + 0.2 * msg.dY / segments;
                msg.posZ = msg.posZ + 0.2 * msg.dZ / segments;
                world.addParticle(options, msg.posX, msg.posY, msg.posZ, 0.0, 1.0E-6, 0.0);
            }
        }
    }
}