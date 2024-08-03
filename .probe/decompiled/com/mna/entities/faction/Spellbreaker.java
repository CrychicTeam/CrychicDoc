package com.mna.entities.faction;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.entities.IFactionEnemy;
import com.mna.api.entities.ISpellInteractibleEntity;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.effects.EffectInit;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.boss.BossMonster;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import java.util.EnumSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class Spellbreaker extends BaseFactionMob<Spellbreaker> implements ISpellInteractibleEntity<Spellbreaker> {

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final String INSTRUCTION_SILENCE = "silence";

    private static final String INSTRUCTION_RALLY = "rally";

    private static final int SHIELD_BASH_CD = 300;

    private static final int RALLY_CD = 3600;

    private int shieldBashCooldown = 0;

    private int rallyCooldown = 0;

    private boolean isActing = false;

    private boolean isMeleeAttacking = false;

    private boolean isShieldBashing = false;

    private boolean isRallying = false;

    public Spellbreaker(EntityType<Spellbreaker> type, Level worldIn) {
        super(type, worldIn);
    }

    @Override
    public IFaction getFaction() {
        return Factions.COUNCIL;
    }

    @Override
    public boolean isBlocking() {
        return !this.isActing;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        if (!this.isActing) {
            return this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.combat_walk")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.model.combat_idle"));
        } else if (this.isShieldBashing) {
            return state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.combat_shield_bash"));
        } else {
            return this.isRallying ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.combat_rally")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.model.combat_stab"));
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.shieldBashCooldown--;
        this.rallyCooldown--;
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(2, this, new Spellbreaker.RallyGoal()));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), false));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.35F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public void swing(InteractionHand p_226292_1_, boolean p_226292_2_) {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", this::handleDelayCallback));
            this.isActing = true;
            this.isMeleeAttacking = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isMeleeAttacking) {
            return true;
        } else {
            if (this.tier >= 2 && this.shieldBashCooldown <= 0) {
                this.isShieldBashing = true;
                DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("silence", 20, entityIn, this::handleDelayCallback));
            } else {
                DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 20, entityIn, this::handleDelayCallback));
            }
            this.isMeleeAttacking = false;
            return true;
        }
    }

    public boolean damageEntity(Entity entityIn) {
        float f = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
        if (entityIn instanceof LivingEntity) {
            f += EnchantmentHelper.getDamageBonus(this.m_21205_(), ((LivingEntity) entityIn).getMobType());
            f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
        }
        int i = EnchantmentHelper.getFireAspect(this);
        if (i > 0) {
            entityIn.setSecondsOnFire(i * 4);
        }
        boolean flag = entityIn.hurt(this.m_269291_().mobAttack(this), f);
        if (flag) {
            if (f1 > 0.0F && entityIn instanceof LivingEntity) {
                ((LivingEntity) entityIn).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
            }
            if (entityIn instanceof Player playerentity) {
                this.maybeDisableShield(playerentity, this.m_21205_(), playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
            }
            this.m_19970_(this, entityIn);
            this.m_21335_(entityIn);
            if (this.f_19796_.nextFloat() < 0.4F && entityIn instanceof Mob) {
                ((Mob) entityIn).setTarget(this);
            }
        }
        return flag;
    }

    private void maybeDisableShield(Player p_233655_1_, ItemStack p_233655_2_, ItemStack p_233655_3_) {
        if (!p_233655_2_.isEmpty() && !p_233655_3_.isEmpty() && p_233655_2_.getItem() instanceof AxeItem && p_233655_3_.getItem() == Items.SHIELD) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.f_19796_.nextFloat() < f) {
                p_233655_1_.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.m_9236_().broadcastEntityEvent(p_233655_1_, (byte) 30);
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0).add(Attributes.MOVEMENT_SPEED, 0.55F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    private void handleDelayCallback(String identifier, Entity entity) {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            switch(identifier) {
                case "damage":
                    this.damageEntity(entity);
                    this.m_5496_(SFX.Entity.Generic.WOOSH, 1.0F, (float) (0.9 + Math.random() * 0.2F));
                    break;
                case "silence":
                    this.damageEntity(entity);
                    if (entity instanceof Player) {
                        ((Player) entity).m_7292_(new MobEffectInstance(EffectInit.SILENCE.get(), 100));
                    }
                    this.m_5496_(SFX.Entity.Spellbreaker.SHIELD_BASH, 1.0F, (float) (0.9 + Math.random() * 0.2F));
                    break;
                case "rally":
                    this.m_5496_(SFX.Entity.Spellbreaker.RALLY, 1.0F, 1.0F);
                    for (Monster m : this.m_9236_().m_6443_(Monster.class, this.m_20191_().inflate(10.0), e -> e.m_6084_() && e instanceof Spellbreaker)) {
                        m.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, false, true));
                        m.m_7292_(new MobEffectInstance(MobEffects.REGENERATION, 100, 1, false, false));
                        m.m_7292_(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 2, false, true));
                    }
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isActing = false;
            if (this.isShieldBashing) {
                this.shieldBashCooldown = 300;
                this.isShieldBashing = false;
            } else if (this.isRallying) {
                this.rallyCooldown = 3600;
                this.isRallying = false;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isActing);
        nbt.putBoolean("shield_bashing", this.isShieldBashing);
        nbt.putBoolean("rallying", this.isRallying);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isActing = nbt.getBoolean("attacking");
        this.isShieldBashing = nbt.getBoolean("shield_bashing");
        this.isRallying = nbt.getBoolean("rallying");
    }

    @Override
    public boolean onShapeTarget(ISpellDefinition spell, SpellSource source) {
        if (source.getCaster() instanceof BossMonster && !(Math.random() < 0.25)) {
            return true;
        } else {
            Vec3 fwd = Vec3.directionFromRotation(this.m_20155_());
            return ManaAndArtificeMod.getSpellHelper().reflectSpell(this.m_9236_(), this, spell, source, this.m_20182_(), fwd, true);
        }
    }

    public class RallyGoal extends Goal {

        public RallyGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return Spellbreaker.this.rallyCooldown <= 0 && Spellbreaker.this.m_5448_() != null && Spellbreaker.this.m_21223_() < Spellbreaker.this.m_21233_() * 0.75F && !Spellbreaker.this.m_21023_(MobEffects.REGENERATION) && !Spellbreaker.this.m_21023_(MobEffects.DAMAGE_RESISTANCE) && !Spellbreaker.this.m_21023_(MobEffects.DAMAGE_BOOST) && !Spellbreaker.this.isActing;
        }

        @Override
        public void start() {
            Spellbreaker.this.m_21573_().stop();
            Spellbreaker.this.isRallying = true;
            Spellbreaker.this.isActing = true;
            DelayedEventQueue.pushEvent(Spellbreaker.this.m_9236_(), new TimedDelayedEvent<>("rally", 20, Spellbreaker.this, Spellbreaker.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(Spellbreaker.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 40, "", Spellbreaker.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(Spellbreaker.this);
        }

        @Override
        public boolean canContinueToUse() {
            return Spellbreaker.this.isRallying;
        }
    }
}