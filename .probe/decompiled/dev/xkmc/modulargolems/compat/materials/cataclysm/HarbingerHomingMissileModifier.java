package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.projectile.Wither_Homing_Missile_Entity;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class HarbingerHomingMissileModifier extends GolemModifier {

    public static void addBeam(LivingEntity user, LivingEntity target, Vec3 pos) {
        Wither_Homing_Missile_Entity laserBeam = new Wither_Homing_Missile_Entity(user.m_9236_(), user, target);
        laserBeam.m_20343_(pos.x(), pos.y(), pos.z());
        user.m_9236_().m_7967_(laserBeam);
    }

    public HarbingerHomingMissileModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new HarbingerHomingMissileAttackGoal(entity, lv));
    }
}