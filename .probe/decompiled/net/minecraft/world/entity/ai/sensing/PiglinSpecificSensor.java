package net.minecraft.world.entity.ai.sensing;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PiglinSpecificSensor extends Sensor<LivingEntity> {

    @Override
    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, new MemoryModuleType[] { MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_REPELLENT });
    }

    @Override
    protected void doTick(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        Brain<?> $$2 = livingEntity1.getBrain();
        $$2.setMemory(MemoryModuleType.NEAREST_REPELLENT, findNearestRepellent(serverLevel0, livingEntity1));
        Optional<Mob> $$3 = Optional.empty();
        Optional<Hoglin> $$4 = Optional.empty();
        Optional<Hoglin> $$5 = Optional.empty();
        Optional<Piglin> $$6 = Optional.empty();
        Optional<LivingEntity> $$7 = Optional.empty();
        Optional<Player> $$8 = Optional.empty();
        Optional<Player> $$9 = Optional.empty();
        int $$10 = 0;
        List<AbstractPiglin> $$11 = Lists.newArrayList();
        List<AbstractPiglin> $$12 = Lists.newArrayList();
        NearestVisibleLivingEntities $$13 = (NearestVisibleLivingEntities) $$2.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty());
        for (LivingEntity $$14 : $$13.findAll(p_186157_ -> true)) {
            if ($$14 instanceof Hoglin) {
                Hoglin $$15 = (Hoglin) $$14;
                if ($$15.m_6162_() && $$5.isEmpty()) {
                    $$5 = Optional.of($$15);
                } else if ($$15.isAdult()) {
                    $$10++;
                    if ($$4.isEmpty() && $$15.canBeHunted()) {
                        $$4 = Optional.of($$15);
                    }
                }
            } else if ($$14 instanceof PiglinBrute $$16) {
                $$11.add($$16);
            } else if ($$14 instanceof Piglin) {
                Piglin $$17 = (Piglin) $$14;
                if ($$17.isBaby() && $$6.isEmpty()) {
                    $$6 = Optional.of($$17);
                } else if ($$17.m_34667_()) {
                    $$11.add($$17);
                }
            } else if ($$14 instanceof Player) {
                Player $$18 = (Player) $$14;
                if ($$8.isEmpty() && !PiglinAi.isWearingGold($$18) && livingEntity1.canAttack($$14)) {
                    $$8 = Optional.of($$18);
                }
                if ($$9.isEmpty() && !$$18.isSpectator() && PiglinAi.isPlayerHoldingLovedItem($$18)) {
                    $$9 = Optional.of($$18);
                }
            } else if (!$$3.isEmpty() || !($$14 instanceof WitherSkeleton) && !($$14 instanceof WitherBoss)) {
                if ($$7.isEmpty() && PiglinAi.isZombified($$14.m_6095_())) {
                    $$7 = Optional.of($$14);
                }
            } else {
                $$3 = Optional.of((Mob) $$14);
            }
        }
        for (LivingEntity $$20 : (List) $$2.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES).orElse(ImmutableList.of())) {
            if ($$20 instanceof AbstractPiglin) {
                AbstractPiglin $$21 = (AbstractPiglin) $$20;
                if ($$21.isAdult()) {
                    $$12.add($$21);
                }
            }
        }
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, $$3);
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, $$4);
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, $$5);
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, $$7);
        $$2.setMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, $$8);
        $$2.setMemory(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, $$9);
        $$2.setMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS, $$12);
        $$2.setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, $$11);
        $$2.setMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, $$11.size());
        $$2.setMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, $$10);
    }

    private static Optional<BlockPos> findNearestRepellent(ServerLevel serverLevel0, LivingEntity livingEntity1) {
        return BlockPos.findClosestMatch(livingEntity1.m_20183_(), 8, 4, p_186160_ -> isValidRepellent(serverLevel0, p_186160_));
    }

    private static boolean isValidRepellent(ServerLevel serverLevel0, BlockPos blockPos1) {
        BlockState $$2 = serverLevel0.m_8055_(blockPos1);
        boolean $$3 = $$2.m_204336_(BlockTags.PIGLIN_REPELLENTS);
        return $$3 && $$2.m_60713_(Blocks.SOUL_CAMPFIRE) ? CampfireBlock.isLitCampfire($$2) : $$3;
    }
}