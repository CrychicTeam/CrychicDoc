package dev.xkmc.modulargolems.compat.materials.botania;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.ai.goal.Goal;

public class ManaBurstModifier extends ManaModifier {

    public ManaBurstModifier() {
        super(StatFilterType.ATTACK, 5);
    }

    @Override
    public void onRegisterGoals(AbstractGolemEntity<?, ?> entity, int lv, BiConsumer<Integer, Goal> addGoal) {
        addGoal.accept(5, new ManaBurstAttackGoal(entity, lv));
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int prob = (int) Math.round(MGConfig.COMMON.manaBurstDamage.get() * (double) v * 100.0);
        int manaCost = MGConfig.COMMON.manaBurstCost.get() * v;
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", prob, manaCost).withStyle(ChatFormatting.GREEN));
    }
}