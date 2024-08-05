package yesman.epicfight.world.capabilities.entitypatch.player;

import java.util.Collection;
import java.util.UUID;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.ActionAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.skill.CapabilitySkill;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.eventlistener.AttackSpeedModifyEvent;
import yesman.epicfight.world.entity.eventlistener.FallEvent;
import yesman.epicfight.world.entity.eventlistener.ModifyBaseDamageEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public abstract class PlayerPatch<T extends Player> extends LivingEntityPatch<T> {

    private static final UUID ACTION_EVENT_UUID = UUID.fromString("e6beeac4-77d2-11eb-9439-0242ac130002");

    public static final EntityDataAccessor<Float> STAMINA = new EntityDataAccessor<>(253, EntityDataSerializers.FLOAT);

    protected float modelYRot;

    protected PlayerEventListener eventListeners;

    protected int tickSinceLastAction;

    protected PlayerPatch.PlayerMode playerMode = PlayerPatch.PlayerMode.MINING;

    protected double xo;

    protected double yo;

    protected double zo;

    protected int lastChargingTick;

    protected int chargingAmount;

    protected ChargeableSkill chargingSkill;

    public PlayerPatch() {
        this.eventListeners = new PlayerEventListener(this);
    }

    public void onConstructed(T entityIn) {
        super.onConstructed(entityIn);
        entityIn.m_20088_().define(STAMINA, 0.0F);
    }

    public void onJoinWorld(T entityIn, EntityJoinLevelEvent event) {
        super.onJoinWorld(entityIn, event);
        CapabilitySkill skillCapability = this.getSkillCapability();
        skillCapability.skillContainers[SkillSlots.BASIC_ATTACK.universalOrdinal()].setSkill(EpicFightSkills.BASIC_ATTACK);
        skillCapability.skillContainers[SkillSlots.AIR_ATTACK.universalOrdinal()].setSkill(EpicFightSkills.AIR_ATTACK);
        skillCapability.skillContainers[SkillSlots.KNOCKDOWN_WAKEUP.universalOrdinal()].setSkill(EpicFightSkills.KNOCKDOWN_WAKEUP);
        this.tickSinceLastAction = 0;
        this.eventListeners.addEventListener(PlayerEventListener.EventType.ACTION_EVENT_SERVER, ACTION_EVENT_UUID, playerEvent -> this.resetActionTick());
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        this.original.m_21051_(EpicFightAttributes.MAX_STAMINA.get()).setBaseValue(15.0);
        this.original.m_21051_(EpicFightAttributes.STAMINA_REGEN.get()).setBaseValue(1.0);
        this.original.m_21051_(EpicFightAttributes.OFFHAND_IMPACT.get()).setBaseValue(0.5);
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
        clientAnimator.addLivingAnimation(LivingMotions.SNEAK, Animations.BIPED_SNEAK);
        clientAnimator.addLivingAnimation(LivingMotions.SWIM, Animations.BIPED_SWIM);
        clientAnimator.addLivingAnimation(LivingMotions.FLOAT, Animations.BIPED_FLOAT);
        clientAnimator.addLivingAnimation(LivingMotions.KNEEL, Animations.BIPED_KNEEL);
        clientAnimator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        clientAnimator.addLivingAnimation(LivingMotions.MOUNT, Animations.BIPED_MOUNT);
        clientAnimator.addLivingAnimation(LivingMotions.SIT, Animations.BIPED_SIT);
        clientAnimator.addLivingAnimation(LivingMotions.FLY, Animations.BIPED_FLYING);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        clientAnimator.addLivingAnimation(LivingMotions.JUMP, Animations.BIPED_JUMP);
        clientAnimator.addLivingAnimation(LivingMotions.CLIMB, Animations.BIPED_CLIMBING);
        clientAnimator.addLivingAnimation(LivingMotions.SLEEP, Animations.BIPED_SLEEPING);
        clientAnimator.addLivingAnimation(LivingMotions.CREATIVE_FLY, Animations.BIPED_CREATIVE_FLYING);
        clientAnimator.addLivingAnimation(LivingMotions.CREATIVE_IDLE, Animations.BIPED_CREATIVE_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.DIGGING, Animations.BIPED_DIG);
        clientAnimator.addLivingAnimation(LivingMotions.AIM, Animations.BIPED_BOW_AIM);
        clientAnimator.addLivingAnimation(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT);
        clientAnimator.addLivingAnimation(LivingMotions.DRINK, Animations.BIPED_DRINK);
        clientAnimator.addLivingAnimation(LivingMotions.EAT, Animations.BIPED_EAT);
        clientAnimator.addLivingAnimation(LivingMotions.SPECTATE, Animations.BIPED_SPYGLASS_USE);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    public void copySkillsFrom(PlayerPatch<?> old) {
        CapabilitySkill oldSkill = old.getSkillCapability();
        CapabilitySkill newSkill = this.getSkillCapability();
        int i = 0;
        for (SkillContainer container : newSkill.skillContainers) {
            container.setExecuter(this);
            Skill oldone = oldSkill.skillContainers[i].getSkill();
            if (oldone != null && oldone.getCategory().shouldSynchronize()) {
                container.setSkill(oldSkill.skillContainers[i].getSkill());
            }
            i++;
        }
        for (SkillCategory skillCategory : SkillCategory.ENUM_MANAGER.universalValues()) {
            if (oldSkill.hasCategory(skillCategory)) {
                for (Skill learnedSkill : oldSkill.getLearnedSkills(skillCategory)) {
                    newSkill.addLearnedSkill(learnedSkill);
                }
            }
        }
    }

    public void changeModelYRot(float degree) {
        this.modelYRot = degree;
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        OpenMatrix4f mat = super.getModelMatrix(partialTicks);
        return mat.scale(0.9375F, 0.9375F, 0.9375F);
    }

    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if (this.state.canBasicAttack()) {
            this.tickSinceLastAction++;
        }
        float stamina = this.getStamina();
        float maxStamina = this.getMaxStamina();
        float staminaRegen = (float) this.original.m_21133_(EpicFightAttributes.STAMINA_REGEN.get());
        int regenStandbyTime = 900 / (int) (30.0F * staminaRegen);
        if (stamina < maxStamina && this.tickSinceLastAction > regenStandbyTime) {
            float staminaFactor = 1.0F + (float) Math.pow((double) (stamina / (maxStamina - stamina * 0.5F)), 2.0);
            this.setStamina(stamina + maxStamina * 0.01F * staminaFactor * staminaRegen);
        }
        if (maxStamina < stamina) {
            this.setStamina(maxStamina);
        }
        this.xo = this.original.m_20185_();
        this.yo = this.original.m_20186_();
        this.zo = this.original.m_20189_();
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        if (this.original.m_20202_() == null) {
            for (SkillContainer container : this.getSkillCapability().skillContainers) {
                if (container != null) {
                    container.update();
                }
            }
        }
        super.tick(event);
    }

    public SkillContainer getSkill(Skill skill) {
        return skill == null ? null : this.getSkillCapability().getSkillContainer(skill);
    }

    public SkillContainer getSkill(SkillSlot slot) {
        return this.getSkill(slot.universalOrdinal());
    }

    public SkillContainer getSkill(int slotIndex) {
        return this.getSkillCapability().skillContainers[slotIndex];
    }

    public CapabilitySkill getSkillCapability() {
        return this.original.getCapability(EpicFightCapabilities.CAPABILITY_SKILL).orElse(CapabilitySkill.EMPTY);
    }

    public PlayerEventListener getEventListener() {
        return this.eventListeners;
    }

    @Override
    public float getModifiedBaseDamage(float baseDamage) {
        ModifyBaseDamageEvent<PlayerPatch<?>> event = new ModifyBaseDamageEvent<>(this, baseDamage);
        this.getEventListener().triggerEvents(PlayerEventListener.EventType.MODIFY_DAMAGE_EVENT, event);
        return event.getDamage();
    }

    public float getAttackSpeed(InteractionHand hand) {
        float baseSpeed;
        if (hand == InteractionHand.MAIN_HAND) {
            baseSpeed = (float) this.original.m_21133_(Attributes.ATTACK_SPEED);
        } else {
            baseSpeed = (float) (this.isOffhandItemValid() ? this.original.m_21133_(EpicFightAttributes.OFFHAND_ATTACK_SPEED.get()) : this.original.m_21172_(Attributes.ATTACK_SPEED));
        }
        return this.getModifiedAttackSpeed(this.getAdvancedHoldingItemCapability(hand), baseSpeed);
    }

    public float getModifiedAttackSpeed(CapabilityItem itemCapability, float baseSpeed) {
        AttackSpeedModifyEvent event = new AttackSpeedModifyEvent(this, itemCapability, baseSpeed);
        this.eventListeners.triggerEvents(PlayerEventListener.EventType.MODIFY_ATTACK_SPEED_EVENT, event);
        float weight = this.getWeight();
        if (weight > 40.0F) {
            float attenuation = (float) Mth.clamp(this.getOriginal().m_9236_().getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
            return event.getAttackSpeed() + -0.1F * (weight / 40.0F) * Math.max(event.getAttackSpeed() - 0.8F, 0.0F) * 1.5F * attenuation;
        } else {
            return event.getAttackSpeed();
        }
    }

    public double getWeaponAttribute(Attribute attribute, ItemStack itemstack) {
        AttributeInstance attrInstance = new AttributeInstance(attribute, ai -> {
        });
        Collection<AttributeModifier> itemModifiers = itemstack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(attribute);
        double baseValue = this.original.m_21051_(attribute) == null ? attribute.getDefaultValue() : this.original.m_21051_(attribute).getBaseValue();
        attrInstance.setBaseValue(baseValue);
        for (AttributeModifier modifier : this.original.m_21051_(attribute).getModifiers()) {
            if (!itemModifiers.contains(modifier)) {
                attrInstance.addTransientModifier(modifier);
            }
        }
        for (AttributeModifier modifierx : itemModifiers) {
            if (!attrInstance.hasModifier(modifierx)) {
                attrInstance.addTransientModifier(modifierx);
            }
        }
        CapabilityItem itemCapability = EpicFightCapabilities.getItemStackCapabilityOr(itemstack, null);
        if (itemCapability != null) {
            for (AttributeModifier modifierxx : itemCapability.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(attribute)) {
                if (!attrInstance.hasModifier(modifierxx)) {
                    attrInstance.addTransientModifier(modifierxx);
                }
            }
        }
        return attrInstance.getValue();
    }

    @Override
    public AttackResult attack(EpicFightDamageSource damageSource, Entity target, InteractionHand hand) {
        float fallDist = this.original.f_19789_;
        boolean onGround = this.original.f_19861_;
        boolean shouldSwap = hand == InteractionHand.OFF_HAND;
        this.epicFightDamageSource = damageSource;
        this.original.f_20922_ = Integer.MAX_VALUE;
        this.original.f_19789_ = 0.0F;
        this.original.f_19861_ = false;
        this.setOffhandDamage(shouldSwap);
        this.original.attack(target);
        this.recoverMainhandDamage(shouldSwap);
        this.epicFightDamageSource = null;
        this.original.f_19789_ = fallDist;
        this.original.f_19861_ = onGround;
        return super.attack(damageSource, target, hand);
    }

    @Override
    public EpicFightDamageSource getDamageSource(StaticAnimation animation, InteractionHand hand) {
        EpicFightDamageSources damageSources = EpicFightDamageSources.of(this.original.m_9236_());
        EpicFightDamageSource damagesource = damageSources.playerAttack(this.original).setAnimation(animation);
        damagesource.setImpact(this.getImpact(hand));
        damagesource.setArmorNegation(this.getArmorNegation(hand));
        damagesource.setHurtItem(this.getOriginal().m_21120_(hand));
        return damagesource;
    }

    @Override
    public void cancelAnyAction() {
        super.cancelAnyAction();
        this.resetSkillCharging();
    }

    public float getMaxStamina() {
        AttributeInstance maxStamina = this.original.m_21051_(EpicFightAttributes.MAX_STAMINA.get());
        return (float) (maxStamina == null ? 0.0 : maxStamina.getValue());
    }

    public float getStamina() {
        return this.getMaxStamina() == 0.0F ? 0.0F : this.original.m_20088_().get(STAMINA);
    }

    public float getModifiedStaminaConsume(float amount) {
        float attenuation = (float) Mth.clamp(this.original.m_9236_().getGameRules().getInt(EpicFightGamerules.WEIGHT_PENALTY), 0, 100) / 100.0F;
        float weight = this.getWeight();
        return ((weight / 40.0F - 1.0F) * attenuation + 1.0F) * amount;
    }

    public void setStamina(float value) {
        float f1 = Math.max(Math.min(value, this.getMaxStamina()), 0.0F);
        this.original.m_20088_().set(STAMINA, f1);
    }

    public abstract boolean consumeStamina(float var1);

    public void consumeStaminaAlways(float amount) {
        float currentStamina = this.getStamina();
        this.setStamina(currentStamina - amount);
        this.resetActionTick();
    }

    public boolean hasStamina(float amount) {
        return this.getStamina() > amount;
    }

    public void resetActionTick() {
        this.tickSinceLastAction = 0;
    }

    public int getTickSinceLastAction() {
        return this.tickSinceLastAction;
    }

    public void startSkillCharging(ChargeableSkill chargingSkill) {
        chargingSkill.startCharging(this);
        this.lastChargingTick = this.original.f_19797_;
        this.chargingSkill = chargingSkill;
    }

    public void resetSkillCharging() {
        if (this.chargingSkill != null) {
            this.chargingAmount = 0;
            this.chargingSkill.resetCharging(this);
            this.chargingSkill = null;
        }
    }

    public boolean isChargingSkill() {
        return this.chargingSkill != null;
    }

    public boolean isChargingSkill(Skill chargingSkill) {
        return this.chargingSkill == chargingSkill;
    }

    public int getLastChargingTick() {
        return this.lastChargingTick;
    }

    public void setChargingAmount(int amount) {
        if (this.isChargingSkill()) {
            this.chargingAmount = Math.min(amount, this.getChargingSkill().getMaxChargingTicks());
        } else {
            this.chargingAmount = 0;
        }
    }

    public int getChargingAmount() {
        return this.chargingAmount;
    }

    public float getSkillChargingTicks(float partialTicks) {
        return this.isChargingSkill() ? (float) (this.original.f_19797_ - this.getLastChargingTick()) - 1.0F + partialTicks : 0.0F;
    }

    public int getSkillChargingTicks() {
        return this.isChargingSkill() ? Math.min(this.original.f_19797_ - this.getLastChargingTick(), this.chargingSkill.getMaxChargingTicks()) : 0;
    }

    public int getAccumulatedChargeAmount() {
        return this.getChargingSkill() != null ? this.getChargingSkill().getChargingAmount(this) : 0;
    }

    public ChargeableSkill getChargingSkill() {
        return this.chargingSkill;
    }

    public boolean isUnstable() {
        return this.original.m_21255_() || this.currentLivingMotion == LivingMotions.FALL;
    }

    @Override
    public boolean shouldMoveOnCurrentSide(ActionAnimation actionAnimation) {
        return this.isLogicalClient();
    }

    public void openSkillBook(ItemStack itemstack, InteractionHand hand) {
    }

    @Override
    public void onFall(LivingFallEvent event) {
        FallEvent fallEvent = new FallEvent(this, event);
        this.getEventListener().triggerEvents(PlayerEventListener.EventType.FALL_EVENT, fallEvent);
        this.setAirborneState(false);
    }

    public void toggleMode() {
        switch(this.playerMode) {
            case MINING:
                this.toBattleMode(true);
                break;
            case BATTLE:
                this.toMiningMode(true);
        }
    }

    public void toMode(PlayerPatch.PlayerMode playerMode, boolean synchronize) {
        switch(playerMode) {
            case MINING:
                this.toMiningMode(synchronize);
                break;
            case BATTLE:
                this.toBattleMode(synchronize);
        }
    }

    public PlayerPatch.PlayerMode getPlayerMode() {
        return this.playerMode;
    }

    public void toMiningMode(boolean synchronize) {
        this.playerMode = PlayerPatch.PlayerMode.MINING;
    }

    public void toBattleMode(boolean synchronize) {
        this.playerMode = PlayerPatch.PlayerMode.BATTLE;
    }

    public boolean isBattleMode() {
        return this.playerMode == PlayerPatch.PlayerMode.BATTLE;
    }

    @Override
    public double getXOld() {
        return this.xo;
    }

    @Override
    public double getYOld() {
        return this.yo;
    }

    @Override
    public double getZOld() {
        return this.zo;
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        if (this.original.m_20202_() != null) {
            return Animations.BIPED_HIT_ON_MOUNT;
        } else {
            return switch(stunType) {
                case LONG ->
                    Animations.BIPED_HIT_LONG;
                case SHORT, HOLD ->
                    Animations.BIPED_HIT_SHORT;
                case KNOCKDOWN ->
                    Animations.BIPED_KNOCKDOWN;
                case NEUTRALIZE ->
                    Animations.BIPED_COMMON_NEUTRALIZED;
                case FALL ->
                    Animations.BIPED_LANDING;
                case NONE ->
                    null;
                default ->
                    null;
            };
        }
    }

    public static enum PlayerMode {

        MINING, BATTLE
    }
}