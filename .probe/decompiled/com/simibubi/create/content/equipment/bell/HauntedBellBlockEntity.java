package com.simibubi.create.content.equipment.bell;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class HauntedBellBlockEntity extends AbstractBellBlockEntity {

    public static final int DISTANCE = 10;

    public static final int RECHARGE_TICKS = 65;

    public static final int EFFECT_TICKS = 20;

    public int effectTicks = 0;

    public HauntedBellBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public PartialModel getBellModel() {
        return AllPartialModels.HAUNTED_BELL;
    }

    @Override
    public boolean ring(Level world, BlockPos pos, Direction direction) {
        if (this.isRinging && this.ringingTicks < 65) {
            return false;
        } else {
            HauntedBellPulser.sendPulse(world, pos, 10, false);
            this.effectTicks = 20;
            return super.ring(world, pos, direction);
        }
    }

    @Override
    protected void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.putInt("EffectTicks", this.effectTicks);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        this.effectTicks = compound.getInt("EffectTicks");
    }

    @Override
    public void tick() {
        super.tick();
        if (this.effectTicks > 0) {
            this.effectTicks--;
            if (this.f_58857_.isClientSide) {
                RandomSource rand = this.f_58857_.getRandom();
                if (!(rand.nextFloat() > 0.25F)) {
                    this.spawnParticle(rand);
                    this.playSound(rand);
                }
            }
        }
    }

    protected void spawnParticle(RandomSource rand) {
        double x = (double) this.f_58858_.m_123341_() + rand.nextDouble();
        double y = (double) this.f_58858_.m_123342_() + 0.5;
        double z = (double) this.f_58858_.m_123343_() + rand.nextDouble();
        double vx = rand.nextDouble() * 0.04 - 0.02;
        double vy = 0.1;
        double vz = rand.nextDouble() * 0.04 - 0.02;
        this.f_58857_.addParticle(ParticleTypes.SOUL, x, y, z, vx, vy, vz);
    }

    protected void playSound(RandomSource rand) {
        float vol = rand.nextFloat() * 0.4F + rand.nextFloat() > 0.9F ? 0.6F : 0.0F;
        float pitch = 0.6F + rand.nextFloat() * 0.4F;
        this.f_58857_.playSound(null, this.f_58858_, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, vol, pitch);
    }
}