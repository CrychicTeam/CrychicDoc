package com.github.alexthe666.alexsmobs.tileentity;

import com.github.alexthe666.alexsmobs.block.AMBlockRegistry;
import com.github.alexthe666.alexsmobs.block.BlockLeafcutterAntChamber;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityLeafcutterAnt;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TileEntityLeafcutterAnthill extends BlockEntity {

    private static final Direction[] DIRECTIONS_UP = new Direction[] { Direction.UP, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    private final List<TileEntityLeafcutterAnthill.Ant> ants = Lists.newArrayList();

    private int leafFeedings = 0;

    public TileEntityLeafcutterAnthill(BlockPos pos, BlockState state) {
        super(AMTileEntityRegistry.LEAFCUTTER_ANTHILL.get(), pos, state);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TileEntityLeafcutterAnthill entity) {
        entity.tickAnts();
    }

    @Nullable
    public static Entity loadEntityAndExecute(CompoundTag compound, Level worldIn, Function<Entity, Entity> p_220335_2_) {
        return (Entity) loadEntity(compound, worldIn).map(p_220335_2_).map(p_220346_3_ -> {
            if (compound.contains("Passengers", 9)) {
                ListTag listnbt = compound.getList("Passengers", 10);
                for (int i = 0; i < listnbt.size(); i++) {
                    Entity entity = loadEntityAndExecute(listnbt.getCompound(i), worldIn, p_220335_2_);
                    if (entity != null) {
                        entity.startRiding(p_220346_3_, true);
                    }
                }
            }
            return p_220346_3_;
        }).orElse(null);
    }

    private static Optional<Entity> loadEntity(CompoundTag compound, Level worldIn) {
        try {
            return loadEntityUnchecked(compound, worldIn);
        } catch (RuntimeException var3) {
            return Optional.empty();
        }
    }

    public static Optional<Entity> loadEntityUnchecked(CompoundTag compound, Level worldIn) {
        EntityLeafcutterAnt leafcutterAnt = AMEntityRegistry.LEAFCUTTER_ANT.get().create(worldIn);
        leafcutterAnt.m_20258_(compound);
        return Optional.of(leafcutterAnt);
    }

    public boolean hasNoAnts() {
        return this.ants.isEmpty();
    }

    public boolean hasAtleastThisManyAnts(int antCount) {
        return this.ants.size() >= antCount;
    }

    public boolean isFullOfAnts() {
        return this.ants.size() == AMConfig.leafcutterAntColonySize;
    }

    public void angerAnts(@Nullable LivingEntity p_226963_1_, BlockState p_226963_2_, BeehiveBlockEntity.BeeReleaseStatus p_226963_3_) {
        List<Entity> list = this.tryReleaseAnt(p_226963_2_, p_226963_3_);
        if (p_226963_1_ != null) {
            for (Entity entity : list) {
                if (entity instanceof EntityLeafcutterAnt) {
                    EntityLeafcutterAnt entityLeafcutterAnt = (EntityLeafcutterAnt) entity;
                    if (p_226963_1_.m_20182_().distanceToSqr(entity.position()) <= 16.0) {
                        entityLeafcutterAnt.setTarget(p_226963_1_);
                    }
                    entityLeafcutterAnt.setStayOutOfHiveCountdown(400);
                }
            }
        }
    }

    public void angerAntsBecauseAnteater(@Nullable LivingEntity p_226963_1_, BlockState p_226963_2_, BeehiveBlockEntity.BeeReleaseStatus p_226963_3_) {
        List<Entity> list = this.tryReleaseAntAnteater(p_226963_2_, p_226963_3_);
        if (p_226963_1_ != null) {
            for (Entity entity : list) {
                if (entity instanceof EntityLeafcutterAnt) {
                    EntityLeafcutterAnt entityLeafcutterAnt = (EntityLeafcutterAnt) entity;
                    if (p_226963_1_.m_20182_().distanceToSqr(entity.position()) <= 16.0) {
                        entityLeafcutterAnt.setTarget(p_226963_1_);
                    }
                    entityLeafcutterAnt.setStayOutOfHiveCountdown(400);
                }
            }
        }
    }

    private List<Entity> tryReleaseAnt(BlockState p_226965_1_, BeehiveBlockEntity.BeeReleaseStatus p_226965_2_) {
        List<Entity> list = Lists.newArrayList();
        this.ants.removeIf(p_226966_4_ -> this.addAntToWorld(p_226965_1_, p_226966_4_, list, p_226965_2_));
        return list;
    }

    private List<Entity> tryReleaseAntAnteater(BlockState p_226965_1_, BeehiveBlockEntity.BeeReleaseStatus p_226965_2_) {
        List<Entity> list = Lists.newArrayList();
        this.ants.removeIf(ant -> !ant.queen && this.addAntToWorld(p_226965_1_, ant, list, p_226965_2_));
        return list;
    }

    private boolean addAntToWorld(BlockState p_235651_1_, TileEntityLeafcutterAnthill.Ant p_235651_2_, @Nullable List<Entity> p_235651_3_, BeehiveBlockEntity.BeeReleaseStatus p_235651_4_) {
        BlockPos blockpos = this.m_58899_();
        CompoundTag compoundnbt = p_235651_2_.entityData;
        compoundnbt.remove("Passengers");
        compoundnbt.remove("Leash");
        compoundnbt.remove("UUID");
        BlockPos blockpos1 = blockpos.above();
        boolean flag = !this.f_58857_.getBlockState(blockpos1).m_60816_(this.f_58857_, blockpos1).isEmpty();
        if (flag && p_235651_4_ != BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY) {
            return false;
        } else {
            Entity entity = loadEntityAndExecute(compoundnbt, this.f_58857_, p_226960_0_ -> p_226960_0_);
            if (entity != null) {
                if (entity instanceof EntityLeafcutterAnt entityLeafcutterAnt) {
                    entityLeafcutterAnt.setLeaf(false);
                    if (p_235651_4_ == BeehiveBlockEntity.BeeReleaseStatus.HONEY_DELIVERED) {
                    }
                    if (p_235651_3_ != null) {
                        p_235651_3_.add(entityLeafcutterAnt);
                    }
                    float f = entity.getBbWidth();
                    double d0 = (double) blockpos.m_123341_() + 0.5;
                    double d1 = (double) blockpos.m_123342_() + 1.0;
                    double d2 = (double) blockpos.m_123343_() + 0.5;
                    entity.moveTo(d0, d1, d2, entity.getYRot(), entity.getXRot());
                    if (((EntityLeafcutterAnt) entity).isQueen()) {
                        entityLeafcutterAnt.setStayOutOfHiveCountdown(400);
                    }
                }
                this.f_58857_.m_220407_(GameEvent.BLOCK_ACTIVATE, this.m_58899_(), GameEvent.Context.of(this.m_58900_()));
                this.f_58857_.playSound(null, blockpos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                return this.f_58857_.m_7967_(entity);
            } else {
                return false;
            }
        }
    }

    public void tryEnterHive(EntityLeafcutterAnt p_226962_1_, boolean p_226962_2_, int p_226962_3_) {
        if (this.ants.size() < AMConfig.leafcutterAntColonySize) {
            p_226962_1_.m_8127_();
            p_226962_1_.m_20153_();
            CompoundTag compoundnbt = new CompoundTag();
            p_226962_1_.m_20223_(compoundnbt);
            if (p_226962_2_) {
                if (!this.f_58857_.isClientSide && (double) p_226962_1_.m_217043_().nextFloat() < AMConfig.leafcutterAntFungusGrowChance) {
                    this.growFungus();
                }
                this.leafFeedings++;
                if (this.leafFeedings >= AMConfig.leafcutterAntRepopulateFeedings && this.getAntsInAreaCount(32.0) < Mth.ceil((float) AMConfig.leafcutterAntColonySize * 0.5F) && this.hasQueen()) {
                    this.leafFeedings = 0;
                    this.ants.add(new TileEntityLeafcutterAnthill.Ant(new CompoundTag(), 0, 100, false));
                }
            }
            this.ants.add(new TileEntityLeafcutterAnthill.Ant(compoundnbt, p_226962_3_, p_226962_2_ ? 100 : 200, p_226962_1_.isQueen()));
            if (this.f_58857_ != null) {
                BlockPos blockpos = this.m_58899_();
                this.f_58857_.m_220407_(GameEvent.BLOCK_ACTIVATE, this.m_58899_(), GameEvent.Context.of(this.m_58900_()));
                this.f_58857_.playSound(null, (double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            p_226962_1_.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    private int getAntsInAreaCount(double size) {
        int ants = this.getAntCount();
        Vec3 vec = Vec3.atCenterOf(this.m_58899_());
        AABB box = new AABB(vec.add(-size, -size, -size), vec.add(size, size, size));
        return ants + this.f_58857_.m_45976_(EntityLeafcutterAnt.class, box).size();
    }

    public boolean hasQueen() {
        for (TileEntityLeafcutterAnthill.Ant ant : this.ants) {
            if (ant.queen) {
                return true;
            }
        }
        return false;
    }

    public void releaseQueens() {
        this.ants.removeIf(p_226966_4_ -> p_226966_4_.queen && this.addAntToWorld(this.m_58900_(), p_226966_4_, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED));
    }

    public void tryEnterHive(EntityLeafcutterAnt p_226961_1_, boolean p_226961_2_) {
        this.tryEnterHive(p_226961_1_, p_226961_2_, 0);
    }

    public int getAntCount() {
        return this.ants.size();
    }

    @Override
    public void setChanged() {
        if (this.isNearFire()) {
            this.angerAnts(null, this.f_58857_.getBlockState(this.m_58899_()), BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
        }
        super.setChanged();
    }

    public boolean isNearFire() {
        if (this.f_58857_ == null) {
            return false;
        } else {
            for (BlockPos blockpos : BlockPos.betweenClosed(this.f_58858_.offset(-1, -1, -1), this.f_58858_.offset(1, 1, 1))) {
                if (this.f_58857_.getBlockState(blockpos).m_60734_() instanceof FireBlock) {
                    return true;
                }
            }
            return false;
        }
    }

    public BlockState shrinkFungus() {
        BlockPos bottomChamber = this.m_58899_().below();
        while (this.f_58857_.getBlockState(bottomChamber.below()).m_60734_() == AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get() && bottomChamber.m_123342_() > 0) {
            bottomChamber = bottomChamber.below();
        }
        BlockPos chamber = bottomChamber;
        if (!this.isUnfilledChamber(bottomChamber)) {
            BlockState prev = this.f_58857_.getBlockState(bottomChamber);
            if (prev.m_60713_(AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get())) {
                int fungalLevel = (Integer) prev.m_61143_(BlockLeafcutterAntChamber.FUNGUS);
                this.f_58857_.setBlockAndUpdate(bottomChamber, (BlockState) AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState().m_61124_(BlockLeafcutterAntChamber.FUNGUS, Math.min(0, fungalLevel - 1)));
                return prev;
            }
        } else {
            boolean flag = false;
            List<BlockPos> possibleChambers = new ArrayList();
            while (!flag) {
                for (BlockPos blockpos : BlockPos.betweenClosed(chamber.offset(-4, 0, -4), chamber.offset(4, 0, 4))) {
                    if (this.isUnfilledChamber(blockpos)) {
                        possibleChambers.add(blockpos.immutable());
                        flag = true;
                    }
                }
                if (!flag) {
                    chamber = chamber.above();
                    if (chamber.m_123342_() > this.f_58858_.m_123342_()) {
                        return null;
                    }
                }
            }
            Collections.shuffle(possibleChambers);
            if (!possibleChambers.isEmpty()) {
                BlockPos newChamber = (BlockPos) possibleChambers.get(0);
                if (newChamber != null && !this.isUnfilledChamber(newChamber)) {
                    BlockState prev = this.f_58857_.getBlockState(newChamber);
                    if (prev.m_60713_(AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get())) {
                        int fungalLevel = (Integer) prev.m_61143_(BlockLeafcutterAntChamber.FUNGUS);
                        this.f_58857_.setBlockAndUpdate(newChamber, (BlockState) AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState().m_61124_(BlockLeafcutterAntChamber.FUNGUS, Math.min(fungalLevel - 1, 0)));
                        return prev;
                    }
                }
            }
        }
        return null;
    }

    public void growFungus() {
        if (!this.hasNoAnts()) {
            BlockPos bottomChamber = this.m_58899_().below();
            while (this.f_58857_.getBlockState(bottomChamber.below()).m_60734_() == AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get() && bottomChamber.m_123342_() > 0) {
                bottomChamber = bottomChamber.below();
            }
            BlockPos chamber = bottomChamber;
            if (this.isUnfilledChamber(bottomChamber)) {
                int fungalLevel = (Integer) this.f_58857_.getBlockState(bottomChamber).m_61143_(BlockLeafcutterAntChamber.FUNGUS);
                int fungalLevel2 = Mth.clamp(fungalLevel + 1 + this.f_58857_.getRandom().nextInt(1), 0, 5);
                this.f_58857_.setBlockAndUpdate(bottomChamber, (BlockState) AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState().m_61124_(BlockLeafcutterAntChamber.FUNGUS, fungalLevel2));
            } else {
                boolean flag = false;
                List<BlockPos> possibleChambers = new ArrayList();
                while (!flag) {
                    for (BlockPos blockpos : BlockPos.betweenClosed(chamber.offset(-4, 0, -4), chamber.offset(4, 0, 4))) {
                        if (this.isUnfilledChamber(blockpos)) {
                            possibleChambers.add(blockpos.immutable());
                            flag = true;
                        }
                    }
                    if (!flag) {
                        chamber = chamber.above();
                        if (chamber.m_123342_() > this.f_58858_.m_123342_()) {
                            return;
                        }
                    }
                }
                Collections.shuffle(possibleChambers);
                if (!possibleChambers.isEmpty()) {
                    BlockPos newChamber = (BlockPos) possibleChambers.get(0);
                    if (newChamber != null && this.isUnfilledChamber(newChamber)) {
                        int fungalLevel = (Integer) this.f_58857_.getBlockState(newChamber).m_61143_(BlockLeafcutterAntChamber.FUNGUS);
                        int fungalLevel2 = Mth.clamp(fungalLevel + 1 + this.f_58857_.getRandom().nextInt(1), 0, 5);
                        this.f_58857_.setBlockAndUpdate(newChamber, (BlockState) AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get().defaultBlockState().m_61124_(BlockLeafcutterAntChamber.FUNGUS, fungalLevel2));
                    }
                }
            }
        }
    }

    private boolean isUnfilledChamber(BlockPos pos) {
        return this.f_58857_.getBlockState(pos).m_60734_() == AMBlockRegistry.LEAFCUTTER_ANT_CHAMBER.get() && (Integer) this.f_58857_.getBlockState(pos).m_61143_(BlockLeafcutterAntChamber.FUNGUS) < 5;
    }

    private void tickAnts() {
        Iterator<TileEntityLeafcutterAnthill.Ant> iterator = this.ants.iterator();
        BlockState blockstate = this.m_58900_();
        while (iterator.hasNext()) {
            TileEntityLeafcutterAnthill.Ant ant = (TileEntityLeafcutterAnthill.Ant) iterator.next();
            if (ant.ticksInHive > ant.minOccupationTicks && !ant.queen) {
                BeehiveBlockEntity.BeeReleaseStatus beehivetileentity$state = ant.entityData.getBoolean("HasNectar") ? BeehiveBlockEntity.BeeReleaseStatus.HONEY_DELIVERED : BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED;
                if (this.addAntToWorld(blockstate, ant, null, beehivetileentity$state)) {
                    iterator.remove();
                }
            }
            ant.ticksInHive++;
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.ants.clear();
        this.leafFeedings = nbt.getInt("LeafFeedings");
        ListTag listnbt = nbt.getList("Ants", 10);
        for (int i = 0; i < listnbt.size(); i++) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            TileEntityLeafcutterAnthill.Ant beehiveTileEntity$ant = new TileEntityLeafcutterAnthill.Ant(compoundnbt.getCompound("EntityData"), compoundnbt.getInt("TicksInHive"), compoundnbt.getInt("MinOccupationTicks"), compoundnbt.getBoolean("Queen"));
            this.ants.add(beehiveTileEntity$ant);
        }
    }

    public ListTag getAnts() {
        ListTag listnbt = new ListTag();
        for (TileEntityLeafcutterAnthill.Ant beehiveTileEntity$ant : this.ants) {
            beehiveTileEntity$ant.entityData.remove("UUID");
            CompoundTag compoundnbt = new CompoundTag();
            compoundnbt.put("EntityData", beehiveTileEntity$ant.entityData);
            compoundnbt.putInt("TicksInHive", beehiveTileEntity$ant.ticksInHive);
            compoundnbt.putInt("MinOccupationTicks", beehiveTileEntity$ant.minOccupationTicks);
            listnbt.add(compoundnbt);
        }
        return listnbt;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Ants", this.getAnts());
        compound.putInt("LeafFeedings", this.leafFeedings);
    }

    static class Ant {

        private final CompoundTag entityData;

        private final int minOccupationTicks;

        private int ticksInHive;

        private final boolean queen;

        private Ant(CompoundTag p_i225767_1_, int p_i225767_2_, int p_i225767_3_, boolean queen) {
            p_i225767_1_.remove("UUID");
            this.entityData = p_i225767_1_;
            this.ticksInHive = p_i225767_2_;
            this.minOccupationTicks = p_i225767_3_;
            this.queen = queen;
        }
    }
}