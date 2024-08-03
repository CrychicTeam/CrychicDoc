package io.redspace.ironsspellbooks.entity.mobs.dead_king_boss;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.MinecraftInstanceHelper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class DeadKingCorpseEntity extends AbstractSpellCastingMob {

    public static final int ambienceRange = 32;

    DeadKingAmbienceSoundManager ambienceSoundManager;

    private static final EntityDataAccessor<Boolean> TRIGGERED = SynchedEntityData.defineId(DeadKingCorpseEntity.class, EntityDataSerializers.BOOLEAN);

    private int currentAnimTime;

    private final int animLength = 300;

    private final RawAnimation idle = RawAnimation.begin().thenLoop("dead_king_rest");

    private final RawAnimation rise = RawAnimation.begin().thenPlay("dead_king_rise");

    public DeadKingCorpseEntity(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.m_21530_();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.triggered()) {
            this.currentAnimTime++;
            if (!this.m_9236_().isClientSide) {
                if (this.currentAnimTime > 300) {
                    DeadKingBoss boss = new DeadKingBoss(this.m_9236_());
                    boss.m_20219_(this.m_20182_().add(0.0, 1.0, 0.0));
                    boss.finalizeSpawn((ServerLevel) this.m_9236_(), this.m_9236_().getCurrentDifficultyAt(boss.m_20097_()), MobSpawnType.TRIGGERED, null, null);
                    int playerCount = Math.max(this.m_9236_().m_45976_(Player.class, boss.m_20191_().inflate(32.0)).size(), 1);
                    boss.m_21204_().getInstance(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("Gank Health Bonus", (double) (playerCount - 1) * 0.5, AttributeModifier.Operation.MULTIPLY_BASE));
                    boss.m_21153_(boss.m_21233_());
                    boss.m_21204_().getInstance(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("Gank Damage Bonus", (double) (playerCount - 1) * 0.25, AttributeModifier.Operation.MULTIPLY_BASE));
                    boss.m_21204_().getInstance(AttributeRegistry.SPELL_RESIST.get()).addPermanentModifier(new AttributeModifier("Gank Spell Resist Bonus", (double) (playerCount - 1) * 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
                    boss.m_21530_();
                    this.m_9236_().m_7967_(boss);
                    MagicManager.spawnParticles(this.m_9236_(), ParticleTypes.SCULK_SOUL, this.m_20182_().x, this.m_20182_().y + 2.5, this.m_20182_().z, 80, 0.2, 0.2, 0.2, 0.25, true);
                    this.m_9236_().playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundRegistry.DEAD_KING_SPAWN.get(), SoundSource.MASTER, 20.0F, 1.0F);
                    this.m_146870_();
                }
            } else {
                this.resurrectParticles();
            }
        } else if (this.f_19853_.isClientSide && this.f_19797_ % 40 == 0) {
            MinecraftInstanceHelper.ifPlayerPresent(player -> {
                if (this.m_20280_(player) < 1024.0) {
                    if (this.ambienceSoundManager == null) {
                        this.ambienceSoundManager = new DeadKingAmbienceSoundManager(this);
                    }
                    this.ambienceSoundManager.trigger();
                }
            });
        }
    }

    private void resurrectParticles() {
        float f = (float) this.currentAnimTime / 300.0F;
        float rot = (float) (this.currentAnimTime * 12) + 1.0F + f * 15.0F;
        float height = f * 4.0F + 0.4F * Mth.sin((float) (this.currentAnimTime * 30) * (float) (Math.PI / 180.0)) * f * f;
        float distance = Mth.clamp(Utils.smoothstep(0.0F, 1.15F, f * 3.0F), 0.0F, 1.15F);
        Vec3 pos = new Vec3(0.0, 0.0, (double) distance).yRot(rot * (float) (Math.PI / 180.0)).add(0.0, (double) height, 0.0).add(this.m_20182_());
        this.f_19853_.addParticle(ParticleTypes.SCULK_SOUL, pos.x, pos.y, pos.z, 0.0, 0.0, 0.0);
        float radius = 4.0F;
        if (this.f_19796_.nextFloat() < f * 1.5F) {
            Vec3 random = this.m_20182_().add(new Vec3((double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * radius), 3.5 + (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * radius), (double) ((this.f_19796_.nextFloat() * 2.0F - 1.0F) * radius)));
            Vec3 motion = this.m_20182_().subtract(random).scale(0.04F);
            this.f_19853_.addParticle(ParticleTypes.SCULK_SOUL, random.x, random.y, random.z, motion.x, motion.y, motion.z);
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            this.m_146870_();
            return true;
        } else {
            Player player = this.f_19853_.m_45930_(this, 8.0);
            if (player != null) {
                this.trigger();
            }
            return false;
        }
    }

    @Override
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (!this.triggered()) {
            this.trigger();
            return InteractionResult.sidedSuccess(this.m_9236_().isClientSide);
        } else {
            return super.m_6071_(pPlayer, pHand);
        }
    }

    private void trigger() {
        if (!this.triggered()) {
            this.f_19853_.playSound(null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundRegistry.DEAD_KING_RESURRECT.get(), SoundSource.AMBIENT, 2.0F, 1.0F);
            this.f_19804_.set(TRIGGERED, true);
            if (this.ambienceSoundManager != null) {
                this.ambienceSoundManager.triggerStop();
            }
        }
    }

    public boolean triggered() {
        return this.f_19804_.get(TRIGGERED);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(TRIGGERED, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "idle", 0, this::idlePredicate));
    }

    private PlayState idlePredicate(AnimationState event) {
        if (this.triggered()) {
            event.getController().setAnimation(this.rise);
        } else {
            event.getController().setAnimation(this.idle);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public boolean shouldBeExtraAnimated() {
        return false;
    }

    @Override
    public boolean shouldAlwaysAnimateHead() {
        return false;
    }
}