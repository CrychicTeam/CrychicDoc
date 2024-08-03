package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.AlchemyResultEvent;
import com.rekindled.embers.api.event.AlchemyStartEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.misc.AlchemyResult;
import com.rekindled.embers.api.tile.IBin;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.ISparkable;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.AlchemyCircleParticleOptions;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.StarParticleOptions;
import com.rekindled.embers.recipe.AlchemyContext;
import com.rekindled.embers.recipe.IAlchemyRecipe;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class AlchemyTabletBlockEntity extends BlockEntity implements ISparkable, ISoundController, IExtraCapabilityInformation {

    public static final Direction[] UPGRADE_SIDES = new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN };

    public static final int CONSUME_AMOUNT = 2;

    public static final int SPARK_THRESHOLD = 1000;

    public static final int PROCESSING_TIME = 40;

    public AlchemyTabletBlockEntity.TabletItemStackHandler inventory = new AlchemyTabletBlockEntity.TabletItemStackHandler(1, this);

    public IItemHandler outputHandler = new IItemHandler() {

        @Override
        public int getSlots() {
            return AlchemyTabletBlockEntity.this.inventory.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return AlchemyTabletBlockEntity.this.inventory.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return AlchemyTabletBlockEntity.this.inventory.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return AlchemyTabletBlockEntity.this.outputMode ? AlchemyTabletBlockEntity.this.inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            return AlchemyTabletBlockEntity.this.inventory.getSlotLimit(slot);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return AlchemyTabletBlockEntity.this.inventory.isItemValid(slot, stack);
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public LazyOptional<IItemHandler> outputHolder = LazyOptional.of(() -> this.outputHandler);

    public boolean outputMode = false;

    public int progress = 0;

    public int process = 0;

    static Random rand = new Random();

    public IAlchemyRecipe cachedRecipe = null;

    protected List<UpgradeContext> upgrades = new ArrayList();

    public static final int SOUND_PROCESS = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public AlchemyTabletBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.ALCHEMY_TABLET_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("outputMode")) {
            this.outputMode = nbt.getBoolean("outputMode");
        }
        this.progress = nbt.getInt("progress");
        this.inventory.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putBoolean("outputMode", this.outputMode);
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

    public static void clientTick(Level level, BlockPos pos, BlockState state, AlchemyTabletBlockEntity blockEntity) {
        blockEntity.handleSound();
        if (blockEntity.progress > 0) {
            if (blockEntity.process < 20) {
                blockEntity.process++;
            }
            List<AlchemyPedestalTopBlockEntity> pedestals = getNearbyPedestals(level, pos);
            for (AlchemyPedestalTopBlockEntity pedestal : pedestals) {
                pedestal.setActive(3);
                level.addParticle(StarParticleOptions.EMBER, (double) ((float) pedestal.m_58899_().m_123341_() + 0.5F), (double) ((float) pedestal.m_58899_().m_123342_() + 0.75F), (double) ((float) pedestal.m_58899_().m_123343_() + 0.5F), 0.0, 1.0E-5, 0.0);
                for (int j = 0; j < 16; j++) {
                    float coeff = rand.nextFloat();
                    float x = ((float) pos.m_123341_() + 0.5F) * coeff + (1.0F - coeff) * ((float) pedestal.m_58899_().m_123341_() + 0.5F);
                    float y = ((float) pos.m_123342_() + 0.875F) * coeff + (1.0F - coeff) * ((float) pedestal.m_58899_().m_123342_() + 0.75F);
                    float z = ((float) pos.m_123343_() + 0.5F) * coeff + (1.0F - coeff) * ((float) pedestal.m_58899_().m_123343_() + 0.5F);
                    level.addParticle(GlowParticleOptions.EMBER, (double) x, (double) y, (double) z, 0.0, 1.0E-5, 0.0);
                }
            }
            if (level.getGameTime() % 10L == 0L && pedestals.size() > 0) {
                AlchemyPedestalTopBlockEntity pedestal = (AlchemyPedestalTopBlockEntity) pedestals.get(rand.nextInt(pedestals.size()));
                float dx = (float) pos.m_123341_() + 0.5F - ((float) pedestal.m_58899_().m_123341_() + 0.5F);
                float dy = (float) pos.m_123342_() + 0.875F - ((float) pedestal.m_58899_().m_123342_() + 0.75F);
                float dz = (float) pos.m_123343_() + 0.5F - ((float) pedestal.m_58899_().m_123343_() + 0.5F);
                float speed = 0.5F;
                for (int j = 0; j < 20; j++) {
                    level.addParticle(StarParticleOptions.EMBER, (double) ((float) pedestal.m_58899_().m_123341_() + 0.5F), (double) ((float) pedestal.m_58899_().m_123342_() + 0.75F), (double) ((float) pedestal.m_58899_().m_123343_() + 0.5F), (double) (dx * speed), (double) (dy * speed), (double) (dz * speed));
                }
            }
        } else if (blockEntity.progress == 0 && blockEntity.process > 0) {
            blockEntity.process--;
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AlchemyTabletBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, UPGRADE_SIDES);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (blockEntity.progress > 0 && level.getGameTime() % 10L == 0L) {
            List<AlchemyPedestalTopBlockEntity> pedestals = getNearbyPedestals(level, pos);
            if (blockEntity.progress < 40) {
                blockEntity.progress++;
                blockEntity.setChanged();
            } else {
                List<IAlchemyRecipe.PedestalContents> contents = getPedestalContents(pedestals);
                AlchemyContext context = new AlchemyContext(blockEntity.inventory.getStackInSlot(0), contents, ((ServerLevel) level).getSeed());
                blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.ALCHEMY.get(), context, level);
                if (blockEntity.cachedRecipe != null) {
                    AlchemyResult result = blockEntity.cachedRecipe.getResult(context);
                    AlchemyResultEvent event = new AlchemyResultEvent(blockEntity, blockEntity.cachedRecipe, result, 2);
                    UpgradeUtil.throwEvent(blockEntity, event, blockEntity.upgrades);
                    ItemStack stack = event.isFailure() ? event.getResult().createResultStack(event.getResultStack().copy()) : event.getResultStack().copy();
                    SoundEvent finishSound = event.isFailure() ? EmbersSounds.ALCHEMY_FAIL.get() : EmbersSounds.ALCHEMY_SUCCESS.get();
                    level.playSound(null, pos, finishSound, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!event.isFailure()) {
                        UpgradeUtil.throwEvent(blockEntity, new MachineRecipeEvent.Success<>(blockEntity, blockEntity.cachedRecipe), blockEntity.upgrades);
                        for (AlchemyPedestalTopBlockEntity pedestal : pedestals) {
                            pedestal.inventory.setStackInSlot(0, ItemStack.EMPTY);
                        }
                    } else {
                        for (int i = 0; i < event.getConsumeAmount(); i++) {
                            ((AlchemyPedestalTopBlockEntity) pedestals.get(rand.nextInt(pedestals.size()))).inventory.setStackInSlot(0, ItemStack.EMPTY);
                        }
                    }
                    ((ServerLevel) level).sendParticles(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, 4.0F), (double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_() + 0.875, (double) ((float) pos.m_123343_() + 0.5F), 24, 0.1, 0.1, 0.1, 0.5);
                    blockEntity.progress = 0;
                    if (level.getBlockEntity(pos.below()) instanceof IBin bin && bin.getInventory().insertItem(0, stack, true).isEmpty()) {
                        blockEntity.inventory.extractItem(0, 1, false);
                        bin.getInventory().insertItem(0, stack, false);
                        return;
                    }
                    blockEntity.outputMode = true;
                    blockEntity.inventory.extractItem(0, 1, false);
                    ItemStack remainder = blockEntity.inventory.forceInsertItem(0, stack, false);
                    if (!remainder.isEmpty()) {
                        level.m_7967_(new ItemEntity(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.0, (double) pos.m_123343_() + 0.5, remainder));
                    }
                    blockEntity.outputMode = true;
                } else {
                    blockEntity.progress = 0;
                    blockEntity.setChanged();
                }
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (this.f_58859_ || cap != ForgeCapabilities.ITEM_HANDLER) {
            return super.getCapability(cap, side);
        } else {
            return side == Direction.DOWN ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.outputHolder) : ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
        }
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

    @Override
    public void sparkProgress(BlockEntity tile, double ember) {
        if (this.progress == 0 && !(ember < 1000.0)) {
            List<IAlchemyRecipe.PedestalContents> pedestals = getPedestalContents(getNearbyPedestals(this.f_58857_, this.f_58858_));
            AlchemyContext context = new AlchemyContext(this.inventory.getStackInSlot(0), pedestals, ((ServerLevel) this.f_58857_).getSeed());
            this.cachedRecipe = Misc.getRecipe(this.cachedRecipe, RegistryManager.ALCHEMY.get(), context, this.f_58857_);
            AlchemyStartEvent event = new AlchemyStartEvent(this, context, this.cachedRecipe);
            UpgradeUtil.throwEvent(this, event, this.upgrades);
            if (event.getRecipe() != null) {
                ((ServerLevel) this.f_58857_).sendParticles(AlchemyCircleParticleOptions.DEFAULT, (double) this.f_58858_.m_123341_() + 0.5, (double) this.f_58858_.m_123342_() + 1.01, (double) this.f_58858_.m_123343_() + 0.5, 5, 0.0, 0.0, 0.0, 1.0);
                this.progress = 1;
                this.setChanged();
                this.f_58857_.playSound(null, this.f_58858_, EmbersSounds.ALCHEMY_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }
    }

    public static ArrayList<IAlchemyRecipe.PedestalContents> getPedestalContents(List<AlchemyPedestalTopBlockEntity> pedestals) {
        ArrayList<IAlchemyRecipe.PedestalContents> contents = new ArrayList();
        for (AlchemyPedestalTopBlockEntity pedestal : pedestals) {
            contents.add(pedestal.getContents());
        }
        return contents;
    }

    public static ArrayList<AlchemyPedestalTopBlockEntity> getNearbyPedestals(Level world, BlockPos pos) {
        ArrayList<AlchemyPedestalTopBlockEntity> pedestals = new ArrayList();
        BlockPos.MutableBlockPos pedestalPos = pos.mutable();
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                pedestalPos.set(pos.m_123341_() + i, pos.m_123342_() + 1, pos.m_123343_() + j);
                BlockEntity tile = world.getBlockEntity(pedestalPos);
                if (tile instanceof AlchemyPedestalTopBlockEntity && ((AlchemyPedestalTopBlockEntity) tile).isValid()) {
                    pedestals.add((AlchemyPedestalTopBlockEntity) tile);
                }
            }
        }
        return pedestals;
    }

    @Override
    public void playSound(int id) {
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.ALCHEMY_LOOP.get(), SoundSource.BLOCKS, true, 1.5F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 1.0F, (float) this.f_58858_.m_123343_() + 0.5F);
            default:
                this.soundsPlaying.add(id);
        }
    }

    @Override
    public void stopSound(int id) {
        this.soundsPlaying.remove(id);
    }

    @Override
    public boolean isSoundPlaying(int id) {
        return this.soundsPlaying.contains(id);
    }

    @Override
    public int[] getSoundIDs() {
        return SOUND_IDS;
    }

    @Override
    public boolean shouldPlaySound(int id) {
        return id == 1 && this.progress > 0;
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER && facing == Direction.DOWN) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.alchemy_result")));
        } else {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.BOTH, "embers.tooltip.goggles.item", null));
        }
    }

    public static class TabletItemStackHandler extends ItemStackHandler {

        AlchemyTabletBlockEntity entity;

        public TabletItemStackHandler(int size, AlchemyTabletBlockEntity entity) {
            super(size);
            this.entity = entity;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        public ItemStack forceInsertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (!this.isItemValid(slot, stack)) {
                return stack;
            } else {
                this.validateSlotIndex(slot);
                ItemStack existing = this.stacks.get(slot);
                int limit = 64;
                if (!existing.isEmpty()) {
                    if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                        return stack;
                    }
                    limit -= existing.getCount();
                }
                if (limit <= 0) {
                    return stack;
                } else {
                    boolean reachedLimit = stack.getCount() > limit;
                    if (!simulate) {
                        if (existing.isEmpty()) {
                            this.stacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                        } else {
                            existing.grow(reachedLimit ? limit : stack.getCount());
                        }
                        this.onContentsChanged(slot);
                    }
                    return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
                }
            }
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (this.getStackInSlot(slot).isEmpty()) {
                this.entity.outputMode = false;
            }
            this.entity.setChanged();
        }
    }
}