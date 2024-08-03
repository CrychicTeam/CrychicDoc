package com.rekindled.embers.blockentity;

import com.google.common.collect.Lists;
import com.rekindled.embers.ConfigManager;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.event.HeatCoilVisualEvent;
import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.recipe.SingleItemContainer;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.Misc;
import com.rekindled.embers.util.sound.ISoundController;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Vector3f;

public class HearthCoilBlockEntity extends BlockEntity implements ISoundController, IExtraDialInformation, IExtraCapabilityInformation {

    public static final Color DEFAULT_COLOR = new Color(255, 64, 16);

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            HearthCoilBlockEntity.this.setChanged();
        }
    };

    public ItemStackHandler inventory = new ItemStackHandler(1) {

        @Override
        public void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            HearthCoilBlockEntity.this.setChanged();
        }
    };

    public LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this.inventory);

    protected static Random random = new Random();

    protected int progress = 0;

    public double heat = 0.0;

    protected int ticksExisted = 0;

    public static final int SOUND_LOW_LOOP = 1;

    public static final int SOUND_MID_LOOP = 2;

    public static final int SOUND_HIGH_LOOP = 3;

    public static final int SOUND_PROCESS = 4;

    public static final int[] SOUND_IDS = new int[] { 1, 2, 3, 4 };

    HashSet<Integer> soundsPlaying = new HashSet();

    boolean isWorking;

    protected List<UpgradeContext> upgrades;

    public HashMap<RecipeType<?>, AbstractCookingRecipe> cachedRecipes = new HashMap();

    public HearthCoilBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.HEARTH_COIL_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(8000.0);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        if (nbt.contains("inventory")) {
            this.inventory.deserializeNBT(nbt.getCompound("inventory"));
        }
        if (nbt.contains("progress")) {
            this.progress = nbt.getInt("progress");
        }
        this.heat = nbt.getDouble("heat");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.put("inventory", this.inventory.serializeNBT());
        nbt.putInt("progress", this.progress);
        nbt.putDouble("heat", this.heat);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        nbt.putDouble("heat", this.heat);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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
        this.holder.invalidate();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }

    public static <T extends AbstractCookingRecipe> void serverTick(Level level, BlockPos pos, BlockState state, HearthCoilBlockEntity blockEntity) {
        blockEntity.ticksExisted++;
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { Direction.DOWN });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, ConfigManager.HEARTH_COIL_EMBER_COST.get(), blockEntity.upgrades);
            double prevHeat = blockEntity.heat;
            Boolean cancel = null;
            if (blockEntity.capability.getEmber() >= emberCost) {
                cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                if (!cancel) {
                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                    blockEntity.capability.removeAmount(emberCost, true);
                    if (blockEntity.ticksExisted % 20 == 0) {
                        blockEntity.heat = blockEntity.heat + UpgradeUtil.getOtherParameter(blockEntity, "heating_speed", ConfigManager.HEARTH_COIL_HEATING_SPEED.get().doubleValue(), blockEntity.upgrades);
                    }
                } else if (blockEntity.ticksExisted % 20 == 0) {
                    blockEntity.heat = blockEntity.heat - UpgradeUtil.getOtherParameter(blockEntity, "cooling_speed", ConfigManager.HEARTH_COIL_COOLING_SPEED.get().doubleValue(), blockEntity.upgrades);
                }
            } else if (blockEntity.ticksExisted % 20 == 0) {
                blockEntity.heat = blockEntity.heat - UpgradeUtil.getOtherParameter(blockEntity, "cooling_speed", ConfigManager.HEARTH_COIL_COOLING_SPEED.get().doubleValue(), blockEntity.upgrades);
            }
            double maxHeat = UpgradeUtil.getOtherParameter(blockEntity, "max_heat", ConfigManager.HEARTH_COIL_MAX_HEAT.get().doubleValue(), blockEntity.upgrades);
            blockEntity.heat = Mth.clamp(blockEntity.heat, 0.0, maxHeat);
            blockEntity.isWorking = false;
            if (blockEntity.heat != prevHeat) {
                blockEntity.setChanged();
            }
            int cookTime = UpgradeUtil.getWorkTime(blockEntity, (int) Math.ceil(Mth.clampedLerp((double) ConfigManager.HEARTH_COIL_MIN_COOK_TIME.get().intValue(), (double) ConfigManager.HEARTH_COIL_MAX_COOK_TIME.get().intValue(), 1.0 - blockEntity.heat / maxHeat)), blockEntity.upgrades);
            if (blockEntity.heat > 0.0 && blockEntity.ticksExisted % cookTime == 0) {
                if (cancel == null) {
                    cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                }
                if (!cancel) {
                    List<ItemEntity> items = level.m_45976_(ItemEntity.class, new AABB((double) (pos.m_123341_() - 1), (double) pos.m_123342_(), (double) (pos.m_123343_() - 1), (double) (pos.m_123341_() + 2), (double) (pos.m_123342_() + 2), (double) (pos.m_123343_() + 2)));
                    for (ItemEntity item : items) {
                        item.setUnlimitedLifetime();
                        item.lifespan = 10800;
                    }
                    if (items.size() > 0) {
                        int i = random.nextInt(items.size());
                        ItemEntity entityItem = (ItemEntity) items.get(i);
                        SingleItemContainer wrapper = new SingleItemContainer(entityItem.getItem());
                        RecipeType<?> type = UpgradeUtil.getOtherParameter(blockEntity, "recipe_type", RecipeType.SMELTING, blockEntity.upgrades);
                        if (blockEntity.cachedRecipes.containsKey(type)) {
                            blockEntity.cachedRecipes.put(type, (AbstractCookingRecipe) Misc.getRecipe((T) blockEntity.cachedRecipes.get(type), type, wrapper, level));
                        } else {
                            blockEntity.cachedRecipes.put(type, (AbstractCookingRecipe) Misc.getRecipe(null, type, wrapper, level));
                        }
                        if (blockEntity.cachedRecipes.get(type) != null) {
                            ArrayList<ItemStack> returns = Lists.newArrayList(new ItemStack[] { ((AbstractCookingRecipe) blockEntity.cachedRecipes.get(type)).assemble(wrapper, level.registryAccess()) });
                            UpgradeUtil.throwEvent(blockEntity, new MachineRecipeEvent.Success<>(blockEntity, (AbstractCookingRecipe) blockEntity.cachedRecipes.get(type)), blockEntity.upgrades);
                            UpgradeUtil.transformOutput(blockEntity, returns, blockEntity.upgrades);
                            depleteItem(entityItem, 1);
                            for (ItemStack stack : returns) {
                                ItemStack remainder = blockEntity.inventory.insertItem(0, stack, false);
                                if (!remainder.isEmpty()) {
                                    level.m_7967_(new ItemEntity(level, entityItem.m_20185_(), entityItem.m_20186_(), entityItem.m_20189_(), remainder));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, HearthCoilBlockEntity blockEntity) {
        blockEntity.handleSound();
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { Direction.DOWN });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            if (blockEntity.heat > 0.0) {
                int particleCount = (int) ((float) (1 + random.nextInt(2)) * (1.0F + (float) Math.sqrt(blockEntity.heat)));
                HeatCoilVisualEvent event = new HeatCoilVisualEvent(blockEntity, DEFAULT_COLOR, particleCount, 0.0F);
                UpgradeUtil.throwEvent(blockEntity, event, blockEntity.upgrades);
                Color color = event.getColor();
                GlowParticleOptions options = new GlowParticleOptions(new Vector3f((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F), 2.0F);
                for (int i = 0; i < event.getParticles(); i++) {
                    level.addParticle(options, (double) ((float) pos.m_123341_() - 0.2F + random.nextFloat() * 1.4F), (double) ((float) pos.m_123342_() + 1.275F), (double) ((float) pos.m_123343_() - 0.2F + random.nextFloat() * 1.4F), (Math.random() * 2.0 - 1.0) * 0.2, (double) (random.nextFloat() * event.getVerticalSpeed()), (Math.random() * 2.0 - 1.0) * 0.2);
                }
            }
        }
    }

    public static void depleteItem(ItemEntity entityItem, int inputCount) {
        ItemStack stack = entityItem.getItem();
        stack.shrink(inputCount);
        entityItem.setItem(stack);
        ((ServerLevel) entityItem.m_9236_()).sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 5.0F), entityItem.m_20185_(), entityItem.m_20186_(), entityItem.m_20189_(), 2, 0.07, 0.07, 0.07, 1.0);
        ((ServerLevel) entityItem.m_9236_()).sendParticles(new SmokeParticleOptions(SmokeParticleOptions.SMOKE_COLOR, 2.0F), entityItem.m_20185_(), entityItem.m_20186_(), entityItem.m_20189_(), 3, 0.07, 0.07, 0.07, 1.0);
        if (stack.isEmpty()) {
            entityItem.m_146870_();
        }
    }

    @Override
    public void playSound(int id) {
        float soundX = (float) this.f_58858_.m_123341_() + 0.5F;
        float soundY = (float) this.f_58858_.m_123342_() - 0.5F;
        float soundZ = (float) this.f_58858_.m_123343_() + 0.5F;
        switch(id) {
            case 1:
                EmbersSounds.playMachineSound(this, 1, EmbersSounds.HEATCOIL_LOW.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 2:
                EmbersSounds.playMachineSound(this, 2, EmbersSounds.HEATCOIL_MID.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 3:
                EmbersSounds.playMachineSound(this, 3, EmbersSounds.HEATCOIL_HIGH.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
                break;
            case 4:
                EmbersSounds.playMachineSound(this, 4, EmbersSounds.HEATCOIL_COOK.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, soundX, soundY, soundZ);
        }
        this.soundsPlaying.add(id);
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
        double heatRatio = this.heat / ConfigManager.HEARTH_COIL_MAX_HEAT.get();
        float highVolume = (float) Mth.clampedLerp(0.0, 1.0, (heatRatio - 0.75) * 4.0);
        float midVolume = (float) Mth.clampedLerp(0.0, 1.0, (heatRatio - 0.25) * 4.0) - highVolume;
        float lowVolume = (float) Mth.clampedLerp(0.0, 1.0, heatRatio * 10.0) - midVolume;
        switch(id) {
            case 1:
                return lowVolume > 0.0F;
            case 2:
                return midVolume > 0.0F;
            case 3:
                return highVolume > 0.0F;
            default:
                return false;
        }
    }

    @Override
    public float getCurrentVolume(int id, float volume) {
        double heatRatio = this.heat / ConfigManager.HEARTH_COIL_MAX_HEAT.get();
        float highVolume = (float) Mth.clampedLerp(0.0, 1.0, (heatRatio - 0.75) * 4.0);
        float midVolume = (float) Mth.clampedLerp(0.0, 1.0, (heatRatio - 0.25) * 4.0) - highVolume;
        float lowVolume = (float) Mth.clampedLerp(0.0, 1.0, heatRatio * 10.0) - midVolume;
        switch(id) {
            case 1:
                return lowVolume;
            case 2:
                return midVolume;
            case 3:
                return highVolume;
            default:
                return 0.0F;
        }
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        if ("ember".equals(dialType)) {
            DecimalFormat heatFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.heat");
            double maxHeat = UpgradeUtil.getOtherParameter(this, "max_heat", ConfigManager.HEARTH_COIL_MAX_HEAT.get().doubleValue(), this.upgrades);
            double heat = Mth.clamp(this.heat, 0.0, maxHeat);
            information.add(Component.translatable("embers.tooltip.dial.heat", heatFormat.format(heat), heatFormat.format(maxHeat)));
        }
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    @Override
    public boolean hasCapabilityDescription(Capability<?> capability) {
        return capability == ForgeCapabilities.ITEM_HANDLER;
    }

    @Override
    public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
        if (capability == ForgeCapabilities.ITEM_HANDLER) {
            strings.add(IExtraCapabilityInformation.formatCapability(IExtraCapabilityInformation.EnumIOType.OUTPUT, "embers.tooltip.goggles.item", null));
        }
    }
}