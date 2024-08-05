package com.mna.entities.constructs;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.entities.EntityInit;
import com.mna.items.ItemInit;
import com.mna.tools.InventoryUtilities;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class MagicBroom extends AbstractGolem {

    private static final float detection_radius = 16.0F;

    private static final float detection_height = 4.0F;

    private static final float max_distance_from_container = 48.0F;

    private static final float movement_speed = 0.4F;

    public static final int BROOM_TYPE_NORMAL = 0;

    public static final int BROOM_TYPE_VORTEX = 1;

    private static final Predicate<ItemEntity> ITEM_SELECTOR = e -> e.getPersistentData().contains("PreventRemoteMovement") ? false : !e.hasPickUpDelay() && e.m_6084_();

    private int deposit_counter = 0;

    private boolean depositing = false;

    private static final EntityDataAccessor<BlockPos> CONTAINER_POSITION = SynchedEntityData.defineId(MagicBroom.class, EntityDataSerializers.BLOCK_POS);

    private static final EntityDataAccessor<Integer> BROOM_TYPE = SynchedEntityData.defineId(MagicBroom.class, EntityDataSerializers.INT);

    private static final String KEY_TARGETPOS = "magic_broom_targetPosition";

    private static final String KEY_BROOM_TYPE = "magic_broom_type";

    private float moveCounter = 0.0F;

    private float moveRotation = 0.0F;

    public MagicBroom(EntityType<? extends AbstractGolem> type, Level worldIn) {
        super(type, worldIn);
        this.m_21553_(true);
    }

    public float getStepHeight() {
        return 1.8F;
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0).add(ForgeMod.SWIM_SPEED.get(), 2.0);
    }

    private void updateRotations() {
        if (Math.abs(this.m_20184_().length()) > 0.1F) {
            this.moveCounter += 0.3F;
            this.moveRotation = (float) Math.sin((double) this.moveCounter) - (float) Math.sin((double) (this.moveCounter - 1.0F));
            if ((int) this.moveCounter % 3 == 0 && this.m_9236_().isClientSide()) {
                this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GRASS_STEP, SoundSource.NEUTRAL, 0.25F, 1.0F, false);
            }
        } else {
            this.moveCounter = 1.57F;
            if (this.moveRotation > 0.0F) {
                this.moveRotation -= 0.1F;
                if (this.moveRotation < 0.0F) {
                    this.moveRotation = 0.0F;
                }
            } else if (this.moveRotation < 0.0F) {
                this.moveRotation += 0.1F;
                if (this.moveRotation > 0.0F) {
                    this.moveRotation = 0.0F;
                }
            }
        }
    }

    public float getRotation() {
        return this.moveRotation;
    }

    public MagicBroom(Level worldIn) {
        super(EntityInit.MAGIC_BROOM.get(), worldIn);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new MagicBroom.FindItemsGoal());
        this.f_21345_.addGoal(2, new MagicBroom.DepositItemsGoal());
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(CONTAINER_POSITION, BlockPos.ZERO);
        this.f_19804_.define(BROOM_TYPE, 0);
    }

    public int getBroomType() {
        return this.f_19804_.get(BROOM_TYPE);
    }

    public void setBroomType(int type) {
        this.f_19804_.set(BROOM_TYPE, type);
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.getBroomType() == 1) {
            if (this.m_9236_().isClientSide()) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.3F, 0.05F, 0.5);
            }
            this.m_9236_().m_45976_(ItemEntity.class, this.m_20191_().inflate(16.0)).forEach(e -> {
                if (e != null && e.m_6084_() && this.m_21574_().hasLineOfSight(e)) {
                    Vec3 tPos = e.m_20182_();
                    Vec3 cPos = this.m_20182_();
                    Vec3 delta = cPos.subtract(tPos).normalize();
                    e.m_20256_(delta.scale(0.15F));
                }
            });
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (player.m_6144_() && !this.m_9236_().isClientSide() && this.m_6084_()) {
            this.m_21553_(false);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            ItemEntity entity = new ItemEntity(this.m_9236_(), this.m_20185_(), this.m_20186_(), this.m_20189_(), new ItemStack(this.getBroomType() == 0 ? ItemInit.MAGIC_BROOM.get() : ItemInit.VORTEX_BROOM.get()));
            entity.setPickUpDelay(20);
            this.m_9236_().m_7967_(entity);
            this.dropAllDeathLoot(this.m_21225_());
            return InteractionResult.SUCCESS;
        } else {
            return super.m_7111_(player, vec, hand);
        }
    }

    public BlockPos getContainerPosition() {
        return this.f_19804_.get(CONTAINER_POSITION);
    }

    public void setContainerPosition(BlockPos pos) {
        this.f_19804_.set(CONTAINER_POSITION, pos);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos blockPos0, BlockState blockState1) {
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        ItemStack curStack = this.m_6844_(EquipmentSlot.MAINHAND);
        return curStack.isEmpty() ? true : curStack.getItem() == stack.getItem() && curStack.getCount() <= curStack.getMaxStackSize() - stack.getCount();
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        this.updateRotations();
        if (this.m_9236_().isClientSide() && Math.abs(this.m_20184_().length()) > 0.1F) {
            float particle_spread = 0.05F;
            Vec3 velocity = new Vec3(-0.05F + Math.random() * 0.1F, 0.025F, -0.05F + Math.random() * 0.1F);
            this.m_9236_().addParticle(new MAParticleType(ParticleInit.DUST.get()), this.m_20185_() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.m_20186_() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.m_20189_() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
        }
        if (this.m_6084_()) {
            for (ItemEntity itementity : this.m_9236_().m_45976_(ItemEntity.class, this.m_20191_().inflate(3.0, 0.25, 3.0))) {
                if (itementity.m_6084_() && !itementity.getItem().isEmpty() && !itementity.hasPickUpDelay()) {
                    this.pickUpItem(itementity);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source.is(DamageTypes.IN_WALL) ? false : super.m_6469_(source, amount);
    }

    @Override
    protected void pickUpItem(ItemEntity itemEntity) {
        if (!itemEntity.hasPickUpDelay() && !itemEntity.m_9236_().isClientSide()) {
            ItemStack mhStack = this.m_6844_(EquipmentSlot.MAINHAND);
            ItemStack groundStack = itemEntity.getItem();
            if (mhStack.isEmpty()) {
                this.m_8061_(EquipmentSlot.MAINHAND, groundStack);
                this.m_7938_(itemEntity, mhStack.getCount());
                itemEntity.m_142687_(Entity.RemovalReason.DISCARDED);
            } else if (ItemStack.matches(groundStack, mhStack)) {
                int space = mhStack.getMaxStackSize() - mhStack.getCount();
                int qtyCollected = Math.min(space, groundStack.getCount());
                ItemStack pickupStack = groundStack.split(qtyCollected);
                mhStack.setCount(mhStack.getCount() + pickupStack.getCount());
                this.m_8061_(EquipmentSlot.MAINHAND, mhStack);
                if (groundStack.getCount() <= 0) {
                    itemEntity.m_142687_(Entity.RemovalReason.DISCARDED);
                } else {
                    itemEntity.setItem(groundStack);
                }
            }
        }
    }

    @Override
    protected void dropAllDeathLoot(DamageSource damageSourceIn) {
        ItemStack itemstack = this.m_6844_(EquipmentSlot.MAINHAND);
        if (!itemstack.isEmpty()) {
            this.m_19983_(itemstack);
            this.m_8061_(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.m_7380_(compound);
        BlockPos target = this.getContainerPosition();
        CompoundTag targetPos = new CompoundTag();
        targetPos.putInt("x", target.m_123341_());
        targetPos.putInt("y", target.m_123342_());
        targetPos.putInt("z", target.m_123343_());
        compound.put("magic_broom_targetPosition", targetPos);
        compound.putInt("magic_broom_type", this.f_19804_.get(BROOM_TYPE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.m_7378_(compound);
        if (compound.contains("magic_broom_targetPosition")) {
            CompoundTag targetPos = compound.getCompound("magic_broom_targetPosition");
            this.setContainerPosition(new BlockPos(targetPos.getInt("x"), targetPos.getInt("y"), targetPos.getInt("z")));
        }
        if (compound.contains("magic_broom_type")) {
            this.f_19804_.set(BROOM_TYPE, compound.getInt("magic_broom_type"));
        }
    }

    class DepositItemsGoal extends Goal {

        private BlockEntity targetTE;

        private boolean abort = false;

        public DepositItemsGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.abort) {
                this.abort = false;
                return false;
            } else {
                return MagicBroom.this.m_6844_(EquipmentSlot.MAINHAND).isEmpty() ? false : MagicBroom.this.m_20183_().m_123314_(MagicBroom.this.getContainerPosition(), 48.0);
            }
        }

        @Override
        public void stop() {
            super.stop();
            MagicBroom.this.depositing = false;
            MagicBroom.this.deposit_counter = 10;
        }

        @Override
        public void start() {
            BlockPos target = MagicBroom.this.getContainerPosition();
            MagicBroom.this.m_21573_().moveTo((double) target.m_123341_(), (double) target.m_123342_(), (double) target.m_123343_(), 0.4F);
            this.targetTE = MagicBroom.this.m_20193_().getBlockEntity(MagicBroom.this.getContainerPosition());
            this.abort = false;
        }

        @Override
        public void tick() {
            if (this.targetTE == null) {
                this.abort = true;
            } else {
                LazyOptional<IItemHandler> handler = this.targetTE.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.UP);
                Player player = FakePlayerFactory.getMinecraft((ServerLevel) MagicBroom.this.m_9236_());
                if (!handler.isPresent()) {
                    this.abort = true;
                } else {
                    ItemStack activeStack = MagicBroom.this.m_6844_(EquipmentSlot.MAINHAND);
                    BlockPos target = MagicBroom.this.getContainerPosition();
                    double dist = MagicBroom.this.m_20275_((double) target.m_123341_(), (double) target.m_123342_(), (double) target.m_123343_());
                    if (dist < 4.0) {
                        MagicBroom.this.f_21344_.stop();
                        if (!MagicBroom.this.depositing) {
                            MagicBroom.this.depositing = true;
                            if (this.targetTE instanceof Container) {
                                ((Container) this.targetTE).startOpen(player);
                            }
                        }
                        if (MagicBroom.this.deposit_counter <= 0) {
                            MagicBroom.this.deposit_counter = 10;
                            if (InventoryUtilities.mergeIntoInventory((IItemHandler) handler.resolve().get(), activeStack, 10)) {
                                MagicBroom.this.depositing = false;
                            }
                        } else {
                            MagicBroom.this.deposit_counter--;
                        }
                        if (!MagicBroom.this.depositing && this.targetTE instanceof Container) {
                            ((Container) this.targetTE).stopOpen(player);
                        }
                    } else {
                        MagicBroom.this.m_21573_().moveTo((double) target.m_123341_(), (double) target.m_123342_(), (double) target.m_123343_(), 0.4F);
                    }
                }
            }
        }
    }

    class FindItemsGoal extends Goal {

        private BlockPos originPos;

        private boolean abort = false;

        private ArrayList<Integer> ignoredEntityIDs;

        private int stuck_count;

        private Vec3 last_pos;

        public FindItemsGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
            this.ignoredEntityIDs = new ArrayList();
        }

        @Override
        public boolean canUse() {
            if (MagicBroom.this.depositing) {
                return false;
            } else if (this.abort) {
                this.abort = false;
                return false;
            } else {
                List<ItemEntity> list = MagicBroom.this.m_9236_().m_6443_(ItemEntity.class, MagicBroom.this.m_20191_().inflate(16.0, 4.0, 16.0), e -> MagicBroom.this.m_21574_().hasLineOfSight(e) ? MagicBroom.ITEM_SELECTOR.test(e) : false);
                if (!list.isEmpty()) {
                    ItemStack stack = MagicBroom.this.m_6844_(EquipmentSlot.MAINHAND);
                    if (stack.getCount() == stack.getMaxStackSize()) {
                        return false;
                    }
                    if (stack.isEmpty()) {
                        return true;
                    }
                    for (ItemEntity item : list) {
                        if (item.getItem().getItem() == MagicBroom.this.m_6844_(EquipmentSlot.MAINHAND).getItem()) {
                            return true;
                        }
                    }
                }
                return false;
            }
        }

        @Override
        public void tick() {
            if (this.originPos != null && MagicBroom.this.m_9236_().isLoaded(this.originPos)) {
                if (this.last_pos != null && MagicBroom.this.m_20182_().distanceTo(this.last_pos) < 0.25) {
                    this.stuck_count++;
                } else {
                    this.stuck_count = 0;
                }
                ItemStack itemstack = MagicBroom.this.m_6844_(EquipmentSlot.MAINHAND);
                ItemEntity pickupTarget = null;
                if (itemstack.isEmpty()) {
                    List<ItemEntity> list = MagicBroom.this.m_9236_().m_6443_(ItemEntity.class, new AABB(this.originPos).inflate(16.0, 4.0, 16.0), e -> !this.ignoredEntityIDs.contains(e.m_19879_()) && MagicBroom.ITEM_SELECTOR.test(e));
                    if (!list.isEmpty()) {
                        pickupTarget = (ItemEntity) list.get(0);
                    }
                } else {
                    List<ItemEntity> list = MagicBroom.this.m_9236_().m_6443_(ItemEntity.class, new AABB(this.originPos).inflate(16.0, 4.0, 16.0), item -> !this.ignoredEntityIDs.contains(item.m_19879_()) && !item.hasPickUpDelay() && item.m_6084_() && item.getItem().getItem() == itemstack.getItem());
                    if (!list.isEmpty() && itemstack.getCount() < itemstack.getMaxStackSize()) {
                        pickupTarget = (ItemEntity) list.get(0);
                    }
                }
                if (pickupTarget != null && (this.stuck_count == 20 || !MagicBroom.this.m_21573_().moveTo(pickupTarget, 0.4F))) {
                    this.ignoredEntityIDs.add(pickupTarget.m_19879_());
                    this.stuck_count = 0;
                }
            } else {
                this.abort = true;
            }
        }

        @Override
        public void start() {
            this.ignoredEntityIDs.clear();
            this.stuck_count = 0;
            this.last_pos = null;
            this.originPos = MagicBroom.this.getContainerPosition();
            this.abort = false;
            List<ItemEntity> list = MagicBroom.this.m_9236_().m_6443_(ItemEntity.class, MagicBroom.this.m_20191_().inflate(16.0, 1.0, 16.0), e -> MagicBroom.this.m_21574_().hasLineOfSight(e) ? MagicBroom.ITEM_SELECTOR.test(e) : false);
            if (!list.isEmpty()) {
                MagicBroom.this.m_21573_().moveTo((Entity) list.get(0), 0.4F);
            }
        }
    }
}