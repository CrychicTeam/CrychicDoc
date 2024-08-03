package net.minecraft.network.protocol.game;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class ClientboundSoundEntityPacket implements Packet<ClientGamePacketListener> {

    private final Holder<SoundEvent> sound;

    private final SoundSource source;

    private final int id;

    private final float volume;

    private final float pitch;

    private final long seed;

    public ClientboundSoundEntityPacket(Holder<SoundEvent> holderSoundEvent0, SoundSource soundSource1, Entity entity2, float float3, float float4, long long5) {
        this.sound = holderSoundEvent0;
        this.source = soundSource1;
        this.id = entity2.getId();
        this.volume = float3;
        this.pitch = float4;
        this.seed = long5;
    }

    public ClientboundSoundEntityPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.sound = friendlyByteBuf0.readById(BuiltInRegistries.SOUND_EVENT.asHolderIdMap(), SoundEvent::m_263214_);
        this.source = friendlyByteBuf0.readEnum(SoundSource.class);
        this.id = friendlyByteBuf0.readVarInt();
        this.volume = friendlyByteBuf0.readFloat();
        this.pitch = friendlyByteBuf0.readFloat();
        this.seed = friendlyByteBuf0.readLong();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeId(BuiltInRegistries.SOUND_EVENT.asHolderIdMap(), this.sound, (p_263534_, p_263498_) -> p_263498_.writeToNetwork(p_263534_));
        friendlyByteBuf0.writeEnum(this.source);
        friendlyByteBuf0.writeVarInt(this.id);
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

    public int getId() {
        return this.id;
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
        clientGamePacketListener0.handleSoundEntityEvent(this);
    }
}