package com.sihenzhang.crockpot.block.entity;

import com.google.common.base.Preconditions;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.base.CrockPotSoundEvents;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.util.I18nUtils;
import java.util.ArrayList;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class CrockPotBlockEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (slot < 4) {
                return CrockPotBlockEntity.this.isValidIngredient(stack);
            } else {
                return slot == 4 ? CrockPotBlockEntity.isFuel(stack) : super.isItemValid(slot, stack);
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            CrockPotBlockEntity.this.markUpdated();
        }
    };

    private final RangedWrapper itemHandlerInput = new RangedWrapper(this.itemHandler, 0, 4);

    private final RangedWrapper itemHandlerFuel = new RangedWrapper(this.itemHandler, 4, 5);

    private final RangedWrapper itemHandlerOutput = new RangedWrapper(this.itemHandler, 5, 6);

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            CrockPotBlockEntity.this.playSound(pState, CrockPotSoundEvents.CROCK_POT_OPEN.get());
            CrockPotBlockEntity.this.updateBlockState(pState, true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            CrockPotBlockEntity.this.playSound(pState, CrockPotSoundEvents.CROCK_POT_CLOSE.get());
            CrockPotBlockEntity.this.updateBlockState(pState, false);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
        }

        @Override
        protected boolean isOwnContainer(Player pPlayer) {
            return pPlayer.containerMenu instanceof CrockPotMenu crockPotMenu ? crockPotMenu.getBlockEntity() == CrockPotBlockEntity.this : false;
        }
    };

    private final int potLevel;

    private int burningTime;

    private int burningTotalTime;

    private int cookingTime;

    private int cookingTotalTime;

    private ItemStack result = ItemStack.EMPTY;

    private int cookingSoundPlayingTime;

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> this.itemHandler);

    private final LazyOptional<IItemHandler> itemHandlerInputCap = LazyOptional.of(() -> this.itemHandlerInput);

    private final LazyOptional<IItemHandler> itemHandlerFuelCap = LazyOptional.of(() -> this.itemHandlerFuel);

    private final LazyOptional<IItemHandler> itemHandlerOutputCap = LazyOptional.of(() -> this.itemHandlerOutput);

    public CrockPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrockPotBlockEntities.CROCK_POT_BLOCK_ENTITY.get(), pPos, pBlockState);
        Preconditions.checkArgument(pBlockState.m_60734_() instanceof CrockPotBlock, "Block of the `CrockPotEntity` must be an instance of `CrockPotBlock`.");
        this.potLevel = ((CrockPotBlock) pBlockState.m_60734_()).getPotLevel();
    }

    @Override
    public Component getDisplayName() {
        return I18nUtils.createComponent("container", "crock_pot");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CrockPotMenu(pContainerId, pPlayerInventory, this);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, CrockPotBlockEntity pBlockEntity) {
        boolean hasChanged = false;
        boolean isBurning = pBlockEntity.isBurning();
        if (pBlockEntity.isBurning()) {
            pBlockEntity.burningTime--;
            hasChanged = true;
        }
        ItemStack fuelStack = pBlockEntity.itemHandlerFuel.getStackInSlot(0);
        if (pBlockEntity.isBurning() || isFuel(fuelStack)) {
            if (!pBlockEntity.isCooking() && pBlockEntity.itemHandlerOutput.getStackInSlot(0).isEmpty()) {
                CrockPotCookingRecipe.Wrapper recipeWrapper = pBlockEntity.getRecipeWrapper();
                if (recipeWrapper != null) {
                    Optional<CrockPotCookingRecipe> optionalRecipe = CrockPotCookingRecipe.getRecipeFor(recipeWrapper, pLevel);
                    if (optionalRecipe.isPresent()) {
                        CrockPotCookingRecipe recipe = (CrockPotCookingRecipe) optionalRecipe.get();
                        pBlockEntity.cookingTotalTime = pBlockEntity.getActualCookingTotalTime(recipe);
                        pBlockEntity.result = recipe.assemble(recipeWrapper, pLevel.registryAccess());
                        pBlockEntity.shrinkInputs();
                        Containers.dropContents(pLevel, pPos, recipe.m_7457_(recipeWrapper));
                        hasChanged = true;
                    }
                }
            }
            if (pBlockEntity.isCooking()) {
                if (!pBlockEntity.isBurning() && isFuel(fuelStack)) {
                    pBlockEntity.burningTime = pBlockEntity.burningTotalTime = ForgeHooks.getBurnTime(fuelStack, null);
                    ItemStack remainingItem = fuelStack.getCraftingRemainingItem();
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        pBlockEntity.itemHandlerFuel.setStackInSlot(0, remainingItem);
                    }
                    hasChanged = true;
                }
                if (pBlockEntity.isBurning() && pBlockEntity.itemHandlerOutput.getStackInSlot(0).isEmpty()) {
                    pBlockEntity.cookingTime++;
                    if (pBlockEntity.cookingSoundPlayingTime % 5 == 0) {
                        pBlockEntity.playSound(pState, CrockPotSoundEvents.CROCK_POT_RATTLE.get());
                        pBlockEntity.cookingSoundPlayingTime = 0;
                    }
                    pBlockEntity.cookingSoundPlayingTime++;
                    if (pBlockEntity.cookingTime >= pBlockEntity.cookingTotalTime) {
                        pBlockEntity.cookingTime = 0;
                        pBlockEntity.itemHandlerOutput.setStackInSlot(0, pBlockEntity.result);
                        pBlockEntity.result = ItemStack.EMPTY;
                    }
                    hasChanged = true;
                }
            }
        }
        if (!pBlockEntity.isBurning()) {
            pBlockEntity.cookingSoundPlayingTime = 0;
        }
        if (isBurning != pBlockEntity.isBurning()) {
            pState = (BlockState) pState.m_61124_(CrockPotBlock.LIT, pBlockEntity.isBurning());
            pLevel.setBlock(pPos, pState, 3);
            hasChanged = true;
        }
        if (hasChanged) {
            pBlockEntity.markUpdated();
        }
    }

    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    public int getPotLevel() {
        return this.potLevel;
    }

    @Nullable
    public CrockPotCookingRecipe.Wrapper getRecipeWrapper() {
        int size = this.itemHandlerInput.getSlots();
        ArrayList<ItemStack> stacks = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            ItemStack stackInSlot = this.itemHandlerInput.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                return null;
            }
            stacks.add(stackInSlot.copyWithCount(1));
        }
        FoodValues mergedFoodValues = FoodValues.merge(stacks.stream().map(stack -> FoodValuesDefinition.getFoodValues(stack, this.f_58857_)).toList());
        return new CrockPotCookingRecipe.Wrapper(stacks, mergedFoodValues, this.getPotLevel());
    }

    public boolean isValidIngredient(ItemStack stack) {
        return !FoodValuesDefinition.getFoodValues(stack, this.f_58857_).isEmpty();
    }

    public static boolean isFuel(ItemStack pStack) {
        return ForgeHooks.getBurnTime(pStack, null) > 0;
    }

    public boolean isBurning() {
        return this.burningTime > 0;
    }

    public float getBurningProgress() {
        return this.burningTotalTime != 0 ? (float) this.burningTime / (float) this.burningTotalTime : 0.0F;
    }

    public boolean isCooking() {
        return this.result != null && !this.result.isEmpty();
    }

    public float getCookingProgress() {
        return this.cookingTotalTime != 0 ? (float) this.cookingTime / (float) this.cookingTotalTime : 0.0F;
    }

    public ItemStack getResult() {
        return this.result;
    }

    private void shrinkInputs() {
        for (int i = 0; i < this.itemHandlerInput.getSlots(); i++) {
            this.itemHandlerInput.getStackInSlot(i).shrink(1);
        }
    }

    private int getActualCookingTotalTime(CrockPotCookingRecipe recipe) {
        return Math.max((int) ((double) recipe.getCookingTime() * (1.0 - CrockPotConfigs.CROCK_POT_SPEED_MODIFIER.get() * (double) this.getPotLevel())), 1);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.itemHandler.deserializeNBT(pTag.getCompound("ItemHandler"));
        this.burningTime = pTag.getInt("BurningTime");
        this.burningTotalTime = pTag.getInt("BurningTotalTime");
        this.cookingTime = pTag.getInt("CookingTime");
        this.cookingTotalTime = pTag.getInt("CookingTotalTime");
        if (pTag.contains("Result", 10)) {
            this.result.deserializeNBT(pTag.getCompound("Result"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("ItemHandler", this.itemHandler.serializeNBT());
        pTag.putInt("BurningTime", this.burningTime);
        pTag.putInt("BurningTotalTime", this.burningTotalTime);
        pTag.putInt("CookingTime", this.cookingTime);
        pTag.putInt("CookingTotalTime", this.cookingTotalTime);
        pTag.put("Result", this.result.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("ItemHandler", this.itemHandler.serializeNBT());
        tag.putInt("BurningTime", this.burningTime);
        tag.putInt("BurningTotalTime", this.burningTotalTime);
        tag.putInt("CookingTime", this.cookingTime);
        tag.putInt("CookingTotalTime", this.cookingTotalTime);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        this.m_6596_();
        this.f_58857_.sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
    }

    public void startOpen(Player pPlayer) {
        if (!this.f_58859_ && !pPlayer.isSpectator()) {
            this.openersCounter.incrementOpeners(pPlayer, this.f_58857_, this.m_58899_(), this.m_58900_());
        }
    }

    public void stopOpen(Player pPlayer) {
        if (!this.f_58859_ && !pPlayer.isSpectator()) {
            this.openersCounter.decrementOpeners(pPlayer, this.f_58857_, this.m_58899_(), this.m_58900_());
        }
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.f_58857_, this.m_58899_(), this.m_58900_());
        }
    }

    void updateBlockState(BlockState pState, boolean pOpen) {
        this.f_58857_.setBlock(this.m_58899_(), (BlockState) pState.m_61124_(CrockPotBlock.OPEN, pOpen), 3);
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        Vec3i vec3i = ((Direction) pState.m_61143_(CrockPotBlock.FACING)).getNormal();
        double d0 = (double) this.f_58858_.m_123341_() + 0.5 + (double) vec3i.getX() / 2.0;
        double d1 = (double) this.f_58858_.m_123342_() + 0.5 + (double) vec3i.getY() / 2.0;
        double d2 = (double) this.f_58858_.m_123343_() + 0.5 + (double) vec3i.getZ() / 2.0;
        this.f_58857_.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, this.f_58857_.random.nextFloat() * 0.1F + 0.9F);
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == null) {
                return this.itemHandlerCap.cast();
            } else {
                return switch(side) {
                    case UP ->
                        this.itemHandlerInputCap.cast();
                    case DOWN ->
                        this.itemHandlerOutputCap.cast();
                    default ->
                        this.itemHandlerFuelCap.cast();
                };
            }
        } else {
            return super.getCapability(cap, side);
        }
    }
}