package dev.xkmc.modulargolems.compat.materials.cataclysm;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.ai.goal.Goal;

public class AncientRemnantSandstormModifier extends GolemModifier {

    public AncientRemnantSandstormModifier() {
        super(StatFilterType.MASS, 1);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new AncientRemnantSandstormAttackGoal(entity, lv));
    }
}