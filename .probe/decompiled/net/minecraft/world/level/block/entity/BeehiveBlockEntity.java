package net.minecraft.world.level.block.entity;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BeehiveBlockEntity extends BlockEntity {

    public static final String TAG_FLOWER_POS = "FlowerPos";

    public static final String MIN_OCCUPATION_TICKS = "MinOccupationTicks";

    public static final String ENTITY_DATA = "EntityData";

    public static final String TICKS_IN_HIVE = "TicksInHive";

    public static final String HAS_NECTAR = "HasNectar";

    public static final String BEES = "Bees";

    private static final List<String> IGNORED_BEE_TAGS = Arrays.asList("Air", "ArmorDropChances", "ArmorItems", "Brain", "CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying", "Fire", "HandDropChances", "HandItems", "HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos", "Rotation", "CannotEnterHiveTicks", "TicksSincePollination", "CropsGrownSincePollination", "HivePos", "Passengers", "Leash", "UUID");

    public static final int MAX_OCCUPANTS = 3;

    private static final int MIN_TICKS_BEFORE_REENTERING_HIVE = 400;

    private static final int MIN_OCCUPATION_TICKS_NECTAR = 2400;

    public static final int MIN_OCCUPATION_TICKS_NECTARLESS = 600;

    private final List<BeehiveBlockEntity.BeeData> stored = Lists.newArrayList();

    @Nullable
    private BlockPos savedFlowerPos;

    public BeehiveBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.BEEHIVE, blockPos0, blockState1);
    }

    @Override
    public void setChanged() {
        if (this.isFireNearby()) {
            this.emptyAllLivingFromHive(null, this.f_58857_.getBlockState(this.m_58899_()), BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
        }
        super.setChanged();
    }

    public boolean isFireNearby() {
        if (this.f_58857_ == null) {
            return false;
        } else {
            for (BlockPos $$0 : BlockPos.betweenClosed(this.f_58858_.offset(-1, -1, -1), this.f_58858_.offset(1, 1, 1))) {
                if (this.f_58857_.getBlockState($$0).m_60734_() instanceof FireBlock) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isEmpty() {
        return this.stored.isEmpty();
    }

    public boolean isFull() {
        return this.stored.size() == 3;
    }

    public void emptyAllLivingFromHive(@Nullable Player player0, BlockState blockState1, BeehiveBlockEntity.BeeReleaseStatus beehiveBlockEntityBeeReleaseStatus2) {
        List<Entity> $$3 = this.releaseAllOccupants(blockState1, beehiveBlockEntityBeeReleaseStatus2);
        if (player0 != null) {
            for (Entity $$4 : $$3) {
                if ($$4 instanceof Bee) {
                    Bee $$5 = (Bee) $$4;
                    if (player0.m_20182_().distanceToSqr($$4.position()) <= 16.0) {
                        if (!this.isSedated()) {
                            $$5.m_6710_(player0);
                        } else {
                            $$5.setStayOutOfHiveCountdown(400);
                        }
                    }
                }
            }
        }
    }

    private List<Entity> releaseAllOccupants(BlockState blockState0, BeehiveBlockEntity.BeeReleaseStatus beehiveBlockEntityBeeReleaseStatus1) {
        List<Entity> $$2 = Lists.newArrayList();
        this.stored.removeIf(p_272556_ -> releaseOccupant(this.f_58857_, this.f_58858_, blockState0, p_272556_, $$2, beehiveBlockEntityBeeReleaseStatus1, this.savedFlowerPos));
        if (!$$2.isEmpty()) {
            super.setChanged();
        }
        return $$2;
    }

    public void addOccupant(Entity entity0, boolean boolean1) {
        this.addOccupantWithPresetTicks(entity0, boolean1, 0);
    }

    @VisibleForDebug
    public int getOccupantCount() {
        return this.stored.size();
    }

    public static int getHoneyLevel(BlockState blockState0) {
        return (Integer) blockState0.m_61143_(BeehiveBlock.HONEY_LEVEL);
    }

    @VisibleForDebug
    public boolean isSedated() {
        return CampfireBlock.isSmokeyPos(this.f_58857_, this.m_58899_());
    }

    public void addOccupantWithPresetTicks(Entity entity0, boolean boolean1, int int2) {
        if (this.stored.size() < 3) {
            entity0.stopRiding();
            entity0.ejectPassengers();
            CompoundTag $$3 = new CompoundTag();
            entity0.save($$3);
            this.storeBee($$3, int2, boolean1);
            if (this.f_58857_ != null) {
                if (entity0 instanceof Bee $$4 && $$4.hasSavedFlowerPos() && (!this.hasSavedFlowerPos() || this.f_58857_.random.nextBoolean())) {
                    this.savedFlowerPos = $$4.getSavedFlowerPos();
                }
                BlockPos $$5 = this.m_58899_();
                this.f_58857_.playSound(null, (double) $$5.m_123341_(), (double) $$5.m_123342_(), (double) $$5.m_123343_(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.f_58857_.m_220407_(GameEvent.BLOCK_CHANGE, $$5, GameEvent.Context.of(entity0, this.m_58900_()));
            }
            entity0.discard();
            super.setChanged();
        }
    }

    public void storeBee(CompoundTag compoundTag0, int int1, boolean boolean2) {
        this.stored.add(new BeehiveBlockEntity.BeeData(compoundTag0, int1, boolean2 ? 2400 : 600));
    }

    private static boolean releaseOccupant(Level level0, BlockPos blockPos1, BlockState blockState2, BeehiveBlockEntity.BeeData beehiveBlockEntityBeeData3, @Nullable List<Entity> listEntity4, BeehiveBlockEntity.BeeReleaseStatus beehiveBlockEntityBeeReleaseStatus5, @Nullable BlockPos blockPos6) {
        if ((level0.isNight() || level0.isRaining()) && beehiveBlockEntityBeeReleaseStatus5 != BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY) {
            return false;
        } else {
            CompoundTag $$7 = beehiveBlockEntityBeeData3.entityData.copy();
            removeIgnoredBeeTags($$7);
            $$7.put("HivePos", NbtUtils.writeBlockPos(blockPos1));
            $$7.putBoolean("NoGravity", true);
            Direction $$8 = (Direction) blockState2.m_61143_(BeehiveBlock.FACING);
            BlockPos $$9 = blockPos1.relative($$8);
            boolean $$10 = !level0.getBlockState($$9).m_60812_(level0, $$9).isEmpty();
            if ($$10 && beehiveBlockEntityBeeReleaseStatus5 != BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY) {
                return false;
            } else {
                Entity $$11 = EntityType.loadEntityRecursive($$7, level0, p_58740_ -> p_58740_);
                if ($$11 != null) {
                    if (!$$11.getType().is(EntityTypeTags.BEEHIVE_INHABITORS)) {
                        return false;
                    } else {
                        if ($$11 instanceof Bee $$12) {
                            if (blockPos6 != null && !$$12.hasSavedFlowerPos() && level0.random.nextFloat() < 0.9F) {
                                $$12.setSavedFlowerPos(blockPos6);
                            }
                            if (beehiveBlockEntityBeeReleaseStatus5 == BeehiveBlockEntity.BeeReleaseStatus.HONEY_DELIVERED) {
                                $$12.dropOffNectar();
                                if (blockState2.m_204338_(BlockTags.BEEHIVES, p_202037_ -> p_202037_.m_61138_(BeehiveBlock.HONEY_LEVEL))) {
                                    int $$13 = getHoneyLevel(blockState2);
                                    if ($$13 < 5) {
                                        int $$14 = level0.random.nextInt(100) == 0 ? 2 : 1;
                                        if ($$13 + $$14 > 5) {
                                            $$14--;
                                        }
                                        level0.setBlockAndUpdate(blockPos1, (BlockState) blockState2.m_61124_(BeehiveBlock.HONEY_LEVEL, $$13 + $$14));
                                    }
                                }
                            }
                            setBeeReleaseData(beehiveBlockEntityBeeData3.ticksInHive, $$12);
                            if (listEntity4 != null) {
                                listEntity4.add($$12);
                            }
                            float $$15 = $$11.getBbWidth();
                            double $$16 = $$10 ? 0.0 : 0.55 + (double) ($$15 / 2.0F);
                            double $$17 = (double) blockPos1.m_123341_() + 0.5 + $$16 * (double) $$8.getStepX();
                            double $$18 = (double) blockPos1.m_123342_() + 0.5 - (double) ($$11.getBbHeight() / 2.0F);
                            double $$19 = (double) blockPos1.m_123343_() + 0.5 + $$16 * (double) $$8.getStepZ();
                            $$11.moveTo($$17, $$18, $$19, $$11.getYRot(), $$11.getXRot());
                        }
                        level0.playSound(null, blockPos1, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level0.m_220407_(GameEvent.BLOCK_CHANGE, blockPos1, GameEvent.Context.of($$11, level0.getBlockState(blockPos1)));
                        return level0.m_7967_($$11);
                    }
                } else {
                    return false;
                }
            }
        }
    }

    static void removeIgnoredBeeTags(CompoundTag compoundTag0) {
        for (String $$1 : IGNORED_BEE_TAGS) {
            compoundTag0.remove($$1);
        }
    }

    private static void setBeeReleaseData(int int0, Bee bee1) {
        int $$2 = bee1.m_146764_();
        if ($$2 < 0) {
            bee1.m_146762_(Math.min(0, $$2 + int0));
        } else if ($$2 > 0) {
            bee1.m_146762_(Math.max(0, $$2 - int0));
        }
        bee1.m_27601_(Math.max(0, bee1.m_27591_() - int0));
    }

    private boolean hasSavedFlowerPos() {
        return this.savedFlowerPos != null;
    }

    private static void tickOccupants(Level level0, BlockPos blockPos1, BlockState blockState2, List<BeehiveBlockEntity.BeeData> listBeehiveBlockEntityBeeData3, @Nullable BlockPos blockPos4) {
        boolean $$5 = false;
        Iterator<BeehiveBlockEntity.BeeData> $$6 = listBeehiveBlockEntityBeeData3.iterator();
        while ($$6.hasNext()) {
            BeehiveBlockEntity.BeeData $$7 = (BeehiveBlockEntity.BeeData) $$6.next();
            if ($$7.ticksInHive > $$7.minOccupationTicks) {
                BeehiveBlockEntity.BeeReleaseStatus $$8 = $$7.entityData.getBoolean("HasNectar") ? BeehiveBlockEntity.BeeReleaseStatus.HONEY_DELIVERED : BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED;
                if (releaseOccupant(level0, blockPos1, blockState2, $$7, null, $$8, blockPos4)) {
                    $$5 = true;
                    $$6.remove();
                }
            }
            $$7.ticksInHive++;
        }
        if ($$5) {
            m_155232_(level0, blockPos1, blockState2);
        }
    }

    public static void serverTick(Level level0, BlockPos blockPos1, BlockState blockState2, BeehiveBlockEntity beehiveBlockEntity3) {
        tickOccupants(level0, blockPos1, blockState2, beehiveBlockEntity3.stored, beehiveBlockEntity3.savedFlowerPos);
        if (!beehiveBlockEntity3.stored.isEmpty() && level0.getRandom().nextDouble() < 0.005) {
            double $$4 = (double) blockPos1.m_123341_() + 0.5;
            double $$5 = (double) blockPos1.m_123342_();
            double $$6 = (double) blockPos1.m_123343_() + 0.5;
            level0.playSound(null, $$4, $$5, $$6, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
        DebugPackets.sendHiveInfo(level0, blockPos1, blockState2, beehiveBlockEntity3);
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.stored.clear();
        ListTag $$1 = compoundTag0.getList("Bees", 10);
        for (int $$2 = 0; $$2 < $$1.size(); $$2++) {
            CompoundTag $$3 = $$1.getCompound($$2);
            BeehiveBlockEntity.BeeData $$4 = new BeehiveBlockEntity.BeeData($$3.getCompound("EntityData"), $$3.getInt("TicksInHive"), $$3.getInt("MinOccupationTicks"));
            this.stored.add($$4);
        }
        this.savedFlowerPos = null;
        if (compoundTag0.contains("FlowerPos")) {
            this.savedFlowerPos = NbtUtils.readBlockPos(compoundTag0.getCompound("FlowerPos"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.put("Bees", this.writeBees());
        if (this.hasSavedFlowerPos()) {
            compoundTag0.put("FlowerPos", NbtUtils.writeBlockPos(this.savedFlowerPos));
        }
    }

    public ListTag writeBees() {
        ListTag $$0 = new ListTag();
        for (BeehiveBlockEntity.BeeData $$1 : this.stored) {
            CompoundTag $$2 = $$1.entityData.copy();
            $$2.remove("UUID");
            CompoundTag $$3 = new CompoundTag();
            $$3.put("EntityData", $$2);
            $$3.putInt("TicksInHive", $$1.ticksInHive);
            $$3.putInt("MinOccupationTicks", $$1.minOccupationTicks);
            $$0.add($$3);
        }
        return $$0;
    }

    static class BeeData {

        final CompoundTag entityData;

        int ticksInHive;

        final int minOccupationTicks;

        BeeData(CompoundTag compoundTag0, int int1, int int2) {
            BeehiveBlockEntity.removeIgnoredBeeTags(compoundTag0);
            this.entityData = compoundTag0;
            this.ticksInHive = int1;
            this.minOccupationTicks = int2;
        }
    }

    public static enum BeeReleaseStatus {

        HONEY_DELIVERED, BEE_RELEASED, EMERGENCY
    }
}