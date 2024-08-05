package com.mna.entities.faction;

import com.mna.api.entities.IFactionEnemy;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.entities.EntityInit;
import com.mna.entities.ai.FactionTierWrapperGoal;
import com.mna.entities.ai.LerpLeap;
import com.mna.entities.faction.base.BaseFactionMob;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class DemonImp extends BaseFactionMob<DemonImp> {

    private static final int ACTION_LEAP = 1000;

    public DemonImp(EntityType<DemonImp> type, Level worldIn) {
        super(type, worldIn);
    }

    public DemonImp(Level worldIn) {
        this(EntityInit.DEMON_IMP.get(), worldIn);
    }

    public static boolean canSpawnPredicate(EntityType<DemonImp> mobType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
        return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && levelAccessor.m_8055_(pos.below()).m_60734_() != Blocks.NETHER_WART_BLOCK;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BaseFactionMob<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (this.getAction() != DemonImp.Action.IDLE) {
            label25: switch(this.getAction()) {
                case ATTACKING:
                    switch(this.getAttackAction()) {
                        case LEFT:
                            builder.thenPlay("animation.model.swipe_left");
                            break label25;
                        case RIGHT:
                            builder.thenPlay("animation.model.swipe_right");
                            break label25;
                        case NONE:
                        case BITE:
                        default:
                            builder.thenPlay("animation.model.bite");
                            break label25;
                    }
                case LEAP_START:
                    builder.thenPlay("animation.model.leap_start");
                    break;
                case LEAPING:
                    builder.thenPlay("animation.model.leap_loop");
                    break;
                case LEAP_LANDING:
                    builder.thenPlay("animation.model.leap_land");
            }
        }
        if (builder.getAnimationStages().size() == 0) {
            double movementSpeed = this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length();
            if (movementSpeed > 0.1) {
                builder.thenLoop("animation.model.run");
            } else if (movementSpeed > 0.02) {
                builder.thenLoop("animation.model.walk");
            } else {
                builder.thenLoop("animation.model.idle");
            }
        }
        return state.setAndContinue(builder);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.m_9236_().isClientSide()) {
            for (int i = 0; i < 5; i++) {
                this.m_9236_().addParticle(new MAParticleType(ParticleInit.HELLFIRE.get()), -0.5 + this.m_20185_() + Math.random(), this.m_20186_() + Math.random() * (double) this.m_20192_(), -0.5 + this.m_20189_() + Math.random(), this.m_20184_().x, 0.1F, this.m_20184_().z);
            }
        }
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
    }

    private DemonImp.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= DemonImp.Action.values().length ? DemonImp.Action.IDLE : DemonImp.Action.values()[ordinal];
    }

    private DemonImp.AttackAction getAttackAction() {
        int ordinal = (this.getStateFlag() & 65535) >> 12 & 15;
        return ordinal >= DemonImp.AttackAction.values().length ? DemonImp.AttackAction.NONE : DemonImp.AttackAction.values()[ordinal];
    }

    private void setAction(DemonImp.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAttackAction(DemonImp.AttackAction action) {
        int ordinal = (action.ordinal() & 15) << 12;
        int flag = this.getStateFlag();
        flag &= -61441;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (this.getAction() != DemonImp.Action.IDLE) {
            return false;
        } else {
            ArrayList<DemonImp.AttackAction> actionPool = new ArrayList();
            actionPool.add(DemonImp.AttackAction.LEFT);
            actionPool.add(DemonImp.AttackAction.RIGHT);
            actionPool.add(DemonImp.AttackAction.BITE);
            DemonImp.AttackAction selected = (DemonImp.AttackAction) actionPool.get((int) ((double) actionPool.size() * Math.random()));
            this.setAction(DemonImp.Action.ATTACKING);
            this.setAttackAction(selected);
            this.setTimer("", selected.animLength, () -> {
                this.damageEntity(entityIn);
                this.m_5496_(SFX.Entity.Imp.ATTACK, 1.0F, (float) (0.6 + Math.random() * 0.8));
            });
            this.setTimer("resetattack", selected.resetTime, () -> {
                this.setAttackAction(DemonImp.AttackAction.NONE);
                this.setAction(DemonImp.Action.IDLE);
            });
            ServerMessageDispatcher.sendEntityStateMessage(this);
            this.m_5496_(SFX.Entity.Imp.ATTACK, 1.0F, (float) (0.9 + Math.random() * 0.2));
            return true;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new FactionTierWrapperGoal(1, this, new LerpLeap<>(this, 10, 3, t -> this.m_5448_() != null && !this.isOnCooldown(1000) && this.m_20270_(this.m_5448_()) > 8.0F, e -> {
            switch(e) {
                case DAMAGE:
                    this.setAction(DemonImp.Action.LEAP_LANDING);
                    if (this.m_5448_() != null) {
                        this.damageEntity(this.m_5448_());
                    }
                    break;
                case LEAP:
                    this.setAction(DemonImp.Action.LEAPING);
                    break;
                case START:
                    this.setAction(DemonImp.Action.LEAP_START);
                    this.m_5496_(SFX.Entity.Imp.LEAP, 1.0F, 1.0F);
                    break;
                case STOP:
                    this.setAction(DemonImp.Action.IDLE);
                    this.setCooldown(1000, 300);
            }
        }).setLeapSpeed(4.0F).setEndWaitTicks(4)));
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), false));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.35F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, this::factionTargetPlayerPredicate));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, this::factionTargetHelpPredicate));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);
        if (!this.m_9236_().isClientSide() && cause.getEntity() instanceof LivingEntity && this.tier >= 3) {
            this.m_9236_().explode(this, this.m_20185_(), this.m_20186_(), this.m_20189_(), 5.0F, Level.ExplosionInteraction.NONE);
        }
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SFX.Entity.Imp.IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.Imp.DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SFX.Entity.Imp.ATTACK;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            return false;
        } else {
            if (source.is(DamageTypeTags.IS_FREEZING) || source.is(DamageTypeTags.IS_LIGHTNING)) {
                amount *= 0.5F;
            }
            amount = this.applyDamageResists(source, amount);
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
                if (this.getAction() == DemonImp.Action.LEAP_LANDING) {
                    f += 4.0F;
                }
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
                    if (this.f_19796_.nextFloat() < 0.2F && entityIn instanceof Mob) {
                        ((Mob) entityIn).setTarget(this);
                    }
                }
                return flag;
            }
        }
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
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.ATTACK_DAMAGE, 6.0).add(Attributes.ATTACK_SPEED, 40.0).add(Attributes.ATTACK_KNOCKBACK, 1.0).add(Attributes.FOLLOW_RANGE, 32.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.0);
    }

    @Override
    public CompoundTag getPacketData() {
        return new CompoundTag();
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
    }

    public static enum Action {

        IDLE, ATTACKING, LEAP_START, LEAPING, LEAP_LANDING
    }

    public static enum AttackAction {

        NONE(0, 0), LEFT(10, 14), RIGHT(10, 14), BITE(10, 14);

        public final int animLength;

        public final int resetTime;

        private AttackAction(int animLength, int resetTime) {
            this.animLength = animLength;
            this.resetTime = resetTime;
        }
    }
}