package dev.xkmc.modulargolems.content.modifier.special;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.item.golem.GolemPart;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.ai.goal.Goal;

public class SonicModifier extends GolemModifier {

    public SonicModifier() {
        super(StatFilterType.HEALTH, 1);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new SonicAttackGoal(entity, lv));
    }

    @Override
    public boolean canExistOn(GolemPart<?, ?> part) {
        return part == GolemItems.GOLEM_BODY.get();
    }
}