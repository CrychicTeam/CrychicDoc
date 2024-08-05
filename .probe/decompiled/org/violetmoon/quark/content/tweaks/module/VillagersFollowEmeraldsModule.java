package org.violetmoon.quark.content.tweaks.module;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.play.entity.ZEntityJoinLevel;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.MiscUtil;

@ZetaLoadModule(category = "tweaks")
public class VillagersFollowEmeraldsModule extends ZetaModule {

    @Hint
    Item emerald_block = Items.EMERALD_BLOCK;

    @PlayEvent
    public void onVillagerAppear(ZEntityJoinLevel event) {
        if (event.getEntity() instanceof Villager villager) {
            boolean alreadySetUp = villager.f_21345_.getAvailableGoals().stream().anyMatch(goal -> goal.getGoal() instanceof TemptGoal);
            if (!alreadySetUp) {
                try {
                    MiscUtil.addGoalJustAfterLatestWithPriority(villager.f_21345_, 2, new TemptGoal(villager, 0.6, Ingredient.of(Items.EMERALD_BLOCK), false));
                } catch (IllegalArgumentException var5) {
                }
            }
        }
    }
}