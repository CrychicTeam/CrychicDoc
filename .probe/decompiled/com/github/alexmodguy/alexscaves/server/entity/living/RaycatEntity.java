package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.AnimalFollowOwnerGoal;
import com.github.alexmodguy.alexscaves.server.entity.ai.RaycatSitOnBlockGoal;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.github.alexthe666.citadel.server.entity.IComandableMob;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import org.jetbrains.annotations.Nullable;

public class RaycatEntity extends TamableAnimal implements IComandableMob {

    private static final EntityDataAccessor<Integer> ABSORB_TARGET_ID = SynchedEntityData.defineId(RaycatEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(RaycatEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> LAY_TIME = SynchedEntityData.defineId(RaycatEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> ABSORB_AMOUNT = SynchedEntityData.defineId(RaycatEntity.class, EntityDataSerializers.FLOAT);

    private float sitProgress;

    private float prevSitProgress;

    private float layProgress;

    private float prevLayProgress;

    private float prevAbsorbAmount;

    private int absorbCooldown = 300 + this.f_19796_.nextInt(300);

    public RaycatEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new TemptGoal(this, 1.0, Ingredient.of(Items.COD), false));
        this.f_21345_.addGoal(3, new AnimalFollowOwnerGoal(this, 1.2, 5.0F, 2.0F, false) {

            @Override
            public boolean shouldFollow() {
                return RaycatEntity.this.getCommand() == 2;
            }
        });
        this.f_21345_.addGoal(4, new RaycatSitOnBlockGoal(this, 1.0));
        this.f_21345_.addGoal(5, new LeapAtTargetGoal(this, 0.3F));
        this.f_21345_.addGoal(6, new OcelotAttackGoal(this));
        this.f_21345_.addGoal(7, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(10, new RandomLookAroundGoal(this));
    }

    public static boolean checkRaycatSpawnRules(EntityType<? extends Animal> type, LevelAccessor levelAccessor, MobSpawnType mobType, BlockPos pos, RandomSource randomSource) {
        return levelAccessor.m_8055_(pos.below()).m_60713_(ACBlockRegistry.RADROCK.get()) && levelAccessor.m_6425_(pos).isEmpty() && levelAccessor.m_6425_(pos.below()).isEmpty();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader levelReader) {
        return levelReader.m_8055_(pos.below()).m_60713_(ACBlockRegistry.RADROCK.get()) ? 10.0F : super.m_5610_(pos, levelReader);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean isMaxGroupSizeReached(int i) {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(ABSORB_TARGET_ID, -1);
        this.f_19804_.define(LAY_TIME, 0);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(ABSORB_AMOUNT, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH, 24.0).add(Attributes.ATTACK_DAMAGE, 1.0);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ACItemRegistry.RADGILL.get());
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSitProgress = this.sitProgress;
        this.prevLayProgress = this.layProgress;
        this.prevAbsorbAmount = this.getAbsorbAmount();
        if (this.m_21825_() && this.sitProgress < 5.0F) {
            this.sitProgress++;
        }
        if (!this.m_21825_() && this.sitProgress > 0.0F) {
            this.sitProgress--;
        }
        if (this.getLayTime() > 0 && this.layProgress < 5.0F) {
            this.layProgress++;
        }
        if (this.getLayTime() <= 0 && this.layProgress > 0.0F) {
            this.layProgress--;
        }
        Entity absorbTarget = this.getAbsorbTarget();
        if (this.m_21023_(ACEffectRegistry.IRRADIATED.get()) && this.f_19797_ % 10 == 0) {
            this.m_5634_(1.0F);
        }
        if (this.absorbCooldown > 0) {
            this.absorbCooldown--;
        } else {
            LivingEntity owner = this.m_269323_();
            if (absorbTarget == null) {
                if (!this.m_9236_().isClientSide) {
                    Entity closestIrradiated = null;
                    if (owner != null && owner.m_20270_(this) < 20.0F && owner.hasEffect(ACEffectRegistry.IRRADIATED.get())) {
                        closestIrradiated = owner;
                    } else {
                        for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(9.0))) {
                            if (!(entity instanceof RaycatEntity) && entity.hasEffect(ACEffectRegistry.IRRADIATED.get()) && (closestIrradiated == null || closestIrradiated.distanceTo(this) > entity.m_20270_(this)) && (this.m_21824_() || !(entity instanceof Player))) {
                                closestIrradiated = entity;
                            }
                        }
                    }
                    this.setAbsorbTargetId(closestIrradiated == null ? -1 : closestIrradiated.getId());
                    this.resetAbsorbCooldown();
                }
            } else if (this.getAbsorbAmount() <= 0.0F) {
                this.setAbsorbAmount(1.0F);
                this.m_216990_(ACSoundRegistry.RAYCAT_ABSORB.get());
            } else {
                this.setAbsorbAmount(Math.max(0.0F, this.getAbsorbAmount() - 0.05F));
                if (this.getAbsorbAmount() <= 0.0F) {
                    int currentRad = this.m_21023_(ACEffectRegistry.IRRADIATED.get()) ? this.m_21124_(ACEffectRegistry.IRRADIATED.get()).getAmplifier() + 1 : 0;
                    this.m_5634_(10.0F);
                    this.m_7292_(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 200, currentRad));
                    this.m_21391_(absorbTarget, 30.0F, 30.0F);
                    if (absorbTarget instanceof LivingEntity living) {
                        MobEffectInstance effectInstance = living.getEffect(ACEffectRegistry.IRRADIATED.get());
                        if (effectInstance != null) {
                            int timeLeft = effectInstance.getDuration();
                            int level = effectInstance.getAmplifier();
                            living.removeEffect(ACEffectRegistry.IRRADIATED.get());
                            if (level > 0) {
                                living.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), timeLeft, level - 1));
                            }
                        }
                    }
                    this.setAbsorbTargetId(-1);
                    if (!this.m_9236_().isClientSide) {
                        this.resetAbsorbCooldown();
                    }
                }
            }
        }
    }

    private void resetAbsorbCooldown() {
        this.absorbCooldown = 300 + this.f_19796_.nextInt(300);
    }

    public Entity getAbsorbTarget() {
        int id = this.f_19804_.get(ABSORB_TARGET_ID);
        return id == -1 ? null : this.m_9236_().getEntity(id);
    }

    public void setAbsorbTargetId(int id) {
        this.f_19804_.set(ABSORB_TARGET_ID, id);
    }

    public float getSitProgress(float partialTicks) {
        return (this.prevSitProgress + (this.sitProgress - this.prevSitProgress) * partialTicks) * 0.2F;
    }

    public float getLayProgress(float partialTicks) {
        return (this.prevLayProgress + (this.layProgress - this.prevLayProgress) * partialTicks) * 0.2F;
    }

    public float getAbsorbAmount(float partialTicks) {
        return this.prevAbsorbAmount + (this.getAbsorbAmount() - this.prevAbsorbAmount) * partialTicks;
    }

    public float getAbsorbAmount() {
        return this.f_19804_.get(ABSORB_AMOUNT);
    }

    public void setAbsorbAmount(float absorbAmount) {
        this.f_19804_.set(ABSORB_AMOUNT, absorbAmount);
    }

    @Override
    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    @Override
    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    public int getLayTime() {
        return this.f_19804_.get(LAY_TIME);
    }

    public void setLayTime(int layTime) {
        this.f_19804_.set(LAY_TIME, layTime);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());
        compound.putInt("LayTime", this.getLayTime());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCommand(compound.getInt("Command"));
        this.setLayTime(compound.getInt("LayTime"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.is(ACItemRegistry.RADGILL.get()) && !this.m_21824_()) {
            this.usePlayerItem(player, hand, itemstack);
            if (this.m_217043_().nextInt(3) == 0) {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            InteractionResult type = super.m_6071_(player, hand);
            if (!interactionresult.consumesAction() && !type.consumesAction() && this.m_21824_() && this.m_21830_(player) && !this.isFood(itemstack) && !player.m_6144_()) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexscaves.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 1;
                if (sit) {
                    this.m_21839_(true);
                } else {
                    this.m_21839_(false);
                }
                return InteractionResult.SUCCESS;
            } else {
                return type;
            }
        }
    }

    @Override
    protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
        if (this.isFood(itemStack)) {
            this.m_5496_(ACSoundRegistry.RAYCAT_EAT.get(), 1.0F, 1.0F);
        }
        super.m_142075_(player, hand, itemStack);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob mob) {
        return ACEntityRegistry.RAYCAT.get().create(serverLevel);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.m_21824_() ? ACSoundRegistry.RAYCAT_TAME_IDLE.get() : ACSoundRegistry.RAYCAT_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.RAYCAT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.RAYCAT_HURT.get();
    }
}