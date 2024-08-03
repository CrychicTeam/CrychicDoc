package noppes.npcs.packets.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import noppes.npcs.shared.common.PacketBasic;

public class PacketParticle extends PacketBasic {

    private final double posX;

    private final double posY;

    private final double posZ;

    private final float height;

    private final float width;

    private final String name;

    public PacketParticle(double posX, double posY, double posZ, float height, float width, String name) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.height = height;
        this.width = width;
        this.name = name;
    }

    public static void encode(PacketParticle msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.posX);
        buf.writeDouble(msg.posY);
        buf.writeDouble(msg.posZ);
        buf.writeFloat(msg.height);
        buf.writeFloat(msg.width);
        buf.writeUtf(msg.name);
    }

    public static PacketParticle decode(FriendlyByteBuf buf) {
        return new PacketParticle(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readFloat(), buf.readFloat(), buf.readUtf(32767));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handle() {
        Level world = Minecraft.getInstance().level;
        RandomSource rand = world.random;
        if (this.name.equals("heal")) {
            for (int k = 0; k < 6; k++) {
                world.addParticle(ParticleTypes.INSTANT_EFFECT, this.posX + (rand.nextDouble() - 0.5) * (double) this.width, this.posY + rand.nextDouble() * (double) this.height, this.posZ + (rand.nextDouble() - 0.5) * (double) this.width, 0.0, 0.0, 0.0);
                world.addParticle(ParticleTypes.EFFECT, this.posX + (rand.nextDouble() - 0.5) * (double) this.width, this.posY + rand.nextDouble() * (double) this.height, this.posZ + (rand.nextDouble() - 0.5) * (double) this.width, 0.0, 0.0, 0.0);
            }
        }
    }
}