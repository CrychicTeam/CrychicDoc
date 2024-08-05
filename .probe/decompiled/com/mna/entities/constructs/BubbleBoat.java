package com.mna.entities.constructs;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

public class BubbleBoat extends Boat {

    private static final EntityDataAccessor<Byte> UPGRADE = SynchedEntityData.defineId(BubbleBoat.class, EntityDataSerializers.BYTE);

    private float boatRotation = 0.0F;

    private float prevBoatRotation = 0.0F;

    public BubbleBoat(EntityType<? extends BubbleBoat> p_i50129_1_, Level p_i50129_2_) {
        super(p_i50129_1_, p_i50129_2_);
        this.f_19850_ = true;
    }

    public float getBoatRotation(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevBoatRotation, this.boatRotation);
    }

    public BubbleBoat(Level worldIn, double x, double y, double z) {
        this(EntityInit.BUBBLE_BOAT.get(), worldIn);
        this.m_6034_(x, y, z);
        this.m_20256_(Vec3.ZERO);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
    }

    public BubbleBoat setBrimstone() {
        this.f_19804_.set(UPGRADE, (byte) 1);
        return this;
    }

    public boolean isBrimstone() {
        return this.f_19804_.get(UPGRADE) == 1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(UPGRADE, (byte) 0);
    }

    @Override
    public void tick() {
        this.underwaterControl();
        this.handleSpecialLavaProperties();
        super.tick();
        if (this.m_9236_().isClientSide() && this.m_20197_().size() > 0 && this.f_38279_ != Boat.Status.IN_AIR && this.f_38279_ != Boat.Status.ON_LAND) {
            float RADIUS = 3.0F;
            Vec3 OFFSET = new Vec3(-1.0, -0.25, -1.0);
            Vec3 VELOCITY = new Vec3(-0.01F + Math.random() * 0.02F, -0.01F + Math.random() * 0.02F, -0.01F + Math.random() * 0.02F);
            int amount = (int) Math.ceil(this.m_20184_().length() * 25.0);
            for (int i = 0; i < amount; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.WATER.get()), OFFSET.x + this.m_20185_() + Math.random() * (double) RADIUS, OFFSET.y + this.m_20186_() + Math.random() * (double) RADIUS, OFFSET.z + this.m_20189_() + Math.random() * (double) RADIUS, VELOCITY.x, VELOCITY.y, VELOCITY.z);
            }
        }
        this.prevBoatRotation = this.boatRotation;
        if (this.m_20197_().size() > 0) {
            double motionMagnitude = this.m_20184_().length() * 0.25;
            if (motionMagnitude > 0.001) {
                this.boatRotation = (float) ((double) this.boatRotation - motionMagnitude);
                this.boatRotation = (float) ((double) this.boatRotation % (Math.PI * 2));
            }
        } else {
            double halfPi = Math.PI / 2;
            this.boatRotation -= 0.1F;
            if ((double) Math.abs(this.boatRotation) % halfPi <= 0.1F) {
                this.boatRotation = 0.0F;
                this.prevBoatRotation = 0.0F;
            }
        }
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        if (type == ForgeMod.WATER_TYPE.get()) {
            return true;
        } else {
            return type == ForgeMod.LAVA_TYPE.get() ? this.isBrimstone() : false;
        }
    }

    @Override
    public boolean ignoreExplosion() {
        return this.isBrimstone() ? true : super.m_6128_();
    }

    @Override
    public boolean fireImmune() {
        return this.isBrimstone() ? true : super.m_5825_();
    }

    @Override
    public boolean displayFireAnimation() {
        return this.isBrimstone() ? false : super.m_6051_();
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
        if (this.m_20077_() && this.isBrimstone()) {
            this.f_19789_ = 0.0F;
        }
        super.checkFallDamage(y, onGroundIn, state, pos);
    }

    private void underwaterControl() {
        this.f_38265_ = 0.0F;
        if ((this.f_38279_ == Boat.Status.UNDER_WATER || this.f_38279_ == Boat.Status.UNDER_FLOWING_WATER) && this.m_20197_().size() > 0) {
            double yVel = Math.min(this.m_20184_().y + 0.2F, 2.0);
            this.m_20256_(new Vec3(this.m_20184_().x, yVel, this.m_20184_().z));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public Item getDropItem() {
        return this.f_19804_.get(UPGRADE) == 0 ? ItemInit.BUBBLE_BOAT.get() : ItemInit.BRIMSTONE_BOAT.get();
    }

    private void handleSpecialLavaProperties() {
        if (this.m_20077_()) {
            this.m_20197_().forEach(e -> {
                if (e instanceof LivingEntity && (((LivingEntity) e).getEffect(MobEffects.FIRE_RESISTANCE) == null || ((LivingEntity) e).getEffect(MobEffects.FIRE_RESISTANCE).getDuration() < 20)) {
                    ((LivingEntity) e).addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200));
                }
            });
            this.m_6853_(false);
            CollisionContext iselectioncontext = CollisionContext.of(this);
            if (iselectioncontext.isAbove(LiquidBlock.STABLE_SHAPE, this.m_20183_(), true) && !this.m_9236_().getFluidState(this.m_20183_().above()).is(FluidTags.LAVA)) {
                this.m_6853_(true);
            } else {
                this.m_20334_(this.m_20184_().x, this.m_20184_().y * 0.5 + 0.05, this.m_20184_().z);
            }
        }
    }

    public float getStepHeight() {
        return this.m_20077_() ? 2.0F : 1.0F;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.m_20197_().size() < 1;
    }

    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.3;
    }
}