package yesman.epicfight.skill.weaponinnate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;

public abstract class WeaponInnateSkill extends Skill {

    protected List<Map<AnimationProperty.AttackPhaseProperty<?>, Object>> properties = Lists.newArrayList();

    public static Skill.Builder<WeaponInnateSkill> createWeaponInnateBuilder() {
        return new Skill.Builder<WeaponInnateSkill>().setCategory(SkillCategories.WEAPON_INNATE).setResource(Skill.Resource.WEAPON_INNATE_ENERGY);
    }

    public WeaponInnateSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public boolean canExecute(PlayerPatch<?> executer) {
        if (executer.isLogicalClient()) {
            return super.canExecute(executer);
        } else {
            ItemStack itemstack = executer.getOriginal().m_21205_();
            return super.canExecute(executer) && EpicFightCapabilities.getItemStackCapability(itemstack).getInnateSkill(executer, itemstack) == this && executer.getOriginal().m_20202_() == null && (!executer.getSkill(this).isActivated() || this.activateType == Skill.ActivateType.TOGGLE);
        }
    }

    @Override
    public List<Component> getTooltipOnItem(ItemStack itemstack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        List<Component> list = Lists.newArrayList();
        String traslatableText = this.getTranslationKey();
        list.add(Component.translatable(traslatableText).withStyle(ChatFormatting.WHITE).append(Component.literal(String.format("[%.0f]", this.consumption)).withStyle(ChatFormatting.AQUA)));
        list.add(Component.translatable(traslatableText + ".tooltip").withStyle(ChatFormatting.DARK_GRAY));
        return list;
    }

    protected void generateTooltipforPhase(List<Component> list, ItemStack itemstack, CapabilityItem itemcap, PlayerPatch<?> playerpatch, Map<AnimationProperty.AttackPhaseProperty<?>, Object> propertyMap, String title) {
        Multimap<Attribute, AttributeModifier> capAttributes = itemcap.getAttributeModifiers(EquipmentSlot.MAINHAND, playerpatch);
        double damage = playerpatch.getWeaponAttribute(Attributes.ATTACK_DAMAGE, itemstack);
        double armorNegation = playerpatch.getWeaponAttribute(EpicFightAttributes.ARMOR_NEGATION.get(), itemstack);
        double impact = playerpatch.getWeaponAttribute(EpicFightAttributes.IMPACT.get(), itemstack);
        double maxStrikes = playerpatch.getWeaponAttribute(EpicFightAttributes.MAX_STRIKES.get(), itemstack);
        ValueModifier damageModifier = ValueModifier.empty();
        ValueModifier armorNegationModifier = ValueModifier.empty();
        ValueModifier impactModifier = ValueModifier.empty();
        ValueModifier maxStrikesModifier = ValueModifier.empty();
        for (AttributeModifier modifier : capAttributes.get(EpicFightAttributes.ARMOR_NEGATION.get())) {
            armorNegation += modifier.getAmount();
        }
        for (AttributeModifier modifier : capAttributes.get(EpicFightAttributes.IMPACT.get())) {
            impact += modifier.getAmount();
        }
        for (AttributeModifier modifier : capAttributes.get(EpicFightAttributes.MAX_STRIKES.get())) {
            maxStrikes += modifier.getAmount();
        }
        this.getProperty(AnimationProperty.AttackPhaseProperty.DAMAGE_MODIFIER, propertyMap).ifPresent(damageModifier::merge);
        this.getProperty(AnimationProperty.AttackPhaseProperty.ARMOR_NEGATION_MODIFIER, propertyMap).ifPresent(armorNegationModifier::merge);
        this.getProperty(AnimationProperty.AttackPhaseProperty.IMPACT_MODIFIER, propertyMap).ifPresent(impactModifier::merge);
        this.getProperty(AnimationProperty.AttackPhaseProperty.MAX_STRIKES_MODIFIER, propertyMap).ifPresent(maxStrikesModifier::merge);
        impactModifier.merge(ValueModifier.multiplier(1.0F + (float) itemstack.getEnchantmentLevel(Enchantments.KNOCKBACK) * 0.12F));
        Double baseDamage = damage;
        damage = (double) damageModifier.getTotalValue(playerpatch.getModifiedBaseDamage((float) damage));
        armorNegation = (double) armorNegationModifier.getTotalValue((float) armorNegation);
        impact = (double) impactModifier.getTotalValue((float) impact);
        maxStrikes = (double) maxStrikesModifier.getTotalValue((float) maxStrikes);
        list.add(Component.literal(title).withStyle(ChatFormatting.UNDERLINE).withStyle(ChatFormatting.GRAY));
        MutableComponent damageComponent = Component.translatable("damage_source.epicfight.damage", Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(damage)).withStyle(ChatFormatting.RED)).withStyle(ChatFormatting.DARK_GRAY);
        this.getProperty(AnimationProperty.AttackPhaseProperty.EXTRA_DAMAGE, propertyMap).ifPresent(extraDamageSet -> extraDamageSet.forEach(extraDamage -> extraDamage.setTooltips(itemstack, damageComponent, baseDamage)));
        list.add(damageComponent);
        if (armorNegation != 0.0) {
            list.add(Component.translatable(EpicFightAttributes.ARMOR_NEGATION.get().getDescriptionId(), Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(armorNegation) + "%").withStyle(ChatFormatting.GOLD)).withStyle(ChatFormatting.DARK_GRAY));
        }
        if (impact != 0.0) {
            list.add(Component.translatable(EpicFightAttributes.IMPACT.get().getDescriptionId(), Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(impact)).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.DARK_GRAY));
        }
        list.add(Component.translatable(EpicFightAttributes.MAX_STRIKES.get().getDescriptionId(), Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(maxStrikes)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.DARK_GRAY));
        Optional<StunType> stunOption = this.getProperty(AnimationProperty.AttackPhaseProperty.STUN_TYPE, propertyMap);
        stunOption.ifPresent(stunType -> list.add(Component.translatable(stunType.toString()).withStyle(ChatFormatting.DARK_GRAY)));
        if (!stunOption.isPresent()) {
            list.add(Component.translatable(StunType.SHORT.toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    protected <V> Optional<V> getProperty(AnimationProperty.AttackPhaseProperty<V> propertyKey, Map<AnimationProperty.AttackPhaseProperty<?>, Object> map) {
        return Optional.ofNullable(map.get(propertyKey));
    }

    public WeaponInnateSkill newProperty() {
        this.properties.add(Maps.newHashMap());
        return this;
    }

    public <T> WeaponInnateSkill addProperty(AnimationProperty.AttackPhaseProperty<T> propertyKey, T object) {
        ((Map) this.properties.get(this.properties.size() - 1)).put(propertyKey, object);
        return this;
    }
}