package com.mna.entities.boss;

import com.mna.api.sound.SFX;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.entities.EntityInit;
import com.mna.entities.IAnimPacketSync;
import com.mna.entities.boss.attacks.PumpkinKingEntangle;
import com.mna.entities.boss.attacks.PumpkinKingIncinerate;
import com.mna.network.ServerMessageDispatcher;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PumpkinKing extends BossMonster<PumpkinKing> implements IEntityAdditionalSpawnData, IAnimPacketSync<PumpkinKing> {

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS).setDarkenScreen(true);

    private static final Predicate<Player> randomPlayerFlyable = p -> p.getAbilities().flying || !p.m_20096_();

    private static final String INSTRUCTION_DAMAGE = "damage";

    private static final String INSTRUCTION_ENTANGLE = "entangle";

    private static final String INSTRUCTION_INCINERATE = "incinerate";

    private static final int ENTANGLE_CD = 300;

    private static final int INCINERATE_CD = 300;

    private static final int ATTACK_CD = 20;

    private int entangleCooldown = 0;

    private int incinerateCooldown = 0;

    private int attackCooldown = 0;

    private boolean isAttacking = false;

    private boolean leftHandPunch = false;

    private boolean isEntangling = false;

    private boolean isIncinerating = false;

    public PumpkinKing(EntityType<? extends PumpkinKing> type, Level world) {
        super(type, world);
    }

    public PumpkinKing(Level world) {
        this(EntityInit.PUMPKIN_KING.get(), world);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new BossMonster.DoNothingGoal());
        this.f_21345_.addGoal(1, new PumpkinKing.EntangleGoal());
        this.f_21345_.addGoal(1, new PumpkinKing.IncinerateGoal());
        this.f_21345_.addGoal(2, new MeleeAttackGoal(this, this.m_21133_(Attributes.MOVEMENT_SPEED), false));
        this.f_21345_.addGoal(5, new RandomStrollGoal(this, 0.35F));
        this.f_21345_.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, le -> true));
    }

    @Override
    public void tick() {
        super.tick();
        this.entangleCooldown--;
        this.incinerateCooldown--;
        this.attackCooldown--;
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (!this.isAttacking && this.attackCooldown <= 0) {
            this.m_5496_(SFX.Entity.PumpkinKing.ATTACK, this.m_6121_(), this.m_6100_());
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("damage", 10, entityIn, this::handleDelayCallback));
            DelayedEventQueue.pushEvent(this.m_9236_(), new TimedDelayedEvent<>("resetattack", 15, "", this::handleDelayCallback));
            this.isAttacking = true;
            this.leftHandPunch = Math.random() < 0.5;
            ServerMessageDispatcher.sendEntityStateMessage(this);
            return true;
        } else {
            return true;
        }
    }

    @Override
    protected ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    private void handleDelayCallback(String identifier, Entity entity) {
        if (!this.m_9236_().isClientSide() && this.m_6084_()) {
            switch(identifier) {
                case "damage":
                    this.damageEntity(entity);
                    break;
                case "incinerate":
                    ArrayList<Player> attacked = new ArrayList();
                    for (int i = 0; i < 10; i++) {
                        Player playerx = this.getRandomNearbyPlayer(null);
                        if (playerx == null) {
                            return;
                        }
                        if (!attacked.contains(playerx)) {
                            this.spawnIncinerate(playerx);
                            attacked.add(playerx);
                        }
                        if (attacked.size() >= 4 || attacked.size() == this.m_9236_().m_6907_().size()) {
                            return;
                        }
                    }
                    break;
                case "entangle":
                    Player player = this.getRandomNearbyPlayer(randomPlayerFlyable);
                    if (player != null) {
                        this.spawnEntangle(player);
                    }
            }
        }
    }

    private void spawnIncinerate(Player player) {
        PumpkinKingIncinerate incinerate = new PumpkinKingIncinerate(this.m_9236_());
        incinerate.m_6034_(player.m_20185_(), player.m_20186_(), player.m_20189_());
        this.m_9236_().m_7967_(incinerate);
    }

    private void spawnEntangle(Player player) {
        BlockPos spawnPos = player.m_20183_();
        int offsetAmount = 10;
        spawnPos = spawnPos.offset(-offsetAmount + (int) (Math.random() * (double) offsetAmount * 2.0), 0, -offsetAmount + (int) (Math.random() * (double) offsetAmount * 2.0));
        while (spawnPos.m_123342_() > 0 && this.m_9236_().m_46859_(spawnPos)) {
            spawnPos = spawnPos.below();
        }
        spawnPos = spawnPos.above();
        PumpkinKingEntangle entangle = new PumpkinKingEntangle(this.m_9236_());
        entangle.m_6034_((double) ((float) spawnPos.m_123341_() + 0.5F), (double) spawnPos.m_123342_(), (double) ((float) spawnPos.m_123343_() + 0.5F));
        entangle.setTarget(player);
        this.m_9236_().m_7967_(entangle);
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
                this.maybeDisableShield(playerentity, playerentity.m_6117_() ? playerentity.m_21211_() : ItemStack.EMPTY);
            }
            this.m_19970_(this, entityIn);
            this.m_21335_(entityIn);
            if (this.f_19796_.nextFloat() < 0.4F && entityIn instanceof Mob) {
                ((Mob) entityIn).setTarget(this);
            }
        }
        return flag;
    }

    private void maybeDisableShield(Player player, ItemStack playerItem) {
        if (!playerItem.isEmpty() && playerItem.getItem().canPerformAction(playerItem, ToolActions.SHIELD_BLOCK)) {
            float f = 0.25F + (float) EnchantmentHelper.getBlockEfficiency(this) * 0.05F;
            if (this.f_19796_.nextFloat() < f) {
                player.getCooldowns().addCooldown(Items.SHIELD, 100);
                this.m_9236_().broadcastEntityEvent(player, (byte) 30);
            }
        }
    }

    private void handleDelayCallback(String identifier, String data) {
        if (!this.m_9236_().isClientSide()) {
            this.isAttacking = false;
            if (this.isEntangling) {
                this.entangleCooldown = 300;
                if (this.incinerateCooldown <= 0) {
                    this.incinerateCooldown = 37;
                }
                this.isEntangling = false;
            } else if (this.isIncinerating) {
                this.incinerateCooldown = 300;
                this.isIncinerating = false;
            } else {
                this.attackCooldown = 20;
            }
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("attacking", this.isAttacking);
        nbt.putBoolean("entangling", this.isEntangling);
        nbt.putBoolean("incinerating", this.isIncinerating);
        nbt.putBoolean("left_hand_punch", this.leftHandPunch);
        return nbt;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.isAttacking = nbt.getBoolean("attacking");
        this.isEntangling = nbt.getBoolean("entangling");
        this.isIncinerating = nbt.getBoolean("incinerating");
        this.leftHandPunch = nbt.getBoolean("left_hand_punch");
    }

    @Override
    public void setupSpawn() {
        this.setInvulnerableTicks(79);
        this.m_21153_(this.m_21233_() / 3.0F);
        this.m_5496_(SFX.Entity.PumpkinKing.SPAWN, this.m_6121_(), this.m_6100_());
    }

    @Override
    public ResourceLocation getArenaStructureID() {
        return null;
    }

    @Override
    public int getArenaStructureSegment() {
        return -1;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SFX.Entity.PumpkinKing.IDLE;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.PumpkinKing.DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SFX.Entity.PumpkinKing.HURT;
    }

    @Override
    public boolean hurt(DamageSource type, float amount) {
        if (this.getInvulnerableTicks() > 0 && type.is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return false;
        } else if (this.isDamageCheaty(type)) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            return false;
        } else {
            if (type.is(DamageTypeTags.IS_FREEZING)) {
                amount *= 2.0F;
            }
            if (type.is(DamageTypeTags.IS_FIRE)) {
                amount = (float) ((double) amount * 0.5);
            }
            return super.hurt(type, amount);
        }
    }

    @Override
    protected void customServerAiStep() {
        if (this.getInvulnerableTicks() > 0) {
            int j1 = this.getInvulnerableTicks() - 1;
            if (j1 <= 0) {
                if (!this.m_20067_()) {
                    this.m_9236_().globalLevelEvent(1023, this.m_20183_(), 0);
                }
                this.m_5634_(this.m_21233_());
            }
            this.setInvulnerableTicks(j1);
            if (this.f_19797_ % 10 == 0) {
                this.m_5634_(25.0F);
            }
        }
        super.customServerAiStep();
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BossMonster<?>> state) {
        if (this.getInvulnerableTicks() > 0) {
            state.getController().transitionLength(0);
            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.PumpkinKing.spawn"));
        } else {
            state.getController().transitionLength(2);
            if (!this.isAttacking) {
                return this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F ? state.setAndContinue(RawAnimation.begin().thenLoop("animation.PumpkinKing.walk")) : state.setAndContinue(RawAnimation.begin().thenLoop("animation.PumpkinKing.idle"));
            } else if (this.isIncinerating) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.PumpkinKing.incinerate"));
            } else if (this.isEntangling) {
                return state.setAndContinue(RawAnimation.begin().thenPlay("animation.PumpkinKing.entangle"));
            } else {
                return this.leftHandPunch ? state.setAndContinue(RawAnimation.begin().thenPlay("animation.PumpkinKing.swipe_left").thenLoop("animation.PumpkinKing.idle")) : state.setAndContinue(RawAnimation.begin().thenPlay("animation.PumpkinKing.swipe_right").thenLoop("animation.PumpkinKing.idle"));
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 300.0).add(Attributes.MOVEMENT_SPEED, 0.6F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 4.0);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.m_146922_(additionalData.readFloat());
        this.f_19859_ = additionalData.readFloat();
        this.m_146926_(additionalData.readFloat());
        this.f_19860_ = additionalData.readFloat();
        this.f_20883_ = this.m_146908_();
        this.f_20884_ = this.f_19859_;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.m_146908_());
        buffer.writeFloat(this.f_19859_);
        buffer.writeFloat(this.m_146909_());
        buffer.writeFloat(this.f_19860_);
    }

    public class EntangleGoal extends Goal {

        public EntangleGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return PumpkinKing.this.entangleCooldown <= 0 && PumpkinKing.this.m_5448_() != null && !PumpkinKing.this.isAttacking && PumpkinKing.this.incinerateCooldown <= 0;
        }

        @Override
        public void start() {
            PumpkinKing.this.m_21573_().stop();
            PumpkinKing.this.isEntangling = true;
            PumpkinKing.this.isAttacking = true;
            PumpkinKing.this.m_5496_(SFX.Entity.PumpkinKing.ENTANGLE, PumpkinKing.this.m_6121_(), PumpkinKing.this.m_6100_());
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("entangle", 15, PumpkinKing.this, PumpkinKing.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("entangle", 25, PumpkinKing.this, PumpkinKing.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("entangle", 35, PumpkinKing.this, PumpkinKing.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("entangle", 45, PumpkinKing.this, PumpkinKing.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 60, "", PumpkinKing.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(PumpkinKing.this);
        }

        @Override
        public boolean canContinueToUse() {
            return PumpkinKing.this.isEntangling;
        }
    }

    public class IncinerateGoal extends Goal {

        public IncinerateGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return PumpkinKing.this.incinerateCooldown <= 0 && PumpkinKing.this.m_5448_() != null && !PumpkinKing.this.isAttacking;
        }

        @Override
        public void start() {
            PumpkinKing.this.m_21573_().stop();
            PumpkinKing.this.isIncinerating = true;
            PumpkinKing.this.isAttacking = true;
            PumpkinKing.this.m_5496_(SFX.Entity.PumpkinKing.INCINERATE, PumpkinKing.this.m_6121_(), PumpkinKing.this.m_6100_());
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("incinerate", 65, PumpkinKing.this, PumpkinKing.this::handleDelayCallback));
            DelayedEventQueue.pushEvent(PumpkinKing.this.m_9236_(), new TimedDelayedEvent<>("resetattack", 82, "", PumpkinKing.this::handleDelayCallback));
            ServerMessageDispatcher.sendEntityStateMessage(PumpkinKing.this);
        }

        @Override
        public boolean canContinueToUse() {
            return PumpkinKing.this.isIncinerating;
        }
    }
}