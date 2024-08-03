package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "crockpot")
public class AnimalsFollowPowcakeEvent {

    @SubscribeEvent
    public static void onAnimalAppear(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof Animal animal) {
            boolean hasTemptGoal = false;
            boolean hasEatGoal = false;
            for (WrappedGoal wrappedGoal : animal.f_21345_.getAvailableGoals()) {
                Goal goal = wrappedGoal.getGoal();
                hasTemptGoal = hasTemptGoal || isTemptGoal(goal);
                hasEatGoal = hasEatGoal || isEatGoal(goal);
            }
            if (!hasTemptGoal) {
                try {
                    animal.f_21345_.addGoal(3, new TemptGoal(animal, 0.8, Ingredient.of(CrockPotItems.POW_CAKE.get()), false));
                } catch (Exception var8) {
                    CrockPot.LOGGER.error("Error when adding TemptGoal to {} {}", animal.getClass().getName(), animal);
                }
            }
            if (!hasEatGoal) {
                try {
                    animal.f_21345_.addGoal(4, new PowCakeBlock.AnimalEatPowCakeGoal(animal, 0.8, 3));
                } catch (Exception var7) {
                    CrockPot.LOGGER.error("Error when adding AnimalEatPowCakeGoal to {} {}", animal.getClass().getName(), animal);
                }
            }
        }
    }

    private static boolean isTemptGoal(Goal goal) {
        if (goal instanceof TemptGoal temptGoal && temptGoal.items.test(CrockPotItems.POW_CAKE.get().getDefaultInstance())) {
            return true;
        }
        return false;
    }

    private static boolean isEatGoal(Goal goal) {
        return goal instanceof PowCakeBlock.AnimalEatPowCakeGoal;
    }
}