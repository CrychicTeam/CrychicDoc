package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.ai.AnimalAIWanderRanged;
import com.github.alexthe666.alexsmobs.entity.ai.DirectPathNavigator;
import com.github.alexthe666.alexsmobs.entity.ai.FlightMoveController;
import com.github.alexthe666.alexsmobs.entity.ai.FlyingAIFollowOwner;
import com.github.alexthe666.alexsmobs.entity.ai.SmartClimbPathNavigator;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.alexsmobs.misc.AMTagRegistry;
import com.google.common.collect.Maps;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class EntitySugarGlider extends TamableAnimal implements IFollower {

    public static final ResourceLocation SUGAR_GLIDER_REWARD = new ResourceLocation("alexsmobs", "gameplay/sugar_glider_reward");

    public static final Map<Block, Item> LEAF_TO_SAPLING = Util.make(Maps.newHashMap(), map -> {
        map.put(Blocks.OAK_LEAVES, Items.OAK_SAPLING);
        map.put(Blocks.BIRCH_LEAVES, Items.BIRCH_SAPLING);
        map.put(Blocks.SPRUCE_LEAVES, Items.SPRUCE_SAPLING);
        map.put(Blocks.JUNGLE_LEAVES, Items.JUNGLE_SAPLING);
        map.put(Blocks.ACACIA_LEAVES, Items.ACACIA_SAPLING);
        map.put(Blocks.DARK_OAK_LEAVES, Items.DARK_OAK_SAPLING);
        map.put(Blocks.MANGROVE_LEAVES, Items.MANGROVE_PROPAGULE);
    });

    public static final Map<Block, List<Item>> LEAF_TO_RARES = Util.make(Maps.newHashMap(), map -> {
        map.put(Blocks.OAK_LEAVES, List.of(Items.APPLE));
        map.put(Blocks.JUNGLE_LEAVES, List.of(AMItemRegistry.BANANA.get(), AMItemRegistry.LEAFCUTTER_ANT_PUPA.get(), Items.COCOA_BEANS));
        map.put(Blocks.ACACIA_LEAVES, List.of(AMItemRegistry.ACACIA_BLOSSOM.get()));
    });

    private static final EntityDataAccessor<Direction> ATTACHED_FACE = SynchedEntityData.defineId(EntitySugarGlider.class, EntityDataSerializers.DIRECTION);

    private static final EntityDataAccessor<Byte> CLIMBING = SynchedEntityData.defineId(EntitySugarGlider.class, EntityDataSerializers.BYTE);

    private static final EntityDataAccessor<Boolean> GLIDING = SynchedEntityData.defineId(EntitySugarGlider.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> FORAGING_TIME = SynchedEntityData.defineId(EntitySugarGlider.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(EntitySugarGlider.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(EntitySugarGlider.class, EntityDataSerializers.BOOLEAN);

    private static final Direction[] POSSIBLE_DIRECTIONS = new Direction[] { Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private final int attachChangeCooldown = 0;

    public float glideProgress;

    public float prevGlideProgress;

    public float forageProgress;

    public float prevForageProgress;

    public float sitProgress;

    public float prevSitProgress;

    public float attachChangeProgress = 0.0F;

    public float prevAttachChangeProgress = 0.0F;

    public Direction prevAttachDir = Direction.DOWN;

    private boolean isGlidingNavigator;

    private boolean stopClimbing = false;

    private int forageCooldown = 0;

    private int detachCooldown = 0;

    private int rideCooldown = 0;

    protected EntitySugarGlider(EntityType type, Level level) {
        super(type, level);
        this.m_21441_(BlockPathTypes.WATER, -1.0F);
        this.switchNavigator(true);
    }

    public static AttributeSupplier.Builder bakeAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 8.0).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(2, new FlyingAIFollowOwner(this, 1.0, 5.0F, 2.0F, true));
        this.f_21345_.addGoal(3, new TemptGoal(this, 1.1, Ingredient.of(Items.SWEET_BERRIES, Items.HONEYCOMB), false) {

            @Override
            public void start() {
                super.start();
                EntitySugarGlider.this.f_19804_.set(EntitySugarGlider.ATTACHED_FACE, Direction.DOWN);
            }
        });
        this.f_21345_.addGoal(4, new BreedGoal(this, 0.8) {

            @Override
            public void start() {
                super.m_8056_();
                EntitySugarGlider.this.f_19804_.set(EntitySugarGlider.ATTACHED_FACE, Direction.DOWN);
            }
        });
        this.f_21345_.addGoal(5, new EntitySugarGlider.GlideGoal());
        this.f_21345_.addGoal(6, new PanicGoal(this, 1.0));
        this.f_21345_.addGoal(7, new AnimalAIWanderRanged(this, 100, 1.0, 10, 7));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.f_21345_.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(CLIMBING, (byte) 0);
        this.f_19804_.define(ATTACHED_FACE, Direction.DOWN);
        this.f_19804_.define(GLIDING, false);
        this.f_19804_.define(FORAGING_TIME, 0);
        this.f_19804_.define(COMMAND, 0);
        this.f_19804_.define(SITTING, false);
    }

    private void switchNavigator(boolean onGround) {
        if (onGround) {
            this.f_21342_ = new MoveControl(this);
            this.f_21344_ = new SmartClimbPathNavigator(this, this.m_9236_());
            this.isGlidingNavigator = false;
        } else {
            this.f_21342_ = new FlightMoveController(this, 0.6F, false);
            this.f_21344_ = new DirectPathNavigator(this, this.m_9236_());
            this.isGlidingNavigator = true;
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.HONEYCOMB);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return AMSoundRegistry.SUGAR_GLIDER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return AMSoundRegistry.SUGAR_GLIDER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AMSoundRegistry.SUGAR_GLIDER_HURT.get();
    }

    public static boolean canSugarGliderSpawn(EntityType type, LevelAccessor worldIn, MobSpawnType reason, BlockPos pos, RandomSource randomIn) {
        return m_186209_(worldIn, pos);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader reader) {
        if (reader.m_45784_(this) && !reader.containsAnyLiquid(this.m_20191_())) {
            BlockPos blockpos = this.m_20183_();
            BlockState blockstate2 = reader.m_8055_(blockpos.below());
            return blockstate2.m_204336_(BlockTags.LEAVES) || blockstate2.m_204336_(BlockTags.LOGS) || blockstate2.m_60713_(Blocks.GRASS_BLOCK);
        } else {
            return false;
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
        return AMEntityRegistry.rollSpawn(AMConfig.sugarGliderSpawnRolls, this.m_217043_(), spawnReasonIn);
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.m_274367_(1.0F);
        this.prevGlideProgress = this.glideProgress;
        this.prevAttachChangeProgress = this.attachChangeProgress;
        this.prevForageProgress = this.forageProgress;
        this.prevSitProgress = this.sitProgress;
        if (this.attachChangeProgress > 0.0F) {
            this.attachChangeProgress -= 0.25F;
        }
        if (this.glideProgress < 5.0F && this.isGliding()) {
            this.glideProgress += 2.5F;
        }
        if (this.glideProgress > 0.0F && !this.isGliding()) {
            this.glideProgress -= 2.5F;
        }
        if (this.forageProgress < 5.0F && this.getForagingTime() > 0) {
            this.forageProgress++;
        }
        if (this.forageProgress > 0.0F && this.getForagingTime() <= 0) {
            this.forageProgress--;
        }
        boolean sitVisual = this.isOrderedToSit() && !this.m_20069_() && this.m_20096_();
        if (this.sitProgress < 5.0F && sitVisual) {
            this.sitProgress++;
        }
        if (this.sitProgress > 0.0F && !sitVisual) {
            this.sitProgress--;
        }
        if (this.isGliding()) {
            if (this.shouldStopGliding()) {
                this.setGliding(false);
            } else {
                this.m_20256_(this.m_20184_().multiply(0.99, 0.5, 0.99));
            }
        }
        Vec3 vector3d = this.m_20184_();
        if (!this.m_9236_().isClientSide) {
            this.setBesideClimbableBlock(this.f_19862_);
            if (!this.m_20096_() && !this.isOrderedToSit() && !this.m_20072_() && !this.m_20077_() && !this.isGliding() && !this.m_20159_()) {
                Direction closestDirection = Direction.DOWN;
                double closestDistance = 100.0;
                for (Direction dir : POSSIBLE_DIRECTIONS) {
                    BlockPos antPos = new BlockPos(Mth.floor(this.m_20185_()), Mth.floor(this.m_20186_()), Mth.floor(this.m_20189_()));
                    BlockPos offsetPos = antPos.relative(dir);
                    Vec3 offset = Vec3.atCenterOf(offsetPos);
                    if (closestDistance > this.m_20182_().distanceTo(offset) && this.m_9236_().loadedAndEntityCanStandOnFace(offsetPos, this, dir.getOpposite())) {
                        closestDistance = this.m_20182_().distanceTo(offset);
                        closestDirection = dir;
                    }
                }
                this.f_19804_.set(ATTACHED_FACE, closestDistance > (double) (this.m_20205_() * 0.5F + 0.7F) ? Direction.DOWN : closestDirection);
            } else {
                this.f_19804_.set(ATTACHED_FACE, Direction.DOWN);
            }
        }
        boolean flag = false;
        if (this.getAttachmentFacing() != Direction.DOWN) {
            if (!this.f_19862_ && this.getAttachmentFacing() != Direction.UP) {
                Vec3 vec = Vec3.atLowerCornerOf(this.getAttachmentFacing().getNormal());
                this.m_20256_(vector3d.add(vec.normalize().multiply(0.1F, 0.1F, 0.1F)));
            }
            if (!this.m_20096_() && vector3d.y < 0.0) {
                this.m_20256_(vector3d.multiply(1.0, 0.5, 1.0));
                flag = true;
            }
        }
        if (this.getAttachmentFacing() != Direction.DOWN && !this.isGliding()) {
            this.m_20242_(true);
            this.m_20256_(vector3d.multiply(0.6, 0.4, 0.6));
        } else {
            this.m_20242_(false);
        }
        if (this.prevAttachDir != this.getAttachmentFacing()) {
            this.attachChangeProgress = 1.0F;
        }
        this.prevAttachDir = this.getAttachmentFacing();
        if (!this.m_9236_().isClientSide) {
            if ((this.getAttachmentFacing() == Direction.UP || this.isGliding()) && !this.isGlidingNavigator) {
                this.switchNavigator(false);
            }
            if (this.getAttachmentFacing() != Direction.UP && this.isGlidingNavigator) {
                this.switchNavigator(true);
            }
        }
        BlockPos on = this.m_20183_().relative(this.getAttachmentFacing());
        if (this.shouldForage() && this.m_9236_().getBlockState(on).m_204336_(BlockTags.LEAVES)) {
            BlockState state = this.m_9236_().getBlockState(on);
            if (this.getForagingTime() < 100) {
                if (this.f_19796_.nextInt(2) == 0) {
                    for (int i = 0; i < 4 + this.f_19796_.nextInt(2); i++) {
                        double motX = this.f_19796_.nextGaussian() * 0.02;
                        double motY = this.f_19796_.nextGaussian() * 0.02;
                        double motZ = this.f_19796_.nextGaussian() * 0.02;
                        this.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), (double) ((float) on.m_123341_() + this.f_19796_.nextFloat()), (double) ((float) on.m_123342_() + this.f_19796_.nextFloat()), (double) ((float) on.m_123343_() + this.f_19796_.nextFloat()), motX, motY, motZ);
                    }
                }
                this.setForagingTime(this.getForagingTime() + 1);
            } else {
                if (!this.m_9236_().isClientSide) {
                    List<ItemStack> lootList = this.getForageLoot(state);
                    if (lootList.size() > 0) {
                        for (ItemStack stack : lootList) {
                            ItemEntity e = this.m_19983_(stack.copy());
                            e.f_19812_ = true;
                            e.m_20256_(e.m_20184_().multiply(0.2, 0.2, 0.2));
                        }
                    }
                }
                this.forageCooldown = 8000 + 8000 * this.f_19796_.nextInt(2);
                this.setForagingTime(0);
            }
        } else {
            this.setForagingTime(0);
        }
        if (this.detachCooldown > 0) {
            this.detachCooldown--;
        }
        if (this.rideCooldown > 0) {
            this.rideCooldown--;
        }
    }

    @Override
    public void rideTick() {
        Entity entity = this.m_20202_();
        if (this.m_20159_() && !entity.isAlive()) {
            this.m_8127_();
        } else if (this.m_21824_() && entity instanceof LivingEntity && this.m_21830_((LivingEntity) entity)) {
            this.m_20334_(0.0, 0.0, 0.0);
            this.tick();
            if (this.m_20159_()) {
                Entity mount = this.m_20202_();
                if (mount instanceof Player) {
                    ((LivingEntity) mount).addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0, true, false));
                    this.f_20883_ = ((LivingEntity) mount).yBodyRot;
                    this.m_146922_(mount.getYRot());
                    this.f_20885_ = ((LivingEntity) mount).yHeadRot;
                    this.f_19859_ = ((LivingEntity) mount).yHeadRot;
                    float radius = 0.0F;
                    float angle = (float) (Math.PI / 180.0) * (((LivingEntity) mount).yBodyRot - 180.0F);
                    double extraX = (double) (0.0F * Mth.sin((float) Math.PI + angle));
                    double extraZ = (double) (0.0F * Mth.cos(angle));
                    this.m_6034_(mount.getX() + extraX, Math.max(mount.getY() + (double) mount.getBbHeight() + 0.1, mount.getY()), mount.getZ() + extraZ);
                    if (!mount.isAlive() || this.rideCooldown == 0 && mount.isShiftKeyDown()) {
                        this.m_6038_();
                    }
                }
            }
        } else {
            super.m_6083_();
        }
    }

    private List<ItemStack> getForageLoot(BlockState leafState) {
        Item sapling = (Item) LEAF_TO_SAPLING.get(leafState.m_60734_());
        List<Item> rares = (List<Item>) LEAF_TO_RARES.get(leafState.m_60734_());
        float rng = this.m_217043_().nextFloat();
        if (rng < 0.1F && rares != null) {
            Item item = rares.size() <= 1 ? (Item) rares.get(0) : (Item) rares.get(this.m_217043_().nextInt(rares.size()));
            return List.of(new ItemStack(item));
        } else if (rng < 0.25F && sapling != null) {
            return List.of(new ItemStack(sapling));
        } else {
            LootTable loottable = this.m_9236_().getServer().getLootData().m_278676_(SUGAR_GLIDER_REWARD);
            return loottable.getRandomItems(new LootParams.Builder((ServerLevel) this.m_9236_()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.BLOCK_STATE, leafState).create(LootContextParamSets.PIGLIN_BARTER));
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isOrderedToSit()) {
            if (this.m_21573_().getPath() != null) {
                this.m_21573_().stop();
            }
            travelVector = Vec3.ZERO;
        }
        if (this.m_20069_() && this.m_20184_().y > 0.0) {
            this.m_20256_(this.m_20184_().multiply(1.0, 0.5, 1.0));
        }
        super.m_7023_(travelVector);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.f_19804_.set(ATTACHED_FACE, Direction.from3DDataValue(compound.getByte("AttachFace")));
        this.setCommand(compound.getInt("SugarGliderCommand"));
        this.setOrderedToSit(compound.getBoolean("SugarGliderSitting"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("AttachFace", (byte) this.f_19804_.get(ATTACHED_FACE).get3DDataValue());
        compound.putInt("SugarGliderCommand", this.getCommand());
        compound.putBoolean("SugarGliderSitting", this.isOrderedToSit());
    }

    public boolean canTrample(BlockState state, BlockPos pos, float fallDistance) {
        return false;
    }

    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    protected void onInsideBlock(BlockState state) {
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.IN_WALL) || super.m_6673_(source);
    }

    @Override
    public boolean onClimbable() {
        return this.isBesideClimbableBlock() && !this.isGliding() && !this.stopClimbing && !this.isOrderedToSit();
    }

    public boolean isBesideClimbableBlock() {
        return (this.f_19804_.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.f_19804_.get(CLIMBING);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.f_19804_.set(CLIMBING, b0);
    }

    public Direction getAttachmentFacing() {
        return this.f_19804_.get(ATTACHED_FACE);
    }

    public boolean isGliding() {
        return this.f_19804_.get(GLIDING);
    }

    public void setGliding(boolean gliding) {
        this.f_19804_.set(GLIDING, gliding);
    }

    public int getForagingTime() {
        return this.f_19804_.get(FORAGING_TIME);
    }

    public void setForagingTime(int feedingTime) {
        this.f_19804_.set(FORAGING_TIME, feedingTime);
    }

    @Override
    public boolean isOrderedToSit() {
        return this.f_19804_.get(SITTING);
    }

    @Override
    public void setOrderedToSit(boolean sit) {
        this.f_19804_.set(SITTING, sit);
    }

    public int getCommand() {
        return this.f_19804_.get(COMMAND);
    }

    public void setCommand(int command) {
        this.f_19804_.set(COMMAND, command);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        InteractionResult type = super.m_6071_(player, hand);
        if (!this.m_21824_() && itemstack.is(Items.SWEET_BERRIES)) {
            this.m_142075_(player, hand, itemstack);
            this.m_146850_(GameEvent.EAT);
            this.m_5496_(SoundEvents.FOX_EAT, this.m_6121_(), this.m_6100_());
            if (this.m_217043_().nextInt(2) == 0) {
                this.m_21828_(player);
                this.m_9236_().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.m_9236_().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        } else if (this.m_21824_() && itemstack.is(AMTagRegistry.INSECT_ITEMS)) {
            if (this.m_21223_() < this.m_21233_()) {
                this.m_142075_(player, hand, itemstack);
                this.m_146850_(GameEvent.EAT);
                this.m_5496_(SoundEvents.FOX_EAT, this.m_6121_(), this.m_6100_());
                this.m_5634_(5.0F);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        } else {
            InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
            if (interactionresult == InteractionResult.SUCCESS || type == InteractionResult.SUCCESS || !this.m_21824_() || !this.m_21830_(player) || this.isFood(itemstack)) {
                return type;
            } else if (player.m_6144_() && player.m_20197_().isEmpty()) {
                this.m_20329_(player);
                this.rideCooldown = 20;
                return InteractionResult.SUCCESS;
            } else {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() == 3) {
                    this.setCommand(0);
                }
                player.displayClientMessage(Component.translatable("entity.alexsmobs.all.command_" + this.getCommand(), this.m_7755_()), true);
                boolean sit = this.getCommand() == 2;
                if (sit) {
                    this.setOrderedToSit(true);
                    return InteractionResult.SUCCESS;
                } else {
                    this.setOrderedToSit(false);
                    return InteractionResult.SUCCESS;
                }
            }
        }
    }

    @Override
    public void calculateEntityAnimation(boolean b) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, (this.m_20186_() - this.f_19855_) * 2.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 6.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        return new WallClimberNavigation(this, worldIn) {

            @Override
            protected boolean canUpdatePath() {
                return super.m_7632_() || ((EntitySugarGlider) this.f_26494_).isBesideClimbableBlock() || this.f_26494_.f_20899_;
            }
        };
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return AMEntityRegistry.SUGAR_GLIDER.get().create(serverLevel);
    }

    private boolean shouldStopGliding() {
        return this.m_20096_() || this.getAttachmentFacing() != Direction.DOWN;
    }

    private boolean shouldForage() {
        return this.m_21824_() && !this.m_6162_() && this.forageCooldown == 0;
    }

    @Override
    public boolean shouldFollow() {
        return this.getCommand() == 1;
    }

    @Override
    public void followEntity(TamableAnimal tameable, LivingEntity owner, double followSpeed) {
        if (!(this.m_20270_(owner) < 5.0F) && !this.m_6162_()) {
            Vec3 fly = new Vec3(0.0, 0.0, 0.0);
            float f = 0.5F;
            if (this.m_20096_()) {
                fly = fly.add(0.0, 0.4, 0.0);
                f = 0.9F;
            }
            fly = fly.add(owner.m_146892_().subtract(this.m_20182_()).normalize().scale((double) f));
            this.m_20256_(fly);
            Vec3 move = this.m_20184_();
            double d0 = move.horizontalDistance();
            this.m_146926_((float) (-Mth.atan2(move.y, d0) * 180.0F / (float) Math.PI));
            this.m_146922_((float) Mth.atan2(move.z, move.x) * (180.0F / (float) Math.PI) - 90.0F);
            this.setGliding(true);
        } else {
            this.setGliding(!this.m_20096_());
            this.m_21573_().moveTo(owner, followSpeed);
        }
    }

    private boolean canSeeBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 blockVec = Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = this.m_9236_().m_45547_(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        return result.getBlockPos().equals(destinationBlock);
    }

    private class GlideGoal extends Goal {

        private boolean climbing;

        private int climbTime = 0;

        private int leapSearchCooldown = 0;

        private int climbTimeout = 0;

        private BlockPos climb;

        private BlockPos glide;

        private boolean itsOver = false;

        private int airtime = 0;

        private Direction climbOffset = Direction.UP;

        private GlideGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (EntitySugarGlider.this.getForagingTime() <= 0 && !EntitySugarGlider.this.m_6162_() && !EntitySugarGlider.this.isOrderedToSit() && EntitySugarGlider.this.m_217043_().nextInt(45) == 0) {
                if (EntitySugarGlider.this.getAttachmentFacing() != Direction.DOWN) {
                    this.climb = EntitySugarGlider.this.m_20183_().relative(EntitySugarGlider.this.getAttachmentFacing());
                } else {
                    this.climb = this.findClimbPos();
                }
                return this.climb != null;
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.climb != null && !this.itsOver && this.climbTimeout < 30 && (!this.climbing || !EntitySugarGlider.this.m_9236_().m_46859_(this.climb) && !EntitySugarGlider.this.m_21573_().isStuck()) && EntitySugarGlider.this.getForagingTime() <= 0 && !EntitySugarGlider.this.isOrderedToSit();
        }

        @Override
        public void start() {
            this.climbTimeout = 0;
            this.leapSearchCooldown = 0;
            this.airtime = 0;
            this.climbing = true;
            this.climbTime = 0;
            EntitySugarGlider.this.m_21573_().stop();
        }

        @Override
        public void stop() {
            this.climbTimeout = 0;
            this.climb = null;
            this.glide = null;
            this.itsOver = false;
            EntitySugarGlider.this.stopClimbing = false;
            EntitySugarGlider.this.setGliding(false);
            EntitySugarGlider.this.m_21573_().stop();
        }

        @Override
        public void tick() {
            if (this.leapSearchCooldown > 0) {
                this.leapSearchCooldown--;
            }
            if (this.climbing) {
                float inDir = EntitySugarGlider.this.getAttachmentFacing() == Direction.DOWN && EntitySugarGlider.this.m_20186_() > (double) ((float) this.climb.m_123342_() + 0.3F) ? 0.5F + EntitySugarGlider.this.m_20205_() * 0.5F : 0.5F;
                Vec3 offset = Vec3.atCenterOf(this.climb).subtract(0.0, 0.0, 0.0).add((double) ((float) this.climbOffset.getStepX() * inDir), (double) ((float) this.climbOffset.getStepY() * inDir), (double) ((float) this.climbOffset.getStepZ() * inDir));
                double d0 = (double) ((float) this.climb.m_123341_() + 0.5F) - EntitySugarGlider.this.m_20185_();
                double d2 = (double) ((float) this.climb.m_123343_() + 0.5F) - EntitySugarGlider.this.m_20189_();
                double xzDistSqr = d0 * d0 + d2 * d2;
                if (EntitySugarGlider.this.m_20186_() > offset.y - 0.3F - (double) EntitySugarGlider.this.m_20206_()) {
                    EntitySugarGlider.this.stopClimbing = true;
                }
                if (xzDistSqr < 3.0 && EntitySugarGlider.this.getAttachmentFacing() != Direction.DOWN) {
                    Vec3 silly = new Vec3(d0, 0.0, d2).normalize().scale(0.1);
                    EntitySugarGlider.this.m_20256_(EntitySugarGlider.this.m_20184_().add(silly));
                } else {
                    EntitySugarGlider.this.m_21573_().moveTo(offset.x, offset.y, offset.z, 1.0);
                }
                if (EntitySugarGlider.this.getAttachmentFacing() == Direction.DOWN) {
                    this.climbTimeout++;
                    this.climbTime = 0;
                } else {
                    this.climbTimeout = 0;
                    this.climbTime++;
                    if (this.climbTime > 40 && this.leapSearchCooldown == 0) {
                        BlockPos leapTo = this.findLeapPos(EntitySugarGlider.this.shouldForage() && EntitySugarGlider.this.f_19796_.nextInt(5) != 0);
                        this.leapSearchCooldown = 5 + EntitySugarGlider.this.m_217043_().nextInt(10);
                        if (leapTo != null) {
                            EntitySugarGlider.this.stopClimbing = false;
                            EntitySugarGlider.this.setGliding(true);
                            EntitySugarGlider.this.m_21573_().stop();
                            EntitySugarGlider.this.f_19804_.set(EntitySugarGlider.ATTACHED_FACE, Direction.DOWN);
                            this.glide = leapTo;
                            this.climbing = false;
                        }
                    }
                }
            } else if (this.glide != null) {
                EntitySugarGlider.this.stopClimbing = false;
                EntitySugarGlider.this.setGliding(true);
                if (this.airtime > 5 && (EntitySugarGlider.this.f_19862_ || EntitySugarGlider.this.m_20096_() || Math.sqrt(EntitySugarGlider.this.m_20238_(Vec3.atCenterOf(this.glide))) < 1.1F)) {
                    EntitySugarGlider.this.setGliding(false);
                    EntitySugarGlider.this.detachCooldown = 20 + EntitySugarGlider.this.f_19796_.nextInt(80);
                    this.itsOver = true;
                }
                Vec3 fly = Vec3.atCenterOf(this.glide).subtract(EntitySugarGlider.this.m_20182_()).normalize().scale(0.3F);
                EntitySugarGlider.this.m_20256_(fly);
                Vec3 move = EntitySugarGlider.this.m_20184_();
                double d0x = move.horizontalDistance();
                EntitySugarGlider.this.m_146926_((float) (-Mth.atan2(move.y, d0x) * 180.0F / (float) Math.PI));
                EntitySugarGlider.this.m_146922_((float) Mth.atan2(move.z, move.x) * (180.0F / (float) Math.PI) - 90.0F);
                this.airtime++;
            }
        }

        private BlockPos findClimbPos() {
            BlockPos mobPos = EntitySugarGlider.this.m_20183_();
            for (int i = 0; i < 15; i++) {
                BlockPos offset = mobPos.offset(EntitySugarGlider.this.f_19796_.nextInt(16) - 8, EntitySugarGlider.this.f_19796_.nextInt(4) + 1, EntitySugarGlider.this.f_19796_.nextInt(16) - 8);
                double d0 = (double) ((float) offset.m_123341_() + 0.5F) - EntitySugarGlider.this.m_20185_();
                double d2 = (double) ((float) offset.m_123343_() + 0.5F) - EntitySugarGlider.this.m_20189_();
                double xzDistSqr = d0 * d0 + d2 * d2;
                Vec3 blockVec = Vec3.atCenterOf(offset);
                BlockHitResult result = EntitySugarGlider.this.m_9236_().m_45547_(new ClipContext(EntitySugarGlider.this.m_146892_(), blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, EntitySugarGlider.this));
                if (result.getType() != HitResult.Type.MISS && xzDistSqr > 4.0 && result.getDirection().getAxis() != Direction.Axis.Y && this.getDistanceOffGround(result.getBlockPos().relative(result.getDirection())) > 3 && this.isPositionEasilyClimbable(result.getBlockPos())) {
                    this.climbOffset = result.getDirection();
                    return result.getBlockPos();
                }
            }
            return null;
        }

        private BlockPos findLeapPos(boolean leavesOnly) {
            BlockPos mobPos = EntitySugarGlider.this.m_20183_().relative(this.climbOffset.getOpposite());
            for (int i = 0; i < 15; i++) {
                BlockPos offset = mobPos.offset(EntitySugarGlider.this.f_19796_.nextInt(32) - 16, -1 - EntitySugarGlider.this.f_19796_.nextInt(4), EntitySugarGlider.this.f_19796_.nextInt(32) - 16);
                Vec3 blockVec = Vec3.atCenterOf(offset);
                BlockHitResult result = EntitySugarGlider.this.m_9236_().m_45547_(new ClipContext(EntitySugarGlider.this.m_146892_(), blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, EntitySugarGlider.this));
                if (result.getType() != HitResult.Type.MISS && result.getBlockPos().m_123331_(mobPos) > 4.0 && (!leavesOnly || EntitySugarGlider.this.m_9236_().getBlockState(result.getBlockPos()).m_204336_(BlockTags.LEAVES))) {
                    return result.getBlockPos();
                }
            }
            return null;
        }

        private int getDistanceOffGround(BlockPos pos) {
            int dist;
            for (dist = 0; pos.m_123342_() > -64 && EntitySugarGlider.this.m_9236_().m_46859_(pos); dist++) {
                pos = pos.below();
            }
            return dist;
        }

        private boolean isPositionEasilyClimbable(BlockPos pos) {
            pos = pos.below();
            while ((double) pos.m_123342_() > EntitySugarGlider.this.m_20186_() && !EntitySugarGlider.this.m_9236_().m_46859_(pos)) {
                pos = pos.below();
            }
            return (double) pos.m_123342_() <= EntitySugarGlider.this.m_20186_();
        }
    }
}