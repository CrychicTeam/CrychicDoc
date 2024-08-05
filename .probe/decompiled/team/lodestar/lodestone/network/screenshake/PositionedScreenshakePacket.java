package team.lodestar.lodestone.network.screenshake;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.screenshake.PositionedScreenshakeInstance;

public class PositionedScreenshakePacket extends ScreenshakePacket {

    public final Vec3 position;

    public final float falloffDistance;

    public final float maxDistance;

    public final Easing falloffEasing;

    public PositionedScreenshakePacket(int duration, Vec3 position, float falloffDistance, float maxDistance, Easing falloffEasing) {
        super(duration);
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.falloffEasing = falloffEasing;
    }

    public PositionedScreenshakePacket(int duration, Vec3 position, float falloffDistance, float maxDistance) {
        this(duration, position, falloffDistance, maxDistance, Easing.LINEAR);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(this.duration, this.position, this.falloffDistance, this.maxDistance, this.falloffEasing).setIntensity(this.intensity1, this.intensity2, this.intensity3).setEasing(this.intensityCurveStartEasing, this.intensityCurveEndEasing));
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.duration);
        buf.writeDouble(this.position.x);
        buf.writeDouble(this.position.y);
        buf.writeDouble(this.position.z);
        buf.writeFloat(this.falloffDistance);
        buf.writeFloat(this.maxDistance);
        buf.writeUtf(this.falloffEasing.name);
        buf.writeFloat(this.intensity1);
        buf.writeFloat(this.intensity2);
        buf.writeFloat(this.intensity3);
        buf.writeUtf(this.intensityCurveStartEasing.name);
        buf.writeUtf(this.intensityCurveEndEasing.name);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PositionedScreenshakePacket.class, PositionedScreenshakePacket::encode, PositionedScreenshakePacket::decode, LodestoneClientPacket::handle);
    }

    public static PositionedScreenshakePacket decode(FriendlyByteBuf buf) {
        return (PositionedScreenshakePacket) new PositionedScreenshakePacket(buf.readInt(), new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readFloat(), buf.readFloat(), Easing.valueOf(buf.readUtf())).setIntensity(buf.readFloat(), buf.readFloat(), buf.readFloat()).setEasing(Easing.valueOf(buf.readUtf()), Easing.valueOf(buf.readUtf()));
    }
}