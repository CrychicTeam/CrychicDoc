package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.entity.projectile.Death_Laser_Beam_Entity;
import com.github.L_Ender.cataclysm.init.ModEntities;
import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class HarbingerDeathBeamModifier extends GolemModifier {

    public static Death_Laser_Beam_Entity addBeam(LivingEntity user) {
        Death_Laser_Beam_Entity beam = new Death_Laser_Beam_Entity((EntityType) ModEntities.DEATH_LASER_BEAM.get(), user.m_9236_(), user, user.m_20185_(), user.m_20188_(), user.m_20189_(), (user.yHeadRot + 90.0F) * (float) (Math.PI / 180.0), -user.m_146909_() * (float) (Math.PI / 180.0), 60);
        user.m_9236_().m_7967_(beam);
        return beam;
    }

    public HarbingerDeathBeamModifier(StatFilterType type, int maxLevel) {
        super(type, maxLevel);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new HarbingerDeathBeamAttackGoal(entity, lv));
    }
}