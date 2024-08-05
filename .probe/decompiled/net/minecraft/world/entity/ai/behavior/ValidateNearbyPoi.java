package net.minecraft.world.entity.ai.behavior;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ValidateNearbyPoi {

    private static final int MAX_DISTANCE = 16;

    public static BehaviorControl<LivingEntity> create(Predicate<Holder<PoiType>> predicateHolderPoiType0, MemoryModuleType<GlobalPos> memoryModuleTypeGlobalPos1) {
        return BehaviorBuilder.create(p_259215_ -> p_259215_.group(p_259215_.present(memoryModuleTypeGlobalPos1)).apply(p_259215_, p_259498_ -> (p_259843_, p_259259_, p_260036_) -> {
            GlobalPos $$6 = p_259215_.get(p_259498_);
            BlockPos $$7 = $$6.pos();
            if (p_259843_.m_46472_() == $$6.dimension() && $$7.m_203195_(p_259259_.m_20182_(), 16.0)) {
                ServerLevel $$8 = p_259843_.getServer().getLevel($$6.dimension());
                if ($$8 == null || !$$8.getPoiManager().exists($$7, predicateHolderPoiType0)) {
                    p_259498_.erase();
                } else if (bedIsOccupied($$8, $$7, p_259259_)) {
                    p_259498_.erase();
                    p_259843_.getPoiManager().release($$7);
                    DebugPackets.sendPoiTicketCountPacket(p_259843_, $$7);
                }
                return true;
            } else {
                return false;
            }
        }));
    }

    private static boolean bedIsOccupied(ServerLevel serverLevel0, BlockPos blockPos1, LivingEntity livingEntity2) {
        BlockState $$3 = serverLevel0.m_8055_(blockPos1);
        return $$3.m_204336_(BlockTags.BEDS) && (Boolean) $$3.m_61143_(BedBlock.OCCUPIED) && !livingEntity2.isSleeping();
    }
}