package yesman.epicfight.world.capabilities.item;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.collider.Collider;
import yesman.epicfight.gameasset.ColliderPreset;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class WeaponCapability extends CapabilityItem {

    protected final Function<LivingEntityPatch<?>, Style> stylegetter;

    protected final Function<LivingEntityPatch<?>, Boolean> weaponCombinationPredicator;

    protected final Skill passiveSkill;

    protected final SoundEvent smashingSound;

    protected final SoundEvent hitSound;

    protected final Collider weaponCollider;

    protected final HitParticleType hitParticle;

    protected final Map<Style, List<StaticAnimation>> autoAttackMotions;

    protected final Map<Style, Function<ItemStack, Skill>> innateSkill;

    protected final Map<Style, Map<LivingMotion, StaticAnimation>> livingMotionModifiers;

    protected final boolean canBePlacedOffhand;

    protected final Function<Style, Boolean> comboCancel;

    protected WeaponCapability(CapabilityItem.Builder builder) {
        super(builder);
        WeaponCapability.Builder weaponBuilder = (WeaponCapability.Builder) builder;
        this.autoAttackMotions = weaponBuilder.autoAttackMotionMap;
        this.innateSkill = weaponBuilder.innateSkillByStyle;
        this.livingMotionModifiers = weaponBuilder.livingMotionModifiers;
        this.stylegetter = weaponBuilder.styleProvider;
        this.weaponCombinationPredicator = weaponBuilder.weaponCombinationPredicator;
        this.passiveSkill = weaponBuilder.passiveSkill;
        this.smashingSound = weaponBuilder.swingSound;
        this.hitParticle = weaponBuilder.hitParticle;
        this.hitSound = weaponBuilder.hitSound;
        this.weaponCollider = weaponBuilder.collider;
        this.canBePlacedOffhand = weaponBuilder.canBePlacedOffhand;
        this.comboCancel = weaponBuilder.comboCancel;
        this.attributeMap.putAll(weaponBuilder.attributeMap);
    }

    @Override
    public final List<StaticAnimation> getAutoAttckMotion(PlayerPatch<?> playerpatch) {
        return (List<StaticAnimation>) this.autoAttackMotions.get(this.getStyle(playerpatch));
    }

    @Override
    public final Skill getInnateSkill(PlayerPatch<?> playerpatch, ItemStack itemstack) {
        return (Skill) ((Function) this.innateSkill.getOrDefault(this.getStyle(playerpatch), (Function) s -> null)).apply(itemstack);
    }

    @Override
    public Skill getPassiveSkill() {
        return this.passiveSkill;
    }

    @Override
    public final List<StaticAnimation> getMountAttackMotion() {
        return (List<StaticAnimation>) this.autoAttackMotions.get(CapabilityItem.Styles.MOUNT);
    }

    @Override
    public Style getStyle(LivingEntityPatch<?> entitypatch) {
        return (Style) this.stylegetter.apply(entitypatch);
    }

    @Override
    public SoundEvent getSmashingSound() {
        return this.smashingSound;
    }

    @Override
    public SoundEvent getHitSound() {
        return this.hitSound;
    }

    @Override
    public HitParticleType getHitParticle() {
        return this.hitParticle;
    }

    @Override
    public Collider getWeaponCollider() {
        return this.weaponCollider != null ? this.weaponCollider : super.getWeaponCollider();
    }

    @Override
    public boolean canBePlacedOffhand() {
        return this.canBePlacedOffhand;
    }

    @Override
    public boolean shouldCancelCombo(LivingEntityPatch<?> entitypatch) {
        return (Boolean) this.comboCancel.apply(this.getStyle(entitypatch));
    }

    @Override
    public Map<LivingMotion, StaticAnimation> getLivingMotionModifier(LivingEntityPatch<?> player, InteractionHand hand) {
        if (this.livingMotionModifiers != null && hand != InteractionHand.OFF_HAND) {
            Map<LivingMotion, StaticAnimation> motions = (Map<LivingMotion, StaticAnimation>) this.livingMotionModifiers.getOrDefault(this.getStyle(player), Maps.newHashMap());
            ((Map) this.livingMotionModifiers.getOrDefault(CapabilityItem.Styles.COMMON, Maps.newHashMap())).forEach(motions::putIfAbsent);
            return motions;
        } else {
            return super.getLivingMotionModifier(player, hand);
        }
    }

    @Override
    public UseAnim getUseAnimation(LivingEntityPatch<?> playerpatch) {
        if (this.livingMotionModifiers != null) {
            Style style = this.getStyle(playerpatch);
            if (this.livingMotionModifiers.containsKey(style) && ((Map) this.livingMotionModifiers.get(style)).containsKey(LivingMotions.BLOCK)) {
                return UseAnim.BLOCK;
            }
        }
        return UseAnim.NONE;
    }

    @Override
    public boolean canHoldInOffhandAlone() {
        return false;
    }

    @Override
    public boolean checkOffhandValid(LivingEntityPatch<?> entitypatch) {
        return super.checkOffhandValid(entitypatch) || (Boolean) this.weaponCombinationPredicator.apply(entitypatch);
    }

    @Override
    public boolean availableOnHorse() {
        return this.getMountAttackMotion() != null;
    }

    public static WeaponCapability.Builder builder() {
        return new WeaponCapability.Builder();
    }

    public static class Builder extends CapabilityItem.Builder {

        Function<LivingEntityPatch<?>, Style> styleProvider;

        Function<LivingEntityPatch<?>, Boolean> weaponCombinationPredicator;

        Skill passiveSkill;

        SoundEvent swingSound;

        SoundEvent hitSound;

        HitParticleType hitParticle;

        Collider collider;

        Map<Style, List<StaticAnimation>> autoAttackMotionMap;

        Map<Style, Function<ItemStack, Skill>> innateSkillByStyle;

        Map<Style, Map<LivingMotion, StaticAnimation>> livingMotionModifiers;

        Function<Style, Boolean> comboCancel;

        boolean canBePlacedOffhand;

        protected Builder() {
            this.constructor = WeaponCapability::new;
            this.styleProvider = entitypatch -> CapabilityItem.Styles.ONE_HAND;
            this.weaponCombinationPredicator = entitypatch -> false;
            this.passiveSkill = null;
            this.swingSound = EpicFightSounds.WHOOSH.get();
            this.hitSound = EpicFightSounds.BLUNT_HIT.get();
            this.hitParticle = EpicFightParticles.HIT_BLADE.get();
            this.collider = ColliderPreset.FIST;
            this.autoAttackMotionMap = Maps.newHashMap();
            this.innateSkillByStyle = Maps.newHashMap();
            this.livingMotionModifiers = null;
            this.canBePlacedOffhand = true;
            this.comboCancel = style -> true;
        }

        public WeaponCapability.Builder category(WeaponCategory category) {
            super.category(category);
            return this;
        }

        public WeaponCapability.Builder styleProvider(Function<LivingEntityPatch<?>, Style> styleProvider) {
            this.styleProvider = styleProvider;
            return this;
        }

        public WeaponCapability.Builder passiveSkill(Skill passiveSkill) {
            this.passiveSkill = passiveSkill;
            return this;
        }

        public WeaponCapability.Builder swingSound(SoundEvent swingSound) {
            this.swingSound = swingSound;
            return this;
        }

        public WeaponCapability.Builder hitSound(SoundEvent hitSound) {
            this.hitSound = hitSound;
            return this;
        }

        public WeaponCapability.Builder hitParticle(HitParticleType hitParticle) {
            this.hitParticle = hitParticle;
            return this;
        }

        public WeaponCapability.Builder collider(Collider collider) {
            this.collider = collider;
            return this;
        }

        public WeaponCapability.Builder canBePlacedOffhand(boolean canBePlacedOffhand) {
            this.canBePlacedOffhand = canBePlacedOffhand;
            return this;
        }

        public WeaponCapability.Builder livingMotionModifier(Style wieldStyle, LivingMotion livingMotion, StaticAnimation animation) {
            if (this.livingMotionModifiers == null) {
                this.livingMotionModifiers = Maps.newHashMap();
            }
            if (!this.livingMotionModifiers.containsKey(wieldStyle)) {
                this.livingMotionModifiers.put(wieldStyle, Maps.newHashMap());
            }
            ((Map) this.livingMotionModifiers.get(wieldStyle)).put(livingMotion, animation);
            return this;
        }

        public WeaponCapability.Builder addStyleAttibutes(Style style, Pair<Attribute, AttributeModifier> attributePair) {
            super.addStyleAttibutes(style, attributePair);
            return this;
        }

        public WeaponCapability.Builder newStyleCombo(Style style, StaticAnimation... animation) {
            this.autoAttackMotionMap.put(style, Lists.newArrayList(animation));
            return this;
        }

        public WeaponCapability.Builder weaponCombinationPredicator(Function<LivingEntityPatch<?>, Boolean> predicator) {
            this.weaponCombinationPredicator = predicator;
            return this;
        }

        public WeaponCapability.Builder innateSkill(Style style, Function<ItemStack, Skill> innateSkill) {
            this.innateSkillByStyle.put(style, innateSkill);
            return this;
        }

        public WeaponCapability.Builder comboCancel(Function<Style, Boolean> comboCancel) {
            this.comboCancel = comboCancel;
            return this;
        }
    }
}