package io.github.edwinmindcraft.origins.data.generator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import io.github.apace100.apoli.util.AttributedEntityAttributeModifier;
import io.github.apace100.apoli.util.Comparison;
import io.github.apace100.apoli.util.modifier.ModifierUtil;
import io.github.apace100.origins.Origins;
import io.github.apace100.origins.power.OriginsPowerTypes;
import io.github.apace100.origins.registry.ModDamageSources;
import io.github.apace100.origins.registry.ModEnchantments;
import io.github.edwinmindcraft.apoli.api.configuration.DoubleComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.FieldConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.HolderConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.IntegerComparisonConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.ListConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.NoConfiguration;
import io.github.edwinmindcraft.apoli.api.configuration.PowerReference;
import io.github.edwinmindcraft.apoli.api.configuration.TagConfiguration;
import io.github.edwinmindcraft.apoli.api.generator.PowerGenerator;
import io.github.edwinmindcraft.apoli.api.power.ConditionData;
import io.github.edwinmindcraft.apoli.api.power.PowerData;
import io.github.edwinmindcraft.apoli.api.power.IActivePower.Key;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredBlockCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredDamageCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityAction;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredEntityCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredItemCondition;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredModifier;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import io.github.edwinmindcraft.apoli.api.power.configuration.power.TogglePowerConfiguration.Impl;
import io.github.edwinmindcraft.apoli.api.power.factory.PowerFactory;
import io.github.edwinmindcraft.apoli.api.registry.ApoliBuiltinRegistries;
import io.github.edwinmindcraft.apoli.common.action.configuration.DamageConfiguration;
import io.github.edwinmindcraft.apoli.common.action.configuration.GiveConfiguration;
import io.github.edwinmindcraft.apoli.common.action.configuration.PlaySoundConfiguration;
import io.github.edwinmindcraft.apoli.common.action.entity.DamageAction;
import io.github.edwinmindcraft.apoli.common.action.entity.GiveAction;
import io.github.edwinmindcraft.apoli.common.action.entity.IntegerEntityAction;
import io.github.edwinmindcraft.apoli.common.action.entity.PlaySoundAction;
import io.github.edwinmindcraft.apoli.common.condition.block.HeightCondition;
import io.github.edwinmindcraft.apoli.common.condition.configuration.BlockCollisionConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.configuration.EnchantmentConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.configuration.FluidTagComparisonConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.configuration.ProjectileConfiguration;
import io.github.edwinmindcraft.apoli.common.condition.configuration.EnchantmentConfiguration.Calculation;
import io.github.edwinmindcraft.apoli.common.condition.damage.ProjectileCondition;
import io.github.edwinmindcraft.apoli.common.condition.damage.SimpleDamageCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.BlockCollisionCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.EnchantmentCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.FluidHeightCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.OnBlockCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.PowerActiveCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.SimpleEntityCondition;
import io.github.edwinmindcraft.apoli.common.condition.entity.SubmergedInCondition;
import io.github.edwinmindcraft.apoli.common.condition.item.DelegatedItemCondition;
import io.github.edwinmindcraft.apoli.common.condition.item.IngredientCondition;
import io.github.edwinmindcraft.apoli.common.condition.item.SimpleItemCondition;
import io.github.edwinmindcraft.apoli.common.condition.meta.ConditionStreamConfiguration;
import io.github.edwinmindcraft.apoli.common.power.ActionOnItemUsePower;
import io.github.edwinmindcraft.apoli.common.power.ActionOnWakeUpPower;
import io.github.edwinmindcraft.apoli.common.power.AttributeModifyTransferPower;
import io.github.edwinmindcraft.apoli.common.power.AttributePower;
import io.github.edwinmindcraft.apoli.common.power.BurnPower;
import io.github.edwinmindcraft.apoli.common.power.ClimbingPower;
import io.github.edwinmindcraft.apoli.common.power.DamageOverTimePower;
import io.github.edwinmindcraft.apoli.common.power.EffectImmunityPower;
import io.github.edwinmindcraft.apoli.common.power.EntityGroupPower;
import io.github.edwinmindcraft.apoli.common.power.InvulnerablePower;
import io.github.edwinmindcraft.apoli.common.power.ModelColorPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyDamageDealtPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyDamageTakenPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyFallingPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyPlayerSpawnPower;
import io.github.edwinmindcraft.apoli.common.power.ModifyValueBlockPower;
import io.github.edwinmindcraft.apoli.common.power.MultiplePower;
import io.github.edwinmindcraft.apoli.common.power.NightVisionPower;
import io.github.edwinmindcraft.apoli.common.power.ParticlePower;
import io.github.edwinmindcraft.apoli.common.power.PreventItemActionPower;
import io.github.edwinmindcraft.apoli.common.power.PreventSleepPower;
import io.github.edwinmindcraft.apoli.common.power.StackingStatusEffectPower;
import io.github.edwinmindcraft.apoli.common.power.ToggleNightVisionPower;
import io.github.edwinmindcraft.apoli.common.power.TogglePower;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnItemUseConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnWakeUpConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.AttributeModifyTransferConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.BurnConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ClimbingConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ColorConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.DamageOverTimeConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.EffectImmunityConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyDamageDealtConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyDamageTakenConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyFallingConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyValueBlockConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.MultipleConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ParticleConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.PreventSleepConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.StackingStatusEffectConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ToggleNightVisionConfiguration;
import io.github.edwinmindcraft.apoli.common.power.configuration.ActionOnItemUseConfiguration.TriggerType;
import io.github.edwinmindcraft.apoli.common.power.configuration.ModifyPlayerSpawnConfiguration.SpawnStrategy;
import io.github.edwinmindcraft.apoli.common.registry.ApoliPowers;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliDefaultActions;
import io.github.edwinmindcraft.apoli.common.registry.action.ApoliEntityActions;
import io.github.edwinmindcraft.apoli.common.registry.condition.ApoliBlockConditions;
import io.github.edwinmindcraft.apoli.common.registry.condition.ApoliDamageConditions;
import io.github.edwinmindcraft.apoli.common.registry.condition.ApoliDefaultConditions;
import io.github.edwinmindcraft.apoli.common.registry.condition.ApoliEntityConditions;
import io.github.edwinmindcraft.apoli.common.registry.condition.ApoliItemConditions;
import io.github.edwinmindcraft.origins.common.power.configuration.NoSlowdownConfiguration;
import io.github.edwinmindcraft.origins.common.power.configuration.WaterVisionConfiguration;
import io.github.edwinmindcraft.origins.data.tag.OriginsBlockTags;
import io.github.edwinmindcraft.origins.data.tag.OriginsItemTags;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OriginsPowerProvider extends PowerGenerator {

    public OriginsPowerProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, "origins", existingFileHelper);
    }

    private static Map<String, Holder<ConfiguredPower<?, ?>>> makeAquaAffinity() {
        Builder<String, Holder<ConfiguredPower<?, ?>>> builder = ImmutableMap.builder();
        Holder<ConfiguredBlockCondition<?, ?>> allow = (Holder<ConfiguredBlockCondition<?, ?>>) ApoliDefaultConditions.BLOCK_DEFAULT.getHolder().orElseThrow(RuntimeException::new);
        builder.put("underwater", Holder.direct(((ModifyValueBlockPower) ApoliPowers.MODIFY_BREAK_SPEED.get()).configure(new ModifyValueBlockConfiguration(ListConfiguration.of(new ConfiguredModifier[] { ModifierUtil.fromAttributeModifier(new AttributeModifier(UUID.randomUUID(), "Unnamed attribute modifier", 4.0, AttributeModifier.Operation.MULTIPLY_TOTAL)) }), allow), PowerData.builder().addCondition(ApoliEntityConditions.and(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((SubmergedInCondition) ApoliEntityConditions.SUBMERGED_IN.get()).configure(new TagConfiguration(FluidTags.WATER)), (ConfiguredEntityCondition) ((EnchantmentCondition) ApoliEntityConditions.ENCHANTMENT.get()).configure(new EnchantmentConfiguration(new IntegerComparisonConfiguration(Comparison.EQUAL, 0), Optional.of(Enchantments.AQUA_AFFINITY), Calculation.SUM)) })).build())));
        builder.put("ungrounded", Holder.direct(((ModifyValueBlockPower) ApoliPowers.MODIFY_BREAK_SPEED.get()).configure(new ModifyValueBlockConfiguration(ListConfiguration.of(new ConfiguredModifier[] { ModifierUtil.fromAttributeModifier(new AttributeModifier(UUID.randomUUID(), "Unnamed attribute modifier", 4.0, AttributeModifier.Operation.MULTIPLY_TOTAL)) }), allow), PowerData.builder().addCondition(ApoliEntityConditions.and(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((FluidHeightCondition) ApoliEntityConditions.FLUID_HEIGHT.get()).configure(new FluidTagComparisonConfiguration(new DoubleComparisonConfiguration(Comparison.GREATER_THAN, 0.0), FluidTags.WATER)), ((OnBlockCondition) ApoliEntityConditions.ON_BLOCK.get()).configure(HolderConfiguration.defaultCondition(ApoliBuiltinRegistries.CONFIGURED_BLOCK_CONDITIONS), new ConditionData(true)) })).build())));
        return builder.build();
    }

    private void makeArachnidPowers() {
        PowerData hidden = PowerData.builder().hidden().build();
        ConfiguredItemCondition<?, ?> carnivore = ApoliItemConditions.and(new ConfiguredItemCondition[] { ((DelegatedItemCondition) ApoliItemConditions.OR.get()).configure(ConditionStreamConfiguration.or(ImmutableList.of(HolderSet.direct(Holder::m_205709_, ImmutableList.of((ConfiguredItemCondition) ((IngredientCondition) ApoliItemConditions.INGREDIENT.get()).configure(FieldConfiguration.of(Ingredient.of(OriginsItemTags.MEAT))), (ConfiguredItemCondition) ((SimpleItemCondition) ApoliItemConditions.MEAT.get()).configure(NoConfiguration.INSTANCE)))), ApoliItemConditions.PREDICATE), new ConditionData(true)), (ConfiguredItemCondition) ((SimpleItemCondition) ApoliItemConditions.FOOD.get()).configure(NoConfiguration.INSTANCE), ((IngredientCondition) ApoliItemConditions.INGREDIENT.get()).configure(FieldConfiguration.of(Ingredient.of(OriginsItemTags.IGNORE_DIET)), new ConditionData(true)) });
        this.add("arthropod", ((EntityGroupPower) ApoliPowers.ENTITY_GROUP.get()).configure(FieldConfiguration.of(MobType.ARTHROPOD), hidden));
        this.add("carnivore", ((PreventItemActionPower) ApoliPowers.PREVENT_ITEM_USAGE.get()).configure(FieldConfiguration.of(Optional.of(carnivore)), PowerData.DEFAULT));
        this.add("climbing", ApoliPowers.multiple(ImmutableMap.of("toggle", ((TogglePower) ApoliPowers.TOGGLE.get()).configure(new Impl(true, Key.PRIMARY, false), PowerData.DEFAULT), "climbing", ((ClimbingPower) ApoliPowers.CLIMBING.get()).configure(new ClimbingConfiguration(true, Holder.direct(ApoliEntityConditions.or(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((BlockCollisionCondition) ApoliEntityConditions.BLOCK_COLLISION.get()).configure(new BlockCollisionConfiguration(new Vec3(-0.01, 0.0, -0.01), (Holder) ApoliDefaultConditions.BLOCK_DEFAULT.getHolder().orElseThrow())), (ConfiguredEntityCondition) ((BlockCollisionCondition) ApoliEntityConditions.BLOCK_COLLISION.get()).configure(new BlockCollisionConfiguration(new Vec3(0.01, 0.0, 0.01), (Holder) ApoliDefaultConditions.BLOCK_DEFAULT.getHolder().orElseThrow())) }))), PowerData.builder().addCondition(ApoliEntityConditions.and(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((PowerActiveCondition) ApoliEntityConditions.POWER_ACTIVE.get()).configure(new PowerReference(Origins.identifier("climbing_toggle"))), (ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.COLLIDED_HORIZONTALLY.get()).configure(NoConfiguration.INSTANCE) })).build()))));
        this.add("fragile", ((AttributePower) ApoliPowers.ATTRIBUTE.get()).configure(new AttributeConfiguration(new AttributedEntityAttributeModifier[] { new AttributedEntityAttributeModifier(Attributes.MAX_HEALTH, new AttributeModifier("Fragile health reduction", -6.0, AttributeModifier.Operation.ADDITION)) }), PowerData.DEFAULT));
    }

    private void makeAvianPowers() {
        ConfiguredItemCondition<?, ?> vegetarian = ApoliItemConditions.and(new ConfiguredItemCondition[] { (ConfiguredItemCondition) ((DelegatedItemCondition) ApoliItemConditions.OR.get()).configure(ConditionStreamConfiguration.or(ImmutableList.of(HolderSet.direct(Holder::m_205709_, ImmutableList.of((ConfiguredItemCondition) ((IngredientCondition) ApoliItemConditions.INGREDIENT.get()).configure(FieldConfiguration.of(Ingredient.of(OriginsItemTags.MEAT))), (ConfiguredItemCondition) ((SimpleItemCondition) ApoliItemConditions.MEAT.get()).configure(NoConfiguration.INSTANCE)))), ApoliItemConditions.PREDICATE)), (ConfiguredItemCondition) ((SimpleItemCondition) ApoliItemConditions.FOOD.get()).configure(NoConfiguration.INSTANCE), ((IngredientCondition) ApoliItemConditions.INGREDIENT.get()).configure(FieldConfiguration.of(Ingredient.of(OriginsItemTags.IGNORE_DIET)), new ConditionData(true)) });
        this.add("vegetarian", ((PreventItemActionPower) ApoliPowers.PREVENT_ITEM_USAGE.get()).configure(FieldConfiguration.of(Optional.of(vegetarian)), PowerData.DEFAULT));
        this.add("tailwind", ((AttributePower) ApoliPowers.ATTRIBUTE.get()).configure(new AttributeConfiguration(new AttributedEntityAttributeModifier[] { new AttributedEntityAttributeModifier(Attributes.MOVEMENT_SPEED, new AttributeModifier("Tailwind speed bonus", 0.2, AttributeModifier.Operation.MULTIPLY_BASE)) }), PowerData.DEFAULT));
        this.add("lay_eggs", ((ActionOnWakeUpPower) ApoliPowers.ACTION_ON_WAKE_UP.get()).configure(new ActionOnWakeUpConfiguration(null, ApoliEntityActions.and(new ConfiguredEntityAction[] { ((GiveAction) ApoliEntityActions.GIVE.get()).configure(new GiveConfiguration(new ItemStack(Items.EGG, 1))), ((PlaySoundAction) ApoliEntityActions.PLAY_SOUND.get()).configure(new PlaySoundConfiguration(SoundEvents.CHICKEN_EGG, 1.0F, 1.0F)) }), null), PowerData.DEFAULT));
        this.add("slow_falling", ((ModifyFallingPower) ApoliPowers.MODIFY_FALLING.get()).configure(new ModifyFallingConfiguration(Optional.of(0.01), false, ListConfiguration.of(new ConfiguredModifier[0])), PowerData.builder().addCondition(ApoliEntityConditions.or(new ConfiguredEntityCondition[] { ApoliEntityConditions.and(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.SNEAKING.get()).configure(NoConfiguration.INSTANCE), (ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.FALL_FLYING.get()).configure(NoConfiguration.INSTANCE) }), ApoliEntityConditions.and(new ConfiguredEntityCondition[] { ((SimpleEntityCondition) ApoliEntityConditions.SNEAKING.get()).configure(NoConfiguration.INSTANCE, new ConditionData(true)), ((SimpleEntityCondition) ApoliEntityConditions.FALL_FLYING.get()).configure(NoConfiguration.INSTANCE, new ConditionData(true)) }) })).build()));
        this.add("like_air", ((AttributeModifyTransferPower) ApoliPowers.ATTRIBUTE_MODIFY_TRANSFER.get()).configure(new AttributeModifyTransferConfiguration((PowerFactory) ApoliPowers.MODIFY_AIR_SPEED.get(), Attributes.MOVEMENT_SPEED, 1.0), PowerData.DEFAULT));
        this.add("fresh_air", ((PreventSleepPower) ApoliPowers.PREVENT_SLEEP.get()).configure(new PreventSleepConfiguration(Holder.direct((ConfiguredBlockCondition) ((HeightCondition) ApoliBlockConditions.HEIGHT.get()).configure(new IntegerComparisonConfiguration(Comparison.LESS_THAN, 86))), "origins.avian_sleep_fail", false), PowerData.DEFAULT));
    }

    private void makeBlazebornPowers() {
        PowerData hidden = PowerData.builder().hidden().build();
        this.add("burning_wrath", ((ModifyDamageDealtPower) ApoliPowers.MODIFY_DAMAGE_DEALT.get()).configure(new ModifyDamageDealtConfiguration(new ConfiguredModifier[] { ModifierUtil.fromAttributeModifier(new AttributeModifier("Additional damage while on fire", 3.0, AttributeModifier.Operation.ADDITION)) }), PowerData.builder().addCondition((ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.ON_FIRE.get()).configure(NoConfiguration.INSTANCE)).build()));
        this.add("damage_from_potions", ((ActionOnItemUsePower) ApoliPowers.ACTION_ON_ITEM_USE.get()).configure(new ActionOnItemUseConfiguration(Holder.direct((ConfiguredItemCondition) ((IngredientCondition) ApoliItemConditions.INGREDIENT.get()).configure(new FieldConfiguration(Ingredient.of(Items.POTION)))), Holder.direct(((DamageAction) ApoliEntityActions.DAMAGE.get()).configure(new DamageConfiguration(Optional.of(ModDamageSources.NO_WATER_FOR_GILLS), Optional.empty(), 2.0F))), (Holder) ApoliDefaultActions.ITEM_DEFAULT.getHolder().orElseThrow(), TriggerType.FINISH, 0), hidden));
        this.add("damage_from_snowballs", ((ModifyDamageTakenPower) ApoliPowers.MODIFY_DAMAGE_TAKEN.get()).configure(new ModifyDamageTakenConfiguration(ListConfiguration.of(new ConfiguredModifier[] { ModifierUtil.fromAttributeModifier(new AttributeModifier("Snowball damage taken like Blazes", 3.0, AttributeModifier.Operation.ADDITION)) }), Holder.direct((ConfiguredDamageCondition) ((ProjectileCondition) ApoliDamageConditions.PROJECTILE.get()).configure(new ProjectileConfiguration(Optional.of(EntityType.SNOWBALL), (Holder) ApoliDefaultConditions.ENTITY_DEFAULT.getHolder().orElseThrow()))), (Holder) ApoliDefaultConditions.BIENTITY_DEFAULT.getHolder().orElseThrow(), (Holder) ApoliDefaultActions.ENTITY_DEFAULT.getHolder().orElseThrow(), (Holder) ApoliDefaultActions.ENTITY_DEFAULT.getHolder().orElseThrow(), (Holder) ApoliDefaultActions.BIENTITY_DEFAULT.getHolder().orElseThrow(), (Holder) ApoliDefaultConditions.ENTITY_DEFAULT.getHolder().orElseThrow(), (Holder) ApoliDefaultConditions.ENTITY_DEFAULT.getHolder().orElseThrow()), hidden));
        this.add("fire_immunity", ((InvulnerablePower) ApoliPowers.INVULNERABILITY.get()).configure(HolderConfiguration.of(Holder.direct((ConfiguredDamageCondition) ((SimpleDamageCondition) ApoliDamageConditions.FIRE.get()).configure(NoConfiguration.INSTANCE))), PowerData.DEFAULT));
        this.add("flame_particles", ((ParticlePower) ApoliPowers.PARTICLE.get()).configure(new ParticleConfiguration(ParticleTypes.FLAME, 4, false, new Vec3(0.25, 0.5, 0.25), 1.0F, 1, false, 0.0F), hidden));
        this.add("hotblooded", ((EffectImmunityPower) ApoliPowers.EFFECT_IMMUNITY.get()).configure(new EffectImmunityConfiguration(ListConfiguration.of(new MobEffect[] { MobEffects.POISON, MobEffects.HUNGER }), false), PowerData.DEFAULT));
        this.add("nether_spawn", ((ModifyPlayerSpawnPower) ApoliPowers.MODIFY_PLAYER_SPAWN.get()).configure(new ModifyPlayerSpawnConfiguration(Level.NETHER, 0.125F, null, SpawnStrategy.CENTER, null, null), PowerData.DEFAULT));
        this.add("water_vulnerability", ((DamageOverTimePower) ApoliPowers.DAMAGE_OVER_TIME.get()).configure(new DamageOverTimeConfiguration(20, 1, 1.0F, 2.0F, ModDamageSources.NO_WATER_FOR_GILLS, null, ModEnchantments.WATER_PROTECTION.get(), 1.0F), PowerData.builder().addCondition(ApoliEntityConditions.or(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((FluidHeightCondition) ApoliEntityConditions.FLUID_HEIGHT.get()).configure(new FluidTagComparisonConfiguration(new DoubleComparisonConfiguration(Comparison.GREATER_THAN, 0.0), FluidTags.WATER)), (ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.IN_RAIN.get()).configure(NoConfiguration.INSTANCE) })).build()));
    }

    protected void populate() {
        this.makeArachnidPowers();
        this.makeAvianPowers();
        this.makeBlazebornPowers();
        PowerData hidden = PowerData.builder().hidden().build();
        ConditionData inverted = new ConditionData(true);
        this.add("like_water", OriginsPowerTypes.LIKE_WATER.get().configure(NoConfiguration.INSTANCE, PowerData.DEFAULT));
        this.add("water_breathing", OriginsPowerTypes.WATER_BREATHING.get().configure(NoConfiguration.INSTANCE, PowerData.DEFAULT));
        this.add("scare_creepers", OriginsPowerTypes.SCARE_CREEPERS.get().configure(NoConfiguration.INSTANCE, PowerData.DEFAULT));
        this.add("water_vision", ((MultiplePower) ApoliPowers.MULTIPLE.get()).configure(new MultipleConfiguration(ImmutableMap.of("vision", Holder.direct(OriginsPowerTypes.WATER_VISION.get().configure(new WaterVisionConfiguration(1.0F), PowerData.builder().addCondition((ConfiguredEntityCondition) ((PowerActiveCondition) ApoliEntityConditions.POWER_ACTIVE.get()).configure(new PowerReference(Origins.identifier("water_vision_toggle")))).build())), "toggle", Holder.direct(((ToggleNightVisionPower) ApoliPowers.TOGGLE_NIGHT_VISION.get()).configure(new ToggleNightVisionConfiguration(true, Key.PRIMARY, 1.0F), PowerData.builder().addCondition((ConfiguredEntityCondition) ((SubmergedInCondition) ApoliEntityConditions.SUBMERGED_IN.get()).configure(new TagConfiguration(FluidTags.WATER))).build())))), PowerData.DEFAULT));
        this.add("no_cobweb_slowdown", OriginsPowerTypes.NO_SLOWDOWN.get().configure(new NoSlowdownConfiguration(OriginsBlockTags.COBWEBS), hidden));
        this.add("conduit_power_on_land", OriginsPowerTypes.CONDUIT_POWER_ON_LAND.get().configure(NoConfiguration.INSTANCE, hidden));
        this.add("aerial_combatant", ((ModifyDamageDealtPower) ApoliPowers.MODIFY_DAMAGE_DEALT.get()).configure(new ModifyDamageDealtConfiguration(new ConfiguredModifier[] { ModifierUtil.fromAttributeModifier(new AttributeModifier("Extra damage while fall flying", 1.0, AttributeModifier.Operation.MULTIPLY_BASE)) }), PowerData.builder().addCondition((ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.FALL_FLYING.get()).configure(NoConfiguration.INSTANCE)).build()));
        this.add("air_from_potions", ((ActionOnItemUsePower) ApoliPowers.ACTION_ON_ITEM_USE.get()).configure(new ActionOnItemUseConfiguration(Holder.direct((ConfiguredItemCondition) ((IngredientCondition) ApoliItemConditions.INGREDIENT.get()).configure(FieldConfiguration.of(Ingredient.of(Items.POTION)))), Holder.direct(((IntegerEntityAction) ApoliEntityActions.GAIN_AIR.get()).configure(FieldConfiguration.of(60))), (Holder) ApoliDefaultActions.ITEM_DEFAULT.getHolder().orElseThrow(RuntimeException::new), TriggerType.FINISH, 0), hidden));
        this.add("aqua_affinity", ((MultiplePower) ApoliPowers.MULTIPLE.get()).configure(new MultipleConfiguration(makeAquaAffinity()), PowerData.DEFAULT));
        this.add("aquatic", ((EntityGroupPower) ApoliPowers.ENTITY_GROUP.get()).configure(FieldConfiguration.of(MobType.WATER), hidden));
        this.add("arcane_skin", ((ModelColorPower) ApoliPowers.MODEL_COLOR.get()).configure(new ColorConfiguration(0.5F, 0.5F, 1.0F, 0.7F), PowerData.DEFAULT));
        this.add("burn_in_daylight", ((BurnPower) ApoliPowers.BURN.get()).configure(new BurnConfiguration(20, 6), PowerData.builder().addCondition(ApoliEntityConditions.and(new ConfiguredEntityCondition[] { (ConfiguredEntityCondition) ((SimpleEntityCondition) ApoliEntityConditions.EXPOSED_TO_SUN.get()).configure(NoConfiguration.INSTANCE), ((SimpleEntityCondition) ApoliEntityConditions.INVISIBLE.get()).configure(NoConfiguration.INSTANCE, inverted) })).build()));
        this.add("cat_vision", ((NightVisionPower) ApoliPowers.NIGHT_VISION.get()).configure(FieldConfiguration.of(0.4F), PowerData.builder().addCondition(((SubmergedInCondition) ApoliEntityConditions.SUBMERGED_IN.get()).configure(new TagConfiguration(FluidTags.WATER), inverted)).build()));
        this.add("claustrophobia", ((StackingStatusEffectPower) ApoliPowers.STACKING_STATUS_EFFECT.get()).configure(new StackingStatusEffectConfiguration(ListConfiguration.of(new MobEffectInstance[] { new MobEffectInstance(MobEffects.WEAKNESS, 100, 0, true, false, true), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0, true, false, true) }), -20, 361, 10), PowerData.builder().addCondition((ConfiguredEntityCondition) ((BlockCollisionCondition) ApoliEntityConditions.BLOCK_COLLISION.get()).configure(new BlockCollisionConfiguration(new Vec3(0.0, 1.0, 0.0), (Holder) ApoliDefaultConditions.BLOCK_DEFAULT.getHolder().orElseThrow()))).build()));
    }
}