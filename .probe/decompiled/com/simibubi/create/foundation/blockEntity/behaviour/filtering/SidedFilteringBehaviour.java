package com.simibubi.create.foundation.blockEntity.behaviour.filtering;

import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.NBTHelper;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SidedFilteringBehaviour extends FilteringBehaviour {

    Map<Direction, FilteringBehaviour> sidedFilters;

    private BiFunction<Direction, FilteringBehaviour, FilteringBehaviour> filterFactory;

    private Predicate<Direction> validDirections;

    public SidedFilteringBehaviour(SmartBlockEntity be, ValueBoxTransform.Sided sidedSlot, BiFunction<Direction, FilteringBehaviour, FilteringBehaviour> filterFactory, Predicate<Direction> validDirections) {
        super(be, sidedSlot);
        this.filterFactory = filterFactory;
        this.validDirections = validDirections;
        this.sidedFilters = new IdentityHashMap();
        this.updateFilterPresence();
    }

    @Override
    public void initialize() {
        super.initialize();
    }

    public FilteringBehaviour get(Direction side) {
        return (FilteringBehaviour) this.sidedFilters.get(side);
    }

    public void updateFilterPresence() {
        Set<Direction> valid = new HashSet();
        for (Direction d : Iterate.directions) {
            if (this.validDirections.test(d)) {
                valid.add(d);
            }
        }
        for (Direction dx : Iterate.directions) {
            if (valid.contains(dx)) {
                if (!this.sidedFilters.containsKey(dx)) {
                    this.sidedFilters.put(dx, (FilteringBehaviour) this.filterFactory.apply(dx, new FilteringBehaviour(this.blockEntity, this.slotPositioning)));
                }
            } else if (this.sidedFilters.containsKey(dx)) {
                this.removeFilter(dx);
            }
        }
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.put("Filters", NBTHelper.writeCompoundList(this.sidedFilters.entrySet(), entry -> {
            CompoundTag compound = new CompoundTag();
            compound.putInt("Side", ((Direction) entry.getKey()).get3DDataValue());
            ((FilteringBehaviour) entry.getValue()).write(compound, clientPacket);
            return compound;
        }));
        super.write(nbt, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        NBTHelper.iterateCompoundList(nbt.getList("Filters", 10), compound -> {
            Direction face = Direction.from3DDataValue(compound.getInt("Side"));
            if (this.sidedFilters.containsKey(face)) {
                ((FilteringBehaviour) this.sidedFilters.get(face)).read(compound, clientPacket);
            }
        });
        super.read(nbt, clientPacket);
    }

    @Override
    public void tick() {
        super.tick();
        this.sidedFilters.values().forEach(BlockEntityBehaviour::tick);
    }

    @Override
    public boolean setFilter(Direction side, ItemStack stack) {
        if (!this.sidedFilters.containsKey(side)) {
            return true;
        } else {
            ((FilteringBehaviour) this.sidedFilters.get(side)).setFilter(stack);
            return true;
        }
    }

    @Override
    public ItemStack getFilter(Direction side) {
        return !this.sidedFilters.containsKey(side) ? ItemStack.EMPTY : ((FilteringBehaviour) this.sidedFilters.get(side)).getFilter();
    }

    public boolean test(Direction side, ItemStack stack) {
        return !this.sidedFilters.containsKey(side) ? true : ((FilteringBehaviour) this.sidedFilters.get(side)).test(stack);
    }

    @Override
    public void destroy() {
        this.sidedFilters.values().forEach(FilteringBehaviour::destroy);
        super.destroy();
    }

    @Override
    public ItemRequirement getRequiredItems() {
        return (ItemRequirement) this.sidedFilters.values().stream().reduce(ItemRequirement.NONE, (a, b) -> a.union(b.getRequiredItems()), (a, b) -> a.union(b));
    }

    public void removeFilter(Direction side) {
        if (this.sidedFilters.containsKey(side)) {
            ((FilteringBehaviour) this.sidedFilters.remove(side)).destroy();
        }
    }

    public boolean testHit(Direction direction, Vec3 hit) {
        ValueBoxTransform.Sided sidedPositioning = (ValueBoxTransform.Sided) this.slotPositioning;
        BlockState state = this.blockEntity.m_58900_();
        Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(this.blockEntity.m_58899_()));
        return sidedPositioning.fromSide(direction).testHit(state, localHit);
    }
}