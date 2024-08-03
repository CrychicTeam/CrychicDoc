package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.entities.constructs.animated.Construct;
import com.mna.network.messages.BaseMessage;
import com.mna.tools.ParticleConfigurations;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpawnParticleEffectMessage extends BaseMessage {

    Vec3 position;

    SpawnParticleEffectMessage.ParticleTypes particleType;

    CompoundTag particleMeta;

    public SpawnParticleEffectMessage(double x, double y, double z, SpawnParticleEffectMessage.ParticleTypes type) {
        this(x, y, z, type, null);
    }

    public SpawnParticleEffectMessage(double x, double y, double z, SpawnParticleEffectMessage.ParticleTypes type, CompoundTag meta) {
        this.position = new Vec3(x, y, z);
        this.particleType = type;
        this.particleMeta = meta;
        this.messageIsValid = true;
    }

    public SpawnParticleEffectMessage() {
        this.messageIsValid = false;
    }

    public Vec3 getPosition() {
        return this.position;
    }

    public SpawnParticleEffectMessage.ParticleTypes getType() {
        return this.particleType;
    }

    @Nullable
    public CompoundTag getMeta() {
        return this.particleMeta;
    }

    public static SpawnParticleEffectMessage decode(FriendlyByteBuf buf) {
        SpawnParticleEffectMessage msg = new SpawnParticleEffectMessage();
        try {
            msg.particleType = (SpawnParticleEffectMessage.ParticleTypes) SpawnParticleEffectMessage.ParticleTypes.valueOf(SpawnParticleEffectMessage.ParticleTypes.class, buf.readUtf(32767));
            msg.position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
            msg.particleMeta = buf.readAnySizeNbt();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading SpawnParticleEffectMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(SpawnParticleEffectMessage message, FriendlyByteBuf buf) {
        buf.writeUtf(message.getType().name());
        buf.writeDouble(message.getPosition().x);
        buf.writeDouble(message.getPosition().y);
        buf.writeDouble(message.getPosition().z);
        buf.writeNbt(message.particleMeta);
    }

    public void handle(Level world) {
        switch(this.particleType) {
            case MANAWEAVE_CRAFT_COMPLETE:
                ParticleConfigurations.ArcaneParticleBurst(world, this.position);
                break;
            case ENSORCELLATION_ITEM_PULL:
                ParticleConfigurations.ItemPullParticle(world, new Vec3(this.position.x, this.position.y, this.position.z), this.particleMeta);
                break;
            case CONSTRUCT_HAMMER_SMASH:
                Construct.SpawnSmashParticles(world, this.position);
                break;
            case CONSTRUCT_AXE_SMASH:
                Construct.SpawnCritParticles(world, this.position, this.particleMeta);
        }
    }

    public static enum ParticleTypes {

        MANAWEAVE_CRAFT_COMPLETE, ENSORCELLATION_ITEM_PULL, CONSTRUCT_HAMMER_SMASH, CONSTRUCT_AXE_SMASH
    }
}