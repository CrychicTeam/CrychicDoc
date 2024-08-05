package com.mna.entities.boss;

import com.mna.api.entities.IFactionEnemy;
import com.mna.api.entities.ai.CastSpellOnSelfGoal;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.tools.RLoc;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.ai.AIPredicateWrapperGoal;
import com.mna.entities.ai.LerpLeap;
import com.mna.factions.Factions;
import com.mna.items.ItemInit;
import com.mna.network.ServerMessageDispatcher;
import com.mna.network.messages.to_client.SpawnParticleEffectMessage;
import com.mna.spells.SpellCaster;
import com.mna.spells.components.ComponentFling;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.EntityUtil;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
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
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import org.apache.commons.lang3.mutable.MutableBoolean;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CouncilWarden extends BossMonster<CouncilWarden> implements IFactionEnemy<CouncilWarden> {

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_20).setDarkenScreen(true);

    private static final int COOLDOWN_ID_LEAP = 1000;

    private static final int COOLDOWN_ID_GRAVITY_WELL = 1001;

    private static final int COOLDOWN_ID_PULL = 1002;

    private static final int COOLDOWN_ID_GREATER_ANIMUS = 1003;

    private static final int COOLDOWN_ID_ARCANE_EMBERS = 1004;

    private static final int COOLDOWN_ID_ARCANE_SMITE = 1005;

    private static final int COOLDOWN_ID_SHIELD = 1006;

    private static final int COOLDOWN_ID_LEAP_AWAY = 1007;

    private int globalSpellCooldown = 0;

    public CouncilWarden(EntityType<? extends Monster> p_i48553_1_, Level p_i48553_2_) {
        super(p_i48553_1_, p_i48553_2_);
    }

    public CouncilWarden(Level level, Vec3 position) {
        this(EntityInit.COUNCIL_WARDEN.get(), level);
        this.m_146884_(position);
    }

    public CouncilWarden.WeaponState getWeaponState() {
        int ordinal = this.getStateFlag() & 3;
        return ordinal < CouncilWarden.WeaponState.values().length ? CouncilWarden.WeaponState.values()[ordinal] : CouncilWarden.WeaponState.SWORD_ON_BACK;
    }

    private CouncilWarden.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= CouncilWarden.Action.values().length ? CouncilWarden.Action.IDLE : CouncilWarden.Action.values()[ordinal];
    }

    private CouncilWarden.AttackAction getAttackAction() {
        int ordinal = (this.getStateFlag() & 65535) >> 12 & 15;
        return ordinal >= CouncilWarden.AttackAction.values().length ? CouncilWarden.AttackAction.NONE : CouncilWarden.AttackAction.values()[ordinal];
    }

    private void setWeaponState(CouncilWarden.WeaponState state) {
        byte ordinal = (byte) (state.ordinal() & 3);
        int flag = this.getStateFlag();
        flag &= -4;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAction(CouncilWarden.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private void setAttackAction(CouncilWarden.AttackAction action) {
        int ordinal = (action.ordinal() & 15) << 12;
        int flag = this.getStateFlag();
        flag &= -61441;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    @Override
    protected ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BossMonster<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        if (this.getInvulnerableTicks() > 0) {
            builder.thenPlay("animation.CouncilWarden.summon");
        } else if (this.getAction() == CouncilWarden.Action.SWORD_LEAP_UP || this.getAction() == CouncilWarden.Action.LEAP_AWAY) {
            builder.thenPlay("animation.CouncilWarden.swordspike_jump");
        } else if (this.getAction() == CouncilWarden.Action.ATTACKING) {
            switch(this.getAttackAction()) {
                case LEFT_CROSS:
                    builder.thenPlay("animation.CouncilWarden.cross_left");
                    break;
                case RIGHT_CROSS:
                    builder.thenPlay("animation.CouncilWarden.cross_right");
                    break;
                case LEFT_CAST:
                    builder.thenPlay("animation.CouncilWarden.offhand_cast_left");
                    break;
                case RIGHT_CAST:
                    builder.thenPlay("animation.CouncilWarden.offhand_cast_right");
                    break;
                case SELF_CAST:
                    builder.thenPlay("animation.CouncilWarden.self_cast");
                    break;
                case SLAM_CAST:
                    builder.thenPlay("animation.CouncilWarden.cast_slam");
                    break;
                case NONE:
                default:
                    if (this.m_5448_() != null) {
                        builder.thenLoop("animation.CouncilWarden.idle_combat");
                    } else {
                        builder.thenLoop("animation.CouncilWarden.idle");
                    }
            }
        } else {
            double movementSpeed = this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length();
            if (movementSpeed > 0.1) {
                builder.thenLoop("animation.CouncilWarden.run");
            } else if (movementSpeed > 0.02) {
                if (this.m_5448_() != null) {
                    builder.thenLoop("animation.CouncilWarden.walk_combat");
                } else {
                    builder.thenLoop("animation.CouncilWarden.walk");
                }
            } else if (this.m_5448_() != null) {
                builder.thenLoop("animation.CouncilWarden.idle_combat");
            } else {
                builder.thenLoop("animation.CouncilWarden.idle");
            }
        }
        return state.setAndContinue(builder);
    }

    private void setCastAnimation() {
        this.setAction(CouncilWarden.Action.ATTACKING);
        this.setAttackAction(this.getWeaponState() != CouncilWarden.WeaponState.SWORD_IN_HAND && Math.random() < 0.5 ? CouncilWarden.AttackAction.RIGHT_CAST : CouncilWarden.AttackAction.LEFT_CAST);
    }

    private void setSelfCastAnimation() {
        this.setAction(CouncilWarden.Action.ATTACKING);
        this.setAttackAction(CouncilWarden.AttackAction.SELF_CAST);
    }

    private void setSlamCastAnimation() {
        this.setAction(CouncilWarden.Action.ATTACKING);
        this.setAttackAction(CouncilWarden.AttackAction.SLAM_CAST);
    }

    private void resetAfterSpellcast(int cooldownModifier) {
        this.setAction(CouncilWarden.Action.IDLE);
        this.setAttackAction(CouncilWarden.AttackAction.NONE);
        this.globalSpellCooldown = 10 + (int) (Math.random() * (double) cooldownModifier);
    }

    @Override
    public void setupSpawn() {
        this.setInvulnerableTicks(40);
        this.m_20331_(true);
        this.m_21153_(this.m_21233_() / 3.0F);
        this.m_21557_(true);
    }

    @Override
    public ResourceLocation getArenaStructureID() {
        return RLoc.create("faction/boss/council_island");
    }

    @Override
    public int getArenaStructureSegment() {
        return 1;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getInvulnerableTicks() > 0) {
            if (this.getInvulnerableTicks() > 1) {
                this.m_5634_(5.0F);
                if (this.getInvulnerableTicks() == 20) {
                    this.setWeaponState(CouncilWarden.WeaponState.SWORD_IN_HAND);
                }
            } else if (this.getInvulnerableTicks() == 1) {
                this.m_21557_(false);
                this.m_20331_(false);
                this.m_21153_(this.m_21233_());
            }
            this.setInvulnerableTicks(this.getInvulnerableTicks() - 1);
        } else {
            if (this.globalSpellCooldown > 0) {
                this.globalSpellCooldown--;
            }
            if (this.getAction() == CouncilWarden.Action.ATTACKING && this.m_5448_() != null) {
                this.m_7618_(EntityAnchorArgument.Anchor.FEET, this.m_5448_().m_20182_());
            }
        }
    }

    @Override
    protected void registerGoals() {
        double moveSpeed = this.m_21133_(Attributes.MOVEMENT_SPEED);
        this.f_21345_.addGoal(0, new FloatGoal(this));
        ItemStack earthquake = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe quakeSpell = new SpellRecipe(Shapes.TOUCH, Components.EARTHQUAKE);
        quakeSpell.getShape().setValue(Attribute.RADIUS, 1.0F);
        quakeSpell.getComponent(0).setValue(Attribute.DURATION, 10.0F);
        quakeSpell.writeToNBT(earthquake.getOrCreateTag());
        ItemStack bolt_gravity_well = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe gravWellSpell = new SpellRecipe(Shapes.BOLT, Components.GRAVITY_WELL);
        gravWellSpell.addComponent(Components.SLOW);
        gravWellSpell.getShape().setValue(Attribute.RANGE, 48.0F);
        gravWellSpell.getComponent(0).setValue(Attribute.DURATION, 10.0F);
        gravWellSpell.getComponent(1).setValue(Attribute.DURATION, 10.0F);
        gravWellSpell.getComponent(1).setValue(Attribute.MAGNITUDE, 3.0F);
        gravWellSpell.writeToNBT(bolt_gravity_well.getOrCreateTag());
        ItemStack pull = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe pullSpell = new SpellRecipe(Shapes.PROJECTILE, Components.PULL);
        pullSpell.getShape().setValue(Attribute.RADIUS, 9.0F);
        pullSpell.getShape().setValue(Attribute.SPEED, 2.0F);
        pullSpell.getComponent(0).setValue(Attribute.SPEED, 3.0F);
        pullSpell.writeToNBT(pull.getOrCreateTag());
        ItemStack greater_animus = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe animusSpell = new SpellRecipe(Shapes.SELF, Components.GREATER_ANIMUS);
        animusSpell.getComponent(0).setValue(Attribute.DURATION, 30.0F);
        animusSpell.getComponent(0).setValue(Attribute.LESSER_MAGNITUDE, 1000.0F);
        animusSpell.writeToNBT(greater_animus.getOrCreateTag());
        ItemStack arcane_embers = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe embersSpell = new SpellRecipe(Shapes.EMBERS, Components.MAGIC_DAMAGE);
        embersSpell.getComponent(0).setValue(Attribute.DAMAGE, 20.0F);
        embersSpell.getShape().setValue(Attribute.MAGNITUDE, 3.0F);
        embersSpell.writeToNBT(arcane_embers.getOrCreateTag());
        ItemStack arcane_smite = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe smiteSpell = new SpellRecipe(Shapes.SMITE, Components.MAGIC_DAMAGE);
        smiteSpell.getComponent(0).setValue(Attribute.DAMAGE, 20.0F);
        smiteSpell.getComponent(0).setValue(Attribute.RADIUS, 3.0F);
        smiteSpell.addComponent(Components.DAMPEN_MAGIC);
        smiteSpell.getComponent(1).setValue(Attribute.DURATION, 20.0F);
        smiteSpell.writeToNBT(arcane_smite.getOrCreateTag());
        ItemStack shield = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe shieldSpell = new SpellRecipe(Shapes.SELF, Components.SHIELD);
        shieldSpell.getComponent(0).setValue(Attribute.DURATION, 60.0F);
        shieldSpell.getComponent(0).setValue(Attribute.MAGNITUDE, 1.0F);
        shieldSpell.writeToNBT(shield.getOrCreateTag());
        this.registerShield(1, shield);
        this.registerEmbers(1, arcane_embers);
        this.registerPull(5, pull);
        this.registerLeapGoal(5, earthquake);
        this.f_21345_.addGoal(7, new AIPredicateWrapperGoal<>(this, new BossMonster.AdjustableSpeedMeleeAttackGoal(this, moveSpeed, true, 10).setReachMultiplier(1.25F)).executionPredicate(e -> e.getWeaponState() == CouncilWarden.WeaponState.SWORD_IN_HAND).continuationPredicate(e -> e.getWeaponState() == CouncilWarden.WeaponState.SWORD_IN_HAND));
        this.f_21345_.addGoal(8, new RandomStrollGoal(this, moveSpeed));
        this.f_21345_.addGoal(9, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21346_.addGoal(1, new BossMonster.ThreatTableHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, le -> true));
        this.f_21346_.addGoal(4, new NearestAttackableTargetGoal(this, Mob.class, 10, true, false, e -> e instanceof IFactionEnemy && ((IFactionEnemy) e).getFaction() != this.getFaction()));
    }

    private void registerLeapGoal(int priority, ItemStack spell) {
        this.f_21345_.addGoal(priority, new LerpLeap<>(this, 10, 8, e -> e.m_20096_() && e.m_5448_() != null && e.m_5448_().m_20270_(e) > 8.0F && e.m_5448_().m_20096_() && e.m_5448_().isAlive() && !e.isOnCooldown(1000) && e.getWeaponState() == CouncilWarden.WeaponState.SWORD_IN_HAND, evt -> {
            switch(evt) {
                case START:
                    this.setAction(CouncilWarden.Action.SWORD_LEAP_UP);
                    break;
                case STOP:
                    this.setAction(CouncilWarden.Action.IDLE);
                    this.setAttackAction(CouncilWarden.AttackAction.NONE);
                    this.setCooldown(1000, 1500);
                    break;
                case DAMAGE:
                    this.m_9236_().getEntities(this, this.m_20191_().inflate(4.0), tgt -> tgt instanceof LivingEntity && tgt.isAlive() && tgt != this && !this.m_7307_(tgt)).stream().map(e -> (LivingEntity) e).forEach(target -> {
                        float mX = (float) (this.m_20185_() - target.m_20185_());
                        float mZ = (float) (this.m_20189_() - target.m_20189_());
                        float speed = 1.9499999F;
                        ComponentFling.flingTarget(target, new Vec3((double) mX, (double) speed, (double) mZ), speed, 3.0F);
                        target.hurt(this.m_269291_().mobAttack(this), 30.0F);
                        target.addEffect(new MobEffectInstance(EffectInit.EARTHQUAKE.get(), 100, 1));
                    });
                    SpellSource me = new SpellSource(this, InteractionHand.MAIN_HAND);
                    for (int i = -5; i <= 5; i++) {
                        for (int j = -5; j <= 5; j++) {
                            if (!(Math.random() < 0.5) && !(i == 0 & j == 0)) {
                                SpellTarget hint = new SpellTarget(this.m_20183_().below().offset(i, 0, j), Direction.UP);
                                SpellCaster.Affect(spell, SpellRecipe.fromNBT(spell.getOrCreateTag()), this.m_9236_(), me, hint);
                            }
                        }
                    }
                    if (!this.m_9236_().isClientSide()) {
                        ServerMessageDispatcher.sendParticleEffect(this.m_9236_().dimension(), 32.0F, this.m_20185_(), this.m_20186_(), this.m_20189_(), SpawnParticleEffectMessage.ParticleTypes.CONSTRUCT_HAMMER_SMASH);
                    }
            }
        }).setForceLerpTicks(38).setEndWaitTicks(20));
    }

    private void registerLeapAwayGoal(int priority) {
        this.f_21345_.addGoal(priority, new LerpLeap<>(this, 10, 8, e -> e.m_20096_() && e.m_5448_() != null && e.m_5448_().isAlive() && e.m_5448_().m_20270_(e) < 6.0F && !e.isOnCooldown(1007), evt -> {
            switch(evt) {
                case START:
                    this.setAction(CouncilWarden.Action.LEAP_AWAY);
                    break;
                case STOP:
                    this.setAction(CouncilWarden.Action.IDLE);
                    this.setAttackAction(CouncilWarden.AttackAction.NONE);
                    this.setCooldown(1007, 2000);
            }
        }).setForceLerpTicks(38).setEndWaitTicks(20).setEndAdjuster(end -> {
            Vec3 rPos = EntityUtil.getRetreatPos(this, this.m_20156_().scale(-1.0), 16);
            return rPos == null ? end : rPos;
        }));
    }

    private void registerBoltGravWell(int priority, ItemStack bolt_gravity_well) {
        this.f_21345_.addGoal(priority, new BossMonster.CastSpellAtTargetGoal(bolt_gravity_well, this.m_21133_(Attributes.MOVEMENT_SPEED), 100, 42.0F, 2.0F, false, 1001, 7, 5, null, () -> this.resetAfterSpellcast(30), me -> {
            this.setCastAnimation();
            return true;
        }).setUsePredicate(e -> this.getAction() == CouncilWarden.Action.IDLE && this.globalSpellCooldown == 0 && !this.m_5448_().m_20096_()));
    }

    private void registerPull(int priority, ItemStack pull) {
        this.f_21345_.addGoal(priority, new BossMonster.CastSpellAtTargetGoal(pull, this.m_21133_(Attributes.MOVEMENT_SPEED), 100, 42.0F, 2.0F, true, 1002, 7, 5, null, () -> this.resetAfterSpellcast(30), me -> {
            this.setCastAnimation();
            return true;
        }).setUsePredicate(e -> this.getAction() == CouncilWarden.Action.IDLE && this.globalSpellCooldown == 0 && !this.m_5448_().m_20096_()).setTargetPredicate(e -> e.m_20270_(this) > 8.0F));
    }

    private void registerSmite(int priority, ItemStack arcane_smite) {
        this.f_21345_.addGoal(priority, new BossMonster.CastSpellAtTargetGoal(arcane_smite, this.m_21133_(Attributes.MOVEMENT_SPEED), 200, 42.0F, 2.0F, true, 1005, 7, 5, null, () -> this.resetAfterSpellcast(30), me -> {
            this.setSlamCastAnimation();
            return true;
        }).setUsePredicate(e -> this.getAction() == CouncilWarden.Action.IDLE && this.globalSpellCooldown == 0 && !this.m_5448_().m_20096_()).setTargetPredicate(e -> e.m_20270_(this) > 2.0F));
    }

    private void registerShield(int priority, ItemStack shield) {
        this.f_21345_.addGoal(priority, new CastSpellOnSelfGoal<>(this, shield, e -> !e.isOnCooldown(1006) && !e.m_21023_(MobEffects.DAMAGE_RESISTANCE) && this.globalSpellCooldown <= 0, me -> me.setSelfCastAnimation(), me -> {
            me.resetAfterSpellcast(30);
            me.setCooldown(1006, 300);
        }, 21));
    }

    private void registerEmbers(int priority, ItemStack arcane_embers) {
        this.f_21345_.addGoal(priority, new CastSpellOnSelfGoal<>(this, arcane_embers, e -> !e.isOnCooldown(1004) && this.globalSpellCooldown <= 0, me -> me.setSelfCastAnimation(), me -> {
            me.resetAfterSpellcast(30);
            me.setCooldown(1004, 300);
        }, 21));
    }

    private void registerGreaterAnimus(int priority, ItemStack greater_animus) {
        this.f_21345_.addGoal(priority, new CastSpellOnSelfGoal<>(this, greater_animus, e -> !e.isOnCooldown(1003) && this.globalSpellCooldown <= 0 && (double) this.m_21223_() < (double) this.m_21233_() * 0.5 && this.getWeaponState() == CouncilWarden.WeaponState.SWORD_IN_HAND, me -> {
            me.setSelfCastAnimation();
            SpellRecipe recipe = new SpellRecipe(Shapes.PROJECTILE, Components.IMPALE).addComponent(Components.SLOW).addComponent(Components.WEAKNESS);
            recipe.getComponent(0).setValue(Attribute.DAMAGE, 20.0F);
            recipe.getComponent(1).setValue(Attribute.MAGNITUDE, 2.0F);
            recipe.getComponent(1).setValue(Attribute.DURATION, 40.0F);
            recipe.getComponent(2).setValue(Attribute.MAGNITUDE, 2.0F);
            recipe.getComponent(2).setValue(Attribute.DURATION, 40.0F);
            me.m_21008_(InteractionHand.OFF_HAND, recipe.createAsSpell());
        }, me -> {
            me.resetAfterSpellcast(30);
            me.setCooldown(1003, 300);
        }, 21));
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isOnCooldown(1) && this.getAction() == CouncilWarden.Action.IDLE) {
            ArrayList<CouncilWarden.AttackAction> actionPool = new ArrayList();
            actionPool.add(CouncilWarden.AttackAction.LEFT_CROSS);
            actionPool.add(CouncilWarden.AttackAction.RIGHT_CROSS);
            CouncilWarden.AttackAction selected = (CouncilWarden.AttackAction) actionPool.get((int) ((double) actionPool.size() * Math.random()));
            this.setAction(CouncilWarden.Action.ATTACKING);
            this.setAttackAction(selected);
            this.setTimer("", selected.animLength, () -> {
                for (LivingEntity livingentity : this.m_9236_().m_45976_(LivingEntity.class, entityIn.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
                    if (livingentity != this) {
                        this.damageEntity(livingentity);
                    }
                }
                this.m_5496_(SFX.Entity.Odin.ATTACK, 1.0F, (float) (0.6 + Math.random() * 0.8));
            });
            this.setTimer("resetattack", selected.resetTime, () -> {
                this.setAttackAction(CouncilWarden.AttackAction.NONE);
                this.setAction(CouncilWarden.Action.IDLE);
                this.setCooldown(1, 5);
            });
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.setupSpawn();
    }

    public boolean damageEntity(Entity entityIn) {
        float f = (float) this.m_21133_(Attributes.ATTACK_DAMAGE);
        float f1 = (float) this.m_21133_(Attributes.ATTACK_KNOCKBACK);
        ArrayList<Entity> targets = new ArrayList();
        if (this.getAttackAction() == CouncilWarden.AttackAction.LEFT_CROSS || this.getAttackAction() == CouncilWarden.AttackAction.RIGHT_CROSS) {
            EntityUtil.getEntitiesWithinCone(this.m_9236_(), this.m_20182_(), Vec3.directionFromRotation(this.m_146909_(), this.m_6080_()), 6.0F, -60.0F, 60.0F, e -> e instanceof LivingEntity && e.isAlive() && e != this && e != entityIn);
        }
        targets.add(entityIn);
        MutableBoolean hurtTarget = new MutableBoolean(false);
        targets.forEach(t -> {
            boolean hurt = t.hurt(this.m_269291_().mobAttack(this), f);
            if (hurt) {
                hurtTarget.setTrue();
                if (f1 > 0.0F && t instanceof LivingEntity) {
                    ((LivingEntity) t).knockback((double) (f1 * 0.5F), (double) Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)), (double) (-Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0))));
                    this.m_20256_(this.m_20184_().multiply(0.6, 1.0, 0.6));
                }
                if (t instanceof Player playerentity) {
                    this.maybeDisableShield(playerentity, playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
                    if (!this.lowerBuffs(playerentity)) {
                        this.suppressEnchantments(playerentity);
                    }
                }
                this.m_21335_(t);
                if (this.f_19796_.nextFloat() < 0.4F && t instanceof Mob) {
                    ((Mob) t).setTarget(this);
                }
            }
        });
        return hurtTarget.getValue();
    }

    private void maybeDisableShield(Player player, ItemStack playerItem) {
        if (!playerItem.isEmpty() && playerItem.getItem().canPerformAction(playerItem, ToolActions.SHIELD_BLOCK)) {
            player.getCooldowns().addCooldown(Items.SHIELD, 100);
            this.m_9236_().broadcastEntityEvent(player, (byte) 30);
        }
    }

    private void suppressEnchantments(Player target) {
        if (!target.m_21023_(EffectInit.DISJUNCTION.get())) {
            target.m_7292_(new MobEffectInstance(EffectInit.DISJUNCTION.get(), 100, 5));
        }
    }

    private boolean lowerBuffs(Player target) {
        List<MobEffectInstance> beneficialEffects = target.m_21220_().stream().filter(e -> e.getEffect().isBeneficial()).toList();
        if (beneficialEffects.size() == 0) {
            return false;
        } else {
            int numToLower = MathUtils.weightedRandomNumber(beneficialEffects.size() + 1);
            for (int i = 0; i < numToLower; i++) {
                MobEffectInstance effect = (MobEffectInstance) beneficialEffects.get(i);
                if (effect.getAmplifier() == 0) {
                    target.m_21195_(effect.getEffect());
                    i--;
                    numToLower--;
                } else {
                    int duration = effect.getDuration();
                    int amp = effect.getAmplifier() - 1;
                    target.m_21195_(effect.getEffect());
                    target.m_7292_(new MobEffectInstance(effect.getEffect(), duration, amp));
                }
            }
            return true;
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 850.0).add(Attributes.MOVEMENT_SPEED, 0.7F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 20.0).add(Attributes.ATTACK_DAMAGE, 20.0).add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
    }

    @Override
    public HashMap<String, Integer> getDamageResists() {
        return new HashMap();
    }

    @Override
    public IFaction getFaction() {
        return Factions.COUNCIL;
    }

    @Override
    public Player getRaidTarget() {
        return null;
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public void setRaidTarget(Player player) {
    }

    @Override
    public void setTier(int tier) {
    }

    public static enum Action {

        IDLE,
        ATTACKING,
        SWORD_LEAP_UP,
        SWORD_LEAP_LAND,
        SWORD_THROW_START,
        SWORD_THROW_LOOP,
        SWORD_THROW_END,
        LEAP_AWAY
    }

    public static enum AttackAction {

        NONE(0, 0),
        LEFT_CROSS(8, 12),
        RIGHT_CROSS(8, 12),
        LEFT_CAST(8, 12),
        RIGHT_CAST(8, 12),
        SELF_CAST(8, 12),
        SLAM_CAST(11, 15);

        public final int animLength;

        public final int resetTime;

        private AttackAction(int animLength, int resetTime) {
            this.animLength = animLength;
            this.resetTime = resetTime;
        }
    }

    public static enum WeaponState {

        SWORD_ON_BACK, SWORD_IN_HAND, NO_SWORD
    }
}