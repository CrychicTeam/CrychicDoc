package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.KeybindUsingMount;
import com.github.alexmodguy.alexscaves.server.message.MountedEntityKeyMessage;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

public class SubmarineEntity extends Entity implements KeybindUsingMount {

    private static final EntityDataAccessor<Float> RIGHT_PROPELLER_ROT = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> LEFT_PROPELLER_ROT = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> BACK_PROPELLER_ROT = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> ACCELERATION = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> LIGHTS = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> WAXED = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> OXIDIZATION_LEVEL = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DAMAGE_LEVEL = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DANGER_ALERT_TICKS = SynchedEntityData.defineId(SubmarineEntity.class, EntityDataSerializers.INT);

    private static final float TOP_SPEED = 0.65F;

    private float prevLeftPropellerRot;

    private float prevRightPropellerRot;

    private float prevBackPropellerRot;

    private int lSteps;

    private double lx;

    private double ly;

    private double lz;

    private double lyr;

    private double lxr;

    private double lxd;

    private double lyd;

    private double lzd;

    private int controlUpTicks = 0;

    private int controlDownTicks = 0;

    private int turnRightTicks = 0;

    private int turnLeftTicks = 0;

    private int floodlightToggleCooldown = 0;

    private double damageSustained = 0.0;

    private int oxidizeTime = 24000 * (2 + this.f_19796_.nextInt(2));

    public int submergedTicks = 0;

    public int shakeTime = 0;

    private float prevSonarFlashAmount;

    private float sonarFlashAmount;

    private int creakTime;

    private boolean wereLightsOn;

    public SubmarineEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    public SubmarineEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(ACEntityRegistry.SUBMARINE.get(), world);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.f_19804_.define(RIGHT_PROPELLER_ROT, 0.0F);
        this.f_19804_.define(LEFT_PROPELLER_ROT, 0.0F);
        this.f_19804_.define(BACK_PROPELLER_ROT, 0.0F);
        this.f_19804_.define(ACCELERATION, 0.0F);
        this.f_19804_.define(LIGHTS, false);
        this.f_19804_.define(WAXED, false);
        this.f_19804_.define(OXIDIZATION_LEVEL, 0);
        this.f_19804_.define(DAMAGE_LEVEL, 0);
        this.f_19804_.define(DANGER_ALERT_TICKS, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.setOxidizationLevel(tag.getInt("Oxidization"));
        this.setDamageLevel(tag.getInt("DamageLevel"));
        this.setWaxed(tag.getBoolean("Waxed"));
        this.setLightsOn(tag.getBoolean("LightsOn"));
        if (tag.contains("OxidizeTime")) {
            this.oxidizeTime = tag.getInt("OxidizeTime");
        }
        if (tag.contains("DamageSustained")) {
            this.damageSustained = (double) tag.getInt("DamageSustained");
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putInt("Oxidization", this.getOxidizationLevel());
        tag.putInt("DamageLevel", this.getDamageLevel());
        tag.putBoolean("Waxed", this.isWaxed());
        tag.putBoolean("LightsOn", this.areLightsOn());
        tag.putInt("OxidizeTime", this.oxidizeTime);
        tag.putDouble("DamageSustained", this.damageSustained);
    }

    @Override
    public void tick() {
        super.tick();
        float leftPropellerRot = this.getLeftPropellerRot();
        float rightPropellerRot = this.getRightPropellerRot();
        float backPropellerRot = this.getBackPropellerRot();
        if (this.controlDownTicks <= 0 && (this.getDamageLevel() < 4 || this.m_20096_())) {
            if (this.controlUpTicks > 0 && this.getWaterHeight() > 1.5F) {
                this.m_20256_(this.m_20184_().add(0.0, 0.08, 0.0));
                this.controlUpTicks--;
            }
        } else {
            this.m_20256_(this.m_20184_().add(0.0, -0.08, 0.0));
            this.controlDownTicks--;
        }
        if (this.f_19797_ % 200 == 0 && this.damageSustained > 0.0) {
            this.damageSustained--;
        }
        this.f_19860_ = this.m_146909_();
        this.f_19859_ = Mth.wrapDegrees(this.m_146908_());
        this.prevSonarFlashAmount = this.sonarFlashAmount;
        if (this.getDangerAlertTicks() > 0 && this.sonarFlashAmount < 1.0F) {
            this.sonarFlashAmount += 0.25F;
        }
        if (this.getDangerAlertTicks() <= 0 && this.sonarFlashAmount > 0.0F) {
            this.sonarFlashAmount -= 0.25F;
        }
        if (this.getDangerAlertTicks() > 0 && this.getDamageLevel() <= 3 && this.m_20160_() && this.f_19797_ % 20 == 0) {
            this.m_216990_(ACSoundRegistry.SUBMARINE_SONAR.get());
        }
        if (this.getDamageLevel() > 0 && this.m_20160_() && this.creakTime-- <= 0) {
            this.creakTime = 500 - this.getDamageLevel() * 120 + this.f_19796_.nextInt(60);
            this.m_216990_(ACSoundRegistry.SUBMARINE_CREAK.get());
        }
        float acceleration = this.getAcceleration();
        if (this.m_9236_().isClientSide) {
            if (this.lSteps > 0) {
                double d5 = this.m_20185_() + (this.lx - this.m_20185_()) / (double) this.lSteps;
                double d6 = this.m_20186_() + (this.ly - this.m_20186_()) / (double) this.lSteps;
                double d7 = this.m_20189_() + (this.lz - this.m_20189_()) / (double) this.lSteps;
                this.m_146922_(Mth.wrapDegrees((float) this.lyr));
                this.m_146926_(this.m_146909_() + (float) (this.lxr - (double) this.m_146909_()) / (float) this.lSteps);
                this.lSteps--;
                this.m_6034_(d5, d6, d7);
            } else {
                this.m_20090_();
            }
            Player player = AlexsCaves.PROXY.getClientSidePlayer();
            if (player != null && player.m_20365_(this)) {
                if (AlexsCaves.PROXY.isKeyDown(0) && this.controlUpTicks < 2) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 0));
                    this.controlUpTicks = 10;
                }
                if (AlexsCaves.PROXY.isKeyDown(1) && this.controlDownTicks < 2) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 1));
                    this.controlDownTicks = 10;
                }
                if (AlexsCaves.PROXY.isKeyDown(2) && this.floodlightToggleCooldown <= 0) {
                    AlexsCaves.sendMSGToServer(new MountedEntityKeyMessage(this.m_19879_(), player.m_19879_(), 2));
                    this.floodlightToggleCooldown = 5;
                }
            }
            if (this.m_20160_() && this.m_20072_() && this.m_6084_()) {
                AlexsCaves.PROXY.playWorldSound(this, (byte) 15);
            }
        } else {
            if (acceleration < 0.0F) {
                this.setAcceleration(Math.min(0.0F, acceleration + 0.01F));
            }
            if (acceleration > 0.0F) {
                this.setAcceleration(Math.max(0.0F, acceleration - 0.01F));
            }
            if (Math.abs(acceleration) > 0.0F) {
                Vec3 vec3 = new Vec3(0.0, 0.0, (double) (Mth.clamp(acceleration, -0.25F, 0.65F) * 0.2F)).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
                this.m_20256_(this.m_20184_().add(vec3));
            }
            if (this.m_20072_()) {
                this.m_6478_(MoverType.SELF, this.m_20184_());
                this.m_20256_(this.m_20184_().multiply(0.8F, 0.8F, 0.8F));
            } else {
                this.m_20256_(this.m_20184_().add(0.0, -0.5, 0.0));
                this.m_6478_(MoverType.SELF, this.m_20184_().scale(0.9F));
                this.m_20256_(this.m_20184_().multiply(0.1F, 0.3F, 0.1F));
            }
            if (!this.isWaxed() && this.getOxidizationLevel() < 3) {
                if (this.oxidizeTime > 0) {
                    this.oxidizeTime--;
                } else {
                    this.resetOxidizeTime();
                    this.setOxidizationLevel(this.getOxidizationLevel() + 1);
                }
            }
            if (this.getDangerAlertTicks() > 0) {
                this.setDangerAlertTicks(this.getDangerAlertTicks() - 1);
            }
        }
        float xRotSet = Mth.clamp(-((float) this.m_20184_().y) * 2.0F, -1.0F, 1.0F) * (-180.0F / (float) Math.PI) * (float) Math.signum((double) this.getAcceleration() + 0.01);
        float rot = acceleration * 30.0F + Math.signum(acceleration) * 15.0F;
        this.setBackPropellerRot(backPropellerRot + rot);
        this.setLeftPropellerRot(leftPropellerRot + rot + (float) (this.turnLeftTicks > 0 ? 5 * this.turnLeftTicks : 0));
        this.setRightPropellerRot(rightPropellerRot + rot + (float) (this.turnRightTicks > 0 ? 5 * this.turnRightTicks : 0));
        if (this.getWaterHeight() >= 1.5F) {
            if (Math.abs(this.getAcceleration()) > 0.05F) {
                Vec3 bubblesAt = new Vec3(0.0, 0.3F, -2.0).xRot((float) Math.toRadians((double) this.m_146909_())).yRot((float) Math.toRadians((double) (-this.m_146908_())));
                for (int i = 0; i < 1 + this.f_19796_.nextInt(4); i++) {
                    float offsetX = 0.5F - this.f_19796_.nextFloat();
                    float offsetY = 0.5F - this.f_19796_.nextFloat();
                    float offsetZ = 0.5F - this.f_19796_.nextFloat();
                    this.m_9236_().addParticle(ParticleTypes.BUBBLE_COLUMN_UP, this.m_20185_() + (double) offsetX + bubblesAt.x, this.m_20227_(0.5) + (double) offsetY + bubblesAt.y, this.m_20189_() + (double) offsetZ + bubblesAt.z, 0.0, 0.0, 0.0);
                }
            }
            if (this.submergedTicks < 10) {
                this.submergedTicks++;
            }
        } else if (this.submergedTicks > 0) {
            this.submergedTicks = 0;
        }
        if (this.floodlightToggleCooldown > 0) {
            this.floodlightToggleCooldown--;
        }
        if (this.turnLeftTicks > 0) {
            this.turnLeftTicks--;
        }
        if (this.turnRightTicks > 0) {
            this.turnRightTicks--;
        }
        if (this.shakeTime > 0) {
            this.shakeTime--;
        }
        if (this.wereLightsOn != this.areLightsOn()) {
            this.m_216990_(this.wereLightsOn ? ACSoundRegistry.SUBMARINE_LIGHT_OFF.get() : ACSoundRegistry.SUBMARINE_LIGHT_ON.get());
            this.wereLightsOn = this.areLightsOn();
        }
        this.m_146926_(ACMath.approachRotation(this.m_146909_(), Mth.clamp(this.getDamageLevel() >= 4 ? 0.0F : xRotSet, -50.0F, 50.0F), 2.0F));
        this.prevLeftPropellerRot = leftPropellerRot;
        this.prevRightPropellerRot = rightPropellerRot;
        this.prevBackPropellerRot = backPropellerRot;
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.remove(removalReason);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (this.m_6109_() && this.lSteps > 0) {
            this.lSteps = 0;
            this.m_19890_(this.lx, this.ly, this.lz, (float) this.lyr, (float) this.lxr);
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yr, float xr, int steps, boolean b) {
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.lyr = (double) yr;
        this.lxr = (double) xr;
        this.lSteps = steps;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    @Override
    public void lerpMotion(double lerpX, double lerpY, double lerpZ) {
        this.lxd = lerpX;
        this.lyd = lerpY;
        this.lzd = lerpZ;
        this.m_20334_(this.lxd, this.lyd, this.lzd);
    }

    public boolean areLightsOn() {
        return this.f_19804_.get(LIGHTS);
    }

    public void setLightsOn(boolean bool) {
        this.f_19804_.set(LIGHTS, bool);
    }

    public boolean isWaxed() {
        return this.f_19804_.get(WAXED);
    }

    public void setWaxed(boolean waxed) {
        this.f_19804_.set(WAXED, waxed);
    }

    public int getOxidizationLevel() {
        return this.f_19804_.get(OXIDIZATION_LEVEL);
    }

    public void setOxidizationLevel(int level) {
        this.f_19804_.set(OXIDIZATION_LEVEL, level);
    }

    public int getDamageLevel() {
        return this.f_19804_.get(DAMAGE_LEVEL);
    }

    public void setDamageLevel(int level) {
        this.f_19804_.set(DAMAGE_LEVEL, level);
    }

    public int getDangerAlertTicks() {
        return this.f_19804_.get(DANGER_ALERT_TICKS);
    }

    public void setDangerAlertTicks(int ticks) {
        this.f_19804_.set(DANGER_ALERT_TICKS, ticks);
    }

    public boolean canBeRiddenUnderFluidType(FluidType type, Entity rider) {
        return type.supportsBoating(null);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        boolean prev = super.shouldRender(x, y, z);
        return prev || this.m_20160_() && this.m_146895_() != null && this.m_146895_().shouldRender(x, y, z);
    }

    @Override
    public void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
        label22: {
            if (this.m_20365_(passenger) && passenger instanceof LivingEntity living && !this.m_146899_()) {
                this.clampRotation(living);
                if (passenger instanceof Player) {
                    this.tickController((Player) passenger);
                }
                float f1 = -(this.m_146909_() / 40.0F);
                Vec3 seatOffset = new Vec3(0.0, -0.2F, (double) (0.8F + f1)).xRot((float) Math.toRadians((double) this.m_146909_())).yRot((float) Math.toRadians((double) (-this.m_146908_())));
                double d0 = this.m_20186_() + (double) (this.m_20206_() * 0.5F) + seatOffset.y + passenger.getMyRidingOffset();
                moveFunction.accept(passenger, this.m_20185_() + seatOffset.x, d0, this.m_20189_() + seatOffset.z);
                living.m_20301_(Math.min(living.m_20146_() + 2, living.m_6062_()));
                break label22;
            }
            super.positionRider(passenger, moveFunction);
        }
        if (this.getDamageLevel() >= 4) {
            passenger.stopRiding();
        }
    }

    @Override
    public void handleEntityEvent(byte b) {
        if (b == 45) {
            for (int i = 0; i < 5; i++) {
                this.m_9236_().addParticle(ParticleTypes.WAX_ON, this.m_20208_(0.9), this.m_20187_(), this.m_20262_(0.9), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (this.f_19796_.nextFloat() * 0.15F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
            }
        } else if (b == 46) {
            for (int i = 0; i < 5; i++) {
                this.m_9236_().addParticle(ParticleTypes.WAX_OFF, this.m_20208_(0.9), this.m_20187_(), this.m_20262_(0.9), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (this.f_19796_.nextFloat() * 0.15F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
            }
        } else if (b == 47) {
            Block particleState = Blocks.COPPER_BLOCK;
            switch(this.getOxidizationLevel()) {
                case 1:
                    particleState = Blocks.EXPOSED_COPPER;
                    break;
                case 2:
                    particleState = Blocks.WEATHERED_COPPER;
                    break;
                case 3:
                    particleState = Blocks.OXIDIZED_COPPER;
            }
            for (int i = 0; i < 10; i++) {
                this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, particleState.defaultBlockState()), this.m_20208_(0.5), this.m_20187_(), this.m_20262_(0.5), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), (double) (this.f_19796_.nextFloat() * 0.15F), (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
            }
            this.shakeTime = 20;
        } else if (b == 48) {
            this.shakeTime = 10;
        } else {
            super.handleEntityEvent(b);
        }
    }

    private void tickController(Player passenger) {
        if (passenger.f_20900_ != 0.0F) {
            float turn = -Math.signum(passenger.f_20900_);
            if (turn > 0.0F) {
                this.turnLeftTicks = 5;
            } else {
                this.turnRightTicks = 5;
            }
            this.m_146922_(this.m_146908_() + turn * 2.5F);
        }
        if (passenger.f_20902_ != 0.0F) {
            float back = -Math.signum(passenger.f_20902_);
            if (back < 0.0F) {
                this.setAcceleration(Mth.approach(this.getAcceleration(), 1.0F, 0.02F));
            } else {
                this.setAcceleration(Mth.approach(this.getAcceleration(), -0.5F, 0.02F));
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            ItemStack itemStack = player.m_21120_(hand);
            if (!itemStack.canPerformAction(ToolActions.AXE_SCRAPE) || this.getOxidizationLevel() <= 0 && !this.isWaxed()) {
                if (itemStack.is(Items.HONEYCOMB) && !this.isWaxed()) {
                    player.m_6674_(hand);
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }
                    this.m_5496_(SoundEvents.HONEYCOMB_WAX_ON, 1.0F, 1.0F);
                    this.setWaxed(true);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 45);
                    return InteractionResult.CONSUME;
                } else if (itemStack.is(Items.COPPER_INGOT) && this.getDamageLevel() > 0) {
                    player.m_6674_(hand);
                    if (!player.isCreative()) {
                        itemStack.shrink(1);
                    }
                    this.m_5496_(ACSoundRegistry.SUBMARINE_REPAIR.get(), 1.0F, 1.0F);
                    this.setDamageLevel(Math.max(this.getDamageLevel() - 1, 0));
                    this.damageSustained = 0.0;
                    return InteractionResult.CONSUME;
                } else if (!this.m_9236_().isClientSide && this.getDamageLevel() < 4) {
                    return player.m_20329_(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
                } else {
                    return InteractionResult.SUCCESS;
                }
            } else {
                player.m_6674_(hand);
                if (!player.isCreative()) {
                    itemStack.hurtAndBreak(1, player, player1 -> player1.m_21190_(hand));
                }
                if (this.isWaxed()) {
                    this.m_5496_(SoundEvents.AXE_WAX_OFF, 1.0F, 1.0F);
                    this.setWaxed(false);
                } else {
                    this.setOxidizationLevel(this.getOxidizationLevel() - 1);
                    this.m_5496_(SoundEvents.AXE_SCRAPE, 1.0F, 1.0F);
                }
                this.m_9236_().broadcastEntityEvent(this, (byte) 46);
                this.resetOxidizeTime();
                return InteractionResult.CONSUME;
            }
        }
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
        super.thunderHit(level, lightningBolt);
        if (this.getOxidizationLevel() > 0 && !this.isWaxed()) {
            this.setOxidizationLevel(0);
            this.resetOxidizeTime();
            this.m_9236_().broadcastEntityEvent(this, (byte) 46);
        }
    }

    private void resetOxidizeTime() {
        this.oxidizeTime = 24000 * (2 + this.f_19796_.nextInt(2));
    }

    protected void clampRotation(LivingEntity livingEntity) {
        livingEntity.setYBodyRot(this.m_146908_());
        float f = Mth.wrapDegrees(livingEntity.m_146908_() - this.m_146908_());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        livingEntity.f_19859_ += f1 - f;
        livingEntity.yBodyRotO += f1 - f;
        livingEntity.m_146922_(livingEntity.m_146908_() + f1 - f);
        livingEntity.setYHeadRot(livingEntity.m_146908_());
    }

    public float getLeftPropellerRot() {
        return this.f_19804_.get(LEFT_PROPELLER_ROT);
    }

    public void setLeftPropellerRot(float f) {
        this.f_19804_.set(LEFT_PROPELLER_ROT, f);
    }

    public float getLeftPropellerRot(float partialTick) {
        return this.prevLeftPropellerRot + (this.getLeftPropellerRot() - this.prevLeftPropellerRot) * partialTick;
    }

    public float getRightPropellerRot() {
        return this.f_19804_.get(RIGHT_PROPELLER_ROT);
    }

    public void setRightPropellerRot(float f) {
        this.f_19804_.set(RIGHT_PROPELLER_ROT, f);
    }

    public float getRightPropellerRot(float partialTick) {
        return this.prevRightPropellerRot + (this.getRightPropellerRot() - this.prevRightPropellerRot) * partialTick;
    }

    public float getBackPropellerRot() {
        return this.f_19804_.get(BACK_PROPELLER_ROT);
    }

    public void setBackPropellerRot(float f) {
        this.f_19804_.set(BACK_PROPELLER_ROT, f);
    }

    public float getBackPropellerRot(float partialTick) {
        return this.prevBackPropellerRot + (this.getBackPropellerRot() - this.prevBackPropellerRot) * partialTick;
    }

    public float getAcceleration() {
        return this.f_19804_.get(ACCELERATION);
    }

    public void setAcceleration(float f) {
        this.f_19804_.set(ACCELERATION, f);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.m_213877_();
    }

    @Override
    public boolean isPushable() {
        return !this.m_213877_();
    }

    @Override
    public boolean isPickable() {
        return !this.m_213877_();
    }

    @Override
    public boolean shouldBeSaved() {
        return !this.m_213877_();
    }

    @Override
    public float getPickRadius() {
        return this.m_20160_() ? -this.m_20205_() * 0.5F : 0.0F;
    }

    @Override
    public boolean isAttackable() {
        return !this.m_213877_();
    }

    public float getWaterHeight() {
        return (float) this.getFluidTypeHeight(ForgeMod.WATER_TYPE.get());
    }

    @Override
    public void onKeyPacket(Entity keyPresser, int type) {
        if (keyPresser.isPassengerOfSameVehicle(this)) {
            if (type == 0) {
                this.controlUpTicks = 10;
            }
            if (type == 1) {
                this.controlDownTicks = 10;
            }
            if (type == 2) {
                this.setLightsOn(!this.areLightsOn());
                this.floodlightToggleCooldown = 5;
            }
        }
    }

    @Override
    public void onAboveBubbleCol(boolean b) {
        if (!this.m_20160_()) {
            super.onAboveBubbleCol(b);
        }
    }

    @Override
    public void onInsideBubbleColumn(boolean b) {
        if (!this.m_20160_()) {
            super.onAboveBubbleCol(b);
        }
        this.m_183634_();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || damageSource.is(DamageTypes.DROWN) || damageSource.is(DamageTypes.DRY_OUT) || damageSource.is(DamageTypes.CACTUS) || damageSource.is(DamageTypes.HOT_FLOOR) || damageSource.is(DamageTypes.IN_FIRE) || damageSource.is(DamageTypes.ON_FIRE) || damageSource.is(DamageTypes.FALL);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damageValue) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            this.damageSustained += (double) damageValue;
            boolean flag = false;
            this.m_9236_().broadcastEntityEvent(this, (byte) 48);
            if (this.damageSustained >= 10.0) {
                this.damageSustained = 0.0;
                this.m_9236_().broadcastEntityEvent(this, (byte) 47);
                if (this.getDamageLevel() >= 4) {
                    if (!this.m_213877_()) {
                        for (int i = 0; i < 2 + this.f_19796_.nextInt(3); i++) {
                            this.m_19998_(Items.COPPER_INGOT);
                        }
                    }
                    this.remove(Entity.RemovalReason.KILLED);
                    flag = true;
                    this.m_216990_(ACSoundRegistry.SUBMARINE_DESTROY.get());
                } else {
                    this.setDamageLevel(this.getDamageLevel() + 1);
                }
            }
            if (!flag) {
                this.m_216990_(ACSoundRegistry.SUBMARINE_HIT.get());
            }
            return true;
        }
    }

    public float getSonarFlashAmount(float partialTicks) {
        float f = this.prevSonarFlashAmount + (this.sonarFlashAmount - this.prevSonarFlashAmount) * partialTicks;
        float f1 = (float) ((double) f * (Math.cos((double) (((float) this.f_19797_ + partialTicks) * 0.4F)) + 1.0) * 0.5);
        return 1.0F - f + f1;
    }

    public static void alertSubmarineMountOf(LivingEntity living) {
        if (living.isAlive() && living.m_20202_() instanceof SubmarineEntity submarine && submarine.getDamageLevel() <= 3) {
            submarine.setDangerAlertTicks(100);
        }
    }
}