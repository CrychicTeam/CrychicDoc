package net.liopyu.entityjs.events;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.ai.CustomGoal;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.BreathAirGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.ClimbOnTopOfPowderSnowGoal;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowBoatGoal;
import net.minecraft.world.entity.ai.goal.FollowMobGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.ai.goal.RestrictSunGoal;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

public class AddGoalSelectorsEventJS<T extends Mob> extends GoalEventJS<T> {

    public AddGoalSelectorsEventJS(T mob, GoalSelector selector) {
        super(mob, selector);
    }

    @Info(value = "Remove a goal from the entity via class reference.\n\nExample of usage:\n=====================================\nlet $PanicGoal = Java.loadClass(\"net.minecraft.world.entity.ai.goal.PanicGoal\")\nbuilder.removeGoal($PanicGoal)\n=====================================\n", params = { @Param(name = "goal", value = "The goal class to remove") })
    public void removeGoal(Class<? extends Goal> goal) {
        this.selector.removeAllGoals(g -> goal == g.getClass());
    }

    @Info(value = "Remove all goals fitting the specified predicate. Returns a boolean\n\nExample of usage:\n=====================================\nlet $PanicGoal = Java.loadClass(\"net.minecraft.world.entity.ai.goal.PanicGoal\")\ne.removeGoals(context => {\n    const { goal, entity } = context\n    return goal.getClass() == $PanicGoal\n})\n=====================================\n", params = { @Param(name = "goalFunction", value = "A function to remove goals with entity & available goals as arguments") })
    public void removeGoals(Function<ContextUtils.GoalContext, Boolean> goalFunction) {
        this.selector.removeAllGoals(g -> {
            ContextUtils.GoalContext context = new ContextUtils.GoalContext(this.getEntity(), g);
            Object remove = EntityJSHelperClass.convertObjectToDesired(goalFunction.apply(context), "boolean");
            if (remove != null) {
                return (Boolean) remove;
            } else {
                ConsoleJS.SERVER.error("[EntityJS]: Failed to remove goals from entity " + this.getEntity().m_7755_() + ": function must return a boolean.");
                return false;
            }
        });
    }

    @Info(value = "Remove all goals.\n\nExample of usage:\n=====================================\nbuilder.removeAllGoals()\n=====================================\n", params = { @Param(name = "goal", value = "The goal to remove") })
    public void removeAllGoals() {
        this.selector.removeAllGoals(goal -> true);
    }

    @Info(value = "Enables the addition of arbitrary goals to an entity\n\nIt is the responsibility of the user to ensure the goal is\ncompatible with the entity\n\nExample of usage:\n=====================================\nbuilder.arbitraryGoal(3, entity -> new $PathFindToRaidGoal(entity))\n=====================================\n\nNote in the example the entity must be an instance of Raider\n", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "goalSupplier", value = "The goal supplier, a function that takes a Mob and returns a Goal") })
    public void arbitraryGoal(int priority, Function<T, Goal> goalSupplier) {
        this.selector.addGoal(priority, (Goal) goalSupplier.apply(this.mob));
    }

    @Info(value = "Adds a custom goal to the entity", params = { @Param(name = "name", value = "The name of the custom goal"), @Param(name = "priority", value = "The priority of the goal"), @Param(name = "canUse", value = "Determines if the entity can use the goal"), @Param(name = "canContinueToUse", value = "Determines if the entity can continue to use the goal, may be null"), @Param(name = "isInterruptable", value = "If the goal may be interrupted"), @Param(name = "start", value = "The action to perform when the goal starts"), @Param(name = "stop", value = "The action to perform when the goal stops"), @Param(name = "requiresUpdateEveryTick", value = "If the goal needs to be updated every tick"), @Param(name = "tick", value = "The action to perform when the goal ticks") })
    public void customGoal(String name, int priority, Predicate<T> canUse, @Nullable Predicate<T> canContinueToUse, boolean isInterruptable, Consumer<T> start, Consumer<T> stop, boolean requiresUpdateEveryTick, Consumer<T> tick) {
        this.selector.addGoal(priority, new CustomGoal(name, this.mob, canUse, canContinueToUse, isInterruptable, start, stop, requiresUpdateEveryTick, tick));
    }

    @Info(value = "Adds a `AvoidEntityGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "entityClassToAvoid", value = "The class of entity to avoid"), @Param(name = "avoidPredicate", value = "The conditions under which an entity will be avoided"), @Param(name = "maxDist", value = "The maximum distance from a entity the mob will detect and flee from it"), @Param(name = "walkSpeedModifier", value = "Modifies the mob's speed when avoiding an entity"), @Param(name = "sprintSpeedModifier", value = "Modifies the mob's speed when avoiding an entity at close range"), @Param(name = "onAvoidEntityPredicate", value = "An additional predicate for entity avoidance") })
    public <E extends LivingEntity> void avoidEntity(int priority, Class<E> entityClassToAvoid, Predicate<LivingEntity> avoidPredicate, float maxDist, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> onAvoidEntityPredicate) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new AvoidEntityGoal((PathfinderMob) this.mob, entityClassToAvoid, avoidPredicate, maxDist, walkSpeedModifier, sprintSpeedModifier, onAvoidEntityPredicate));
        }
    }

    @Info(value = "Adds a `BreakDoorGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "doorBreakTime", value = "The time it takes to break a door, limited to 240 ticks"), @Param(name = "validDifficulties", value = "Determines what difficulties are valid for the goal") })
    public void breakDoor(int priority, int doorBreakTime, Predicate<Difficulty> validDifficulties) {
        this.selector.addGoal(priority, new BreakDoorGoal(this.mob, doorBreakTime, validDifficulties));
    }

    @Info(value = "Adds a `BreathAirGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void breathAir(int priority) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new BreathAirGoal((PathfinderMob) this.mob));
        }
    }

    @Info(value = "Adds a `BreedGoal` to the entity, only applicable to **animal** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "partnerClass", value = "The class of animal that this entity breeds with, may be null to specify it be the same class as this entity") })
    public void breed(int priority, double speedModifier, @Nullable Class<? extends Animal> partnerClass) {
        if (this.isAnimal) {
            this.selector.addGoal(priority, new BreedGoal((Animal) this.mob, speedModifier, partnerClass != null ? partnerClass : UtilsJS.cast(this.mob.getClass())));
        }
    }

    @Info(value = "Adds a `FloatGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void floatSwim(int priority) {
        this.selector.addGoal(priority, new FloatGoal(this.mob));
    }

    @Info(value = "Adds a `RemoveBlockGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "block", value = "The registry name of a block, the block to be removed"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "verticalSearchRange", value = "The vertical range the mob will search for the block") })
    public void removeBlock(int priority, ResourceLocation block, double speedModifier, int verticalSearchRange) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new RemoveBlockGoal((Block) Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(block)), (PathfinderMob) this.mob, speedModifier, verticalSearchRange));
        }
    }

    @Info(value = "Adds a `ClimbOnTopOfPowderSnowGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void climbOnTopOfPowderedSnow(int priority) {
        this.selector.addGoal(priority, new ClimbOnTopOfPowderSnowGoal(this.mob, this.mob.m_9236_()));
    }

    @Info(value = "Adds a `EatBlockGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void eatGrass(int priority) {
        this.selector.addGoal(priority, new EatBlockGoal(this.mob));
    }

    @Info(value = "Adds a `MeleeAttackGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "followTargetEventIfNotSeen", value = "Determines if the entity should follow the target even if it doesn't see it") })
    public void meleeAttack(int priority, double speedModifier, boolean followTargetEvenIfNotSeen) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new MeleeAttackGoal((PathfinderMob) this.mob, speedModifier, followTargetEvenIfNotSeen));
        }
    }

    @Info(value = "Adds a `FleeSunGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move") })
    public void fleeSun(int priority, double speedModifier) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new FleeSunGoal((PathfinderMob) this.mob, speedModifier));
        }
    }

    @Info(value = "Adds a `FollowBoatGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void followBoat(int priority) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new FollowBoatGoal((PathfinderMob) this.mob));
        }
    }

    @Info(value = "Adds a `FollowMobGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "stopDistance", value = "The distance away from the target the mob will stop"), @Param(name = "areaSize", value = "The distance away from the mob, that will be searched for mobs to follow") })
    public void followMob(int priority, double speedModifier, float stopDistance, float areaSize) {
        this.selector.addGoal(priority, new FollowMobGoal(this.mob, speedModifier, stopDistance, areaSize));
    }

    @Info(value = "Adds a `FollowOwnerGoal` to the entity, only applicable to **tamable** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "startDistance", value = "The distance away from the owner the mob will start moving"), @Param(name = "stopDistance", value = "The distance away from the owner the mob will stop moving"), @Param(name = "canFly", value = "If the mob can teleport into leaves") })
    public void followOwner(int priority, double speedModifier, float startDistance, float stopDistance, boolean canFly) {
        if (this.isTamable) {
            this.selector.addGoal(priority, new FollowOwnerGoal((TamableAnimal) this.mob, speedModifier, startDistance, stopDistance, canFly));
        }
    }

    @Info(value = "Adds a `FollowParentGoal` to the entity, only applicable to **animal** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move") })
    public void followParent(int priority, double speedModifier) {
        if (this.isAnimal) {
            this.selector.addGoal(priority, new FollowParentGoal((Animal) this.mob, speedModifier));
        }
    }

    @Info(value = "Adds a `LookAtPlayerGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "targetClass", value = "The entity class that should be looked at"), @Param(name = "lookDistance", value = "How far away the entity should be looked at"), @Param(name = "probability", value = "The probability, in the range [0, 1], that the goal may be used"), @Param(name = "onlyHorizontal", value = "Determines if the eye level must be the same to follow the target entity") })
    public <E extends LivingEntity> void lookAtEntity(int priority, Class<E> targetClass, float lookDistance, float probability, boolean onlyHorizontal) {
        this.selector.addGoal(priority, new LookAtPlayerGoal(this.mob, targetClass, lookDistance, probability, onlyHorizontal));
    }

    @Info(value = "Adds a `LeapAtTargetGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "deltaY", value = "Sets the delta movement of the animal in the y-axis") })
    public void leapAtTarget(int priority, float deltaY) {
        this.selector.addGoal(priority, new LeapAtTargetGoal(this.mob, deltaY));
    }

    @Info(value = "Adds a `RandomStrollGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "interval", value = "Sets the interval at which the goal will be 'refreshed, any values below 1 will be 1.'"), @Param(name = "checkNoActionTime", value = "Determines if the mob's noActionTime property should be checked") })
    public void randomStroll(int priority, double speedModifier, int interval, boolean checkNoActionTime) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new RandomStrollGoal((PathfinderMob) this.mob, speedModifier, Math.max(1, interval), checkNoActionTime));
        }
    }

    @Info(value = "Adds a `MoveBackToVillageGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "checkNoActionTime", value = "Determines if the mob's noActionTime property should be checked") })
    public void moveBackToVillage(int priority, double speedModifier, boolean checkNoActionTime) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new MoveBackToVillageGoal((PathfinderMob) this.mob, speedModifier, checkNoActionTime));
        }
    }

    @Info(value = "Adds a `MoveThroughVillageGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "onlyAtNight", value = "If this goal should only apply at night"), @Param(name = "distanceToPoi", value = "The minimum distance to a poi the mob must be to have it be considered 'visited'"), @Param(name = "canDealWithDoors", value = "If doors can be opened to navigate as part of this goal") })
    public void moveThroughVillage(int priority, double speedModifier, boolean onlyAtNight, int distanceToPoi, Supplier<Boolean> canDealWithDoors) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new MoveThroughVillageGoal((PathfinderMob) this.mob, speedModifier, onlyAtNight, distanceToPoi, canDealWithDoors::get));
        }
    }

    @Info(value = "Adds a `MoveTowardsRestrictionGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move") })
    public void moveTowardsRestriction(int priority, double speedModifier) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new MoveTowardsRestrictionGoal((PathfinderMob) this.mob, speedModifier));
        }
    }

    @Info(value = "Adds a `MoveTowardsTargetGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "distanceWithin", value = "The distance the target must be within to move towards it") })
    public void moveTowardsTarget(int priority, double speedModifier, float distanceWithin) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new MoveTowardsTargetGoal((PathfinderMob) this.mob, speedModifier, distanceWithin));
        }
    }

    @Info(value = "Adds a `OcelotAttackGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void ocelotAttack(int priority) {
        this.selector.addGoal(priority, new OcelotAttackGoal(this.mob));
    }

    @Info(value = "Adds a `OpenDoorGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "closeDoor", value = "If the entity should also close doors") })
    public void openDoor(int priority, boolean closeDoor) {
        this.selector.addGoal(priority, new OpenDoorGoal(this.mob, closeDoor));
    }

    @Info(value = "Adds a `PanicGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move") })
    public void panic(int priority, double speedModifier) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new PanicGoal((PathfinderMob) this.mob, speedModifier));
        }
    }

    @Info(value = "Adds a `RandomLookAroundGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void randomLookAround(int priority) {
        this.selector.addGoal(priority, new RandomLookAroundGoal(this.mob));
    }

    @Info(value = "Adds a `RandomSwimmingGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "interval", value = "Sets the interval at which the goal will be refreshed") })
    public void randomSwimming(int priority, double speedModifier, int interval) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new RandomSwimmingGoal((PathfinderMob) this.mob, speedModifier, interval));
        }
    }

    @Info(value = "Adds a `RangedAttackGoal` to the entity, only applicable to **ranged attack** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "attackIntervalMin", value = "The minimum interval between attacks"), @Param(name = "attackIntervalMax", value = "The maximum interval between attacks"), @Param(name = "attackRadius", value = "The maximum distance something can be attacked from") })
    public <E extends Mob & RangedAttackMob> void rangedAttack(int priority, double speedModifier, int attackIntervalMin, int attackIntervalMax, float attackRadius) {
        if (this.isRangedAttack) {
            this.selector.addGoal(priority, new RangedAttackGoal((RangedAttackMob) this.mob, speedModifier, attackIntervalMin, attackIntervalMax, attackRadius));
        }
    }

    @Info(value = "Adds a `RestrictSunGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void restrictSun(int priority) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new RestrictSunGoal((PathfinderMob) this.mob));
        }
    }

    @Info(value = "Adds a `RunAroundLikeCrazyGoal` to the entity, only applicable to **horse** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move") })
    public void horseRunAroundLikeCrazy(int priority, double speedModifier) {
        if (this.isHorse) {
            this.selector.addGoal(priority, new RunAroundLikeCrazyGoal((AbstractHorse) this.mob, speedModifier));
        }
    }

    @Info(value = "Adds a `SitWhenOrderedToGoal` to the entity, only applicable to **tamable** mobs", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void sitWhenOrdered(int priority) {
        if (this.isTamable) {
            this.selector.addGoal(priority, new SitWhenOrderedToGoal((TamableAnimal) this.mob));
        }
    }

    @Info(value = "Adds a `StrollThroughVillageGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "interval", value = "Sets how often the goal 'refreshes'") })
    public void strollThroughVillage(int priority, int interval) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new StrollThroughVillageGoal((PathfinderMob) this.mob, interval));
        }
    }

    @Info(value = "Adds a `TemptGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "temptItems", value = "The ingredient that determines what items tempt the mob"), @Param(name = "canScare", value = "If the mob can be scared by getting to close to the tempter") })
    public void tempt(int priority, double speedModifier, Ingredient temptItems, boolean canScare) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new TemptGoal((PathfinderMob) this.mob, speedModifier, temptItems, canScare));
        }
    }

    @Info(value = "Adds a `TryFindWaterGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal") })
    public void tryFindWater(int priority) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new TryFindWaterGoal((PathfinderMob) this.mob));
        }
    }

    @Info(value = "Adds a `UseItemGoal` to the entity", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "itemToUse", value = "The item that will be used"), @Param(name = "soundEvent", value = "The registry name of a sound event that should play when the item is used, may be null to indicate not sound event should play"), @Param(name = "canUseSelector", value = "Determines when the item may be used") })
    public void useItem(int priority, ItemStack itemToUse, @Nullable ResourceLocation soundEvent, Predicate<T> canUseSelector) {
        this.selector.addGoal(priority, new UseItemGoal(this.mob, itemToUse, soundEvent == null ? null : ForgeRegistries.SOUND_EVENTS.getValue(soundEvent), canUseSelector));
    }

    @Info(value = "Adds a `WaterAvoidingRandomFlyingGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move") })
    public void waterAvoidingRandomFlying(int priority, double speedModifier) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new WaterAvoidingRandomFlyingGoal((PathfinderMob) this.mob, speedModifier));
        }
    }

    @Info(value = "Adds a `WaterAvoidRandomStrollingGoal` to the entity, only applicable to **pathfinder** mobs", params = { @Param(name = "priority", value = "The priority of the goal"), @Param(name = "speedModifier", value = "Sets the speed at which the mob should try to move"), @Param(name = "probability", value = "The probability, in the range [0, 1], that the entity picks a new position") })
    public void waterAvoidingRandomStroll(int priority, double speedModifier, float probability) {
        if (this.isPathFinder) {
            this.selector.addGoal(priority, new WaterAvoidingRandomStrollGoal((PathfinderMob) this.mob, speedModifier, probability));
        }
    }
}