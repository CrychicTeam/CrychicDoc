package com.mna.api.particles;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.particles.parameters.ParticleBoolean;
import com.mna.api.particles.parameters.ParticleColor;
import com.mna.api.particles.parameters.ParticleFloat;
import com.mna.api.particles.parameters.ParticleInt;
import com.mna.api.particles.parameters.ParticleItemStack;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class MAParticleType extends ParticleType<MAParticleType> implements ParticleOptions {

    private ParticleType<MAParticleType> type;

    private ParticleColor color;

    private ParticleFloat scale;

    private ParticleInt life;

    private ParticleInt lifePadding;

    private ParticleFloat gravity;

    private ParticleBoolean physics;

    private IParticleMoveType mover;

    private ParticleItemStack stack;

    public boolean disableDepthTest;

    public static final Codec<MAParticleType> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.FLOAT.fieldOf("r").forGetter(d -> d.color != null ? d.color.getRed() : 1.0F), Codec.FLOAT.fieldOf("g").forGetter(d -> d.color != null ? d.color.getGreen() : 1.0F), Codec.FLOAT.fieldOf("b").forGetter(d -> d.color != null ? d.color.getBlue() : 1.0F), Codec.FLOAT.fieldOf("a").forGetter(d -> d.color != null ? d.color.getAlpha() : 1.0F), Codec.FLOAT.fieldOf("scale").forGetter(d -> d.scale != null ? d.scale.value() : 1.0F), Codec.INT.fieldOf("life").forGetter(d -> d.life != null ? d.life.value() : 20), Codec.INT.fieldOf("lifePadding").forGetter(d -> d.lifePadding != null ? d.lifePadding.value() : 0), Codec.FLOAT.fieldOf("gravity").forGetter(d -> d.gravity != null ? d.gravity.value() : 0.0F), Codec.BOOL.fieldOf("physics").forGetter(d -> d.physics != null ? d.physics.value() : false), Codec.BOOL.fieldOf("disableDepthTest").forGetter(d -> d.disableDepthTest)).apply(instance, MAParticleType::new));

    static final ParticleOptions.Deserializer<MAParticleType> DESERIALIZER = new ParticleOptions.Deserializer<MAParticleType>() {

        public MAParticleType fromCommand(ParticleType<MAParticleType> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            String colorString = null;
            String scaleString = null;
            String lifeString = null;
            String lifePaddingString = null;
            String gravityString = null;
            String physString = null;
            String stackString = null;
            try {
                colorString = reader.readString();
            } catch (Exception var18) {
            }
            try {
                scaleString = reader.readString();
            } catch (Exception var17) {
            }
            try {
                lifeString = reader.readString();
            } catch (Exception var16) {
            }
            try {
                lifePaddingString = reader.readString();
            } catch (Exception var15) {
            }
            try {
                gravityString = reader.readString();
            } catch (Exception var14) {
            }
            try {
                physString = reader.readString();
            } catch (Exception var13) {
            }
            try {
                stackString = reader.readString();
            } catch (Exception var12) {
            }
            MAParticleType deserialized = new MAParticleType(type);
            if (colorString != null) {
                ParticleColor color = ParticleColor.deserialize(colorString);
                if (color != null) {
                    deserialized.setColor(color.getRed(), color.getGreen(), color.getBlue());
                }
            }
            if (scaleString != null) {
                ParticleFloat scale = ParticleFloat.deserialize(scaleString);
                if (scale != null) {
                    deserialized.setScale(scale.value());
                }
            }
            if (lifeString != null) {
                ParticleInt life = ParticleInt.deserialize(lifeString);
                if (life != null) {
                    deserialized.setMaxAge(life.value());
                }
            }
            if (lifePaddingString != null) {
                ParticleInt lifePadding = ParticleInt.deserialize(lifePaddingString);
                if (lifePadding != null) {
                    deserialized.setAgePadding(lifePadding.value());
                }
            }
            if (gravityString != null) {
                ParticleFloat gravity = ParticleFloat.deserialize(gravityString);
                if (gravity != null) {
                    deserialized.setGravity(gravity.value());
                }
            }
            if (physString != null) {
                ParticleBoolean phys = ParticleBoolean.deserialize(physString);
                if (phys != null) {
                    deserialized.setPhysics(phys.value());
                }
            }
            if (stackString != null) {
                ParticleItemStack stack = ParticleItemStack.deserialize(stackString);
                if (stack != null) {
                    deserialized.setStack(stack.value());
                }
            }
            return deserialized;
        }

        public MAParticleType fromNetwork(ParticleType<MAParticleType> type, FriendlyByteBuf buffer) {
            ParticleColor color = null;
            if (buffer.readBoolean()) {
                color = ParticleColor.deserialize(buffer.readUtf(32767));
            }
            return new MAParticleType(type, color, ParticleFloat.deserialize(buffer), ParticleInt.deserialize(buffer), ParticleInt.deserialize(buffer), ParticleFloat.deserialize(buffer), ParticleBoolean.deserialize(buffer), ParticleItemStack.deserialize(buffer), ManaAndArtificeMod.getParticleSerializationHelper().fromID(buffer.readInt()).deserialize(buffer), buffer.readBoolean());
        }
    };

    public MAParticleType() {
        super(false, DESERIALIZER);
    }

    public MAParticleType(ParticleType<MAParticleType> type) {
        this();
        this.type = type;
    }

    private MAParticleType(float r, float g, float b, float a, float scale, int life, int lifePadding, float gravity, boolean physics, boolean disableDepth) {
        super(disableDepth, DESERIALIZER);
        this.setColor(r, g, b, a);
        this.setScale(scale);
        this.setGravity(gravity);
        this.setPhysics(physics);
        this.setMaxAge(life);
    }

    private MAParticleType(ParticleType<MAParticleType> type, ParticleColor color, ParticleFloat scale, ParticleInt life, ParticleInt lifePadding, ParticleFloat gravity, ParticleBoolean physics, ParticleItemStack stack, IParticleMoveType mover, boolean disableDepth) {
        super(disableDepth, DESERIALIZER);
        this.color = color;
        this.scale = scale;
        this.life = life;
        this.lifePadding = lifePadding;
        this.gravity = gravity;
        this.physics = physics;
        this.mover = mover;
    }

    @Override
    public Codec<MAParticleType> codec() {
        return CODEC;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf packetBuffer) {
        if (this.color != null) {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeUtf(this.color.serialize());
        } else {
            packetBuffer.writeBoolean(false);
        }
        ParticleFloat.serialize(this.scale, packetBuffer);
        ParticleInt.serialize(this.life, packetBuffer);
        ParticleInt.serialize(this.lifePadding, packetBuffer);
        ParticleFloat.serialize(this.gravity, packetBuffer);
        ParticleBoolean.serialize(this.physics, packetBuffer);
        if (this.mover != null) {
            packetBuffer.writeBoolean(true);
            this.mover.serialize(packetBuffer);
        } else {
            packetBuffer.writeBoolean(false);
        }
        packetBuffer.writeBoolean(this.disableDepthTest);
    }

    @Override
    public String writeToString() {
        return this.getType().toString() + " " + (this.color != null ? this.color.serialize() : "NO COLOR OVERRIDE") + " " + (this.scale != null ? this.scale.serialize() : "NO SCALE OVERRIDE") + (this.life != null ? this.life.serialize() : "NO LIFE OVERRIDE") + (this.lifePadding != null ? this.lifePadding.serialize() : "NO LIFE PADDING") + (this.mover != null ? this.mover.serialize() : "NO MOVE OVERRIDE");
    }

    public MAParticleType setColor(float r, float g, float b, float a) {
        this.color = new ParticleColor(r, g, b, a);
        return this;
    }

    public MAParticleType setColor(float r, float g, float b) {
        this.color = new ParticleColor(r, g, b, 255.0F);
        return this;
    }

    public MAParticleType setColor(double r, double g, double b, double a) {
        this.color = new ParticleColor((float) r, (float) g, (float) b, (float) a);
        return this;
    }

    public MAParticleType setColor(double r, double g, double b) {
        this.color = new ParticleColor((float) r, (float) g, (float) b, 255.0F);
        return this;
    }

    public MAParticleType setColor(int r, int g, int b) {
        this.color = new ParticleColor((float) r, (float) g, (float) b, 255.0F);
        return this;
    }

    public MAParticleType setColor(int r, int g, int b, int a) {
        this.color = new ParticleColor(r, g, b, a);
        return this;
    }

    public MAParticleType setScale(float scale) {
        this.scale = new ParticleFloat(scale);
        return this;
    }

    public MAParticleType setMaxAge(int age) {
        this.life = new ParticleInt(age);
        return this;
    }

    public MAParticleType setAgePadding(int padding) {
        this.lifePadding = new ParticleInt(padding);
        return this;
    }

    public MAParticleType setGravity(float gravity) {
        this.gravity = new ParticleFloat(gravity);
        return this;
    }

    public MAParticleType setPhysics(boolean physics) {
        this.physics = new ParticleBoolean(physics);
        return this;
    }

    public MAParticleType setStack(ItemStack stack) {
        this.stack = new ParticleItemStack(stack);
        return this;
    }

    public MAParticleType setMover(IParticleMoveType mover) {
        this.mover = mover;
        return this;
    }

    @Nullable
    public IParticleMoveType getMover() {
        return this.mover;
    }

    @Nullable
    public ParticleColor getColor() {
        return this.color;
    }

    @Nullable
    public ParticleFloat getScale() {
        return this.scale;
    }

    @Nullable
    public ParticleInt getLife() {
        return this.life;
    }

    @Nullable
    public ParticleInt getLifePadding() {
        return this.lifePadding;
    }

    @Nullable
    public ParticleFloat getGravity() {
        return this.gravity;
    }

    @Nullable
    public ParticleBoolean getPhysics() {
        return this.physics;
    }

    @Nullable
    public ParticleItemStack getStack() {
        return this.stack;
    }

    @Override
    public ParticleType<MAParticleType> getType() {
        return (ParticleType<MAParticleType>) (this.type == null ? ParticleInit.BLUE_SPARKLE_VELOCITY.get() : this.type);
    }
}