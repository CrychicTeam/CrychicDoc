package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public class ClientboundStopSoundPacket implements Packet<ClientGamePacketListener> {

    private static final int HAS_SOURCE = 1;

    private static final int HAS_SOUND = 2;

    @Nullable
    private final ResourceLocation name;

    @Nullable
    private final SoundSource source;

    public ClientboundStopSoundPacket(@Nullable ResourceLocation resourceLocation0, @Nullable SoundSource soundSource1) {
        this.name = resourceLocation0;
        this.source = soundSource1;
    }

    public ClientboundStopSoundPacket(FriendlyByteBuf friendlyByteBuf0) {
        int $$1 = friendlyByteBuf0.readByte();
        if (($$1 & 1) > 0) {
            this.source = friendlyByteBuf0.readEnum(SoundSource.class);
        } else {
            this.source = null;
        }
        if (($$1 & 2) > 0) {
            this.name = friendlyByteBuf0.readResourceLocation();
        } else {
            this.name = null;
        }
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        if (this.source != null) {
            if (this.name != null) {
                friendlyByteBuf0.writeByte(3);
                friendlyByteBuf0.writeEnum(this.source);
                friendlyByteBuf0.writeResourceLocation(this.name);
            } else {
                friendlyByteBuf0.writeByte(1);
                friendlyByteBuf0.writeEnum(this.source);
            }
        } else if (this.name != null) {
            friendlyByteBuf0.writeByte(2);
            friendlyByteBuf0.writeResourceLocation(this.name);
        } else {
            friendlyByteBuf0.writeByte(0);
        }
    }

    @Nullable
    public ResourceLocation getName() {
        return this.name;
    }

    @Nullable
    public SoundSource getSource() {
        return this.source;
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleStopSoundEvent(this);
    }
}