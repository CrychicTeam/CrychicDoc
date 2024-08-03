package fr.frinn.custommachinery.common.integration.kubejs.requirements;

import fr.frinn.custommachinery.api.integration.kubejs.RecipeJSBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import fr.frinn.custommachinery.common.requirement.EntityRequirement;
import fr.frinn.custommachinery.common.util.Utils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public interface EntityRequirementJS extends RecipeJSBuilder {

    default RecipeJSBuilder requireEntities(int amount, int radius, String[] filter, boolean whitelist) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return entityFilter.isEmpty() && whitelist ? this.error("Can't use \"requireEntities\" in whitelist mode with an empty filter", new Object[0]) : this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.CHECK_AMOUNT, amount, radius, entityFilter, whitelist));
    }

    default RecipeJSBuilder requireEntitiesHealth(int amount, int radius, String[] filter, boolean whitelist) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return !entityFilter.isEmpty() ? this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.CHECK_HEALTH, amount, radius, entityFilter, whitelist)) : this.error("Can't use \"requireEntitiesHealth\" in whitelist mode with an empty filter", new Object[0]);
    }

    default RecipeJSBuilder consumeEntityHealthOnStart(int amount, int radius, String[] filter, boolean whitelist) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return !entityFilter.isEmpty() ? this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.CONSUME_HEALTH, amount, radius, entityFilter, whitelist)) : this.error("Can't use \"consumeEntityHealthOnStart\" in whitelist mode with an empty filter", new Object[0]);
    }

    default RecipeJSBuilder consumeEntityHealthOnEnd(int amount, int radius, String[] filter, boolean whitelist) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return !entityFilter.isEmpty() ? this.addRequirement(new EntityRequirement(RequirementIOMode.OUTPUT, EntityRequirement.ACTION.CONSUME_HEALTH, amount, radius, entityFilter, whitelist)) : this.error("Can't use \"consumeEntityHealthOnEnd\" in whitelist mode with an empty filter", new Object[0]);
    }

    default RecipeJSBuilder killEntitiesOnStart(int amount, int radius, String[] filter, boolean whitelist) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return !entityFilter.isEmpty() ? this.addRequirement(new EntityRequirement(RequirementIOMode.INPUT, EntityRequirement.ACTION.KILL, amount, radius, entityFilter, whitelist)) : this.error("Can't use \"killEntitiesOnStart\" in whitelist mode with an empty filter", new Object[0]);
    }

    default RecipeJSBuilder killEntitiesOnEnd(int amount, int radius, String[] filter, boolean whitelist) {
        List<EntityType<?>> entityFilter = new ArrayList();
        for (String type : filter) {
            if (!Utils.isResourceNameValid(type) || !BuiltInRegistries.ENTITY_TYPE.m_7804_(new ResourceLocation(type))) {
                return this.error("Invalid entity ID: {}", new Object[] { type });
            }
            entityFilter.add(BuiltInRegistries.ENTITY_TYPE.get(new ResourceLocation(type)));
        }
        return !entityFilter.isEmpty() ? this.addRequirement(new EntityRequirement(RequirementIOMode.OUTPUT, EntityRequirement.ACTION.KILL, amount, radius, entityFilter, whitelist)) : this.error("Can't use \"killEntitiesOnEnd\" in whitelist mode with an empty filter", new Object[0]);
    }
}