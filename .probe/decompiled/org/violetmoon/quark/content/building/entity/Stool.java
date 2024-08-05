package org.violetmoon.quark.content.building.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.building.block.StoolBlock;

public class Stool extends Entity {

    public Stool(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        super.tick();
        List<Entity> passengers = this.m_20197_();
        boolean dead = passengers.isEmpty();
        BlockPos pos = this.m_20183_();
        BlockState state = this.m_9236_().getBlockState(pos);
        if (!dead && !(state.m_60734_() instanceof StoolBlock)) {
            PistonMovingBlockEntity piston;
            boolean didOffset;
            label51: {
                piston = null;
                didOffset = false;
                if (this.m_9236_().getBlockEntity(pos) instanceof PistonMovingBlockEntity pistonBE && pistonBE.getMovedState().m_60734_() instanceof StoolBlock) {
                    piston = pistonBE;
                    break label51;
                }
                for (Direction d : Direction.values()) {
                    BlockPos offPos = pos.relative(d);
                    if (this.m_9236_().getBlockEntity(offPos) instanceof PistonMovingBlockEntity pistonBE && pistonBE.getMovedState().m_60734_() instanceof StoolBlock) {
                        piston = pistonBE;
                        break;
                    }
                }
            }
            if (piston != null) {
                Direction dir = piston.getMovementDirection();
                this.m_6478_(MoverType.PISTON, new Vec3((double) ((float) dir.getStepX()) * 0.33, (double) ((float) dir.getStepY()) * 0.33, (double) ((float) dir.getStepZ()) * 0.33));
                didOffset = true;
            }
            dead = !didOffset;
        }
        if (dead && !this.m_9236_().isClientSide) {
            this.m_6089_();
            if (state.m_60734_() instanceof StoolBlock) {
                this.m_9236_().setBlockAndUpdate(pos, (BlockState) state.m_61124_(StoolBlock.SAT_IN, false));
            }
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.3;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(@NotNull CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(@NotNull CompoundTag compound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}