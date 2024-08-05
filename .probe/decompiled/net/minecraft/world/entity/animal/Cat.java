package net.minecraft.world.entity.animal;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.CatVariantTags;
import net.minecraft.tags.StructureTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;

public class Cat extends TamableAnimal implements VariantHolder<CatVariant> {

    public static final double TEMPT_SPEED_MOD = 0.6;

    public static final double WALK_SPEED_MOD = 0.8;

    public static final double SPRINT_SPEED_MOD = 1.33;

    private static final Ingredient TEMPT_INGREDIENT = Ingredient.of(Items.COD, Items.SALMON);

    private static final EntityDataAccessor<CatVariant> DATA_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.CAT_VARIANT);

    private static final EntityDataAccessor<Boolean> IS_LYING = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);

    private Cat.CatAvoidEntityGoal<Player> avoidPlayersGoal;

    @Nullable
    private TemptGoal temptGoal;

    private float lieDownAmount;

    private float lieDownAmountO;

    private float lieDownAmountTail;

    private float lieDownAmountOTail;

    private float relaxStateOneAmount;

    private float relaxStateOneAmountO;

    public Cat(EntityType<? extends Cat> entityTypeExtendsCat0, Level level1) {
        super(entityTypeExtendsCat0, level1);
    }

    public ResourceLocation getResourceLocation() {
        return this.getVariant().texture();
    }

    @Override
    protected void registerGoals() {
        this.temptGoal = new Cat.CatTemptGoal(this, 0.6, TEMPT_INGREDIENT, true);
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(1, new PanicGoal(this, 1.5));
        this.f_21345_.addGoal(2, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new Cat.CatRelaxOnOwnerGoal(this));
        this.f_21345_.addGoal(4, this.temptGoal);
        this.f_21345_.addGoal(5, new CatLieOnBedGoal(this, 1.1, 8));
        this.f_21345_.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 5.0F, false));
        this.f_21345_.addGoal(7, new CatSitOnBlockGoal(this, 0.8));
        this.f_21345_.addGoal(8, new LeapAtTargetGoal(this, 0.3F));
        this.f_21345_.addGoal(9, new OcelotAttackGoal(this));
        this.f_21345_.addGoal(10, new BreedGoal(this, 0.8));
        this.f_21345_.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 0.8, 1.0000001E-5F));
        this.f_21345_.addGoal(12, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.f_21346_.addGoal(1, new NonTameRandomTargetGoal(this, Rabbit.class, false, null));
        this.f_21346_.addGoal(1, new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public CatVariant getVariant() {
        return this.f_19804_.get(DATA_VARIANT_ID);
    }

    public void setVariant(CatVariant catVariant0) {
        this.f_19804_.set(DATA_VARIANT_ID, catVariant0);
    }

    public void setLying(boolean boolean0) {
        this.f_19804_.set(IS_LYING, boolean0);
    }

    public boolean isLying() {
        return this.f_19804_.get(IS_LYING);
    }

    public void setRelaxStateOne(boolean boolean0) {
        this.f_19804_.set(RELAX_STATE_ONE, boolean0);
    }

    public boolean isRelaxStateOne() {
        return this.f_19804_.get(RELAX_STATE_ONE);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.f_19804_.get(DATA_COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor dyeColor0) {
        this.f_19804_.set(DATA_COLLAR_COLOR, dyeColor0.getId());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_VARIANT_ID, BuiltInRegistries.CAT_VARIANT.getOrThrow(CatVariant.BLACK));
        this.f_19804_.define(IS_LYING, false);
        this.f_19804_.define(RELAX_STATE_ONE, false);
        this.f_19804_.define(DATA_COLLAR_COLOR, DyeColor.RED.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag0) {
        super.addAdditionalSaveData(compoundTag0);
        compoundTag0.putString("variant", BuiltInRegistries.CAT_VARIANT.getKey(this.getVariant()).toString());
        compoundTag0.putByte("CollarColor", (byte) this.getCollarColor().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag0) {
        super.readAdditionalSaveData(compoundTag0);
        CatVariant $$1 = BuiltInRegistries.CAT_VARIANT.get(ResourceLocation.tryParse(compoundTag0.getString("variant")));
        if ($$1 != null) {
            this.setVariant($$1);
        }
        if (compoundTag0.contains("CollarColor", 99)) {
            this.setCollarColor(DyeColor.byId(compoundTag0.getInt("CollarColor")));
        }
    }

    @Override
    public void customServerAiStep() {
        if (this.m_21566_().hasWanted()) {
            double $$0 = this.m_21566_().getSpeedModifier();
            if ($$0 == 0.6) {
                this.m_20124_(Pose.CROUCHING);
                this.m_6858_(false);
            } else if ($$0 == 1.33) {
                this.m_20124_(Pose.STANDING);
                this.m_6858_(true);
            } else {
                this.m_20124_(Pose.STANDING);
                this.m_6858_(false);
            }
        } else {
            this.m_20124_(Pose.STANDING);
            this.m_6858_(false);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.m_21824_()) {
            if (this.m_27593_()) {
                return SoundEvents.CAT_PURR;
            } else {
                return this.f_19796_.nextInt(4) == 0 ? SoundEvents.CAT_PURREOW : SoundEvents.CAT_AMBIENT;
            }
        } else {
            return SoundEvents.CAT_STRAY_AMBIENT;
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return 120;
    }

    public void hiss() {
        this.m_5496_(SoundEvents.CAT_HISS, this.m_6121_(), this.m_6100_());
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource0) {
        return SoundEvents.CAT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAT_DEATH;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected void usePlayerItem(Player player0, InteractionHand interactionHand1, ItemStack itemStack2) {
        if (this.isFood(itemStack2)) {
            this.m_5496_(SoundEvents.CAT_EAT, 1.0F, 1.0F);
        }
        super.m_142075_(player0, interactionHand1, itemStack2);
    }

    private float getAttackDamage() {
        return (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
    }

    @Override
    public boolean doHurtTarget(Entity entity0) {
        return entity0.hurt(this.m_269291_().mobAttack(this), this.getAttackDamage());
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.temptGoal != null && this.temptGoal.isRunning() && !this.m_21824_() && this.f_19797_ % 100 == 0) {
            this.m_5496_(SoundEvents.CAT_BEG_FOR_FOOD, 1.0F, 1.0F);
        }
        this.handleLieDown();
    }

    private void handleLieDown() {
        if ((this.isLying() || this.isRelaxStateOne()) && this.f_19797_ % 5 == 0) {
            this.m_5496_(SoundEvents.CAT_PURR, 0.6F + 0.4F * (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()), 1.0F);
        }
        this.updateLieDownAmount();
        this.updateRelaxStateOneAmount();
    }

    private void updateLieDownAmount() {
        this.lieDownAmountO = this.lieDownAmount;
        this.lieDownAmountOTail = this.lieDownAmountTail;
        if (this.isLying()) {
            this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
            this.lieDownAmountTail = Math.min(1.0F, this.lieDownAmountTail + 0.08F);
        } else {
            this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
            this.lieDownAmountTail = Math.max(0.0F, this.lieDownAmountTail - 0.13F);
        }
    }

    private void updateRelaxStateOneAmount() {
        this.relaxStateOneAmountO = this.relaxStateOneAmount;
        if (this.isRelaxStateOne()) {
            this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
        } else {
            this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
        }
    }

    public float getLieDownAmount(float float0) {
        return Mth.lerp(float0, this.lieDownAmountO, this.lieDownAmount);
    }

    public float getLieDownAmountTail(float float0) {
        return Mth.lerp(float0, this.lieDownAmountOTail, this.lieDownAmountTail);
    }

    public float getRelaxStateOneAmount(float float0) {
        return Mth.lerp(float0, this.relaxStateOneAmountO, this.relaxStateOneAmount);
    }

    @Nullable
    public Cat getBreedOffspring(ServerLevel serverLevel0, AgeableMob ageableMob1) {
        Cat $$2 = EntityType.CAT.create(serverLevel0);
        if ($$2 != null && ageableMob1 instanceof Cat $$3) {
            if (this.f_19796_.nextBoolean()) {
                $$2.setVariant(this.getVariant());
            } else {
                $$2.setVariant($$3.getVariant());
            }
            if (this.m_21824_()) {
                $$2.m_21816_(this.m_21805_());
                $$2.m_7105_(true);
                if (this.f_19796_.nextBoolean()) {
                    $$2.setCollarColor(this.getCollarColor());
                } else {
                    $$2.setCollarColor($$3.getCollarColor());
                }
            }
        }
        return $$2;
    }

    @Override
    public boolean canMate(Animal animal0) {
        if (!this.m_21824_()) {
            return false;
        } else {
            return !(animal0 instanceof Cat $$1) ? false : $$1.m_21824_() && super.m_7848_(animal0);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor0, DifficultyInstance difficultyInstance1, MobSpawnType mobSpawnType2, @Nullable SpawnGroupData spawnGroupData3, @Nullable CompoundTag compoundTag4) {
        spawnGroupData3 = super.m_6518_(serverLevelAccessor0, difficultyInstance1, mobSpawnType2, spawnGroupData3, compoundTag4);
        boolean $$5 = serverLevelAccessor0.m_46940_() > 0.9F;
        TagKey<CatVariant> $$6 = $$5 ? CatVariantTags.FULL_MOON_SPAWNS : CatVariantTags.DEFAULT_SPAWNS;
        BuiltInRegistries.CAT_VARIANT.getTag($$6).flatMap(p_289435_ -> p_289435_.m_213653_(serverLevelAccessor0.m_213780_())).ifPresent(p_262565_ -> this.setVariant((CatVariant) p_262565_.value()));
        ServerLevel $$7 = serverLevelAccessor0.getLevel();
        if ($$7.structureManager().getStructureWithPieceAt(this.m_20183_(), StructureTags.CATS_SPAWN_AS_BLACK).isValid()) {
            this.setVariant(BuiltInRegistries.CAT_VARIANT.getOrThrow(CatVariant.ALL_BLACK));
            this.m_21530_();
        }
        return spawnGroupData3;
    }

    @Override
    public InteractionResult mobInteract(Player player0, InteractionHand interactionHand1) {
        ItemStack $$2 = player0.m_21120_(interactionHand1);
        Item $$3 = $$2.getItem();
        if (this.m_9236_().isClientSide) {
            if (this.m_21824_() && this.m_21830_(player0)) {
                return InteractionResult.SUCCESS;
            } else {
                return !this.isFood($$2) || !(this.m_21223_() < this.m_21233_()) && this.m_21824_() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        } else {
            if (this.m_21824_()) {
                if (this.m_21830_(player0)) {
                    if (!($$3 instanceof DyeItem)) {
                        if ($$3.isEdible() && this.isFood($$2) && this.m_21223_() < this.m_21233_()) {
                            this.usePlayerItem(player0, interactionHand1, $$2);
                            this.m_5634_((float) $$3.getFoodProperties().getNutrition());
                            return InteractionResult.CONSUME;
                        }
                        InteractionResult $$5 = super.m_6071_(player0, interactionHand1);
                        if (!$$5.consumesAction() || this.m_6162_()) {
                            this.m_21839_(!this.m_21827_());
                        }
                        return $$5;
                    }
                    DyeColor $$4 = ((DyeItem) $$3).getDyeColor();
                    if ($$4 != this.getCollarColor()) {
                        this.setCollarColor($$4);
                        if (!player0.getAbilities().instabuild) {
                            $$2.shrink(1);
                        }
                        this.m_21530_();
                        return InteractionResult.CONSUME;
                    }
                }
            } else if (this.isFood($$2)) {
                this.usePlayerItem(player0, interactionHand1, $$2);
                if (this.f_19796_.nextInt(3) == 0) {
                    this.m_21828_(player0);
                    this.m_21839_(true);
                    this.m_9236_().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.m_9236_().broadcastEntityEvent(this, (byte) 6);
                }
                this.m_21530_();
                return InteractionResult.CONSUME;
            }
            InteractionResult $$6 = super.m_6071_(player0, interactionHand1);
            if ($$6.consumesAction()) {
                this.m_21530_();
            }
            return $$6;
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack0) {
        return TEMPT_INGREDIENT.test(itemStack0);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose0, EntityDimensions entityDimensions1) {
        return entityDimensions1.height * 0.5F;
    }

    @Override
    public boolean removeWhenFarAway(double double0) {
        return !this.m_21824_() && this.f_19797_ > 2400;
    }

    @Override
    protected void reassessTameGoals() {
        if (this.avoidPlayersGoal == null) {
            this.avoidPlayersGoal = new Cat.CatAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8, 1.33);
        }
        this.f_21345_.removeGoal(this.avoidPlayersGoal);
        if (!this.m_21824_()) {
            this.f_21345_.addGoal(4, this.avoidPlayersGoal);
        }
    }

    @Override
    public boolean isSteppingCarefully() {
        return this.m_6047_() || super.m_20161_();
    }

    static class CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

        private final Cat cat;

        public CatAvoidEntityGoal(Cat cat0, Class<T> classT1, float float2, double double3, double double4) {
            super(cat0, classT1, float2, double3, double4, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
            this.cat = cat0;
        }

        @Override
        public boolean canUse() {
            return !this.cat.m_21824_() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.cat.m_21824_() && super.canContinueToUse();
        }
    }

    static class CatRelaxOnOwnerGoal extends Goal {

        private final Cat cat;

        @Nullable
        private Player ownerPlayer;

        @Nullable
        private BlockPos goalPos;

        private int onBedTicks;

        public CatRelaxOnOwnerGoal(Cat cat0) {
            this.cat = cat0;
        }

        @Override
        public boolean canUse() {
            if (!this.cat.m_21824_()) {
                return false;
            } else if (this.cat.m_21827_()) {
                return false;
            } else {
                LivingEntity $$0 = this.cat.m_269323_();
                if ($$0 instanceof Player) {
                    this.ownerPlayer = (Player) $$0;
                    if (!$$0.isSleeping()) {
                        return false;
                    }
                    if (this.cat.m_20280_(this.ownerPlayer) > 100.0) {
                        return false;
                    }
                    BlockPos $$1 = this.ownerPlayer.m_20183_();
                    BlockState $$2 = this.cat.m_9236_().getBlockState($$1);
                    if ($$2.m_204336_(BlockTags.BEDS)) {
                        this.goalPos = (BlockPos) $$2.m_61145_(BedBlock.f_54117_).map(p_28209_ -> $$1.relative(p_28209_.getOpposite())).orElseGet(() -> new BlockPos($$1));
                        return !this.spaceIsOccupied();
                    }
                }
                return false;
            }
        }

        private boolean spaceIsOccupied() {
            for (Cat $$1 : this.cat.m_9236_().m_45976_(Cat.class, new AABB(this.goalPos).inflate(2.0))) {
                if ($$1 != this.cat && ($$1.isLying() || $$1.isRelaxStateOne())) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.cat.m_21824_() && !this.cat.m_21827_() && this.ownerPlayer != null && this.ownerPlayer.m_5803_() && this.goalPos != null && !this.spaceIsOccupied();
        }

        @Override
        public void start() {
            if (this.goalPos != null) {
                this.cat.m_21837_(false);
                this.cat.m_21573_().moveTo((double) this.goalPos.m_123341_(), (double) this.goalPos.m_123342_(), (double) this.goalPos.m_123343_(), 1.1F);
            }
        }

        @Override
        public void stop() {
            this.cat.setLying(false);
            float $$0 = this.cat.m_9236_().m_46942_(1.0F);
            if (this.ownerPlayer.getSleepTimer() >= 100 && (double) $$0 > 0.77 && (double) $$0 < 0.8 && (double) this.cat.m_9236_().getRandom().nextFloat() < 0.7) {
                this.giveMorningGift();
            }
            this.onBedTicks = 0;
            this.cat.setRelaxStateOne(false);
            this.cat.m_21573_().stop();
        }

        private void giveMorningGift() {
            RandomSource $$0 = this.cat.m_217043_();
            BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();
            $$1.set(this.cat.m_21523_() ? this.cat.m_21524_().blockPosition() : this.cat.m_20183_());
            this.cat.m_20984_((double) ($$1.m_123341_() + $$0.nextInt(11) - 5), (double) ($$1.m_123342_() + $$0.nextInt(5) - 2), (double) ($$1.m_123343_() + $$0.nextInt(11) - 5), false);
            $$1.set(this.cat.m_20183_());
            LootTable $$2 = this.cat.m_9236_().getServer().getLootData().m_278676_(BuiltInLootTables.CAT_MORNING_GIFT);
            LootParams $$3 = new LootParams.Builder((ServerLevel) this.cat.m_9236_()).withParameter(LootContextParams.ORIGIN, this.cat.m_20182_()).withParameter(LootContextParams.THIS_ENTITY, this.cat).create(LootContextParamSets.GIFT);
            for (ItemStack $$5 : $$2.getRandomItems($$3)) {
                this.cat.m_9236_().m_7967_(new ItemEntity(this.cat.m_9236_(), (double) $$1.m_123341_() - (double) Mth.sin(this.cat.f_20883_ * (float) (Math.PI / 180.0)), (double) $$1.m_123342_(), (double) $$1.m_123343_() + (double) Mth.cos(this.cat.f_20883_ * (float) (Math.PI / 180.0)), $$5));
            }
        }

        @Override
        public void tick() {
            if (this.ownerPlayer != null && this.goalPos != null) {
                this.cat.m_21837_(false);
                this.cat.m_21573_().moveTo((double) this.goalPos.m_123341_(), (double) this.goalPos.m_123342_(), (double) this.goalPos.m_123343_(), 1.1F);
                if (this.cat.m_20280_(this.ownerPlayer) < 2.5) {
                    this.onBedTicks++;
                    if (this.onBedTicks > this.m_183277_(16)) {
                        this.cat.setLying(true);
                        this.cat.setRelaxStateOne(false);
                    } else {
                        this.cat.m_21391_(this.ownerPlayer, 45.0F, 45.0F);
                        this.cat.setRelaxStateOne(true);
                    }
                } else {
                    this.cat.setLying(false);
                }
            }
        }
    }

    static class CatTemptGoal extends TemptGoal {

        @Nullable
        private Player selectedPlayer;

        private final Cat cat;

        public CatTemptGoal(Cat cat0, double double1, Ingredient ingredient2, boolean boolean3) {
            super(cat0, double1, ingredient2, boolean3);
            this.cat = cat0;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.selectedPlayer == null && this.f_25924_.m_217043_().nextInt(this.m_183277_(600)) == 0) {
                this.selectedPlayer = this.f_25925_;
            } else if (this.f_25924_.m_217043_().nextInt(this.m_183277_(500)) == 0) {
                this.selectedPlayer = null;
            }
        }

        @Override
        protected boolean canScare() {
            return this.selectedPlayer != null && this.selectedPlayer.equals(this.f_25925_) ? false : super.canScare();
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.cat.m_21824_();
        }
    }
}