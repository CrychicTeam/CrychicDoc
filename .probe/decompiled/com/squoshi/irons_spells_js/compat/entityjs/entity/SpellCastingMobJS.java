package com.squoshi.irons_spells_js.compat.entityjs.entity;

import com.google.common.collect.Maps;
import com.mojang.serialization.Dynamic;
import com.squoshi.irons_spells_js.compat.entityjs.entity.builder.SpellCastingMobJSBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import io.redspace.ironsspellbooks.spells.fire.BurningDashSpell;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.entities.nonliving.entityjs.PartEntityJS;
import net.liopyu.entityjs.events.AddGoalSelectorsEventJS;
import net.liopyu.entityjs.events.AddGoalTargetsEventJS;
import net.liopyu.entityjs.events.BuildBrainEventJS;
import net.liopyu.entityjs.events.BuildBrainProviderEventJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.liopyu.entityjs.util.EventHandlers;
import net.liopyu.entityjs.util.ModKeybinds;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.entity.PartEntity;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpellCastingMobJS extends AbstractSpellCastingMob implements IAnimatableJS, IMagicEntity {

    private static final EntityDataAccessor<Boolean> DATA_CANCEL_CAST = SynchedEntityData.defineId(SpellCastingMobJS.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Boolean> DATA_DRINKING_POTION = SynchedEntityData.defineId(SpellCastingMobJS.class, EntityDataSerializers.BOOLEAN);

    private final MagicData playerMagicData = new MagicData(true);

    private static final AttributeModifier SPEED_MODIFIER_DRINKING = new AttributeModifier(UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E"), "Drinking speed penalty", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Nullable
    private SpellData castingSpell;

    private final HashMap<String, AbstractSpell> spells = Maps.newHashMap();

    private int drinkTime;

    public boolean hasUsedSingleAttack;

    private AbstractSpell lastCastSpellType = SpellRegistry.none();

    private AbstractSpell instantCastSpellType = SpellRegistry.none();

    private final SpellCastingMobJSBuilder builder;

    private final AnimatableInstanceCache animationFactory;

    protected PathNavigation navigation;

    public final PartEntityJS<?>[] partEntities;

    private final NonNullList<ItemStack> handItems;

    private final NonNullList<ItemStack> armorItems;

    protected boolean thisJumping;

    public String entityName() {
        return this.m_6095_().toString();
    }

    public SpellCastingMobJS(SpellCastingMobJSBuilder builder, EntityType<? extends AbstractSpellCastingMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.playerMagicData.setSyncedData(new SyncedSpellData(this));
        this.f_21365_ = this.createLookControl();
        this.handItems = NonNullList.withSize(2, ItemStack.EMPTY);
        this.armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
        this.thisJumping = false;
        this.builder = builder;
        this.animationFactory = GeckoLibUtil.createInstanceCache(this);
        List<PartEntityJS<?>> tempPartEntities = new ArrayList();
        for (ContextUtils.PartEntityParams<SpellCastingMobJS> params : builder.partEntityParamsList) {
            PartEntityJS<?> partEntity = new PartEntityJS<>(this, params.name, params.width, params.height, params.builder);
            tempPartEntities.add(partEntity);
        }
        this.partEntities = (PartEntityJS<?>[]) tempPartEntities.toArray(new PartEntityJS[0]);
        this.navigation = this.createNavigation(pLevel);
    }

    @Override
    protected LookControl createLookControl() {
        return new LookControl(this) {

            @Override
            protected boolean resetXRotOnTick() {
                return SpellCastingMobJS.this.m_5448_() == null;
            }
        };
    }

    @Override
    public MagicData getMagicData() {
        return this.playerMagicData;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.f_19804_.define(DATA_CANCEL_CAST, false);
        this.f_19804_.define(DATA_DRINKING_POTION, false);
    }

    @Override
    public boolean isDrinkingPotion() {
        return this.f_19804_.get(DATA_DRINKING_POTION);
    }

    @Override
    protected void setDrinkingPotion(boolean drinkingPotion) {
        this.f_19804_.set(DATA_DRINKING_POTION, drinkingPotion);
    }

    @Override
    public void startDrinkingPotion() {
        if (!this.m_9236_().isClientSide) {
            this.setDrinkingPotion(true);
            this.drinkTime = 35;
            AttributeInstance attributeinstance = this.m_21051_(Attributes.MOVEMENT_SPEED);
            attributeinstance.removeModifier(SPEED_MODIFIER_DRINKING);
            attributeinstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
        }
    }

    private void finishDrinkingPotion() {
        this.setDrinkingPotion(false);
        this.heal(Math.min(Math.max(10.0F, this.m_21233_() / 10.0F), this.m_21233_() / 4.0F));
        this.m_21051_(Attributes.MOVEMENT_SPEED).removeModifier(SPEED_MODIFIER_DRINKING);
        if (!this.m_20067_()) {
            this.m_9236_().playSound((Player) null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.WITCH_DRINK, this.m_5720_(), 1.0F, 0.8F + this.f_19796_.nextFloat() * 0.4F);
        }
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
        super.onSyncedDataUpdated(pKey);
        if (this.m_9236_().isClientSide && pKey.getId() == DATA_CANCEL_CAST.getId()) {
            this.cancelCast();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.playerMagicData.getSyncedData().saveNBTData(pCompound);
        pCompound.putBoolean("usedSpecial", this.hasUsedSingleAttack);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        SyncedSpellData syncedSpellData = new SyncedSpellData(this);
        syncedSpellData.loadNBTData(pCompound);
        if (syncedSpellData.isCasting()) {
            AbstractSpell spell = SpellRegistry.getSpell(syncedSpellData.getCastingSpellId());
            this.initiateCastSpell(spell, syncedSpellData.getCastingSpellLevel());
        }
        this.playerMagicData.setSyncedData(syncedSpellData);
        this.hasUsedSingleAttack = pCompound.getBoolean("usedSpecial");
    }

    @Override
    public void cancelCast() {
        if (this.builder.onCancelledCast != null) {
            this.builder.onCancelledCast.accept(this);
        }
        if (this.isCasting()) {
            if (!this.m_9236_().isClientSide) {
                this.f_19804_.set(DATA_CANCEL_CAST, !this.f_19804_.get(DATA_CANCEL_CAST));
            }
            this.castComplete();
        }
    }

    private void castComplete() {
        if (!this.m_9236_().isClientSide) {
            if (this.castingSpell != null) {
                this.castingSpell.getSpell().onServerCastComplete(this.m_9236_(), this.castingSpell.getLevel(), this, this.playerMagicData, false);
            }
        } else {
            this.playerMagicData.resetCastingState();
        }
        this.castingSpell = null;
    }

    @Override
    public void startAutoSpinAttack(int pAttackTicks) {
        this.f_20938_ = pAttackTicks;
        if (!this.m_9236_().isClientSide) {
            this.m_21155_(4, true);
        }
        this.m_146922_((float) (Math.atan2(this.m_20184_().x, this.m_20184_().z) * 180.0F / (float) Math.PI));
    }

    @Override
    public void setSyncedSpellData(SyncedSpellData syncedSpellData) {
        if (this.m_9236_().isClientSide) {
            boolean isCasting = this.playerMagicData.isCasting();
            this.playerMagicData.setSyncedData(syncedSpellData);
            this.castingSpell = this.playerMagicData.getCastingSpell();
            if (this.castingSpell != null) {
                if (!this.playerMagicData.isCasting() && isCasting) {
                    this.castComplete();
                } else if (this.playerMagicData.isCasting() && !isCasting) {
                    AbstractSpell spell = this.playerMagicData.getCastingSpell().getSpell();
                    this.initiateCastSpell(spell, this.playerMagicData.getCastingSpellLevel());
                    if (this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                        this.instantCastSpellType = this.castingSpell.getSpell();
                        this.castingSpell.getSpell().onClientPreCast(this.m_9236_(), this.castingSpell.getLevel(), this, InteractionHand.MAIN_HAND, this.playerMagicData);
                        this.castComplete();
                    }
                }
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isDrinkingPotion()) {
            if (this.drinkTime-- <= 0) {
                this.finishDrinkingPotion();
            } else if (this.drinkTime % 4 == 0 && !this.m_20067_()) {
                this.m_9236_().playSound((Player) null, this.m_20185_(), this.m_20186_(), this.m_20189_(), SoundEvents.GENERIC_DRINK, this.m_5720_(), 1.0F, Utils.random.nextFloat() * 0.1F + 0.9F);
            }
        }
        if (this.castingSpell != null) {
            this.playerMagicData.handleCastDuration();
            if (this.playerMagicData.isCasting()) {
                this.castingSpell.getSpell().onServerCastTick(this.m_9236_(), this.castingSpell.getLevel(), this, this.playerMagicData);
            }
            this.forceLookAtTarget(this.m_5448_());
            if (this.playerMagicData.getCastDurationRemaining() <= 0) {
                if (this.castingSpell.getSpell().getCastType() == CastType.LONG || this.castingSpell.getSpell().getCastType() == CastType.INSTANT) {
                    this.castingSpell.getSpell().onCast(this.m_9236_(), this.castingSpell.getLevel(), this, CastSource.MOB, this.playerMagicData);
                }
                this.castComplete();
            } else if (this.castingSpell.getSpell().getCastType() == CastType.CONTINUOUS && (this.playerMagicData.getCastDurationRemaining() + 1) % 10 == 0) {
                this.castingSpell.getSpell().onCast(this.m_9236_(), this.castingSpell.getLevel(), this, CastSource.MOB, this.playerMagicData);
            }
        }
    }

    @Override
    public void initiateCastSpell(AbstractSpell spell, int spellLevel) {
        if (spell == SpellRegistry.none()) {
            this.castingSpell = null;
        } else {
            this.castingSpell = new SpellData(spell, spellLevel);
            if (this.m_5448_() != null) {
                this.forceLookAtTarget(this.m_5448_());
            }
            if (!this.m_9236_().isClientSide && !this.castingSpell.getSpell().checkPreCastConditions(this.m_9236_(), spellLevel, this, this.playerMagicData)) {
                this.castingSpell = null;
            } else {
                if (spell == SpellRegistry.TELEPORT_SPELL.get() || spell == SpellRegistry.FROST_STEP_SPELL.get()) {
                    this.setTeleportLocationBehindTarget(10);
                } else if (spell == SpellRegistry.BLOOD_STEP_SPELL.get()) {
                    this.setTeleportLocationBehindTarget(3);
                } else if (spell == SpellRegistry.BURNING_DASH_SPELL.get()) {
                    this.setBurningDashDirectionData();
                }
                this.playerMagicData.initiateCast(this.castingSpell.getSpell(), this.castingSpell.getLevel(), this.castingSpell.getSpell().getEffectiveCastTime(this.castingSpell.getLevel(), this), CastSource.MOB, SpellSelectionManager.MAINHAND);
                if (!this.m_9236_().isClientSide) {
                    this.castingSpell.getSpell().onServerPreCast(this.m_9236_(), this.castingSpell.getLevel(), this, this.playerMagicData);
                }
            }
        }
    }

    @Override
    public void notifyDangerousProjectile(Projectile projectile) {
    }

    @Override
    public boolean isCasting() {
        if (this.builder.isCasting != null) {
            Object obj = this.builder.isCasting.apply(this);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[KubeJS Irons Spells]: Invalid return value for isCasting from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + this.playerMagicData.isCasting());
        }
        return this.playerMagicData.isCasting();
    }

    @Override
    public boolean setTeleportLocationBehindTarget(int distance) {
        LivingEntity target = this.m_5448_();
        boolean valid = false;
        if (target != null) {
            Vec3 rotation = target.m_20154_().normalize().scale((double) (-distance));
            Vec3 pos = target.m_20182_();
            Vec3 teleportPos = rotation.add(pos);
            for (int i = 0; i < 24; i++) {
                Vec3 randomness = Utils.getRandomVec3((double) (0.15F * (float) i)).multiply(1.0, 0.0, 1.0);
                Vec3 var10 = Utils.moveToRelativeGroundLevel(this.m_9236_(), target.m_20182_().subtract(new Vec3(0.0, 0.0, (double) ((float) distance / (float) (i / 7 + 1))).yRot(-(target.m_146908_() + (float) (i * 45)) * (float) (Math.PI / 180.0))).add(randomness), 5);
                teleportPos = new Vec3(var10.x, var10.y + 0.1F, var10.z);
                AABB reposBB = this.m_20191_().move(teleportPos.subtract(this.m_20182_()));
                if (!this.m_9236_().m_186437_(this, reposBB.inflate(-0.05F))) {
                    valid = true;
                    break;
                }
            }
            if (valid) {
                this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(teleportPos));
            } else {
                this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.m_20182_()));
            }
        } else {
            this.playerMagicData.setAdditionalCastData(new TeleportSpell.TeleportData(this.m_20182_()));
        }
        return valid;
    }

    @Override
    public void setBurningDashDirectionData() {
        this.playerMagicData.setAdditionalCastData(new BurningDashSpell.BurningDashDirectionOverrideCastData());
    }

    private void forceLookAtTarget(LivingEntity target) {
        if (target != null) {
            double d0 = target.m_20185_() - this.m_20185_();
            double d2 = target.m_20189_() - this.m_20189_();
            double d1 = target.m_20188_() - this.m_20188_();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
            float f1 = (float) (-(Mth.atan2(d1, d3) * 180.0F / (float) Math.PI));
            this.m_146926_(f1 % 360.0F);
            this.m_146922_(f % 360.0F);
        }
    }

    private void addClientSideParticles() {
        double d0 = 0.4;
        double d1 = 0.3;
        double d2 = 0.35;
        float f = this.f_20883_ * (float) (Math.PI / 180.0) + Mth.cos((float) this.f_19797_ * 0.6662F) * 0.25F;
        float f1 = Mth.cos(f);
        float f2 = Mth.sin(f);
        this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() + (double) f1 * 0.6, this.m_20186_() + 1.8, this.m_20189_() + (double) f2 * 0.6, d0, d1, d2);
        this.m_9236_().addParticle(ParticleTypes.ENTITY_EFFECT, this.m_20185_() - (double) f1 * 0.6, this.m_20186_() + 1.8, this.m_20189_() - (double) f2 * 0.6, d0, d1, d2);
    }

    @Override
    public void setId(int entityId) {
        super.m_20234_(entityId);
        for (int i = 0; i < this.partEntities.length; i++) {
            PartEntityJS<?> partEntity = this.partEntities[i];
            if (partEntity != null) {
                partEntity.m_20234_(entityId + i + 1);
            }
        }
    }

    public void tickPart(String partName, double offsetX, double offsetY, double offsetZ) {
        double x = this.m_20185_();
        double y = this.m_20186_();
        double z = this.m_20189_();
        for (PartEntityJS<?> partEntity : this.partEntities) {
            if (partEntity.name.equals(partName)) {
                partEntity.movePart(x + offsetX, y + offsetY, z + offsetZ, partEntity.m_146908_(), partEntity.m_146909_());
                return;
            }
        }
        EntityJSHelperClass.logWarningMessageOnce("Part with name " + partName + " not found for entity: " + this.entityName());
    }

    public boolean isMultipartEntity() {
        return this.partEntities != null;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.m_141965_(pPacket);
    }

    public PartEntity<?>[] getParts() {
        return (PartEntity<?>[]) Objects.requireNonNullElseGet(this.partEntities, () -> new PartEntity[0]);
    }

    @Override
    public BaseLivingEntityBuilder<?> getBuilder() {
        return this.builder;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationFactory;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        if (EventHandlers.buildBrainProvider.hasListeners()) {
            BuildBrainProviderEventJS<SpellCastingMobJS> event = new BuildBrainProviderEventJS<>();
            EventHandlers.buildBrainProvider.post(event, this.getTypeId());
            return event.provide();
        } else {
            return super.m_5490_();
        }
    }

    @Override
    protected Brain<SpellCastingMobJS> makeBrain(Dynamic<?> dynamic0) {
        if (EventHandlers.buildBrain.hasListeners()) {
            Brain<SpellCastingMobJS> brain = UtilsJS.cast(this.brainProvider().makeBrain(dynamic0));
            EventHandlers.buildBrain.post(new BuildBrainEventJS<>(brain), this.getTypeId());
            return brain;
        } else {
            return UtilsJS.cast(super.m_8075_(dynamic0));
        }
    }

    @Override
    protected void registerGoals() {
        if (EventHandlers.addGoalTargets.hasListeners()) {
            EventHandlers.addGoalTargets.post(new AddGoalTargetsEventJS<>(this, this.f_21346_), this.getTypeId());
        }
        if (EventHandlers.addGoalSelectors.hasListeners()) {
            EventHandlers.addGoalSelectors.post(new AddGoalSelectorsEventJS<>(this, this.f_21345_), this.getTypeId());
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.builder.onInteract != null) {
            ContextUtils.MobInteractContext context = new ContextUtils.MobInteractContext(this, pPlayer, pHand);
            EntityJSHelperClass.consumerCallback(this.builder.onInteract, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onInteract.");
        }
        return super.m_6071_(pPlayer, pHand);
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (this.builder != null && this.builder.onHurtTarget != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(pEntity, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHurtTarget, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurtTarget.");
        }
        return super.m_7327_(pEntity);
    }

    public void onJump() {
        if (this.builder.onLivingJump != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onLivingJump, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingJump.");
        }
    }

    @Override
    public void aiStep() {
        super.m_8107_();
        if (this.canJump() && this.m_20096_() && this.m_21573_().isInProgress() && this.shouldJump()) {
            this.jump();
        }
        if (this.builder.aiStep != null) {
            EntityJSHelperClass.consumerCallback(this.builder.aiStep, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: aiStep.");
        }
    }

    @Override
    protected void tickDeath() {
        if (this.builder.tickDeath != null) {
            EntityJSHelperClass.consumerCallback(this.builder.tickDeath, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tickDeath.");
        } else {
            super.m_6153_();
        }
    }

    @Override
    protected void tickLeash() {
        super.m_6119_();
        if (this.builder.tickLeash != null) {
            Player $$0 = (Player) this.m_21524_();
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext($$0, this);
            EntityJSHelperClass.consumerCallback(this.builder.tickLeash, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: tickLeash.");
        }
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        if (this.builder.canBeLeashed != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(pPlayer, this);
            Object obj = this.builder.canBeLeashed.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canBeLeashed from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to false.");
        }
        return false;
    }

    @Override
    public void setTarget(@org.jetbrains.annotations.Nullable LivingEntity target) {
        super.m_6710_(target);
        if (this.builder.onTargetChanged != null) {
            ContextUtils.TargetChangeContext context = new ContextUtils.TargetChangeContext(target, this);
            EntityJSHelperClass.consumerCallback(this.builder.onTargetChanged, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onTargetChanged.");
        }
    }

    @Override
    public void ate() {
        super.m_8035_();
        if (this.builder.ate != null) {
            EntityJSHelperClass.consumerCallback(this.builder.ate, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: ate.");
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        if (this.builder != null && this.builder.createNavigation != null) {
            ContextUtils.EntityLevelContext context = new ContextUtils.EntityLevelContext(pLevel, this);
            Object obj = this.builder.createNavigation.apply(context);
            if (obj instanceof PathNavigation) {
                return (PathNavigation) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for createNavigation from entity: " + var10000 + ". Value: " + obj + ". Must be PathNavigation. Defaulting to super method.");
                return super.m_6037_(pLevel);
            }
        } else {
            return super.m_6037_(pLevel);
        }
    }

    @Override
    public MobType getMobType() {
        return this.builder != null ? this.builder.mobType : super.m_6336_();
    }

    public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
        ItemStack itemstack = this.m_6298_(this.m_21120_(ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof BowItem)));
        AbstractArrow abstractarrow = this.getArrow(itemstack, pDistanceFactor);
        if (this.m_21205_().getItem() instanceof BowItem) {
            abstractarrow = ((BowItem) this.m_21205_().getItem()).customArrow(abstractarrow);
        }
        double d0 = pTarget.m_20185_() - this.m_20185_();
        double d1 = pTarget.m_20227_(0.3333333333333333) - abstractarrow.m_20186_();
        double d2 = pTarget.m_20189_() - this.m_20189_();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
        this.m_5496_(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
        this.m_9236_().m_7967_(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack pArrowStack, float pVelocity) {
        return ProjectileUtil.getMobArrow(this, pArrowStack, pVelocity);
    }

    public boolean canJump() {
        return (Boolean) Objects.requireNonNullElse(this.builder.canJump, true);
    }

    public void jump() {
        double jumpPower = (double) (this.m_6118_() + this.getJumpBoostPower());
        Vec3 currentVelocity = this.m_20184_();
        this.m_20334_(currentVelocity.x, jumpPower, currentVelocity.z);
        this.f_19812_ = true;
        if (this.m_20142_()) {
            float yawRadians = this.m_146908_() * (float) (Math.PI / 180.0);
            this.m_20256_(this.m_20184_().add(-Math.sin((double) yawRadians) * 0.2, 0.0, Math.cos((double) yawRadians) * 0.2));
        }
        this.f_19812_ = true;
        this.onJump();
        ForgeHooks.onLivingJump(this);
    }

    public boolean shouldJump() {
        BlockPos forwardPos = this.m_20183_().relative(this.m_6350_());
        return this.m_9236_().loadedAndEntityCanStandOn(forwardPos, this) && (double) this.getStepHeight() < this.m_9236_().getBlockState(forwardPos).m_60808_(this.m_9236_(), forwardPos).max(Direction.Axis.Y);
    }

    @Override
    public HumanoidArm getMainArm() {
        return this.builder.mainArm != null ? (HumanoidArm) this.builder.mainArm : super.m_5737_();
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader levelReader) {
        if (this.builder.walkTargetValue == null) {
            return super.m_5610_(pos, levelReader);
        } else {
            ContextUtils.EntityBlockPosLevelContext context = new ContextUtils.EntityBlockPosLevelContext(pos, levelReader, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.walkTargetValue.apply(context), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for walkTargetValue from entity: " + var10000 + ". Value: " + this.builder.walkTargetValue.apply(context) + ". Must be a float. Defaulting to " + super.m_5610_(pos, levelReader));
                return super.m_5610_(pos, levelReader);
            }
        }
    }

    @Override
    protected boolean shouldStayCloseToLeashHolder() {
        if (this.builder.shouldStayCloseToLeashHolder == null) {
            return super.m_213814_();
        } else {
            Object value = this.builder.shouldStayCloseToLeashHolder.apply(this);
            if (value instanceof Boolean b) {
                return b;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldStayCloseToLeashHolder from entity: " + this.entityName() + ". Value: " + value + ". Must be a boolean. Defaulting to " + super.m_213814_());
                return super.m_213814_();
            }
        }
    }

    public boolean canFireProjectileWeaponPredicate(ProjectileWeaponItem projectileWeapon) {
        if (this.builder.canFireProjectileWeaponPredicate != null) {
            ContextUtils.EntityProjectileWeaponContext context = new ContextUtils.EntityProjectileWeaponContext(projectileWeapon, this);
            Object obj = this.builder.canFireProjectileWeaponPredicate.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            String var10000 = this.entityName();
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canFireProjectileWeaponPredicate from entity: " + var10000 + ". Value: " + obj + ". Must be a boolean. Defaulting to false.");
        }
        return false;
    }

    public boolean canFireProjectileWeapons(ProjectileWeaponItem projectileWeapon) {
        return this.builder.canFireProjectileWeapon == null ? super.m_5886_(projectileWeapon) : this.builder.canFireProjectileWeapon.test(projectileWeapon.m_7968_()) && projectileWeapon instanceof ProjectileWeaponItem;
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeapon) {
        return !this.canFireProjectileWeapons(projectileWeapon) && !this.canFireProjectileWeaponPredicate(projectileWeapon) ? super.m_5886_(projectileWeapon) : this.canFireProjectileWeapons(projectileWeapon) && this.canFireProjectileWeaponPredicate(projectileWeapon);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.builder.setAmbientSound != null ? ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setAmbientSound) : super.m_7515_();
    }

    @Override
    public boolean canHoldItem(ItemStack stack) {
        if (this.builder.canHoldItem != null) {
            ContextUtils.EntityItemStackContext context = new ContextUtils.EntityItemStackContext(stack, this);
            Object obj = this.builder.canHoldItem.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canHoldItem from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7252_(stack));
        }
        return super.m_7252_(stack);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.shouldDespawnInPeaceful, () -> super.m_8028_());
    }

    @Override
    public boolean isPersistenceRequired() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.isPersistenceRequired, () -> super.m_21532_());
    }

    @Override
    public double getMeleeAttackRangeSqr(LivingEntity entity) {
        if (this.builder.meleeAttackRangeSqr != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.meleeAttackRangeSqr.apply(this), "double");
            if (obj != null) {
                return (Double) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for meleeAttackRangeSqr from entity: " + var10000 + ". Value: " + this.builder.meleeAttackRangeSqr.apply(this) + ". Must be a double. Defaulting to " + super.m_142593_(entity));
                return super.m_142593_(entity);
            }
        } else {
            return super.m_142593_(entity);
        }
    }

    @Override
    public int getAmbientSoundInterval() {
        return this.builder.ambientSoundInterval != null ? (Integer) this.builder.ambientSoundInterval : super.m_8100_();
    }

    @Override
    public double getMyRidingOffset() {
        if (this.builder.myRidingOffset == null) {
            return super.getMyRidingOffset();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.myRidingOffset.apply(this), "double");
            if (obj != null) {
                return (Double) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for myRidingOffset from entity: " + var10000 + ". Value: " + this.builder.myRidingOffset.apply(this) + ". Must be a double. Defaulting to " + super.getMyRidingOffset());
                return super.getMyRidingOffset();
            }
        }
    }

    @Override
    protected double followLeashSpeed() {
        return (Double) Objects.requireNonNullElseGet(this.builder.followLeashSpeed, () -> super.m_5823_());
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        if (this.builder.removeWhenFarAway == null) {
            return super.m_6785_(pDistanceToClosestPlayer);
        } else {
            ContextUtils.EntityDistanceToPlayerContext context = new ContextUtils.EntityDistanceToPlayerContext(pDistanceToClosestPlayer, this);
            Object obj = this.builder.removeWhenFarAway.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for removeWhenFarAway from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6785_(pDistanceToClosestPlayer));
                return super.m_6785_(pDistanceToClosestPlayer);
            }
        }
    }

    @Override
    public boolean isAlliedTo(Entity pEntity) {
        if (this.builder.isAlliedTo != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(pEntity, this);
            Object obj = this.builder.isAlliedTo.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAlliedTo from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7307_(pEntity));
        }
        return super.m_7307_(pEntity);
    }

    @Override
    public void travel(Vec3 pTravelVector) {
        LivingEntity livingentity = this.getControllingPassenger();
        if (this.m_6084_() && this.m_20160_() && this.builder.canSteer && livingentity != null) {
            if (this.getControllingPassenger() instanceof Player && this.builder.mountJumpingEnabled) {
                if (this.ableToJump()) {
                    this.setThisJumping(true);
                }
                if (this.thisJumping) {
                    this.setThisJumping(false);
                    double jumpPower = (double) (this.m_6118_() + this.getJumpBoostPower());
                    Vec3 currentVelocity = this.m_20184_();
                    double newVelocityX = currentVelocity.x;
                    double newVelocityY = currentVelocity.y + jumpPower;
                    double newVelocityZ = currentVelocity.z;
                    this.m_20334_(newVelocityX, newVelocityY, newVelocityZ);
                    this.onJump();
                    ForgeHooks.onLivingJump(this);
                }
            }
            LivingEntity passenger = this.getControllingPassenger();
            this.f_19859_ = this.m_146908_();
            this.f_19860_ = this.m_146909_();
            this.m_146922_(passenger.m_146908_());
            this.m_146926_(passenger.m_146909_() * 0.5F);
            this.m_19915_(this.m_146908_(), this.m_146909_());
            this.f_20883_ = this.m_146908_();
            this.f_20885_ = this.f_20883_;
            float x = passenger.xxa * 0.5F;
            float z = passenger.zza;
            if (z <= 0.0F) {
                z *= 0.25F;
            }
            this.m_7910_((float) this.m_21133_(Attributes.MOVEMENT_SPEED));
            super.m_7023_(new Vec3((double) x, pTravelVector.y, (double) z));
        } else {
            super.m_7023_(pTravelVector);
        }
        if (this.builder.travel != null) {
            ContextUtils.Vec3Context context = new ContextUtils.Vec3Context(pTravelVector, this);
            EntityJSHelperClass.consumerCallback(this.builder.travel, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: travel.");
        }
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.builder.tick != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.tick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: tick.");
        }
    }

    public void onAddedToWorld() {
        super.onAddedToWorld();
        if (this.builder.onAddedToWorld != null && !this.m_9236_().isClientSide()) {
            EntityJSHelperClass.consumerCallback(this.builder.onAddedToWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onAddedToWorld.");
        }
    }

    @Override
    protected void doAutoAttackOnTouch(@NotNull LivingEntity target) {
        super.m_6727_(target);
        if (this.builder.doAutoAttackOnTouch != null) {
            ContextUtils.AutoAttackContext context = new ContextUtils.AutoAttackContext(this, target);
            EntityJSHelperClass.consumerCallback(this.builder.doAutoAttackOnTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: doAutoAttackOnTouch.");
        }
    }

    @Override
    protected int decreaseAirSupply(int int0) {
        if (this.builder.onDecreaseAirSupply != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onDecreaseAirSupply, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onDecreaseAirSupply.");
        }
        return super.m_7302_(int0);
    }

    @Override
    protected int increaseAirSupply(int int0) {
        if (this.builder.onIncreaseAirSupply != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onIncreaseAirSupply, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onIncreaseAirSupply.");
        }
        return super.m_7305_(int0);
    }

    @Override
    protected void blockedByShield(@NotNull LivingEntity livingEntity0) {
        super.m_6731_(livingEntity0);
        if (this.builder.onBlockedByShield != null) {
            ContextUtils.LivingEntityContext context = new ContextUtils.LivingEntityContext(this, livingEntity0);
            EntityJSHelperClass.consumerCallback(this.builder.onBlockedByShield, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onDecreaseAirSupply.");
        }
    }

    @Override
    public void onEquipItem(EquipmentSlot slot, ItemStack previous, ItemStack current) {
        super.m_238392_(slot, previous, current);
        if (this.builder.onEquipItem != null) {
            ContextUtils.EntityEquipmentContext context = new ContextUtils.EntityEquipmentContext(slot, previous, current, this);
            EntityJSHelperClass.consumerCallback(this.builder.onEquipItem, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEquipItem.");
        }
    }

    @Override
    public void onEffectAdded(@NotNull MobEffectInstance effectInstance, @org.jetbrains.annotations.Nullable Entity entity) {
        if (this.builder.onEffectAdded != null) {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(effectInstance, this);
            EntityJSHelperClass.consumerCallback(this.builder.onEffectAdded, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEffectAdded.");
        } else {
            super.m_142540_(effectInstance, entity);
        }
    }

    @Override
    protected void onEffectRemoved(@NotNull MobEffectInstance effectInstance) {
        if (this.builder.onEffectRemoved != null) {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(effectInstance, this);
            EntityJSHelperClass.consumerCallback(this.builder.onEffectRemoved, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEffectRemoved.");
        } else {
            super.m_7285_(effectInstance);
        }
    }

    @Override
    public void heal(float amount) {
        super.m_5634_(amount);
        if (this.builder.onLivingHeal != null) {
            ContextUtils.EntityHealContext context = new ContextUtils.EntityHealContext(this, amount);
            EntityJSHelperClass.consumerCallback(this.builder.onLivingHeal, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingHeal.");
        }
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
        super.m_6667_(damageSource);
        if (this.builder.onDeath != null) {
            ContextUtils.DeathContext context = new ContextUtils.DeathContext(this, damageSource);
            EntityJSHelperClass.consumerCallback(this.builder.onDeath, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onDeath.");
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource damageSource, int lootingMultiplier, boolean allowDrops) {
        if (this.builder.dropCustomDeathLoot != null) {
            ContextUtils.EntityLootContext context = new ContextUtils.EntityLootContext(damageSource, lootingMultiplier, allowDrops, this);
            EntityJSHelperClass.consumerCallback(this.builder.dropCustomDeathLoot, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: dropCustomDeathLoot.");
        } else {
            super.m_7472_(damageSource, lootingMultiplier, allowDrops);
        }
    }

    @Override
    protected void onFlap() {
        if (this.builder.onFlap != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onFlap, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onFlap.");
        }
        super.m_142043_();
    }

    public boolean ableToJump() {
        return ModKeybinds.mount_jump.isDown() && this.m_20096_();
    }

    public void setThisJumping(boolean value) {
        this.thisJumping = value;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        LivingEntity var10000;
        if (this.m_146895_() instanceof LivingEntity entity) {
            var10000 = entity;
        } else {
            var10000 = null;
        }
        return var10000;
    }

    @Info("Calls a triggerable animation to be played anywhere.\n")
    public void triggerAnimation(String controllerName, String animName) {
        this.triggerAnim(controllerName, animName);
    }

    @Override
    public boolean canCollideWith(Entity pEntity) {
        if (this.builder.canCollideWith != null) {
            ContextUtils.CollidingEntityContext context = new ContextUtils.CollidingEntityContext(this, pEntity);
            Object obj = this.builder.canCollideWith.apply(context);
            if (obj instanceof Boolean b) {
                return b;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canCollideWith from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7337_(pEntity));
        }
        return super.m_7337_(pEntity);
    }

    @Override
    protected float getSoundVolume() {
        return (Float) Objects.requireNonNullElseGet(this.builder.setSoundVolume, () -> super.m_6121_());
    }

    @Override
    protected float getWaterSlowDown() {
        return (Float) Objects.requireNonNullElseGet(this.builder.setWaterSlowDown, () -> super.m_6108_());
    }

    @Override
    protected float getBlockJumpFactor() {
        if (this.builder.setBlockJumpFactor == null) {
            return super.m_20098_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setBlockJumpFactor.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setBlockJumpFactor from entity: " + var10000 + ". Value: " + this.builder.setBlockJumpFactor.apply(this) + ". Must be a float. Defaulting to " + super.m_20098_());
                return super.m_20098_();
            }
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        if (this.builder != null && this.builder.setStandingEyeHeight != null) {
            ContextUtils.EntityPoseDimensionsContext context = new ContextUtils.EntityPoseDimensionsContext(pPose, pDimensions, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setStandingEyeHeight.apply(context), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setStandingEyeHeight from entity: " + var10000 + ". Value: " + this.builder.setStandingEyeHeight.apply(context) + ". Must be a float. Defaulting to " + super.m_6431_(pPose, pDimensions));
                return super.m_6431_(pPose, pDimensions);
            }
        } else {
            return super.m_6431_(pPose, pDimensions);
        }
    }

    @Override
    public boolean isPushable() {
        return this.builder.isPushable;
    }

    @Override
    protected float getBlockSpeedFactor() {
        if (this.builder.blockSpeedFactor == null) {
            return super.m_6041_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.blockSpeedFactor.apply(this), "float");
            if (this.builder.blockSpeedFactor == null) {
                return super.m_6041_();
            } else if (obj != null) {
                return (Float) obj;
            } else {
                Object var10000 = this.builder.get();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for blockSpeedFactor from entity: " + var10000 + ". Value: " + this.builder.blockSpeedFactor.apply(this) + ". Must be a float, defaulting to " + super.m_6041_());
                return super.m_6041_();
            }
        }
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity entity) {
        if (this.builder.canAddPassenger == null) {
            return super.m_7310_(entity);
        } else {
            ContextUtils.PassengerEntityContext context = new ContextUtils.PassengerEntityContext(entity, this);
            Object obj = this.builder.canAddPassenger.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAddPassenger from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + super.m_7310_(entity));
                return super.m_7310_(entity);
            }
        }
    }

    @Override
    protected boolean shouldDropLoot() {
        if (this.builder.shouldDropLoot != null) {
            Object obj = this.builder.shouldDropLoot.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldDropLoot from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean, defaulting to " + super.m_6125_());
        }
        return super.m_6125_();
    }

    @Override
    protected boolean isAffectedByFluids() {
        if (this.builder.isAffectedByFluids != null) {
            Object obj = this.builder.isAffectedByFluids.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAffectedByFluids from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6129_());
        }
        return super.m_6129_();
    }

    @Override
    protected boolean isAlwaysExperienceDropper() {
        return this.builder.isAlwaysExperienceDropper;
    }

    @Override
    protected boolean isImmobile() {
        if (this.builder.isImmobile != null) {
            Object obj = this.builder.isImmobile.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isImmobile from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6107_());
        }
        return super.m_6107_();
    }

    @Override
    protected boolean isFlapping() {
        if (this.builder.isFlapping != null) {
            Object obj = this.builder.isFlapping.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isFlapping from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142039_());
        }
        return super.m_142039_();
    }

    @Override
    public int calculateFallDamage(float fallDistance, float pDamageMultiplier) {
        if (this.builder.calculateFallDamage == null) {
            return super.m_5639_(fallDistance, pDamageMultiplier);
        } else {
            ContextUtils.CalculateFallDamageContext context = new ContextUtils.CalculateFallDamageContext(fallDistance, pDamageMultiplier, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.calculateFallDamage.apply(context), "integer");
            if (obj != null) {
                return (Integer) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for calculateFallDamage from entity: " + var10000 + ". Value: " + this.builder.calculateFallDamage.apply(context) + ". Must be an int, defaulting to " + super.m_5639_(fallDistance, pDamageMultiplier));
                return super.m_5639_(fallDistance, pDamageMultiplier);
            }
        }
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.repositionEntityAfterLoad, () -> super.m_6093_());
    }

    @Override
    protected float nextStep() {
        if (this.builder.nextStep != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.nextStep.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            }
            String var10000 = this.entityName();
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for nextStep from entity: " + var10000 + ". Value: " + this.builder.nextStep.apply(this) + ". Must be a float, defaulting to " + super.m_6059_());
        }
        return super.m_6059_();
    }

    @org.jetbrains.annotations.Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource0) {
        if (this.builder.setHurtSound == null) {
            return super.m_7975_(damageSource0);
        } else {
            ContextUtils.HurtContext context = new ContextUtils.HurtContext(this, damageSource0);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setHurtSound.apply(context), "resourcelocation");
            if (obj != null) {
                return (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) obj));
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setHurtSound from entity: " + var10000 + ". Value: " + this.builder.setHurtSound.apply(context) + ". Must be a ResourceLocation or String. Defaulting to \"minecraft:entity.generic.hurt\"");
                return super.m_7975_(damageSource0);
            }
        }
    }

    @Override
    protected SoundEvent getSwimSplashSound() {
        return this.builder.setSwimSplashSound == null ? super.m_5509_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSplashSound));
    }

    @Override
    protected SoundEvent getSwimSound() {
        return this.builder.setSwimSound == null ? super.m_5501_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setSwimSound));
    }

    @Override
    public boolean canAttackType(@NotNull EntityType<?> entityType) {
        if (this.builder.canAttackType != null) {
            ContextUtils.EntityTypeEntityContext context = new ContextUtils.EntityTypeEntityContext(this, entityType);
            Object obj = this.builder.canAttackType.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAttackType from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6549_(entityType));
        }
        return super.m_6549_(entityType);
    }

    @Override
    public float getScale() {
        if (this.builder.scale == null) {
            return super.m_6134_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.scale.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for scale from entity: " + var10000 + ". Value: " + this.builder.scale.apply(this) + ". Must be a float. Defaulting to " + super.m_6134_());
                return super.m_6134_();
            }
        }
    }

    @Override
    public boolean shouldDropExperience() {
        if (this.builder.shouldDropExperience != null) {
            Object obj = this.builder.shouldDropExperience.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldDropExperience from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6149_());
        }
        return super.m_6149_();
    }

    @Override
    public double getVisibilityPercent(@org.jetbrains.annotations.Nullable Entity entity0) {
        if (this.builder.visibilityPercent != null) {
            ContextUtils.VisualContext context = new ContextUtils.VisualContext(entity0, this);
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.visibilityPercent.apply(context), "double");
            if (obj != null) {
                return (Double) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for visibilityPercent from entity: " + var10000 + ". Value: " + this.builder.visibilityPercent.apply(context) + ". Must be a double. Defaulting to " + super.m_20968_(entity0));
                return super.m_20968_(entity0);
            }
        } else {
            return super.m_20968_(entity0);
        }
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity entity) {
        if (this.builder.canAttack != null) {
            ContextUtils.LivingEntityContext context = new ContextUtils.LivingEntityContext(this, entity);
            Object obj = this.builder.canAttack.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj && super.m_6779_(entity);
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canAttack from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6779_(entity));
        }
        return super.m_6779_(entity);
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effectInstance) {
        if (this.builder.canBeAffected == null) {
            return super.m_7301_(effectInstance);
        } else {
            ContextUtils.OnEffectContext context = new ContextUtils.OnEffectContext(effectInstance, this);
            Object result = this.builder.canBeAffected.apply(context);
            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canBeAffected from entity: " + this.entityName() + ". Value: " + result + ". Must be a boolean. Defaulting to " + super.m_7301_(effectInstance));
                return super.m_7301_(effectInstance);
            }
        }
    }

    @Override
    public boolean isInvertedHealAndHarm() {
        if (this.builder.invertedHealAndHarm == null) {
            return super.m_21222_();
        } else {
            Object obj = this.builder.invertedHealAndHarm.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for invertedHealAndHarm from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_21222_());
                return super.m_21222_();
            }
        }
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.builder.setDeathSound == null ? super.m_5592_() : (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.setDeathSound));
    }

    @NotNull
    @Override
    public LivingEntity.Fallsounds getFallSounds() {
        return this.builder.fallSounds != null ? new LivingEntity.Fallsounds((SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.smallFallSound)), (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.largeFallSound))) : super.m_196493_();
    }

    @NotNull
    @Override
    public SoundEvent getEatingSound(@NotNull ItemStack itemStack) {
        return this.builder.eatingSound != null ? (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue((ResourceLocation) this.builder.eatingSound)) : super.m_7866_(itemStack);
    }

    @Override
    public boolean onClimbable() {
        if (this.builder.onClimbable == null) {
            return super.m_6147_();
        } else {
            Object obj = this.builder.onClimbable.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            } else {
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for onClimbable from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to super.onClimbable(): " + super.m_6147_());
                return super.m_6147_();
            }
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return (Boolean) Objects.requireNonNullElseGet(this.builder.canBreatheUnderwater, () -> super.m_6040_());
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, @NotNull DamageSource damageSource) {
        if (this.builder.onLivingFall != null) {
            ContextUtils.EntityFallDamageContext context = new ContextUtils.EntityFallDamageContext(this, damageMultiplier, distance, damageSource);
            EntityJSHelperClass.consumerCallback(this.builder.onLivingFall, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLivingFall.");
        }
        return super.m_142535_(distance, damageMultiplier, damageSource);
    }

    @Override
    public void setSprinting(boolean sprinting) {
        if (this.builder.onSprint != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onSprint, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onSprint.");
        }
        super.m_6858_(sprinting);
    }

    @Override
    public float getJumpBoostPower() {
        if (this.builder.jumpBoostPower == null) {
            return super.m_285755_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.jumpBoostPower.apply(this), "float");
            if (obj != null) {
                return (Float) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for jumpBoostPower from entity: " + var10000 + ". Value: " + this.builder.jumpBoostPower.apply(this) + ". Must be a float. Defaulting to " + super.m_285755_());
                return super.m_285755_();
            }
        }
    }

    @Override
    public boolean canStandOnFluid(@NotNull FluidState fluidState) {
        if (this.builder.canStandOnFluid != null) {
            ContextUtils.EntityFluidStateContext context = new ContextUtils.EntityFluidStateContext(this, fluidState);
            Object obj = this.builder.canStandOnFluid.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canStandOnFluid from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_203441_(fluidState));
        }
        return super.m_203441_(fluidState);
    }

    @Override
    public boolean isSensitiveToWater() {
        if (this.builder.isSensitiveToWater != null) {
            Object obj = this.builder.isSensitiveToWater.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isSensitiveToWater from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6126_());
        }
        return super.m_6126_();
    }

    @Override
    public void stopRiding() {
        super.m_8127_();
        if (this.builder.onStopRiding != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopRiding, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopRiding.");
        }
    }

    @Override
    public void rideTick() {
        super.rideTick();
        if (this.builder.rideTick != null) {
            EntityJSHelperClass.consumerCallback(this.builder.rideTick, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: rideTick.");
        }
    }

    @Override
    public void onItemPickup(@NotNull ItemEntity itemEntity0) {
        super.m_21053_(itemEntity0);
        if (this.builder.onItemPickup != null) {
            ContextUtils.EntityItemEntityContext context = new ContextUtils.EntityItemEntityContext(this, itemEntity0);
            EntityJSHelperClass.consumerCallback(this.builder.onItemPickup, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onItemPickup.");
        }
    }

    @Override
    public boolean hasLineOfSight(@NotNull Entity entity) {
        if (this.builder.hasLineOfSight != null) {
            ContextUtils.LineOfSightContext context = new ContextUtils.LineOfSightContext(entity, this);
            Object obj = this.builder.hasLineOfSight.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for hasLineOfSight from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142582_(entity));
        }
        return super.m_142582_(entity);
    }

    @Override
    public void onEnterCombat() {
        if (this.builder.onEnterCombat != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onEnterCombat, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onEnterCombat.");
        } else {
            super.m_8108_();
        }
    }

    @Override
    public void onLeaveCombat() {
        if (this.builder.onLeaveCombat != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onLeaveCombat, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onLeaveCombat.");
        }
        super.m_8098_();
    }

    @Override
    public boolean isAffectedByPotions() {
        if (this.builder.isAffectedByPotions != null) {
            Object obj = this.builder.isAffectedByPotions.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAffectedByPotions from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_5801_());
        }
        return super.m_5801_();
    }

    @Override
    public boolean attackable() {
        if (this.builder.isAttackable != null) {
            Object obj = this.builder.isAttackable.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isAttackable from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_5789_());
        }
        return super.m_5789_();
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack itemStack) {
        if (this.builder.canTakeItem != null) {
            ContextUtils.EntityItemLevelContext context = new ContextUtils.EntityItemLevelContext(this, itemStack, this.m_9236_());
            Object obj = this.builder.canTakeItem.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTakeItem from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_7066_(itemStack));
        }
        return super.m_7066_(itemStack);
    }

    @Override
    public boolean isSleeping() {
        if (this.builder.isSleeping != null) {
            Object obj = this.builder.isSleeping.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isSleeping from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_5803_());
        }
        return super.m_5803_();
    }

    @Override
    public void startSleeping(@NotNull BlockPos blockPos) {
        if (this.builder.onStartSleeping != null) {
            ContextUtils.EntityBlockPosContext context = new ContextUtils.EntityBlockPosContext(this, blockPos);
            EntityJSHelperClass.consumerCallback(this.builder.onStartSleeping, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStartSleeping.");
        }
        super.m_5802_(blockPos);
    }

    @Override
    public void stopSleeping() {
        if (this.builder.onStopSleeping != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onStopSleeping, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onStopSleeping.");
        }
        super.m_5796_();
    }

    @NotNull
    @Override
    public ItemStack eat(@NotNull Level level, @NotNull ItemStack itemStack) {
        if (this.builder.eat != null) {
            ContextUtils.EntityItemLevelContext context = new ContextUtils.EntityItemLevelContext(this, itemStack, level);
            EntityJSHelperClass.consumerCallback(this.builder.eat, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: eat.");
            return itemStack;
        } else {
            return super.m_5584_(level, itemStack);
        }
    }

    public boolean shouldRiderFaceForward(@NotNull Player player) {
        if (this.builder.shouldRiderFaceForward != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(player, this);
            Object obj = this.builder.shouldRiderFaceForward.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for shouldRiderFaceForward from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.shouldRiderFaceForward(player));
        }
        return super.shouldRiderFaceForward(player);
    }

    @Override
    public boolean canFreeze() {
        if (this.builder.canFreeze != null) {
            Object obj = this.builder.canFreeze.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canFreeze from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142079_());
        }
        return super.m_142079_();
    }

    @Override
    public boolean isFreezing() {
        if (this.builder.isFreezing != null) {
            Object obj = this.builder.isFreezing.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isFreezing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_203117_());
        }
        return super.m_203117_();
    }

    @Override
    public boolean isCurrentlyGlowing() {
        if (this.builder.isCurrentlyGlowing != null && !this.m_9236_().isClientSide()) {
            Object obj = this.builder.isCurrentlyGlowing.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isCurrentlyGlowing from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142038_());
        }
        return super.m_142038_();
    }

    @Override
    public boolean canDisableShield() {
        if (this.builder.canDisableShield != null) {
            Object obj = this.builder.canDisableShield.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canDisableShield from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_213824_());
        }
        return super.m_213824_();
    }

    @Override
    public void onClientRemoval() {
        if (this.builder.onClientRemoval != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onClientRemoval, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onClientRemoval.");
        }
        super.m_142036_();
    }

    @Override
    public void actuallyHurt(DamageSource pDamageSource, float pDamageAmount) {
        if (this.builder.onHurt != null) {
            ContextUtils.EntityDamageContext context = new ContextUtils.EntityDamageContext(pDamageSource, pDamageAmount, this);
            EntityJSHelperClass.consumerCallback(this.builder.onHurt, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: onHurt.");
        }
        super.m_6475_(pDamageSource, pDamageAmount);
    }

    @Override
    public void lavaHurt() {
        if (this.builder.lavaHurt != null) {
            EntityJSHelperClass.consumerCallback(this.builder.lavaHurt, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: lavaHurt.");
        }
        super.m_20093_();
    }

    @Override
    public int getExperienceReward() {
        if (this.builder.experienceReward != null) {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.experienceReward.apply(this), "integer");
            if (obj != null) {
                return (Integer) obj;
            }
            String var10000 = this.entityName();
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for experienceReward from entity: " + var10000 + ". Value: " + this.builder.experienceReward.apply(this) + ". Must be an integer. Defaulting to " + super.m_213860_());
        }
        return super.m_213860_();
    }

    @Override
    public boolean dampensVibrations() {
        if (this.builder.dampensVibrations != null) {
            Object obj = this.builder.dampensVibrations.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for dampensVibrations from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_213854_());
        }
        return super.m_213854_();
    }

    @Override
    public void playerTouch(Player player0) {
        if (this.builder.playerTouch != null) {
            ContextUtils.PlayerEntityContext context = new ContextUtils.PlayerEntityContext(player0, this);
            EntityJSHelperClass.consumerCallback(this.builder.playerTouch, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: playerTouch.");
        }
    }

    @Override
    public boolean showVehicleHealth() {
        if (this.builder.showVehicleHealth != null) {
            Object obj = this.builder.showVehicleHealth.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for showVehicleHealth from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_20152_());
        }
        return super.m_20152_();
    }

    @Override
    public void thunderHit(ServerLevel serverLevel0, LightningBolt lightningBolt1) {
        if (this.builder.thunderHit != null) {
            super.m_8038_(serverLevel0, lightningBolt1);
            ContextUtils.ThunderHitContext context = new ContextUtils.ThunderHitContext(serverLevel0, lightningBolt1, this);
            EntityJSHelperClass.consumerCallback(this.builder.thunderHit, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: thunderHit.");
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource0) {
        if (this.builder.isInvulnerableTo != null) {
            ContextUtils.DamageContext context = new ContextUtils.DamageContext(this, damageSource0);
            Object obj = this.builder.isInvulnerableTo.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for isInvulnerableTo from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6673_(damageSource0));
        }
        return super.m_6673_(damageSource0);
    }

    @Override
    public boolean canChangeDimensions() {
        if (this.builder.canChangeDimensions != null) {
            Object obj = this.builder.canChangeDimensions.apply(this);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canChangeDimensions from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_6072_());
        }
        return super.m_6072_();
    }

    @Override
    public boolean mayInteract(@NotNull Level level0, @NotNull BlockPos blockPos1) {
        if (this.builder.mayInteract != null) {
            ContextUtils.MayInteractContext context = new ContextUtils.MayInteractContext(level0, blockPos1, this);
            Object obj = this.builder.mayInteract.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for mayInteract from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.m_142265_(level0, blockPos1));
        }
        return super.m_142265_(level0, blockPos1);
    }

    public boolean canTrample(@NotNull BlockState state, @NotNull BlockPos pos, float fallDistance) {
        if (this.builder.canTrample != null) {
            ContextUtils.CanTrampleContext context = new ContextUtils.CanTrampleContext(state, pos, fallDistance, this);
            Object obj = this.builder.canTrample.apply(context);
            if (obj instanceof Boolean) {
                return (Boolean) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for canTrample from entity: " + this.entityName() + ". Value: " + obj + ". Must be a boolean. Defaulting to " + super.canTrample(state, pos, fallDistance));
        }
        return super.canTrample(state, pos, fallDistance);
    }

    public void onRemovedFromWorld() {
        if (this.builder.onRemovedFromWorld != null) {
            EntityJSHelperClass.consumerCallback(this.builder.onRemovedFromWorld, this, "[EntityJS]: Error in " + this.entityName() + "builder for field: onRemovedFromWorld.");
        }
        super.onRemovedFromWorld();
    }

    @Override
    public int getMaxFallDistance() {
        if (this.builder.setMaxFallDistance == null) {
            return super.m_6056_();
        } else {
            Object obj = EntityJSHelperClass.convertObjectToDesired(this.builder.setMaxFallDistance.apply(this), "integer");
            if (obj != null) {
                return (Integer) obj;
            } else {
                String var10000 = this.entityName();
                EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for setMaxFallDistance from entity: " + var10000 + ". Value: " + this.builder.setMaxFallDistance.apply(this) + ". Must be an integer. Defaulting to " + super.m_6056_());
                return super.m_6056_();
            }
        }
    }

    @Override
    public void lerpTo(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
        super.m_6453_(x, y, z, yaw, pitch, posRotationIncrements, teleport);
        if (this.builder.lerpTo != null) {
            ContextUtils.LerpToContext context = new ContextUtils.LerpToContext(x, y, z, yaw, pitch, posRotationIncrements, teleport, this);
            EntityJSHelperClass.consumerCallback(this.builder.lerpTo, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: lerpTo.");
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return this.handItems;
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch(slot.getType()) {
            case HAND ->
                (ItemStack) this.handItems.get(slot.getIndex());
            case ARMOR ->
                (ItemStack) this.armorItems.get(slot.getIndex());
        };
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        this.m_181122_(stack);
        switch(slot.getType()) {
            case HAND:
                this.onEquipItem(slot, this.handItems.set(slot.getIndex(), stack), stack);
                break;
            case ARMOR:
                this.onEquipItem(slot, this.armorItems.set(slot.getIndex(), stack), stack);
        }
    }
}