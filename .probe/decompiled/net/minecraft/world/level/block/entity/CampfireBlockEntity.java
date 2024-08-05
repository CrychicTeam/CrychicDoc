package net.minecraft.world.level.block.entity;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Clearable;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class CampfireBlockEntity extends BlockEntity implements Clearable {

    private static final int BURN_COOL_SPEED = 2;

    private static final int NUM_SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    private final int[] cookingProgress = new int[4];

    private final int[] cookingTime = new int[4];

    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);

    public CampfireBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.CAMPFIRE, blockPos0, blockState1);
    }

    public static void cookTick(Level level0, BlockPos blockPos1, BlockState blockState2, CampfireBlockEntity campfireBlockEntity3) {
        boolean $$4 = false;
        for (int $$5 = 0; $$5 < campfireBlockEntity3.items.size(); $$5++) {
            ItemStack $$6 = campfireBlockEntity3.items.get($$5);
            if (!$$6.isEmpty()) {
                $$4 = true;
                campfireBlockEntity3.cookingProgress[$$5]++;
                if (campfireBlockEntity3.cookingProgress[$$5] >= campfireBlockEntity3.cookingTime[$$5]) {
                    Container $$7 = new SimpleContainer($$6);
                    ItemStack $$8 = (ItemStack) campfireBlockEntity3.quickCheck.getRecipeFor($$7, level0).map(p_270054_ -> p_270054_.m_5874_($$7, level0.registryAccess())).orElse($$6);
                    if ($$8.isItemEnabled(level0.m_246046_())) {
                        Containers.dropItemStack(level0, (double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_(), $$8);
                        campfireBlockEntity3.items.set($$5, ItemStack.EMPTY);
                        level0.sendBlockUpdated(blockPos1, blockState2, blockState2, 3);
                        level0.m_220407_(GameEvent.BLOCK_CHANGE, blockPos1, GameEvent.Context.of(blockState2));
                    }
                }
            }
        }
        if ($$4) {
            m_155232_(level0, blockPos1, blockState2);
        }
    }

    public static void cooldownTick(Level level0, BlockPos blockPos1, BlockState blockState2, CampfireBlockEntity campfireBlockEntity3) {
        boolean $$4 = false;
        for (int $$5 = 0; $$5 < campfireBlockEntity3.items.size(); $$5++) {
            if (campfireBlockEntity3.cookingProgress[$$5] > 0) {
                $$4 = true;
                campfireBlockEntity3.cookingProgress[$$5] = Mth.clamp(campfireBlockEntity3.cookingProgress[$$5] - 2, 0, campfireBlockEntity3.cookingTime[$$5]);
            }
        }
        if ($$4) {
            m_155232_(level0, blockPos1, blockState2);
        }
    }

    public static void particleTick(Level level0, BlockPos blockPos1, BlockState blockState2, CampfireBlockEntity campfireBlockEntity3) {
        RandomSource $$4 = level0.random;
        if ($$4.nextFloat() < 0.11F) {
            for (int $$5 = 0; $$5 < $$4.nextInt(2) + 2; $$5++) {
                CampfireBlock.makeParticles(level0, blockPos1, (Boolean) blockState2.m_61143_(CampfireBlock.SIGNAL_FIRE), false);
            }
        }
        int $$6 = ((Direction) blockState2.m_61143_(CampfireBlock.FACING)).get2DDataValue();
        for (int $$7 = 0; $$7 < campfireBlockEntity3.items.size(); $$7++) {
            if (!campfireBlockEntity3.items.get($$7).isEmpty() && $$4.nextFloat() < 0.2F) {
                Direction $$8 = Direction.from2DDataValue(Math.floorMod($$7 + $$6, 4));
                float $$9 = 0.3125F;
                double $$10 = (double) blockPos1.m_123341_() + 0.5 - (double) ((float) $$8.getStepX() * 0.3125F) + (double) ((float) $$8.getClockWise().getStepX() * 0.3125F);
                double $$11 = (double) blockPos1.m_123342_() + 0.5;
                double $$12 = (double) blockPos1.m_123343_() + 0.5 - (double) ((float) $$8.getStepZ() * 0.3125F) + (double) ((float) $$8.getClockWise().getStepZ() * 0.3125F);
                for (int $$13 = 0; $$13 < 4; $$13++) {
                    level0.addParticle(ParticleTypes.SMOKE, $$10, $$11, $$12, 0.0, 5.0E-4, 0.0);
                }
            }
        }
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.items.clear();
        ContainerHelper.loadAllItems(compoundTag0, this.items);
        if (compoundTag0.contains("CookingTimes", 11)) {
            int[] $$1 = compoundTag0.getIntArray("CookingTimes");
            System.arraycopy($$1, 0, this.cookingProgress, 0, Math.min(this.cookingTime.length, $$1.length));
        }
        if (compoundTag0.contains("CookingTotalTimes", 11)) {
            int[] $$2 = compoundTag0.getIntArray("CookingTotalTimes");
            System.arraycopy($$2, 0, this.cookingTime, 0, Math.min(this.cookingTime.length, $$2.length));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        ContainerHelper.saveAllItems(compoundTag0, this.items, true);
        compoundTag0.putIntArray("CookingTimes", this.cookingProgress);
        compoundTag0.putIntArray("CookingTotalTimes", this.cookingTime);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag $$0 = new CompoundTag();
        ContainerHelper.saveAllItems($$0, this.items, true);
        return $$0;
    }

    public Optional<CampfireCookingRecipe> getCookableRecipe(ItemStack itemStack0) {
        return this.items.stream().noneMatch(ItemStack::m_41619_) ? Optional.empty() : this.quickCheck.getRecipeFor(new SimpleContainer(itemStack0), this.f_58857_);
    }

    public boolean placeFood(@Nullable Entity entity0, ItemStack itemStack1, int int2) {
        for (int $$3 = 0; $$3 < this.items.size(); $$3++) {
            ItemStack $$4 = this.items.get($$3);
            if ($$4.isEmpty()) {
                this.cookingTime[$$3] = int2;
                this.cookingProgress[$$3] = 0;
                this.items.set($$3, itemStack1.split(1));
                this.f_58857_.m_220407_(GameEvent.BLOCK_CHANGE, this.m_58899_(), GameEvent.Context.of(entity0, this.m_58900_()));
                this.markUpdated();
                return true;
            }
        }
        return false;
    }

    private void markUpdated() {
        this.m_6596_();
        this.m_58904_().sendBlockUpdated(this.m_58899_(), this.m_58900_(), this.m_58900_(), 3);
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    public void dowse() {
        if (this.f_58857_ != null) {
            this.markUpdated();
        }
    }
}