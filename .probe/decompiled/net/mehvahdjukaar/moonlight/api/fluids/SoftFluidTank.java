package net.mehvahdjukaar.moonlight.api.fluids;

import com.mojang.datafixers.util.Pair;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.fluids.forge.SoftFluidTankImpl;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SoftFluidTank {

    public static final int BOTTLE_COUNT = 1;

    public static final int BOWL_COUNT = 2;

    public static final int BUCKET_COUNT = 4;

    protected final int capacity;

    protected SoftFluidStack fluidStack = SoftFluidStack.empty();

    protected int stillTintCache = 0;

    protected int flowingTintCache = 0;

    protected int particleTintCache = 0;

    protected boolean needsColorRefresh = true;

    protected SoftFluidTank(int capacity) {
        this.capacity = capacity;
    }

    @ExpectPlatform
    @Transformed
    public static SoftFluidTank create(int capacity) {
        return SoftFluidTankImpl.create(capacity);
    }

    public boolean interactWithPlayer(Player player, InteractionHand hand, @Nullable Level world, @Nullable BlockPos pos) {
        ItemStack handStack = player.m_21120_(hand);
        ItemStack returnStack = this.interactWithItem(handStack, world, pos, false);
        if (returnStack != null) {
            Utils.swapItem(player, hand, returnStack);
            if (!handStack.isEmpty()) {
                player.awardStat(Stats.ITEM_USED.get(handStack.getItem()));
            }
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public ItemStack interactWithItem(ItemStack stack, Level world, @Nullable BlockPos pos, boolean simulate) {
        InteractionResultHolder<ItemStack> fillResult = this.fillItem(stack, world, pos, simulate);
        if (fillResult.getResult().consumesAction()) {
            return fillResult.getObject();
        } else {
            InteractionResultHolder<ItemStack> drainResult = this.drainItem(stack, world, pos, simulate);
            return drainResult.getResult().consumesAction() ? drainResult.getObject() : null;
        }
    }

    public InteractionResultHolder<ItemStack> drainItem(ItemStack filledContainerStack, @Nullable Level world, @Nullable BlockPos pos, boolean simulate) {
        return this.drainItem(filledContainerStack, world, pos, simulate, true);
    }

    public InteractionResultHolder<ItemStack> drainItem(ItemStack filledContainer, Level level, @Nullable BlockPos pos, boolean simulate, boolean playSound) {
        Pair<SoftFluidStack, FluidContainerList.Category> extracted = SoftFluidStack.fromItem(filledContainer);
        if (extracted == null) {
            return InteractionResultHolder.pass(ItemStack.EMPTY);
        } else {
            SoftFluidStack fluidStack = (SoftFluidStack) extracted.getFirst();
            if (this.addFluid(fluidStack, true) == fluidStack.getCount()) {
                FluidContainerList.Category category = (FluidContainerList.Category) extracted.getSecond();
                ItemStack emptyContainer = category.getEmptyContainer().getDefaultInstance();
                if (!simulate) {
                    this.addFluid(fluidStack, false);
                    SoundEvent sound = category.getEmptySound();
                    if (sound != null && pos != null) {
                        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
                return InteractionResultHolder.sidedSuccess(emptyContainer, level.isClientSide);
            } else {
                return InteractionResultHolder.pass(ItemStack.EMPTY);
            }
        }
    }

    public InteractionResultHolder<ItemStack> fillItem(ItemStack emptyContainer, @Nullable Level world, @Nullable BlockPos pos, boolean simulate) {
        return this.fillItem(emptyContainer, world, pos, simulate, true);
    }

    public InteractionResultHolder<ItemStack> fillItem(ItemStack emptyContainer, Level level, @Nullable BlockPos pos, boolean simulate, boolean playSound) {
        Pair<ItemStack, FluidContainerList.Category> pair = this.fluidStack.toItem(emptyContainer, simulate);
        if (pair != null) {
            FluidContainerList.Category category = (FluidContainerList.Category) pair.getSecond();
            SoundEvent sound = category.getEmptySound();
            if (sound != null && pos != null) {
                level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return InteractionResultHolder.sidedSuccess((ItemStack) pair.getFirst(), level.isClientSide);
        } else {
            return InteractionResultHolder.pass(ItemStack.EMPTY);
        }
    }

    protected void addFluidOntoExisting(SoftFluidStack stack) {
        this.fluidStack.grow(stack.getCount());
    }

    @Nullable
    public InteractionResultHolder<ItemStack> fillBottle(Level world, BlockPos pos) {
        return this.fillItem(Items.GLASS_BOTTLE.getDefaultInstance(), world, pos, false);
    }

    @Nullable
    public InteractionResultHolder<ItemStack> fillBucket(Level world, BlockPos pos) {
        return this.fillItem(Items.BUCKET.getDefaultInstance(), world, pos, false);
    }

    @Nullable
    public InteractionResultHolder<ItemStack> fillBowl(Level world, BlockPos pos) {
        return this.fillItem(Items.BOWL.getDefaultInstance(), world, pos, false);
    }

    @Deprecated(forRemoval = true)
    public boolean canAddSoftFluid(SoftFluidStack fluidStack) {
        return this.getSpace() < fluidStack.getCount() ? false : this.isFluidCompatible(fluidStack);
    }

    public boolean isFluidCompatible(SoftFluidStack fluidStack) {
        return this.fluidStack.isFluidEqual(fluidStack) || this.isEmpty();
    }

    @Deprecated(forRemoval = true)
    public boolean addFluid(SoftFluidStack stack) {
        return this.addFluid(stack, false) == stack.getCount();
    }

    public int addFluid(SoftFluidStack stack, boolean simulate) {
        if (!this.isFluidCompatible(stack)) {
            return 0;
        } else {
            int space = this.getSpace();
            if (space == 0) {
                return 0;
            } else {
                int amount = Math.min(space, stack.getCount());
                if (simulate) {
                    return amount;
                } else {
                    SoftFluidStack toAdd = stack.split(amount);
                    if (this.isEmpty()) {
                        this.setFluid(toAdd);
                    } else {
                        this.addFluidOntoExisting(toAdd);
                    }
                    return amount;
                }
            }
        }
    }

    public SoftFluidStack removeFluid(int amount, boolean simulate) {
        if (this.isEmpty()) {
            return SoftFluidStack.empty();
        } else {
            int toRemove = Math.min(amount, this.fluidStack.getCount());
            SoftFluidStack stack = this.fluidStack.copyWithCount(toRemove);
            if (!simulate) {
                this.fluidStack.shrink(toRemove);
            }
            return stack;
        }
    }

    @Deprecated(forRemoval = true)
    public boolean transferFluid(SoftFluidTank destination) {
        return this.transferFluid(destination, 1);
    }

    @Deprecated(forRemoval = true)
    public boolean transferFluid(SoftFluidTank destination, int amount) {
        if (this.isEmpty()) {
            return false;
        } else {
            SoftFluidStack removed = this.removeFluid(amount, false);
            if (destination.addFluid(removed, true) == removed.getCount()) {
                destination.addFluid(removed, false);
                return true;
            } else {
                return false;
            }
        }
    }

    public int getSpace() {
        return Math.max(0, this.capacity - this.fluidStack.getCount());
    }

    public int getFluidCount() {
        return this.fluidStack.getCount();
    }

    public boolean isFull() {
        return this.fluidStack.getCount() == this.capacity;
    }

    public boolean isEmpty() {
        return this.fluidStack.isEmpty();
    }

    public float getHeight(float maxHeight) {
        return maxHeight * (float) this.fluidStack.getCount() / (float) this.capacity;
    }

    public int getComparatorOutput() {
        float f = (float) this.fluidStack.getCount() / (float) this.capacity;
        return Mth.floor(f * 14.0F) + 1;
    }

    public SoftFluidStack getFluid() {
        return this.fluidStack;
    }

    public SoftFluid getFluidValue() {
        return this.fluidStack.getHolder().value();
    }

    public void setFluid(SoftFluidStack fluid) {
        this.fluidStack = fluid;
        this.refreshTintCache();
    }

    public void refreshTintCache() {
        this.stillTintCache = 0;
        this.needsColorRefresh = true;
    }

    private void fillCount() {
        this.fluidStack.setCount(this.capacity);
    }

    public void clear() {
        this.setFluid(SoftFluidStack.empty());
    }

    public void copyContent(SoftFluidTank other) {
        SoftFluidStack stack = other.getFluid();
        this.setFluid(stack.copyWithCount(Math.min(this.capacity, stack.getCount())));
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void capCapacity() {
        this.fluidStack.setCount(Mth.clamp(this.fluidStack.getCount(), 0, this.capacity));
    }

    private void cacheColors(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        this.stillTintCache = this.fluidStack.getStillColor(world, pos);
        this.flowingTintCache = this.fluidStack.getFlowingColor(world, pos);
        this.particleTintCache = this.fluidStack.getParticleColor(world, pos);
        this.needsColorRefresh = false;
    }

    @Deprecated(forRemoval = true)
    public int getTintColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        return this.getCachedStillColor(world, pos);
    }

    @Deprecated(forRemoval = true)
    public int getFlowingTint(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        return this.getCachedFlowingColor(world, pos);
    }

    @Deprecated(forRemoval = true)
    public int getParticleColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        return this.getCachedParticleColor(world, pos);
    }

    public int getCachedStillColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        if (this.needsColorRefresh) {
            this.cacheColors(world, pos);
        }
        return this.stillTintCache;
    }

    public int getCachedFlowingColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        if (this.needsColorRefresh) {
            this.cacheColors(world, pos);
        }
        return this.flowingTintCache;
    }

    public int getCachedParticleColor(@Nullable BlockAndTintGetter world, @Nullable BlockPos pos) {
        if (this.needsColorRefresh) {
            this.cacheColors(world, pos);
        }
        return this.particleTintCache;
    }

    public boolean containsFood() {
        return !this.fluidStack.getFoodProvider().isEmpty();
    }

    public void load(CompoundTag compound) {
        if (compound.contains("FluidHolder")) {
            CompoundTag cmp = compound.getCompound("FluidHolder");
            this.fluidStack = SoftFluidStack.load(cmp);
            if (this.isEmpty()) {
                this.fluidStack = SoftFluidStack.empty();
            }
        }
    }

    public CompoundTag save(CompoundTag compound) {
        CompoundTag cmp = new CompoundTag();
        if (this.isEmpty()) {
            this.fluidStack = SoftFluidStack.empty();
        }
        this.fluidStack.save(cmp);
        compound.put("FluidHolder", cmp);
        return compound;
    }

    public boolean tryDrinkUpFluid(Player player, Level world) {
        if (!this.isEmpty() && this.containsFood() && this.fluidStack.getFoodProvider().consume(player, world, this.fluidStack::applyNBTtoItemStack)) {
            this.fluidStack.shrink(1);
            return true;
        } else {
            return false;
        }
    }

    public static int getLiquidCountFromItem(Item i) {
        if (i == Items.GLASS_BOTTLE) {
            return 1;
        } else if (i == Items.BOWL) {
            return 2;
        } else {
            return i == Items.BUCKET ? 4 : 0;
        }
    }
}