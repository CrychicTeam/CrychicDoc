package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.common.requirement.EffectRequirement;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public interface EffectRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder giveEffectOnEnd(String effect, int time, int radius) {
        return this.giveEffectOnEnd(effect, time, radius, 1, new String[0]);
    }

    default RecipeJSBuilder giveEffectOnEnd(String effect, int time, int radius, int level) {
        return this.giveEffectOnEnd(effect, time, radius, level, new String[0]);
    }

    default RecipeJSBuilder giveEffectOnEnd(String effect, int time, int radius, String[] filter) {
        return this.giveEffectOnEnd(effect, time, radius, 1, filter);
    }

    default RecipeJSBuilder giveEffectOnEnd(String effect, int time, int radius, int level, String[] filter) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return ResourceLocation.isValidResourceLocation(effect) && BuiltInRegistries.MOB_EFFECT.containsKey(new ResourceLocation(effect)) ? this.addRequirement(new EffectRequirement(BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(effect)), time, level, radius, entityFilter, true)) : this.error("Invalid effect ID: {}", new Object[] { effect });
    }

    default RecipeJSBuilder giveEffectEachTick(String effect, int time, int radius) {
        return this.giveEffectEachTick(effect, time, radius, 1, new String[0]);
    }

    default RecipeJSBuilder giveEffectEachTick(String effect, int time, int radius, Object levelOrFilter) {
        if (levelOrFilter instanceof Number) {
            return this.giveEffectEachTick(effect, time, radius, ((Number) levelOrFilter).intValue(), new String[0]);
        } else if (levelOrFilter instanceof String) {
            return this.giveEffectEachTick(effect, time, radius, 1, new String[] { (String) levelOrFilter });
        } else {
            return levelOrFilter instanceof String[] ? this.giveEffectEachTick(effect, time, radius, 1, (String[]) levelOrFilter) : this.error("Invalid 4th param given to 'giveEffectEachTick' : {}", new Object[] { levelOrFilter.toString() });
        }
    }

    default RecipeJSBuilder giveEffectEachTick(String effect, int time, int radius, int level, String[] filter) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return ResourceLocation.isValidResourceLocation(effect) && BuiltInRegistries.MOB_EFFECT.containsKey(new ResourceLocation(effect)) ? this.addRequirement(new EffectRequirement(BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation(effect)), time, level, radius, entityFilter, false)) : this.error("Invalid effect ID: {}", new Object[] { effect });
    }
}