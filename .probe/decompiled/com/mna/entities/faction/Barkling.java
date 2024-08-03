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
import com.mna.entities.EntityInit;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.ProjectileHelper;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class Barkling extends BaseFactionMob<Barkling> implements ISpellInteractibleEntity<Barkling> {

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final int PARRY_CD_T1 = 40;

    private static final int PARRY_CD_T2 = 20;

    private int parryCooldown = 0;

    private boolean isActing = false;

    private boolean isMeleeAttacking = false;

    private boolean isParrying = false;

    public Barkling(EntityType<Barkling> type, Level worldIn) {
        super(type, worldIn);
    }

    public Barkling(Level worldIn) {
        this(EntityInit.BARKLING.get(), worldIn);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (this.isActing) {
            if (this.isParrying) {
                builder.thenPlay("animation.barkling.parry");
            } else if (this.f_20912_ == InteractionHand.MAIN_HAND) {
                builder.thenPlay("animation.barkling.attack_1");
            } else {
                builder.thenPlay("animation.barkling.attack_2");
            }
        }
        if (this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F) {
            if (this.m_5448_() != null) {
                builder.thenLoop("animation.barkling.walk_hasTarget");
            } else {
                builder.thenLoop("animation.barkling.walk_noTarget");
            }
        } else if (this.m_5448_() != null) {
            builder.thenLoop("animation.barkling.idle_hasTarget");
        } else {
            builder.thenLoop("animation.barkling.idle_noTarget");
        }
        return state.setAndContinue(builder);
    }

    @Override
    public void tick() {
        super.tick();
        this.parryCooldown--;
    }

    @Override
    public IFaction getFaction() {
        return Factions.FEY;
    }

    @Override
    public void setTarget(LivingEntity pLivingEntity) {
        if (this.m_5448_() != pLivingEntity) {
            super.m_6710_(pLivingEntity);
            if (!this.m_9236_().isClientSide()) {
                ServerMessageDispatcher.sendEntityStateMessage(this);
            }
        }
    }

    @Override
    public void swing(InteractionHand p_226292_1_, boolean p_226292_2_) {
        if (!this.isActing) {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 20, "", this::handleDelayCallback));
            this.isActing = true;
            this.isMeleeAttacking = true;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.m_5496_(SFX.Entity.Generic.WOOSH, 1.0F, (float) (0.9 + Math.random() * 0.2));
        }
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isMeleeAttacking) {
            return true;
        } else {
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 10, entityIn, this::handleDelayCallback));
            this.isMeleeAttacking = false;
            return true;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(1, this, new Barkling.ParryGoal()));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED) * 1.5, false));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.35F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.CHORUS_FLOWER_DEATH;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.AXE_STRIP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.WOOD_PLACE;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isParrying) {
            Entity e = source.getDirectEntity();
            if (source.is(DamageTypes.MOB_PROJECTILE) && e instanceof Projectile) {
                ProjectileHelper.ReflectProjectile(this, (Projectile) e, this.getTier() >= 1, (float) e.getDeltaMovement().length());
                return false;
            }
        }
        if (source.is(DamageTypes.DROWN)) {
            return false;
        } else {
            if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)) {
                amount *= 2.0F;
            }
            return super.hurt(source, amount);
        }
    }

    public boolean damageEntity(Entity entityIn) {
        if (this.m_5448_() == null) {
            return false;
        } else {
            double dist = this.m_20280_(this.m_5448_());
            if (dist > 9.0) {
                return false;
            } else {
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
                    this.m_19970_(this, entityIn);
                    this.m_21335_(entityIn);
                    if (this.f_19796_.nextFloat() < 0.2F && entityIn instanceof Mob) {
                        ((Mob) entityIn).setTarget(this);
                    }
                }
                return flag;
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.45F).add(ForgeMod.SWIM_SPEED.get(), 0.6F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.9);
    }

    private void handleDelayCallback(String identifier, Entity entity) {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            byte var4 = -1;
            switch(identifier.hashCode()) {
                case -1339126929:
                    if (identifier.equals("damage")) {
                        var4 = 0;
                    }
                default:
                    switch(var4) {
                        case 0:
                            this.damageEntity(entity);
                    }
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isActing = false;
            if (this.isParrying) {
                this.parryCooldown = this.getTier() == 1 ? 40 : 20;
                this.isParrying = false;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isActing);
        nbt.putBoolean("parrying", this.isParrying);
        nbt.putInt("target", this.m_5448_() == null ? -1 : this.m_5448_().m_19879_());
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isActing = nbt.getBoolean("attacking");
        this.isParrying = nbt.getBoolean("parrying");
        int targetId = nbt.getInt("target");
        if (targetId == -1) {
            this.setTarget(null);
        }
        Entity e = this.m_9236_().getEntity(targetId);
        if (e instanceof LivingEntity) {
            this.setTarget((LivingEntity) e);
        }
    }

    @Override
    public boolean onShapeTarget(ISpellDefinition spell, SpellSource source) {
        if (!this.isParrying) {
            return false;
        } else {
            Vec3 fwd = Vec3.directionFromRotation(this.m_20155_());
            return ManaAndArtificeMod.getSpellHelper().reflectSpell(this.m_9236_(), this, spell, source, this.m_20182_(), fwd, true);
        }
    }

    public class ParryGoal extends Goal {

        private Entity dodgeEntity;

        public ParryGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        @Override
        public boolean isInterruptable() {
            return false;
        }

        @Override
        public boolean canUse() {
            if (Barkling.this.parryCooldown > 0) {
                return false;
            } else {
                Vec3 rt_startVec = Barkling.this.m_20182_();
                Vec3 thisFwd = Vec3.directionFromRotation(Barkling.this.m_20155_());
                Vec3 rt_endVec = rt_startVec.add(thisFwd.scale(10.0));
                List<Entity> potentials = (List<Entity>) Barkling.this.m_9236_().m_45933_(Barkling.this, Barkling.this.m_20191_().inflate(10.0)).stream().filter(e -> e instanceof Projectile && !e.onGround()).filter(e -> {
                    AABB axisalignedbb = e.getBoundingBox().inflate(5.0);
                    Optional<Vec3> optional = axisalignedbb.clip(rt_startVec, rt_endVec);
                    return optional.isPresent();
                }).collect(Collectors.toList());
                if (potentials.size() == 0) {
                    return false;
                } else {
                    potentials.sort(new Comparator<Entity>() {

                        public int compare(Entity o1, Entity o2) {
                            Double d1 = o1.distanceToSqr(Barkling.this);
                            Double d2 = o2.distanceToSqr(Barkling.this);
                            return d1.compareTo(d2);
                        }
                    });
                    this.dodgeEntity = (Entity) potentials.get(0);
                    return true;
                }
            }
        }

        @Override
        public void start() {
            Barkling.this.m_21573_().stop();
            Barkling.this.isParrying = true;
            Barkling.this.isActing = true;
            Barkling.this.m_21563_().setLookAt(this.dodgeEntity, 30.0F, 30.0F);
            DelayedEventQueue.pushEvent(Barkling.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 70, "", Barkling.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(Barkling.this);
        }

        @Override
        public void tick() {
            Barkling.this.m_21563_().setLookAt(this.dodgeEntity, 30.0F, 30.0F);
        }

        @Override
        public boolean canContinueToUse() {
            return Barkling.this.isParrying;
        }

        protected EntityHitResult rayTraceEntity(Vec3 startVec, Vec3 endVec, float size, Entity searchEntity) {
            return ProjectileUtil.getEntityHitResult(Barkling.this.m_9236_(), Barkling.this, startVec, endVec, Barkling.this.m_20191_().inflate((double) size), entity -> searchEntity == entity);
        }
    }
}