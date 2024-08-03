package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.recipe.IEmberActivationRecipe;
import com.rekindled.embers.recipe.SingleItemContainer;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class CrystalCellBlockEntity extends BlockEntity implements ISoundController, IExtraDialInformation, IExtraCapabilityInformation {

    public static final int MAX_CAPACITY = 1440000;

    Random random = new Random();

    public long ticksExisted = 0L;

    public float angle = 0.0F;

    public long seed = 0L;

    public double renderCapacity;

    public double renderCapacityLast;

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            CrystalCellBlockEntity.this.setChanged();
        }

        @Override
        public boolean acceptsVolatile() {
            return true;
        }
    };

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            CrystalCellBlockEntity.this.setChanged();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return Misc.getRecipe(CrystalCellBlockEntity.this.cachedRecipe, RegistryManager.EMBER_ACTIVATION.get(), new SingleItemContainer(stack), CrystalCellBlockEntity.this.f_58857_) != null ? super.insertItem(slot, stack, simulate) : stack;
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    public IEmberActivationRecipe cachedRecipe = null;

    protected List<UpgradeContext> upgrades;

    public static final int SOUND_AMBIENT = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public CrystalCellBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.CRYSTAL_CELL_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(64000.0);
        this.seed = this.random.nextLong();
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.f_58858_.offset(-1, 1, -1), this.f_58858_.offset(2, 5, 2));
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.seed = nbt.getLong("seed");
        if (nbt.contains("inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
        this.capability.deserializeNBT(nbt);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putLong("seed", this.seed);
        nbt.put("inventory", this.inventory.serializeNBT());
        this.capability.writeToNBT(nbt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putLong("seed", this.seed);
        this.capability.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void commonTick(Level level, BlockPos pos, BlockState state, CrystalCellBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { Direction.DOWN });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            if (level.isClientSide) {
                blockEntity.handleSound();
            }
            blockEntity.ticksExisted++;
            blockEntity.renderCapacityLast = blockEntity.renderCapacity;
            if (blockEntity.renderCapacity < blockEntity.capability.getEmberCapacity()) {
                blockEntity.renderCapacity = blockEntity.renderCapacity + Math.min(10000.0, blockEntity.capability.getEmberCapacity() - blockEntity.renderCapacity);
            } else {
                blockEntity.renderCapacity = blockEntity.renderCapacity - Math.min(10000.0, blockEntity.renderCapacity - blockEntity.capability.getEmberCapacity());
            }
            if (!blockEntity.inventory.getStackInSlot(0).isEmpty() && blockEntity.ticksExisted % 4L == 0L) {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                if (!cancel) {
                    RecipeWrapper wrapper = new RecipeWrapper(blockEntity.inventory);
                    blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.EMBER_ACTIVATION.get(), wrapper, level);
                    if (blockEntity.cachedRecipe != null) {
                        double emberValue = (double) blockEntity.cachedRecipe.process(wrapper);
                        if (!level.isClientSide) {
                            blockEntity.inventory.extractItem(0, 1, false);
                            int maxCapacity = UpgradeUtil.getOtherParameter(blockEntity, "max_capacity", 1440000, blockEntity.upgrades);
                            if (blockEntity.capability.getEmberCapacity() < (double) maxCapacity) {
                                blockEntity.capability.setEmberCapacity(Math.min((double) maxCapacity, blockEntity.capability.getEmberCapacity() + emberValue * 10.0));
                                blockEntity.setChanged();
                            }
                        } else {
                            double angle = blockEntity.random.nextDouble() * 2.0 * Math.PI;
                            double x = (double) pos.m_123341_() + 0.5 + 0.5 * Math.sin(angle);
                            double z = (double) pos.m_123343_() + 0.5 + 0.5 * Math.cos(angle);
                            double x2 = (double) pos.m_123341_() + 0.5;
                            double z2 = (double) pos.m_123343_() + 0.5;
                            float layerHeight = 0.25F;
                            float numLayers = 2.0F + (float) Math.floor(blockEntity.capability.getEmberCapacity() / 120000.0);
                            float height = layerHeight * numLayers;
                            for (float i = 0.0F; i < 72.0F; i++) {
                                float coeff = i / 72.0F;
                                level.addParticle(GlowParticleOptions.EMBER_NOMOTION, x * (double) (1.0F - coeff) + x2 * (double) coeff, (double) ((float) pos.m_123342_() + (1.0F - coeff) + (height / 2.0F + 1.5F) * coeff), z * (double) (1.0F - coeff) + z2 * (double) coeff, 0.0, 0.0, 0.0);
                            }
                            level.playLocalSound(x, (double) pos.m_123342_() + 0.5, z, EmbersSounds.CRYSTAL_CELL_GROW.get(), SoundSource.BLOCKS, 1.0F, 1.0F + blockEntity.random.nextFloat(), false);
                        }
                    }
                }
            }
            float numLayers = 2.0F + (float) Math.floor(blockEntity.capability.getEmberCapacity() / 120000.0);
            if (level.isClientSide) {
                for (int i = 0; (float) i < numLayers / 2.0F; i++) {
                    float layerHeight = 0.25F;
                    float height = layerHeight * numLayers;
                    float xDest = (float) pos.m_123341_() + 0.5F;
                    float yDest = (float) pos.m_123342_() + height / 2.0F + 1.5F;
                    float zDest = (float) pos.m_123343_() + 0.5F;
                    float x = (float) pos.m_123341_() + 0.5F + 2.0F * (blockEntity.random.nextFloat() - 0.5F);
                    float z = (float) pos.m_123343_() + 0.5F + 2.0F * (blockEntity.random.nextFloat() - 0.5F);
                    float y = (float) pos.m_123342_() + 1.0F;
                    level.addParticle(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3((double) ((xDest - x) / 1.0F * blockEntity.random.nextFloat()), (double) ((yDest - y) / 1.0F * blockEntity.random.nextFloat()), (double) ((zDest - z) / 1.0F * blockEntity.random.nextFloat())), 2.0F), (double) x, (double) y, (double) z, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_ && (side == null || side == Direction.DOWN)) {
            if (cap == EmbersCapabilities.EMBER_CAPABILITY) {
                return this.capability.getCapability(cap, side);
            }
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
            }
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
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
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.CRYSTAL_CELL_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() - 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
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
        return id == 1;
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
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.ember")));
        }
    }
}