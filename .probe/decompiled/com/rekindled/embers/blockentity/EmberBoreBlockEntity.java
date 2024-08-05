package com.rekindled.embers.blockentity;

import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.tile.IMechanicallyPowered;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.recipe.BoringContext;
import com.rekindled.embers.recipe.IBoringRecipe;
import com.rekindled.embers.util.EmberGenUtil;
import com.rekindled.embers.util.WeightedItemStack;
import com.rekindled.embers.util.sound.ISoundController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class EmberBoreBlockEntity extends BlockEntity implements ISoundController, IMechanicallyPowered, IExtraDialInformation, IExtraCapabilityInformation {

    public static final int SLOT_FUEL = 8;

    public static final double SUPERSPEED_THRESHOLD = 2.5;

    public static final int SOUND_ON = 1;

    public static final int SOUND_ON_DRILL = 2;

    public static final int SOUND_ON_SUPERSPEED = 3;

    public static final int SOUND_ON_DRILL_SUPERSPEED = 4;

    public static final int[] SOUND_IDS = new int[] { 1, 2, 3, 4 };

    Random random = new Random();

    public long ticksExisted = 0L;

    public float angle = 0.0F;

    public double ticksFueled = 0.0;

    public float lastAngle;

    public boolean isRunning;

    HashSet<Integer> soundsPlaying = new HashSet();

    public List<UpgradeContext> upgrades = new ArrayList();

    public double speedMod;

    public EmberBoreBlockEntity.EmberBoreInventory inventory = new EmberBoreBlockEntity.EmberBoreInventory(9);

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    Boolean canMine = null;

    public EmberBoreBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.EMBER_BORE_ENTITY.get(), pPos, pBlockState);
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, -2, -1), this.f_58858_.offset(2, -1, 2));
    }

    public AABB getBladeBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, -2, -1), this.f_58858_.offset(1, -1, 1));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
        if (nbt.contains("fueled")) {
            this.ticksFueled = nbt.getDouble("fueled");
        }
        this.isRunning = nbt.getBoolean("isRunning");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
        nbt.putDouble("fueled", this.ticksFueled);
        nbt.putBoolean("isRunning", this.isRunning);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putBoolean("isRunning", this.isRunning);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean canMine() {
        if (this.canMine == null) {
            ResourceKey<Biome> biome = (ResourceKey<Biome>) this.f_58857_.m_204166_(this.f_58858_).unwrapKey().get();
            if (biome != null) {
                BoringContext context = new BoringContext(this.f_58857_.dimension().location(), biome.location(), this.f_58858_.m_123342_(), (BlockState[]) this.f_58857_.m_46847_(this.getBladeBoundingBox()).toArray(BlockState[]::new));
                List<IBoringRecipe> recipes = this.f_58857_.getRecipeManager().getRecipesFor(RegistryManager.BORING.get(), context, this.f_58857_);
                UpgradeUtil.throwEvent(this, new MachineRecipeEvent<>(this, recipes), this.upgrades);
                this.canMine = !recipes.isEmpty();
            } else {
                this.canMine = false;
            }
        }
        return this.canMine;
    }

    public boolean isSuperSpeed() {
        return this.speedMod >= 2.5;
    }

    public boolean canInsert(ArrayList<ItemStack> returns) {
        for (ItemStack stack : returns) {
            ItemStack returned = stack;
            for (int slot = 0; slot < this.inventory.getSlots() - 1; slot++) {
                returned = this.inventory.insertItemInternal(slot, returned, true);
            }
            if (!returned.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void insert(ArrayList<ItemStack> returns) {
        for (ItemStack stack : returns) {
            ItemStack returned = stack;
            for (int slot = 0; slot < this.inventory.getSlots() - 1; slot++) {
                returned = this.inventory.insertItemInternal(slot, returned, false);
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, EmberBoreBlockEntity blockEntity) {
        commonTick(level, pos, state, blockEntity);
        boolean previousRunning = blockEntity.isRunning;
        blockEntity.isRunning = false;
        blockEntity.ticksExisted++;
        double fuelConsumption = UpgradeUtil.getOtherParameter(blockEntity, "fuel_consumption", ConfigManager.EMBER_BORE_FUEL_CONSUMPTION.get().doubleValue(), blockEntity.upgrades);
        boolean cancel = false;
        if (blockEntity.ticksFueled >= fuelConsumption) {
            blockEntity.isRunning = true;
            blockEntity.ticksFueled -= fuelConsumption;
            cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
        } else {
            blockEntity.ticksFueled = 0.0;
        }
        if (!cancel) {
            if (blockEntity.ticksFueled < fuelConsumption) {
                ItemStack fuel = blockEntity.inventory.getStackInSlot(8);
                if (!fuel.isEmpty()) {
                    ItemStack fuelCopy = fuel.copy();
                    int burnTime = ForgeHooks.getBurnTime(fuelCopy, RegistryManager.BORING.get());
                    if (burnTime > 0) {
                        blockEntity.ticksFueled = (double) burnTime;
                        fuel.shrink(1);
                        if (fuel.isEmpty()) {
                            blockEntity.inventory.setStackInSlot(8, fuelCopy.getItem().getCraftingRemainingItem(fuelCopy));
                        }
                        blockEntity.setChanged();
                    }
                }
            } else {
                int boreTime = (int) Math.ceil((double) ConfigManager.EMBER_BORE_TIME.get().intValue() / (blockEntity.speedMod * ConfigManager.EMBER_BORE_SPEED_MOD.get()));
                if (blockEntity.ticksExisted % (long) boreTime == 0L) {
                    ResourceKey<Biome> biome = (ResourceKey<Biome>) level.m_204166_(pos).unwrapKey().get();
                    if (biome != null) {
                        BoringContext context = new BoringContext(level.dimension().location(), biome.location(), pos.m_123342_(), (BlockState[]) level.m_46847_(blockEntity.getBladeBoundingBox()).toArray(BlockState[]::new));
                        List<IBoringRecipe> recipes = level.getRecipeManager().getRecipesFor(RegistryManager.BORING.get(), context, level);
                        UpgradeUtil.throwEvent(blockEntity, new MachineRecipeEvent<>(blockEntity, recipes), blockEntity.upgrades);
                        ArrayList<WeightedItemStack> stacks = new ArrayList();
                        float rand = blockEntity.random.nextFloat();
                        double chance = (double) EmberGenUtil.getEmberDensity(((ServerLevel) level).getSeed(), pos.m_123341_(), pos.m_123343_());
                        for (IBoringRecipe recipe : recipes) {
                            if ((double) rand < (recipe.getChance() == -1.0 ? chance : recipe.getChance())) {
                                stacks.add(recipe.getOutput(context));
                            }
                        }
                        ArrayList<ItemStack> returns = new ArrayList();
                        if (!stacks.isEmpty()) {
                            Optional<WeightedItemStack> picked = WeightedRandom.getRandomItem(level.getRandom(), stacks);
                            returns.add(((WeightedItemStack) picked.get()).getStack().copy());
                        }
                        UpgradeUtil.transformOutput(blockEntity, returns, blockEntity.upgrades);
                        if (blockEntity.canInsert(returns)) {
                            blockEntity.insert(returns);
                        }
                    }
                }
            }
        } else {
            blockEntity.isRunning = false;
        }
        if (blockEntity.isRunning != previousRunning) {
            blockEntity.setChanged();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, EmberBoreBlockEntity blockEntity) {
        commonTick(level, pos, state, blockEntity);
        blockEntity.handleSound();
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, EmberBoreBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { Direction.UP });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            blockEntity.speedMod = UpgradeUtil.getTotalSpeedModifier(blockEntity, blockEntity.upgrades);
            blockEntity.lastAngle = blockEntity.angle;
            if (blockEntity.isRunning) {
                blockEntity.angle = (float) ((double) blockEntity.angle + 12.0 * blockEntity.speedMod);
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && (side == null || side == Direction.UP) && cap == ForgeCapabilities.ITEM_HANDLER ? ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder) : super.getCapability(cap, side);
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
    public void playSound(int id) {
        float soundX = (float) this.f_58858_.m_123341_() + 0.5F;
        float soundY = (float) this.f_58858_.m_123342_() - 0.5F;
        float soundZ = (float) this.f_58858_.m_123343_() + 0.5F;
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.BORE_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 2:
                EmbersSounds.playMachineSound(this, 2, EmbersSounds.BORE_LOOP_MINE.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 3:
                EmbersSounds.playMachineSound(this, 3, EmbersSounds.BORE_LOOP_SUPERSPEED.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 4:
                EmbersSounds.playMachineSound(this, 4, EmbersSounds.BORE_LOOP_MINE_SUPERSPEED.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
        }
        this.f_58857_.playLocalSound((double) soundX, (double) soundY, (double) soundZ, EmbersSounds.BORE_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
        this.soundsPlaying.add(id);
    }

    @Override
    public void stopSound(int id) {
        this.f_58857_.playLocalSound((double) ((float) this.f_58858_.m_123341_() + 0.5F), (double) ((float) this.f_58858_.m_123342_() - 0.5F), (double) ((float) this.f_58858_.m_123343_() + 0.5F), EmbersSounds.BORE_STOP.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
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
        switch(id) {
            case 1:
                return this.isRunning && !this.canMine() && !this.isSuperSpeed();
            case 2:
                return this.isRunning && this.canMine() && !this.isSuperSpeed();
            case 3:
                return this.isRunning && !this.canMine() && this.isSuperSpeed();
            case 4:
                return this.isRunning && this.canMine() && this.isSuperSpeed();
            default:
                return false;
        }
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        switch(id) {
            case 1:
                return !this.canMine() && !this.isSuperSpeed() ? 1.0F : 0.0F;
            case 2:
                return this.canMine() && !this.isSuperSpeed() ? 1.0F : 0.0F;
            case 3:
                return !this.canMine() && this.isSuperSpeed() ? 1.0F : 0.0F;
            case 4:
                return this.canMine() && this.isSuperSpeed() ? 1.0F : 0.0F;
            default:
                return 0.0F;
        }
    }

    @Override
    public float getCurrentPitch(int id, float pitch) {
        return this.isSuperSpeed() ? (float) (this.speedMod + 1.0 - 2.5) : (float) this.speedMod;
    }

    @Override
    public double getMechanicalSpeed(double power) {
        return power > 0.0 ? Math.log10(power / 15.0) * 3.0 : 0.0;
    }

    @Override
    public double getMinimumPower() {
        return 15.0;
    }

    @Override
    public double getNominalSpeed() {
        return 1.0;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.fuel")));
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.ember")));
        }
    }

    public class EmberBoreInventory extends ItemStackHandler {

        public EmberBoreInventory() {
        }

        public EmberBoreInventory(int size) {
            super(size);
        }

        public EmberBoreInventory(NonNullList<ItemStack> stacks) {
            super(stacks);
        }

        @Override
        protected void onContentsChanged(int slot) {
            EmberBoreBlockEntity.this.setChanged();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            ItemStack currentFuel = this.getStackInSlot(8);
            if (currentFuel.isEmpty()) {
                int burnTime = ForgeHooks.getBurnTime(stack, RegistryManager.BORING.get());
                return burnTime != 0 ? super.insertItem(8, stack, simulate) : stack;
            } else {
                return super.insertItem(8, stack, simulate);
            }
        }

        public ItemStack insertItemInternal(int slot, ItemStack stack, boolean simulate) {
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 8) {
                ItemStack fuelStack = this.getStackInSlot(8);
                if (fuelStack.getCount() > 1 || ForgeHooks.getBurnTime(fuelStack, RegistryManager.BORING.get()) != 0) {
                    return ItemStack.EMPTY;
                }
            }
            return super.extractItem(slot, amount, simulate);
        }
    }
}