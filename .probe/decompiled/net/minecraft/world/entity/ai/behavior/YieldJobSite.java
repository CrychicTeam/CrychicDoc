package net.minecraft.world.entity.ai.behavior;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.pathfinder.Path;

public class YieldJobSite {

    public static BehaviorControl<Villager> create(float float0) {
        return BehaviorBuilder.create(p_258916_ -> p_258916_.group(p_258916_.present(MemoryModuleType.POTENTIAL_JOB_SITE), p_258916_.absent(MemoryModuleType.JOB_SITE), p_258916_.present(MemoryModuleType.NEAREST_LIVING_ENTITIES), p_258916_.registered(MemoryModuleType.WALK_TARGET), p_258916_.registered(MemoryModuleType.LOOK_TARGET)).apply(p_258916_, (p_258901_, p_258902_, p_258903_, p_258904_, p_258905_) -> (p_258912_, p_258913_, p_258914_) -> {
            if (p_258913_.m_6162_()) {
                return false;
            } else if (p_258913_.getVillagerData().getProfession() != VillagerProfession.NONE) {
                return false;
            } else {
                BlockPos $$9 = p_258916_.<GlobalPos>get(p_258901_).pos();
                Optional<Holder<PoiType>> $$10 = p_258912_.getPoiManager().getType($$9);
                if ($$10.isEmpty()) {
                    return true;
                } else {
                    p_258916_.<List>get(p_258903_).stream().filter(p_258898_ -> p_258898_ instanceof Villager && p_258898_ != p_258913_).map(p_258896_ -> (Villager) p_258896_).filter(LivingEntity::m_6084_).filter(p_258919_ -> nearbyWantsJobsite((Holder<PoiType>) $$10.get(), p_258919_, $$9)).findFirst().ifPresent(p_288865_ -> {
                        p_258904_.erase();
                        p_258905_.erase();
                        p_258901_.erase();
                        if (p_288865_.getBrain().getMemory(MemoryModuleType.JOB_SITE).isEmpty()) {
                            BehaviorUtils.setWalkAndLookTargetMemories(p_288865_, $$9, float0, 1);
                            p_288865_.getBrain().setMemory(MemoryModuleType.POTENTIAL_JOB_SITE, GlobalPos.of(p_258912_.m_46472_(), $$9));
                            DebugPackets.sendPoiTicketCountPacket(p_258912_, $$9);
                        }
                    });
                    return true;
                }
            }
        }));
    }

    private static boolean nearbyWantsJobsite(Holder<PoiType> holderPoiType0, Villager villager1, BlockPos blockPos2) {
        boolean $$3 = villager1.getBrain().getMemory(MemoryModuleType.POTENTIAL_JOB_SITE).isPresent();
        if ($$3) {
            return false;
        } else {
            Optional<GlobalPos> $$4 = villager1.getBrain().getMemory(MemoryModuleType.JOB_SITE);
            VillagerProfession $$5 = villager1.getVillagerData().getProfession();
            if ($$5.heldJobSite().test(holderPoiType0)) {
                return $$4.isEmpty() ? canReachPos(villager1, blockPos2, holderPoiType0.value()) : ((GlobalPos) $$4.get()).pos().equals(blockPos2);
            } else {
                return false;
            }
        }
    }

    private static boolean canReachPos(PathfinderMob pathfinderMob0, BlockPos blockPos1, PoiType poiType2) {
        Path $$3 = pathfinderMob0.m_21573_().createPath(blockPos1, poiType2.validRange());
        return $$3 != null && $$3.canReach();
    }
}