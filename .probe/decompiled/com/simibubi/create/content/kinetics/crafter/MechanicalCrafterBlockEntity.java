package com.simibubi.create.content.kinetics.crafter;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.edgeInteraction.EdgeInteractionBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.InvManipulationBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

public class MechanicalCrafterBlockEntity extends KineticBlockEntity {

    protected MechanicalCrafterBlockEntity.Inventory inventory;

    protected RecipeGridHandler.GroupedItems groupedItems = new RecipeGridHandler.GroupedItems();

    protected ConnectedInputHandler.ConnectedInput input = new ConnectedInputHandler.ConnectedInput();

    protected LazyOptional<IItemHandler> invSupplier = LazyOptional.of(() -> this.input.getItemHandler(this.f_58857_, this.f_58858_));

    protected boolean reRender;

    protected MechanicalCrafterBlockEntity.Phase phase;

    protected int countDown;

    protected boolean covered;

    protected boolean wasPoweredBefore;

    protected RecipeGridHandler.GroupedItems groupedItemsBeforeCraft;

    private InvManipulationBehaviour inserting;

    private EdgeInteractionBehaviour connectivity;

    private ItemStack scriptedResult = ItemStack.EMPTY;

    public MechanicalCrafterBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(20);
        this.phase = MechanicalCrafterBlockEntity.Phase.IDLE;
        this.groupedItemsBeforeCraft = new RecipeGridHandler.GroupedItems();
        this.inventory = new MechanicalCrafterBlockEntity.Inventory(this);
        this.wasPoweredBefore = true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.inserting = new InvManipulationBehaviour(this, this::getTargetFace);
        this.connectivity = new EdgeInteractionBehaviour(this, ConnectedInputHandler::toggleConnection).connectivity(ConnectedInputHandler::shouldConnect).require((Item) AllItems.WRENCH.get());
        behaviours.add(this.inserting);
        behaviours.add(this.connectivity);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.CRAFTER, AllAdvancements.CRAFTER_LAZY });
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (!Mth.equal(this.getSpeed(), 0.0F)) {
            this.award(AllAdvancements.CRAFTER);
            if (Math.abs(this.getSpeed()) < 5.0F) {
                this.award(AllAdvancements.CRAFTER_LAZY);
            }
        }
    }

    public void blockChanged() {
        this.removeBehaviour(InvManipulationBehaviour.TYPE);
        this.inserting = new InvManipulationBehaviour(this, this::getTargetFace);
        this.attachBehaviourLate(this.inserting);
    }

    public BlockFace getTargetFace(Level world, BlockPos pos, BlockState state) {
        return new BlockFace(pos, MechanicalCrafterBlock.getTargetDirection(state));
    }

    public Direction getTargetDirection() {
        return MechanicalCrafterBlock.getTargetDirection(this.m_58900_());
    }

    @Override
    public void writeSafe(CompoundTag compound) {
        super.writeSafe(compound);
        if (this.input != null) {
            CompoundTag inputNBT = new CompoundTag();
            this.input.write(inputNBT);
            compound.put("ConnectedInput", inputNBT);
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.put("Inventory", this.inventory.serializeNBT());
        CompoundTag inputNBT = new CompoundTag();
        this.input.write(inputNBT);
        compound.put("ConnectedInput", inputNBT);
        CompoundTag groupedItemsNBT = new CompoundTag();
        this.groupedItems.write(groupedItemsNBT);
        compound.put("GroupedItems", groupedItemsNBT);
        compound.putString("Phase", this.phase.name());
        compound.putInt("CountDown", this.countDown);
        compound.putBoolean("Cover", this.covered);
        super.write(compound, clientPacket);
        if (clientPacket && this.reRender) {
            compound.putBoolean("Redraw", true);
            this.reRender = false;
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        MechanicalCrafterBlockEntity.Phase phaseBefore = this.phase;
        RecipeGridHandler.GroupedItems before = this.groupedItems;
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        this.input.read(compound.getCompound("ConnectedInput"));
        this.groupedItems = RecipeGridHandler.GroupedItems.read(compound.getCompound("GroupedItems"));
        this.phase = MechanicalCrafterBlockEntity.Phase.IDLE;
        String name = compound.getString("Phase");
        for (MechanicalCrafterBlockEntity.Phase phase : MechanicalCrafterBlockEntity.Phase.values()) {
            if (phase.name().equals(name)) {
                this.phase = phase;
            }
        }
        this.countDown = compound.getInt("CountDown");
        this.covered = compound.getBoolean("Cover");
        super.read(compound, clientPacket);
        if (clientPacket) {
            if (compound.contains("Redraw")) {
                this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 16);
            }
            if (phaseBefore != this.phase && this.phase == MechanicalCrafterBlockEntity.Phase.CRAFTING) {
                this.groupedItemsBeforeCraft = before;
            }
            if (phaseBefore == MechanicalCrafterBlockEntity.Phase.EXPORTING && this.phase == MechanicalCrafterBlockEntity.Phase.WAITING) {
                Direction facing = (Direction) this.m_58900_().m_61143_(MechanicalCrafterBlock.HORIZONTAL_FACING);
                Vec3 vec = Vec3.atLowerCornerOf(facing.getNormal()).scale(0.75).add(VecHelper.getCenterOf(this.f_58858_));
                Direction targetDirection = MechanicalCrafterBlock.getTargetDirection(this.m_58900_());
                vec = vec.add(Vec3.atLowerCornerOf(targetDirection.getNormal()).scale(1.0));
                this.f_58857_.addParticle(ParticleTypes.CRIT, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void invalidate() {
        super.invalidate();
        this.invSupplier.invalidate();
    }

    public int getCountDownSpeed() {
        return this.getSpeed() == 0.0F ? 0 : Mth.clamp((int) Math.abs(this.getSpeed()), 4, 250);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.phase != MechanicalCrafterBlockEntity.Phase.ACCEPTING) {
            boolean onClient = this.f_58857_.isClientSide;
            boolean runLogic = !onClient || this.isVirtual();
            if (this.wasPoweredBefore != this.f_58857_.m_276867_(this.f_58858_)) {
                this.wasPoweredBefore = this.f_58857_.m_276867_(this.f_58858_);
                if (this.wasPoweredBefore) {
                    if (!runLogic) {
                        return;
                    }
                    this.checkCompletedRecipe(true);
                }
            }
            if (this.phase == MechanicalCrafterBlockEntity.Phase.ASSEMBLING) {
                this.countDown = this.countDown - this.getCountDownSpeed();
                if (this.countDown < 0) {
                    this.countDown = 0;
                    if (!runLogic) {
                        return;
                    }
                    if (RecipeGridHandler.getTargetingCrafter(this) != null) {
                        this.phase = MechanicalCrafterBlockEntity.Phase.EXPORTING;
                        this.countDown = 1000;
                        this.sendData();
                        return;
                    }
                    ItemStack result = this.isVirtual() ? this.scriptedResult : RecipeGridHandler.tryToApplyRecipe(this.f_58857_, this.groupedItems);
                    if (result == null) {
                        this.ejectWholeGrid();
                        return;
                    }
                    List<ItemStack> containers = new ArrayList();
                    this.groupedItems.grid.values().forEach(stackx -> {
                        if (stackx.hasCraftingRemainingItem()) {
                            containers.add(stackx.getCraftingRemainingItem().copy());
                        }
                    });
                    if (this.isVirtual()) {
                        this.groupedItemsBeforeCraft = this.groupedItems;
                    }
                    this.groupedItems = new RecipeGridHandler.GroupedItems(result);
                    for (int i = 0; i < containers.size(); i++) {
                        ItemStack stack = (ItemStack) containers.get(i);
                        RecipeGridHandler.GroupedItems container = new RecipeGridHandler.GroupedItems();
                        container.grid.put(Pair.of(i, 0), stack);
                        container.mergeOnto(this.groupedItems, Pointing.LEFT);
                    }
                    this.phase = MechanicalCrafterBlockEntity.Phase.CRAFTING;
                    this.countDown = 2000;
                    this.sendData();
                    return;
                }
            }
            if (this.phase == MechanicalCrafterBlockEntity.Phase.EXPORTING) {
                this.countDown = this.countDown - this.getCountDownSpeed();
                if (this.countDown < 0) {
                    this.countDown = 0;
                    if (!runLogic) {
                        return;
                    }
                    MechanicalCrafterBlockEntity targetingCrafter = RecipeGridHandler.getTargetingCrafter(this);
                    if (targetingCrafter == null) {
                        this.ejectWholeGrid();
                        return;
                    }
                    Pointing pointing = (Pointing) this.m_58900_().m_61143_(MechanicalCrafterBlock.POINTING);
                    this.groupedItems.mergeOnto(targetingCrafter.groupedItems, pointing);
                    this.groupedItems = new RecipeGridHandler.GroupedItems();
                    float pitch = (float) (targetingCrafter.groupedItems.grid.size() * 1) / 16.0F + 0.5F;
                    AllSoundEvents.CRAFTER_CLICK.playOnServer(this.f_58857_, this.f_58858_, 1.0F, pitch);
                    this.phase = MechanicalCrafterBlockEntity.Phase.WAITING;
                    this.countDown = 0;
                    this.sendData();
                    targetingCrafter.continueIfAllPrecedingFinished();
                    targetingCrafter.sendData();
                    return;
                }
            }
            if (this.phase == MechanicalCrafterBlockEntity.Phase.CRAFTING) {
                if (onClient) {
                    Direction facing = (Direction) this.m_58900_().m_61143_(MechanicalCrafterBlock.HORIZONTAL_FACING);
                    float progress = (float) this.countDown / 2000.0F;
                    Vec3 facingVec = Vec3.atLowerCornerOf(facing.getNormal());
                    Vec3 vec = facingVec.scale(0.65).add(VecHelper.getCenterOf(this.f_58858_));
                    Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, this.f_58857_.random, 0.125F).multiply(VecHelper.axisAlingedPlaneOf(facingVec)).normalize().scale((double) (progress * 0.5F)).add(vec);
                    if (progress > 0.5F) {
                        this.f_58857_.addParticle(ParticleTypes.CRIT, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
                    }
                    if (!this.groupedItemsBeforeCraft.grid.isEmpty() && progress < 0.5F && this.groupedItems.grid.containsKey(Pair.of(0, 0))) {
                        ItemStack stack = (ItemStack) this.groupedItems.grid.get(Pair.of(0, 0));
                        this.groupedItemsBeforeCraft = new RecipeGridHandler.GroupedItems();
                        for (int i = 0; i < 10; i++) {
                            Vec3 randVec = VecHelper.offsetRandomly(Vec3.ZERO, this.f_58857_.random, 0.125F).multiply(VecHelper.axisAlingedPlaneOf(facingVec)).normalize().scale(0.25);
                            Vec3 offset2 = randVec.add(vec);
                            randVec = randVec.scale(0.35F);
                            this.f_58857_.addParticle(new ItemParticleOption(ParticleTypes.ITEM, stack), offset2.x, offset2.y, offset2.z, randVec.x, randVec.y, randVec.z);
                        }
                    }
                }
                int prev = this.countDown;
                this.countDown = this.countDown - this.getCountDownSpeed();
                if (this.countDown < 1000 && prev >= 1000) {
                    AllSoundEvents.CRAFTER_CLICK.playOnServer(this.f_58857_, this.f_58858_, 1.0F, 2.0F);
                    AllSoundEvents.CRAFTER_CRAFT.playOnServer(this.f_58857_, this.f_58858_);
                }
                if (this.countDown < 0) {
                    this.countDown = 0;
                    if (!runLogic) {
                        return;
                    }
                    this.tryInsert();
                    return;
                }
            }
            if (this.phase == MechanicalCrafterBlockEntity.Phase.INSERTING) {
                if (runLogic && this.isTargetingBelt()) {
                    this.tryInsert();
                }
            }
        }
    }

    protected boolean isTargetingBelt() {
        DirectBeltInputBehaviour behaviour = this.getTargetingBelt();
        return behaviour != null && behaviour.canInsertFromSide(this.getTargetDirection());
    }

    protected DirectBeltInputBehaviour getTargetingBelt() {
        BlockPos targetPos = this.f_58858_.relative(this.getTargetDirection());
        return BlockEntityBehaviour.get(this.f_58857_, targetPos, DirectBeltInputBehaviour.TYPE);
    }

    public void tryInsert() {
        if (!this.inserting.hasInventory() && !this.isTargetingBelt()) {
            this.ejectWholeGrid();
        } else {
            boolean chagedPhase = this.phase != MechanicalCrafterBlockEntity.Phase.INSERTING;
            List<Pair<Integer, Integer>> inserted = new LinkedList();
            DirectBeltInputBehaviour behaviour = this.getTargetingBelt();
            for (Entry<Pair<Integer, Integer>, ItemStack> entry : this.groupedItems.grid.entrySet()) {
                Pair<Integer, Integer> pair = (Pair<Integer, Integer>) entry.getKey();
                ItemStack stack = (ItemStack) entry.getValue();
                BlockFace face = this.getTargetFace(this.f_58857_, this.f_58858_, this.m_58900_());
                ItemStack remainder = behaviour == null ? this.inserting.insert(stack.copy()) : behaviour.handleInsertion(stack, face.getFace(), false);
                if (!remainder.isEmpty()) {
                    stack.setCount(remainder.getCount());
                } else {
                    inserted.add(pair);
                }
            }
            inserted.forEach(this.groupedItems.grid::remove);
            if (this.groupedItems.grid.isEmpty()) {
                this.ejectWholeGrid();
            } else {
                this.phase = MechanicalCrafterBlockEntity.Phase.INSERTING;
            }
            if (!inserted.isEmpty() || chagedPhase) {
                this.sendData();
            }
        }
    }

    public void ejectWholeGrid() {
        List<MechanicalCrafterBlockEntity> chain = RecipeGridHandler.getAllCraftersOfChain(this);
        if (chain != null) {
            chain.forEach(MechanicalCrafterBlockEntity::eject);
        }
    }

    public void eject() {
        BlockState blockState = this.m_58900_();
        boolean present = AllBlocks.MECHANICAL_CRAFTER.has(blockState);
        Vec3 vec = present ? Vec3.atLowerCornerOf(((Direction) blockState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING)).getNormal()).scale(0.75) : Vec3.ZERO;
        Vec3 ejectPos = VecHelper.getCenterOf(this.f_58858_).add(vec);
        this.groupedItems.grid.forEach((pair, stack) -> this.dropItem(ejectPos, stack));
        if (!this.inventory.m_8020_(0).isEmpty()) {
            this.dropItem(ejectPos, this.inventory.m_8020_(0));
        }
        this.phase = MechanicalCrafterBlockEntity.Phase.IDLE;
        this.groupedItems = new RecipeGridHandler.GroupedItems();
        this.inventory.setStackInSlot(0, ItemStack.EMPTY);
        this.sendData();
    }

    public void dropItem(Vec3 ejectPos, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(this.f_58857_, ejectPos.x, ejectPos.y, ejectPos.z, stack);
        itemEntity.setDefaultPickUpDelay();
        this.f_58857_.m_7967_(itemEntity);
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        if (!this.f_58857_.isClientSide || this.isVirtual()) {
            if (this.phase == MechanicalCrafterBlockEntity.Phase.IDLE && this.craftingItemPresent()) {
                this.checkCompletedRecipe(false);
            }
            if (this.phase == MechanicalCrafterBlockEntity.Phase.INSERTING) {
                this.tryInsert();
            }
        }
    }

    public boolean craftingItemPresent() {
        return !this.inventory.m_8020_(0).isEmpty();
    }

    public boolean craftingItemOrCoverPresent() {
        return !this.inventory.m_8020_(0).isEmpty() || this.covered;
    }

    protected void checkCompletedRecipe(boolean poweredStart) {
        if (this.getSpeed() != 0.0F) {
            if (!this.f_58857_.isClientSide || this.isVirtual()) {
                List<MechanicalCrafterBlockEntity> chain = RecipeGridHandler.getAllCraftersOfChainIf(this, poweredStart ? MechanicalCrafterBlockEntity::craftingItemPresent : MechanicalCrafterBlockEntity::craftingItemOrCoverPresent, poweredStart);
                if (chain != null) {
                    chain.forEach(MechanicalCrafterBlockEntity::begin);
                }
            }
        }
    }

    protected void begin() {
        this.phase = MechanicalCrafterBlockEntity.Phase.ACCEPTING;
        this.groupedItems = new RecipeGridHandler.GroupedItems(this.inventory.m_8020_(0));
        this.inventory.setStackInSlot(0, ItemStack.EMPTY);
        if (RecipeGridHandler.getPrecedingCrafters(this).isEmpty()) {
            this.phase = MechanicalCrafterBlockEntity.Phase.ASSEMBLING;
            this.countDown = 500;
        }
        this.sendData();
    }

    protected void continueIfAllPrecedingFinished() {
        List<MechanicalCrafterBlockEntity> preceding = RecipeGridHandler.getPrecedingCrafters(this);
        if (preceding == null) {
            this.ejectWholeGrid();
        } else {
            for (MechanicalCrafterBlockEntity blockEntity : preceding) {
                if (blockEntity.phase != MechanicalCrafterBlockEntity.Phase.WAITING) {
                    return;
                }
            }
            this.phase = MechanicalCrafterBlockEntity.Phase.ASSEMBLING;
            this.countDown = Math.max(100, this.getCountDownSpeed() + 1);
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return this.isItemHandlerCap(cap) ? this.invSupplier.cast() : super.getCapability(cap, side);
    }

    public void connectivityChanged() {
        this.reRender = true;
        this.sendData();
        this.invSupplier.invalidate();
        this.invSupplier = LazyOptional.of(() -> this.input.getItemHandler(this.f_58857_, this.f_58858_));
    }

    public MechanicalCrafterBlockEntity.Inventory getInventory() {
        return this.inventory;
    }

    public void setScriptedResult(ItemStack scriptedResult) {
        this.scriptedResult = scriptedResult;
    }

    public static class Inventory extends SmartInventory {

        private MechanicalCrafterBlockEntity blockEntity;

        public Inventory(MechanicalCrafterBlockEntity blockEntity) {
            super(1, blockEntity, 1, false);
            this.blockEntity = blockEntity;
            this.forbidExtraction();
            this.whenContentsChanged(slot -> {
                if (!this.m_8020_(slot).isEmpty()) {
                    if (blockEntity.phase == MechanicalCrafterBlockEntity.Phase.IDLE) {
                        blockEntity.checkCompletedRecipe(false);
                    }
                }
            });
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (this.blockEntity.phase != MechanicalCrafterBlockEntity.Phase.IDLE) {
                return stack;
            } else if (this.blockEntity.covered) {
                return stack;
            } else {
                ItemStack insertItem = super.insertItem(slot, stack, simulate);
                if (insertItem.getCount() != stack.getCount() && !simulate) {
                    this.blockEntity.m_58904_().playSound(null, this.blockEntity.m_58899_(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.25F, 0.5F);
                }
                return insertItem;
            }
        }
    }

    static enum Phase {

        IDLE,
        ACCEPTING,
        ASSEMBLING,
        EXPORTING,
        WAITING,
        CRAFTING,
        INSERTING
    }
}