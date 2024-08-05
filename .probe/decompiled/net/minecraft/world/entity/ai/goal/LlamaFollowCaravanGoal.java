package net.minecraft.world.entity.ai.goal;

import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.phys.Vec3;

public class LlamaFollowCaravanGoal extends Goal {

    public final Llama llama;

    private double speedModifier;

    private static final int CARAVAN_LIMIT = 8;

    private int distCheckCounter;

    public LlamaFollowCaravanGoal(Llama llama0, double double1) {
        this.llama = llama0;
        this.speedModifier = double1;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.llama.m_21523_() && !this.llama.inCaravan()) {
            List<Entity> $$0 = this.llama.m_9236_().getEntities(this.llama, this.llama.m_20191_().inflate(9.0, 4.0, 9.0), p_25505_ -> {
                EntityType<?> $$1x = p_25505_.getType();
                return $$1x == EntityType.LLAMA || $$1x == EntityType.TRADER_LLAMA;
            });
            Llama $$1 = null;
            double $$2 = Double.MAX_VALUE;
            for (Entity $$3 : $$0) {
                Llama $$4 = (Llama) $$3;
                if ($$4.inCaravan() && !$$4.hasCaravanTail()) {
                    double $$5 = this.llama.m_20280_($$4);
                    if (!($$5 > $$2)) {
                        $$2 = $$5;
                        $$1 = $$4;
                    }
                }
            }
            if ($$1 == null) {
                for (Entity $$6 : $$0) {
                    Llama $$7 = (Llama) $$6;
                    if ($$7.m_21523_() && !$$7.hasCaravanTail()) {
                        double $$8 = this.llama.m_20280_($$7);
                        if (!($$8 > $$2)) {
                            $$2 = $$8;
                            $$1 = $$7;
                        }
                    }
                }
            }
            if ($$1 == null) {
                return false;
            } else if ($$2 < 4.0) {
                return false;
            } else if (!$$1.m_21523_() && !this.firstIsLeashed($$1, 1)) {
                return false;
            } else {
                this.llama.joinCaravan($$1);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.llama.inCaravan() && this.llama.getCaravanHead().m_6084_() && this.firstIsLeashed(this.llama, 0)) {
            double $$0 = this.llama.m_20280_(this.llama.getCaravanHead());
            if ($$0 > 676.0) {
                if (this.speedModifier <= 3.0) {
                    this.speedModifier *= 1.2;
                    this.distCheckCounter = m_186073_(40);
                    return true;
                }
                if (this.distCheckCounter == 0) {
                    return false;
                }
            }
            if (this.distCheckCounter > 0) {
                this.distCheckCounter--;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void stop() {
        this.llama.leaveCaravan();
        this.speedModifier = 2.1;
    }

    @Override
    public void tick() {
        if (this.llama.inCaravan()) {
            if (!(this.llama.m_21524_() instanceof LeashFenceKnotEntity)) {
                Llama $$0 = this.llama.getCaravanHead();
                double $$1 = (double) this.llama.m_20270_($$0);
                float $$2 = 2.0F;
                Vec3 $$3 = new Vec3($$0.m_20185_() - this.llama.m_20185_(), $$0.m_20186_() - this.llama.m_20186_(), $$0.m_20189_() - this.llama.m_20189_()).normalize().scale(Math.max($$1 - 2.0, 0.0));
                this.llama.m_21573_().moveTo(this.llama.m_20185_() + $$3.x, this.llama.m_20186_() + $$3.y, this.llama.m_20189_() + $$3.z, this.speedModifier);
            }
        }
    }

    private boolean firstIsLeashed(Llama llama0, int int1) {
        if (int1 > 8) {
            return false;
        } else if (llama0.inCaravan()) {
            return llama0.getCaravanHead().m_21523_() ? true : this.firstIsLeashed(llama0.getCaravanHead(), ++int1);
        } else {
            return false;
        }
    }
}