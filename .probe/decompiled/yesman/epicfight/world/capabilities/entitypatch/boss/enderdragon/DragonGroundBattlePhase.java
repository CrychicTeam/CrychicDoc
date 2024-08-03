package yesman.epicfight.world.capabilities.entitypatch.boss.enderdragon;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.utils.math.MathUtils;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class DragonGroundBattlePhase extends PatchedDragonPhase {

    private final List<Player> recognizedPlayers = Lists.newArrayList();

    private PathFinder pathFinder;

    private int aggroCounter;

    private int noPathWarningCounter;

    CombatBehaviors<EnderDragonPatch> combatBehaviors;

    public DragonGroundBattlePhase(EnderDragon dragon) {
        super(dragon);
        if (!dragon.m_9236_().isClientSide()) {
            this.combatBehaviors = MobCombatBehaviors.ENDER_DRAGON.build(this.dragonpatch);
            NodeEvaluator nodeEvaluator = new WalkNodeEvaluator();
            nodeEvaluator.setCanPassDoors(true);
            this.pathFinder = new PathFinder(nodeEvaluator, 100);
        }
    }

    @Override
    public void begin() {
        this.dragonpatch.setGroundPhase();
    }

    @Override
    public void doServerTick() {
        LivingEntity target = this.f_31176_.m_5448_();
        if (target != null) {
            if (isValidTarget(target) && isInEndSpikes(target)) {
                EntityState state = this.dragonpatch.getEntityState();
                this.combatBehaviors.tick();
                this.aggroCounter--;
                if (this.combatBehaviors.hasActivatedMove()) {
                    if (state.canBasicAttack()) {
                        CombatBehaviors.Behavior<EnderDragonPatch> result = this.combatBehaviors.tryProceed();
                        if (result != null) {
                            result.execute(this.dragonpatch);
                        }
                    }
                } else if (!state.inaction()) {
                    CombatBehaviors.Behavior<EnderDragonPatch> result = this.combatBehaviors.selectRandomBehaviorSeries();
                    if (result != null) {
                        result.execute(this.dragonpatch);
                    } else {
                        if (this.f_31176_.f_19797_ % 20 == 0) {
                            if (!this.checkTargetPath(target)) {
                                if (this.noPathWarningCounter++ >= 3) {
                                    this.fly();
                                }
                            } else {
                                this.noPathWarningCounter = 0;
                            }
                        }
                        double dx = target.m_20185_() - this.f_31176_.m_20185_();
                        double dz = target.m_20189_() - this.f_31176_.m_20189_();
                        float yRot = 180.0F - (float) Math.toDegrees(Mth.atan2(dx, dz));
                        this.f_31176_.m_146922_(MathUtils.rotlerp(this.f_31176_.m_146908_(), yRot, 6.0F));
                        Vec3 forward = this.f_31176_.m_20156_().scale(-0.25);
                        this.f_31176_.m_6478_(MoverType.SELF, forward);
                    }
                } else if (this.aggroCounter < 0) {
                    this.aggroCounter = 200;
                    this.searchNearestTarget();
                }
            } else if (!this.dragonpatch.getEntityState().inaction()) {
                this.searchNearestTarget();
            }
        } else {
            this.searchNearestTarget();
            if (this.f_31176_.m_5448_() == null && !this.dragonpatch.getEntityState().inaction()) {
                this.dragonpatch.playAnimationSynchronized(Animations.DRAGON_GROUND_TO_FLY, 0.0F);
                this.f_31176_.getPhaseManager().setPhase(PatchedPhases.FLYING);
                ((DragonFlyingPhase) this.f_31176_.getPhaseManager().getCurrentPhase()).enableAirstrike();
            }
        }
    }

    @Override
    public float onHurt(DamageSource damagesource, float amount) {
        if (damagesource.is(DamageTypeTags.IS_PROJECTILE)) {
            if (damagesource.getDirectEntity() instanceof AbstractArrow) {
                damagesource.getDirectEntity().setSecondsOnFire(1);
            }
            return 0.0F;
        } else {
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(damagesource.getEntity(), LivingEntityPatch.class);
            return damagesource.getEntity() == null || entitypatch != null && entitypatch.getEpicFightDamageSource() != null ? super.m_7584_(damagesource, amount) : 0.0F;
        }
    }

    private void refreshNearbyPlayers(double within) {
        this.recognizedPlayers.clear();
        this.recognizedPlayers.addAll(this.getPlayersNearbyWithin(within));
    }

    private boolean checkTargetPath(LivingEntity target) {
        BlockPos blockpos = this.f_31176_.m_20183_();
        while (this.f_31176_.m_9236_().getBlockState(blockpos).m_280555_()) {
            blockpos = blockpos.above();
        }
        while (!this.f_31176_.m_9236_().getBlockState(blockpos.below()).m_280555_()) {
            blockpos = blockpos.below();
        }
        int sight = 60;
        PathNavigationRegion pathnavigationregion = new PathNavigationRegion(this.f_31176_.m_9236_(), blockpos.offset(-sight, -sight, -sight), blockpos.offset(sight, sight, sight));
        Path path = this.pathFinder.findPath(pathnavigationregion, this.f_31176_, ImmutableSet.of(target.m_20183_()), (float) sight, 0, 1.0F);
        BlockPos pathEnd = path.getNode(path.getNodeCount() - 1).asBlockPos();
        BlockPos targetPos = path.getTarget();
        double xd = (double) Math.abs(pathEnd.m_123341_() - targetPos.m_123341_());
        double yd = (double) Math.abs(pathEnd.m_123342_() - targetPos.m_123342_());
        double zd = (double) Math.abs(pathEnd.m_123343_() - targetPos.m_123343_());
        return xd < (double) this.f_31176_.m_20205_() && yd < (double) this.f_31176_.m_20206_() && zd < (double) this.f_31176_.m_20205_();
    }

    private void searchNearestTarget() {
        this.refreshNearbyPlayers(60.0);
        if (this.recognizedPlayers.size() > 0) {
            int nearestPlayerIndex = 0;
            double nearestDistance = ((Player) this.recognizedPlayers.get(0)).m_20280_(this.f_31176_);
            for (int i = 1; i < this.recognizedPlayers.size(); i++) {
                double distance = ((Player) this.recognizedPlayers.get(i)).m_20280_(this.f_31176_);
                if (distance < nearestDistance) {
                    nearestPlayerIndex = i;
                    nearestDistance = distance;
                }
            }
            Player nearestPlayer = (Player) this.recognizedPlayers.get(nearestPlayerIndex);
            if (isValidTarget(nearestPlayer) && isInEndSpikes(nearestPlayer)) {
                this.dragonpatch.setAttakTargetSync(nearestPlayer);
                return;
            }
        }
        this.dragonpatch.setAttakTargetSync(null);
    }

    public void fly() {
        this.combatBehaviors.execute(6);
    }

    public void resetFlyCooldown() {
        this.combatBehaviors.resetCooldown(6, false);
    }

    @Override
    public boolean isSitting() {
        return true;
    }

    @Override
    public EnderDragonPhase<? extends DragonPhaseInstance> getPhase() {
        return PatchedPhases.GROUND_BATTLE;
    }
}