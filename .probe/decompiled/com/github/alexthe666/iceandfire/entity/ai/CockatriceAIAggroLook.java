package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import com.github.alexthe666.iceandfire.entity.EntityGorgon;
import java.util.function.Predicate;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class CockatriceAIAggroLook extends NearestAttackableTargetGoal<Player> {

    private final EntityCockatrice cockatrice;

    private final TargetingConditions predicate;

    private Player player;

    public CockatriceAIAggroLook(EntityCockatrice cockatriceIn) {
        super(cockatriceIn, Player.class, false);
        this.cockatrice = cockatriceIn;
        Predicate<LivingEntity> LIVING_ENTITY_SELECTOR = target -> EntityGorgon.isEntityLookingAt(target, this.cockatrice, 0.6F) && (double) this.cockatrice.m_20270_(target) < this.m_7623_();
        this.predicate = TargetingConditions.forCombat().range(25.0).selector(LIVING_ENTITY_SELECTOR);
    }

    @Override
    public boolean canUse() {
        if (this.cockatrice.m_21824_()) {
            return false;
        } else {
            this.player = this.cockatrice.m_9236_().m_45941_(this.predicate, this.cockatrice.m_20185_(), this.cockatrice.m_20186_(), this.cockatrice.m_20189_());
            return this.player != null;
        }
    }

    @Override
    public void stop() {
        this.player = null;
        super.m_8041_();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.player == null || this.player.isCreative() || this.player.isSpectator()) {
            return this.f_26137_ != null && this.f_26137_.isAlive() || super.m_8045_();
        } else if (!EntityGorgon.isEntityLookingAt(this.player, this.cockatrice, 0.4F)) {
            return false;
        } else {
            this.cockatrice.m_21391_(this.player, 10.0F, 10.0F);
            if (!this.cockatrice.m_21824_()) {
                this.cockatrice.setTargetedEntity(this.player.m_19879_());
                this.cockatrice.m_6710_(this.player);
            }
            return true;
        }
    }
}