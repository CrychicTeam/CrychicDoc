package com.mna.entities.rituals;

import com.mna.api.config.GeneralConfigValues;
import com.mna.api.particles.ParticleInit;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.tools.math.MathUtils;
import com.mna.tools.math.Vector3;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class TimeChangeBall extends Entity {

    private static final EntityDataAccessor<Byte> ENTITY_STATE = SynchedEntityData.defineId(TimeChangeBall.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Byte> TIME_CHANGE_TYPE = SynchedEntityData.defineId(TimeChangeBall.class, EntityDataSerializers.BYTE);

    private static final Byte STATE_WAITING = (byte) 0;

    private static final Byte STATE_SHOOTING = (byte) 1;

    public static final int WAIT_TIME = 120;

    public static final int LERP_TIME = 100;

    public static final Byte TIME_CHANGE_DAY = (byte) 0;

    public static final Byte TIME_CHANGE_NIGHT = (byte) 1;

    private static final String EVENT_ID = "timeChange";

    private int age;

    Vector3 startPoint;

    Vector3 endPoint;

    Vector3 controlPoint1;

    Vector3 controlPoint2;

    Vec3 lastPosition;

    boolean pointsSet = false;

    public TimeChangeBall(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
        this.m_20331_(true);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public void tick() {
        this.setAge(this.getAge() + 1);
        if (this.m_9236_().isClientSide()) {
            int max_particles_per_tick = 5;
            int numParticles = Math.max((int) Math.ceil((double) ((float) this.getAge() / 120.0F * (float) max_particles_per_tick)), max_particles_per_tick);
            float radiusScale = 0.25F;
            for (int i = 0; i < numParticles; i++) {
                this.m_9236_().addParticle(ParticleInit.SPARKLE_VELOCITY.get(), true, this.m_20185_() + (Math.random() - 0.5) * (double) radiusScale, this.m_20186_() + (Math.random() - 0.5) * (double) radiusScale, this.m_20189_() + (Math.random() - 0.5) * (double) radiusScale, 0.0, -0.5, 0.0);
            }
        }
        if (this.getAge() < 120) {
            float f = (float) this.getAge() / 120.0F;
            this.m_20334_((double) (1.0F + f), (double) (-1.0F - f * 9.0F), (double) (1.0F + f));
        } else {
            if (this.getAge() >= 120 && this.getState() != STATE_SHOOTING) {
                this.setState(STATE_SHOOTING);
                if (!this.m_9236_().isClientSide()) {
                    this.queueTimeChange();
                }
            }
            if (this.getState() == STATE_SHOOTING) {
                if (!this.pointsSet) {
                    this.setPoints();
                }
                float f = ((float) this.getAge() - 120.0F) / 100.0F;
                float f1 = ((float) this.getAge() + 1.0F - 120.0F) / 100.0F;
                this.lastPosition = this.m_20182_();
                Vector3 curPoint = Vector3.bezier(this.startPoint, this.endPoint, this.controlPoint1, this.controlPoint2, f);
                Vector3 nextPoint = Vector3.bezier(this.startPoint, this.endPoint, this.controlPoint1, this.controlPoint2, f1);
                this.m_6034_((double) curPoint.x, (double) curPoint.y, (double) curPoint.z);
                Vector3 diff = nextPoint.sub(curPoint).normalize();
                float angle = (float) (Math.atan2((double) diff.x, (double) diff.y) * 180.0 / Math.PI);
                this.m_20334_(2.0, -10.0, 2.0);
                this.m_146926_(angle);
                if (this.getAge() >= 220) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private boolean queueTimeChange() {
        int targetTime = -1;
        long wrappedWorldTime = this.m_9236_().getDayTime() % 24000L;
        if (this.m_9236_().isDay() && this.getTimeChangeType() == TIME_CHANGE_NIGHT) {
            targetTime = (int) (13000L - wrappedWorldTime);
        } else if (this.m_9236_().isNight() && this.getTimeChangeType() == TIME_CHANGE_DAY) {
            targetTime = 24000 - (int) wrappedWorldTime;
        }
        if (targetTime == -1) {
            return false;
        } else if (!DelayedEventQueue.hasEvent(this.m_9236_(), "timeChange")) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("timeChange", 100, new Pair((ServerLevel) this.m_9236_(), targetTime), TimeChangeBall::flipDayNight));
            return true;
        } else {
            return false;
        }
    }

    private static void flipDayNight(String identifier, Pair<ServerLevel, Integer> data) {
        ServerLevel world = (ServerLevel) data.getFirst();
        int timeRemaining = (Integer) data.getSecond();
        int shift = GeneralConfigValues.GradualTimeChange ? MathUtils.clamp(timeRemaining, 0, 100) : timeRemaining;
        if (shift != 0) {
            world.setDayTime(world.m_46468_() + (long) shift);
            ClientboundSetTimePacket pkt = new ClientboundSetTimePacket(world.m_46467_(), world.m_46468_(), world.m_46469_().getBoolean(GameRules.RULE_DAYLIGHT));
            world.getServer().getPlayerList().broadcastAll(pkt, world.m_46472_());
            DelayedEventQueue.pushEvent(world, new TimedDelayedEvent<>(identifier, 3, new Pair(world, timeRemaining - shift), TimeChangeBall::flipDayNight));
        }
    }

    private void setPoints() {
        this.startPoint = new Vector3(this);
        this.endPoint = this.getCelestialPosition();
        this.controlPoint1 = Vector3.lerp(this.startPoint, this.endPoint, 0.25F);
        this.controlPoint2 = Vector3.lerp(this.startPoint, this.endPoint, 0.75F);
        this.controlPoint1.y = this.endPoint.y;
        this.controlPoint2.y = this.endPoint.y;
        this.pointsSet = true;
    }

    private Vector3 getCelestialPosition() {
        float celestialAngleRads = this.m_9236_().getSunAngle(0.0F) + (float) (Math.PI / 2);
        if (this.getTimeChangeType() == TIME_CHANGE_DAY) {
            celestialAngleRads = (float) ((double) celestialAngleRads + Math.PI);
        }
        double cAngle = Math.cos((double) celestialAngleRads);
        double sAngle = Math.sin((double) celestialAngleRads);
        float distance = 150.0F;
        return new Vector3(this.m_20185_() + cAngle * (double) distance, this.m_20186_() + sAngle * (double) distance, this.m_20189_());
    }

    public void setTimeChangeType(byte type) {
        this.f_19804_.set(TIME_CHANGE_TYPE, type);
    }

    public byte getTimeChangeType() {
        return this.f_19804_.get(TIME_CHANGE_TYPE);
    }

    public byte getState() {
        return this.f_19804_.get(ENTITY_STATE);
    }

    private void setState(byte state) {
        this.f_19804_.set(ENTITY_STATE, state);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(ENTITY_STATE, STATE_WAITING);
        this.f_19804_.define(TIME_CHANGE_TYPE, TIME_CHANGE_DAY);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void checkDespawn() {
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}