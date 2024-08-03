package com.nameless.indestructible.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.nameless.indestructible.api.animation.types.AnimationEvent;
import com.nameless.indestructible.gameasset.GuardAnimations;
import com.nameless.indestructible.main.Indestructible;
import com.nameless.indestructible.network.SPDatapackSync;
import com.nameless.indestructible.utils.ExtraPredicate;
import com.nameless.indestructible.world.capability.AdvancedCustomHumanoidMobPatch;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.data.reloader.MobPatchReloadListener;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.MobPatch;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.capabilities.provider.EntityPatchProvider;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

public class AdvancedMobpatchReloader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private static final Map<EntityType<?>, CompoundTag> TAGMAP = Maps.newHashMap();

    private static final Map<EntityType<?>, MobPatchReloadListener.AbstractMobPatchProvider> ADVANCED_MOB_PATCH_PROVIDERS = Maps.newHashMap();

    public AdvancedMobpatchReloader() {
        super(GSON, "advanced_mobpatch");
    }

    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        for (Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation rl = (ResourceLocation) entry.getKey();
            String pathString = rl.getPath();
            ResourceLocation registryName = new ResourceLocation(rl.getNamespace(), pathString);
            if (!ForgeRegistries.ENTITY_TYPES.containsKey(registryName)) {
                Indestructible.LOGGER.warn("[Custom Entity] Entity named " + registryName + " does not exist");
            } else {
                EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(registryName);
                CompoundTag tag = null;
                try {
                    tag = TagParser.parseTag(((JsonElement) entry.getValue()).toString());
                } catch (CommandSyntaxException var12) {
                    var12.printStackTrace();
                }
                ADVANCED_MOB_PATCH_PROVIDERS.put(entityType, deserializeMobPatchProvider(entityType, tag, false));
                EntityPatchProvider.putCustomEntityPatch(entityType, entity -> () -> ((MobPatchReloadListener.AbstractMobPatchProvider) ADVANCED_MOB_PATCH_PROVIDERS.get(entity.getType())).get(entity));
                TAGMAP.put(entityType, filterClientData(tag));
                if (EpicFightMod.isPhysicalClient()) {
                    ClientEngine.getInstance().renderEngine.registerCustomEntityRenderer(entityType, tag.contains("preset") ? tag.getString("preset") : tag.getString("renderer"));
                }
            }
        }
    }

    private static AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider deserializeMobPatchProvider(EntityType<?> entityType, CompoundTag tag, boolean clientSide) {
        AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider provider = new AdvancedMobpatchReloader.AdvancedCustomHumanoidMobPatchProvider();
        provider.attributeValues = deserializeAdvancedAttributes(tag.getCompound("attributes"));
        ResourceLocation modelLocation = new ResourceLocation(tag.getString("model"));
        ResourceLocation armatureLocation = new ResourceLocation(tag.getString("armature"));
        modelLocation = new ResourceLocation(modelLocation.getNamespace(), "animmodels/" + modelLocation.getPath() + ".json");
        armatureLocation = new ResourceLocation(armatureLocation.getNamespace(), "animmodels/" + armatureLocation.getPath() + ".json");
        if (EpicFightMod.isPhysicalClient()) {
            Minecraft mc = Minecraft.getInstance();
            Meshes.getOrCreateAnimatedMesh(mc.getResourceManager(), modelLocation, HumanoidMesh::new);
            Armature armature = Armatures.getOrCreateArmature(mc.getResourceManager(), armatureLocation, HumanoidArmature::new);
            Armatures.registerEntityTypeArmature(entityType, armature);
        } else {
            Armature armature = Armatures.getOrCreateArmature(null, armatureLocation, HumanoidArmature::new);
            Armatures.registerEntityTypeArmature(entityType, armature);
        }
        provider.defaultAnimations = MobPatchReloadListener.deserializeDefaultAnimations(tag.getCompound("default_livingmotions"));
        provider.faction = Faction.valueOf(tag.getString("faction").toUpperCase(Locale.ROOT));
        provider.scale = tag.getCompound("attributes").contains("scale") ? (float) tag.getCompound("attributes").getDouble("scale") : 1.0F;
        provider.maxStamina = tag.getCompound("attributes").contains("max_stamina") ? (float) tag.getCompound("attributes").getDouble("max_stamina") : 15.0F;
        provider.maxStunShield = tag.getCompound("attributes").contains("max_stun_shield") ? (float) tag.getCompound("attributes").getDouble("max_stun_shield") : 0.0F;
        if (!clientSide) {
            provider.stunAnimations = MobPatchReloadListener.deserializeStunAnimations(tag.getCompound("stun_animations"));
            provider.chasingSpeed = tag.getCompound("attributes").getDouble("chasing_speed");
            provider.AHCombatBehaviors = deserializeAdvancedCombatBehaviors(tag.getList("combat_behavior", 10));
            provider.AHWeaponMotions = MobPatchReloadListener.deserializeHumanoidWeaponMotions(tag.getList("humanoid_weapon_motions", 10));
            provider.guardMotions = deserializeGuardMotions(tag.getList("custom_guard_motion", 10));
            provider.regenStaminaStandbyTime = tag.getCompound("attributes").contains("stamina_regan_delay") ? tag.getCompound("attributes").getInt("stamina_regan_delay") : 30;
            provider.regenStaminaMultiply = tag.getCompound("attributes").contains("stamina_regan_multiply") ? (float) tag.getCompound("attributes").getDouble("stamina_regan_multiply") : 1.0F;
            provider.hasStunReduction = !tag.getCompound("attributes").contains("has_stun_reduction") || tag.getCompound("attributes").getBoolean("has_stun_reduction");
            provider.reganShieldStandbyTime = tag.getCompound("attributes").contains("stun_shield_regan_delay") ? tag.getCompound("attributes").getInt("stun_shield_regan_delay") : 30;
            provider.reganShieldMultiply = tag.getCompound("attributes").contains("stun_shield_regan_multiply") ? (float) tag.getCompound("attributes").getDouble("stun_shield_multiply") : 1.0F;
            provider.staminaLoseMultiply = tag.getCompound("attributes").contains("stamina_lose_multiply") ? (float) tag.getCompound("attributes").getDouble("stamina_lose_multiply") : 0.0F;
            provider.guardRadius = tag.getCompound("attributes").contains("guard_radius") ? (float) tag.getCompound("attributes").getDouble("guard_radius") : 3.0F;
            provider.stunEvent = deserializeStunCommandList(tag.getList("stun_command_list", 10));
        }
        return provider;
    }

    public static CompoundTag filterClientData(CompoundTag tag) {
        CompoundTag clientTag = new CompoundTag();
        extractBranch(clientTag, tag);
        return clientTag;
    }

    public static CompoundTag extractBranch(CompoundTag extract, CompoundTag original) {
        extract.put("model", original.get("model"));
        extract.put("armature", original.get("armature"));
        extract.putBoolean("isHumanoid", original.contains("isHumanoid") ? original.getBoolean("isHumanoid") : false);
        extract.put("renderer", original.get("renderer"));
        extract.put("faction", original.get("faction"));
        extract.put("default_livingmotions", original.get("default_livingmotions"));
        extract.put("attributes", original.get("attributes"));
        return extract;
    }

    public static Stream<CompoundTag> getDataStream() {
        return TAGMAP.entrySet().stream().map(entry -> {
            ((CompoundTag) entry.getValue()).putString("id", ForgeRegistries.ENTITY_TYPES.getKey((EntityType<?>) entry.getKey()).toString());
            return (CompoundTag) entry.getValue();
        });
    }

    public static int getTagCount() {
        return TAGMAP.size();
    }

    @OnlyIn(Dist.CLIENT)
    public static void processServerPacket(SPDatapackSync packet) {
        for (CompoundTag tag : packet.getTags()) {
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(tag.getString("id")));
            ADVANCED_MOB_PATCH_PROVIDERS.put(entityType, deserializeMobPatchProvider(entityType, tag, true));
            EntityPatchProvider.putCustomEntityPatch(entityType, entity -> () -> ((MobPatchReloadListener.AbstractMobPatchProvider) ADVANCED_MOB_PATCH_PROVIDERS.get(entity.getType())).get(entity));
            Minecraft mc = Minecraft.getInstance();
            ResourceLocation armatureLocation = new ResourceLocation(tag.getString("armature"));
            armatureLocation = new ResourceLocation(armatureLocation.getNamespace(), "animmodels/" + armatureLocation.getPath() + ".json");
            boolean humanoid = tag.getBoolean("isHumanoid");
            Armature armature = Armatures.getOrCreateArmature(mc.getResourceManager(), armatureLocation, humanoid ? Armature::new : HumanoidArmature::new);
            Armatures.registerEntityTypeArmature(entityType, armature);
            ClientEngine.getInstance().renderEngine.registerCustomEntityRenderer(entityType, tag.contains("preset") ? tag.getString("preset") : tag.getString("renderer"));
        }
    }

    public static Map<Attribute, Double> deserializeAdvancedAttributes(CompoundTag tag) {
        Map<Attribute, Double> attributes = Maps.newHashMap();
        attributes.put(EpicFightAttributes.WEIGHT.get(), tag.contains("weight", 6) ? tag.getDouble("weight") : 40.0);
        attributes.put(EpicFightAttributes.IMPACT.get(), tag.contains("impact", 6) ? tag.getDouble("impact") : 0.5);
        attributes.put(EpicFightAttributes.ARMOR_NEGATION.get(), tag.contains("armor_negation", 6) ? tag.getDouble("armor_negation") : 0.0);
        attributes.put(EpicFightAttributes.MAX_STRIKES.get(), (double) (tag.contains("max_strikes", 3) ? tag.getInt("max_strikes") : 1));
        if (tag.contains("attack_damage", 6)) {
            attributes.put(Attributes.ATTACK_DAMAGE, tag.getDouble("attack_damage"));
        }
        return attributes;
    }

    public static Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> deserializeAdvancedCombatBehaviors(ListTag tag) {
        Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> combatBehaviorsMapBuilder = Maps.newHashMap();
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag combatBehavior = tag.getCompound(i);
            ListTag categories = combatBehavior.getList("weapon_categories", 8);
            Style style = Style.ENUM_MANAGER.get(combatBehavior.getString("style"));
            CombatBehaviors.Builder<HumanoidMobPatch<?>> builder = deserializeAdvancedBehaviorsBuilder(combatBehavior.getList("behavior_series", 10));
            for (int j = 0; j < categories.size(); j++) {
                WeaponCategory category = WeaponCategory.ENUM_MANAGER.get(categories.getString(j));
                combatBehaviorsMapBuilder.computeIfAbsent(category, key -> Maps.newHashMap());
                ((Map) combatBehaviorsMapBuilder.get(category)).put(style, builder);
            }
        }
        return combatBehaviorsMapBuilder;
    }

    public static Map<WeaponCategory, Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>> deserializeGuardMotions(ListTag tag) {
        Map<WeaponCategory, Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>> map = Maps.newHashMap();
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag list = tag.getCompound(i);
            Style style = Style.ENUM_MANAGER.get(list.getString("style"));
            StaticAnimation guard = list.contains("guard") ? EpicFightMod.getInstance().animationManager.findAnimationByPath(list.getString("guard")) : GuardAnimations.MOB_LONGSWORD_GUARD;
            float stamina_cost_multiply = list.contains("stamina_cost_multiply") ? (float) list.getDouble("stamina_cost_multiply") : 1.0F;
            boolean canBlockProjectile = list.contains("can_block_projectile") && list.getBoolean("can_block_projectile");
            Tag weponTypeTag = list.get("weapon_categories");
            if (weponTypeTag instanceof StringTag) {
                WeaponCategory weaponCategory = WeaponCategory.ENUM_MANAGER.get(weponTypeTag.getAsString());
                if (!map.containsKey(weaponCategory)) {
                    map.put(weaponCategory, Maps.newHashMap());
                }
                ((Map) map.get(weaponCategory)).put(style, Pair.of(guard, Pair.of(stamina_cost_multiply, canBlockProjectile)));
            } else if (weponTypeTag instanceof ListTag) {
                ListTag weponTypesTag = (ListTag) weponTypeTag;
                for (int j = 0; j < weponTypesTag.size(); j++) {
                    WeaponCategory weaponCategory = WeaponCategory.ENUM_MANAGER.get(weponTypesTag.getString(j));
                    if (!map.containsKey(weaponCategory)) {
                        map.put(weaponCategory, Maps.newHashMap());
                    }
                    ((Map) map.get(weaponCategory)).put(style, Pair.of(guard, Pair.of(stamina_cost_multiply, canBlockProjectile)));
                }
            }
        }
        return map;
    }

    private static <T extends MobPatch<?>> CombatBehaviors.Builder<T> deserializeAdvancedBehaviorsBuilder(ListTag tag) {
        CombatBehaviors.Builder<T> builder = CombatBehaviors.builder();
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag behaviorSeries = tag.getCompound(i);
            float weight = (float) behaviorSeries.getDouble("weight");
            int cooldown = behaviorSeries.contains("cooldown") ? behaviorSeries.getInt("cooldown") : 0;
            boolean canBeInterrupted = behaviorSeries.contains("canBeInterrupted") && behaviorSeries.getBoolean("canBeInterrupted");
            boolean looping = behaviorSeries.contains("looping") && behaviorSeries.getBoolean("looping");
            ListTag behaviorList = behaviorSeries.getList("behaviors", 10);
            CombatBehaviors.BehaviorSeries.Builder<T> behaviorSeriesBuilder = CombatBehaviors.BehaviorSeries.builder();
            behaviorSeriesBuilder.weight(weight).cooldown(cooldown).canBeInterrupted(canBeInterrupted).looping(looping);
            for (int j = 0; j < behaviorList.size(); j++) {
                CombatBehaviors.Behavior.Builder<T> behaviorBuilder = CombatBehaviors.Behavior.builder();
                CompoundTag behavior = behaviorList.getCompound(j);
                ListTag conditionList = behavior.getList("conditions", 10);
                if (behavior.contains("animation")) {
                    StaticAnimation animation = EpicFightMod.getInstance().animationManager.findAnimationByPath(behavior.getString("animation"));
                    float speed = behavior.contains("play_speed") ? (float) behavior.getDouble("play_speed") : 1.0F;
                    float stamina = behavior.contains("stamina") ? (float) behavior.getDouble("stamina") : 0.0F;
                    float convertTime = behavior.contains("convert_time") ? (float) behavior.getDouble("convert_time") : 0.0F;
                    AdvancedCustomHumanoidMobPatch.CustomAnimationMotion motion = new AdvancedCustomHumanoidMobPatch.CustomAnimationMotion(animation, convertTime, speed, stamina);
                    List<AnimationEvent.TimeStampedEvent> timeCommandList = behavior.contains("command_list") ? deserializeTimeCommandList(behavior.getList("command_list", 10)) : null;
                    List<AnimationEvent.HitEvent> hitCommandList = behavior.contains("hit_command_list") ? deserializeHitCommandList(behavior.getList("hit_command_list", 10)) : null;
                    int phase = behavior.contains("set_phase") ? behavior.getInt("set_phase") : -1;
                    AdvancedCustomHumanoidMobPatch.DamageSourceModifier modifier = behavior.contains("damage_modifier") ? deserializeDamageModifier(behavior.getCompound("damage_modifier")) : null;
                    behaviorBuilder.behavior(customAttackAnimation(motion, modifier, timeCommandList, hitCommandList, phase));
                } else if (behavior.contains("guard")) {
                    int guardTime = behavior.getInt("guard");
                    StaticAnimation counter = behavior.contains("counter") ? EpicFightMod.getInstance().animationManager.findAnimationByPath(behavior.getString("counter")) : GuardAnimations.MOB_COUNTER_ATTACK;
                    float cost = behavior.contains("counter_cost") ? (float) behavior.getDouble("counter_cost") : 3.0F;
                    float chance = behavior.contains("counter_chance") ? (float) behavior.getDouble("counter_chance") : 0.3F;
                    float speed = behavior.contains("counter_speed") ? (float) behavior.getDouble("counter_speed") : 1.0F;
                    boolean cancel = !behavior.contains("cancel_after_counter") || behavior.getBoolean("cancel_after_counter");
                    int phase = behavior.contains("set_phase") ? behavior.getInt("set_phase") : -1;
                    behaviorBuilder.behavior(setGuardMotion(guardTime, counter, cost, chance, speed, cancel, phase));
                } else if (behavior.contains("wander")) {
                    int strafingTime = behavior.getInt("wander");
                    int inactionTime = behavior.contains("inaction_time") ? behavior.getInt("inaction_time") : behavior.getInt("wander");
                    float forward = behavior.contains("z_axis") ? (float) behavior.getDouble("z_axis") : 0.0F;
                    float clockwise = behavior.contains("x_axis") ? (float) behavior.getDouble("x_axis") : 0.0F;
                    int phase = behavior.contains("set_phase") ? behavior.getInt("set_phase") : -1;
                    behaviorBuilder.behavior(setStrafing(strafingTime, inactionTime, forward, clockwise, phase));
                }
                for (int k = 0; k < conditionList.size(); k++) {
                    CompoundTag condition = conditionList.getCompound(k);
                    CombatBehaviors.BehaviorPredicate<T> predicate = deserializeAdvancedBehaviorPredicate(condition.getString("predicate"), condition);
                    behaviorBuilder.predicate(predicate);
                }
                behaviorSeriesBuilder.nextBehavior(behaviorBuilder);
            }
            builder.newBehaviorSeries(behaviorSeriesBuilder);
        }
        return builder;
    }

    private static <T extends MobPatch<?>> Consumer<T> customAttackAnimation(AdvancedCustomHumanoidMobPatch.CustomAnimationMotion motion, @Nullable AdvancedCustomHumanoidMobPatch.DamageSourceModifier damageSourceModifier, @Nullable List<AnimationEvent.TimeStampedEvent> timeEvents, @Nullable List<AnimationEvent.HitEvent> hitEvents, int phase) {
        return mobpatch -> {
            if (mobpatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch) {
                advancedCustomHumanoidMobPatch.setAttackSpeed(motion.speed());
                advancedCustomHumanoidMobPatch.setStrafingTime(0);
                if (motion.stamina() != 0.0F) {
                    advancedCustomHumanoidMobPatch.setStamina(advancedCustomHumanoidMobPatch.getStamina() - motion.stamina());
                }
                if (timeEvents != null) {
                    for (AnimationEvent.TimeStampedEvent event : timeEvents) {
                        advancedCustomHumanoidMobPatch.addEvent(event);
                    }
                }
                if (hitEvents != null) {
                    for (AnimationEvent.HitEvent event : hitEvents) {
                        advancedCustomHumanoidMobPatch.addEvent(event);
                    }
                }
                advancedCustomHumanoidMobPatch.setDamageSourceModifier(damageSourceModifier);
                if (phase >= 0) {
                    advancedCustomHumanoidMobPatch.setPhase(phase);
                }
            }
            mobpatch.playAnimationSynchronized(motion.animation(), motion.convertTime(), SPPlayAnimation::new);
        };
    }

    public static <T extends MobPatch<?>> Consumer<T> setGuardMotion(int guardTime, StaticAnimation counter, float cost, float chance, float speed, boolean cancel, int phase) {
        return mobpatch -> {
            if (mobpatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch) {
                advancedCustomHumanoidMobPatch.setBlocking(true);
                advancedCustomHumanoidMobPatch.setBlockTick(guardTime);
                advancedCustomHumanoidMobPatch.setCounterMotion(counter, cost, chance, speed);
                advancedCustomHumanoidMobPatch.cancelBlock(cancel);
                if (phase >= 0) {
                    advancedCustomHumanoidMobPatch.setPhase(phase);
                }
            }
        };
    }

    public static <T extends MobPatch<?>> Consumer<T> setStrafing(int strafingTime, int inactionTime, float forward, float clockwise, int phase) {
        return mobpatch -> {
            if (mobpatch instanceof AdvancedCustomHumanoidMobPatch<?> advancedCustomHumanoidMobPatch) {
                advancedCustomHumanoidMobPatch.setStrafingTime(strafingTime);
                advancedCustomHumanoidMobPatch.setInactionTime(inactionTime);
                advancedCustomHumanoidMobPatch.setStrafingDirection(forward, clockwise);
                if (phase >= 0) {
                    advancedCustomHumanoidMobPatch.setPhase(phase);
                }
            }
        };
    }

    private static List<AnimationEvent.TimeStampedEvent> deserializeTimeCommandList(ListTag args) {
        List<AnimationEvent.TimeStampedEvent> list = Lists.newArrayList();
        for (int k = 0; k < args.size(); k++) {
            CompoundTag command = args.getCompound(k);
            AnimationEvent.TimeStampedEvent event = AnimationEvent.TimeStampedEvent.CreateTimeCommandEvent(command.getFloat("time"), command.getString("command"), command.getBoolean("execute_at_target"));
            list.add(event);
        }
        return list;
    }

    private static List<AnimationEvent.HitEvent> deserializeHitCommandList(ListTag args) {
        List<AnimationEvent.HitEvent> list = Lists.newArrayList();
        for (int k = 0; k < args.size(); k++) {
            CompoundTag command = args.getCompound(k);
            AnimationEvent.HitEvent event = AnimationEvent.HitEvent.CreateHitCommandEvent(command.getString("command"), command.getBoolean("execute_at_target"));
            list.add(event);
        }
        return list;
    }

    private static List<AnimationEvent.ConditionalEvent> deserializeStunCommandList(ListTag args) {
        List<AnimationEvent.ConditionalEvent> list = Lists.newArrayList();
        for (int k = 0; k < args.size(); k++) {
            CompoundTag command = args.getCompound(k);
            AnimationEvent.ConditionalEvent event = AnimationEvent.ConditionalEvent.CreateStunCommandEvent(command.getString("command"), StunType.valueOf(command.getString("stun_type").toUpperCase(Locale.ROOT)));
            list.add(event);
        }
        return list;
    }

    private static AdvancedCustomHumanoidMobPatch.DamageSourceModifier deserializeDamageModifier(CompoundTag args) {
        float damage = args.contains("damage") ? args.getFloat("damage") : 1.0F;
        float impact = args.contains("impact") ? args.getFloat("impact") : 1.0F;
        float armor_negation = args.contains("armor_negation") ? args.getFloat("armor_negation") : 1.0F;
        return new AdvancedCustomHumanoidMobPatch.DamageSourceModifier(damage, impact, armor_negation);
    }

    private static <T extends MobPatch<?>> CombatBehaviors.BehaviorPredicate<T> deserializeAdvancedBehaviorPredicate(String type, CompoundTag args) {
        CombatBehaviors.BehaviorPredicate<T> predicate = null;
        List<String[]> loggerNote = Lists.newArrayList();
        switch(type) {
            case "random_chance":
                if (!args.contains("chance", 6)) {
                    loggerNote.add(new String[] { "random_chance", "chance", "double", "0.0" });
                }
                predicate = new CombatBehaviors.RandomChance<>((float) args.getDouble("chance"));
                break;
            case "within_eye_height":
                predicate = new CombatBehaviors.TargetWithinEyeHeight<>();
                break;
            case "within_distance":
                if (!args.contains("min", 6)) {
                    loggerNote.add(new String[] { "within_distance", "min", "double", "0.0" });
                }
                if (!args.contains("max", 6)) {
                    loggerNote.add(new String[] { "within_distance", "max", "double", "0.0" });
                }
                predicate = new CombatBehaviors.TargetWithinDistance<>(args.getDouble("min"), args.getDouble("max"));
                break;
            case "within_angle":
                if (!args.contains("min", 6)) {
                    loggerNote.add(new String[] { "within_angle", "within_distance", "min", "double", "0.0F" });
                }
                if (!args.contains("max", 6)) {
                    loggerNote.add(new String[] { "within_angle", "max", "double", "0.0F" });
                }
                predicate = new CombatBehaviors.TargetWithinAngle<>(args.getDouble("min"), args.getDouble("max"));
                break;
            case "within_angle_horizontal":
                if (!args.contains("min", 6)) {
                    loggerNote.add(new String[] { "within_angle_horizontal", "min", "double", "0.0F" });
                }
                if (!args.contains("max", 6)) {
                    loggerNote.add(new String[] { "within_angle_horizontal", "max", "double", "0.0F" });
                }
                predicate = new CombatBehaviors.TargetWithinAngle.Horizontal<>(args.getDouble("min"), args.getDouble("max"));
                break;
            case "health":
                if (!args.contains("health", 6)) {
                    loggerNote.add(new String[] { "health", "health", "double", "0.0F" });
                }
                if (!args.contains("comparator", 8)) {
                    loggerNote.add(new String[] { "health", "comparator", "string", "" });
                }
                predicate = new CombatBehaviors.Health<>((float) args.getDouble("health"), CombatBehaviors.Health.Comparator.valueOf(args.getString("comparator").toUpperCase(Locale.ROOT)));
                break;
            case "guard_break":
                if (!args.contains("invert")) {
                    loggerNote.add(new String[] { "guard_break", "invert", "boolean", "" });
                }
                predicate = new ExtraPredicate.TargetIsGuardBreak<>(args.getBoolean("invert"));
                break;
            case "knock_down":
                if (!args.contains("invert")) {
                    loggerNote.add(new String[] { "knock_down", "invert", "boolean", "" });
                }
                predicate = new ExtraPredicate.TargetIsKnockDown<>(args.getBoolean("invert"));
                break;
            case "attack_level":
                if (!args.contains("min", 3)) {
                    loggerNote.add(new String[] { "level", "min", "int", "" });
                }
                if (!args.contains("max", 3)) {
                    loggerNote.add(new String[] { "level", "max", "int", "" });
                }
                predicate = new ExtraPredicate.TargetWithinState<>(args.getInt("min"), args.getInt("max"));
                break;
            case "stamina":
                if (!args.contains("stamina", 6)) {
                    loggerNote.add(new String[] { "stamina", "stamina", "double", "0.0F" });
                }
                if (!args.contains("comparator", 8)) {
                    loggerNote.add(new String[] { "stamina", "comparator", "string", "" });
                }
                predicate = new ExtraPredicate.SelfStamina<>((float) args.getDouble("stamina"), CombatBehaviors.Health.Comparator.valueOf(args.getString("comparator").toUpperCase(Locale.ROOT)));
                break;
            case "using_item":
                if (!args.contains("edible")) {
                    loggerNote.add(new String[] { "using_item", "edible", "boolean", "" });
                }
                predicate = new ExtraPredicate.TargetIsUsingItem<>(args.getBoolean("edible"));
                break;
            case "phase":
                if (!args.contains("min", 3)) {
                    loggerNote.add(new String[] { "phase", "min", "int", "" });
                }
                if (!args.contains("max", 3)) {
                    loggerNote.add(new String[] { "phase", "max", "int", "" });
                }
                predicate = new ExtraPredicate.Phase<>(args.getInt("min"), args.getInt("max"));
        }
        for (String[] formatArgs : loggerNote) {
            Indestructible.LOGGER.info(String.format("[Custom Entity Error] can't find a proper argument for %s. [name: %s, type: %s, default: %s]", formatArgs));
        }
        if (predicate == null) {
            throw new IllegalArgumentException("[Custom Entity Error] No predicate type: " + type);
        } else {
            return predicate;
        }
    }

    public static class AdvancedCustomHumanoidMobPatchProvider extends MobPatchReloadListener.AbstractMobPatchProvider {

        protected Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> AHCombatBehaviors;

        protected Map<WeaponCategory, Map<Style, Set<Pair<LivingMotion, StaticAnimation>>>> AHWeaponMotions;

        protected Map<WeaponCategory, Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>> guardMotions;

        protected int regenStaminaStandbyTime;

        protected float regenStaminaMultiply;

        protected boolean hasStunReduction;

        protected float maxStunShield;

        protected int reganShieldStandbyTime;

        protected float reganShieldMultiply;

        protected float staminaLoseMultiply;

        protected float guardRadius;

        protected List<Pair<LivingMotion, StaticAnimation>> defaultAnimations;

        protected Map<StunType, StaticAnimation> stunAnimations;

        protected Map<Attribute, Double> attributeValues;

        protected Faction faction;

        protected double chasingSpeed;

        protected float scale;

        protected float maxStamina;

        protected List<AnimationEvent.ConditionalEvent> stunEvent;

        @Override
        public EntityPatch<?> get(Entity entity) {
            return new AdvancedCustomHumanoidMobPatch(this.faction, this);
        }

        public Map<WeaponCategory, Map<Style, Set<Pair<LivingMotion, StaticAnimation>>>> getHumanoidWeaponMotions() {
            return this.AHWeaponMotions;
        }

        public Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> getHumanoidCombatBehaviors() {
            return this.AHCombatBehaviors;
        }

        public Map<WeaponCategory, Map<Style, Pair<StaticAnimation, Pair<Float, Boolean>>>> getGuardMotions() {
            return this.guardMotions;
        }

        public List<Pair<LivingMotion, StaticAnimation>> getDefaultAnimations() {
            return this.defaultAnimations;
        }

        public Map<StunType, StaticAnimation> getStunAnimations() {
            return this.stunAnimations;
        }

        public Map<Attribute, Double> getAttributeValues() {
            return this.attributeValues;
        }

        public double getChasingSpeed() {
            return this.chasingSpeed;
        }

        public float getScale() {
            return this.scale;
        }

        public float getMaxStamina() {
            return this.maxStamina;
        }

        public int getRegenStaminaStandbyTime() {
            return this.regenStaminaStandbyTime;
        }

        public float getRegenStaminaMultiply() {
            return this.regenStaminaMultiply;
        }

        public boolean hasStunReduction() {
            return this.hasStunReduction;
        }

        public float getMaxStunShield() {
            return this.maxStunShield;
        }

        public int getReganShieldStandbyTime() {
            return this.reganShieldStandbyTime;
        }

        public float getReganShieldMultiply() {
            return this.reganShieldMultiply;
        }

        public float getStaminaLoseMultiply() {
            return this.staminaLoseMultiply;
        }

        public float getGuardRadius() {
            return this.guardRadius;
        }

        public List<AnimationEvent.ConditionalEvent> getStunEvent() {
            return this.stunEvent;
        }
    }
}