package com.mna.entities.projectile;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.EntityInit;
import com.mna.factions.Factions;
import com.mna.tools.math.MathUtils;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class WitchhunterTrickshot extends Entity {

    protected static final EntityDataAccessor<String> EFFECT_ID = SynchedEntityData.defineId(WitchhunterTrickshot.class, EntityDataSerializers.STRING);

    protected static final EntityDataAccessor<CompoundTag> LERPDATA = SynchedEntityData.defineId(WitchhunterTrickshot.class, EntityDataSerializers.COMPOUND_TAG);

    private static final int maxAge = 20;

    private static final float arrowSpawnAge = 10.0F;

    private static final float radius = 4.0F;

    private int effectDuration;

    private int effectMagnitude;

    private Vec3 cachedOffset;

    private MobEffect cachedEffect;

    private Vec3 potion_control_1;

    private Vec3 potion_control_2;

    public WitchhunterTrickshot(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public WitchhunterTrickshot(Level world, Vec3 initialPosition, Vec3 destPos, MobEffect effect) {
        this(world, initialPosition, destPos, effect, 200, 0);
    }

    public WitchhunterTrickshot(Level world, Vec3 initialPosition, Vec3 destPos, MobEffect effect, int duration, int magnitude) {
        super(EntityInit.WITCH_HUNTER_TRICKSHOT.get(), world);
        this.m_6021_(initialPosition.x, initialPosition.y, initialPosition.z);
        this.setDestinationPoint(destPos);
        this.setEffect(effect);
        this.effectDuration = duration;
        this.effectMagnitude = magnitude;
    }

    @Override
    public void tick() {
        this.f_19797_++;
        if (this.m_9236_().isClientSide() && (float) this.f_19797_ == 10.0F) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.CROSSBOW_SHOOT, SoundSource.HOSTILE, 1.0F, (float) (0.9 + Math.random() * 0.2), false);
        }
        if (this.f_19797_ >= 20) {
            if (!this.m_9236_().isClientSide()) {
                this.applyEffect();
            }
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (this.m_9236_().isClientSide() && this.getEffect() != null) {
            this.m_9236_().m_46796_(2002, BlockPos.containing(this.getDestination()), this.getEffect().getColor());
        }
    }

    public boolean renderArrow() {
        return (float) this.f_19797_ > 10.0F;
    }

    public Vec3 getPotionPosition(float partialTicks) {
        if (!this.calculateControlPoints()) {
            return this.m_20182_();
        } else {
            float f = ((float) this.f_19797_ + partialTicks) / 20.0F;
            return MathUtils.bezierVector3d(this.m_20182_(), this.getDestination(), this.potion_control_1, this.potion_control_2, f);
        }
    }

    public Vec3 getArrowPosition(float partialTicks) {
        float f = MathUtils.clamp01(((float) this.f_19797_ + partialTicks - 10.0F) / 10.0F);
        return MathUtils.lerpVector3d(this.m_20182_(), this.getDestination(), f);
    }

    @Nullable
    public MobEffect getEffect() {
        if (this.cachedEffect == null) {
            ResourceLocation effectID = new ResourceLocation(this.f_19804_.get(EFFECT_ID));
            this.cachedEffect = ForgeRegistries.MOB_EFFECTS.getValue(effectID);
        }
        return this.cachedEffect;
    }

    private void applyEffect() {
        MobEffect effect = this.getEffect();
        if (effect != null) {
            for (LivingEntity e : (List) this.m_9236_().getEntities(this, new AABB(BlockPos.containing(this.getDestination())).inflate(4.0), ex -> {
                if (!ex.isAlive()) {
                    return false;
                } else if (ex instanceof Player p) {
                    IPlayerProgression prog = (IPlayerProgression) p.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    return prog == null ? false : prog.getAlliedFaction() != null && prog.getAlliedFaction() != Factions.COUNCIL;
                } else {
                    return !(ex instanceof IFactionEnemy) ? false : ((IFactionEnemy) ex).getFaction() != null && ((IFactionEnemy) ex).getFaction() != Factions.COUNCIL;
                }
            }).stream().map(ex -> (LivingEntity) ex).collect(Collectors.toList())) {
                e.addEffect(new MobEffectInstance(effect, this.effectDuration, this.effectMagnitude));
            }
        }
    }

    public void setEffect(MobEffect effect) {
        this.f_19804_.set(EFFECT_ID, ForgeRegistries.MOB_EFFECTS.getKey(effect).toString());
    }

    public void setDestinationPoint(Vec3 dest) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("x", dest.x);
        nbt.putDouble("y", dest.y);
        nbt.putDouble("z", dest.z);
        this.f_19804_.set(LERPDATA, nbt);
        Vec3 vector3d = dest.subtract(this.m_20182_()).normalize();
        double f = vector3d.horizontalDistance();
        this.m_146922_((float) (Mth.atan2(vector3d.x, vector3d.z) * 180.0F / (float) Math.PI));
        this.m_146926_((float) (Mth.atan2(vector3d.y, f) * 180.0F / (float) Math.PI));
        this.f_19859_ = this.m_146908_();
        this.f_19860_ = this.m_146909_();
    }

    private boolean calculateControlPoints() {
        if (this.potion_control_1 != null && this.potion_control_2 != null) {
            return true;
        } else {
            Vec3 start = this.m_20182_();
            Vec3 end = this.getDestination();
            if (start != null && end != null) {
                this.potion_control_1 = start.add(end.subtract(start).scale(0.3)).add(0.0, 1.0, 0.0);
                this.potion_control_2 = start.add(end.subtract(start).scale(0.6)).add(0.0, 1.0, 0.0);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.m_20191_().inflate(16.0);
    }

    @Nullable
    public Vec3 getDestination() {
        if (this.cachedOffset == null) {
            CompoundTag nbt = this.f_19804_.get(LERPDATA);
            if (nbt == null || !nbt.contains("x") || !nbt.contains("y") || !nbt.contains("z")) {
                return null;
            }
            this.cachedOffset = new Vec3(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z"));
        }
        return this.cachedOffset;
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(EFFECT_ID, ForgeRegistries.MOB_EFFECTS.getKey(MobEffects.MOVEMENT_SLOWDOWN).toString());
        this.f_19804_.define(LERPDATA, new CompoundTag());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("effect")) {
            this.f_19804_.set(EFFECT_ID, compound.get("effect").getAsString());
        }
        if (compound.contains("lerpdata")) {
            this.f_19804_.set(LERPDATA, (CompoundTag) compound.get("lerpdata"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putString("effect", this.f_19804_.get(EFFECT_ID));
        compound.put("lerpdata", this.f_19804_.get(LERPDATA));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}