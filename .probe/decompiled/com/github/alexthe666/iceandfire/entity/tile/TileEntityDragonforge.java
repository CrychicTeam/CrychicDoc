package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeBricks;
import com.github.alexthe666.iceandfire.block.BlockDragonforgeCore;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.DragonType;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.message.MessageUpdateDragonforge;
import com.github.alexthe666.iceandfire.recipe.DragonForgeRecipe;
import com.github.alexthe666.iceandfire.recipe.IafRecipeRegistry;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.NotNull;

public class TileEntityDragonforge extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int[] SLOTS_TOP = new int[] { 0, 1 };

    private static final int[] SLOTS_BOTTOM = new int[] { 2 };

    private static final int[] SLOTS_SIDES = new int[] { 0, 1 };

    private static final Direction[] HORIZONTALS = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public int fireType;

    public int cookTime;

    public int lastDragonFlameTimer = 0;

    LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    private NonNullList<ItemStack> forgeItemStacks = NonNullList.withSize(3, ItemStack.EMPTY);

    private boolean prevAssembled;

    private boolean canAddFlameAgain = true;

    public TileEntityDragonforge(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get(), pos, state);
    }

    public TileEntityDragonforge(BlockPos pos, BlockState state, int fireType) {
        super(IafTileEntityRegistry.DRAGONFORGE_CORE.get(), pos, state);
        this.fireType = fireType;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityDragonforge entityDragonforge) {
        boolean flag = entityDragonforge.isBurning();
        boolean flag1 = false;
        entityDragonforge.fireType = entityDragonforge.getFireType(entityDragonforge.m_58900_().m_60734_());
        if (entityDragonforge.lastDragonFlameTimer > 0) {
            entityDragonforge.lastDragonFlameTimer--;
        }
        entityDragonforge.updateGrills(entityDragonforge.assembled());
        if (!level.isClientSide) {
            if (entityDragonforge.prevAssembled != entityDragonforge.assembled()) {
                BlockDragonforgeCore.setState(entityDragonforge.fireType, entityDragonforge.prevAssembled, level, pos);
            }
            entityDragonforge.prevAssembled = entityDragonforge.assembled();
            if (!entityDragonforge.assembled()) {
                return;
            }
        }
        if (entityDragonforge.cookTime > 0 && entityDragonforge.canSmelt() && entityDragonforge.lastDragonFlameTimer == 0) {
            entityDragonforge.cookTime--;
        }
        if (entityDragonforge.getItem(0).isEmpty() && !level.isClientSide) {
            entityDragonforge.cookTime = 0;
        }
        if (!entityDragonforge.f_58857_.isClientSide) {
            if (entityDragonforge.isBurning()) {
                if (entityDragonforge.canSmelt()) {
                    entityDragonforge.cookTime++;
                    if (entityDragonforge.cookTime >= entityDragonforge.getMaxCookTime()) {
                        entityDragonforge.cookTime = 0;
                        entityDragonforge.smeltItem();
                        flag1 = true;
                    }
                } else if (entityDragonforge.cookTime > 0) {
                    IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(pos.asLong(), entityDragonforge.cookTime));
                    entityDragonforge.cookTime = 0;
                }
            } else if (!entityDragonforge.isBurning() && entityDragonforge.cookTime > 0) {
                entityDragonforge.cookTime = Mth.clamp(entityDragonforge.cookTime - 2, 0, entityDragonforge.getMaxCookTime());
            }
            if (flag != entityDragonforge.isBurning()) {
                flag1 = true;
            }
        }
        if (flag1) {
            entityDragonforge.m_6596_();
        }
        if (!entityDragonforge.canAddFlameAgain) {
            entityDragonforge.canAddFlameAgain = true;
        }
    }

    @Override
    public int getContainerSize() {
        return this.forgeItemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.forgeItemStacks) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void updateGrills(boolean grill) {
        for (Direction facing : HORIZONTALS) {
            BlockPos grillPos = this.m_58899_().relative(facing);
            if (this.grillMatches(this.f_58857_.getBlockState(grillPos).m_60734_())) {
                BlockState grillState = (BlockState) this.getGrillBlock().defaultBlockState().m_61124_(BlockDragonforgeBricks.GRILL, grill);
                if (this.f_58857_.getBlockState(grillPos) != grillState) {
                    this.f_58857_.setBlockAndUpdate(grillPos, grillState);
                }
            }
        }
    }

    public Block getGrillBlock() {
        return switch(this.fireType) {
            case 1 ->
                (Block) IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            case 2 ->
                (Block) IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
            default ->
                (Block) IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
        };
    }

    public boolean grillMatches(Block block) {
        return switch(this.fireType) {
            case 0 ->
                block == IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
            case 1 ->
                block == IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            case 2 ->
                block == IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
            default ->
                false;
        };
    }

    @NotNull
    @Override
    public ItemStack getItem(int index) {
        return this.forgeItemStacks.get(index);
    }

    @NotNull
    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.forgeItemStacks, index, count);
    }

    @NotNull
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.forgeItemStacks, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.forgeItemStacks.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItem(stack, itemstack) && ItemStack.matches(stack, itemstack);
        this.forgeItemStacks.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (index == 0 && !flag || this.cookTime > this.getMaxCookTime()) {
            this.cookTime = 0;
            this.m_6596_();
        }
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.load(compound);
        this.forgeItemStacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.forgeItemStacks);
        this.cookTime = compound.getInt("CookTime");
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        compound.putInt("CookTime", (short) this.cookTime);
        ContainerHelper.saveAllItems(compound, this.forgeItemStacks);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    public boolean isBurning() {
        return this.cookTime > 0;
    }

    public int getFireType(Block block) {
        if (block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE.get() || block == IafBlockRegistry.DRAGONFORGE_FIRE_CORE_DISABLED.get()) {
            return 0;
        } else if (block == IafBlockRegistry.DRAGONFORGE_ICE_CORE.get() || block == IafBlockRegistry.DRAGONFORGE_ICE_CORE_DISABLED.get()) {
            return 1;
        } else {
            return block != IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE.get() && block != IafBlockRegistry.DRAGONFORGE_LIGHTNING_CORE_DISABLED.get() ? 0 : 2;
        }
    }

    public String getTypeID() {
        return switch(this.getFireType(this.m_58900_().m_60734_())) {
            case 0 ->
                "fire";
            case 1 ->
                "ice";
            case 2 ->
                "lightning";
            default ->
                "";
        };
    }

    public int getMaxCookTime() {
        return (Integer) this.getCurrentRecipe().map(DragonForgeRecipe::getCookTime).orElse(100);
    }

    private Block getDefaultOutput() {
        return this.fireType == 1 ? IafBlockRegistry.DRAGON_ICE.get() : IafBlockRegistry.ASH.get();
    }

    private ItemStack getCurrentResult() {
        Optional<DragonForgeRecipe> recipe = this.getCurrentRecipe();
        return (ItemStack) recipe.map(DragonForgeRecipe::getResultItem).orElseGet(() -> new ItemStack(this.getDefaultOutput()));
    }

    public Optional<DragonForgeRecipe> getCurrentRecipe() {
        return this.f_58857_.getRecipeManager().getRecipeFor(IafRecipeRegistry.DRAGON_FORGE_TYPE.get(), this, this.f_58857_);
    }

    public List<DragonForgeRecipe> getRecipes() {
        return this.f_58857_.getRecipeManager().getAllRecipesFor(IafRecipeRegistry.DRAGON_FORGE_TYPE.get());
    }

    public boolean canSmelt() {
        ItemStack cookStack = this.forgeItemStacks.get(0);
        if (cookStack.isEmpty()) {
            return false;
        } else {
            ItemStack forgeRecipeOutput = this.getCurrentResult();
            if (forgeRecipeOutput.isEmpty()) {
                return false;
            } else {
                ItemStack outputStack = this.forgeItemStacks.get(2);
                if (!outputStack.isEmpty() && !ItemStack.isSameItem(outputStack, forgeRecipeOutput)) {
                    return false;
                } else {
                    int calculatedOutputCount = outputStack.getCount() + forgeRecipeOutput.getCount();
                    return calculatedOutputCount <= this.getMaxStackSize() && calculatedOutputCount <= outputStack.getMaxStackSize();
                }
            }
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return player.m_9236_().getBlockEntity(this.f_58858_) != this ? false : player.m_20275_((double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 0.5, (double) this.f_58858_.m_123343_() + 0.5) <= 64.0;
    }

    public void smeltItem() {
        if (this.canSmelt()) {
            ItemStack cookStack = this.forgeItemStacks.get(0);
            ItemStack bloodStack = this.forgeItemStacks.get(1);
            ItemStack outputStack = this.forgeItemStacks.get(2);
            ItemStack output = this.getCurrentResult();
            if (outputStack.isEmpty()) {
                this.forgeItemStacks.set(2, output.copy());
            } else {
                outputStack.grow(output.getCount());
            }
            cookStack.shrink(1);
            bloodStack.shrink(1);
        }
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return switch(index) {
            case 0 ->
                true;
            case 1 ->
                this.getRecipes().stream().anyMatch(item -> item.isValidBlood(stack));
            default ->
                false;
        };
    }

    @NotNull
    @Override
    public int[] getSlotsForFace(@NotNull Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_BOTTOM;
        } else {
            return side == Direction.UP ? SLOTS_TOP : SLOTS_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack itemStackIn, Direction direction) {
        return this.canPlaceItem(index, itemStackIn);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        if (direction == Direction.DOWN && index == 1) {
            Item item = stack.getItem();
            return item == Items.WATER_BUCKET || item == Items.BUCKET;
        } else {
            return true;
        }
    }

    @Override
    public void clearContent() {
        this.forgeItemStacks.clear();
    }

    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        if (this.f_58859_ || facing == null || capability != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(capability, facing);
        } else if (facing == Direction.UP) {
            return this.handlers[0].cast();
        } else {
            return facing == Direction.DOWN ? this.handlers[1].cast() : this.handlers[2].cast();
        }
    }

    @NotNull
    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.dragonforge_fire" + DragonType.getNameFromInt(this.fireType));
    }

    public void transferPower(int i) {
        if (!this.f_58857_.isClientSide) {
            if (this.canSmelt()) {
                if (this.canAddFlameAgain) {
                    this.cookTime = Math.min(this.getMaxCookTime() + 1, this.cookTime + i);
                    this.canAddFlameAgain = false;
                }
            } else {
                this.cookTime = 0;
            }
            IceAndFire.sendMSGToAll(new MessageUpdateDragonforge(this.f_58858_.asLong(), this.cookTime));
        }
        this.lastDragonFlameTimer = 40;
    }

    private boolean checkBoneCorners(BlockPos pos) {
        return this.doesBlockEqual(pos.north().east(), IafBlockRegistry.DRAGON_BONE_BLOCK.get()) && this.doesBlockEqual(pos.north().west(), IafBlockRegistry.DRAGON_BONE_BLOCK.get()) && this.doesBlockEqual(pos.south().east(), IafBlockRegistry.DRAGON_BONE_BLOCK.get()) && this.doesBlockEqual(pos.south().west(), IafBlockRegistry.DRAGON_BONE_BLOCK.get());
    }

    private boolean checkBrickCorners(BlockPos pos) {
        return this.doesBlockEqual(pos.north().east(), this.getBrick()) && this.doesBlockEqual(pos.north().west(), this.getBrick()) && this.doesBlockEqual(pos.south().east(), this.getBrick()) && this.doesBlockEqual(pos.south().west(), this.getBrick());
    }

    private boolean checkBrickSlots(BlockPos pos) {
        return this.doesBlockEqual(pos.north(), this.getBrick()) && this.doesBlockEqual(pos.east(), this.getBrick()) && this.doesBlockEqual(pos.west(), this.getBrick()) && this.doesBlockEqual(pos.south(), this.getBrick());
    }

    private boolean checkY(BlockPos pos) {
        return this.doesBlockEqual(pos.above(), this.getBrick()) && this.doesBlockEqual(pos.below(), this.getBrick());
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187480_();
    }

    public boolean assembled() {
        return this.checkBoneCorners(this.f_58858_.below()) && this.checkBrickSlots(this.f_58858_.below()) && this.checkBrickCorners(this.f_58858_) && this.atleastThreeAreBricks(this.f_58858_) && this.checkY(this.f_58858_) && this.checkBoneCorners(this.f_58858_.above()) && this.checkBrickSlots(this.f_58858_.above());
    }

    private Block getBrick() {
        return switch(this.fireType) {
            case 0 ->
                (Block) IafBlockRegistry.DRAGONFORGE_FIRE_BRICK.get();
            case 1 ->
                (Block) IafBlockRegistry.DRAGONFORGE_ICE_BRICK.get();
            default ->
                (Block) IafBlockRegistry.DRAGONFORGE_LIGHTNING_BRICK.get();
        };
    }

    private boolean doesBlockEqual(BlockPos pos, Block block) {
        return this.f_58857_.getBlockState(pos).m_60734_() == block;
    }

    private boolean atleastThreeAreBricks(BlockPos pos) {
        int count = 0;
        for (Direction facing : HORIZONTALS) {
            if (this.f_58857_.getBlockState(pos.relative(facing)).m_60734_() == this.getBrick()) {
                count++;
            }
        }
        return count > 2;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new ContainerDragonForge(id, this, playerInventory, new SimpleContainerData(0));
    }

    @NotNull
    @Override
    protected AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new ContainerDragonForge(id, this, player, new SimpleContainerData(0));
    }
}