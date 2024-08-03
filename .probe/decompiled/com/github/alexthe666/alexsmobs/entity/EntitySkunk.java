package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.client.particle.AMParticleRegistry;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import java.util.Collection;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntitySkunk extends Animal {

    public float prevSprayProgress;

    public float sprayProgress;

    private int prevSprayTime = 0;

    private int harassedTime;

    private int sprayCooldown;

    private Vec3 sprayAt;

    private static final EntityDataAccessor<Integer> SPRAY_TIME = SynchedEntityData.defineId(EntitySkunk.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> SPRAY_YAW = SynchedEntityData.defineId(EntitySkunk.class, EntityDataSerializers.FLOAT);

    protected EntitySkunk(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(SPRAY_YAW, 0.0F);
        this.f_19804_.define(SPRAY_TIME, 0);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new EntitySkunk.SprayGoal());
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.5) {

            @Override
            public void tick() {
                super.m_8037_();
                EntitySkunk.this.harassedTime += 10;
            }
        });
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(Items.SWEET_BERRIES), false));
        this.f_21345_.addGoal(2, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new RandomStrollGoal(this, 1.0, 60));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.0));
        this.f_21345_.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(7, new RandomLookAroundGoal(this));
        this.f_21345_.addGoal(3, new AvoidEntityGoal(this, LivingEntity.class, AMEntityRegistry.buildPredicateFromTag(AMTagRegistry.SKUNK_FEARS), 10.0F, 1.3, 1.1, EntitySelector.NO_CREATIVE_OR_SPECTATOR) {

            @Override
            public boolean canUse() {
                return super.canUse() && EntitySkunk.this.getSprayTime() <= 0;
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && EntitySkunk.this.getSprayTime() <= 0;
            }

            @Override
            public void tick() {
                super.tick();
                if (this.f_25016_ != null) {
                    EntitySkunk.this.sprayAt = this.f_25016_.m_20182_();
                }
                EntitySkunk.this.harassedTime += 4;
            }
        });
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.skunkSpawnRolls, this.m_217043_(), spawnReasonIn) && super.m_5545_(worldIn, spawnReasonIn);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SWEET_BERRIES);
    }

    public float getSprayYaw() {
        return this.f_19804_.get(SPRAY_YAW);
    }

    public void setSprayYaw(float yaw) {
        this.f_19804_.set(SPRAY_YAW, yaw);
    }

    public int getSprayTime() {
        return this.f_19804_.get(SPRAY_TIME);
    }

    public void setSprayTime(int time) {
        this.f_19804_.set(SPRAY_TIME, time);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SKUNK_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SKUNK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SKUNK_HURT.get();
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevSprayProgress = this.sprayProgress;
        if (this.getSprayTime() > 0) {
            if (this.sprayProgress < 5.0F) {
                this.sprayProgress++;
            }
            this.setSprayTime(this.getSprayTime() - 1);
            if (this.getSprayTime() == 0) {
                this.spawnLingeringCloud();
            } else if (this.getSprayTime() % 6 == 0) {
                this.m_216990_(AMSoundRegistry.SKUNK_SPRAY.get());
            }
            this.f_20883_ = this.m_146908_();
            this.m_146922_(this.approachRotation(this.getSprayYaw(), this.m_146908_() + 10.0F, 15.0F));
        }
        if (this.getSprayTime() <= 0 && this.sprayProgress > 0.0F) {
            this.sprayProgress--;
        }
        if (!this.m_9236_().isClientSide) {
            if (this.harassedTime > 200 && this.sprayCooldown == 0 && !this.m_6162_()) {
                this.harassedTime = 0;
                this.sprayCooldown = 200 + this.f_19796_.nextInt(200);
                this.setSprayTime(60 + this.f_19796_.nextInt(60));
            }
            if (this.harassedTime > 0) {
                this.harassedTime--;
            }
            if (this.sprayCooldown > 0) {
                this.sprayCooldown--;
            }
            Entity lastHurt = this.m_21188_();
            if (lastHurt != null) {
                this.sprayAt = lastHurt.position();
            }
        }
        this.prevSprayTime = this.getSprayTime();
    }

    private void spawnLingeringCloud() {
        Collection<MobEffectInstance> collection = this.m_21220_();
        if (!collection.isEmpty()) {
            float fartDistance = 2.5F;
            Vec3 modelBack = new Vec3(0.0, 0.4F, -2.5).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
            Vec3 fartAt = this.m_20182_().add(modelBack);
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.m_9236_(), fartAt.x, fartAt.y, fartAt.z);
            areaeffectcloud.setRadius(2.5F);
            areaeffectcloud.setRadiusOnUse(-0.25F);
            areaeffectcloud.setWaitTime(20);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());
            for (MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }
            this.m_9236_().m_7967_(areaeffectcloud);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 48) {
            Vec3 modelBack = new Vec3(0.0, 0.4F, -0.4F).xRot(-this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-this.m_146908_() * (float) (Math.PI / 180.0));
            Vec3 particleFrom = this.m_20182_().add(modelBack);
            float scale = this.f_19796_.nextFloat() * 0.5F + 1.0F;
            Vec3 particleTo = modelBack.multiply((double) scale, 1.0, (double) scale);
            for (int i = 0; i < 3; i++) {
                double d0 = this.f_19796_.nextGaussian() * 0.1;
                double d1 = this.f_19796_.nextGaussian() * 0.1;
                double d2 = this.f_19796_.nextGaussian() * 0.1;
                this.m_9236_().addParticle(AMParticleRegistry.SMELLY.get(), particleFrom.x, particleFrom.y, particleFrom.z, particleTo.x + d0, particleTo.y - 0.4F + d1, particleTo.z + d2);
            }
        } else {
            super.handleEntityEvent(id);
        }
    }

    private float approachRotation(float current, float target, float max) {
        float f = Mth.wrapDegrees(target - current);
        if (f > max) {
            f = max;
        }
        if (f < -max) {
            f = -max;
        }
        return Mth.wrapDegrees(current + f);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return AMEntityRegistry.SKUNK.get().create(this.m_9236_());
    }

    private class SprayGoal extends Goal {

        private int actualSprayTime = 0;

        public SprayGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return EntitySkunk.this.getSprayTime() > 0;
        }

        @Override
        public void stop() {
            this.actualSprayTime = 0;
        }

        @Override
        public void tick() {
            EntitySkunk.this.m_21573_().stop();
            Vec3 sprayAt = this.getSprayAt();
            double d0 = EntitySkunk.this.m_20185_() - sprayAt.x;
            double d2 = EntitySkunk.this.m_20189_() - sprayAt.z;
            float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            EntitySkunk.this.setSprayYaw(f);
            if (EntitySkunk.this.sprayProgress >= 5.0F) {
                EntitySkunk.this.m_9236_().broadcastEntityEvent(EntitySkunk.this, (byte) 48);
                if (this.actualSprayTime > 10 && EntitySkunk.this.f_19796_.nextInt(2) == 0) {
                    Vec3 skunkPos = new Vec3(EntitySkunk.this.m_20185_(), EntitySkunk.this.m_20188_(), EntitySkunk.this.m_20189_());
                    float xAdd = EntitySkunk.this.f_19796_.nextFloat() * 20.0F - 10.0F;
                    float yAdd = EntitySkunk.this.f_19796_.nextFloat() * 20.0F - 10.0F;
                    float maxSprayDist = 5.0F;
                    Vec3 modelBack = new Vec3(0.0, 0.0, -5.0).xRot((xAdd - EntitySkunk.this.m_146909_()) * (float) (Math.PI / 180.0)).yRot((yAdd - EntitySkunk.this.m_146908_()) * (float) (Math.PI / 180.0));
                    HitResult hitResult = EntitySkunk.this.m_9236_().m_45547_(new ClipContext(skunkPos, skunkPos.add(modelBack), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, EntitySkunk.this));
                    if (hitResult != null) {
                        BlockPos pos;
                        Direction dir;
                        if (hitResult instanceof BlockHitResult block) {
                            pos = block.getBlockPos().relative(block.getDirection());
                            dir = block.getDirection().getOpposite();
                        } else {
                            pos = AMBlockPos.fromVec3(hitResult.getLocation());
                            dir = Direction.UP;
                        }
                        BlockState currentState = EntitySkunk.this.m_9236_().getBlockState(pos);
                        BlockState sprayState = ((MultifaceBlock) AMBlockRegistry.SKUNK_SPRAY.get()).getStateForPlacement(EntitySkunk.this.m_9236_().getBlockState(pos), EntitySkunk.this.m_9236_(), pos, dir);
                        if ((currentState.m_60795_() || currentState.m_247087_()) && sprayState != null && sprayState.m_60713_(AMBlockRegistry.SKUNK_SPRAY.get())) {
                            EntitySkunk.this.m_9236_().setBlockAndUpdate(pos, sprayState);
                        }
                        double sprayDist = hitResult.getLocation().subtract(skunkPos).length() / 5.0;
                        AABB poisonBox = new AABB(skunkPos, skunkPos.add(modelBack.scale(sprayDist)).add(0.0, 1.5, 0.0)).inflate(1.0);
                        Collection<MobEffectInstance> collection = EntitySkunk.this.m_21220_();
                        for (LivingEntity entity : EntitySkunk.this.m_9236_().m_45976_(LivingEntity.class, poisonBox)) {
                            if (!(entity instanceof EntitySkunk)) {
                                entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300));
                                if (entity instanceof ServerPlayer serverPlayer) {
                                    AMAdvancementTriggerRegistry.SKUNK_SPRAY.trigger(serverPlayer);
                                }
                                for (MobEffectInstance mobeffectinstance : collection) {
                                    entity.addEffect(new MobEffectInstance(mobeffectinstance));
                                }
                            }
                        }
                    }
                }
                this.actualSprayTime++;
            }
        }

        private Vec3 getSprayAt() {
            Entity last = EntitySkunk.this.m_21188_();
            if (EntitySkunk.this.sprayAt != null) {
                return EntitySkunk.this.sprayAt;
            } else if (last != null) {
                return last.position();
            } else {
                Vec3 modelBack = new Vec3(0.0, 0.4F, -1.0).xRot(-EntitySkunk.this.m_146909_() * (float) (Math.PI / 180.0)).yRot(-EntitySkunk.this.m_146908_() * (float) (Math.PI / 180.0));
                return EntitySkunk.this.m_20182_().add(modelBack);
            }
        }
    }
}