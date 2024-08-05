package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlock;
import com.simibubi.create.content.kinetics.fan.EncasedFanBlockEntity;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChuteBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {

    float pull;

    float push;

    ItemStack item = ItemStack.EMPTY;

    LerpedFloat itemPosition = LerpedFloat.linear();

    ChuteItemHandler itemHandler = new ChuteItemHandler(this);

    LazyOptional<IItemHandler> lazyHandler = LazyOptional.of(() -> this.itemHandler);

    boolean canPickUpItems = false;

    float bottomPullDistance;

    float beltBelowOffset;

    TransportedItemStackHandlerBehaviour beltBelow;

    boolean updateAirFlow;

    int airCurrentUpdateCooldown;

    int entitySearchCooldown;

    VersionedInventoryTrackerBehaviour invVersionTracker;

    LazyOptional<IItemHandler> capAbove = LazyOptional.empty();

    LazyOptional<IItemHandler> capBelow = LazyOptional.empty();

    public ChuteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.bottomPullDistance = 0.0F;
        this.updateAirFlow = true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(new DirectBeltInputBehaviour(this).onlyInsertWhen(d -> this.canDirectlyInsertCached()));
        behaviours.add(this.invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CHUTE });
    }

    public boolean canDirectlyInsertCached() {
        return this.canPickUpItems;
    }

    private boolean canDirectlyInsert() {
        BlockState blockState = this.m_58900_();
        BlockState blockStateAbove = this.f_58857_.getBlockState(this.f_58858_.above());
        if (!AbstractChuteBlock.isChute(blockState)) {
            return false;
        } else if (AbstractChuteBlock.getChuteFacing(blockStateAbove) == Direction.DOWN) {
            return false;
        } else {
            return this.getItemMotion() > 0.0F && this.getInputChutes().isEmpty() ? false : AbstractChuteBlock.isOpenChute(blockState);
        }
    }

    @Override
    public void initialize() {
        super.initialize();
        this.onAdded();
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(this.f_58858_).expandTowards(0.0, -3.0, 0.0);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            this.canPickUpItems = this.canDirectlyInsert();
        }
        boolean clientSide = this.f_58857_ != null && this.f_58857_.isClientSide && !this.isVirtual();
        float itemMotion = this.getItemMotion();
        if (itemMotion != 0.0F && this.f_58857_ != null && this.f_58857_.isClientSide) {
            this.spawnParticles(itemMotion);
        }
        this.tickAirStreams(itemMotion);
        if (this.item.isEmpty() && !clientSide) {
            if (itemMotion < 0.0F) {
                this.handleInputFromAbove();
            }
            if (itemMotion > 0.0F) {
                this.handleInputFromBelow();
            }
        } else {
            float nextOffset = this.itemPosition.getValue() + itemMotion;
            if (itemMotion < 0.0F) {
                if (nextOffset < 0.5F) {
                    if (!this.handleDownwardOutput(true)) {
                        nextOffset = 0.5F;
                    } else if (nextOffset < 0.0F) {
                        this.handleDownwardOutput(clientSide);
                        nextOffset = this.itemPosition.getValue();
                    }
                }
            } else if (itemMotion > 0.0F && nextOffset > 0.5F) {
                if (!this.handleUpwardOutput(true)) {
                    nextOffset = 0.5F;
                } else if (nextOffset > 1.0F) {
                    this.handleUpwardOutput(clientSide);
                    nextOffset = this.itemPosition.getValue();
                }
            }
            this.itemPosition.setValue((double) nextOffset);
        }
    }

    private void updateAirFlow(float itemSpeed) {
        this.updateAirFlow = false;
        if (itemSpeed > 0.0F && this.f_58857_ != null && !this.f_58857_.isClientSide) {
            float speed = this.pull - this.push;
            this.beltBelow = null;
            float maxPullDistance;
            if (speed >= 128.0F) {
                maxPullDistance = 3.0F;
            } else if (speed >= 64.0F) {
                maxPullDistance = 2.0F;
            } else if (speed >= 32.0F) {
                maxPullDistance = 1.0F;
            } else {
                maxPullDistance = Mth.lerp(speed / 32.0F, 0.0F, 1.0F);
            }
            if (AbstractChuteBlock.isChute(this.f_58857_.getBlockState(this.f_58858_.below()))) {
                maxPullDistance = 0.0F;
            }
            float flowLimit = maxPullDistance;
            if (maxPullDistance > 0.0F) {
                flowLimit = AirCurrent.getFlowLimit(this.f_58857_, this.f_58858_, maxPullDistance, Direction.DOWN);
            }
            for (int i = 1; (float) i <= flowLimit + 1.0F; i++) {
                TransportedItemStackHandlerBehaviour behaviour = BlockEntityBehaviour.get(this.f_58857_, this.f_58858_.below(i), TransportedItemStackHandlerBehaviour.TYPE);
                if (behaviour != null) {
                    this.beltBelow = behaviour;
                    this.beltBelowOffset = (float) (i - 1);
                    break;
                }
            }
            this.bottomPullDistance = Math.max(0.0F, flowLimit);
        }
        this.sendData();
    }

    private void findEntities(float itemSpeed) {
        if ((!(this.bottomPullDistance <= 0.0F) || this.getItem().isEmpty()) && !(itemSpeed <= 0.0F) && this.f_58857_ != null && !this.f_58857_.isClientSide) {
            if (this.canCollectItemsFromBelow()) {
                Vec3 center = VecHelper.getCenterOf(this.f_58858_);
                AABB searchArea = new AABB(center.add(0.0, (double) (-this.bottomPullDistance) - 0.5, 0.0), center.add(0.0, -0.5, 0.0)).inflate(0.45F);
                for (ItemEntity itemEntity : this.f_58857_.m_45976_(ItemEntity.class, searchArea)) {
                    if (itemEntity.m_6084_()) {
                        ItemStack entityItem = itemEntity.getItem();
                        if (this.canAcceptItem(entityItem)) {
                            this.setItem(entityItem.copy(), (float) (itemEntity.m_20191_().getCenter().y - (double) this.f_58858_.m_123342_()));
                            itemEntity.m_146870_();
                            break;
                        }
                    }
                }
            }
        }
    }

    private void extractFromBelt(float itemSpeed) {
        if (!(itemSpeed <= 0.0F) && this.f_58857_ != null && !this.f_58857_.isClientSide) {
            if (this.getItem().isEmpty() && this.beltBelow != null) {
                this.beltBelow.handleCenteredProcessingOnAllItems(0.5F, ts -> {
                    if (this.canAcceptItem(ts.stack)) {
                        this.setItem(ts.stack.copy(), -this.beltBelowOffset);
                        return TransportedItemStackHandlerBehaviour.TransportedResult.removeItem();
                    } else {
                        return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                    }
                });
            }
        }
    }

    private void tickAirStreams(float itemSpeed) {
        if (!this.f_58857_.isClientSide && this.airCurrentUpdateCooldown-- <= 0) {
            this.airCurrentUpdateCooldown = AllConfigs.server().kinetics.fanBlockCheckRate.get();
            this.updateAirFlow = true;
        }
        if (this.updateAirFlow) {
            this.updateAirFlow(itemSpeed);
        }
        if (this.entitySearchCooldown-- <= 0 && this.item.isEmpty()) {
            this.entitySearchCooldown = 5;
            this.findEntities(itemSpeed);
        }
        this.extractFromBelt(itemSpeed);
    }

    public void blockBelowChanged() {
        this.updateAirFlow = true;
        this.capBelow = LazyOptional.empty();
    }

    private void spawnParticles(float itemMotion) {
        if (this.f_58857_ != null) {
            BlockState blockState = this.m_58900_();
            boolean up = itemMotion > 0.0F;
            float absMotion = up ? itemMotion : -itemMotion;
            if (blockState != null && AbstractChuteBlock.isChute(blockState)) {
                if (this.push != 0.0F || this.pull != 0.0F) {
                    if (up && AbstractChuteBlock.isOpenChute(blockState) && BlockHelper.noCollisionInSpace(this.f_58857_, this.f_58858_.above())) {
                        this.spawnAirFlow(1.0F, 2.0F, absMotion, 0.5F);
                    }
                    if (AbstractChuteBlock.getChuteFacing(blockState) == Direction.DOWN) {
                        if (AbstractChuteBlock.isTransparentChute(blockState)) {
                            this.spawnAirFlow(up ? 0.0F : 1.0F, up ? 1.0F : 0.0F, absMotion, 1.0F);
                        }
                        if (!up && BlockHelper.noCollisionInSpace(this.f_58857_, this.f_58858_.below())) {
                            this.spawnAirFlow(0.0F, -1.0F, absMotion, 0.5F);
                        }
                        if (up && this.canCollectItemsFromBelow() && this.bottomPullDistance > 0.0F) {
                            this.spawnAirFlow(-this.bottomPullDistance, 0.0F, absMotion, 2.0F);
                            this.spawnAirFlow(-this.bottomPullDistance, 0.0F, absMotion, 2.0F);
                        }
                    }
                }
            }
        }
    }

    private void spawnAirFlow(float verticalStart, float verticalEnd, float motion, float drag) {
        if (this.f_58857_ != null) {
            AirParticleData airParticleData = new AirParticleData(drag, motion);
            Vec3 origin = Vec3.atLowerCornerOf(this.f_58858_);
            float xOff = Create.RANDOM.nextFloat() * 0.5F + 0.25F;
            float zOff = Create.RANDOM.nextFloat() * 0.5F + 0.25F;
            Vec3 v = origin.add((double) xOff, (double) verticalStart, (double) zOff);
            Vec3 d = origin.add((double) xOff, (double) verticalEnd, (double) zOff).subtract(v);
            if (Create.RANDOM.nextFloat() < 2.0F * motion) {
                this.f_58857_.addAlwaysVisibleParticle(airParticleData, v.x, v.y, v.z, d.x, d.y, d.z);
            }
        }
    }

    private void handleInputFromAbove() {
        if (!this.capAbove.isPresent()) {
            this.capAbove = this.grabCapability(Direction.UP);
        }
        this.handleInput(this.capAbove.orElse(null), 1.0F);
    }

    private void handleInputFromBelow() {
        if (!this.capBelow.isPresent()) {
            this.capBelow = this.grabCapability(Direction.DOWN);
        }
        this.handleInput(this.capBelow.orElse(null), 0.0F);
    }

    private void handleInput(IItemHandler inv, float startLocation) {
        if (inv != null) {
            if (!this.invVersionTracker.stillWaiting(inv)) {
                Predicate<ItemStack> canAccept = this::canAcceptItem;
                int count = this.getExtractionAmount();
                ItemHelper.ExtractionCountMode mode = this.getExtractionMode();
                if (mode == ItemHelper.ExtractionCountMode.UPTO || !ItemHelper.extract(inv, canAccept, mode, count, true).isEmpty()) {
                    ItemStack extracted = ItemHelper.extract(inv, canAccept, mode, count, false);
                    if (!extracted.isEmpty()) {
                        this.setItem(extracted, startLocation);
                        return;
                    }
                }
                this.invVersionTracker.awaitNewVersion(inv);
            }
        }
    }

    private boolean handleDownwardOutput(boolean simulate) {
        BlockState blockState = this.m_58900_();
        ChuteBlockEntity targetChute = this.getTargetChute(blockState);
        Direction direction = AbstractChuteBlock.getChuteFacing(blockState);
        if (this.f_58857_ != null && direction != null && this.canOutputItems()) {
            if (!this.capBelow.isPresent()) {
                this.capBelow = this.grabCapability(Direction.DOWN);
            }
            if (this.capBelow.isPresent()) {
                if (this.f_58857_.isClientSide && !this.isVirtual()) {
                    return false;
                }
                IItemHandler inv = this.capBelow.orElse(null);
                if (this.invVersionTracker.stillWaiting(inv)) {
                    return false;
                }
                ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, this.item, simulate);
                ItemStack held = this.getItem();
                if (!simulate) {
                    this.setItem(remainder, this.itemPosition.getValue(0.0F));
                }
                if (remainder.getCount() != held.getCount()) {
                    return true;
                }
                this.invVersionTracker.awaitNewVersion(inv);
                if (direction == Direction.DOWN) {
                    return false;
                }
            }
            if (targetChute != null) {
                boolean canInsert = targetChute.canAcceptItem(this.item);
                if (!simulate && canInsert) {
                    targetChute.setItem(this.item, direction == Direction.DOWN ? 1.0F : 0.51F);
                    this.setItem(ItemStack.EMPTY);
                }
                return canInsert;
            } else if (direction.getAxis().isHorizontal()) {
                return false;
            } else if (FunnelBlock.getFunnelFacing(this.f_58857_.getBlockState(this.f_58858_.below())) == Direction.DOWN) {
                return false;
            } else if (Block.canSupportRigidBlock(this.f_58857_, this.f_58858_.below())) {
                return false;
            } else {
                if (!simulate) {
                    Vec3 dropVec = VecHelper.getCenterOf(this.f_58858_).add(0.0, -0.75, 0.0);
                    ItemEntity dropped = new ItemEntity(this.f_58857_, dropVec.x, dropVec.y, dropVec.z, this.item.copy());
                    dropped.setDefaultPickUpDelay();
                    dropped.m_20334_(0.0, -0.25, 0.0);
                    this.f_58857_.m_7967_(dropped);
                    this.setItem(ItemStack.EMPTY);
                }
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean handleUpwardOutput(boolean simulate) {
        BlockState stateAbove = this.f_58857_.getBlockState(this.f_58858_.above());
        if (this.f_58857_ != null && this.canOutputItems()) {
            if (AbstractChuteBlock.isOpenChute(this.m_58900_())) {
                if (!this.capAbove.isPresent()) {
                    this.capAbove = this.grabCapability(Direction.UP);
                }
                if (this.capAbove.isPresent()) {
                    if (this.f_58857_.isClientSide && !this.isVirtual() && !ChuteBlock.isChute(stateAbove)) {
                        return false;
                    }
                    int countBefore = this.item.getCount();
                    IItemHandler inv = this.capAbove.orElse(null);
                    if (this.invVersionTracker.stillWaiting(inv)) {
                        return false;
                    }
                    ItemStack remainder = ItemHandlerHelper.insertItemStacked(inv, this.item, simulate);
                    if (!simulate) {
                        this.item = remainder;
                    }
                    if (countBefore != remainder.getCount()) {
                        return true;
                    }
                    this.invVersionTracker.awaitNewVersion(inv);
                    return false;
                }
            }
            ChuteBlockEntity bestOutput = null;
            List<ChuteBlockEntity> inputChutes = this.getInputChutes();
            for (ChuteBlockEntity targetChute : inputChutes) {
                if (targetChute.canAcceptItem(this.item)) {
                    float itemMotion = targetChute.getItemMotion();
                    if (!(itemMotion < 0.0F) && (bestOutput == null || bestOutput.getItemMotion() < itemMotion)) {
                        bestOutput = targetChute;
                    }
                }
            }
            if (bestOutput != null) {
                if (!simulate) {
                    bestOutput.setItem(this.item, 0.0F);
                    this.setItem(ItemStack.EMPTY);
                }
                return true;
            } else if (FunnelBlock.getFunnelFacing(this.f_58857_.getBlockState(this.f_58858_.above())) == Direction.UP) {
                return false;
            } else if (BlockHelper.hasBlockSolidSide(stateAbove, this.f_58857_, this.f_58858_.above(), Direction.DOWN)) {
                return false;
            } else if (!inputChutes.isEmpty()) {
                return false;
            } else {
                if (!simulate) {
                    Vec3 dropVec = VecHelper.getCenterOf(this.f_58858_).add(0.0, 0.5, 0.0);
                    ItemEntity dropped = new ItemEntity(this.f_58857_, dropVec.x, dropVec.y, dropVec.z, this.item.copy());
                    dropped.setDefaultPickUpDelay();
                    dropped.m_20334_(0.0, (double) (this.getItemMotion() * 2.0F), 0.0);
                    this.f_58857_.m_7967_(dropped);
                    this.setItem(ItemStack.EMPTY);
                }
                return true;
            }
        } else {
            return false;
        }
    }

    protected boolean canAcceptItem(ItemStack stack) {
        return this.item.isEmpty();
    }

    protected int getExtractionAmount() {
        return 16;
    }

    protected ItemHelper.ExtractionCountMode getExtractionMode() {
        return ItemHelper.ExtractionCountMode.UPTO;
    }

    protected boolean canCollectItemsFromBelow() {
        return true;
    }

    protected boolean canOutputItems() {
        return true;
    }

    private LazyOptional<IItemHandler> grabCapability(Direction side) {
        BlockPos pos = this.f_58858_.relative(side);
        if (this.f_58857_ == null) {
            return LazyOptional.empty();
        } else {
            BlockEntity be = this.f_58857_.getBlockEntity(pos);
            if (be == null) {
                return LazyOptional.empty();
            } else {
                return !(be instanceof ChuteBlockEntity) || side == Direction.DOWN && be instanceof SmartChuteBlockEntity && !(this.getItemMotion() > 0.0F) ? be.getCapability(ForgeCapabilities.ITEM_HANDLER, side.getOpposite()) : LazyOptional.empty();
            }
        }
    }

    public void setItem(ItemStack stack) {
        this.setItem(stack, this.getItemMotion() < 0.0F ? 1.0F : 0.0F);
    }

    public void setItem(ItemStack stack, float insertionPos) {
        this.item = stack;
        this.itemPosition.startWithValue((double) insertionPos);
        this.invVersionTracker.reset();
        if (!this.f_58857_.isClientSide) {
            this.notifyUpdate();
            this.award(AllAdvancements.CHUTE);
        }
    }

    @Override
    public void invalidate() {
        if (this.lazyHandler != null) {
            this.lazyHandler.invalidate();
        }
        super.invalidate();
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("Item", this.item.serializeNBT());
        compound.putFloat("ItemPosition", this.itemPosition.getValue());
        compound.putFloat("Pull", this.pull);
        compound.putFloat("Push", this.push);
        compound.putFloat("BottomAirFlowDistance", this.bottomPullDistance);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        ItemStack previousItem = this.item;
        this.item = ItemStack.of(compound.getCompound("Item"));
        this.itemPosition.startWithValue((double) compound.getFloat("ItemPosition"));
        this.pull = compound.getFloat("Pull");
        this.push = compound.getFloat("Push");
        this.bottomPullDistance = compound.getFloat("BottomAirFlowDistance");
        super.read(compound, clientPacket);
        if (this.m_58898_() && this.f_58857_ != null && this.f_58857_.isClientSide && !previousItem.equals(this.item, false) && !this.item.isEmpty()) {
            if (this.f_58857_.random.nextInt(3) != 0) {
                return;
            }
            Vec3 p = VecHelper.getCenterOf(this.f_58858_);
            p = VecHelper.offsetRandomly(p, this.f_58857_.random, 0.5F);
            Vec3 m = Vec3.ZERO;
            this.f_58857_.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.item), p.x, p.y, p.z, m.x, m.y, m.z);
        }
    }

    public float getItemMotion() {
        float fanSpeedModifier = 0.015625F;
        float maxItemSpeed = 20.0F;
        float gravity = 4.0F;
        float motion = (this.push + this.pull) * 0.015625F;
        return (Mth.clamp(motion, -20.0F, 20.0F) + (motion <= 0.0F ? -4.0F : 0.0F)) / 20.0F;
    }

    @Override
    public void destroy() {
        super.destroy();
        ChuteBlockEntity targetChute = this.getTargetChute(this.m_58900_());
        List<ChuteBlockEntity> inputChutes = this.getInputChutes();
        if (!this.item.isEmpty() && this.f_58857_ != null) {
            Containers.dropItemStack(this.f_58857_, (double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_(), (double) this.f_58858_.m_123343_(), this.item);
        }
        this.m_7651_();
        if (targetChute != null) {
            targetChute.updatePull();
            targetChute.propagatePush();
        }
        inputChutes.forEach(c -> c.updatePush(inputChutes.size()));
    }

    public void onAdded() {
        this.refreshBlockState();
        this.updatePull();
        ChuteBlockEntity targetChute = this.getTargetChute(this.m_58900_());
        if (targetChute != null) {
            targetChute.propagatePush();
        } else {
            this.updatePush(1);
        }
    }

    public void updatePull() {
        float totalPull = this.calculatePull();
        if (this.pull != totalPull) {
            this.pull = totalPull;
            this.updateAirFlow = true;
            this.sendData();
            ChuteBlockEntity targetChute = this.getTargetChute(this.m_58900_());
            if (targetChute != null) {
                targetChute.updatePull();
            }
        }
    }

    public void updatePush(int branchCount) {
        float totalPush = this.calculatePush(branchCount);
        if (this.push != totalPush) {
            this.updateAirFlow = true;
            this.push = totalPush;
            this.sendData();
            this.propagatePush();
        }
    }

    public void propagatePush() {
        List<ChuteBlockEntity> inputs = this.getInputChutes();
        inputs.forEach(c -> c.updatePush(inputs.size()));
    }

    protected float calculatePull() {
        BlockState blockStateAbove = this.f_58857_.getBlockState(this.f_58858_.above());
        if (AllBlocks.ENCASED_FAN.has(blockStateAbove) && blockStateAbove.m_61143_(EncasedFanBlock.FACING) == Direction.DOWN) {
            BlockEntity be = this.f_58857_.getBlockEntity(this.f_58858_.above());
            if (be instanceof EncasedFanBlockEntity && !be.isRemoved()) {
                EncasedFanBlockEntity fan = (EncasedFanBlockEntity) be;
                return fan.getSpeed();
            }
        }
        float totalPull = 0.0F;
        for (Direction d : Iterate.directions) {
            ChuteBlockEntity inputChute = this.getInputChute(d);
            if (inputChute != null) {
                totalPull += inputChute.pull;
            }
        }
        return totalPull;
    }

    protected float calculatePush(int branchCount) {
        if (this.f_58857_ == null) {
            return 0.0F;
        } else {
            BlockState blockStateBelow = this.f_58857_.getBlockState(this.f_58858_.below());
            if (AllBlocks.ENCASED_FAN.has(blockStateBelow) && blockStateBelow.m_61143_(EncasedFanBlock.FACING) == Direction.UP) {
                BlockEntity be = this.f_58857_.getBlockEntity(this.f_58858_.below());
                if (be instanceof EncasedFanBlockEntity && !be.isRemoved()) {
                    EncasedFanBlockEntity fan = (EncasedFanBlockEntity) be;
                    return fan.getSpeed();
                }
            }
            ChuteBlockEntity targetChute = this.getTargetChute(this.m_58900_());
            return targetChute == null ? 0.0F : targetChute.push / (float) branchCount;
        }
    }

    @Nullable
    private ChuteBlockEntity getTargetChute(BlockState state) {
        if (this.f_58857_ == null) {
            return null;
        } else {
            Direction targetDirection = AbstractChuteBlock.getChuteFacing(state);
            if (targetDirection == null) {
                return null;
            } else {
                BlockPos chutePos = this.f_58858_.below();
                if (targetDirection.getAxis().isHorizontal()) {
                    chutePos = chutePos.relative(targetDirection.getOpposite());
                }
                BlockState chuteState = this.f_58857_.getBlockState(chutePos);
                if (!AbstractChuteBlock.isChute(chuteState)) {
                    return null;
                } else {
                    BlockEntity be = this.f_58857_.getBlockEntity(chutePos);
                    return be instanceof ChuteBlockEntity ? (ChuteBlockEntity) be : null;
                }
            }
        }
    }

    private List<ChuteBlockEntity> getInputChutes() {
        List<ChuteBlockEntity> inputs = new LinkedList();
        for (Direction d : Iterate.directions) {
            ChuteBlockEntity inputChute = this.getInputChute(d);
            if (inputChute != null) {
                inputs.add(inputChute);
            }
        }
        return inputs;
    }

    @Nullable
    private ChuteBlockEntity getInputChute(Direction direction) {
        if (this.f_58857_ != null && direction != Direction.DOWN) {
            direction = direction.getOpposite();
            BlockPos chutePos = this.f_58858_.above();
            if (direction.getAxis().isHorizontal()) {
                chutePos = chutePos.relative(direction);
            }
            BlockState chuteState = this.f_58857_.getBlockState(chutePos);
            Direction chuteFacing = AbstractChuteBlock.getChuteFacing(chuteState);
            if (chuteFacing != direction) {
                return null;
            } else {
                BlockEntity be = this.f_58857_.getBlockEntity(chutePos);
                return be instanceof ChuteBlockEntity && !be.isRemoved() ? (ChuteBlockEntity) be : null;
            }
        } else {
            return null;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        boolean downward = this.getItemMotion() < 0.0F;
        Lang.translate("tooltip.chute.header").forGoggles(tooltip);
        if (this.pull == 0.0F && this.push == 0.0F) {
            Lang.translate("tooltip.chute.no_fans_attached").style(ChatFormatting.GRAY).forGoggles(tooltip);
        }
        if (this.pull != 0.0F) {
            Lang.translate("tooltip.chute.fans_" + (this.pull > 0.0F ? "pull_up" : "push_down")).style(ChatFormatting.GRAY).forGoggles(tooltip);
        }
        if (this.push != 0.0F) {
            Lang.translate("tooltip.chute.fans_" + (this.push > 0.0F ? "push_up" : "pull_down")).style(ChatFormatting.GRAY).forGoggles(tooltip);
        }
        Lang.text("-> ").add(Lang.translate("tooltip.chute.items_move_" + (downward ? "down" : "up"))).style(ChatFormatting.YELLOW).forGoggles(tooltip);
        if (!this.item.isEmpty()) {
            Lang.translate("tooltip.chute.contains", Components.translatable(this.item.getDescriptionId()).getString(), this.item.getCount()).style(ChatFormatting.GREEN).forGoggles(tooltip);
        }
        return true;
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap == ForgeCapabilities.ITEM_HANDLER ? this.lazyHandler.cast() : super.getCapability(cap, side);
    }

    public ItemStack getItem() {
        return this.item;
    }
}