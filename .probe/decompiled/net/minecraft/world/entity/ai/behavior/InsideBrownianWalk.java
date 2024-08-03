package net.minecraft.world.entity.ai.behavior;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class InsideBrownianWalk {

    public static BehaviorControl<PathfinderMob> create(float float0) {
        return BehaviorBuilder.create(p_258399_ -> p_258399_.group(p_258399_.absent(MemoryModuleType.WALK_TARGET)).apply(p_258399_, p_258397_ -> (p_258393_, p_258394_, p_258395_) -> {
            if (p_258393_.m_45527_(p_258394_.m_20183_())) {
                return false;
            } else {
                BlockPos $$5 = p_258394_.m_20183_();
                List<BlockPos> $$6 = (List<BlockPos>) BlockPos.betweenClosedStream($$5.offset(-1, -1, -1), $$5.offset(1, 1, 1)).map(BlockPos::m_7949_).collect(Collectors.toList());
                Collections.shuffle($$6);
                $$6.stream().filter(p_23230_ -> !p_258393_.m_45527_(p_23230_)).filter(p_23237_ -> p_258393_.m_46575_(p_23237_, p_258394_)).filter(p_23227_ -> p_258393_.m_45786_(p_258394_)).findFirst().ifPresent(p_258402_ -> p_258397_.set(new WalkTarget(p_258402_, float0, 0)));
                return true;
            }
        }));
    }
}