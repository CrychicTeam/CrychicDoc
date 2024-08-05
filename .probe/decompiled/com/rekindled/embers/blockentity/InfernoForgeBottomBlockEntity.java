package com.rekindled.embers.blockentity;

import com.google.common.collect.Lists;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.augment.AugmentUtil;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.event.DialInformationEvent;
import com.rekindled.embers.api.event.EmberEvent;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.api.tile.IExtraDialInformation;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.api.upgrades.UpgradeUtil;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.GlowParticleOptions;
import com.rekindled.embers.particle.SmokeParticleOptions;
import com.rekindled.embers.particle.SparkParticleOptions;
import com.rekindled.embers.power.DefaultEmberCapability;
import com.rekindled.embers.recipe.IEmberActivationRecipe;
import com.rekindled.embers.recipe.SingleItemContainer;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class InfernoForgeBottomBlockEntity extends BlockEntity implements IExtraDialInformation, ISoundController {

    public static double EMBER_COST = 16.0;

    public static int MAX_LEVEL = 5;

    public static double MAX_CRYSTAL_VALUE = 115200.0;

    public static double CHANCE_MIDPOINT = 14400.0;

    public static int PROCESS_TIME = 200;

    public IEmberCapability capability = new DefaultEmberCapability() {

        @Override
        public void onContentsChanged() {
            super.onContentsChanged();
            InfernoForgeBottomBlockEntity.this.setChanged();
        }
    };

    static Random random = new Random();

    public int progress = 0;

    public double emberValue = 0.0;

    public IEmberActivationRecipe cachedEmberRecipe = null;

    public static final int SOUND_PROCESS = 1;

    public static final int[] SOUND_IDS = new int[] { 1 };

    HashSet<Integer> soundsPlaying = new HashSet();

    protected List<UpgradeContext> upgrades = new ArrayList();

    public InfernoForgeBottomBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(RegistryManager.INFERNO_FORGE_BOTTOM_ENTITY.get(), pPos, pBlockState);
        this.capability.setEmberCapacity(32000.0);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.capability.deserializeNBT(nbt);
        this.progress = nbt.getInt("progress");
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        this.capability.writeToNBT(nbt);
        nbt.putInt("progress", this.progress);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbt = super.getUpdateTag();
        this.capability.writeToNBT(nbt);
        nbt.putInt("progress", this.progress);
        return nbt;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, InfernoForgeBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { Direction.DOWN });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        blockEntity.handleSound();
        if (blockEntity.progress > 0) {
            if (random.nextInt(10) == 0) {
                if (random.nextInt(3) == 0) {
                    level.addParticle(SparkParticleOptions.EMBER, (double) ((float) pos.m_123341_() - 0.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) ((float) pos.m_123342_() + 1.75F), (double) ((float) pos.m_123343_() - 0.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * random.nextFloat()), (double) (0.125F * (random.nextFloat() - 0.5F)));
                }
                if (random.nextInt(3) == 0) {
                    level.addParticle(SparkParticleOptions.EMBER, (double) ((float) pos.m_123341_() + 1.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) ((float) pos.m_123342_() + 1.75F), (double) ((float) pos.m_123343_() - 0.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * random.nextFloat()), (double) (0.125F * (random.nextFloat() - 0.5F)));
                }
                if (random.nextInt(3) == 0) {
                    level.addParticle(SparkParticleOptions.EMBER, (double) ((float) pos.m_123341_() + 1.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) ((float) pos.m_123342_() + 1.75F), (double) ((float) pos.m_123343_() + 1.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * random.nextFloat()), (double) (0.125F * (random.nextFloat() - 0.5F)));
                }
                if (random.nextInt(3) == 0) {
                    level.addParticle(SparkParticleOptions.EMBER, (double) ((float) pos.m_123341_() - 0.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) ((float) pos.m_123342_() + 1.75F), (double) ((float) pos.m_123343_() + 1.5F + 0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * (random.nextFloat() - 0.5F)), (double) (0.125F * random.nextFloat()), (double) (0.125F * (random.nextFloat() - 0.5F)));
                }
            }
            level.addParticle(SmokeParticleOptions.BIG_SMOKE, (double) ((float) pos.m_123341_() - 0.3F), (double) ((float) pos.m_123342_() + 1.85F), (double) ((float) pos.m_123343_() - 0.3F), (double) (0.025F * (random.nextFloat() - 0.5F)), (double) (0.05F * (random.nextFloat() + 1.0F)), (double) (0.025F * (random.nextFloat() - 0.5F)));
            level.addParticle(SmokeParticleOptions.BIG_SMOKE, (double) ((float) pos.m_123341_() + 1.3F), (double) ((float) pos.m_123342_() + 1.85F), (double) ((float) pos.m_123343_() - 0.3F), (double) (0.025F * (random.nextFloat() - 0.5F)), (double) (0.05F * (random.nextFloat() + 1.0F)), (double) (0.025F * (random.nextFloat() - 0.5F)));
            level.addParticle(SmokeParticleOptions.BIG_SMOKE, (double) ((float) pos.m_123341_() + 1.3F), (double) ((float) pos.m_123342_() + 1.85F), (double) ((float) pos.m_123343_() + 1.3F), (double) (0.025F * (random.nextFloat() - 0.5F)), (double) (0.05F * (random.nextFloat() + 1.0F)), (double) (0.025F * (random.nextFloat() - 0.5F)));
            level.addParticle(SmokeParticleOptions.BIG_SMOKE, (double) ((float) pos.m_123341_() - 0.3F), (double) ((float) pos.m_123342_() + 1.85F), (double) ((float) pos.m_123343_() + 1.3F), (double) (0.025F * (random.nextFloat() - 0.5F)), (double) (0.05F * (random.nextFloat() + 1.0F)), (double) (0.025F * (random.nextFloat() - 0.5F)));
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, InfernoForgeBottomBlockEntity blockEntity) {
        blockEntity.upgrades = UpgradeUtil.getUpgrades(level, pos, new Direction[] { Direction.DOWN });
        UpgradeUtil.verifyUpgrades(blockEntity, blockEntity.upgrades);
        if (!UpgradeUtil.doTick(blockEntity, blockEntity.upgrades)) {
            if (level.getBlockEntity(pos.above()) instanceof InfernoForgeTopBlockEntity hatch && !hatch.open) {
                long openTicks = level.getGameTime() - hatch.lastToggle;
                if (openTicks > 7L) {
                    blockEntity.updateProgress();
                }
            }
            if (blockEntity.progress <= 0) {
                if (level.getGameTime() % 20L == 1L) {
                    for (ItemEntity e : level.m_45976_(ItemEntity.class, new AABB((double) pos.m_123341_(), (double) pos.m_123342_() + 0.25, (double) pos.m_123343_(), (double) pos.m_123341_() + 1.0, (double) pos.m_123342_() + 1.5, (double) pos.m_123343_() + 1.0))) {
                        e.setExtendedLifetime();
                    }
                }
            } else {
                boolean cancel = UpgradeUtil.doWork(blockEntity, blockEntity.upgrades);
                double emberCost = UpgradeUtil.getTotalEmberConsumption(blockEntity, EMBER_COST, blockEntity.upgrades);
                if (!cancel && !(blockEntity.capability.getEmber() < emberCost)) {
                    UpgradeUtil.throwEvent(blockEntity, new EmberEvent(blockEntity, EmberEvent.EnumType.CONSUME, emberCost), blockEntity.upgrades);
                    blockEntity.progress--;
                    blockEntity.capability.removeAmount(emberCost, true);
                    List<ItemEntity> items = blockEntity.getValidItems();
                    for (ItemEntity e : items) {
                        e.setExtendedLifetime();
                        e.setPickUpDelay(20);
                    }
                    if (blockEntity.progress == 0) {
                        if (items.isEmpty()) {
                            blockEntity.progress = 0;
                            blockEntity.setChanged();
                        } else {
                            boolean forgeSuccess = false;
                            if (level.getBlockEntity(pos.above()) instanceof InfernoForgeTopBlockEntity hatchx) {
                                hatchx.open = true;
                                hatchx.lastToggle = level.getGameTime();
                                hatchx.setChanged();
                                level.playSound(null, pos, EmbersSounds.INFERNO_FORGE_OPEN.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                            }
                            if (blockEntity.emberValue > 0.0) {
                                for (ItemEntity item : items) {
                                    if (!AugmentUtil.hasHeat(item.getItem())) {
                                        if (item.getItem().is(ItemTags.MUSIC_DISCS)) {
                                            item.setItem(new ItemStack(RegistryManager.MUSIC_DISC_7F_PATTERNS.get()));
                                            forgeSuccess = true;
                                        } else {
                                            item.m_146870_();
                                        }
                                    } else if (Misc.random.nextDouble() < UpgradeUtil.getOtherParameter(blockEntity, "reforge_chance", Math.atan(blockEntity.emberValue / (CHANCE_MIDPOINT + (double) (14400 * AugmentUtil.getLevel(item.getItem())))) / (Math.PI / 2), blockEntity.upgrades)) {
                                        ItemStack stack = item.getItem();
                                        AugmentUtil.setHeat(stack, 0.0F);
                                        AugmentUtil.setLevel(stack, AugmentUtil.getLevel(stack) + 1);
                                        item.setItem(stack);
                                        blockEntity.progress = 0;
                                        forgeSuccess = true;
                                    }
                                }
                            }
                            level.playSound(null, pos.above(), forgeSuccess ? EmbersSounds.INFERNO_FORGE_SUCCESS.get() : EmbersSounds.INFERNO_FORGE_FAIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                            if (level instanceof ServerLevel serverLevel) {
                                if (forgeSuccess) {
                                    serverLevel.sendParticles(new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, new Vec3(0.0, 0.65F, 0.0), 4.7F), (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 1.5F), (double) ((float) pos.m_123343_() + 0.5F), 80, 0.1, 0.1, 0.1, 1.0);
                                }
                                serverLevel.sendParticles(SmokeParticleOptions.BIG_SMOKE, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 1.5, (double) pos.m_123343_() + 0.5, 20, 0.1, 0.1, 0.1, 1.0);
                            }
                            blockEntity.setChanged();
                        }
                    }
                } else {
                    blockEntity.progress = 0;
                    blockEntity.setChanged();
                }
            }
        }
    }

    public void updateProgress() {
        if (this.progress == 0) {
            List<ItemEntity> items = this.getValidItems();
            if (!items.isEmpty()) {
                this.progress = PROCESS_TIME;
                this.f_58857_.playSound(null, this.f_58858_, EmbersSounds.INFERNO_FORGE_START.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                this.setChanged();
            }
        }
    }

    private List<ItemEntity> getValidItems() {
        List<ItemEntity> items = this.f_58857_.m_45976_(ItemEntity.class, new AABB((double) this.f_58858_.m_123341_(), (double) this.f_58858_.m_123342_() + 0.25, (double) this.f_58858_.m_123343_(), (double) this.f_58858_.m_123341_() + 1.0, (double) this.f_58858_.m_123342_() + 1.5, (double) this.f_58858_.m_123343_() + 1.0));
        ItemStack pickedItem = ItemStack.EMPTY;
        this.emberValue = 0.0;
        for (ItemEntity item : items) {
            ItemStack stack = item.getItem();
            if (AugmentUtil.hasHeat(stack) || stack.is(ItemTags.MUSIC_DISCS)) {
                if (!pickedItem.isEmpty() || (AugmentUtil.getLevel(stack) >= MAX_LEVEL || !(AugmentUtil.getHeat(stack) >= AugmentUtil.getMaxHeat(stack))) && !stack.is(ItemTags.MUSIC_DISCS)) {
                    return Lists.newArrayList();
                }
                pickedItem = stack;
            } else {
                Container context = new SingleItemContainer(stack);
                this.cachedEmberRecipe = Misc.getRecipe(this.cachedEmberRecipe, RegistryManager.EMBER_ACTIVATION.get(), context, this.f_58857_);
                if (this.cachedEmberRecipe == null) {
                    return Lists.newArrayList();
                }
                this.emberValue = this.emberValue + (double) (this.cachedEmberRecipe.getOutput(context) * stack.getCount());
            }
        }
        return (List<ItemEntity>) (!pickedItem.isEmpty() && this.emberValue > 0.0 ? items : Lists.newArrayList());
    }

    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return !this.f_58859_ && cap == EmbersCapabilities.EMBER_CAPABILITY ? this.capability.getCapability(cap, side) : super.getCapability(cap, side);
    }

    public void invalidateCaps() {
        super.invalidateCaps();
        this.capability.invalidate();
    }

    @Override
    public void playSound(int id) {
        if (id == 1) {
            EmbersSounds.playMachineSound(this, 1, EmbersSounds.INFERNO_FORGE_LOOP.get(), SoundSource.BLOCKS, true, 1.0F, 1.0F, (float) this.f_58858_.m_123341_() + 0.5F, (float) this.f_58858_.m_123342_() + 0.5F, (float) this.f_58858_.m_123343_() + 0.5F);
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
        return id == 1 && this.progress > 0;
    }

    @Override
    public void addDialInformation(Direction facing, List<Component> information, String dialType) {
        UpgradeUtil.throwEvent(this, new DialInformationEvent(this, information, dialType), this.upgrades);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.f_58857_ instanceof ServerLevel) {
            ((ServerLevel) this.f_58857_).getChunkSource().blockChanged(this.f_58858_);
        }
    }
}