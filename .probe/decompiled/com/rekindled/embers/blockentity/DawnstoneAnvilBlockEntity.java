package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.tile.IBin;
import com.rekindled.embers.api.tile.IHammerable;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.recipe.IDawnstoneAnvilRecipe;
import com.rekindled.embers.util.Misc;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class DawnstoneAnvilBlockEntity extends BlockEntity implements IHammerable {

    int progress = 0;

    public ItemStackHandler inventory = new ItemStackHandler(2) {

        @Override
        protected void onContentsChanged(int slot) {
            DawnstoneAnvilBlockEntity.this.setChanged();
        }

        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    static Random random = new Random();

    public IDawnstoneAnvilRecipe cachedRecipe = null;

    public DawnstoneAnvilBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.DAWNSTONE_ANVIL_ENTITY.get(), pPos, pBlockState);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(0, 0, 0), this.f_58858_.offset(1, 2, 1));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.progress = nbt.getInt("progress");
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("progress", this.progress);
        nbt.put("inventory", this.inventory.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putInt("progress", this.progress);
        nbt.put("inventory", this.inventory.serializeNBT());
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == ForgeCapabilities.ITEM_HANDLER ? this.holder.cast() : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    public boolean onHit() {
        RecipeWrapper context = new RecipeWrapper(this.inventory);
        this.cachedRecipe = Misc.getRecipe(this.cachedRecipe, RegistryManager.DAWNSTONE_ANVIL_RECIPE.get(), context, this.f_58857_);
        if (this.cachedRecipe == null) {
            return false;
        } else {
            this.progress++;
            this.f_58857_.playSound(null, this.f_58858_, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.25F, 2.0F + random.nextFloat());
            if (this.progress > ConfigManager.DAWNSTONE_ANVIL_MAX_HITS.get()) {
                this.progress = 0;
                for (ItemStack result : this.cachedRecipe.getOutput(context)) {
                    BlockEntity bin = this.f_58857_.getBlockEntity(this.f_58858_.below());
                    if (bin instanceof IBin) {
                        ItemStack remainder = ((IBin) bin).getInventory().insertItem(0, result, false);
                        if (!remainder.isEmpty() && !this.f_58857_.isClientSide()) {
                            this.f_58857_.m_7967_(new ItemEntity(this.f_58857_, (double) this.f_58858_.m_123341_() + 0.5, (double) ((float) this.f_58858_.m_123342_() + 1.0625F), (double) this.f_58858_.m_123343_() + 0.5, remainder));
                        }
                    } else if (!this.f_58857_.isClientSide()) {
                        this.f_58857_.m_7967_(new ItemEntity(this.f_58857_, (double) this.f_58858_.m_123341_() + 0.5, (double) ((float) this.f_58858_.m_123342_() + 1.0625F), (double) this.f_58858_.m_123343_() + 0.5, result));
                    }
                }
                this.inventory.setStackInSlot(0, ItemStack.EMPTY);
                this.inventory.setStackInSlot(1, ItemStack.EMPTY);
                if (this.f_58857_ instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, 1.0F), (double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 1.0625F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), 10, 0.1, 0.0, 0.1, 1.0);
                    serverLevel.sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 3.0F), (double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 1.0625F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), 10, 0.1, 0.0, 0.1, 1.0);
                }
                this.f_58857_.playSound(null, this.f_58858_, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 1.0F, 0.95F + random.nextFloat() * 0.1F);
            } else if (this.f_58857_ instanceof ServerLevel serverLevel) {
                this.setChanged();
                serverLevel.sendParticles(new SparkParticleOptions(GlowParticleOptions.EMBER_COLOR, 1.0F), (double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() + 1.0625F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), 1, 0.02, 0.0, 0.02, 1.0);
            }
            return true;
        }
    }

    @Override
    public void onHit(BlockEntity hammer) {
        this.progress = ConfigManager.DAWNSTONE_ANVIL_MAX_HITS.get();
        this.onHit();
    }

    @Override
    public boolean isValid() {
        this.cachedRecipe = Misc.getRecipe(this.cachedRecipe, RegistryManager.DAWNSTONE_ANVIL_RECIPE.get(), new RecipeWrapper(this.inventory), this.f_58857_);
        return this.cachedRecipe != null;
    }
}