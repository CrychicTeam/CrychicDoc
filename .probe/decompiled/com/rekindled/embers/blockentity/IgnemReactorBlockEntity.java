package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.recipe.IEmberActivationRecipe;
import com.rekindled.embers.recipe.SingleItemContainer;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
import java.text.DecimalFormat;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.joml.Vector3f;

public class IgnemReactorBlockEntity extends BlockEntity implements ISoundController, IExtraDialInformation, IExtraCapabilityInformation {

    public static final double BASE_MULTIPLIER = 1.0;

    public static final int PROCESS_TIME = 20;

    static Random random = new Random();

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            IgnemReactorBlockEntity.this.setChanged();
        }

        @Override
        public boolean acceptsVolatile() {
            return true;
        }
    };

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        protected void onContentsChanged(int slot) {
            IgnemReactorBlockEntity.this.setChanged();
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return Misc.getRecipe(IgnemReactorBlockEntity.this.cachedRecipe, RegistryManager.EMBER_ACTIVATION.get(), new SingleItemContainer(stack), IgnemReactorBlockEntity.this.f_58857_) != null ? super.insertItem(slot, stack, simulate) : stack;
        }
    };

    int progress = -1;

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    protected List<UpgradeContext> upgrades = new ArrayList();

    public IEmberActivationRecipe cachedRecipe = null;

    public double catalyzerMult;

    public double combustorMult;

    public static final int SOUND_HAS_EMBER = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    public IgnemReactorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.IGNEM_REACTOR_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(128000.0);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains("inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
        this.capability.deserializeNBT(nbt);
        if (nbt.contains("progress")) {
            this.progress = nbt.getInt("progress");
        }
        this.catalyzerMult = nbt.getDouble("catalyzer");
        this.combustorMult = nbt.getDouble("combustor");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
        this.capability.writeToNBT(nbt);
        nbt.putInt("progress", this.progress);
        nbt.putDouble("catalyzer", this.catalyzerMult);
        nbt.putDouble("combustor", this.combustorMult);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.capability.writeToNBT(nbt);
        nbt.putDouble("catalyzer", this.catalyzerMult);
        nbt.putDouble("combustor", this.combustorMult);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, IgnemReactorBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        blockEntity.handleSound();
        if (blockEntity.capability.getEmber() > 0.0) {
            double catalyzerRatio = 0.0;
            if (blockEntity.catalyzerMult > 0.0 || blockEntity.combustorMult > 0.0) {
                catalyzerRatio = blockEntity.catalyzerMult / (blockEntity.catalyzerMult + blockEntity.combustorMult);
            }
            int r = (int) Mth.clampedLerp(255.0, 255.0, catalyzerRatio);
            int g = (int) Mth.clampedLerp(64.0, 64.0, catalyzerRatio);
            int b = (int) Mth.clampedLerp(16.0, 64.0, catalyzerRatio);
            float size = (float) Mth.clampedLerp(4.0, 2.0, catalyzerRatio);
            GlowParticleOptions options = new GlowParticleOptions(new Vector3f((float) r / 255.0F, (float) g / 255.0F, (float) b / 255.0F), size);
            for (int i = 0; (double) i < Math.ceil(blockEntity.capability.getEmber() / 500.0); i++) {
                float vx = (float) Mth.clampedLerp(0.0, ((double) random.nextFloat() - 0.5) * 0.1F, catalyzerRatio);
                float vy = (float) Mth.clampedLerp((double) (random.nextFloat() * 0.05F), ((double) random.nextFloat() - 0.5) * 0.2F, catalyzerRatio);
                float vz = (float) Mth.clampedLerp(0.0, ((double) random.nextFloat() - 0.5) * 0.1F, catalyzerRatio);
                level.addParticle(options, (double) ((float) pos.m_123341_() + 0.25F + random.nextFloat() * 0.5F), (double) ((float) pos.m_123342_() + 0.25F + random.nextFloat() * 0.5F), (double) ((float) pos.m_123343_() + 0.25F + random.nextFloat() * 0.5F), (double) vx, (double) vy, (double) vz);
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, IgnemReactorBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, Misc.horizontals);
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
            if (!cancel && !blockEntity.inventory.getStackInSlot(0).isEmpty()) {
                blockEntity.progress++;
                if (blockEntity.progress > UpgradeUtil.getWorkTime(blockEntity, 20, blockEntity.upgrades)) {
                    blockEntity.catalyzerMult = 0.0;
                    blockEntity.combustorMult = 0.0;
                    double multiplier = 1.0;
                    for (Direction facing : Misc.horizontals) {
                        BlockEntity tile = level.getBlockEntity(pos.relative(facing).below());
                        if (tile instanceof CatalysisChamberBlockEntity) {
                            blockEntity.catalyzerMult = blockEntity.catalyzerMult + ((CatalysisChamberBlockEntity) tile).multiplier;
                        }
                        if (tile instanceof CombustionChamberBlockEntity) {
                            blockEntity.combustorMult = blockEntity.combustorMult + ((CombustionChamberBlockEntity) tile).multiplier;
                        }
                    }
                    if (Math.max(blockEntity.combustorMult, blockEntity.catalyzerMult) < 2.0 * Math.min(blockEntity.combustorMult, blockEntity.catalyzerMult)) {
                        multiplier += blockEntity.combustorMult;
                        multiplier += blockEntity.catalyzerMult;
                        blockEntity.progress = 0;
                        if (blockEntity.inventory != null) {
                            RecipeWrapper wrapper = new RecipeWrapper(blockEntity.inventory);
                            blockEntity.cachedRecipe = Misc.getRecipe(blockEntity.cachedRecipe, RegistryManager.EMBER_ACTIVATION.get(), wrapper, level);
                            if (blockEntity.cachedRecipe != null) {
                                double emberValue = (double) blockEntity.cachedRecipe.getOutput(wrapper);
                                double ember = UpgradeUtil.getTotalEmberProduction(blockEntity, multiplier * emberValue, blockEntity.upgrades);
                                if (ember > 0.0 && blockEntity.capability.getEmber() + ember <= blockEntity.capability.getEmberCapacity()) {
                                    level.playSound(null, pos, EmbersSounds.IGNEM_REACTOR.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                                    if (level instanceof ServerLevel serverLevel) {
                                        serverLevel.sendParticles(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3(0.0, 0.65F, 0.0), 4.7F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), 80, 0.1, 0.1, 0.1, 1.0);
                                        serverLevel.sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 5.0F), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 20, 0.1, 0.1, 0.1, 1.0);
                                    }
                                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.PRODUCE, ember), blockEntity.upgrades);
                                    blockEntity.capability.addAmount(ember, true);
                                    blockEntity.cachedRecipe.process(wrapper);
                                }
                            }
                        }
                    }
                }
                blockEntity.setChanged();
            }
        }
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (!this.f_58859_) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
            }
            if (cap == EmbersCapabilities.EMBER_CAPABILITY) {
                return this.capability.getCapability(cap, side);
            }
        }
        return super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.holder.invalidate();
        this.capability.invalidate();
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
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.GENERATOR_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
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
        return id == 1 && this.capability.getEmber() > 0.0;
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        return (float) ((this.capability.getEmber() + 5000.0) / (this.capability.getEmberCapacity() + 5000.0));
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER || capability == EmbersCapabilities.EMBER_CAPABILITY;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.INPUT, "embers.tooltip.goggles.item", Component.translatable("embers.tooltip.goggles.item.ember")));
        }
        if (capability == EmbersCapabilities.EMBER_CAPABILITY) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.ember", null));
        }
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        if ("ember".equals(dialType) && Math.max(this.combustorMult, this.catalyzerMult) < 2.0 * Math.min(this.combustorMult, this.catalyzerMult)) {
            DecimalFormat multiplierFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.ember_multiplier");
            double multiplier = 1.0 + this.combustorMult + this.catalyzerMult;
            information.add(Component.translatable("embers.tooltip.dial.ember_multiplier", multiplierFormat.format(multiplier)));
        }
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }
}