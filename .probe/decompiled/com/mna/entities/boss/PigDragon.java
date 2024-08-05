package com.mna.entities.boss;

import com.mna.api.sound.SFX;
import com.mna.entities.EntityInit;
import com.mna.entities.boss.attacks.OrangeChickenProjectile;
import com.mna.tools.math.MathUtils;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PigDragon extends BossMonster<PigDragon> {

    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(this.m_5446_(), BossEvent.BossBarColor.PINK, BossEvent.BossBarOverlay.NOTCHED_20).setDarkenScreen(true);

    private static final int COOLDOWN_SHOOT_CHICKEN = 0;

    private static final int COOLDOWN_DIVEBOMB = 0;

    private int spawnTicks = 90;

    private int flapCount = 0;

    private Vec3 targetPosition;

    public PigDragon(EntityType<PigDragon> type, Level world) {
        super(type, world);
        this.m_20242_(true);
        this.f_19794_ = true;
        this.setAction(PigDragon.Action.SUMMON);
    }

    public PigDragon(Level world) {
        this(EntityInit.PIG_DRAGON.get(), world);
        this.setAction(PigDragon.Action.SUMMON);
    }

    @Override
    protected PlayState handleAnimState(AnimationState<? extends BossMonster<?>> state) {
        RawAnimation builder = RawAnimation.begin();
        switch(this.getAction()) {
            case DIVEBOMBING:
                builder.thenLoop("animation.PigDragon.dive");
                break;
            case SHOOTING:
                builder.thenPlay("animation.PigDragon.fly_breath");
                break;
            case SUMMON:
                builder.thenPlay("animation.PigDragon.summon");
                break;
            case IDLE:
            default:
                if (this.flapCount > 0) {
                    builder.thenLoop("animation.PigDragon.fly");
                } else {
                    builder.thenLoop("animation.PigDragon.glide_gentle");
                }
        }
        return state.setAndContinue(builder);
    }

    @Override
    protected ServerBossEvent getBossEvent() {
        return this.bossEvent;
    }

    @Override
    public void setupSpawn() {
    }

    private double distSqToTarget() {
        return this.targetPosition == null ? 0.0 : this.targetPosition.distanceToSqr(this.m_20182_());
    }

    @Override
    protected void registerGoals() {
        this.f_21346_.addGoal(1, new PigDragon.ShootChicken());
        this.f_21346_.addGoal(2, new PigDragon.DiveBomb());
        this.f_21346_.addGoal(3, new PigDragon.PigDragonFly());
        this.f_21346_.addGoal(1, new BossMonster.ThreatTableHurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, le -> true));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, BossMonster.class, 10, true, false, le -> le != this));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.targetPosition != null) {
            Vec3 myPos = this.m_20182_();
            if (this.m_20184_().x == 0.0 && this.m_20184_().y == 0.0 && this.m_20184_().z == 0.0) {
                this.m_20334_(Math.random(), Math.random(), Math.random());
            }
            float tickTheta = 5.135F;
            Vec3 desiredHeading = this.targetPosition.subtract(myPos).normalize();
            Vec3 calculatedHeading = MathUtils.rotateTowards(this.m_20184_(), desiredHeading, tickTheta).normalize().scale(0.35F);
            this.m_20256_(calculatedHeading);
        }
        if (this.m_20184_().length() > 0.1F) {
            Vec3 target = this.m_20182_().add(this.m_20184_().normalize().scale(30.0));
            Vec3 vec3 = EntityAnchorArgument.Anchor.FEET.apply(this);
            double d0 = target.x - vec3.x;
            double d1 = target.y - vec3.y;
            double d2 = target.z - vec3.z;
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            this.m_146926_(Mth.wrapDegrees((float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI))));
            this.m_146922_(Mth.wrapDegrees((float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F));
            this.m_5616_(this.m_146908_());
        }
        if (this.m_9236_().isClientSide()) {
            if (this.flapCount > 0) {
                this.flapCount--;
                if (this.flapCount == 0) {
                    this.flapCount = -((int) (60.0 + Math.random() * 100.0));
                }
            } else if (this.flapCount <= 0) {
                this.flapCount++;
                if (this.flapCount == 0) {
                    this.flapCount = (int) (60.0 + Math.random() * 100.0);
                }
            }
        } else if (this.spawnTicks > 0) {
            this.m_21557_(true);
            this.spawnTicks--;
            this.m_6478_(MoverType.SELF, new Vec3(0.0, 0.25, 0.0));
            this.setAction(PigDragon.Action.SUMMON);
            this.m_20256_(Vec3.ZERO);
            if (this.spawnTicks == 0) {
                this.m_21557_(false);
                this.setAction(PigDragon.Action.IDLE);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource type, float amount) {
        return type.is(DamageTypeTags.IS_EXPLOSION) ? false : super.hurt(type, amount);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.checkWalls(this.m_20191_());
        if (!this.m_9236_().isClientSide() && this.f_20916_ == 0) {
            this.knockBack(this.m_9236_().getEntities(this, this.m_20191_().inflate(4.0, 2.0, 4.0).move(0.0, -2.0, 0.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
            this.hurtNearby(this.m_9236_().getEntities(this, this.m_20191_().inflate(1.0), EntitySelector.NO_CREATIVE_OR_SPECTATOR));
        }
    }

    private void setAction(PigDragon.Action action) {
        byte ordinal = (byte) ((action.ordinal() & 0xFF) << 3);
        int flag = this.getStateFlag();
        flag &= -2041;
        flag |= ordinal;
        this.setState(new int[] { flag });
    }

    private PigDragon.Action getAction() {
        int ordinal = (this.getStateFlag() & 2040) >> 3 & 0xFF;
        return ordinal >= PigDragon.Action.values().length ? PigDragon.Action.IDLE : PigDragon.Action.values()[ordinal];
    }

    @Override
    public ResourceLocation getArenaStructureID() {
        return null;
    }

    @Override
    public int getArenaStructureSegment() {
        return -1;
    }

    private void hurtNearby(List<Entity> pEntities) {
        for (Entity entity : pEntities) {
            if (entity instanceof LivingEntity) {
                entity.hurt(this.m_269291_().mobAttack(this), (float) this.m_21051_(Attributes.ATTACK_DAMAGE).getValue());
                this.m_19970_(this, entity);
            }
        }
    }

    private void knockBack(List<Entity> pEntities) {
        double d0 = (this.m_20191_().minX + this.m_20191_().maxX) / 2.0;
        double d1 = (this.m_20191_().minZ + this.m_20191_().maxZ) / 2.0;
        for (Entity entity : pEntities) {
            if (entity instanceof LivingEntity) {
                double d2 = entity.getX() - d0;
                double d3 = entity.getZ() - d1;
                double d4 = Math.max(d2 * d2 + d3 * d3, 0.1);
                entity.push(d2 / d4 * 4.0, 0.2F, d3 / d4 * 4.0);
                if (((LivingEntity) entity).getLastHurtByMobTimestamp() < entity.tickCount - 2) {
                    entity.hurt(this.m_269291_().mobAttack(this), 5.0F);
                    this.m_19970_(this, entity);
                }
            }
        }
    }

    private boolean checkWalls(AABB pArea) {
        int i = Mth.floor(pArea.minX);
        int j = Mth.floor(pArea.minY);
        int k = Mth.floor(pArea.minZ);
        int l = Mth.floor(pArea.maxX);
        int i1 = Mth.floor(pArea.maxY);
        int j1 = Mth.floor(pArea.maxZ);
        boolean flag = false;
        boolean flag1 = false;
        for (int k1 = i; k1 <= l; k1++) {
            for (int l1 = j; l1 <= i1; l1++) {
                for (int i2 = k; i2 <= j1; i2++) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                    if (!blockstate.m_60795_() && !blockstate.m_204336_(BlockTags.DRAGON_TRANSPARENT)) {
                        if (ForgeHooks.canEntityDestroy(this.m_9236_(), blockpos, this) && !blockstate.m_204336_(BlockTags.DRAGON_IMMUNE)) {
                            flag1 = this.m_9236_().removeBlock(blockpos, false) || flag1;
                        } else {
                            flag = true;
                        }
                    }
                }
            }
        }
        if (flag1) {
            BlockPos blockpos1 = new BlockPos(i + this.f_19796_.nextInt(l - i + 1), j + this.f_19796_.nextInt(i1 - j + 1), k + this.f_19796_.nextInt(j1 - k + 1));
            this.m_9236_().m_46796_(2008, blockpos1, 0);
        }
        return flag;
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.Oink.OINK;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 600.0).add(Attributes.MOVEMENT_SPEED, 0.7F).add(Attributes.FOLLOW_RANGE, 40.0).add(Attributes.ARMOR, 14.0).add(Attributes.ATTACK_DAMAGE, 20.0).add(ForgeMod.ENTITY_GRAVITY.get(), 0.0).add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    public static enum Action {

        IDLE, SHOOTING, DIVEBOMBING, SUMMON
    }

    private class DiveBomb extends Goal {

        private static final AttributeModifier DIVE_BONUS = new AttributeModifier(UUID.fromString("22b8d8eb-16a3-4ee3-80b7-73b19b8e9006"), "dive bonus", 1.0, AttributeModifier.Operation.ADDITION);

        @Override
        public boolean canUse() {
            return !PigDragon.this.isOnCooldown(0) && PigDragon.this.m_5448_() != null;
        }

        @Override
        public void start() {
            PigDragon.this.setAction(PigDragon.Action.DIVEBOMBING);
            PigDragon.this.m_21051_(Attributes.MOVEMENT_SPEED).addTransientModifier(DIVE_BONUS);
            PigDragon.this.targetPosition = PigDragon.this.m_5448_().m_146892_();
        }

        @Override
        public void stop() {
            PigDragon.this.setCooldown(0, 300);
            PigDragon.this.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(DIVE_BONUS);
            PigDragon.this.setAction(PigDragon.Action.IDLE);
        }

        @Override
        public boolean canContinueToUse() {
            return PigDragon.this.distSqToTarget() >= 4.0;
        }
    }

    class PigDragonFly extends Goal {

        public PigDragonFly() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            PigDragon.Action action = PigDragon.this.getAction();
            return action != PigDragon.Action.SHOOTING && action != PigDragon.Action.DIVEBOMBING ? PigDragon.this.distSqToTarget() < 2.0 : false;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource random = PigDragon.this.m_217043_();
            double d0 = PigDragon.this.m_20185_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = PigDragon.this.m_20186_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = PigDragon.this.m_20189_() + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            int adjustments = 0;
            int y;
            for (y = (int) d1; adjustments < 32; adjustments++) {
                BlockPos bpTestPos = BlockPos.containing(d0, (double) y, d2);
                if (!PigDragon.this.m_9236_().m_46859_(bpTestPos)) {
                    break;
                }
                y--;
            }
            d1 = (double) (y + 16);
            PigDragon.this.targetPosition = new Vec3(d0, d1, d2);
        }
    }

    private class ShootChicken extends Goal {

        int tickCount = 0;

        @Override
        public boolean canUse() {
            return !PigDragon.this.isOnCooldown(0) && PigDragon.this.m_5448_() != null;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void start() {
            PigDragon.this.setAction(PigDragon.Action.SHOOTING);
            Vec3 delta = PigDragon.this.m_5448_().m_20182_().subtract(PigDragon.this.m_20182_());
            Vec3 target = PigDragon.this.m_20182_().add(delta.scale(2.0));
            PigDragon.this.targetPosition = new Vec3(target.x, PigDragon.this.m_20182_().y, target.z);
            this.tickCount = 0;
        }

        @Override
        public void stop() {
            PigDragon.this.setCooldown(0, 40);
            PigDragon.this.setAction(PigDragon.Action.IDLE);
        }

        @Override
        public boolean canContinueToUse() {
            return this.tickCount <= 40 && this.canUse();
        }

        @Override
        public void tick() {
            if (this.tickCount++ % 20 == 0 && PigDragon.this.m_5448_() != null) {
                PigDragon.this.m_9236_().playSound(null, PigDragon.this.m_5448_(), SoundEvents.CHICKEN_HURT, PigDragon.this.getSoundSource(), 1.0F, (float) (0.9 + Math.random() * 0.2));
                Vec3 delta = PigDragon.this.m_5448_().m_20182_().subtract(PigDragon.this.m_20182_()).normalize();
                OrangeChickenProjectile proj = new OrangeChickenProjectile(PigDragon.this.m_9236_(), PigDragon.this.m_5448_());
                proj.m_146884_(PigDragon.this.m_20182_().add(PigDragon.this.m_20184_().normalize()));
                proj.m_6686_(delta.x, delta.y, delta.z, 0.5F, 0.0F);
                PigDragon.this.m_9236_().m_7967_(proj);
            }
        }
    }
}