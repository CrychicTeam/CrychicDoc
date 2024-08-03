package com.mna.particles.emitter;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.particles.types.movers.ParticleBezierMover;
import com.mna.particles.types.movers.ParticleLerpMover;
import com.mna.particles.types.movers.ParticleOrbitMover;
import com.mna.particles.types.movers.ParticleSphereOrbitMover;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class EmitterData {

    public int[] rgbStart = new int[] { -1, -1, -1, 255 };

    public float[] velocity = new float[] { 0.0F, 0.0F, 0.0F };

    public float[] offset = new float[] { 0.0F, 0.0F, 0.0F };

    public float[] target = new float[] { 0.0F, 0.0F, 0.0F };

    public float[] spread = new float[] { 0.0F, 0.0F, 0.0F };

    public float scale = 0.25F;

    public float gravity;

    public int amount = 1;

    public int rate = 1;

    public int age;

    public boolean randomColor;

    public boolean randomSpeed;

    public boolean randomTarget;

    public boolean randomScale;

    public boolean collision;

    public boolean mirror;

    public boolean enabled;

    public boolean showInFirstPerson;

    public EmitterData.ParticleControllerTypes controller = EmitterData.ParticleControllerTypes.VELOCITY;

    public EmitterData.ParticleTypes type = EmitterData.ParticleTypes.FLAME;

    public EmitterData() {
        this.enabled = false;
    }

    public CompoundTag getTag() {
        CompoundTag tag = new CompoundTag();
        tag.putIntArray("rgbStart", this.rgbStart);
        this.writeFloatArray(tag, "velocity", this.velocity);
        this.writeFloatArray(tag, "offset", this.offset);
        this.writeFloatArray(tag, "target", this.target);
        this.writeFloatArray(tag, "spread", this.spread);
        tag.putFloat("scale", this.scale);
        tag.putFloat("gravity", this.gravity);
        tag.putInt("amount", this.amount);
        tag.putInt("age", this.age);
        tag.putInt("rate", this.rate);
        tag.putBoolean("randomColor", this.randomColor);
        tag.putBoolean("randomSpeed", this.randomSpeed);
        tag.putBoolean("randomTarget", this.randomTarget);
        tag.putBoolean("randomScale", this.randomScale);
        tag.putBoolean("collision", this.collision);
        tag.putBoolean("mirror", this.mirror);
        tag.putBoolean("enabled", this.enabled);
        tag.putBoolean("showInFirstPerson", this.showInFirstPerson);
        tag.putString("controller", this.controller.name());
        tag.putString("type", this.type.name());
        return tag;
    }

    public static EmitterData fromTag(CompoundTag tag) {
        EmitterData inst = new EmitterData();
        if (tag.contains("rgbStart")) {
            inst.rgbStart = tag.getIntArray("rgbStart");
        }
        if (inst.rgbStart.length < 4) {
            inst.rgbStart = new int[4];
        }
        inst.velocity = readFloatArray(tag, "velocity", 3);
        inst.offset = readFloatArray(tag, "offset", 3);
        inst.target = readFloatArray(tag, "target", 3);
        inst.spread = readFloatArray(tag, "spread", 3);
        if (tag.contains("scale")) {
            inst.scale = tag.getFloat("scale");
        }
        if (tag.contains("gravity")) {
            inst.gravity = tag.getFloat("gravity");
        }
        if (tag.contains("age")) {
            inst.age = tag.getInt("age");
        }
        if (tag.contains("rate")) {
            inst.rate = tag.getInt("rate");
        }
        if (tag.contains("amount")) {
            inst.amount = tag.getInt("amount");
        }
        if (tag.contains("randomColor")) {
            inst.randomColor = tag.getBoolean("randomColor");
        }
        if (tag.contains("randomSpeed")) {
            inst.randomSpeed = tag.getBoolean("randomSpeed");
        }
        if (tag.contains("randomTarget")) {
            inst.randomTarget = tag.getBoolean("randomTarget");
        }
        if (tag.contains("randomScale")) {
            inst.randomScale = tag.getBoolean("randomScale");
        }
        if (tag.contains("collision")) {
            inst.collision = tag.getBoolean("collision");
        }
        if (tag.contains("mirror")) {
            inst.mirror = tag.getBoolean("mirror");
        }
        if (tag.contains("enabled")) {
            inst.enabled = tag.getBoolean("enabled");
        }
        if (tag.contains("showInFirstPerson")) {
            inst.showInFirstPerson = tag.getBoolean("showInFirstPerson");
        }
        if (tag.contains("controller")) {
            inst.controller = EmitterData.ParticleControllerTypes.valueOf(tag.getString("controller"));
        }
        if (tag.contains("type")) {
            inst.type = EmitterData.ParticleTypes.valueOf(tag.getString("type"));
        }
        return inst;
    }

    @OnlyIn(Dist.CLIENT)
    public void spawn(Level world, Vec3 origin, Vec3 forward) {
        if (this.enabled && !Minecraft.getInstance().isPaused() && this.rate >= 1) {
            if (world.getGameTime() % (long) this.rate == 0L) {
                if (this.mirror) {
                    for (int i = 0; (double) i < Math.ceil((double) ((float) this.amount / 2.0F)); i++) {
                        this.spawnSingleParticle(world, origin, forward, false);
                        this.spawnSingleParticle(world, origin, forward, true);
                    }
                } else {
                    for (int i = 0; i < this.amount; i++) {
                        this.spawnSingleParticle(world, origin, forward, false);
                    }
                }
            }
        }
    }

    private void spawnSingleParticle(Level world, Vec3 origin, Vec3 forward, boolean mirror) {
        MAParticleType pfx = this.getPFX();
        if (pfx != null) {
            switch(this.controller) {
                case ORBIT:
                case RING:
                    forward = new Vec3(1.0, 0.0, 0.0);
                default:
                    if (this.randomColor) {
                        pfx.setColor(Math.random() * 255.0, Math.random() * 255.0, Math.random() * 255.0);
                    }
                    if (this.rgbStart.length == 3) {
                        pfx.setColor(this.rgbStart[0], this.rgbStart[1], this.rgbStart[2]);
                    } else if (this.rgbStart.length == 4) {
                        pfx.setColor(this.rgbStart[0], this.rgbStart[1], this.rgbStart[2], this.rgbStart[3]);
                    }
                    if (this.randomScale) {
                        pfx.setScale((float) Math.random() * this.scale);
                    } else {
                        pfx.setScale(this.scale);
                    }
                    pfx.setGravity(this.gravity / 50.0F);
                    if (this.age > 0) {
                        pfx.setMaxAge(this.age);
                    }
                    pfx.setPhysics(this.collision);
                    Vec3 side = forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
                    Vec3 paramOffset = this.computeVec3(this.offset, this.spread);
                    if (mirror) {
                        paramOffset = new Vec3(paramOffset.x * -1.0, paramOffset.y, paramOffset.z);
                    }
                    Vec3 xOffset = side.scale(paramOffset.z);
                    Vec3 zOffset = forward.scale(paramOffset.x);
                    Vec3 spawnPos = origin.add(0.0, paramOffset.y, 0.0).add(xOffset).add(zOffset);
                    Vec3 paramVel = this.computeVec3(this.velocity, this.randomSpeed);
                    if (mirror) {
                        paramVel = new Vec3(paramVel.x * -1.0, paramVel.y, paramVel.z);
                    }
                    Vec3 xVel = side.scale(paramVel.x);
                    Vec3 zVel = forward.scale(paramVel.z);
                    Vec3 vel = xVel.add(zVel).add(0.0, paramVel.y, 0.0);
                    if (this.type != EmitterData.ParticleTypes.LIGHTNING) {
                        switch(this.controller) {
                            case ORBIT:
                                pfx.setMover(new ParticleOrbitMover(spawnPos.x, spawnPos.y, spawnPos.z, vel.x, vel.y, vel.z));
                                break;
                            case RING:
                                pfx.setMover(new ParticleSphereOrbitMover(spawnPos.x, spawnPos.y, spawnPos.z, vel.x, vel.y * 180.0, vel.z));
                                break;
                            case BEZIER:
                                Vec3 targetPos = origin.add(this.computeVec3(this.target, this.randomTarget ? this.spread : null));
                                if (mirror) {
                                    targetPos = new Vec3(targetPos.x * -1.0, targetPos.y, targetPos.z);
                                }
                                pfx.setMover(new ParticleBezierMover(spawnPos, targetPos));
                                break;
                            case LERP:
                                Vec3 targetPos = origin.add(this.computeVec3(this.target, this.randomTarget ? this.spread : null));
                                if (mirror) {
                                    targetPos = new Vec3(targetPos.x * -1.0, targetPos.y, targetPos.z);
                                }
                                pfx.setMover(new ParticleLerpMover(spawnPos.x, spawnPos.y, spawnPos.z, targetPos.x, targetPos.y, targetPos.z));
                        }
                        world.addParticle(pfx, spawnPos.x, spawnPos.y, spawnPos.z, vel.x, vel.y, vel.z);
                    } else {
                        Vec3 targetPos = origin.add(this.computeVec3(this.target, this.randomTarget ? this.spread : null));
                        if (mirror) {
                            targetPos = new Vec3(targetPos.x * -1.0, targetPos.y, targetPos.z);
                        }
                        world.addParticle(pfx, spawnPos.x, spawnPos.y, spawnPos.z, targetPos.x, targetPos.y, targetPos.z);
                    }
            }
        }
    }

    private Vec3 computeVec3(float[] array, float[] spread) {
        return spread == null ? new Vec3((double) array[0], (double) array[1], (double) array[2]) : new Vec3((double) (-spread[0] + array[0]) + (double) (spread[0] * 2.0F) * Math.random(), (double) (-spread[1] + array[1]) + (double) (spread[1] * 2.0F) * Math.random(), (double) (-spread[2] + array[2]) + (double) (spread[2] * 2.0F) * Math.random());
    }

    private Vec3 computeVec3(float[] array, boolean random) {
        return random ? new Vec3((double) array[0] * Math.random(), (double) array[1] * Math.random(), (double) array[2] * Math.random()) : new Vec3((double) array[0], (double) array[1], (double) array[2]);
    }

    private static float[] readFloatArray(CompoundTag tag, String key, int minLength) {
        if (!tag.contains(key)) {
            return new float[minLength];
        } else {
            CompoundTag subElem = tag.getCompound(key);
            int len = subElem.getInt("length");
            float[] output = new float[Math.max(len, minLength)];
            for (int i = 0; i < len; i++) {
                output[i] = subElem.getFloat("e" + i);
            }
            return output;
        }
    }

    private void writeFloatArray(CompoundTag tag, String key, float[] value) {
        CompoundTag subElem = new CompoundTag();
        subElem.putInt("length", value.length);
        for (int i = 0; i < value.length; i++) {
            subElem.putFloat("e" + i, value[i]);
        }
        tag.put(key, subElem);
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    private MAParticleType getPFX() {
        switch(this.type) {
            case AIR:
                return new MAParticleType(ParticleInit.AIR_VELOCITY.get());
            case ARCANE:
                return new MAParticleType(ParticleInit.ARCANE.get());
            case BLUEFLAME:
                return new MAParticleType(ParticleInit.BLUE_FLAME.get());
            case BLUESPARKLE:
                return new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get());
            case BONE:
                return new MAParticleType(ParticleInit.BONE.get());
            case COZY_SMOKE:
                return new MAParticleType(ParticleInit.COZY_SMOKE.get());
            case DRIP:
                return new MAParticleType(ParticleInit.DRIP.get());
            case DUST:
                return new MAParticleType(ParticleInit.DUST.get());
            case EARTH:
                return new MAParticleType(ParticleInit.EARTH.get());
            case ENCHANT:
                return new MAParticleType(ParticleInit.ENCHANT.get());
            case ENDER:
                return new MAParticleType(ParticleInit.ENDER_VELOCITY.get());
            case FLAME:
                return new MAParticleType(ParticleInit.FLAME.get());
            case FROST:
                return new MAParticleType(ParticleInit.FROST.get());
            case GLOW:
                return new MAParticleType(ParticleInit.GLOW.get());
            case HEART:
                return new MAParticleType(ParticleInit.HEART.get());
            case LIGHTNING:
                return new MAParticleType(ParticleInit.LIGHTNING_BOLT.get());
            case MAGELIGHT:
                return new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get());
            case SOUL:
                return new MAParticleType(ParticleInit.SOUL.get());
            case SPARKLE:
                return new MAParticleType(ParticleInit.SPARKLE_VELOCITY.get());
            case TRAIL:
                return new MAParticleType(ParticleInit.TRAIL_VELOCITY.get());
            case WATER:
                return new MAParticleType(ParticleInit.WATER.get());
            default:
                return null;
        }
    }

    public static enum ParticleControllerTypes {

        VELOCITY, ORBIT, RING, LERP, BEZIER
    }

    public static enum ParticleTypes {

        AIR,
        ARCANE,
        BLUEFLAME,
        BLUESPARKLE,
        BONE,
        COZY_SMOKE,
        DRIP,
        DUST,
        EARTH,
        ENCHANT,
        ENDER,
        FLAME,
        FROST,
        GLOW,
        HEART,
        LIGHTNING,
        MAGELIGHT,
        SOUL,
        SPARKLE,
        TRAIL,
        WATER
    }
}