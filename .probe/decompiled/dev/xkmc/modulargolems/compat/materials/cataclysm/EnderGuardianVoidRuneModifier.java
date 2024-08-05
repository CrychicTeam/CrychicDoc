package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnderGuardianVoidRuneModifier extends GolemModifier {

    public static void addRune(LivingEntity user, LivingEntity target, int lv) {
        double minY = Math.min(target.m_20186_(), user.m_20186_());
        double maxY = Math.max(target.m_20186_(), user.m_20186_()) + 1.0;
        Vec3 v = target.m_146892_().subtract(user.m_146892_()).normalize();
        float angle = (float) Mth.atan2(v.z, v.x);
        for (int j = 1; j <= 15; j++) {
            double dist = 1.25 * (double) j;
            spawnFangs(user, user.m_20185_() + (double) Mth.cos(angle) * dist, user.m_20189_() + (double) Mth.sin(angle) * dist, minY, maxY, angle, j);
        }
    }

    private static void spawnFangs(LivingEntity user, double x, double z, double minY, double maxY, float rotation, int delay) {
        BlockPos pos = BlockPos.containing(x, maxY, z);
        boolean flag = false;
        double dy = 0.0;
        do {
            BlockPos below = pos.below();
            BlockState state = user.m_9236_().getBlockState(below);
            if (state.m_60783_(user.m_9236_(), below, Direction.UP)) {
                if (!user.m_9236_().m_46859_(pos)) {
                    BlockState next = user.m_9236_().getBlockState(pos);
                    VoxelShape shape = next.m_60812_(user.m_9236_(), pos);
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
            user.m_9236_().m_7967_(new Void_Rune_Entity(user.m_9236_(), x, (double) pos.m_123342_() + dy, z, rotation, delay, user));
        }
    }

    public EnderGuardianVoidRuneModifier() {
        super(StatFilterType.MASS, 1);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new EnderGuardianVoidRuneAttackGoal(entity, lv));
    }
}