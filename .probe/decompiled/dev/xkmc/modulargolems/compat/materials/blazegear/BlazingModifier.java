package dev.xkmc.modulargolems.compat.materials.blazegear;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlazingModifier extends GolemModifier {

    public BlazingModifier() {
        super(StatFilterType.MASS, 3);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new BlazeAttackGoal(entity, lv));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void onClientTick(AbstractGolemEntity<?, ?> entity, int value) {
        entity.m_9236_().addParticle(ParticleTypes.LARGE_SMOKE, entity.m_20208_(0.5), entity.m_20187_(), entity.m_20262_(0.5), 0.0, 0.0, 0.0);
    }
}