package io.github.lightman314.lightmanscurrency.common.blockentity;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.misc.IServerTicker;
import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.common.core.ModBlockEntities;
import io.github.lightman314.lightmanscurrency.common.crafting.CoinMintRecipe;
import io.github.lightman314.lightmanscurrency.common.crafting.RecipeValidator;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class CoinMintBlockEntity extends EasyBlockEntity implements IServerTicker {

    SimpleContainer storage = new SimpleContainer(2);

    private CoinMintRecipe lastRelevantRecipe = null;

    private int mintTime = 0;

    private final CoinMintBlockEntity.MintItemCapability itemHandler = new CoinMintBlockEntity.MintItemCapability(this);

    private final LazyOptional<IItemHandler> inventoryHandlerLazyOptional = LazyOptional.of(() -> this.itemHandler);

    public SimpleContainer getStorage() {
        return this.storage;
    }

    public int getMintTime() {
        return this.mintTime;
    }

    public float getMintProgress() {
        return (float) this.mintTime / (float) this.getExpectedMintTime();
    }

    public int getExpectedMintTime() {
        return this.lastRelevantRecipe != null ? this.lastRelevantRecipe.getDuration() : -1;
    }

    private List<CoinMintRecipe> getCoinMintRecipes() {
        return (List<CoinMintRecipe>) (this.f_58857_ != null ? getCoinMintRecipes(this.f_58857_) : Lists.newArrayList());
    }

    public static List<CoinMintRecipe> getCoinMintRecipes(Level level) {
        return RecipeValidator.getValidMintRecipes(level);
    }

    public CoinMintBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.COIN_MINT.get(), pos, state);
    }

    protected CoinMintBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.storage.addListener(this::onInventoryChanged);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag compound) {
        InventoryUtil.saveAllItems("Storage", compound, this.storage);
        compound.putInt("MintTime", this.mintTime);
        super.m_183515_(compound);
    }

    @Override
    public void load(@NotNull CompoundTag compound) {
        super.m_142466_(compound);
        this.storage = InventoryUtil.loadAllItems("Storage", compound, 2);
        this.storage.addListener(this::onInventoryChanged);
        if (compound.contains("MintTime")) {
            this.mintTime = compound.getInt("MintTime");
        }
    }

    public void onLoad() {
        if (this.f_58857_.isClientSide) {
            BlockEntityUtil.requestUpdatePacket(this);
        }
        this.lastRelevantRecipe = this.getRelevantRecipe();
    }

    private void onInventoryChanged(Container inventory) {
        if (inventory == this.storage) {
            this.m_6596_();
            CoinMintRecipe newRecipe = this.getRelevantRecipe();
            if (this.lastRelevantRecipe != newRecipe) {
                this.lastRelevantRecipe = newRecipe;
                this.mintTime = 0;
                this.markMintTimeDirty();
            }
        }
    }

    @Override
    public void serverTick() {
        if (this.lastRelevantRecipe != null && this.storage.getItem(0).getCount() >= this.lastRelevantRecipe.ingredientCount && this.hasOutputSpace()) {
            this.mintTime++;
            if (this.mintTime >= this.lastRelevantRecipe.getDuration()) {
                this.mintTime = 0;
                this.mintCoin();
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.5F, 1.0F);
            }
            this.markMintTimeDirty();
        } else if (this.mintTime > 0) {
            this.mintTime = 0;
            this.markMintTimeDirty();
        }
    }

    private void markMintTimeDirty() {
        this.m_6596_();
        CompoundTag updateTag = new CompoundTag();
        updateTag.putInt("MintTime", this.mintTime);
        BlockEntityUtil.sendUpdatePacket(this, updateTag);
    }

    public void dumpContents(Level world, BlockPos pos) {
        InventoryUtil.dumpContents(world, pos, this.storage);
    }

    public boolean validMintInput(ItemStack item) {
        Container tempInv = new SimpleContainer(2);
        tempInv.setItem(0, item);
        for (CoinMintRecipe recipe : this.getCoinMintRecipes()) {
            if (recipe.matches(tempInv, this.f_58857_)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasOutputSpace() {
        if (this.lastRelevantRecipe == null) {
            return false;
        } else {
            ItemStack mintOutput = this.lastRelevantRecipe.getResultItem(this.f_58857_.registryAccess());
            ItemStack currentOutputSlot = this.getStorage().getItem(1);
            if (currentOutputSlot.isEmpty()) {
                return true;
            } else {
                return !InventoryUtil.ItemMatches(currentOutputSlot, mintOutput) ? false : currentOutputSlot.getMaxStackSize() - currentOutputSlot.getCount() >= this.lastRelevantRecipe.getOutputItem().getCount();
            }
        }
    }

    @Nullable
    public CoinMintRecipe getRelevantRecipe() {
        ItemStack mintInput = this.getStorage().getItem(0);
        if (mintInput.isEmpty()) {
            return null;
        } else {
            for (CoinMintRecipe recipe : this.getCoinMintRecipes()) {
                if (recipe.matches(this.storage, this.f_58857_)) {
                    return recipe;
                }
            }
            return null;
        }
    }

    public void mintCoin() {
        this.lastRelevantRecipe = this.getRelevantRecipe();
        if (this.lastRelevantRecipe != null) {
            ItemStack mintOutput = this.lastRelevantRecipe.getResultItem(this.f_58857_.registryAccess());
            if (!mintOutput.isEmpty()) {
                if (this.hasOutputSpace()) {
                    if (this.storage.getItem(0).getCount() >= this.lastRelevantRecipe.ingredientCount) {
                        if (this.getStorage().getItem(1).isEmpty()) {
                            this.getStorage().setItem(1, mintOutput);
                        } else {
                            this.getStorage().getItem(1).grow(mintOutput.getCount());
                        }
                        this.getStorage().removeItem(0, mintOutput.getCount());
                        this.m_6596_();
                    }
                }
            }
        }
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.inventoryHandlerLazyOptional);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.inventoryHandlerLazyOptional.invalidate();
    }

    public static class MintItemCapability implements IItemHandler {

        final CoinMintBlockEntity mint;

        public MintItemCapability(CoinMintBlockEntity tileEntity) {
            this.mint = tileEntity;
        }

        @Override
        public int getSlots() {
            return this.mint.getStorage().getContainerSize();
        }

        @Nonnull
        @Override
        public ItemStack getStackInSlot(int slot) {
            return this.mint.getStorage().getItem(slot);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot != 0) {
                return stack.copy();
            } else if (!this.mint.validMintInput(stack)) {
                return stack.copy();
            } else {
                ItemStack currentStack = this.mint.getStorage().getItem(0);
                if (currentStack.isEmpty()) {
                    if (stack.getCount() > stack.getMaxStackSize()) {
                        stack = stack.copy();
                        ItemStack placeStack = stack.split(stack.getMaxStackSize());
                        if (!simulate) {
                            this.mint.getStorage().setItem(0, placeStack);
                        }
                        return stack;
                    } else {
                        if (!simulate) {
                            this.mint.getStorage().setItem(0, stack.copy());
                        }
                        return ItemStack.EMPTY;
                    }
                } else if (InventoryUtil.ItemMatches(currentStack, stack)) {
                    int newAmount = MathUtil.clamp(currentStack.getCount() + stack.getCount(), 0, currentStack.getMaxStackSize());
                    if (!simulate) {
                        ItemStack newStack = currentStack.copy();
                        newStack.setCount(newAmount);
                        this.mint.getStorage().setItem(0, newStack);
                    }
                    ItemStack leftoverStack = stack.copy();
                    leftoverStack.setCount(stack.getCount() + currentStack.getCount() - newAmount);
                    return leftoverStack;
                } else {
                    return stack.copy();
                }
            }
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot != 1) {
                return ItemStack.EMPTY;
            } else {
                amount = MathUtil.clamp(amount, 0, 64);
                ItemStack currentStack = this.mint.getStorage().getItem(1).copy();
                if (currentStack.isEmpty()) {
                    return ItemStack.EMPTY;
                } else {
                    ItemStack outputStack = currentStack.copy();
                    if (outputStack.getCount() > amount) {
                        outputStack.setCount(amount);
                    }
                    if (!simulate) {
                        currentStack.setCount(currentStack.getCount() - outputStack.getCount());
                        if (currentStack.getCount() <= 0) {
                            currentStack = ItemStack.EMPTY;
                        }
                        this.mint.getStorage().setItem(1, currentStack);
                    }
                    return outputStack;
                }
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot == 0 && this.mint.validMintInput(stack);
        }
    }
}