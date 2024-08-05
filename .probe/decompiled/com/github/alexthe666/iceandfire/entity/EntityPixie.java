package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafItemTags;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIEnterHouse;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIFlee;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIFollowOwner;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIMoveRandom;
import com.github.alexthe666.iceandfire.entity.ai.PixieAIPickupItem;
import com.github.alexthe666.iceandfire.entity.ai.PixieAISteal;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import com.github.alexthe666.iceandfire.enums.EnumParticles;
import com.github.alexthe666.iceandfire.message.MessageUpdatePixieHouse;
import com.github.alexthe666.iceandfire.misc.IafSoundRegistry;
import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class EntityPixie extends TamableAnimal {

    public static final float[][] PARTICLE_RGB = new float[][] { { 1.0F, 0.752F, 0.792F }, { 0.831F, 0.662F, 1.0F }, { 0.513F, 0.843F, 1.0F }, { 0.654F, 0.909F, 0.615F }, { 0.996F, 0.788F, 0.407F } };

    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityPixie.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntityPixie.class, EntityDataSerializers.INT);

    public static final int STEAL_COOLDOWN = 3000;

    public MobEffect[] positivePotions = new MobEffect[] { MobEffects.DAMAGE_BOOST, MobEffects.JUMP, MobEffects.MOVEMENT_SPEED, MobEffects.LUCK, MobEffects.DIG_SPEED };

    public MobEffect[] negativePotions = new MobEffect[] { MobEffects.WEAKNESS, MobEffects.CONFUSION, MobEffects.MOVEMENT_SLOWDOWN, MobEffects.UNLUCK, MobEffects.DIG_SLOWDOWN };

    public boolean slowSpeed = false;

    public int ticksUntilHouseAI;

    public int ticksHeldItemFor;

    private BlockPos housePos;

    public int stealCooldown = 0;

    private boolean isSitting;

    public EntityPixie(EntityType type, Level worldIn) {
        super(type, worldIn);
        this.f_21342_ = new EntityPixie.AIMoveControl(this);
        this.f_21364_ = 3;
        this.m_21409_(EquipmentSlot.MAINHAND, 0.0F);
    }

    public static BlockPos getPositionRelativetoGround(Entity entity, Level world, double x, double z, RandomSource rand) {
        BlockPos pos = BlockPos.containing(x, (double) entity.getBlockY(), z);
        for (int yDown = 0; yDown < 3; yDown++) {
            if (!world.m_46859_(pos.below(yDown))) {
                return pos.above(yDown);
            }
        }
        return pos;
    }

    public static BlockPos findAHouse(Entity entity, Level world) {
        for (int xSearch = -10; xSearch < 10; xSearch++) {
            for (int ySearch = -10; ySearch < 10; ySearch++) {
                for (int zSearch = -10; zSearch < 10; zSearch++) {
                    if (world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch)) != null && world.getBlockEntity(entity.blockPosition().offset(xSearch, ySearch, zSearch)) instanceof TileEntityPixieHouse house && !house.hasPixie) {
                        return entity.blockPosition().offset(xSearch, ySearch, zSearch);
                    }
                }
            }
        }
        return entity.blockPosition();
    }

    public boolean isPixieSitting() {
        if (this.m_9236_().isClientSide) {
            boolean isSitting = (this.f_19804_.<Byte>get(f_21798_) & 1) != 0;
            this.isSitting = isSitting;
            this.m_21839_(isSitting);
            return isSitting;
        } else {
            return this.isSitting;
        }
    }

    public void setPixieSitting(boolean sitting) {
        if (!this.m_9236_().isClientSide) {
            this.isSitting = sitting;
            this.m_21837_(sitting);
        }
        byte b0 = this.f_19804_.<Byte>get(f_21798_);
        if (sitting) {
            this.f_19804_.set(f_21798_, (byte) (b0 | 1));
        } else {
            this.f_19804_.set(f_21798_, (byte) (b0 & -2));
        }
    }

    @Override
    public boolean isOrderedToSit() {
        return this.isPixieSitting();
    }

    @Override
    public int getExperienceReward() {
        return 3;
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.m_9236_().isClientSide && this.m_217043_().nextInt(3) == 0 && !this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            this.stealCooldown = 3000;
            return true;
        } else {
            return !this.isOwnerClose() || (source.getEntity() == null || source != this.m_9236_().damageSources().fallingBlock(source.getEntity())) && source != this.m_9236_().damageSources().inWall() && (this.m_269323_() == null || source.getEntity() != this.m_269323_()) ? super.m_6469_(source, amount) : false;
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource source) {
        boolean invulnerable = super.m_6673_(source);
        if (!invulnerable) {
            Entity owner = this.m_269323_();
            if (owner != null && source.getEntity() == owner) {
                return true;
            }
        }
        return invulnerable;
    }

    @Override
    public void die(@NotNull DamageSource cause) {
        if (!this.m_9236_().isClientSide && !this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
            this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
            this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
        super.die(cause);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(COLOR, 0);
        this.f_19804_.define(COMMAND, 0);
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
        if (this.m_269323_() != entityIn) {
            entityIn.push(this);
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.m_21830_(player)) {
            if (player.m_21120_(hand).is(IafItemTags.HEAL_PIXIE) && this.m_21223_() < this.m_21233_()) {
                this.m_5634_(5.0F);
                player.m_21120_(hand).shrink(1);
                this.m_5496_(IafSoundRegistry.PIXIE_TAUNT, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            } else {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() > 1) {
                    this.setCommand(0);
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            if (player.m_21120_(hand).getItem() == IafBlockRegistry.JAR_EMPTY.get().asItem() && !this.m_21824_()) {
                if (!player.isCreative()) {
                    player.m_21120_(hand).shrink(1);
                }
                Block jar = IafBlockRegistry.JAR_PIXIE_0.get();
                switch(this.getColor()) {
                    case 0:
                        jar = IafBlockRegistry.JAR_PIXIE_0.get();
                        break;
                    case 1:
                        jar = IafBlockRegistry.JAR_PIXIE_1.get();
                        break;
                    case 2:
                        jar = IafBlockRegistry.JAR_PIXIE_2.get();
                        break;
                    case 3:
                        jar = IafBlockRegistry.JAR_PIXIE_3.get();
                        break;
                    case 4:
                        jar = IafBlockRegistry.JAR_PIXIE_4.get();
                }
                ItemStack stack = new ItemStack(jar, 1);
                if (!this.m_9236_().isClientSide) {
                    if (!this.m_21120_(InteractionHand.MAIN_HAND).isEmpty()) {
                        this.m_5552_(this.m_21120_(InteractionHand.MAIN_HAND), 0.0F);
                        this.stealCooldown = 3000;
                    }
                    this.m_5552_(stack, 0.0F);
                }
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
            return super.m_6071_(player, hand);
        }
    }

    public void flipAI(boolean flee) {
    }

    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PixieAIFollowOwner(this, 1.0, 2.0F, 4.0F));
        this.f_21345_.addGoal(2, new PixieAIPickupItem(this, false));
        this.f_21345_.addGoal(2, new PixieAIFlee(this, Player.class, 10.0F, (Predicate) entity -> true));
        this.f_21345_.addGoal(2, new PixieAISteal(this, 1.0));
        this.f_21345_.addGoal(3, new PixieAIMoveRandom(this));
        this.f_21345_.addGoal(4, new PixieAIEnterHouse(this));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        spawnDataIn = super.m_6518_(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        this.setColor(this.f_19796_.nextInt(5));
        this.m_21008_(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        if (dataTag != null) {
            System.out.println("EntityPixie spawned with dataTag: " + dataTag);
        }
        return spawnDataIn;
    }

    private boolean isBeyondHeight() {
        if (this.m_20186_() > (double) this.m_9236_().m_151558_()) {
            return true;
        } else {
            BlockPos height = this.m_9236_().m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, this.m_20183_());
            int maxY = 20 + height.m_123342_();
            return this.m_20186_() > (double) maxY;
        }
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
        this.setPixieSitting(command == 1);
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (!this.m_9236_().isClientSide) {
            if (this.isPixieSitting() && this.getCommand() != 1) {
                this.setPixieSitting(false);
            }
            if (!this.isPixieSitting() && this.getCommand() == 1) {
                this.setPixieSitting(true);
            }
            if (this.isPixieSitting()) {
                this.m_21573_().stop();
            }
        }
        if (this.stealCooldown > 0) {
            this.stealCooldown--;
        }
        if (!this.m_21205_().isEmpty() && !this.m_21824_()) {
            this.ticksHeldItemFor++;
        } else {
            this.ticksHeldItemFor = 0;
        }
        if (!this.isPixieSitting() && !this.isBeyondHeight()) {
            this.m_20256_(this.m_20184_().add(0.0, 0.08, 0.0));
        }
        if (this.m_9236_().isClientSide) {
            IceAndFire.PROXY.spawnParticle(EnumParticles.If_Pixie, this.m_20185_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), this.m_20186_() + (double) (this.f_19796_.nextFloat() * this.m_20206_()), this.m_20189_() + (double) (this.f_19796_.nextFloat() * this.m_20205_() * 2.0F) - (double) this.m_20205_(), (double) PARTICLE_RGB[this.getColor()][0], (double) PARTICLE_RGB[this.getColor()][1], (double) PARTICLE_RGB[this.getColor()][2]);
        }
        if (this.ticksUntilHouseAI > 0) {
            this.ticksUntilHouseAI--;
        }
        if (!this.m_9236_().isClientSide && this.housePos != null && this.m_20238_(Vec3.atCenterOf(this.housePos)) < 1.5 && this.m_9236_().getBlockEntity(this.housePos) != null && this.m_9236_().getBlockEntity(this.housePos) instanceof TileEntityPixieHouse house) {
            if (house.hasPixie) {
                this.housePos = null;
            } else {
                house.hasPixie = true;
                house.pixieType = this.getColor();
                house.pixieItems.set(0, this.m_21120_(InteractionHand.MAIN_HAND));
                house.tamedPixie = this.m_21824_();
                house.pixieOwnerUUID = this.m_21805_();
                IceAndFire.sendMSGToAll(new MessageUpdatePixieHouse(this.housePos.asLong(), true, this.getColor()));
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
        if (this.m_269323_() != null && this.isOwnerClose() && this.f_19797_ % 80 == 0) {
            this.m_269323_().addEffect(new MobEffectInstance(this.positivePotions[this.getColor()], 100, 0, false, false));
        }
    }

    public int getColor() {
        return Mth.clamp(this.m_20088_().get(COLOR), 0, 4);
    }

    public void setColor(int color) {
        this.m_20088_().set(COLOR, color);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.setColor(compound.getInt("Color"));
        this.stealCooldown = compound.getInt("StealCooldown");
        this.ticksHeldItemFor = compound.getInt("HoldingTicks");
        this.setPixieSitting(compound.getBoolean("PixieSitting"));
        this.setCommand(compound.getInt("Command"));
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("Color", this.getColor());
        compound.putInt("Command", this.getCommand());
        compound.putInt("StealCooldown", this.stealCooldown);
        compound.putInt("HoldingTicks", this.ticksHeldItemFor);
        compound.putBoolean("PixieSitting", this.isPixieSitting());
        super.addAdditionalSaveData(compound);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverWorld, @NotNull AgeableMob ageable) {
        return null;
    }

    public void setHousePosition(BlockPos blockPos) {
        this.housePos = blockPos;
    }

    public BlockPos getHousePos() {
        return this.housePos;
    }

    public boolean isOwnerClose() {
        return this.m_21824_() && this.m_269323_() != null && this.m_20280_(this.m_269323_()) < 100.0;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return IafSoundRegistry.PIXIE_IDLE;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return IafSoundRegistry.PIXIE_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return IafSoundRegistry.PIXIE_DIE;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity entityIn) {
        if (this.m_21824_()) {
            LivingEntity livingentity = this.m_269323_();
            if (entityIn == livingentity) {
                return true;
            }
            if (entityIn instanceof TamableAnimal) {
                return ((TamableAnimal) entityIn).isOwnedBy(livingentity);
            }
            if (livingentity != null) {
                return livingentity.m_7307_(entityIn);
            }
        }
        return super.isAlliedTo(entityIn);
    }

    class AIMoveControl extends MoveControl {

        public AIMoveControl(EntityPixie pixie) {
            super(pixie);
        }

        @Override
        public void tick() {
            float speedMod = 1.0F;
            if (EntityPixie.this.slowSpeed) {
                speedMod = 2.0F;
            }
            if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
                if (EntityPixie.this.f_19862_) {
                    EntityPixie.this.m_146922_(this.f_24974_.m_146908_() + 180.0F);
                    speedMod = 0.1F;
                    BlockPos target = EntityPixie.getPositionRelativetoGround(EntityPixie.this, EntityPixie.this.m_9236_(), EntityPixie.this.m_20185_() + (double) EntityPixie.this.f_19796_.nextInt(15) - 7.0, EntityPixie.this.m_20189_() + (double) EntityPixie.this.f_19796_.nextInt(15) - 7.0, EntityPixie.this.f_19796_);
                    this.f_24975_ = (double) target.m_123341_();
                    this.f_24976_ = (double) target.m_123342_();
                    this.f_24977_ = (double) target.m_123343_();
                }
                double d0 = this.f_24975_ - EntityPixie.this.m_20185_();
                double d1 = this.f_24976_ - EntityPixie.this.m_20186_();
                double d2 = this.f_24977_ - EntityPixie.this.m_20189_();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                d3 = Math.sqrt(d3);
                if (d3 < EntityPixie.this.m_20191_().getSize()) {
                    this.f_24981_ = MoveControl.Operation.WAIT;
                    EntityPixie.this.m_20256_(EntityPixie.this.m_20184_().multiply(0.5, 0.5, 0.5));
                } else {
                    EntityPixie.this.m_20256_(EntityPixie.this.m_20184_().add(d0 / d3 * 0.05 * this.f_24978_ * (double) speedMod, d1 / d3 * 0.05 * this.f_24978_ * (double) speedMod, d2 / d3 * 0.05 * this.f_24978_ * (double) speedMod));
                    if (EntityPixie.this.m_5448_() == null) {
                        EntityPixie.this.m_146922_(-((float) Mth.atan2(EntityPixie.this.m_20184_().x, EntityPixie.this.m_20184_().z)) * (180.0F / (float) Math.PI));
                        EntityPixie.this.f_20883_ = EntityPixie.this.m_146908_();
                    } else {
                        double d4 = EntityPixie.this.m_5448_().m_20185_() - EntityPixie.this.m_20185_();
                        double d5 = EntityPixie.this.m_5448_().m_20189_() - EntityPixie.this.m_20189_();
                        EntityPixie.this.m_146922_(-((float) Mth.atan2(d4, d5)) * (180.0F / (float) Math.PI));
                        EntityPixie.this.f_20883_ = EntityPixie.this.m_146908_();
                    }
                }
            }
        }
    }
}