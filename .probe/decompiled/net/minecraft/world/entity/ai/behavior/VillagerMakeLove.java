package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableMap;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.pathfinder.Path;

public class VillagerMakeLove extends Behavior<Villager> {

    private static final int INTERACT_DIST_SQR = 5;

    private static final float SPEED_MODIFIER = 0.5F;

    private long birthTimestamp;

    public VillagerMakeLove() {
        super(ImmutableMap.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryStatus.VALUE_PRESENT), 350, 350);
    }

    protected boolean checkExtraStartConditions(ServerLevel serverLevel0, Villager villager1) {
        return this.isBreedingPossible(villager1);
    }

    protected boolean canStillUse(ServerLevel serverLevel0, Villager villager1, long long2) {
        return long2 <= this.birthTimestamp && this.isBreedingPossible(villager1);
    }

    protected void start(ServerLevel serverLevel0, Villager villager1, long long2) {
        AgeableMob $$3 = (AgeableMob) villager1.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
        BehaviorUtils.lockGazeAndWalkToEachOther(villager1, $$3, 0.5F);
        serverLevel0.broadcastEntityEvent($$3, (byte) 18);
        serverLevel0.broadcastEntityEvent(villager1, (byte) 18);
        int $$4 = 275 + villager1.m_217043_().nextInt(50);
        this.birthTimestamp = long2 + (long) $$4;
    }

    protected void tick(ServerLevel serverLevel0, Villager villager1, long long2) {
        Villager $$3 = (Villager) villager1.getBrain().getMemory(MemoryModuleType.BREED_TARGET).get();
        if (!(villager1.m_20280_($$3) > 5.0)) {
            BehaviorUtils.lockGazeAndWalkToEachOther(villager1, $$3, 0.5F);
            if (long2 >= this.birthTimestamp) {
                villager1.eatAndDigestFood();
                $$3.eatAndDigestFood();
                this.tryToGiveBirth(serverLevel0, villager1, $$3);
            } else if (villager1.m_217043_().nextInt(35) == 0) {
                serverLevel0.broadcastEntityEvent($$3, (byte) 12);
                serverLevel0.broadcastEntityEvent(villager1, (byte) 12);
            }
        }
    }

    private void tryToGiveBirth(ServerLevel serverLevel0, Villager villager1, Villager villager2) {
        Optional<BlockPos> $$3 = this.takeVacantBed(serverLevel0, villager1);
        if (!$$3.isPresent()) {
            serverLevel0.broadcastEntityEvent(villager2, (byte) 13);
            serverLevel0.broadcastEntityEvent(villager1, (byte) 13);
        } else {
            Optional<Villager> $$4 = this.breed(serverLevel0, villager1, villager2);
            if ($$4.isPresent()) {
                this.giveBedToChild(serverLevel0, (Villager) $$4.get(), (BlockPos) $$3.get());
            } else {
                serverLevel0.getPoiManager().release((BlockPos) $$3.get());
                DebugPackets.sendPoiTicketCountPacket(serverLevel0, (BlockPos) $$3.get());
            }
        }
    }

    protected void stop(ServerLevel serverLevel0, Villager villager1, long long2) {
        villager1.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
    }

    private boolean isBreedingPossible(Villager villager0) {
        Brain<Villager> $$1 = villager0.getBrain();
        Optional<AgeableMob> $$2 = $$1.getMemory(MemoryModuleType.BREED_TARGET).filter(p_289389_ -> p_289389_.m_6095_() == EntityType.VILLAGER);
        return !$$2.isPresent() ? false : BehaviorUtils.targetIsValid($$1, MemoryModuleType.BREED_TARGET, EntityType.VILLAGER) && villager0.canBreed() && ((AgeableMob) $$2.get()).canBreed();
    }

    private Optional<BlockPos> takeVacantBed(ServerLevel serverLevel0, Villager villager1) {
        return serverLevel0.getPoiManager().take(p_217509_ -> p_217509_.is(PoiTypes.HOME), (p_217506_, p_217507_) -> this.canReach(villager1, p_217507_, p_217506_), villager1.m_20183_(), 48);
    }

    private boolean canReach(Villager villager0, BlockPos blockPos1, Holder<PoiType> holderPoiType2) {
        Path $$3 = villager0.m_21573_().createPath(blockPos1, holderPoiType2.value().validRange());
        return $$3 != null && $$3.canReach();
    }

    private Optional<Villager> breed(ServerLevel serverLevel0, Villager villager1, Villager villager2) {
        Villager $$3 = villager1.getBreedOffspring(serverLevel0, villager2);
        if ($$3 == null) {
            return Optional.empty();
        } else {
            villager1.m_146762_(6000);
            villager2.m_146762_(6000);
            $$3.m_146762_(-24000);
            $$3.m_7678_(villager1.m_20185_(), villager1.m_20186_(), villager1.m_20189_(), 0.0F, 0.0F);
            serverLevel0.m_47205_($$3);
            serverLevel0.broadcastEntityEvent($$3, (byte) 12);
            return Optional.of($$3);
        }
    }

    private void giveBedToChild(ServerLevel serverLevel0, Villager villager1, BlockPos blockPos2) {
        GlobalPos $$3 = GlobalPos.of(serverLevel0.m_46472_(), blockPos2);
        villager1.getBrain().setMemory(MemoryModuleType.HOME, $$3);
    }
}