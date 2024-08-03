package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.AnimationMonster.BossMonsters.The_Leviathan.Abyss_Blast_Portal_Entity;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LeviathanBlastPortalModifier extends GolemModifier {

    public static void addBeam(LivingEntity user, LivingEntity target) {
        double tx = target.m_20185_();
        double ty = target.m_20186_();
        double tz = target.m_20189_();
        float f = (float) Mth.atan2(tz - user.m_20189_(), tx - user.m_20185_());
        spawnFangs(user, user.m_9236_(), tx, tz, ty - 50.0, ty + 3.0, f, 0);
    }

    private static void spawnFangs(LivingEntity user, Level level, double x, double z, double minY, double maxY, float rotation, int delay) {
        BlockPos pos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double dy = 0.0;
        do {
            BlockPos next = pos.below();
            BlockState state = level.getBlockState(next);
            if (state.m_60783_(level, next, Direction.UP)) {
                if (!level.m_46859_(pos)) {
                    VoxelShape shape = level.getBlockState(pos).m_60812_(level, pos);
                    if (!shape.isEmpty()) {
                        dy = shape.max(Direction.Axis.Y);
                    }
                }
                flag = true;
                break;
            }
            pos = pos.below();
        } while (pos.m_123342_() >= Mth.floor(minY) - 1);
        if (flag) {
            level.m_7967_(new Abyss_Blast_Portal_Entity(level, x, (double) pos.m_123342_() + dy, z, rotation, delay, user));
        }
    }

    public LeviathanBlastPortalModifier() {
        super(StatFilterType.MASS, 1);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new LeviathanBlastPortalAttackGoal(entity, lv));
    }
}