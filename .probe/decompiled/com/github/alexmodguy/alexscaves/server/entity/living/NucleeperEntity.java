package com.github.alexmodguy.alexscaves.server.entity.living;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearSirenBlockEntity;
import com.github.alexmodguy.alexscaves.server.block.poi.ACPOIRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.ai.GroundPathNavigatorNoSpin;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearExplosionEntity;
import com.github.alexmodguy.alexscaves.server.entity.util.ActivatesSirens;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.google.common.base.Predicates;
import java.util.EnumSet;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NucleeperEntity extends Monster implements ActivatesSirens {

    private float closeProgress;

    private float prevCloseProgress;

    private float explodeProgress;

    private float prevExplodeProgress;

    private float sirenAngle;

    private float prevSirenAngle;

    private int catScareTime = 0;

    private static final EntityDataAccessor<Boolean> TRIGGERED = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> CLOSE_TIME = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> EXPLODING = SynchedEntityData.defineId(NucleeperEntity.class, EntityDataSerializers.BOOLEAN);

    public NucleeperEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(1, new AvoidEntityGoal<RaycatEntity>(this, RaycatEntity.class, 10.0F, 1.0, 1.2) {

            @Override
            public void tick() {
                super.tick();
                NucleeperEntity.this.catScareTime = 20;
            }
        });
        this.f_21345_.addGoal(2, new NucleeperEntity.MeleeGoal());
        this.f_21345_.addGoal(3, new RandomStrollGoal(this, 1.0, 45));
        this.f_21345_.addGoal(4, new LookAtPlayerGoal(this, Player.class, 15.0F));
        this.f_21345_.addGoal(5, new RandomLookAroundGoal(this));
        this.f_21346_.addGoal(1, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, true, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.MAX_HEALTH, 40.0).add(Attributes.ARMOR, 4.0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigatorNoSpin(this, level);
    }

    @Override
    protected void defineSynchedData() {
        super.m_8097_();
        this.f_19804_.define(TRIGGERED, false);
        this.f_19804_.define(CLOSE_TIME, 0);
        this.f_19804_.define(EXPLODING, false);
    }

    public int getCloseTime() {
        return this.f_19804_.get(CLOSE_TIME);
    }

    public void setCloseTime(int time) {
        this.f_19804_.set(CLOSE_TIME, time);
    }

    public boolean isTriggered() {
        return this.f_19804_.get(TRIGGERED);
    }

    public void setTriggered(boolean triggered) {
        this.f_19804_.set(TRIGGERED, triggered);
    }

    public boolean isExploding() {
        return this.f_19804_.get(EXPLODING);
    }

    public void setExploding(boolean explode) {
        this.f_19804_.set(EXPLODING, explode);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (itemstack.is(ItemTags.CREEPER_IGNITERS)) {
            SoundEvent soundevent = itemstack.is(Items.FIRE_CHARGE) ? SoundEvents.FIRECHARGE_USE : SoundEvents.FLINTANDSTEEL_USE;
            this.m_9236_().playSound(player, this.m_20185_(), this.m_20186_(), this.m_20189_(), soundevent, this.m_5720_(), 1.0F, this.f_19796_.nextFloat() * 0.4F + 0.8F);
            if (!this.m_9236_().isClientSide) {
                this.setTriggered(true);
                itemstack.hurtAndBreak(1, player, p_32290_ -> p_32290_.m_21190_(hand));
            }
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.m_6071_(player, hand);
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        this.prevCloseProgress = this.closeProgress;
        this.prevExplodeProgress = this.explodeProgress;
        this.prevSirenAngle = this.sirenAngle;
        int time = this.getCloseTime();
        if (this.isExploding() && this.explodeProgress < 5.0F) {
            this.explodeProgress++;
        }
        if (!this.isExploding() && this.explodeProgress > 0.0F) {
            this.explodeProgress--;
        }
        if (this.isTriggered() && !this.m_9236_().isClientSide) {
            if (this.catScareTime > 0 && !this.isExploding()) {
                if (time > 0) {
                    this.setCloseTime(time - 1);
                } else {
                    this.setTriggered(false);
                }
            } else if (time < AlexsCaves.COMMON_CONFIG.nucleeperFuseTime.get()) {
                this.setCloseTime(time + 1);
            } else if (this.m_6084_()) {
                this.setExploding(true);
            }
            if ((this.f_19797_ + this.m_19879_()) % 10 == 0 && this.m_9236_() instanceof ServerLevel serverLevel) {
                this.getNearbySirens(serverLevel, 256).forEach(this::activateSiren);
            }
        }
        if (this.isTriggered() && this.m_6084_()) {
            AlexsCaves.PROXY.playWorldSound(this, (byte) 1);
        }
        this.sirenAngle = this.sirenAngle + (10.0F + 30.0F * this.closeProgress) % 360.0F;
        this.closeProgress = (float) time / (float) AlexsCaves.COMMON_CONFIG.nucleeperFuseTime.get().intValue();
        if (this.catScareTime > 0) {
            this.catScareTime--;
        }
        if (this.isExploding() && this.explodeProgress >= 5.0F) {
            this.m_146870_();
            if (!this.m_9236_().isClientSide) {
                this.explode();
            }
        }
    }

    @Override
    public void remove(Entity.RemovalReason removalReason) {
        AlexsCaves.PROXY.clearSoundCacheFor(this);
        super.m_142687_(removalReason);
    }

    private Stream<BlockPos> getNearbySirens(ServerLevel world, int range) {
        PoiManager pointofinterestmanager = world.getPoiManager();
        return pointofinterestmanager.findAll(poiTypeHolder -> poiTypeHolder.is(ACPOIRegistry.NUCLEAR_SIREN.getKey()), Predicates.alwaysTrue(), this.m_20183_(), range, PoiManager.Occupancy.ANY);
    }

    private void activateSiren(BlockPos pos) {
        if (this.m_9236_().getBlockEntity(pos) instanceof NuclearSirenBlockEntity nuclearSirenBlock) {
            nuclearSirenBlock.setNearestNuclearBomb(this);
        }
    }

    private void explode() {
        NuclearExplosionEntity explosion = ACEntityRegistry.NUCLEAR_EXPLOSION.get().create(this.m_9236_());
        explosion.m_20359_(this);
        explosion.setSize(1.0F);
        if (!this.m_9236_().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            explosion.setNoGriefing(true);
        }
        this.m_9236_().m_7967_(explosion);
    }

    public float getCloseProgress(float partialTick) {
        return this.prevCloseProgress + (this.closeProgress - this.prevCloseProgress) * partialTick;
    }

    public float getSirenAngle(float partialTick) {
        return this.prevSirenAngle + (this.sirenAngle - this.prevSirenAngle) * partialTick;
    }

    public float getExplodeProgress(float partialTick) {
        return (this.prevExplodeProgress + (this.explodeProgress - this.prevExplodeProgress) * partialTick) * 0.2F;
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.m_20185_() - this.f_19854_, flying ? this.m_20186_() - this.f_19855_ : 0.0, this.m_20189_() - this.f_19856_);
        float f2 = Math.min(f1 * 8.0F, 1.0F);
        this.f_267362_.update(f2, 0.4F);
    }

    public float getStepHeight() {
        return 1.1F;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ACSoundRegistry.NUCLEEPER_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ACSoundRegistry.NUCLEEPER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ACSoundRegistry.NUCLEEPER_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!this.m_6162_()) {
            this.m_5496_(ACSoundRegistry.NUCLEEPER_STEP.get(), 1.0F, 1.0F);
        }
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int experience, boolean idk) {
        super.m_7472_(damageSource, experience, idk);
        if (damageSource.getEntity() instanceof TremorzillaEntity && damageSource.is(ACDamageTypes.TREMORZILLA_BEAM)) {
            this.m_19998_(ACItemRegistry.MUSIC_DISC_FUSION_FRAGMENT.get());
        }
    }

    @Override
    public boolean shouldStopBlaringSirens() {
        return !this.isTriggered() && !this.isExploding() || this.m_213877_();
    }

    private class MeleeGoal extends Goal {

        public MeleeGoal() {
            this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = NucleeperEntity.this.m_5448_();
            return target != null && target.isAlive();
        }

        @Override
        public void tick() {
            LivingEntity target = NucleeperEntity.this.m_5448_();
            if (target != null && target.isAlive()) {
                NucleeperEntity.this.m_21573_().moveTo(target, 1.0);
                if (NucleeperEntity.this.m_20270_(target) < 3.5F + target.m_20205_()) {
                    NucleeperEntity.this.setTriggered(true);
                }
            }
        }
    }
}