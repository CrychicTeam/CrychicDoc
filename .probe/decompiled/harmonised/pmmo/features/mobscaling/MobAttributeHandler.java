package harmonised.pmmo.features.mobscaling;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.config.codecs.LocationData;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.Reference;
import harmonised.pmmo.util.RegistryUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;

@EventBusSubscriber(modid = "pmmo", bus = Bus.FORGE)
public class MobAttributeHandler {

    private static final UUID MODIFIER_ID = UUID.fromString("c95a6e8c-a1c3-4177-9118-1e2cf49b7fcb");

    private static final Map<ResourceLocation, Float> CAPS = Map.of(new ResourceLocation("generic.max_health"), 1024.0F, new ResourceLocation("generic.movement_speed"), 1.5F, new ResourceLocation("generic.attack_damage"), 2048.0F, new ResourceLocation("zombie.spawn_reinforcements"), 1.0F);

    @SubscribeEvent
    public static void onBossAdd(EntityJoinLevelEvent event) {
        if (Config.MOB_SCALING_ENABLED.get()) {
            if (event.getEntity().getType().is(Tags.EntityTypes.BOSSES) && event.getEntity() instanceof LivingEntity entity && event.getLevel() instanceof ServerLevel level) {
                handle(entity, level, new Vec3(entity.m_20185_(), entity.m_20186_(), entity.m_20189_()), level.m_46791_().getId());
            }
        }
    }

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (Config.MOB_SCALING_ENABLED.get()) {
            if (event.getEntity().m_6095_().is(Reference.MOB_TAG)) {
                handle(event.getEntity(), event.getLevel().getLevel(), new Vec3(event.getX(), event.getY(), event.getZ()), event.getLevel().m_46791_().getId());
            }
        }
    }

    private static void handle(LivingEntity entity, ServerLevel level, Vec3 spawnPos, int diffScale) {
        int range = Config.MOB_SCALING_AOE.get();
        TargetingConditions targetCondition = TargetingConditions.forNonCombat().ignoreInvisibilityTesting().ignoreLineOfSight().range(Math.pow((double) range, 2.0) * 3.0);
        List<Player> nearbyPlayers = level.m_45955_(targetCondition, entity, AABB.ofSize(spawnPos, (double) range, (double) range, (double) range));
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "NearbyPlayers on Spawn: " + MsLoggy.listToString(nearbyPlayers));
        Core core = Core.get(level.getLevel());
        LocationData dimData = core.getLoader().DIMENSION_LOADER.getData(level.getLevel().m_46472_().location());
        LocationData bioData = core.getLoader().BIOME_LOADER.getData(RegistryUtil.getId(level.m_204166_(entity.m_20097_())));
        Map<String, Double> dimMods = (Map<String, Double>) dimData.mobModifiers().getOrDefault(RegistryUtil.getId(entity), new HashMap());
        Map<String, Double> bioMods = (Map<String, Double>) bioData.mobModifiers().getOrDefault(RegistryUtil.getId(entity), new HashMap());
        Map<String, Map<String, Double>> multipliers = Config.MOB_SCALING.get();
        float bossMultiplier = entity.m_6095_().is(Tags.EntityTypes.BOSSES) ? Config.BOSS_SCALING_RATIO.get().floatValue() : 1.0F;
        Set<ResourceLocation> attributeKeys = (Set<ResourceLocation>) Stream.of(dimMods.keySet(), bioMods.keySet(), multipliers.keySet()).flatMap(Collection::stream).map(ResourceLocation::new).collect(Collectors.toSet());
        attributeKeys.forEach(attributeID -> {
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(attributeID);
            if (attribute != null) {
                Map<String, Double> config = (Map<String, Double>) multipliers.getOrDefault(attributeID.toString(), new HashMap());
                AttributeInstance attributeInstance = entity.getAttribute(attribute);
                if (attributeInstance != null) {
                    double base = baseValue(entity, attributeID, attributeInstance);
                    float cap = (Float) CAPS.getOrDefault(attributeID, Float.MAX_VALUE);
                    float bonus = getBonus(nearbyPlayers, config, diffScale, base, cap);
                    bonus += ((Double) dimMods.getOrDefault(attributeID.toString(), 0.0)).floatValue();
                    bonus += ((Double) bioMods.getOrDefault(attributeID.toString(), 0.0)).floatValue();
                    bonus *= bossMultiplier;
                    AttributeModifier modifier = new AttributeModifier(MODIFIER_ID, "Boost to Mob Scaling", (double) bonus, AttributeModifier.Operation.ADDITION);
                    attributeInstance.removeModifier(MODIFIER_ID);
                    attributeInstance.addPermanentModifier(modifier);
                    MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Entity={} Attribute={} value={}", entity.m_5446_().getString(), attributeID.toString(), bonus);
                }
            }
        });
        entity.setHealth(entity.getMaxHealth());
    }

    private static double baseValue(LivingEntity entity, ResourceLocation id, AttributeInstance ai) {
        String var3 = id.toString();
        return switch(var3) {
            case "minecraft:generic.attack_damage" ->
                1.0;
            case "minecraft:generic.movement_speed" ->
                (double) entity.getSpeed();
            default ->
                ai.getBaseValue();
        };
    }

    private static float getBonus(List<Player> nearbyPlayers, Map<String, Double> config, int scale, double ogValue, float cap) {
        Map<String, Integer> totalLevel = new HashMap();
        if (nearbyPlayers.isEmpty()) {
            return 0.0F;
        } else {
            nearbyPlayers.forEach(player -> ((Map) config.keySet().stream().collect(Collectors.toMap(str -> str, str -> Core.get(player.m_9236_()).getData().getPlayerSkillLevel(str, player.m_20148_())))).forEach((skill, level) -> totalLevel.merge(skill, level, Integer::sum)));
            float outValue = 0.0F;
            for (Entry<String, Double> configEntry : config.entrySet()) {
                int averageLevel = (Integer) totalLevel.getOrDefault(configEntry.getKey(), 0) / nearbyPlayers.size();
                if (averageLevel >= Config.MOB_SCALING_BASE_LEVEL.get()) {
                    outValue = (float) ((double) outValue + (Config.MOB_USE_EXPONENTIAL_FORMULA.get() ? Math.pow(Config.MOB_EXPONENTIAL_POWER_BASE.get(), Config.MOB_EXPONENTIAL_LEVEL_MOD.get() * (double) (averageLevel - Config.MOB_SCALING_BASE_LEVEL.get())) : (double) (averageLevel - Config.MOB_SCALING_BASE_LEVEL.get()) * Config.MOB_LINEAR_PER_LEVEL.get()));
                    outValue = (float) ((double) outValue * (Double) configEntry.getValue());
                }
            }
            MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, "Modifier Value: " + outValue * (float) scale);
            outValue *= (float) scale;
            return (double) outValue + ogValue > (double) cap ? cap : outValue;
        }
    }
}