package com.simibubi.create.content.decoration.placard;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class PlacardBlockEntity extends SmartBlockEntity {

    ItemStack heldItem = ItemStack.EMPTY;

    int poweredTicks = 0;

    public PlacardBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_58857_.isClientSide) {
            if (this.poweredTicks != 0) {
                this.poweredTicks--;
                if (this.poweredTicks <= 0) {
                    BlockState blockState = this.m_58900_();
                    this.f_58857_.setBlock(this.f_58858_, (BlockState) blockState.m_61124_(PlacardBlock.POWERED, false), 3);
                    PlacardBlock.updateNeighbours(blockState, this.f_58857_, this.f_58858_);
                }
            }
        }
    }

    public ItemStack getHeldItem() {
        return this.heldItem;
    }

    public void setHeldItem(ItemStack heldItem) {
        this.heldItem = heldItem;
        this.notifyUpdate();
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        tag.putInt("PoweredTicks", this.poweredTicks);
        tag.put("Item", this.heldItem.serializeNBT());
        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        int prevTicks = this.poweredTicks;
        this.poweredTicks = tag.getInt("PoweredTicks");
        this.heldItem = ItemStack.of(tag.getCompound("Item"));
        super.read(tag, clientPacket);
        if (clientPacket && prevTicks < this.poweredTicks) {
            this.spawnParticles();
        }
    }

    private void spawnParticles() {
        BlockState blockState = this.m_58900_();
        if (AllBlocks.PLACARD.has(blockState)) {
            DustParticleOptions pParticleData = new DustParticleOptions(new Vector3f(1.0F, 0.2F, 0.0F), 1.0F);
            Vec3 centerOf = VecHelper.getCenterOf(this.f_58858_);
            Vec3 normal = Vec3.atLowerCornerOf(PlacardBlock.connectedDirection(blockState).getNormal());
            Vec3 offset = VecHelper.axisAlingedPlaneOf(normal);
            for (int i = 0; i < 10; i++) {
                Vec3 v = VecHelper.offsetRandomly(Vec3.ZERO, this.f_58857_.random, 0.5F).multiply(offset).normalize().scale(0.45F).add(normal.scale(-0.45F)).add(centerOf);
                this.f_58857_.addParticle(pParticleData, v.x, v.y, v.z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
    }
}