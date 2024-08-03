package net.minecraft.world.entity.ai.goal;

import java.util.function.Predicate;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;

public class BreakDoorGoal extends DoorInteractGoal {

    private static final int DEFAULT_DOOR_BREAK_TIME = 240;

    private final Predicate<Difficulty> validDifficulties;

    protected int breakTime;

    protected int lastBreakProgress = -1;

    protected int doorBreakTime = -1;

    public BreakDoorGoal(Mob mob0, Predicate<Difficulty> predicateDifficulty1) {
        super(mob0);
        this.validDifficulties = predicateDifficulty1;
    }

    public BreakDoorGoal(Mob mob0, int int1, Predicate<Difficulty> predicateDifficulty2) {
        this(mob0, predicateDifficulty2);
        this.doorBreakTime = int1;
    }

    protected int getDoorBreakTime() {
        return Math.max(240, this.doorBreakTime);
    }

    @Override
    public boolean canUse() {
        if (!super.canUse()) {
            return false;
        } else {
            return !this.f_25189_.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? false : this.isValidDifficulty(this.f_25189_.m_9236_().m_46791_()) && !this.m_25200_();
        }
    }

    @Override
    public void start() {
        super.start();
        this.breakTime = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.breakTime <= this.getDoorBreakTime() && !this.m_25200_() && this.f_25190_.m_203195_(this.f_25189_.m_20182_(), 2.0) && this.isValidDifficulty(this.f_25189_.m_9236_().m_46791_());
    }

    @Override
    public void stop() {
        super.m_8041_();
        this.f_25189_.m_9236_().destroyBlockProgress(this.f_25189_.m_19879_(), this.f_25190_, -1);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_25189_.m_217043_().nextInt(20) == 0) {
            this.f_25189_.m_9236_().m_46796_(1019, this.f_25190_, 0);
            if (!this.f_25189_.f_20911_) {
                this.f_25189_.m_6674_(this.f_25189_.m_7655_());
            }
        }
        this.breakTime++;
        int $$0 = (int) ((float) this.breakTime / (float) this.getDoorBreakTime() * 10.0F);
        if ($$0 != this.lastBreakProgress) {
            this.f_25189_.m_9236_().destroyBlockProgress(this.f_25189_.m_19879_(), this.f_25190_, $$0);
            this.lastBreakProgress = $$0;
        }
        if (this.breakTime == this.getDoorBreakTime() && this.isValidDifficulty(this.f_25189_.m_9236_().m_46791_())) {
            this.f_25189_.m_9236_().removeBlock(this.f_25190_, false);
            this.f_25189_.m_9236_().m_46796_(1021, this.f_25190_, 0);
            this.f_25189_.m_9236_().m_46796_(2001, this.f_25190_, Block.getId(this.f_25189_.m_9236_().getBlockState(this.f_25190_)));
        }
    }

    private boolean isValidDifficulty(Difficulty difficulty0) {
        return this.validDifficulties.test(difficulty0);
    }
}