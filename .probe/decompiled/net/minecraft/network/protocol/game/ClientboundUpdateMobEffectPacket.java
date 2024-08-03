package net.minecraft.network.protocol.game;

import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class ClientboundUpdateMobEffectPacket implements Packet<ClientGamePacketListener> {

    private static final int FLAG_AMBIENT = 1;

    private static final int FLAG_VISIBLE = 2;

    private static final int FLAG_SHOW_ICON = 4;

    private final int entityId;

    private final MobEffect effect;

    private final byte effectAmplifier;

    private final int effectDurationTicks;

    private final byte flags;

    @Nullable
    private final MobEffectInstance.FactorData factorData;

    public ClientboundUpdateMobEffectPacket(int int0, MobEffectInstance mobEffectInstance1) {
        this.entityId = int0;
        this.effect = mobEffectInstance1.getEffect();
        this.effectAmplifier = (byte) (mobEffectInstance1.getAmplifier() & 0xFF);
        this.effectDurationTicks = mobEffectInstance1.getDuration();
        byte $$2 = 0;
        if (mobEffectInstance1.isAmbient()) {
            $$2 = (byte) ($$2 | 1);
        }
        if (mobEffectInstance1.isVisible()) {
            $$2 = (byte) ($$2 | 2);
        }
        if (mobEffectInstance1.showIcon()) {
            $$2 = (byte) ($$2 | 4);
        }
        this.flags = $$2;
        this.factorData = (MobEffectInstance.FactorData) mobEffectInstance1.getFactorData().orElse(null);
    }

    public ClientboundUpdateMobEffectPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readVarInt();
        this.effect = friendlyByteBuf0.readById(BuiltInRegistries.MOB_EFFECT);
        this.effectAmplifier = friendlyByteBuf0.readByte();
        this.effectDurationTicks = friendlyByteBuf0.readVarInt();
        this.flags = friendlyByteBuf0.readByte();
        this.factorData = friendlyByteBuf0.readNullable(p_266628_ -> p_266628_.readWithCodec(NbtOps.INSTANCE, MobEffectInstance.FactorData.CODEC));
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entityId);
        friendlyByteBuf0.writeId(BuiltInRegistries.MOB_EFFECT, this.effect);
        friendlyByteBuf0.writeByte(this.effectAmplifier);
        friendlyByteBuf0.writeVarInt(this.effectDurationTicks);
        friendlyByteBuf0.writeByte(this.flags);
        friendlyByteBuf0.writeNullable(this.factorData, (p_266629_, p_266630_) -> p_266629_.writeWithCodec(NbtOps.INSTANCE, MobEffectInstance.FactorData.CODEC, p_266630_));
    }

    public void handle(ClientGamePacketListener clientGamePacketListener0) {
        clientGamePacketListener0.handleUpdateMobEffect(this);
    }

    public int getEntityId() {
        return this.entityId;
    }

    public MobEffect getEffect() {
        return this.effect;
    }

    public byte getEffectAmplifier() {
        return this.effectAmplifier;
    }

    public int getEffectDurationTicks() {
        return this.effectDurationTicks;
    }

    public boolean isEffectVisible() {
        return (this.flags & 2) == 2;
    }

    public boolean isEffectAmbient() {
        return (this.flags & 1) == 1;
    }

    public boolean effectShowsIcon() {
        return (this.flags & 4) == 4;
    }

    @Nullable
    public MobEffectInstance.FactorData getFactorData() {
        return this.factorData;
    }
}