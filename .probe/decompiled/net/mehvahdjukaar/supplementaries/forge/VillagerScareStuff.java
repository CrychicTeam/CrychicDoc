package net.mehvahdjukaar.supplementaries.forge;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.entity.VillagerAIHooks;
import net.mehvahdjukaar.moonlight.api.events.IVillagerBrainEvent;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
import net.minecraft.world.entity.ai.behavior.VillageBoundRandomStroll;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;

public class VillagerScareStuff {

    private static final Supplier<Activity> NOTEBLOCK_SCARE = RegHelper.registerActivity(Supplementaries.res("scared_by_noteblock"), () -> new Activity("scared_by_noteblock"));

    private static final Supplier<MemoryModuleType<BlockPos>> NEAREST_NOTEBLOCK = RegHelper.registerMemoryModule(Supplementaries.res("scary_noteblock"), (Supplier<MemoryModuleType<BlockPos>>) (() -> new MemoryModuleType(Optional.empty())));

    private static final int DESIRED_DISTANCE = 4;

    public static void init() {
        VillagerAIHooks.addBrainModification(VillagerScareStuff::modifyBrain);
    }

    public static void setup() {
        VillagerAIHooks.registerMemory((MemoryModuleType<?>) NEAREST_NOTEBLOCK.get());
    }

    private static void modifyBrain(IVillagerBrainEvent event) {
        event.addOrReplaceActivity((Activity) NOTEBLOCK_SCARE.get(), getNoteblockPanicPackage(0.5F));
    }

    private static ImmutableList<Pair<Integer, ? extends BehaviorControl<? super Villager>>> getNoteblockPanicPackage(float speedModifier) {
        float f = speedModifier * 1.5F;
        return ImmutableList.of(Pair.of(0, createCamlDownBehavior()), Pair.of(1, SetWalkTargetAwayFrom.pos((MemoryModuleType<BlockPos>) NEAREST_NOTEBLOCK.get(), f, 4, false)), Pair.of(3, VillageBoundRandomStroll.create(f, 2, 2)), getMinimalLookBehavior());
    }

    public static BehaviorControl<LivingEntity> createCamlDownBehavior() {
        return BehaviorBuilder.create(instance -> instance.group(instance.registered((MemoryModuleType) NEAREST_NOTEBLOCK.get())).apply(instance, nearestNoteblock -> (level, livingEntity, time) -> {
            boolean isFarAway = instance.tryGet(nearestNoteblock).filter(pos -> pos.m_203193_(livingEntity.m_20182_()) <= 16.0).isPresent();
            if (!isFarAway) {
                nearestNoteblock.erase();
                livingEntity.getBrain().updateActivityFromSchedule(level.m_46468_(), level.m_46467_());
            }
            return true;
        }));
    }

    private static Pair<Integer, BehaviorControl<LivingEntity>> getMinimalLookBehavior() {
        return Pair.of(5, new RunOne(ImmutableList.of(Pair.of(SetEntityLookTarget.create(EntityType.VILLAGER, 8.0F), 2), Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), 2), Pair.of(new DoNothing(30, 60), 8))));
    }

    public static void scareVillagers(LevelAccessor level, BlockPos pos) {
        if ((Boolean) CommonConfigs.Tweaks.SCARE_VILLAGERS.get()) {
            for (Villager v : level.m_45976_(Villager.class, new AABB(pos).inflate(8.0))) {
                Brain<Villager> brain = v.getBrain();
                if (!brain.isActive(Activity.PANIC) && !brain.isActive((Activity) NOTEBLOCK_SCARE.get())) {
                    brain.eraseMemory(MemoryModuleType.PATH);
                    brain.eraseMemory(MemoryModuleType.WALK_TARGET);
                    brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
                    brain.eraseMemory(MemoryModuleType.BREED_TARGET);
                    brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
                    brain.setMemory((MemoryModuleType<BlockPos>) NEAREST_NOTEBLOCK.get(), pos);
                }
                brain.setActiveActivityIfPossible((Activity) NOTEBLOCK_SCARE.get());
            }
        }
    }
}