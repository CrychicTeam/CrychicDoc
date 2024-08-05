package net.minecraft.network.protocol.game;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class ClientboundSoundPacket implements Packet<ClientGamePacketListener> {

    public static final float LOCATION_ACCURACY = 8.0F;

    private final Holder<SoundEvent> sound;

    private final SoundSource source;

    private final int x;

    private final int y;

    private final int z;

    private final float volume;

    private final float pitch;

    private final long seed;

    public ClientboundSoundPacket(Holder<SoundEvent> holderSoundEvent0, SoundSource soundSource1, double double2, double double3, double double4, float float5, float float6, long long7) {
        this.sound = holderSoundEvent0;
        this.source = soundSource1;
        this.x = (int) (double2 * 8.0);
        this.y = (int) (double3 * 8.0);
        this.z = (int) (double4 * 8.0);
        this.volume = float5;
        this.pitch = float6;
        this.seed = long7;
    }

    public ClientboundSoundPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.sound = friendlyByteBuf0.readById(BuiltInRegistries.SOUND_EVENT.asHolderIdMap(), SoundEvent::m_263214_);
        this.source = friendlyByteBuf0.readEnum(SoundSource.class);
        this.x = friendlyByteBuf0.readInt();
        this.y = friendlyByteBuf0.readInt();
        this.z = friendlyByteBuf0.readInt();
        this.volume = friendlyByteBuf0.readFloat();
        this.pitch = friendlyByteBuf0.readFloat();
        this.seed = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeId(BuiltInRegistries.SOUND_EVENT.asHolderIdMap(), this.sound, (p_263422_, p_263402_) -> p_263402_.writeToNetwork(p_263422_));
        friendlyByteBuf0.writeEnum(this.source);
        friendlyByteBuf0.writeInt(this.x);
        friendlyByteBuf0.writeInt(this.y);
        friendlyByteBuf0.writeInt(this.z);
        friendlyByteBuf0.writeFloat(this.volume);
        friendlyByteBuf0.writeFloat(this.pitch);
        friendlyByteBuf0.writeLong(this.seed);
    }

    public Holder<SoundEvent> getSound() {
        return this.sound;
    }

    public SoundSource getSource() {
        return this.source;
    }

    public double getX() {
        return (double) ((float) this.x / 8.0F);
    }

    public double getY() {
        return (double) ((float) this.y / 8.0F);
    }

    public double getZ() {
        return (double) ((float) this.z / 8.0F);
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public long getSeed() {
        return this.seed;
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleSoundEvent(this);
    }
}