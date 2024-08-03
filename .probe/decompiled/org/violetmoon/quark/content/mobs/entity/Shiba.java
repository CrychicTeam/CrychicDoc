package org.violetmoon.quark.content.mobs.entity;

import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.mobs.ai.BarkAtDarknessGoal;
import org.violetmoon.quark.content.mobs.ai.DeliverFetchedItemGoal;
import org.violetmoon.quark.content.mobs.ai.FetchArrowGoal;
import org.violetmoon.quark.content.mobs.module.ShibaModule;
import org.violetmoon.quark.content.tweaks.ai.NuzzleGoal;
import org.violetmoon.quark.content.tweaks.ai.WantLoveGoal;

public class Shiba extends TamableAnimal {

    private static final EntityDataAccessor<Integer> COLLAR_COLOR = SynchedEntityData.defineId(Shiba.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<ItemStack> MOUTH_ITEM = SynchedEntityData.defineId(Shiba.class, EntityDataSerializers.ITEM_STACK);

    private static final EntityDataAccessor<Integer> FETCHING = SynchedEntityData.defineId(Shiba.class, EntityDataSerializers.INT);

    public BlockPos currentHyperfocus = null;

    private int hyperfocusCooldown = 0;

    public Shiba(EntityType<? extends Shiba> type, Level worldIn) {
        super(type, worldIn);
        this.setTame(false);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(1, new FloatGoal(this));
        this.f_21345_.addGoal(2, new SitWhenOrderedToGoal(this));
        this.f_21345_.addGoal(3, new BarkAtDarknessGoal(this));
        this.f_21345_.addGoal(4, new FetchArrowGoal(this));
        this.f_21345_.addGoal(5, new DeliverFetchedItemGoal(this, 1.1, -1.0F, 32.0F, false));
        this.f_21345_.addGoal(6, new FollowOwnerGoal(this, 1.0, 10.0F, 2.0F, false));
        this.f_21345_.addGoal(7, new TemptGoal(this, 1.0, Ingredient.of(Items.BONE), false));
        this.f_21345_.addGoal(8, new BreedGoal(this, 1.0));
        this.f_21345_.addGoal(9, new NuzzleGoal(this, 0.5, 16.0F, 2.0F, QuarkSounds.ENTITY_SHIBA_WHINE));
        this.f_21345_.addGoal(10, new WantLoveGoal(this, 0.2F));
        this.f_21345_.addGoal(11, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.f_21345_.addGoal(12, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21345_.addGoal(13, new RandomLookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.m_8119_();
        AbstractArrow fetching = this.getFetching();
        if (fetching != null && (this.m_5803_() || fetching.m_9236_() != this.m_9236_() || !fetching.m_6084_() || fetching.pickup == AbstractArrow.Pickup.DISALLOWED)) {
            this.setFetching(null);
        }
        if (!this.m_9236_().isClientSide) {
            if (this.hyperfocusCooldown > 0) {
                this.hyperfocusCooldown--;
            }
            if (fetching == null && !this.m_5803_() && !this.m_21825_() && this.m_21824_() && !this.m_21523_()) {
                LivingEntity owner = this.m_269323_();
                if (this.currentHyperfocus != null) {
                    boolean hyperfocusClear = this.m_9236_().m_45517_(LightLayer.BLOCK, this.currentHyperfocus) > 0;
                    boolean ownerAbsent = owner == null || owner instanceof Player && !owner.getMainHandItem().is(Items.TORCH) && !owner.getOffhandItem().is(Items.TORCH);
                    if (hyperfocusClear || ownerAbsent) {
                        this.currentHyperfocus = null;
                        this.hyperfocusCooldown = 40;
                        if (hyperfocusClear && !ownerAbsent && owner instanceof ServerPlayer sp) {
                            ShibaModule.shibaHelpTrigger.trigger(sp);
                        }
                    }
                }
                if (this.currentHyperfocus == null && owner instanceof Player player && this.hyperfocusCooldown == 0 && (player.m_21205_().is(Items.TORCH) || player.m_21206_().is(Items.TORCH))) {
                    BlockPos ourPos = this.m_20183_();
                    int searchRange = 10;
                    for (int i = 0; i < 20; i++) {
                        BlockPos test = ourPos.offset(this.f_19796_.nextInt(21) - 10, this.f_19796_.nextInt(21) - 10, this.f_19796_.nextInt(21) - 10);
                        if (this.hasLineOfSight(test.above(), 10.0) && this.m_9236_().getBlockState(test).m_60795_() && this.m_9236_().getBlockState(test.below()).m_60804_(this.m_9236_(), test.below()) && this.m_9236_().m_45517_(LightLayer.BLOCK, test) == 0 && (!ShibaModule.ignoreAreasWithSkylight || !this.m_9236_().m_45527_(test))) {
                            this.currentHyperfocus = test;
                        }
                    }
                }
            } else {
                this.currentHyperfocus = null;
            }
        }
        if (!this.m_5803_() && !this.m_9236_().isClientSide && fetching == null && this.getMouthItem().isEmpty()) {
            LivingEntity ownerx = this.m_269323_();
            if (ownerx != null) {
                AABB check = ownerx.m_20191_().inflate(2.0);
                List<AbstractArrow> arrows = this.m_9236_().m_6443_(AbstractArrow.class, check, a -> a.m_19749_() == owner && a.pickup != AbstractArrow.Pickup.DISALLOWED);
                if (arrows.size() > 0) {
                    AbstractArrow arrow = (AbstractArrow) arrows.get(this.m_9236_().random.nextInt(arrows.size()));
                    this.setFetching(arrow);
                }
            }
        }
    }

    public boolean hasLineOfSight(BlockPos pos, double maxRange) {
        Vec3 vec3 = new Vec3(this.m_20185_(), this.m_20188_(), this.m_20189_());
        Vec3 vec31 = new Vec3((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
        return vec31.distanceTo(vec3) > maxRange ? false : this.m_9236_().m_45547_(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
    }

    public AbstractArrow getFetching() {
        int id = this.f_19804_.get(FETCHING);
        if (id == -1) {
            return null;
        } else {
            Entity e = this.m_9236_().getEntity(id);
            return !(e instanceof AbstractArrow) ? null : (AbstractArrow) e;
        }
    }

    public void setFetching(AbstractArrow e) {
        this.f_19804_.set(FETCHING, e == null ? -1 : e.m_19879_());
    }

    @Override
    public boolean isFood(ItemStack stack) {
        Item item = stack.getItem();
        return item.isEdible() && item.getFoodProperties().isMeat();
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(COLLAR_COLOR, DyeColor.RED.getId());
        this.f_19804_.define(MOUTH_ITEM, ItemStack.EMPTY);
        this.f_19804_.define(FETCHING, -1);
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.f_19804_.get(COLLAR_COLOR));
    }

    public void setCollarColor(DyeColor collarcolor) {
        this.f_19804_.set(COLLAR_COLOR, collarcolor.getId());
    }

    public ItemStack getMouthItem() {
        return this.f_19804_.get(MOUTH_ITEM);
    }

    public void setMouthItem(ItemStack stack) {
        this.f_19804_.set(MOUTH_ITEM, stack);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 8;
    }

    @Override
    public boolean canMate(@NotNull Animal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        } else if (!this.m_21824_()) {
            return false;
        } else if (!(otherAnimal instanceof Shiba wolfentity)) {
            return false;
        } else if (!wolfentity.m_21824_()) {
            return false;
        } else {
            return wolfentity.m_5803_() ? false : this.m_27593_() && wolfentity.m_27593_();
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putByte("CollarColor", (byte) this.getCollarColor().getId());
        CompoundTag itemcmp = new CompoundTag();
        ItemStack holding = this.getMouthItem();
        if (!holding.isEmpty()) {
            holding.save(itemcmp);
        }
        compound.put("MouthItem", itemcmp);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("CollarColor")) {
            this.setCollarColor(DyeColor.byId(compound.getInt("CollarColor")));
        }
        if (compound.contains("MouthItem")) {
            CompoundTag itemcmp = compound.getCompound("MouthItem");
            this.setMouthItem(ItemStack.of(itemcmp));
        }
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        Item item = itemstack.getItem();
        Level level = this.m_9236_();
        if (player.m_20163_() && player.m_21205_().isEmpty()) {
            if (hand == InteractionHand.MAIN_HAND && WantLoveGoal.canPet(this)) {
                if (level instanceof ServerLevel serverLevel) {
                    Vec3 pos = this.m_20182_();
                    serverLevel.sendParticles(ParticleTypes.HEART, pos.x, pos.y + 0.5, pos.z, 1, 0.0, 0.0, 0.0, 0.1);
                    this.m_5496_(QuarkSounds.ENTITY_SHIBA_WHINE, 0.6F, 0.5F + (float) Math.random() * 0.5F);
                }
                WantLoveGoal.setPetTime(this);
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        } else {
            if (level.isClientSide) {
                boolean flag = this.m_21830_(player) || this.m_21824_() || item == Items.BONE && !this.m_21824_();
                return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
            }
            if (this.m_21824_()) {
                ItemStack mouthItem = this.getMouthItem();
                if (!mouthItem.isEmpty()) {
                    ItemStack copy = mouthItem.copy();
                    if (!player.addItem(copy)) {
                        this.m_19983_(copy);
                    }
                    if (player.m_9236_() instanceof ServerLevel serverLevel) {
                        Vec3 pos = this.m_20182_();
                        serverLevel.sendParticles(ParticleTypes.HEART, pos.x, pos.y + 0.5, pos.z, 1, 0.0, 0.0, 0.0, 0.1);
                        this.m_5496_(QuarkSounds.ENTITY_SHIBA_WHINE, 0.6F, 0.5F + (float) Math.random() * 0.5F);
                    }
                    this.setMouthItem(ItemStack.EMPTY);
                    return InteractionResult.CONSUME;
                }
                if (this.isFood(itemstack) && this.m_21223_() < this.m_21233_()) {
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    this.m_5634_((float) item.getFoodProperties().getNutrition());
                    return InteractionResult.CONSUME;
                }
                if (!(item instanceof DyeItem)) {
                    if (!itemstack.isEmpty() && mouthItem.isEmpty() && itemstack.getItem() instanceof SwordItem) {
                        ItemStack copyx = itemstack.copy();
                        copyx.setCount(1);
                        itemstack.setCount(itemstack.getCount() - 1);
                        this.setMouthItem(copyx);
                        return InteractionResult.CONSUME;
                    }
                    InteractionResult actionresulttype = super.m_6071_(player, hand);
                    if ((!actionresulttype.consumesAction() || this.m_6162_()) && this.m_21830_(player)) {
                        this.m_21839_(!this.m_21827_());
                        this.f_20899_ = false;
                        this.f_21344_.stop();
                        this.m_6710_(null);
                        return InteractionResult.CONSUME;
                    }
                    return actionresulttype;
                }
                DyeColor dyecolor = ((DyeItem) item).getDyeColor();
                if (dyecolor != this.getCollarColor()) {
                    this.setCollarColor(dyecolor);
                    if (!player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                }
            } else if (item == Items.BONE) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                if (this.f_19796_.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    WantLoveGoal.setPetTime(this);
                    this.m_21828_(player);
                    this.f_21344_.stop();
                    this.m_6710_(null);
                    this.m_21839_(true);
                    level.broadcastEntityEvent(this, (byte) 7);
                } else {
                    level.broadcastEntityEvent(this, (byte) 6);
                }
                return InteractionResult.CONSUME;
            }
        }
        return super.m_6071_(player, hand);
    }

    @Override
    public void setTame(boolean tamed) {
        super.setTame(tamed);
        if (tamed) {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(20.0);
            this.m_21153_(20.0F);
        } else {
            this.m_21051_(Attributes.MAX_HEALTH).setBaseValue(8.0);
        }
        this.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.m_5496_(QuarkSounds.ENTITY_SHIBA_STEP, 0.15F, 1.0F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.f_19796_.nextInt(3) == 0) {
            return this.m_21223_() / this.m_21233_() < 0.5F ? QuarkSounds.ENTITY_SHIBA_WHINE : QuarkSounds.ENTITY_SHIBA_PANT;
        } else {
            return QuarkSounds.ENTITY_SHIBA_AMBIENT;
        }
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSourceIn) {
        return QuarkSounds.ENTITY_SHIBA_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return QuarkSounds.ENTITY_SHIBA_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.4F;
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob mate) {
        Shiba wolfentity = ShibaModule.shibaType.create(world);
        UUID uuid = this.m_21805_();
        if (uuid != null) {
            wolfentity.m_21816_(uuid);
            wolfentity.setTame(true);
        }
        return wolfentity;
    }
}