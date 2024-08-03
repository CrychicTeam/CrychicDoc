package com.simibubi.create.content.kinetics.deployer;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.equipment.sandPaper.SandPaperItem;
import com.simibubi.create.content.equipment.sandPaper.SandPaperPolishingRecipe;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class DeployerBlockEntity extends KineticBlockEntity {

    protected DeployerBlockEntity.State state;

    protected DeployerBlockEntity.Mode mode;

    protected ItemStack heldItem = ItemStack.EMPTY;

    protected DeployerFakePlayer player;

    protected int timer;

    protected float reach;

    protected boolean fistBump = false;

    protected List<ItemStack> overflowItems = new ArrayList();

    protected FilteringBehaviour filtering;

    protected boolean redstoneLocked;

    protected UUID owner;

    private LazyOptional<IItemHandlerModifiable> invHandler;

    private ListTag deferredInventoryList;

    private LerpedFloat animatedOffset;

    public BeltProcessingBehaviour processingBehaviour;

    RecipeWrapper recipeInv = new RecipeWrapper(new ItemStackHandler(2));

    SandPaperPolishingRecipe.SandPaperInv sandpaperInv = new SandPaperPolishingRecipe.SandPaperInv(ItemStack.EMPTY);

    public DeployerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.state = DeployerBlockEntity.State.WAITING;
        this.mode = DeployerBlockEntity.Mode.USE;
        this.heldItem = ItemStack.EMPTY;
        this.redstoneLocked = false;
        this.animatedOffset = LerpedFloat.linear().startWithValue(0.0);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.filtering = new FilteringBehaviour(this, new DeployerFilterSlot());
        behaviours.add(this.filtering);
        this.processingBehaviour = new BeltProcessingBehaviour(this).whenItemEnters((s, i) -> BeltDeployerCallbacks.onItemReceived(s, i, this)).whileItemHeld((s, i) -> BeltDeployerCallbacks.whenItemHeld(s, i, this));
        behaviours.add(this.processingBehaviour);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.TRAIN_CASING, AllAdvancements.ANDESITE_CASING, AllAdvancements.BRASS_CASING, AllAdvancements.COPPER_CASING, AllAdvancements.FIST_BUMP, AllAdvancements.DEPLOYER, AllAdvancements.SELF_DEPLOYING });
    }

    @Override
    public void initialize() {
        super.initialize();
        this.initHandler();
    }

    private void initHandler() {
        if (this.invHandler == null) {
            if (this.f_58857_ instanceof ServerLevel sLevel) {
                this.player = new DeployerFakePlayer(sLevel, this.owner);
                if (this.deferredInventoryList != null) {
                    this.player.m_150109_().load(this.deferredInventoryList);
                    this.deferredInventoryList = null;
                    this.heldItem = this.player.m_21205_();
                    this.sendData();
                }
                Vec3 initialPos = VecHelper.getCenterOf(this.f_58858_.relative((Direction) this.m_58900_().m_61143_(DirectionalKineticBlock.FACING)));
                this.player.m_6034_(initialPos.x, initialPos.y, initialPos.z);
            }
            this.invHandler = LazyOptional.of(this::createHandler);
        }
    }

    protected void onExtract(ItemStack stack) {
        this.player.m_21008_(InteractionHand.MAIN_HAND, stack.copy());
        this.sendData();
        this.m_6596_();
    }

    protected int getTimerSpeed() {
        return (int) (this.getSpeed() == 0.0F ? 0.0F : Mth.clamp(Math.abs(this.getSpeed() * 2.0F), 8.0F, 512.0F));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSpeed() != 0.0F) {
            if (!this.f_58857_.isClientSide && this.player != null && this.player.blockBreakingProgress != null && this.f_58857_.m_46859_((BlockPos) this.player.blockBreakingProgress.getKey())) {
                this.f_58857_.destroyBlockProgress(this.player.m_19879_(), (BlockPos) this.player.blockBreakingProgress.getKey(), -1);
                this.player.blockBreakingProgress = null;
            }
            if (this.timer > 0) {
                this.timer = this.timer - this.getTimerSpeed();
            } else if (!this.f_58857_.isClientSide) {
                ItemStack stack = this.player.m_21205_();
                if (this.state != DeployerBlockEntity.State.WAITING) {
                    if (this.state == DeployerBlockEntity.State.EXPANDING) {
                        if (this.fistBump) {
                            this.triggerFistBump();
                        }
                        this.activate();
                        this.state = DeployerBlockEntity.State.RETRACTING;
                        this.timer = 1000;
                        this.sendData();
                    } else if (this.state == DeployerBlockEntity.State.RETRACTING) {
                        this.state = DeployerBlockEntity.State.WAITING;
                        this.timer = 500;
                        this.sendData();
                    }
                } else if (!this.overflowItems.isEmpty()) {
                    this.timer = this.getTimerSpeed() * 10;
                } else {
                    boolean changed = false;
                    Inventory inventory = this.player.m_150109_();
                    for (int i = 0; i < inventory.getContainerSize() && this.overflowItems.size() <= 10; i++) {
                        ItemStack item = inventory.getItem(i);
                        if (!item.isEmpty() && (item != stack || !this.filtering.test(item))) {
                            this.overflowItems.add(item);
                            inventory.setItem(i, ItemStack.EMPTY);
                            changed = true;
                        }
                    }
                    if (changed) {
                        this.sendData();
                        this.timer = this.getTimerSpeed() * 10;
                    } else {
                        Direction facing = (Direction) this.m_58900_().m_61143_(DirectionalKineticBlock.FACING);
                        if (this.mode == DeployerBlockEntity.Mode.USE && !DeployerHandler.shouldActivate(stack, this.f_58857_, this.f_58858_.relative(facing, 2), facing)) {
                            this.timer = this.getTimerSpeed() * 10;
                        } else if (this.mode != DeployerBlockEntity.Mode.PUNCH || this.fistBump || !this.startFistBump(facing)) {
                            if (!this.redstoneLocked) {
                                this.start();
                            }
                        }
                    }
                }
            }
        }
    }

    protected void start() {
        this.state = DeployerBlockEntity.State.EXPANDING;
        Vec3 movementVector = this.getMovementVector();
        Vec3 rayOrigin = VecHelper.getCenterOf(this.f_58858_).add(movementVector.scale(1.5));
        Vec3 rayTarget = VecHelper.getCenterOf(this.f_58858_).add(movementVector.scale(2.5));
        ClipContext rayTraceContext = new ClipContext(rayOrigin, rayTarget, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, this.player);
        BlockHitResult result = this.f_58857_.m_45547_(rayTraceContext);
        this.reach = (float) (0.5 + Math.min(result.m_82450_().subtract(rayOrigin).length(), 0.75));
        this.timer = 1000;
        this.sendData();
    }

    public boolean startFistBump(Direction facing) {
        int i = 0;
        DeployerBlockEntity partner = null;
        for (i = 2; i < 5; i++) {
            BlockPos otherDeployer = this.f_58858_.relative(facing, i);
            if (!this.f_58857_.isLoaded(otherDeployer)) {
                return false;
            }
            if (this.f_58857_.getBlockEntity(otherDeployer) instanceof DeployerBlockEntity dpe) {
                partner = dpe;
                break;
            }
        }
        if (partner == null) {
            return false;
        } else if (((Direction) this.f_58857_.getBlockState(partner.m_58899_()).m_61143_(DirectionalKineticBlock.FACING)).getOpposite() == facing && partner.mode == DeployerBlockEntity.Mode.PUNCH) {
            if (partner.getSpeed() == 0.0F) {
                return false;
            } else {
                for (DeployerBlockEntity be : Arrays.asList(this, partner)) {
                    be.fistBump = true;
                    be.reach = (float) (i - 2) * 0.5F;
                    be.timer = 1000;
                    be.state = DeployerBlockEntity.State.EXPANDING;
                    be.sendData();
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public void triggerFistBump() {
        int i = 0;
        DeployerBlockEntity deployerBlockEntity = null;
        for (int var6 = 2; var6 < 5; var6++) {
            BlockPos pos = this.f_58858_.relative((Direction) this.m_58900_().m_61143_(DirectionalKineticBlock.FACING), var6);
            if (!this.f_58857_.isLoaded(pos)) {
                return;
            }
            if (this.f_58857_.getBlockEntity(pos) instanceof DeployerBlockEntity dpe) {
                deployerBlockEntity = dpe;
                break;
            }
        }
        if (deployerBlockEntity != null) {
            if (deployerBlockEntity.fistBump && deployerBlockEntity.state == DeployerBlockEntity.State.EXPANDING) {
                if (deployerBlockEntity.timer <= 0) {
                    this.fistBump = false;
                    deployerBlockEntity.fistBump = false;
                    deployerBlockEntity.state = DeployerBlockEntity.State.RETRACTING;
                    deployerBlockEntity.timer = 1000;
                    deployerBlockEntity.sendData();
                    this.award(AllAdvancements.FIST_BUMP);
                    BlockPos soundLocation = BlockPos.containing(Vec3.atCenterOf(this.f_58858_).add(Vec3.atCenterOf(deployerBlockEntity.m_58899_())).scale(0.5));
                    this.f_58857_.playSound(null, soundLocation, SoundEvents.PLAYER_ATTACK_NODAMAGE, SoundSource.BLOCKS, 0.75F, 0.75F);
                }
            }
        }
    }

    protected void activate() {
        Vec3 movementVector = this.getMovementVector();
        Direction direction = (Direction) this.m_58900_().m_61143_(DirectionalKineticBlock.FACING);
        Vec3 center = VecHelper.getCenterOf(this.f_58858_);
        BlockPos clickedPos = this.f_58858_.relative(direction, 2);
        this.player.m_146926_(direction == Direction.UP ? -90.0F : (direction == Direction.DOWN ? 90.0F : 0.0F));
        this.player.m_146922_(direction.toYRot());
        if (direction != Direction.DOWN || BlockEntityBehaviour.get(this.f_58857_, clickedPos, TransportedItemStackHandlerBehaviour.TYPE) == null) {
            DeployerHandler.activate(this.player, center, clickedPos, movementVector, this.mode);
            this.award(AllAdvancements.DEPLOYER);
            if (this.player != null) {
                this.heldItem = this.player.m_21205_();
            }
        }
    }

    protected Vec3 getMovementVector() {
        return !AllBlocks.DEPLOYER.has(this.m_58900_()) ? Vec3.ZERO : Vec3.atLowerCornerOf(((Direction) this.m_58900_().m_61143_(DirectionalKineticBlock.FACING)).getNormal());
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.state = NBTHelper.readEnum(compound, "State", DeployerBlockEntity.State.class);
        this.mode = NBTHelper.readEnum(compound, "Mode", DeployerBlockEntity.Mode.class);
        this.timer = compound.getInt("Timer");
        this.redstoneLocked = compound.getBoolean("Powered");
        if (compound.contains("Owner")) {
            this.owner = compound.getUUID("Owner");
        }
        this.deferredInventoryList = compound.getList("Inventory", 10);
        this.overflowItems = NBTHelper.readItemList(compound.getList("Overflow", 10));
        if (compound.contains("HeldItem")) {
            this.heldItem = ItemStack.of(compound.getCompound("HeldItem"));
        }
        super.read(compound, clientPacket);
        if (clientPacket) {
            this.fistBump = compound.getBoolean("Fistbump");
            this.reach = compound.getFloat("Reach");
            if (compound.contains("Particle")) {
                ItemStack particleStack = ItemStack.of(compound.getCompound("Particle"));
                SandPaperItem.spawnParticles(VecHelper.getCenterOf(this.f_58858_).add(this.getMovementVector().scale((double) (this.reach + 1.0F))), particleStack, this.f_58857_);
            }
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        NBTHelper.writeEnum(compound, "Mode", this.mode);
        NBTHelper.writeEnum(compound, "State", this.state);
        compound.putInt("Timer", this.timer);
        compound.putBoolean("Powered", this.redstoneLocked);
        if (this.owner != null) {
            compound.putUUID("Owner", this.owner);
        }
        if (this.player != null) {
            ListTag invNBT = new ListTag();
            this.player.m_150109_().save(invNBT);
            compound.put("Inventory", invNBT);
            compound.put("HeldItem", this.player.m_21205_().serializeNBT());
            compound.put("Overflow", NBTHelper.writeItemList(this.overflowItems));
        } else if (this.deferredInventoryList != null) {
            compound.put("Inventory", this.deferredInventoryList);
        }
        super.write(compound, clientPacket);
        if (clientPacket) {
            compound.putBoolean("Fistbump", this.fistBump);
            compound.putFloat("Reach", this.reach);
            if (this.player != null) {
                compound.put("HeldItem", this.player.m_21205_().serializeNBT());
                if (this.player.spawnedItemEffects != null) {
                    compound.put("Particle", this.player.spawnedItemEffects.serializeNBT());
                    this.player.spawnedItemEffects = null;
                }
            }
        }
    }

    @Override
    public void writeSafe(CompoundTag tag) {
        NBTHelper.writeEnum(tag, "Mode", this.mode);
        super.writeSafe(tag);
    }

    private IItemHandlerModifiable createHandler() {
        return new DeployerItemHandler(this);
    }

    public void redstoneUpdate() {
        if (!this.f_58857_.isClientSide) {
            boolean blockPowered = this.f_58857_.m_276867_(this.f_58858_);
            if (blockPowered != this.redstoneLocked) {
                this.redstoneLocked = blockPowered;
                this.sendData();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public PartialModel getHandPose() {
        return this.mode == DeployerBlockEntity.Mode.PUNCH ? AllPartialModels.DEPLOYER_HAND_PUNCHING : (this.heldItem.isEmpty() ? AllPartialModels.DEPLOYER_HAND_POINTING : AllPartialModels.DEPLOYER_HAND_HOLDING);
    }

    @Override
    protected AABB createRenderBoundingBox() {
        return super.createRenderBoundingBox().inflate(3.0);
    }

    public void discardPlayer() {
        if (this.player != null) {
            this.player.m_150109_().dropAll();
            this.overflowItems.forEach(itemstack -> this.player.m_7197_(itemstack, true, false));
            this.player.m_146870_();
            this.player = null;
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        if (this.invHandler != null) {
            this.invHandler.invalidate();
        }
    }

    public void changeMode() {
        this.mode = this.mode == DeployerBlockEntity.Mode.PUNCH ? DeployerBlockEntity.Mode.USE : DeployerBlockEntity.Mode.PUNCH;
        this.m_6596_();
        this.sendData();
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.isItemHandlerCap(cap)) {
            if (this.invHandler == null) {
                this.initHandler();
            }
            return this.invHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @Override
    public boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        if (super.addToTooltip(tooltip, isPlayerSneaking)) {
            return true;
        } else if (this.getSpeed() == 0.0F) {
            return false;
        } else if (this.overflowItems.isEmpty()) {
            return false;
        } else {
            TooltipHelper.addHint(tooltip, "hint.full_deployer");
            return true;
        }
    }

    @Override
    public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        Lang.translate("tooltip.deployer.header").forGoggles(tooltip);
        Lang.translate("tooltip.deployer." + (this.mode == DeployerBlockEntity.Mode.USE ? "using" : "punching")).style(ChatFormatting.YELLOW).forGoggles(tooltip);
        if (!this.heldItem.isEmpty()) {
            Lang.translate("tooltip.deployer.contains", Components.translatable(this.heldItem.getDescriptionId()).getString(), this.heldItem.getCount()).style(ChatFormatting.GREEN).forGoggles(tooltip);
        }
        float stressAtBase = this.calculateStressApplied();
        if (IRotate.StressImpact.isEnabled() && !Mth.equal(stressAtBase, 0.0F)) {
            tooltip.add(Components.immutableEmpty());
            this.addStressImpactStats(tooltip, stressAtBase);
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public float getHandOffset(float partialTicks) {
        if (this.isVirtual()) {
            return this.animatedOffset.getValue(partialTicks);
        } else {
            float progress = 0.0F;
            int timerSpeed = this.getTimerSpeed();
            PartialModel handPose = this.getHandPose();
            if (this.state == DeployerBlockEntity.State.EXPANDING) {
                progress = 1.0F - ((float) this.timer - partialTicks * (float) timerSpeed) / 1000.0F;
                if (this.fistBump) {
                    progress *= progress;
                }
            }
            if (this.state == DeployerBlockEntity.State.RETRACTING) {
                progress = ((float) this.timer - partialTicks * (float) timerSpeed) / 1000.0F;
            }
            float handLength = handPose == AllPartialModels.DEPLOYER_HAND_POINTING ? 0.0F : (handPose == AllPartialModels.DEPLOYER_HAND_HOLDING ? 0.25F : 0.1875F);
            return Math.min(Mth.clamp(progress, 0.0F, 1.0F) * (this.reach + handLength), 1.3125F);
        }
    }

    public void setAnimatedOffset(float offset) {
        this.animatedOffset.setValue((double) offset);
    }

    @Nullable
    public Recipe<? extends Container> getRecipe(ItemStack stack) {
        if (this.player != null && this.f_58857_ != null) {
            ItemStack heldItemMainhand = this.player.m_21205_();
            if (heldItemMainhand.getItem() instanceof SandPaperItem) {
                this.sandpaperInv.m_6836_(0, stack);
                return (Recipe<? extends Container>) AllRecipeTypes.SANDPAPER_POLISHING.find(this.sandpaperInv, this.f_58857_).orElse(null);
            } else {
                this.recipeInv.setItem(0, stack);
                this.recipeInv.setItem(1, heldItemMainhand);
                DeployerRecipeSearchEvent event = new DeployerRecipeSearchEvent(this, this.recipeInv);
                event.addRecipe(() -> SequencedAssemblyRecipe.getRecipe(this.f_58857_, event.getInventory(), AllRecipeTypes.DEPLOYING.getType(), DeployerApplicationRecipe.class), 100);
                event.addRecipe(() -> AllRecipeTypes.DEPLOYING.find(event.getInventory(), this.f_58857_), 50);
                event.addRecipe(() -> AllRecipeTypes.ITEM_APPLICATION.find(event.getInventory(), this.f_58857_), 50);
                MinecraftForge.EVENT_BUS.post(event);
                return event.getRecipe();
            }
        } else {
            return null;
        }
    }

    public DeployerFakePlayer getPlayer() {
        return this.player;
    }

    static enum Mode {

        PUNCH, USE
    }

    static enum State {

        WAITING, EXPANDING, RETRACTING, DUMPING
    }
}