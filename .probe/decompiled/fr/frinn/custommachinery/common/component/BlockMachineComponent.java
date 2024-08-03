package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.PartialBlockState;
import fr.frinn.custommachinery.common.util.Utils;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;

public class BlockMachineComponent extends AbstractMachineComponent {

    public BlockMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.BOTH);
    }

    @Override
    public MachineComponentType<BlockMachineComponent> getType() {
        return (MachineComponentType<BlockMachineComponent>) Registration.BLOCK_MACHINE_COMPONENT.get();
    }

    public long getBlockAmount(AABB box, List<IIngredient<PartialBlockState>> filter, boolean whitelist) {
        box = Utils.rotateBox(box, (Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
        box = box.move(this.getManager().getTile().m_58899_());
        return BlockPos.betweenClosedStream(box).map(pos -> new BlockInWorld(this.getManager().getLevel(), pos, false)).filter(block -> filter.stream().flatMap(ingredient -> ingredient.getAll().stream()).anyMatch(state -> state.test(block)) == whitelist).count();
    }

    public boolean placeBlock(AABB box, PartialBlockState block, int amount) {
        box = Utils.rotateBox(box, (Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
        box = box.move(this.getManager().getTile().m_58899_());
        if (BlockPos.betweenClosedStream(box).map(this.getManager().getLevel()::m_8055_).filter(state -> state.m_60734_() == Blocks.AIR).count() < (long) amount) {
            return false;
        } else {
            AtomicInteger toPlace = new AtomicInteger(amount);
            BlockPos.betweenClosedStream(box).forEach(pos -> {
                if (toPlace.get() > 0 && this.getManager().getLevel().getBlockState(pos).m_60734_() == Blocks.AIR) {
                    this.setBlock(this.getManager().getLevel(), pos, block);
                    toPlace.addAndGet(-1);
                }
            });
            return true;
        }
    }

    public boolean replaceBlock(AABB box, PartialBlockState block, int amount, boolean drop, List<IIngredient<PartialBlockState>> filter, boolean whitelist) {
        if (this.getBlockAmount(box, filter, whitelist) < (long) amount) {
            return false;
        } else {
            box = Utils.rotateBox(box, (Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
            box = box.move(this.getManager().getTile().m_58899_());
            AtomicInteger toPlace = new AtomicInteger(amount);
            BlockPos.betweenClosedStream(box).forEach(pos -> {
                if (toPlace.get() > 0) {
                    BlockInWorld cached = new BlockInWorld(this.getManager().getLevel(), pos, false);
                    if (filter.stream().flatMap(ingredient -> ingredient.getAll().stream()).anyMatch(state -> state.test(cached)) == whitelist) {
                        if (!cached.getState().m_60795_()) {
                            this.getManager().getLevel().m_46961_(pos, drop);
                        }
                        this.setBlock(this.getManager().getLevel(), pos, block);
                        toPlace.addAndGet(-1);
                    }
                }
            });
            return true;
        }
    }

    public boolean breakBlock(AABB box, List<IIngredient<PartialBlockState>> filter, boolean whitelist, int amount, boolean drop) {
        if (this.getBlockAmount(box, filter, whitelist) < (long) amount) {
            return false;
        } else {
            box = Utils.rotateBox(box, (Direction) this.getManager().getTile().m_58900_().m_61143_(BlockStateProperties.HORIZONTAL_FACING));
            box = box.move(this.getManager().getTile().m_58899_());
            AtomicInteger toPlace = new AtomicInteger(amount);
            BlockPos.betweenClosedStream(box).forEach(pos -> {
                if (toPlace.get() > 0) {
                    BlockInWorld cached = new BlockInWorld(this.getManager().getLevel(), pos, false);
                    if (filter.stream().flatMap(ingredient -> ingredient.getAll().stream()).anyMatch(state -> state.test(cached)) == whitelist) {
                        if (!cached.getState().m_60795_()) {
                            this.getManager().getLevel().m_46961_(pos, drop);
                        }
                        toPlace.addAndGet(-1);
                    }
                }
            });
            return true;
        }
    }

    private void setBlock(Level world, BlockPos pos, PartialBlockState state) {
        world.setBlockAndUpdate(pos, state.getBlockState());
        BlockEntity tile = world.getBlockEntity(pos);
        if (tile != null && state.getNbt() != null && !state.getNbt().isEmpty()) {
            CompoundTag nbt = state.getNbt().copy();
            nbt.putInt("x", pos.m_123341_());
            nbt.putInt("y", pos.m_123342_());
            nbt.putInt("z", pos.m_123343_());
            tile.load(nbt);
        }
    }
}