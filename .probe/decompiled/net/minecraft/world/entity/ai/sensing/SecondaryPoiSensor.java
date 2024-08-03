package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.Level;

public class SecondaryPoiSensor extends Sensor<Villager> {

    private static final int SCAN_RATE = 40;

    public SecondaryPoiSensor() {
        super(40);
    }

    protected void doTick(ServerLevel serverLevel0, Villager villager1) {
        ResourceKey<Level> $$2 = serverLevel0.m_46472_();
        BlockPos $$3 = villager1.m_20183_();
        List<GlobalPos> $$4 = Lists.newArrayList();
        int $$5 = 4;
        for (int $$6 = -4; $$6 <= 4; $$6++) {
            for (int $$7 = -2; $$7 <= 2; $$7++) {
                for (int $$8 = -4; $$8 <= 4; $$8++) {
                    BlockPos $$9 = $$3.offset($$6, $$7, $$8);
                    if (villager1.getVillagerData().getProfession().secondaryPoi().contains(serverLevel0.m_8055_($$9).m_60734_())) {
                        $$4.add(GlobalPos.of($$2, $$9));
                    }
                }
            }
        }
        Brain<?> $$10 = villager1.getBrain();
        if (!$$4.isEmpty()) {
            $$10.setMemory(MemoryModuleType.SECONDARY_JOB_SITE, $$4);
        } else {
            $$10.eraseMemory(MemoryModuleType.SECONDARY_JOB_SITE);
        }
    }

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.SECONDARY_JOB_SITE);
    }
}