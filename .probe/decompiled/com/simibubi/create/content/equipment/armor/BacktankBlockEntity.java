package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.advancement.CreateAdvancement;
import com.simibubi.create.foundation.blockEntity.ComparatorUtil;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.particle.AirParticleData;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;

public class BacktankBlockEntity extends KineticBlockEntity implements Nameable {

    public int airLevel;

    public int airLevelTimer;

    private Component defaultName;

    private Component customName;

    private int capacityEnchantLevel;

    private ListTag enchantmentTag;

    public BacktankBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.defaultName = getDefaultName(state);
        this.enchantmentTag = new ListTag();
    }

    public static Component getDefaultName(BlockState state) {
        if (AllBlocks.NETHERITE_BACKTANK.has(state)) {
            ((BacktankItem) AllItems.NETHERITE_BACKTANK.get()).m_41466_();
        }
        return ((BacktankItem) AllItems.COPPER_BACKTANK.get()).m_41466_();
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        super.addBehaviours(behaviours);
        this.registerAwardables(behaviours, new CreateAdvancement[] { AllAdvancements.BACKTANK });
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        if (this.getSpeed() != 0.0F) {
            this.award(AllAdvancements.BACKTANK);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getSpeed() != 0.0F) {
            BlockState state = this.m_58900_();
            BooleanProperty waterProperty = BlockStateProperties.WATERLOGGED;
            if (!state.m_61138_(waterProperty) || !(Boolean) state.m_61143_(waterProperty)) {
                if (this.airLevelTimer > 0) {
                    this.airLevelTimer--;
                } else {
                    int max = BacktankUtil.maxAir(this.capacityEnchantLevel);
                    if (this.f_58857_.isClientSide) {
                        Vec3 centerOf = VecHelper.getCenterOf(this.f_58858_);
                        Vec3 v = VecHelper.offsetRandomly(centerOf, this.f_58857_.random, 0.65F);
                        Vec3 m = centerOf.subtract(v);
                        if (this.airLevel != max) {
                            this.f_58857_.addParticle(new AirParticleData(1.0F, 0.05F), v.x, v.y, v.z, m.x, m.y, m.z);
                        }
                    } else if (this.airLevel != max) {
                        int prevComparatorLevel = this.getComparatorOutput();
                        float abs = Math.abs(this.getSpeed());
                        int increment = Mth.clamp(((int) abs - 100) / 20, 1, 5);
                        this.airLevel = Math.min(max, this.airLevel + increment);
                        if (this.getComparatorOutput() != prevComparatorLevel && !this.f_58857_.isClientSide) {
                            this.f_58857_.updateNeighbourForOutputSignal(this.f_58858_, state.m_60734_());
                        }
                        if (this.airLevel == max) {
                            this.sendData();
                        }
                        this.airLevelTimer = Mth.clamp((int) (128.0F - abs / 5.0F) - 108, 0, 20);
                    }
                }
            }
        }
    }

    public int getComparatorOutput() {
        int max = BacktankUtil.maxAir(this.capacityEnchantLevel);
        return ComparatorUtil.fractionToRedstoneLevel((double) ((float) this.airLevel / (float) max));
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("Air", this.airLevel);
        compound.putInt("Timer", this.airLevelTimer);
        compound.putInt("CapacityEnchantment", this.capacityEnchantLevel);
        if (this.customName != null) {
            compound.putString("CustomName", Component.Serializer.toJson(this.customName));
        }
        compound.put("Enchantments", this.enchantmentTag);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        int prev = this.airLevel;
        this.capacityEnchantLevel = compound.getInt("CapacityEnchantment");
        this.airLevel = compound.getInt("Air");
        this.airLevelTimer = compound.getInt("Timer");
        this.enchantmentTag = compound.getList("Enchantments", 10);
        if (compound.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(compound.getString("CustomName"));
        }
        if (prev != 0 && prev != this.airLevel && this.airLevel == BacktankUtil.maxAir(this.capacityEnchantLevel) && clientPacket) {
            this.playFilledEffect();
        }
    }

    protected void playFilledEffect() {
        AllSoundEvents.CONFIRM.playAt(this.f_58857_, this.f_58858_, 0.4F, 1.0F, true);
        Vec3 baseMotion = new Vec3(0.25, 0.1, 0.0);
        Vec3 baseVec = VecHelper.getCenterOf(this.f_58858_);
        for (int i = 0; i < 360; i += 10) {
            Vec3 m = VecHelper.rotate(baseMotion, (double) i, Direction.Axis.Y);
            Vec3 v = baseVec.add(m.normalize().scale(0.25));
            this.f_58857_.addParticle(ParticleTypes.SPIT, v.x, v.y, v.z, m.x, m.y, m.z);
        }
    }

    @Override
    public Component getName() {
        return this.customName != null ? this.customName : this.defaultName;
    }

    public int getAirLevel() {
        return this.airLevel;
    }

    public void setAirLevel(int airLevel) {
        this.airLevel = airLevel;
        this.sendData();
    }

    public void setCustomName(Component customName) {
        this.customName = customName;
    }

    @Override
    public Component getCustomName() {
        return this.customName;
    }

    public ListTag getEnchantmentTag() {
        return this.enchantmentTag;
    }

    public void setEnchantmentTag(ListTag enchantmentTag) {
        this.enchantmentTag = enchantmentTag;
    }

    public void setCapacityEnchantLevel(int capacityEnchantLevel) {
        this.capacityEnchantLevel = capacityEnchantLevel;
    }
}