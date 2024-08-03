package noppes.npcs.ai;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.DoorInteractGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

public class EntityAIBustDoor extends DoorInteractGoal {

    private int breakingTime;

    private int field_75358_j = -1;

    public EntityAIBustDoor(Mob par1EntityLiving) {
        super(par1EntityLiving);
    }

    @Override
    public boolean canUse() {
        return !super.canUse() ? false : !this.m_25200_();
    }

    @Override
    public void start() {
        super.start();
        this.breakingTime = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.breakingTime <= 240 && !this.m_25200_() && this.f_25189_.m_20183_().m_123331_(this.f_25190_) < 4.0;
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
            this.f_25189_.m_9236_().m_5898_((Player) null, 1010, this.f_25190_, 0);
            this.f_25189_.m_6674_(InteractionHand.MAIN_HAND);
        }
        this.breakingTime++;
        int var1 = (int) ((float) this.breakingTime / 240.0F * 10.0F);
        if (var1 != this.field_75358_j) {
            this.f_25189_.m_9236_().destroyBlockProgress(this.f_25189_.m_19879_(), this.f_25190_, var1);
            this.field_75358_j = var1;
        }
        if (this.breakingTime == 240) {
            this.f_25189_.m_9236_().removeBlock(this.f_25190_, false);
            this.f_25189_.m_9236_().m_5898_((Player) null, 1012, this.f_25190_, 0);
            this.f_25189_.m_9236_().m_5898_((Player) null, 2001, this.f_25190_, Block.getId(this.f_25189_.m_9236_().getBlockState(this.f_25190_)));
        }
    }
}