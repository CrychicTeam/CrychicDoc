package harmonised.pmmo.features.autovalues;

import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ReqType;
import harmonised.pmmo.util.Reference;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraftforge.registries.ForgeRegistries;

public class AutoEntity {

    public static final EventType[] EVENTTYPES = new EventType[] { EventType.BREED, EventType.DEATH, EventType.ENTITY, EventType.RIDING, EventType.SHIELD_BLOCK, EventType.TAMING };

    public static Map<String, Integer> processReqs(ReqType type, ResourceLocation entityID) {
        return new HashMap();
    }

    public static Map<String, Long> processXpGains(EventType type, ResourceLocation entityID) {
        if (!type.entityApplicable) {
            return new HashMap();
        } else {
            Map<String, Long> outMap = new HashMap();
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(entityID);
            switch(type) {
                case RIDING:
                    if (entityType.is(Reference.RIDEABLE_TAG)) {
                        outMap.putAll(getXpMap(entityID, type));
                    }
                    break;
                case DEATH:
                case ENTITY:
                case SHIELD_BLOCK:
                    outMap.putAll(getXpMap(entityID, type));
                    break;
                case BREED:
                    if (entityType.is(Reference.BREEDABLE_TAG)) {
                        outMap.putAll(getXpMap(entityID, type));
                    }
                    break;
                case TAMING:
                    if (entityType.is(Reference.TAMABLE_TAG)) {
                        outMap.putAll(getXpMap(entityID, type));
                    }
            }
            return outMap;
        }
    }

    private static Map<String, Long> getXpMap(ResourceLocation entityID, EventType type) {
        EntityType<? extends LivingEntity> entity = (EntityType<? extends LivingEntity>) ForgeRegistries.ENTITY_TYPES.getValue(entityID);
        Map<String, Long> outMap = new HashMap();
        double healthScale = getAttribute(entity, Attributes.MAX_HEALTH) * (Double) AutoValueConfig.ENTITY_ATTRIBUTES.get().getOrDefault(AutoValueConfig.AttributeKey.HEALTH.key, 0.0);
        double speedScale = getAttribute(entity, Attributes.MOVEMENT_SPEED) * (Double) AutoValueConfig.ENTITY_ATTRIBUTES.get().getOrDefault(AutoValueConfig.AttributeKey.SPEED.key, 0.0);
        double damageScale = getAttribute(entity, Attributes.ATTACK_DAMAGE) * (Double) AutoValueConfig.ENTITY_ATTRIBUTES.get().getOrDefault(AutoValueConfig.AttributeKey.DMG.key, 0.0);
        double scale = healthScale + speedScale + damageScale;
        AutoValueConfig.getEntityXpAward(type).forEach((skill, value) -> outMap.put(skill, Double.valueOf((double) value.longValue() * scale).longValue()));
        return outMap;
    }

    private static double getAttribute(EntityType<? extends LivingEntity> entity, Attribute attribute) {
        if (!DefaultAttributes.hasSupplier(entity)) {
            return 0.0;
        } else {
            AttributeSupplier attSup = DefaultAttributes.getSupplier(entity);
            if (attSup == null) {
                return 0.0;
            } else {
                return attSup.hasAttribute(attribute) ? attSup.getBaseValue(attribute) : 0.0;
            }
        }
    }
}