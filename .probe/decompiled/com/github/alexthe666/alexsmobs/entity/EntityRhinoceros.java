package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIHurtByTargetNotBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIPanicBaby;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.GroundPathNavigatorWide;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityRhinoceros extends Animal implements IAnimatedEntity {

    public static final Animation ANIMATION_FLICK_EARS = Animation.create(20);

    public static final Animation ANIMATION_EAT_GRASS = Animation.create(35);

    public static final Animation ANIMATION_FLING = Animation.create(15);

    public static final Animation ANIMATION_SLASH = Animation.create(30);

    private static final EntityDataAccessor<String> APPLIED_POTION = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> POTION_LEVEL = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> INFLICTED_COUNT = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> POTION_DURATION = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_0 = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Optional<UUID>> DATA_TRUSTED_ID_1 = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final EntityDataAccessor<Boolean> ANGRY = SynchedEntityData.defineId(EntityRhinoceros.class, EntityDataSerializers.BOOLEAN);

    private static final Object2IntMap<String> potionToColor = new Object2IntOpenHashMap();

    private int animationTick;

    private Animation currentAnimation;

    protected EntityRhinoceros(EntityType type, Level level) {
        super(type, level);
        this.m_274367_(1.1F);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60.0).add(Attributes.ATTACK_DAMAGE, 8.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ARMOR, 12.0).add(Attributes.ARMOR_TOUGHNESS, 4.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.9).add(Attributes.ATTACK_KNOCKBACK, 2.0);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(DATA_TRUSTED_ID_0, Optional.empty());
        this.f_19804_.define(DATA_TRUSTED_ID_1, Optional.empty());
        this.f_19804_.define(APPLIED_POTION, "");
        this.f_19804_.define(POTION_LEVEL, 0);
        this.f_19804_.define(INFLICTED_COUNT, 0);
        this.f_19804_.define(POTION_DURATION, 0);
        this.f_19804_.define(ANGRY, false);
    }

    @Override
    protected void registerGoals() {
        super.m_8099_();
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MeleeAttackGoal(this, 1.4, true));
        this.f_21345_.addGoal(2, new AnimalAIPanicBaby(this, 1.25));
        this.f_21345_.addGoal(3, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(4, new TemptGoal(this, 1.0, Ingredient.of(Items.WHEAT, Items.POTION), false));
        this.f_21345_.addGoal(5, new FollowParentGoal(this, 1.1));
        this.f_21345_.addGoal(6, new AnimalAIWanderRanged(this, 90, 1.0, 18, 7));
        this.f_21345_.addGoal(7, new EntityRhinoceros.StrollGoal(200));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(8, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new EntityRhinoceros.DefendTrustedTargetGoal(LivingEntity.class, false, false, entity -> !this.trusts(entity.m_20148_())));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal<Raider>(this, Raider.class, 50, true, true, null) {

            @Override
            public boolean canUse() {
                return super.canUse() && !EntityRhinoceros.this.m_6162_();
            }
        });
        this.f_21346_.addGoal(3, new EntityRhinoceros.AIAttackNearPlayers());
        this.f_21346_.addGoal(4, new AnimalAIHurtByTargetNotBaby(this));
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new GroundPathNavigatorWide(this, worldIn);
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.rhinocerosSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public void tick() {
        super.m_8119_();
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (!this.m_9236_().isClientSide) {
            if (this.getAnimation() == NO_ANIMATION && (this.m_5448_() == null || !this.m_5448_().isAlive())) {
                if (this.m_20184_().lengthSqr() < 0.03 && this.m_217043_().nextInt(500) == 0 && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                    this.setAnimation(ANIMATION_EAT_GRASS);
                } else if (this.m_217043_().nextInt(200) == 0) {
                    this.setAnimation(ANIMATION_FLICK_EARS);
                }
            }
            if (this.getAnimation() == ANIMATION_EAT_GRASS && this.getAnimationTick() == 30 && this.m_9236_().getBlockState(this.m_20183_().below()).m_60713_(Blocks.GRASS_BLOCK)) {
                BlockPos down = this.m_20183_().below();
                this.m_9236_().m_46796_(2001, down, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
                this.m_9236_().setBlock(down, Blocks.DIRT.defaultBlockState(), 2);
                this.m_5634_(10.0F);
            }
            LivingEntity target = this.m_5448_();
            if (target != null && target.isAlive()) {
                this.setAngry(this.m_20270_(target) < 20.0F);
                double dist = (double) this.m_20270_(target);
                if (this.m_142582_(target)) {
                    this.m_21391_(target, 30.0F, 30.0F);
                    this.f_20883_ = this.m_146908_();
                }
                if (dist < (double) (this.m_20205_() + 3.0F)) {
                    if (this.getAnimation() == NO_ANIMATION) {
                        this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_SLASH : ANIMATION_FLING);
                    }
                    if (dist < (double) (this.m_20205_() + 1.5F) && this.m_142582_(target)) {
                        if (this.getAnimation() == ANIMATION_FLING && this.getAnimationTick() >= 5 && this.getAnimationTick() <= 8) {
                            float dmg = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue();
                            if (target instanceof Raider) {
                                dmg = 10.0F;
                            }
                            this.attackWithPotion(target, dmg);
                            this.launch(target, 0.0F, 1.0F);
                            for (LivingEntity entity : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(1.0))) {
                                if (!(entity instanceof Animal) && !this.trusts(entity.m_20148_()) && entity != target) {
                                    this.attackWithPotion(entity, Math.max(dmg - 5.0F, 1.0F));
                                    this.launch(entity, 0.0F, 0.5F);
                                }
                            }
                        }
                        if (this.getAnimation() == ANIMATION_SLASH && (this.getAnimationTick() >= 9 && this.getAnimationTick() <= 11 || this.getAnimationTick() >= 19 && this.getAnimationTick() <= 21)) {
                            float dmg = (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getBaseValue();
                            if (target instanceof Raider) {
                                dmg = 10.0F;
                            }
                            this.attackWithPotion(target, dmg);
                            this.launch(target, this.getAnimationTick() <= 15 ? -90.0F : 90.0F, 1.0F);
                            for (LivingEntity entityx : this.m_9236_().m_45976_(LivingEntity.class, this.m_20191_().inflate(1.0))) {
                                if (!(entityx instanceof Animal) && !this.trusts(entityx.m_20148_()) && entityx != target) {
                                    this.attackWithPotion(entityx, Math.max(dmg - 5.0F, 1.0F));
                                    this.launch(entityx, this.getAnimationTick() <= 15 ? -90.0F : 90.0F, 0.5F);
                                }
                            }
                        }
                    }
                }
            } else {
                this.setAngry(false);
            }
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(AMSoundRegistry.ELEPHANT_WALK.get(), 0.2F, 1.2F);
        } else {
            super.m_7355_(pos, state);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.RHINOCEROS_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.RHINOCEROS_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.RHINOCEROS_HURT.get();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item == Items.DEAD_BUSH || item == Items.GRASS;
    }

    public String getAppliedPotionId() {
        return this.f_19804_.get(APPLIED_POTION);
    }

    public void setAppliedPotionId(String potionId) {
        this.f_19804_.set(APPLIED_POTION, potionId);
    }

    public int getPotionColor() {
        String s = this.getAppliedPotionId();
        if (s.isEmpty()) {
            return -1;
        } else if (!potionToColor.containsKey(s)) {
            MobEffect effect = this.getPotionEffect();
            if (effect != null) {
                int color = effect.getColor();
                potionToColor.put(s, color);
                return color;
            } else {
                return -1;
            }
        } else {
            return potionToColor.getInt(s);
        }
    }

    public MobEffect getPotionEffect() {
        return ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(this.getAppliedPotionId()));
    }

    public int getPotionDuration() {
        return this.f_19804_.get(POTION_DURATION);
    }

    public void setPotionDuration(int time) {
        this.f_19804_.set(POTION_DURATION, time);
    }

    public int getPotionLevel() {
        return this.f_19804_.get(POTION_LEVEL);
    }

    public void setPotionLevel(int time) {
        this.f_19804_.set(POTION_LEVEL, time);
    }

    public int getInflictedCount() {
        return this.f_19804_.get(INFLICTED_COUNT);
    }

    public void setInflictedCount(int count) {
        this.f_19804_.set(INFLICTED_COUNT, count);
    }

    public void resetPotion() {
        this.setAppliedPotionId("");
        this.setPotionDuration(0);
        this.setPotionLevel(0);
        this.setInflictedCount(0);
    }

    private List<UUID> getTrustedUUIDs() {
        List<UUID> list = Lists.newArrayList();
        list.add((UUID) this.f_19804_.get(DATA_TRUSTED_ID_0).orElse((UUID) null));
        list.add((UUID) this.f_19804_.get(DATA_TRUSTED_ID_1).orElse((UUID) null));
        return list;
    }

    private void addTrustedUUID(@Nullable UUID uUID0) {
        if (this.f_19804_.get(DATA_TRUSTED_ID_0).isPresent()) {
            this.f_19804_.set(DATA_TRUSTED_ID_1, Optional.ofNullable(uUID0));
        } else {
            this.f_19804_.set(DATA_TRUSTED_ID_0, Optional.ofNullable(uUID0));
        }
    }

    private void launch(Entity launch, float angle, float scale) {
        float rot = 180.0F + angle + this.m_146908_();
        float hugeScale = 1.0F + this.f_19796_.nextFloat() * 0.5F * scale;
        float strength = (float) ((double) hugeScale * (1.0 - ((LivingEntity) launch).getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)));
        float rotRad = rot * (float) (Math.PI / 180.0);
        float x = Mth.sin(rotRad);
        float z = -Mth.cos(rotRad);
        launch.hasImpulse = true;
        Vec3 vec3 = this.m_20184_();
        Vec3 vec31 = vec3.add(new Vec3((double) x, 0.0, (double) z).normalize().scale((double) strength));
        launch.setDeltaMovement(vec31.x, (double) (hugeScale * 0.3F), vec31.z);
        launch.setOnGround(false);
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation getAnimation() {
        return this.currentAnimation;
    }

    @Override
    public void setAnimation(Animation animation) {
        this.currentAnimation = animation;
    }

    private boolean trusts(UUID uuid) {
        return this.getTrustedUUIDs().contains(uuid);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[] { ANIMATION_FLICK_EARS, ANIMATION_EAT_GRASS, ANIMATION_FLING, ANIMATION_SLASH };
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return AMEntityRegistry.RHINOCEROS.get().create(serverLevel);
    }

    public boolean isAngry() {
        return this.f_19804_.get(ANGRY);
    }

    public void setAngry(boolean angry) {
        this.f_19804_.set(ANGRY, angry);
    }

    private void attackWithPotion(LivingEntity target, float dmg) {
        MobEffect potion = this.getPotionEffect();
        target.hurt(this.m_269291_().mobAttack(this), dmg);
        if (potion != null) {
            MobEffectInstance instance = new MobEffectInstance(potion, this.getPotionDuration(), this.getPotionLevel());
            if (!target.hasEffect(potion) && target.addEffect(instance)) {
                this.setInflictedCount(this.getInflictedCount() + 1);
            }
        }
        if (this.getInflictedCount() > 15 && this.f_19796_.nextInt(3) == 0 || this.getInflictedCount() > 20) {
            this.resetPotion();
        }
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.getAnimation() == NO_ANIMATION) {
            this.setAnimation(this.f_19796_.nextBoolean() ? ANIMATION_SLASH : ANIMATION_FLING);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isAlliedTo(Entity entityIn) {
        if (entityIn instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() != null && this.trusts(tamableAnimal.getOwnerUUID())) {
            return true;
        }
        return super.m_7307_(entityIn) || this.trusts(entityIn.getUUID());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        List<UUID> list = this.getTrustedUUIDs();
        ListTag listtag = new ListTag();
        for (UUID uuid : list) {
            if (uuid != null) {
                listtag.add(NbtUtils.createUUID(uuid));
            }
        }
        tag.put("Trusted", listtag);
        tag.putBoolean("Sleeping", this.m_5803_());
        tag.putString("PotionName", this.getAppliedPotionId());
        tag.putInt("PotionLevel", this.getPotionLevel());
        tag.putInt("PotionDuration", this.getPotionDuration());
        tag.putInt("InflictedCount", this.getInflictedCount());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ListTag listtag = tag.getList("Trusted", 11);
        for (int i = 0; i < listtag.size(); i++) {
            this.addTrustedUUID(NbtUtils.loadUUID(listtag.get(i)));
        }
        this.setAppliedPotionId(tag.getString("PotionName"));
        this.setPotionLevel(tag.getInt("PotionLevel"));
        this.setPotionDuration(tag.getInt("PotionDuration"));
        this.setInflictedCount(tag.getInt("InflictedCount"));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        InteractionResult type = super.mobInteract(player, hand);
        if (!this.m_6162_() && (itemstack.getItem() == Items.POTION || itemstack.getItem() == Items.SPLASH_POTION || itemstack.getItem() == Items.LINGERING_POTION)) {
            Potion contained = PotionUtils.getPotion(itemstack);
            if (this.applyPotion(contained)) {
                this.m_146850_(GameEvent.ENTITY_INTERACT);
                this.m_216990_(SoundEvents.DYE_USE);
                this.m_142075_(player, hand, itemstack);
                ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
                if (!player.addItem(bottle)) {
                    player.drop(bottle, false);
                }
                return InteractionResult.SUCCESS;
            }
        } else if (itemstack.getItem() == Items.WHEAT && !this.trusts(player.m_20148_())) {
            this.addTrustedUUID(player.m_20148_());
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_216990_(SoundEvents.HORSE_EAT);
            return InteractionResult.SUCCESS;
        }
        return type;
    }

    public boolean applyPotion(Potion potion) {
        if (potion != null && potion != Potions.WATER) {
            if (potion.getEffects().size() >= 1) {
                MobEffectInstance first = (MobEffectInstance) potion.getEffects().get(0);
                ResourceLocation loc = ForgeRegistries.MOB_EFFECTS.getKey(first.getEffect());
                if (loc != null) {
                    this.setAppliedPotionId(loc.toString());
                    this.setPotionLevel(first.getAmplifier());
                    this.setPotionDuration(first.getDuration());
                    this.setInflictedCount(0);
                    return true;
                }
            }
            return false;
        } else {
            this.resetPotion();
            return true;
        }
    }

    private boolean trustsAny() {
        return this.f_19804_.get(DATA_TRUSTED_ID_0).isPresent() || this.f_19804_.get(DATA_TRUSTED_ID_1).isPresent();
    }

    class AIAttackNearPlayers extends NearestAttackableTargetGoal<Player> {

        public AIAttackNearPlayers() {
            super(EntityRhinoceros.this, Player.class, 80, true, true, null);
        }

        @Override
        public boolean canUse() {
            return !EntityRhinoceros.this.m_6162_() && !EntityRhinoceros.this.m_27593_() && !EntityRhinoceros.this.trustsAny() ? super.canUse() : false;
        }

        @Override
        protected double getFollowDistance() {
            return 3.0;
        }
    }

    class DefendTrustedTargetGoal extends NearestAttackableTargetGoal<LivingEntity> {

        private LivingEntity trustedLastHurtBy;

        private LivingEntity trustedLastHurt;

        private LivingEntity trusted;

        private int timestamp;

        public DefendTrustedTargetGoal(Class<LivingEntity> entities, boolean b, @Nullable boolean b2, Predicate<LivingEntity> pred) {
            super(EntityRhinoceros.this, entities, 10, b, b2, pred);
        }

        @Override
        public boolean canUse() {
            if ((this.f_26049_ <= 0 || this.f_26135_.m_217043_().nextInt(this.f_26049_) == 0) && !this.f_26135_.m_6162_()) {
                for (UUID uuid : EntityRhinoceros.this.getTrustedUUIDs()) {
                    if (uuid != null && EntityRhinoceros.this.m_9236_() instanceof ServerLevel && ((ServerLevel) EntityRhinoceros.this.m_9236_()).getEntity(uuid) instanceof LivingEntity livingentity) {
                        this.trusted = livingentity;
                        this.trustedLastHurtBy = livingentity.getLastHurtByMob();
                        this.trustedLastHurt = livingentity.getLastHurtMob();
                        int i = livingentity.getLastHurtByMobTimestamp();
                        int j = livingentity.getLastHurtMobTimestamp();
                        if (i != this.timestamp && this.m_26150_(this.trustedLastHurtBy, this.f_26051_)) {
                            return true;
                        }
                        if (j != this.timestamp && this.m_26150_(this.trustedLastHurt, this.f_26051_)) {
                            return true;
                        }
                    }
                }
                return false;
            } else {
                return false;
            }
        }

        @Override
        public void start() {
            if (this.trustedLastHurtBy != null) {
                this.m_26070_(this.trustedLastHurtBy);
                this.f_26050_ = this.trustedLastHurtBy;
                if (this.trusted != null) {
                    this.timestamp = this.trusted.getLastHurtByMobTimestamp();
                }
            } else {
                this.m_26070_(this.trustedLastHurt);
                this.f_26050_ = this.trustedLastHurt;
                if (this.trusted != null) {
                    this.timestamp = this.trusted.getLastHurtMobTimestamp();
                }
            }
            super.start();
        }
    }

    class StrollGoal extends MoveThroughVillageGoal {

        public StrollGoal(int timr) {
            super(EntityRhinoceros.this, 1.0, true, timr, () -> false);
        }

        @Override
        public void start() {
            super.start();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && this.canRhinoWander();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.canRhinoWander();
        }

        private boolean canRhinoWander() {
            return !EntityRhinoceros.this.getTrustedUUIDs().isEmpty();
        }
    }
}