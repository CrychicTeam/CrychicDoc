package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDreadKnight;
import com.github.alexthe666.iceandfire.util.IAFMath;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

public class DreadAIRideHorse extends Goal {

    private final EntityDreadKnight knight;

    private AbstractHorse horse;

    @Nonnull
    private List<AbstractHorse> list = IAFMath.emptyAbstractHorseEntityList;

    public DreadAIRideHorse(EntityDreadKnight knight) {
        this.knight = knight;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.knight.m_20159_()) {
            this.list = IAFMath.emptyAbstractHorseEntityList;
            return false;
        } else {
            if (this.knight.m_9236_().getGameTime() % 4L == 0L) {
                this.list = this.knight.m_9236_().m_6443_(AbstractHorse.class, this.knight.m_20191_().inflate(16.0, 7.0, 16.0), entity -> !entity.m_20160_());
            }
            if (this.list.isEmpty()) {
                return false;
            } else {
                this.horse = (AbstractHorse) this.list.get(0);
                return true;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.knight.m_20159_() && this.horse != null && !this.horse.m_20160_();
    }

    @Override
    public void start() {
        this.horse.m_21573_().stop();
    }

    @Override
    public void stop() {
        this.horse = null;
        this.knight.m_21573_().stop();
    }

    @Override
    public void tick() {
        this.knight.m_21563_().setLookAt(this.horse, 30.0F, 30.0F);
        this.knight.m_21573_().moveTo(this.horse, 1.2);
        if (this.knight.m_20280_(this.horse) < 4.0) {
            this.horse.setTamed(true);
            this.knight.m_21573_().stop();
            this.knight.m_20329_(this.horse);
        }
    }
}