package yesman.epicfight.world.capabilities.entitypatch;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public class GlobalMobPatch extends HurtableEntityPatch<Mob> {

    private int remainStunTime;

    @Override
    protected void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        this.remainStunTime--;
    }

    @Override
    public boolean applyStun(StunType stunType, float stunTime) {
        this.original.f_20900_ = 0.0F;
        this.original.f_20901_ = 0.0F;
        this.original.f_20902_ = 0.0F;
        this.original.m_20334_(0.0, 0.0, 0.0);
        this.cancelKnockback = true;
        this.remainStunTime = (int) (stunTime * 20.0F);
        return true;
    }

    @Override
    public boolean isStunned() {
        return this.remainStunTime > 0 && this.original.m_9236_().getGameRules().getBoolean(EpicFightGamerules.GLOBAL_STUN);
    }
}