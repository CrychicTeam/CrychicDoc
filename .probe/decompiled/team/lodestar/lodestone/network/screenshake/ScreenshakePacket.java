package team.lodestar.lodestone.network.screenshake;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.network.LodestoneClientPacket;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

public class ScreenshakePacket extends LodestoneClientPacket {

    public final int duration;

    public float intensity1;

    public float intensity2;

    public float intensity3;

    public Easing intensityCurveStartEasing = Easing.LINEAR;

    public Easing intensityCurveEndEasing = Easing.LINEAR;

    public ScreenshakePacket(int duration) {
        this.duration = duration;
    }

    public ScreenshakePacket setIntensity(float intensity) {
        return this.setIntensity(intensity, intensity);
    }

    public ScreenshakePacket setIntensity(float intensity1, float intensity2) {
        return this.setIntensity(intensity1, intensity2, intensity2);
    }

    public ScreenshakePacket setIntensity(float intensity1, float intensity2, float intensity3) {
        this.intensity1 = intensity1;
        this.intensity2 = intensity2;
        this.intensity3 = intensity3;
        return this;
    }

    public ScreenshakePacket setEasing(Easing easing) {
        this.intensityCurveStartEasing = easing;
        this.intensityCurveEndEasing = easing;
        return this;
    }

    public ScreenshakePacket setEasing(Easing intensityCurveStartEasing, Easing intensityCurveEndEasing) {
        this.intensityCurveStartEasing = intensityCurveStartEasing;
        this.intensityCurveEndEasing = intensityCurveEndEasing;
        return this;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.duration);
        buf.writeFloat(this.intensity1);
        buf.writeFloat(this.intensity2);
        buf.writeFloat(this.intensity3);
        buf.writeUtf(this.intensityCurveStartEasing.name);
        buf.writeUtf(this.intensityCurveEndEasing.name);
    }

    @Override
    public void execute(Supplier<NetworkEvent.Context> context) {
        ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(this.duration).setIntensity(this.intensity1, this.intensity2, this.intensity3).setEasing(this.intensityCurveStartEasing, this.intensityCurveEndEasing));
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, ScreenshakePacket.class, ScreenshakePacket::encode, ScreenshakePacket::decode, LodestoneClientPacket::handle);
    }

    public static ScreenshakePacket decode(FriendlyByteBuf buf) {
        return new ScreenshakePacket(buf.readInt()).setIntensity(buf.readFloat(), buf.readFloat(), buf.readFloat()).setEasing(Easing.valueOf(buf.readUtf()), Easing.valueOf(buf.readUtf()));
    }
}