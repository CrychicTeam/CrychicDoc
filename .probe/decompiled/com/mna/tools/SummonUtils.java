package com.mna.tools;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.entities.ISummonHelper;
import com.mna.api.entities.ai.CastSpellAtTargetGoal;
import com.mna.api.faction.IFaction;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.ai.FollowOwnerGoal;
import com.mna.entities.ai.NeverTargetOwnerGoal;
import com.mna.entities.ai.SummonMeleeFallbackGoal;
import com.mna.entities.ai.TargetDefendOwnerGoal;
import com.mna.factions.Factions;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableInt;

public class SummonUtils implements ISummonHelper {

    public static final String TAG_SUMMON_IDS = "mna:summon_ids";

    public static final String TAG_BONUS_SUMMONS = "mna:bonus_summon_count";

    public static final String TAG_EMBER_IDS = "mna:ember_ids";

    public static boolean isSummon(Entity candidate) {
        return candidate != null && candidate.getPersistentData().contains("mna:summon_tag");
    }

    private static int getSummonerID(Entity candidate) {
        return !isSummon(candidate) ? -1 : candidate.getPersistentData().getInt("mna:summon_tag");
    }

    @Nullable
    public static LivingEntity getSummoner(LivingEntity candidate) {
        if (!isSummon(candidate)) {
            return null;
        } else {
            int summonerID = candidate.getPersistentData().getInt("mna:summon_tag");
            Entity e = candidate.m_9236_().getEntity(summonerID);
            return e instanceof LivingEntity ? (LivingEntity) e : null;
        }
    }

    @Nullable
    public static Player getSummonerAsPlayer(LivingEntity candidate) {
        if (!isSummon(candidate)) {
            return null;
        } else {
            int summonerID = candidate.getPersistentData().getInt("mna:summon_tag");
            Entity e = candidate.m_9236_().getEntity(summonerID);
            return e instanceof Player ? (Player) e : null;
        }
    }

    public static void tagAsSummon(Entity entity, LivingEntity owner) {
        entity.getPersistentData().putInt("mna:summon_tag", owner.m_19879_());
    }

    public static boolean setSummon(Mob candidate, LivingEntity owner, int duration) {
        return setSummon(candidate, owner, true, duration);
    }

    public static boolean setSummon(Mob candidate, LivingEntity owner, boolean overrideAI, int duration) {
        if (isSummon(candidate)) {
            return false;
        } else {
            tagAsSummon(candidate, owner);
            if (duration > 0) {
                candidate.getPersistentData().putBoolean("mna:deathflag", true);
                candidate.m_7292_(new MobEffectInstance(EffectInit.TIMED_DEATH.get(), duration));
            }
            if (overrideAI) {
                candidate.goalSelector.addGoal(0, new FollowOwnerGoal(candidate, 1.35, 8.0F, 20.0F, 32.0F, true));
                candidate.targetSelector.availableGoals.clear();
                candidate.targetSelector.addGoal(0, new NeverTargetOwnerGoal(candidate));
                candidate.targetSelector.addGoal(1, new TargetDefendOwnerGoal(candidate, LivingEntity.class, 10, true, false));
                if (!candidate.goalSelector.availableGoals.stream().anyMatch(g -> g.getGoal() instanceof MeleeAttackGoal || g.getGoal() instanceof CastSpellAtTargetGoal || g.getGoal() instanceof RangedAttackGoal || g.getGoal() instanceof RangedBowAttackGoal || g.getGoal() instanceof RangedCrossbowAttackGoal)) {
                    candidate.goalSelector.addGoal(1, new SummonMeleeFallbackGoal(candidate, 1.35F, false));
                }
                if (candidate instanceof Pillager) {
                    candidate.goalSelector.availableGoals.removeIf(g -> g.getPriority() == 2);
                }
            }
            return true;
        }
    }

    public static List<Mob> getSummons(LivingEntity summoner) {
        return getSummons(summoner, summoner.m_9236_());
    }

    public static List<Mob> getSummons(LivingEntity summoner, Level world) {
        LinkedList<Mob> summons = new LinkedList();
        if (summoner != null && world != null && summoner.getPersistentData().contains("mna:summon_ids")) {
            int[] ids = summoner.getPersistentData().getIntArray("mna:summon_ids");
            for (int i = 0; i < ids.length; i++) {
                Entity e = world.getEntity(ids[i]);
                if (e != null && e instanceof Mob && isSummon(e) && getSummonerID(e) == summoner.m_19879_() && e.isAlive()) {
                    summons.push((Mob) e);
                }
            }
            ArrayList<Integer> idList = new ArrayList();
            summons.forEach(s -> idList.add(s.m_19879_()));
            summoner.getPersistentData().putIntArray("mna:summon_ids", idList);
        }
        return summons;
    }

    public static boolean isTargetFriendly(Entity target, LivingEntity summoner) {
        if (target != null && summoner != null) {
            if (target instanceof TamableAnimal) {
                summoner = ((TamableAnimal) target).m_269323_();
            }
            if (summoner == null) {
                return true;
            } else if (target == summoner) {
                return true;
            } else if (target != summoner.getLastHurtMob() && target != summoner.getLastHurtByMob()) {
                if (target instanceof LivingEntity living) {
                    LivingEntity livingSummoner = getSummoner(living);
                    if (livingSummoner != null) {
                        if (livingSummoner.m_19879_() == summoner.m_19879_()) {
                            return true;
                        }
                        return isTargetFriendly(livingSummoner, summoner);
                    }
                    if (!(summoner instanceof Player)) {
                        if (living instanceof Mob && ((Mob) living).getTarget() == summoner) {
                            return false;
                        }
                        if (living instanceof IFactionEnemy && summoner instanceof IFactionEnemy) {
                            return ((IFactionEnemy) living).getFaction() == ((IFactionEnemy) summoner).getFaction();
                        }
                        return !(living instanceof Player);
                    }
                    IPlayerProgression prog = (IPlayerProgression) summoner.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                    if (prog != null && living instanceof IFactionEnemy) {
                        IFaction livingFaction = ((IFactionEnemy) living).getFaction();
                        IFaction summonerFaction = prog.getAlliedFaction();
                        if (livingFaction != null && summonerFaction != null) {
                            if (summonerFaction.getEnemyFactions().contains(livingFaction)) {
                                return false;
                            }
                            if (summonerFaction.getAlliedFactions().contains(livingFaction)) {
                                return true;
                            }
                        }
                    }
                    if (living.m_5647_() != null && living.m_5647_().isAlliedTo(summoner.m_5647_())) {
                        return true;
                    }
                    if (living instanceof Monster) {
                        return false;
                    }
                    if (living instanceof Mob && ((Mob) living).getTarget() == summoner) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static void clampTrackedEntities(LivingEntity summoner) {
        clampTrackedEntities(summoner, "mna:summon_ids", SummonUtils::getMaxSummons, SummonUtils::isValidEntity);
    }

    public static void clampTrackedEntities(LivingEntity summoner, String key, Function<LivingEntity, Integer> maximumEvaluator, BiFunction<Entity, LivingEntity, Boolean> validator) {
        int maxSummons = (Integer) maximumEvaluator.apply(summoner);
        countAndCleanupTrackedEntities(summoner, key);
        LinkedList<Integer> entityIDs = new LinkedList();
        if (summoner.getPersistentData().contains(key)) {
            int[] ids = summoner.getPersistentData().getIntArray(key);
            for (int i = 0; i < ids.length; i++) {
                entityIDs.add(ids[i]);
            }
            while (entityIDs.size() > maxSummons) {
                Entity e = summoner.m_9236_().getEntity((Integer) entityIDs.pop());
                if (e != null && (Boolean) validator.apply(e, summoner)) {
                    e.discard();
                }
            }
        }
    }

    public static int countTrackedEntities(LivingEntity summoner, String key) {
        if (summoner.getPersistentData().contains(key)) {
            int[] ids = summoner.getPersistentData().getIntArray(key);
            return ids.length;
        } else {
            return 0;
        }
    }

    private static int countAndCleanupTrackedEntities(LivingEntity summoner, String key) {
        if (summoner.getPersistentData().contains(key)) {
            int[] ids = summoner.getPersistentData().getIntArray(key);
            ArrayList<Integer> idList = new ArrayList();
            for (int i : ids) {
                Entity e = summoner.m_9236_().getEntity(i);
                if (e != null && e.isAlive() && isSummon(e) && getSummonerID(e) == summoner.m_19879_()) {
                    idList.add(i);
                }
            }
            summoner.getPersistentData().putIntArray(key, idList);
            return idList.size();
        } else {
            return 0;
        }
    }

    public static boolean isTracked(LivingEntity summoner, Entity e, String key) {
        if (summoner.getPersistentData().contains(key)) {
            int[] ids = summoner.getPersistentData().getIntArray(key);
            for (int i : ids) {
                if (i == e.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isFirstTrackedEntity(LivingEntity summoner, String key, Entity e) {
        countAndCleanupTrackedEntities(summoner, key);
        if (summoner.getPersistentData().contains(key)) {
            int[] ids = summoner.getPersistentData().getIntArray(key);
            if (ids.length > 0) {
                return ids[0] == e.getId();
            }
        }
        return false;
    }

    public static void addTrackedEntity(LivingEntity summoner, Entity summon) {
        addTrackedEntity(summoner, summon, "mna:summon_ids");
    }

    public static void addTrackedEntity(LivingEntity summoner, Entity entity, String key) {
        LinkedList<Integer> entityIDs = new LinkedList();
        if (summoner.getPersistentData().contains(key)) {
            int[] ids = summoner.getPersistentData().getIntArray(key);
            for (int i = 0; i < ids.length; i++) {
                entityIDs.add(ids[i]);
            }
        }
        entityIDs.add(entity.getId());
        summoner.getPersistentData().putIntArray(key, entityIDs);
    }

    public static int getMaxSummons(LivingEntity summoner) {
        MutableInt max_summons = new MutableInt(2);
        summoner.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
            if (p.getAlliedFaction() == Factions.UNDEAD) {
                max_summons.add(p.getTier() - 2);
            }
        });
        int bonus = summoner.getPersistentData().getInt("mna:bonus_summon_count");
        max_summons.add(bonus);
        return max_summons.getValue();
    }

    public static boolean isValidEntity(Entity e, LivingEntity living) {
        return e instanceof LivingEntity && isSummon(e) && getSummoner((LivingEntity) e) == living;
    }

    public static int getMaxEmbers(LivingEntity living) {
        MutableInt max_embers = new MutableInt(3);
        living.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
            if (p.getAlliedFaction() == Factions.COUNCIL) {
                max_embers.add(p.getTier() - 2);
            }
        });
        return max_embers.getValue();
    }

    public static void iterateTrackedEntities(LivingEntity living, String key, Consumer<Integer> iterator) {
        if (living.getPersistentData().contains(key)) {
            int[] ids = living.getPersistentData().getIntArray(key);
            for (int i : ids) {
                iterator.accept(i);
            }
        }
    }

    public static void setBonusSummons(LivingEntity living, int bonus) {
        living.getPersistentData().putInt("mna:bonus_summon_count", bonus);
    }

    @Override
    public boolean checkIsSummon(Entity candidate) {
        return isSummon(candidate);
    }

    @Override
    public LivingEntity resolveSummoner(LivingEntity candidate) {
        return getSummoner(candidate);
    }

    @Override
    public boolean makeSummon(Mob candidate, LivingEntity owner, int duration) {
        return setSummon(candidate, owner, duration);
    }

    @Override
    public boolean makeSummon(Mob candidate, LivingEntity owner, boolean overrideAI, int duration) {
        return setSummon(candidate, owner, overrideAI, duration);
    }

    @Override
    public List<Mob> getAllSummons(LivingEntity summoner) {
        return getSummons(summoner);
    }

    @Override
    public boolean isEntityFriendly(Entity target, LivingEntity summoner) {
        return isTargetFriendly(target, summoner);
    }

    @Override
    public int getSummonCap(LivingEntity summoner) {
        return getMaxSummons(summoner);
    }

    @Override
    public int getEmberCap(LivingEntity living) {
        return getMaxEmbers(living);
    }

    @Override
    public void setSummonBonus(LivingEntity living, int bonus) {
        setBonusSummons(living, bonus);
    }
}