package com.simibubi.create.content.schematics.cannon;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.schematics.SchematicPrinter;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.ItemHelper;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CSchematics;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class SchematicannonBlockEntity extends SmartBlockEntity implements MenuProvider {

    public static final int NEIGHBOUR_CHECKING = 100;

    public static final int MAX_ANCHOR_DISTANCE = 256;

    public SchematicannonInventory inventory;

    public boolean sendUpdate;

    public boolean dontUpdateChecklist;

    public int neighbourCheckCooldown;

    public SchematicPrinter printer;

    public ItemStack missingItem;

    public boolean positionNotLoaded;

    public boolean hasCreativeCrate;

    private int printerCooldown;

    private int skipsLeft;

    private boolean blockSkipped;

    public BlockPos previousTarget;

    public LinkedHashSet<LazyOptional<IItemHandler>> attachedInventories;

    public List<LaunchedItem> flyingBlocks;

    public MaterialChecklist checklist;

    public float fuelLevel;

    public float bookPrintingProgress;

    public float schematicProgress;

    public String statusMsg;

    public SchematicannonBlockEntity.State state;

    public int blocksPlaced;

    public int blocksToPlace;

    public int replaceMode;

    public boolean skipMissing;

    public boolean replaceBlockEntities;

    public boolean firstRenderTick;

    public float defaultYaw;

    public SchematicannonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.setLazyTickRate(30);
        this.attachedInventories = new LinkedHashSet();
        this.flyingBlocks = new LinkedList();
        this.inventory = new SchematicannonInventory(this);
        this.statusMsg = "idle";
        this.state = SchematicannonBlockEntity.State.STOPPED;
        this.replaceMode = 2;
        this.checklist = new MaterialChecklist();
        this.printer = new SchematicPrinter();
    }

    public void findInventories() {
        this.hasCreativeCrate = false;
        this.attachedInventories.clear();
        for (Direction facing : Iterate.directions) {
            if (this.f_58857_.isLoaded(this.f_58858_.relative(facing))) {
                if (AllBlocks.CREATIVE_CRATE.has(this.f_58857_.getBlockState(this.f_58858_.relative(facing)))) {
                    this.hasCreativeCrate = true;
                }
                BlockEntity blockEntity = this.f_58857_.getBlockEntity(this.f_58858_.relative(facing));
                if (blockEntity != null) {
                    LazyOptional<IItemHandler> capability = blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, facing.getOpposite());
                    if (capability.isPresent()) {
                        this.attachedInventories.add(capability);
                    }
                }
            }
        }
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        if (!clientPacket) {
            this.inventory.deserializeNBT(compound.getCompound("Inventory"));
        }
        this.statusMsg = compound.getString("Status");
        this.schematicProgress = compound.getFloat("Progress");
        this.bookPrintingProgress = compound.getFloat("PaperProgress");
        this.fuelLevel = compound.getFloat("Fuel");
        String stateString = compound.getString("State");
        this.state = stateString.isEmpty() ? SchematicannonBlockEntity.State.STOPPED : SchematicannonBlockEntity.State.valueOf(compound.getString("State"));
        this.blocksPlaced = compound.getInt("AmountPlaced");
        this.blocksToPlace = compound.getInt("AmountToPlace");
        this.missingItem = null;
        if (compound.contains("MissingItem")) {
            this.missingItem = ItemStack.of(compound.getCompound("MissingItem"));
        }
        CompoundTag options = compound.getCompound("Options");
        this.replaceMode = options.getInt("ReplaceMode");
        this.skipMissing = options.getBoolean("SkipMissing");
        this.replaceBlockEntities = options.getBoolean("ReplaceTileEntities");
        if (compound.contains("Printer")) {
            this.printer.fromTag(compound.getCompound("Printer"), clientPacket);
        }
        if (compound.contains("FlyingBlocks")) {
            this.readFlyingBlocks(compound);
        }
        this.defaultYaw = compound.getFloat("DefaultYaw");
        super.read(compound, clientPacket);
    }

    protected void readFlyingBlocks(CompoundTag compound) {
        ListTag tagBlocks = compound.getList("FlyingBlocks", 10);
        if (tagBlocks.isEmpty()) {
            this.flyingBlocks.clear();
        }
        boolean pastDead = false;
        for (int i = 0; i < tagBlocks.size(); i++) {
            CompoundTag c = tagBlocks.getCompound(i);
            LaunchedItem launched = LaunchedItem.fromNBT(c, this.blockHolderGetter());
            BlockPos readBlockPos = launched.target;
            if (this.f_58857_ != null && this.f_58857_.isClientSide) {
                while (!pastDead && !this.flyingBlocks.isEmpty() && !((LaunchedItem) this.flyingBlocks.get(0)).target.equals(readBlockPos)) {
                    this.flyingBlocks.remove(0);
                }
                pastDead = true;
                if (i >= this.flyingBlocks.size()) {
                    this.flyingBlocks.add(launched);
                }
            } else {
                this.flyingBlocks.add(launched);
            }
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (!clientPacket) {
            compound.put("Inventory", this.inventory.serializeNBT());
            if (this.state == SchematicannonBlockEntity.State.RUNNING) {
                compound.putBoolean("Running", true);
            }
        }
        compound.putFloat("Progress", this.schematicProgress);
        compound.putFloat("PaperProgress", this.bookPrintingProgress);
        compound.putFloat("Fuel", this.fuelLevel);
        compound.putString("Status", this.statusMsg);
        compound.putString("State", this.state.name());
        compound.putInt("AmountPlaced", this.blocksPlaced);
        compound.putInt("AmountToPlace", this.blocksToPlace);
        if (this.missingItem != null) {
            compound.put("MissingItem", this.missingItem.serializeNBT());
        }
        CompoundTag options = new CompoundTag();
        options.putInt("ReplaceMode", this.replaceMode);
        options.putBoolean("SkipMissing", this.skipMissing);
        options.putBoolean("ReplaceTileEntities", this.replaceBlockEntities);
        compound.put("Options", options);
        CompoundTag printerData = new CompoundTag();
        this.printer.write(printerData);
        compound.put("Printer", printerData);
        ListTag tagFlyingBlocks = new ListTag();
        for (LaunchedItem b : this.flyingBlocks) {
            tagFlyingBlocks.add(b.serializeNBT());
        }
        compound.put("FlyingBlocks", tagFlyingBlocks);
        compound.putFloat("DefaultYaw", this.defaultYaw);
        super.write(compound, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.state != SchematicannonBlockEntity.State.STOPPED && this.neighbourCheckCooldown-- <= 0) {
            this.neighbourCheckCooldown = 100;
            this.findInventories();
        }
        this.firstRenderTick = true;
        this.previousTarget = this.printer.getCurrentTarget();
        this.tickFlyingBlocks();
        if (!this.f_58857_.isClientSide) {
            this.tickPaperPrinter();
            this.refillFuelIfPossible();
            this.skipsLeft = 1000;
            this.blockSkipped = true;
            while (this.blockSkipped && this.skipsLeft-- > 0) {
                this.tickPrinter();
            }
            this.schematicProgress = 0.0F;
            if (this.blocksToPlace > 0) {
                this.schematicProgress = (float) this.blocksPlaced / (float) this.blocksToPlace;
            }
            if (this.sendUpdate) {
                this.sendUpdate = false;
                this.f_58857_.sendBlockUpdated(this.f_58858_, this.m_58900_(), this.m_58900_(), 6);
            }
        }
    }

    public CSchematics config() {
        return AllConfigs.server().schematics;
    }

    protected void tickPrinter() {
        ItemStack blueprint = this.inventory.getStackInSlot(0);
        this.blockSkipped = false;
        if (blueprint.isEmpty() && !this.statusMsg.equals("idle") && this.inventory.getStackInSlot(1).isEmpty()) {
            this.state = SchematicannonBlockEntity.State.STOPPED;
            this.statusMsg = "idle";
            this.sendUpdate = true;
        } else if (this.state == SchematicannonBlockEntity.State.STOPPED) {
            if (this.printer.isLoaded()) {
                this.resetPrinter();
            }
        } else if (this.state != SchematicannonBlockEntity.State.PAUSED || this.positionNotLoaded || this.missingItem != null || !((double) this.fuelLevel > this.getFuelUsageRate())) {
            if (!this.printer.isLoaded()) {
                this.initializePrinter(blueprint);
            } else if (this.printerCooldown > 0) {
                this.printerCooldown--;
            } else if (this.fuelLevel <= 0.0F && !this.hasCreativeCrate) {
                this.fuelLevel = 0.0F;
                this.state = SchematicannonBlockEntity.State.PAUSED;
                this.statusMsg = "noGunpowder";
                this.sendUpdate = true;
            } else {
                if (this.hasCreativeCrate && this.missingItem != null) {
                    this.missingItem = null;
                    this.state = SchematicannonBlockEntity.State.RUNNING;
                }
                if (this.missingItem == null && !this.positionNotLoaded) {
                    if (!this.printer.advanceCurrentPos()) {
                        this.finishedPrinting();
                        return;
                    }
                    this.sendUpdate = true;
                }
                if (!this.m_58904_().isLoaded(this.printer.getCurrentTarget())) {
                    this.positionNotLoaded = true;
                    this.statusMsg = "targetNotLoaded";
                    this.state = SchematicannonBlockEntity.State.PAUSED;
                } else {
                    if (this.positionNotLoaded) {
                        this.positionNotLoaded = false;
                        this.state = SchematicannonBlockEntity.State.RUNNING;
                    }
                    ItemRequirement requirement = this.printer.getCurrentRequirement();
                    if (!requirement.isInvalid() && this.printer.shouldPlaceCurrent(this.f_58857_, this::shouldPlace)) {
                        List<ItemRequirement.StackRequirement> requiredItems = requirement.getRequiredItems();
                        if (!requirement.isEmpty()) {
                            for (ItemRequirement.StackRequirement required : requiredItems) {
                                if (!this.grabItemsFromAttachedInventories(required, true)) {
                                    if (this.skipMissing) {
                                        this.statusMsg = "skipping";
                                        this.blockSkipped = true;
                                        if (this.missingItem != null) {
                                            this.missingItem = null;
                                            this.state = SchematicannonBlockEntity.State.RUNNING;
                                        }
                                        return;
                                    }
                                    this.missingItem = required.stack;
                                    this.state = SchematicannonBlockEntity.State.PAUSED;
                                    this.statusMsg = "missingBlock";
                                    return;
                                }
                            }
                            for (ItemRequirement.StackRequirement requiredx : requiredItems) {
                                this.grabItemsFromAttachedInventories(requiredx, false);
                            }
                        }
                        this.state = SchematicannonBlockEntity.State.RUNNING;
                        ItemStack icon = !requirement.isEmpty() && !requiredItems.isEmpty() ? ((ItemRequirement.StackRequirement) requiredItems.get(0)).stack : ItemStack.EMPTY;
                        this.printer.handleCurrentTarget((target, blockState, blockEntity) -> {
                            this.statusMsg = blockState.m_60734_() != Blocks.AIR ? "placing" : "clearing";
                            this.launchBlockOrBelt(target, icon, blockState, blockEntity);
                        }, (target, entity) -> {
                            this.statusMsg = "placing";
                            this.launchEntity(target, icon, entity);
                        });
                        this.printerCooldown = this.config().schematicannonDelay.get();
                        this.fuelLevel = (float) ((double) this.fuelLevel - this.getFuelUsageRate());
                        this.sendUpdate = true;
                        this.missingItem = null;
                    } else {
                        this.sendUpdate = !this.statusMsg.equals("searching");
                        this.statusMsg = "searching";
                        this.blockSkipped = true;
                    }
                }
            }
        }
    }

    public double getFuelUsageRate() {
        return this.hasCreativeCrate ? 0.0 : this.config().schematicannonFuelUsage.get() / 100.0;
    }

    protected void initializePrinter(ItemStack blueprint) {
        if (!blueprint.hasTag()) {
            this.state = SchematicannonBlockEntity.State.STOPPED;
            this.statusMsg = "schematicInvalid";
            this.sendUpdate = true;
        } else if (!blueprint.getTag().getBoolean("Deployed")) {
            this.state = SchematicannonBlockEntity.State.STOPPED;
            this.statusMsg = "schematicNotPlaced";
            this.sendUpdate = true;
        } else {
            this.printer.loadSchematic(blueprint, this.f_58857_, true);
            if (this.printer.isErrored()) {
                this.state = SchematicannonBlockEntity.State.STOPPED;
                this.statusMsg = "schematicErrored";
                this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                this.inventory.setStackInSlot(1, new ItemStack((ItemLike) AllItems.EMPTY_SCHEMATIC.get()));
                this.printer.resetSchematic();
                this.sendUpdate = true;
            } else if (this.printer.isWorldEmpty()) {
                this.state = SchematicannonBlockEntity.State.STOPPED;
                this.statusMsg = "schematicExpired";
                this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                this.inventory.setStackInSlot(1, new ItemStack((ItemLike) AllItems.EMPTY_SCHEMATIC.get()));
                this.printer.resetSchematic();
                this.sendUpdate = true;
            } else if (!this.printer.getAnchor().m_123314_(this.m_58899_(), 256.0)) {
                this.state = SchematicannonBlockEntity.State.STOPPED;
                this.statusMsg = "targetOutsideRange";
                this.printer.resetSchematic();
                this.sendUpdate = true;
            } else {
                this.state = SchematicannonBlockEntity.State.PAUSED;
                this.statusMsg = "ready";
                this.updateChecklist();
                this.sendUpdate = true;
                this.blocksToPlace = this.blocksToPlace + this.blocksPlaced;
            }
        }
    }

    protected ItemStack getItemForBlock(BlockState blockState) {
        Item item = (Item) BlockItem.f_41373_.getOrDefault(blockState.m_60734_(), Items.AIR);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }

    protected boolean grabItemsFromAttachedInventories(ItemRequirement.StackRequirement required, boolean simulate) {
        if (this.hasCreativeCrate) {
            return true;
        } else {
            this.attachedInventories.removeIf(capx -> !capx.isPresent());
            ItemRequirement.ItemUseType usage = required.usage;
            if (usage == ItemRequirement.ItemUseType.DAMAGE) {
                for (LazyOptional<IItemHandler> cap : this.attachedInventories) {
                    IItemHandler itemHandler = cap.orElse(EmptyHandler.INSTANCE);
                    for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
                        ItemStack extractItem = itemHandler.extractItem(slot, 1, true);
                        if (required.matches(extractItem) && extractItem.isDamageableItem()) {
                            if (!simulate) {
                                ItemStack stack = itemHandler.extractItem(slot, 1, false);
                                stack.setDamageValue(stack.getDamageValue() + 1);
                                if (stack.getDamageValue() <= stack.getMaxDamage()) {
                                    if (itemHandler.getStackInSlot(slot).isEmpty()) {
                                        itemHandler.insertItem(slot, stack, false);
                                    } else {
                                        ItemHandlerHelper.insertItem(itemHandler, stack, false);
                                    }
                                }
                            }
                            return true;
                        }
                    }
                }
                return false;
            } else {
                boolean success = false;
                int amountFound = 0;
                for (LazyOptional<IItemHandler> cap : this.attachedInventories) {
                    IItemHandler itemHandler = cap.orElse(EmptyHandler.INSTANCE);
                    amountFound += ItemHelper.extract(itemHandler, required::matches, ItemHelper.ExtractionCountMode.UPTO, required.stack.getCount(), true).getCount();
                    if (amountFound >= required.stack.getCount()) {
                        success = true;
                        break;
                    }
                }
                if (!simulate && success) {
                    amountFound = 0;
                    for (LazyOptional<IItemHandler> capx : this.attachedInventories) {
                        IItemHandler itemHandler = capx.orElse(EmptyHandler.INSTANCE);
                        amountFound += ItemHelper.extract(itemHandler, required::matches, ItemHelper.ExtractionCountMode.UPTO, required.stack.getCount(), false).getCount();
                        if (amountFound >= required.stack.getCount()) {
                            break;
                        }
                    }
                }
                return success;
            }
        }
    }

    public void finishedPrinting() {
        this.inventory.setStackInSlot(0, ItemStack.EMPTY);
        this.inventory.setStackInSlot(1, new ItemStack((ItemLike) AllItems.EMPTY_SCHEMATIC.get(), this.inventory.getStackInSlot(1).getCount() + 1));
        this.state = SchematicannonBlockEntity.State.STOPPED;
        this.statusMsg = "finished";
        this.resetPrinter();
        AllSoundEvents.SCHEMATICANNON_FINISH.playOnServer(this.f_58857_, this.f_58858_);
        this.sendUpdate = true;
    }

    protected void resetPrinter() {
        this.printer.resetSchematic();
        this.missingItem = null;
        this.sendUpdate = true;
        this.schematicProgress = 0.0F;
        this.blocksPlaced = 0;
        this.blocksToPlace = 0;
    }

    protected boolean shouldPlace(BlockPos pos, BlockState state, BlockEntity be, BlockState toReplace, BlockState toReplaceOther, boolean isNormalCube) {
        if (pos.m_123314_(this.m_58899_(), 2.0)) {
            return false;
        } else if (this.replaceBlockEntities || !toReplace.m_155947_() && (toReplaceOther == null || !toReplaceOther.m_155947_())) {
            if (this.shouldIgnoreBlockState(state, be)) {
                return false;
            } else {
                boolean placingAir = state.m_60795_();
                if (this.replaceMode == 3) {
                    return true;
                } else if (this.replaceMode == 2 && !placingAir) {
                    return true;
                } else {
                    return this.replaceMode == 1 && (isNormalCube || !toReplace.m_60796_(this.f_58857_, pos) && (toReplaceOther == null || !toReplaceOther.m_60796_(this.f_58857_, pos))) && !placingAir ? true : this.replaceMode == 0 && !toReplace.m_60796_(this.f_58857_, pos) && (toReplaceOther == null || !toReplaceOther.m_60796_(this.f_58857_, pos)) && !placingAir;
                }
            }
        } else {
            return false;
        }
    }

    protected boolean shouldIgnoreBlockState(BlockState state, BlockEntity be) {
        if (state.m_60734_() == Blocks.STRUCTURE_VOID) {
            return true;
        } else {
            ItemRequirement requirement = ItemRequirement.of(state, be);
            if (requirement.isEmpty()) {
                return false;
            } else if (requirement.isInvalid()) {
                return false;
            } else if (state.m_61138_(BlockStateProperties.DOUBLE_BLOCK_HALF) && state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                return true;
            } else if (state.m_61138_(BlockStateProperties.BED_PART) && state.m_61143_(BlockStateProperties.BED_PART) == BedPart.HEAD) {
                return true;
            } else if (state.m_60734_() instanceof PistonHeadBlock) {
                return true;
            } else {
                return AllBlocks.BELT.has(state) ? state.m_61143_(BeltBlock.PART) == BeltPart.MIDDLE : false;
            }
        }
    }

    protected void tickFlyingBlocks() {
        List<LaunchedItem> toRemove = new LinkedList();
        for (LaunchedItem b : this.flyingBlocks) {
            if (b.update(this.f_58857_)) {
                toRemove.add(b);
            }
        }
        this.flyingBlocks.removeAll(toRemove);
    }

    protected void refillFuelIfPossible() {
        if (!this.hasCreativeCrate) {
            if (!((double) (1.0F - this.fuelLevel + 0.0078125F) < this.getFuelAddedByGunPowder())) {
                if (!this.inventory.getStackInSlot(4).isEmpty()) {
                    this.inventory.getStackInSlot(4).shrink(1);
                    this.fuelLevel = (float) ((double) this.fuelLevel + this.getFuelAddedByGunPowder());
                    if (this.statusMsg.equals("noGunpowder")) {
                        if (this.blocksPlaced > 0) {
                            this.state = SchematicannonBlockEntity.State.RUNNING;
                        }
                        this.statusMsg = "ready";
                    }
                    this.sendUpdate = true;
                }
            }
        }
    }

    public double getFuelAddedByGunPowder() {
        return this.config().schematicannonGunpowderWorth.get() / 100.0;
    }

    protected void tickPaperPrinter() {
        int BookInput = 2;
        int BookOutput = 3;
        ItemStack blueprint = this.inventory.getStackInSlot(0);
        ItemStack paper = this.inventory.extractItem(BookInput, 1, true);
        boolean outputFull = this.inventory.getStackInSlot(BookOutput).getCount() == this.inventory.getSlotLimit(BookOutput);
        if (!this.printer.isErrored()) {
            if (!this.printer.isLoaded()) {
                if (!blueprint.isEmpty()) {
                    this.initializePrinter(blueprint);
                }
            } else if (!paper.isEmpty() && !outputFull) {
                if (this.bookPrintingProgress >= 1.0F) {
                    this.bookPrintingProgress = 0.0F;
                    if (!this.dontUpdateChecklist) {
                        this.updateChecklist();
                    }
                    this.dontUpdateChecklist = true;
                    ItemStack extractItem = this.inventory.extractItem(BookInput, 1, false);
                    ItemStack stack = AllBlocks.CLIPBOARD.isIn(extractItem) ? this.checklist.createWrittenClipboard() : this.checklist.createWrittenBook();
                    stack.setCount(this.inventory.getStackInSlot(BookOutput).getCount() + 1);
                    this.inventory.setStackInSlot(BookOutput, stack);
                    this.sendUpdate = true;
                } else {
                    this.bookPrintingProgress += 0.05F;
                    this.sendUpdate = true;
                }
            } else {
                if (this.bookPrintingProgress != 0.0F) {
                    this.sendUpdate = true;
                }
                this.bookPrintingProgress = 0.0F;
                this.dontUpdateChecklist = false;
            }
        }
    }

    public static BlockState stripBeltIfNotLast(BlockState blockState) {
        BeltPart part = (BeltPart) blockState.m_61143_(BeltBlock.PART);
        if (part == BeltPart.MIDDLE) {
            return Blocks.AIR.defaultBlockState();
        } else {
            boolean isLastSegment = false;
            Direction facing = (Direction) blockState.m_61143_(BeltBlock.HORIZONTAL_FACING);
            BeltSlope slope = (BeltSlope) blockState.m_61143_(BeltBlock.SLOPE);
            boolean positive = facing.getAxisDirection() == Direction.AxisDirection.POSITIVE;
            boolean start = part == BeltPart.START;
            boolean end = part == BeltPart.END;
            return switch(slope) {
                case DOWNWARD ->
                    start;
                case UPWARD ->
                    end;
                default ->
                    positive && end || !positive && start;
            } ? blockState : (BlockState) AllBlocks.SHAFT.getDefaultState().m_61124_(AbstractSimpleShaftBlock.AXIS, slope == BeltSlope.SIDEWAYS ? Direction.Axis.Y : facing.getClockWise().getAxis());
        }
    }

    protected void launchBlockOrBelt(BlockPos target, ItemStack icon, BlockState blockState, BlockEntity blockEntity) {
        if (!AllBlocks.BELT.has(blockState)) {
            CompoundTag data = BlockHelper.prepareBlockEntityData(blockState, blockEntity);
            this.launchBlock(target, icon, blockState, data);
        } else {
            blockState = stripBeltIfNotLast(blockState);
            if (blockEntity instanceof BeltBlockEntity bbe && AllBlocks.BELT.has(blockState)) {
                BeltBlockEntity.CasingType[] casings = new BeltBlockEntity.CasingType[bbe.beltLength];
                Arrays.fill(casings, BeltBlockEntity.CasingType.NONE);
                BlockPos currentPos = target;
                for (int i = 0; i < bbe.beltLength; i++) {
                    BlockState currentState = bbe.m_58904_().getBlockState(currentPos);
                    if (!(currentState.m_60734_() instanceof BeltBlock) || !(bbe.m_58904_().getBlockEntity(currentPos) instanceof BeltBlockEntity beltAtSegment)) {
                        break;
                    }
                    casings[i] = beltAtSegment.casing;
                    currentPos = BeltBlock.nextSegmentPosition(currentState, currentPos, blockState.m_61143_(BeltBlock.PART) != BeltPart.END);
                }
                this.launchBelt(target, blockState, bbe.beltLength, casings);
                return;
            }
            if (blockState != Blocks.AIR.defaultBlockState()) {
                this.launchBlock(target, icon, blockState, null);
            }
        }
    }

    protected void launchBelt(BlockPos target, BlockState state, int length, BeltBlockEntity.CasingType[] casings) {
        this.blocksPlaced++;
        ItemStack connector = AllItems.BELT_CONNECTOR.asStack();
        this.flyingBlocks.add(new LaunchedItem.ForBelt(this.m_58899_(), target, connector, state, casings));
        this.playFiringSound();
    }

    protected void launchBlock(BlockPos target, ItemStack stack, BlockState state, @Nullable CompoundTag data) {
        if (!state.m_60795_()) {
            this.blocksPlaced++;
        }
        this.flyingBlocks.add(new LaunchedItem.ForBlockState(this.m_58899_(), target, stack, state, data));
        this.playFiringSound();
    }

    protected void launchEntity(BlockPos target, ItemStack stack, Entity entity) {
        this.blocksPlaced++;
        this.flyingBlocks.add(new LaunchedItem.ForEntity(this.m_58899_(), target, stack, entity));
        this.playFiringSound();
    }

    public void playFiringSound() {
        AllSoundEvents.SCHEMATICANNON_LAUNCH_BLOCK.playOnServer(this.f_58857_, this.f_58858_);
    }

    @Override
    public void sendToMenu(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.m_58899_());
        buffer.writeNbt(this.m_5995_());
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return SchematicannonMenu.create(id, inv, this);
    }

    @Override
    public Component getDisplayName() {
        return Lang.translateDirect("gui.schematicannon.title");
    }

    public void updateChecklist() {
        this.checklist.required.clear();
        this.checklist.damageRequired.clear();
        this.checklist.blocksNotLoaded = false;
        if (this.printer.isLoaded() && !this.printer.isErrored()) {
            this.blocksToPlace = this.blocksPlaced;
            this.blocksToPlace = this.blocksToPlace + this.printer.markAllBlockRequirements(this.checklist, this.f_58857_, this::shouldPlace);
            this.printer.markAllEntityRequirements(this.checklist);
        }
        this.checklist.gathered.clear();
        this.findInventories();
        for (LazyOptional<IItemHandler> cap : this.attachedInventories) {
            if (cap.isPresent()) {
                IItemHandler inventory = cap.orElse(EmptyHandler.INSTANCE);
                for (int slot = 0; slot < inventory.getSlots(); slot++) {
                    ItemStack stackInSlot = inventory.getStackInSlot(slot);
                    if (!inventory.extractItem(slot, 1, true).isEmpty()) {
                        this.checklist.collect(stackInSlot);
                    }
                }
            }
        }
        this.sendUpdate = true;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
        this.findInventories();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public AABB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    public static enum State {

        STOPPED, PAUSED, RUNNING
    }
}