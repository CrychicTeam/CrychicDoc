package yesman.epicfight.world.capabilities.entitypatch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeLivingMotion;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public abstract class HumanoidMobPatch<T extends PathfinderMob> extends MobPatch<T> {

    protected Map<WeaponCategory, Map<Style, Set<Pair<LivingMotion, StaticAnimation>>>> weaponLivingMotions;

    protected Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> weaponAttackMotions;

    public HumanoidMobPatch(Faction faction) {
        super(faction);
        this.setWeaponMotions();
    }

    public HumanoidArmature getArmature() {
        return (HumanoidArmature) this.armature;
    }

    @Override
    protected void initAI() {
        super.initAI();
        if (this.original.m_20202_() != null && this.original.m_20202_() instanceof Mob) {
            this.setAIAsMounted(this.original.m_20202_());
        } else {
            this.setAIAsInfantry(this.original.m_21205_().getItem() instanceof ProjectileWeaponItem);
        }
    }

    @Override
    public void onStartTracking(ServerPlayer trackingPlayer) {
        this.modifyLivingMotionByCurrentItem();
    }

    protected void setWeaponMotions() {
        this.weaponLivingMotions = Maps.newHashMap();
        this.weaponLivingMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, Set.of(Pair.of(LivingMotions.WALK, Animations.BIPED_WALK_TWOHAND), Pair.of(LivingMotions.CHASE, Animations.BIPED_WALK_TWOHAND))));
        this.weaponAttackMotions = Maps.newHashMap();
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.AXE, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.HOE, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.PICKAXE, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SHOVEL, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_ONEHAND_TOOLS));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SWORD, ImmutableMap.of(CapabilityItem.Styles.ONE_HAND, MobCombatBehaviors.HUMANOID_ONEHAND_TOOLS, CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_DUAL_SWORD));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.GREATSWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_GREATSWORD));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.UCHIGATANA, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_KATANA));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.LONGSWORD, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_LONGSWORD));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.TACHI, ImmutableMap.of(CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_TACHI));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.SPEAR, ImmutableMap.of(CapabilityItem.Styles.ONE_HAND, MobCombatBehaviors.HUMANOID_SPEAR_ONEHAND, CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_SPEAR_TWOHAND));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.FIST, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_FIST));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.DAGGER, ImmutableMap.of(CapabilityItem.Styles.ONE_HAND, MobCombatBehaviors.HUMANOID_ONEHAND_DAGGER, CapabilityItem.Styles.TWO_HAND, MobCombatBehaviors.HUMANOID_TWOHAND_DAGGER));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.RANGED, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_FIST));
        this.weaponAttackMotions.put(CapabilityItem.WeaponCategories.TRIDENT, ImmutableMap.of(CapabilityItem.Styles.COMMON, MobCombatBehaviors.HUMANOID_SPEAR_ONEHAND));
    }

    protected CombatBehaviors.Builder<HumanoidMobPatch<?>> getHoldingItemWeaponMotionBuilder() {
        CapabilityItem itemCap = this.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        if (this.weaponAttackMotions.containsKey(itemCap.getWeaponCategory())) {
            Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>> motionByStyle = (Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>) this.weaponAttackMotions.get(itemCap.getWeaponCategory());
            Style style = itemCap.getStyle(this);
            if (motionByStyle.containsKey(style) || motionByStyle.containsKey(CapabilityItem.Styles.COMMON)) {
                return (CombatBehaviors.Builder<HumanoidMobPatch<?>>) motionByStyle.getOrDefault(style, (CombatBehaviors.Builder) motionByStyle.get(CapabilityItem.Styles.COMMON));
            }
        }
        return null;
    }

    public void setAIAsInfantry(boolean holdingRanedWeapon) {
        CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = this.getHoldingItemWeaponMotionBuilder();
        if (builder != null) {
            this.original.f_21345_.addGoal(0, new AnimatedAttackGoal<>(this, (CombatBehaviors<HumanoidMobPatch<T>>) builder.build(this)));
            this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), 1.0, true));
        }
    }

    public void setAIAsMounted(Entity ridingEntity) {
        if (this.isArmed() && ridingEntity instanceof AbstractHorse) {
            this.original.f_21345_.addGoal(0, new AnimatedAttackGoal<>(this, (CombatBehaviors<HumanoidMobPatch<T>>) MobCombatBehaviors.MOUNT_HUMANOID_BEHAVIORS.build(this)));
            this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), 1.0, true));
        }
    }

    protected final void commonMobAnimatorInit(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        clientAnimator.addLivingAnimation(LivingMotions.MOUNT, Animations.BIPED_MOUNT);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    protected final void commonAggresiveMobAnimatorInit(ClientAnimator clientAnimator) {
        clientAnimator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        clientAnimator.addLivingAnimation(LivingMotions.WALK, Animations.BIPED_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.CHASE, Animations.BIPED_WALK);
        clientAnimator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        clientAnimator.addLivingAnimation(LivingMotions.MOUNT, Animations.BIPED_MOUNT);
        clientAnimator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateHeldItem(CapabilityItem fromCap, CapabilityItem toCap, ItemStack from, ItemStack to, InteractionHand hand) {
        this.initAI();
        if (hand == InteractionHand.OFF_HAND) {
            if (!from.isEmpty()) {
                from.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_SPEED).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_ATTACK_SPEED.get())::m_22130_);
            }
            if (!fromCap.isEmpty()) {
                fromCap.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(EpicFightAttributes.ARMOR_NEGATION.get()).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get())::m_22130_);
                fromCap.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(EpicFightAttributes.IMPACT.get()).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_IMPACT.get())::m_22130_);
                fromCap.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(EpicFightAttributes.MAX_STRIKES.get()).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_MAX_STRIKES.get())::m_22130_);
            }
            if (!to.isEmpty()) {
                to.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_SPEED).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_ATTACK_SPEED.get())::m_22118_);
            }
            if (!toCap.isEmpty()) {
                toCap.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(EpicFightAttributes.ARMOR_NEGATION.get()).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_ARMOR_NEGATION.get())::m_22118_);
                toCap.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(EpicFightAttributes.IMPACT.get()).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_IMPACT.get())::m_22118_);
                toCap.getAttributeModifiers(EquipmentSlot.MAINHAND, this).get(EpicFightAttributes.MAX_STRIKES.get()).forEach(this.original.m_21051_(EpicFightAttributes.OFFHAND_MAX_STRIKES.get())::m_22118_);
            }
        }
        this.modifyLivingMotionByCurrentItem();
        super.updateHeldItem(fromCap, toCap, from, to, hand);
    }

    public void modifyLivingMotionByCurrentItem() {
        this.<Animator>getAnimator().resetLivingAnimations();
        CapabilityItem mainhandCap = this.getHoldingItemCapability(InteractionHand.MAIN_HAND);
        CapabilityItem offhandCap = this.getAdvancedHoldingItemCapability(InteractionHand.OFF_HAND);
        Map<LivingMotion, StaticAnimation> motionModifier = Maps.newHashMap();
        offhandCap.getLivingMotionModifier(this, InteractionHand.OFF_HAND).forEach(motionModifier::put);
        mainhandCap.getLivingMotionModifier(this, InteractionHand.MAIN_HAND).forEach(motionModifier::put);
        for (Entry<LivingMotion, StaticAnimation> entry : motionModifier.entrySet()) {
            this.<Animator>getAnimator().addLivingAnimation((LivingMotion) entry.getKey(), (StaticAnimation) entry.getValue());
        }
        if (this.weaponLivingMotions != null && this.weaponLivingMotions.containsKey(mainhandCap.getWeaponCategory())) {
            Map<Style, Set<Pair<LivingMotion, StaticAnimation>>> mapByStyle = (Map<Style, Set<Pair<LivingMotion, StaticAnimation>>>) this.weaponLivingMotions.get(mainhandCap.getWeaponCategory());
            Style style = mainhandCap.getStyle(this);
            if (mapByStyle.containsKey(style) || mapByStyle.containsKey(CapabilityItem.Styles.COMMON)) {
                for (Pair<LivingMotion, StaticAnimation> pair : (Set) mapByStyle.getOrDefault(style, (Set) mapByStyle.get(CapabilityItem.Styles.COMMON))) {
                    this.animator.addLivingAnimation((LivingMotion) pair.getFirst(), (StaticAnimation) pair.getSecond());
                }
            }
        }
        SPChangeLivingMotion msg = new SPChangeLivingMotion(this.original.m_19879_());
        msg.putEntries(this.<Animator>getAnimator().getLivingAnimationEntrySet());
        EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(msg, this.original);
    }

    public boolean isArmed() {
        Item heldItem = this.original.m_21205_().getItem();
        return heldItem instanceof SwordItem || heldItem instanceof DiggerItem || heldItem instanceof TridentItem;
    }

    @Override
    public void onMount(boolean isMountOrDismount, Entity ridingEntity) {
        if (this.original != null) {
            if (!this.original.m_9236_().isClientSide() && !this.original.m_21525_()) {
                Set<Goal> toRemove = Sets.newHashSet();
                this.selectGoalToRemove(toRemove);
                toRemove.forEach(this.original.f_21345_::m_25363_);
                if (isMountOrDismount) {
                    this.setAIAsMounted(ridingEntity);
                } else {
                    this.setAIAsInfantry(this.original.m_21205_().getItem() instanceof ProjectileWeaponItem);
                }
            }
        }
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        if (this.original.m_20202_() != null) {
            return Animations.BIPED_HIT_ON_MOUNT;
        } else {
            switch(stunType) {
                case LONG:
                    return Animations.BIPED_HIT_LONG;
                case SHORT:
                    return Animations.BIPED_HIT_SHORT;
                case HOLD:
                    return Animations.BIPED_HIT_SHORT;
                case KNOCKDOWN:
                    return Animations.BIPED_KNOCKDOWN;
                case NEUTRALIZE:
                    return Animations.BIPED_COMMON_NEUTRALIZED;
                case FALL:
                    return Animations.BIPED_LANDING;
                case NONE:
                    return null;
                default:
                    return null;
            }
        }
    }
}