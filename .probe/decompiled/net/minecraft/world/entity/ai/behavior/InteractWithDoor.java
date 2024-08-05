package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.Sets;
import com.mojang.datafixers.kinds.OptionalBox.Mu;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

public class InteractWithDoor {

    private static final int COOLDOWN_BEFORE_RERUNNING_IN_SAME_NODE = 20;

    private static final double SKIP_CLOSING_DOOR_IF_FURTHER_AWAY_THAN = 3.0;

    private static final double MAX_DISTANCE_TO_HOLD_DOOR_OPEN_FOR_OTHER_MOBS = 2.0;

    public static BehaviorControl<LivingEntity> create() {
        MutableObject<Node> $$0 = new MutableObject(null);
        MutableInt $$1 = new MutableInt(0);
        return BehaviorBuilder.create(p_258474_ -> p_258474_.group(p_258474_.present(MemoryModuleType.PATH), p_258474_.registered(MemoryModuleType.DOORS_TO_CLOSE), p_258474_.registered(MemoryModuleType.NEAREST_LIVING_ENTITIES)).apply(p_258474_, (p_258460_, p_258461_, p_258462_) -> (p_258469_, p_258470_, p_258471_) -> {
            Path $$9 = p_258474_.get(p_258460_);
            Optional<Set<GlobalPos>> $$10 = p_258474_.tryGet(p_258461_);
            if (!$$9.notStarted() && !$$9.isDone()) {
                if (Objects.equals($$0.getValue(), $$9.getNextNode())) {
                    $$1.setValue(20);
                } else if ($$1.decrementAndGet() > 0) {
                    return false;
                }
                $$0.setValue($$9.getNextNode());
                Node $$11 = $$9.getPreviousNode();
                Node $$12 = $$9.getNextNode();
                BlockPos $$13 = $$11.asBlockPos();
                BlockState $$14 = p_258469_.m_8055_($$13);
                if ($$14.m_204338_(BlockTags.WOODEN_DOORS, p_201959_ -> p_201959_.getBlock() instanceof DoorBlock)) {
                    DoorBlock $$15 = (DoorBlock) $$14.m_60734_();
                    if (!$$15.isOpen($$14)) {
                        $$15.setOpen(p_258470_, p_258469_, $$14, $$13, true);
                    }
                    $$10 = rememberDoorToClose(p_258461_, $$10, p_258469_, $$13);
                }
                BlockPos $$16 = $$12.asBlockPos();
                BlockState $$17 = p_258469_.m_8055_($$16);
                if ($$17.m_204338_(BlockTags.WOODEN_DOORS, p_201957_ -> p_201957_.getBlock() instanceof DoorBlock)) {
                    DoorBlock $$18 = (DoorBlock) $$17.m_60734_();
                    if (!$$18.isOpen($$17)) {
                        $$18.setOpen(p_258470_, p_258469_, $$17, $$16, true);
                        $$10 = rememberDoorToClose(p_258461_, $$10, p_258469_, $$16);
                    }
                }
                $$10.ifPresent(p_258452_ -> closeDoorsThatIHaveOpenedOrPassedThrough(p_258469_, p_258470_, $$11, $$12, p_258452_, p_258474_.tryGet(p_258462_)));
                return true;
            } else {
                return false;
            }
        }));
    }

    public static void closeDoorsThatIHaveOpenedOrPassedThrough(ServerLevel serverLevel0, LivingEntity livingEntity1, @Nullable Node node2, @Nullable Node node3, Set<GlobalPos> setGlobalPos4, Optional<List<LivingEntity>> optionalListLivingEntity5) {
        Iterator<GlobalPos> $$6 = setGlobalPos4.iterator();
        while ($$6.hasNext()) {
            GlobalPos $$7 = (GlobalPos) $$6.next();
            BlockPos $$8 = $$7.pos();
            if ((node2 == null || !node2.asBlockPos().equals($$8)) && (node3 == null || !node3.asBlockPos().equals($$8))) {
                if (isDoorTooFarAway(serverLevel0, livingEntity1, $$7)) {
                    $$6.remove();
                } else {
                    BlockState $$9 = serverLevel0.m_8055_($$8);
                    if (!$$9.m_204338_(BlockTags.WOODEN_DOORS, p_201952_ -> p_201952_.getBlock() instanceof DoorBlock)) {
                        $$6.remove();
                    } else {
                        DoorBlock $$10 = (DoorBlock) $$9.m_60734_();
                        if (!$$10.isOpen($$9)) {
                            $$6.remove();
                        } else if (areOtherMobsComingThroughDoor(livingEntity1, $$8, optionalListLivingEntity5)) {
                            $$6.remove();
                        } else {
                            $$10.setOpen(livingEntity1, serverLevel0, $$9, $$8, false);
                            $$6.remove();
                        }
                    }
                }
            }
        }
    }

    private static boolean areOtherMobsComingThroughDoor(LivingEntity livingEntity0, BlockPos blockPos1, Optional<List<LivingEntity>> optionalListLivingEntity2) {
        return optionalListLivingEntity2.isEmpty() ? false : ((List) optionalListLivingEntity2.get()).stream().filter(p_289329_ -> p_289329_.m_6095_() == livingEntity0.m_6095_()).filter(p_289331_ -> blockPos1.m_203195_(p_289331_.m_20182_(), 2.0)).anyMatch(p_258454_ -> isMobComingThroughDoor(p_258454_.getBrain(), blockPos1));
    }

    private static boolean isMobComingThroughDoor(Brain<?> brain0, BlockPos blockPos1) {
        if (!brain0.hasMemoryValue(MemoryModuleType.PATH)) {
            return false;
        } else {
            Path $$2 = (Path) brain0.getMemory(MemoryModuleType.PATH).get();
            if ($$2.isDone()) {
                return false;
            } else {
                Node $$3 = $$2.getPreviousNode();
                if ($$3 == null) {
                    return false;
                } else {
                    Node $$4 = $$2.getNextNode();
                    return blockPos1.equals($$3.asBlockPos()) || blockPos1.equals($$4.asBlockPos());
                }
            }
        }
    }

    private static boolean isDoorTooFarAway(ServerLevel serverLevel0, LivingEntity livingEntity1, GlobalPos globalPos2) {
        return globalPos2.dimension() != serverLevel0.m_46472_() || !globalPos2.pos().m_203195_(livingEntity1.m_20182_(), 3.0);
    }

    private static Optional<Set<GlobalPos>> rememberDoorToClose(MemoryAccessor<Mu, Set<GlobalPos>> memoryAccessorMuSetGlobalPos0, Optional<Set<GlobalPos>> optionalSetGlobalPos1, ServerLevel serverLevel2, BlockPos blockPos3) {
        GlobalPos $$4 = GlobalPos.of(serverLevel2.m_46472_(), blockPos3);
        return Optional.of((Set) optionalSetGlobalPos1.map(p_261437_ -> {
            p_261437_.add($$4);
            return p_261437_;
        }).orElseGet(() -> {
            Set<GlobalPos> $$2 = Sets.newHashSet(new GlobalPos[] { $$4 });
            memoryAccessorMuSetGlobalPos0.set($$2);
            return $$2;
        }));
    }
}