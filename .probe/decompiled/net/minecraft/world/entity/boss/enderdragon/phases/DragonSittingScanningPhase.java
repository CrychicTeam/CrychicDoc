package net.minecraft.world.entity.boss.enderdragon.phases;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;

public class DragonSittingScanningPhase extends AbstractDragonSittingPhase {

    private static final int SITTING_SCANNING_IDLE_TICKS = 100;

    private static final int SITTING_ATTACK_Y_VIEW_RANGE = 10;

    private static final int SITTING_ATTACK_VIEW_RANGE = 20;

    private static final int SITTING_CHARGE_VIEW_RANGE = 150;

    private static final TargetingConditions CHARGE_TARGETING = TargetingConditions.forCombat().range(150.0);

    private final TargetingConditions scanTargeting;

    private int scanningTime;

    public DragonSittingScanningPhase(EnderDragon enderDragon0) {
        super(enderDragon0);
        this.scanTargeting = TargetingConditions.forCombat().range(20.0).selector(p_289455_ -> Math.abs(p_289455_.m_20186_() - enderDragon0.m_20186_()) <= 10.0);
    }

    @Override
    public void doServerTick() {
        this.scanningTime++;
        LivingEntity $$0 = this.f_31176_.m_9236_().m_45949_(this.scanTargeting, this.f_31176_, this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
        if ($$0 != null) {
            if (this.scanningTime > 25) {
                this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.SITTING_ATTACKING);
            } else {
                Vec3 $$1 = new Vec3($$0.m_20185_() - this.f_31176_.m_20185_(), 0.0, $$0.m_20189_() - this.f_31176_.m_20189_()).normalize();
                Vec3 $$2 = new Vec3((double) Mth.sin(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)), 0.0, (double) (-Mth.cos(this.f_31176_.m_146908_() * (float) (Math.PI / 180.0)))).normalize();
                float $$3 = (float) $$2.dot($$1);
                float $$4 = (float) (Math.acos((double) $$3) * 180.0F / (float) Math.PI) + 0.5F;
                if ($$4 < 0.0F || $$4 > 10.0F) {
                    double $$5 = $$0.m_20185_() - this.f_31176_.head.m_20185_();
                    double $$6 = $$0.m_20189_() - this.f_31176_.head.m_20189_();
                    double $$7 = Mth.clamp(Mth.wrapDegrees(180.0 - Mth.atan2($$5, $$6) * 180.0F / (float) Math.PI - (double) this.f_31176_.m_146908_()), -100.0, 100.0);
                    this.f_31176_.yRotA *= 0.8F;
                    float $$8 = (float) Math.sqrt($$5 * $$5 + $$6 * $$6) + 1.0F;
                    float $$9 = $$8;
                    if ($$8 > 40.0F) {
                        $$8 = 40.0F;
                    }
                    this.f_31176_.yRotA += (float) $$7 * (0.7F / $$8 / $$9);
                    this.f_31176_.m_146922_(this.f_31176_.m_146908_() + this.f_31176_.yRotA);
                }
            }
        } else if (this.scanningTime >= 100) {
            $$0 = this.f_31176_.m_9236_().m_45949_(CHARGE_TARGETING, this.f_31176_, this.f_31176_.m_20185_(), this.f_31176_.m_20186_(), this.f_31176_.m_20189_());
            this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.TAKEOFF);
            if ($$0 != null) {
                this.f_31176_.getPhaseManager().setPhase(EnderDragonPhase.CHARGING_PLAYER);
                this.f_31176_.getPhaseManager().getPhase(EnderDragonPhase.CHARGING_PLAYER).setTarget(new Vec3($$0.m_20185_(), $$0.m_20186_(), $$0.m_20189_()));
            }
        }
    }

    @Override
    public void begin() {
        this.scanningTime = 0;
    }

    @Override
    public EnderDragonPhase<DragonSittingScanningPhase> getPhase() {
        return EnderDragonPhase.SITTING_SCANNING;
    }
}