package com.simibubi.create.content.processing.burner;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeHooks;

public class BlazeBurnerBlockEntity extends SmartBlockEntity {

    public static final int MAX_HEAT_CAPACITY = 10000;

    public static final int INSERTION_THRESHOLD = 500;

    protected BlazeBurnerBlockEntity.FuelType activeFuel = BlazeBurnerBlockEntity.FuelType.NONE;

    protected int remainingBurnTime = 0;

    protected LerpedFloat headAnimation = LerpedFloat.linear();

    protected LerpedFloat headAngle = LerpedFloat.angular();

    protected boolean isCreative = false;

    protected boolean goggles = false;

    protected boolean hat;

    public BlazeBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.headAngle.startWithValue((double) ((AngleHelper.horizontalAngle((Direction) state.m_61145_(BlazeBurnerBlock.f_54117_).orElse(Direction.SOUTH)) + 180.0F) % 360.0F));
    }

    public BlazeBurnerBlockEntity.FuelType getActiveFuel() {
        return this.activeFuel;
    }

    public int getRemainingBurnTime() {
        return this.remainingBurnTime;
    }

    public boolean isCreative() {
        return this.isCreative;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_58857_.isClientSide) {
            this.tickAnimation();
            if (!this.isVirtual()) {
                this.spawnParticles(this.getHeatLevelFromBlock(), 1.0);
            }
        } else if (!this.isCreative) {
            if (this.remainingBurnTime > 0) {
                this.remainingBurnTime--;
            }
            if (this.activeFuel == BlazeBurnerBlockEntity.FuelType.NORMAL) {
                this.updateBlockState();
            }
            if (this.remainingBurnTime <= 0) {
                if (this.activeFuel == BlazeBurnerBlockEntity.FuelType.SPECIAL) {
                    this.activeFuel = BlazeBurnerBlockEntity.FuelType.NORMAL;
                    this.remainingBurnTime = 5000;
                } else {
                    this.activeFuel = BlazeBurnerBlockEntity.FuelType.NONE;
                }
                this.updateBlockState();
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void tickAnimation() {
        boolean active = this.getHeatLevelFromBlock().isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) && this.isValidBlockAbove();
        if (!active) {
            float target = 0.0F;
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && !player.m_20145_()) {
                double x;
                double z;
                if (this.isVirtual()) {
                    x = -4.0;
                    z = -10.0;
                } else {
                    x = player.m_20185_();
                    z = player.m_20189_();
                }
                double dx = x - ((double) this.m_58899_().m_123341_() + 0.5);
                double dz = z - ((double) this.m_58899_().m_123343_() + 0.5);
                target = AngleHelper.deg(-Mth.atan2(dz, dx)) - 90.0F;
            }
            target = this.headAngle.getValue() + AngleHelper.getShortestAngleDiff((double) this.headAngle.getValue(), (double) target);
            this.headAngle.chase((double) target, 0.25, LerpedFloat.Chaser.exp(5.0));
            this.headAngle.tickChaser();
        } else {
            this.headAngle.chase((double) ((AngleHelper.horizontalAngle((Direction) this.m_58900_().m_61145_(BlazeBurnerBlock.f_54117_).orElse(Direction.SOUTH)) + 180.0F) % 360.0F), 0.125, LerpedFloat.Chaser.EXP);
            this.headAngle.tickChaser();
        }
        this.headAnimation.chase(active ? 1.0 : 0.0, 0.25, LerpedFloat.Chaser.exp(0.25));
        this.headAnimation.tickChaser();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        if (!this.isCreative) {
            compound.putInt("fuelLevel", this.activeFuel.ordinal());
            compound.putInt("burnTimeRemaining", this.remainingBurnTime);
        } else {
            compound.putBoolean("isCreative", true);
        }
        if (this.goggles) {
            compound.putBoolean("Goggles", true);
        }
        if (this.hat) {
            compound.putBoolean("TrainHat", true);
        }
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        this.activeFuel = BlazeBurnerBlockEntity.FuelType.values()[compound.getInt("fuelLevel")];
        this.remainingBurnTime = compound.getInt("burnTimeRemaining");
        this.isCreative = compound.getBoolean("isCreative");
        this.goggles = compound.contains("Goggles");
        this.hat = compound.contains("TrainHat");
        super.read(compound, clientPacket);
    }

    public BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock() {
        return BlazeBurnerBlock.getHeatLevelOf(this.m_58900_());
    }

    public void updateBlockState() {
        this.setBlockHeat(this.getHeatLevel());
    }

    protected void setBlockHeat(BlazeBurnerBlock.HeatLevel heat) {
        BlazeBurnerBlock.HeatLevel inBlockState = this.getHeatLevelFromBlock();
        if (inBlockState != heat) {
            this.f_58857_.setBlockAndUpdate(this.f_58858_, (BlockState) this.m_58900_().m_61124_(BlazeBurnerBlock.HEAT_LEVEL, heat));
            this.notifyUpdate();
        }
    }

    protected boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, boolean simulate) {
        if (this.isCreative) {
            return false;
        } else {
            BlazeBurnerBlockEntity.FuelType newFuel = BlazeBurnerBlockEntity.FuelType.NONE;
            int newBurnTime;
            if (AllTags.AllItemTags.BLAZE_BURNER_FUEL_SPECIAL.matches(itemStack)) {
                newBurnTime = 3200;
                newFuel = BlazeBurnerBlockEntity.FuelType.SPECIAL;
            } else {
                newBurnTime = ForgeHooks.getBurnTime(itemStack, null);
                if (newBurnTime > 0) {
                    newFuel = BlazeBurnerBlockEntity.FuelType.NORMAL;
                } else if (AllTags.AllItemTags.BLAZE_BURNER_FUEL_REGULAR.matches(itemStack)) {
                    newBurnTime = 1600;
                    newFuel = BlazeBurnerBlockEntity.FuelType.NORMAL;
                }
            }
            if (newFuel == BlazeBurnerBlockEntity.FuelType.NONE) {
                return false;
            } else if (newFuel.ordinal() < this.activeFuel.ordinal()) {
                return false;
            } else {
                if (newFuel == this.activeFuel) {
                    if (this.remainingBurnTime <= 500) {
                        newBurnTime += this.remainingBurnTime;
                    } else {
                        if (!forceOverflow || newFuel != BlazeBurnerBlockEntity.FuelType.NORMAL) {
                            return false;
                        }
                        if (this.remainingBurnTime < 10000) {
                            newBurnTime = Math.min(this.remainingBurnTime + newBurnTime, 10000);
                        } else {
                            newBurnTime = this.remainingBurnTime;
                        }
                    }
                }
                if (simulate) {
                    return true;
                } else {
                    this.activeFuel = newFuel;
                    this.remainingBurnTime = newBurnTime;
                    if (this.f_58857_.isClientSide) {
                        this.spawnParticleBurst(this.activeFuel == BlazeBurnerBlockEntity.FuelType.SPECIAL);
                        return true;
                    } else {
                        BlazeBurnerBlock.HeatLevel prev = this.getHeatLevelFromBlock();
                        this.playSound();
                        this.updateBlockState();
                        if (prev != this.getHeatLevelFromBlock()) {
                            this.f_58857_.playSound(null, this.f_58858_, SoundEvents.BLAZE_AMBIENT, SoundSource.BLOCKS, 0.125F + this.f_58857_.random.nextFloat() * 0.125F, 1.15F - this.f_58857_.random.nextFloat() * 0.25F);
                        }
                        return true;
                    }
                }
            }
        }
    }

    protected void applyCreativeFuel() {
        this.activeFuel = BlazeBurnerBlockEntity.FuelType.NONE;
        this.remainingBurnTime = 0;
        this.isCreative = true;
        BlazeBurnerBlock.HeatLevel next = this.getHeatLevelFromBlock().nextActiveLevel();
        if (this.f_58857_.isClientSide) {
            this.spawnParticleBurst(next.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING));
        } else {
            this.playSound();
            if (next == BlazeBurnerBlock.HeatLevel.FADING) {
                next = next.nextActiveLevel();
            }
            this.setBlockHeat(next);
        }
    }

    public boolean isCreativeFuel(ItemStack stack) {
        return AllItems.CREATIVE_BLAZE_CAKE.isIn(stack);
    }

    public boolean isValidBlockAbove() {
        if (this.isVirtual()) {
            return false;
        } else {
            BlockState blockState = this.f_58857_.getBlockState(this.f_58858_.above());
            return AllBlocks.BASIN.has(blockState) || blockState.m_60734_() instanceof FluidTankBlock;
        }
    }

    protected void playSound() {
        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS, 0.125F + this.f_58857_.random.nextFloat() * 0.125F, 0.75F - this.f_58857_.random.nextFloat() * 0.25F);
    }

    protected BlazeBurnerBlock.HeatLevel getHeatLevel() {
        BlazeBurnerBlock.HeatLevel level = BlazeBurnerBlock.HeatLevel.SMOULDERING;
        switch(this.activeFuel) {
            case SPECIAL:
                level = BlazeBurnerBlock.HeatLevel.SEETHING;
                break;
            case NORMAL:
                boolean lowPercent = (double) this.remainingBurnTime / 10000.0 < 0.0125;
                level = lowPercent ? BlazeBurnerBlock.HeatLevel.FADING : BlazeBurnerBlock.HeatLevel.KINDLED;
            case NONE:
        }
        return level;
    }

    protected void spawnParticles(BlazeBurnerBlock.HeatLevel heatLevel, double burstMult) {
        if (this.f_58857_ != null) {
            if (heatLevel != BlazeBurnerBlock.HeatLevel.NONE) {
                RandomSource r = this.f_58857_.getRandom();
                Vec3 c = VecHelper.getCenterOf(this.f_58858_);
                Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, 0.125F).multiply(1.0, 0.0, 1.0));
                if (r.nextInt(4) == 0) {
                    boolean empty = this.f_58857_.getBlockState(this.f_58858_.above()).m_60812_(this.f_58857_, this.f_58858_.above()).isEmpty();
                    if (empty || r.nextInt(8) == 0) {
                        this.f_58857_.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y, v.z, 0.0, 0.0, 0.0);
                    }
                    double yMotion = empty ? 0.0625 : r.nextDouble() * 0.0125F;
                    Vec3 v2 = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, 0.5F).multiply(1.0, 0.25, 1.0).normalize().scale((empty ? 0.25 : 0.5) + r.nextDouble() * 0.125)).add(0.0, 0.5, 0.0);
                    if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING)) {
                        this.f_58857_.addParticle(ParticleTypes.SOUL_FIRE_FLAME, v2.x, v2.y, v2.z, 0.0, yMotion, 0.0);
                    } else if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
                        this.f_58857_.addParticle(ParticleTypes.FLAME, v2.x, v2.y, v2.z, 0.0, yMotion, 0.0);
                    }
                }
            }
        }
    }

    public void spawnParticleBurst(boolean soulFlame) {
        Vec3 c = VecHelper.getCenterOf(this.f_58858_);
        RandomSource r = this.f_58857_.random;
        for (int i = 0; i < 20; i++) {
            Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, r, 0.5F).multiply(1.0, 0.25, 1.0).normalize();
            Vec3 v = c.add(offset.scale(0.5 + r.nextDouble() * 0.125)).add(0.0, 0.125, 0.0);
            Vec3 m = offset.scale(0.03125);
            this.f_58857_.addParticle(soulFlame ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, v.x, v.y, v.z, m.x, m.y, m.z);
        }
    }

    public static enum FuelType {

        NONE, NORMAL, SPECIAL
    }
}